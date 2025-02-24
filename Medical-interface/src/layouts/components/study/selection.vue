<script setup lang="ts">
import { ref, reactive, onBeforeMount } from 'vue'
import type { IconifyIconOffline } from '@/components/ReIcon'
import plus from '@iconify-icons/ep/circle-plus'
import search from '@iconify-icons/ep/search'
import fold from '@iconify-icons/ep/fold'
import refresh from '@iconify-icons/ep/refresh'
import setUp from '@iconify-icons/ep/set-up'
import rank from '@iconify-icons/ep/rank'
import { setAllPropertiesToNull, exportExcel } from '@/utils/commonUtils'
import applicationExport from '@/assets/svg/MdiApplicationExport.svg?component'
import applicationImport from '@/assets/svg/MdiApplicationImport.svg?component'
import { useStudyStateStore } from '@/store/modules/studyState'


defineOptions({
  name: 'studySelection'
})

const emits = defineEmits<{
  changeTableSize: [size: string]
  changeTableCols: [cols: string[]]
}>()

const studyStateStore = useStudyStateStore()

const filterSelect = ref('studyDate')
const filterSearch = ref('')

const visible = ref(false)

const studyForm: Record<string, any> = reactive({
  studyDate: null,
  studyTime: null,
  patientAge: null,
  accessionNumber: null,
  bodyPartExamined: null,
  studyDescription: null,
  dateOfStudyBegin: null,
  dateOfStudyEnd: null,
  patientAgeBegin: null,
  patientAgeEnd: null
})

const checkAll = ref(false)
const isIndeterminate = ref(true)
const checkedCols = ref([
  'studyDate',
  'studyTime',
  'patientAge',
  'accessionNumber',
  'bodyPartExamined',
  'studyDescription'
])

onBeforeMount(() => {
  emits('changeTableCols', checkedCols.value)
})

const cols = [
  { label: '创建时间', prop: 'createTime' },
  { label: '检查日期', prop: 'studyDate' },
  { label: '检查时间', prop: 'studyTime' },
  { label: '患者年龄', prop: 'patientAge' },
  { label: '检查次序', prop: 'accessionNumber' },
  { label: '检查部位', prop: 'bodyPartExamined' },
  { label: '检查描述', prop: 'studyDescription' }
]

const handleCheckAllChange = (val: boolean) => {
  checkedCols.value = val ? cols.map((item) => item.prop) : []
  isIndeterminate.value = false
  emits('changeTableCols', checkedCols.value)
}

const handleCheckedColsChange = (value: string[]) => {
  const checkedCount = value.length
  checkAll.value = checkedCount === cols.length
  isIndeterminate.value = checkedCount > 0 && checkedCount < cols.length
  emits('changeTableCols', value)
}

function singleConditionSearch() {
  setAllPropertiesToNull(studyStateStore.studyFilterCriteria)
  studyStateStore.studyFilterCriteria[filterSelect.value] = filterSearch.value
  studyStateStore.studyPagination.currentPage = 1
  studyStateStore.getStudyListPage()
}

function conditionalFilter() {
  setAllPropertiesToNull(studyStateStore.studyFilterCriteria)
  for (const key in studyForm) {
    if (studyForm.hasOwnProperty(key) && studyForm[key] !== null) {
      // 确保属性是对象自身的，而不是原型链上的
      studyStateStore.studyFilterCriteria[key] = studyForm[key] // 将属性和值复制到目标对象
    }
  }
  studyStateStore.studyPagination.currentPage = 1
  studyStateStore.getStudyListPage()
}

function refreshTable() {
  setAllPropertiesToNull(studyStateStore.studyFilterCriteria)
  studyStateStore.studyPatientId = null
  studyStateStore.studyPagination.currentPage = 1
  studyStateStore.getStudyListPage()
}
</script>

<template>
  <el-card class="box-card">
    <template #header>
      <div class="flex items-center justify-between">
        <div class="flex flex-wrap items-center gap-4 w-30 h-14">
          <a class="text-2xl font-medium tracking-wide text-gray-600">检查列表</a>
        </div>

        <div class="flex flex-wrap items-center gap-5 w-30 h-14">
          <el-input style="width: 500px" placeholder="请输入查询的数据" v-model="filterSearch">
            <template #prepend>
              <el-select placeholder="选择" style="width: 100px" v-model="filterSelect">
                <el-option label="检查日期" value="studyDate" />
                <el-option label="检查时间" value="studyTime" />
                <el-option label="患者年龄" value="patientAge" />
                <el-option label="检查次序" value="accessionNumber" />
                <el-option label="检查部位" value="bodyPartExamined" />
                <el-option label="检查描述" value="studyDescription" />
              </el-select>
            </template>
            <template #append>
              <el-button round @click="singleConditionSearch">
                <template #icon>
                  <IconifyIconOffline :icon="search"></IconifyIconOffline>
                </template>
              </el-button>
            </template>
          </el-input>
          <el-button round @click="visible = true">
            <template #icon>
              <IconifyIconOffline :icon="fold"></IconifyIconOffline>
            </template>
            高级筛选</el-button
          >
        </div>
      </div>
    </template>
    <div class="flex items-center justify-between">
      <div class="flex flex-wrap w-auto h-auto">
        <el-button
          round
          :icon="applicationExport"
          @click="
            exportExcel(
              JSON.parse(JSON.stringify(studyStateStore.studyListTableData)),
              cols,
              checkedCols,
              '检查列表.xlsx'
            )
          "
          >导出检查</el-button
        >
      </div>
      <div class="flex flex-wrap items-center w-auto h-auto gap-x-3">
        <el-tooltip content="刷新" placement="top" effect="light">
          <IconifyIconOffline
            @click="refreshTable()"
            :icon="refresh"
            class="hover:text-blue-500"
            :style="{ fontSize: '24px' }"
          ></IconifyIconOffline>
        </el-tooltip>
        <el-tooltip content="密度" placement="top" effect="light">
          <el-dropdown trigger="click">
            <IconifyIconOffline
              class="hover:text-blue-500"
              :icon="rank"
              :style="{ fontSize: '24px' }"
            ></IconifyIconOffline>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="$emit('changeTableSize', 'small')">紧凑</el-dropdown-item>
                <el-dropdown-item @click="$emit('changeTableSize', 'default')"
                  >默认</el-dropdown-item
                >
                <el-dropdown-item @click="$emit('changeTableSize', 'large')">宽松</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </el-tooltip>
        <el-tooltip content="列设置" placement="top" effect="light">
          <el-dropdown trigger="click" placement="bottom-end" :hide-on-click="false">
            <IconifyIconOffline
              class="hover:text-blue-500"
              :icon="setUp"
              :style="{ fontSize: '24px' }"
            ></IconifyIconOffline>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item>
                  <el-checkbox
                    v-model="checkAll"
                    :indeterminate="isIndeterminate"
                    @change="handleCheckAllChange"
                    >列展示</el-checkbox
                  >
                </el-dropdown-item>
                <el-dropdown-item :divided="true"></el-dropdown-item>
                <el-checkbox-group v-model="checkedCols" @change="handleCheckedColsChange">
                  <el-dropdown-item v-for="(col, index) in cols" :key="index">
                    <el-checkbox :key="col.prop" :value="col.prop">{{ col.label }}</el-checkbox>
                  </el-dropdown-item>
                </el-checkbox-group>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </el-tooltip>
      </div>
    </div>
  </el-card>
  <el-drawer v-model="visible" title="高级筛选">
    <el-form :model="studyForm" label-width="120px">
      <el-form-item label="检查日期">
        <el-col :span="11">
          <el-date-picker
            v-model="studyForm.dateOfStudyBegin"
            type="date"
            placeholder="请选择日期范围"
            style="width: 100%"
          />
        </el-col>
        <el-col :span="2" class="text-center">
          <span class="text-gray-500">-</span>
        </el-col>
        <el-col :span="11">
          <el-date-picker
            v-model="studyForm.dateOfStudyEnd"
            placeholder="请选择日期范围"
            style="width: 100%"
          />
        </el-col>
      </el-form-item>
      <el-form-item label="检查时间">
        <el-input v-model="studyForm.studyTimebegin" />
      </el-form-item>
      <el-form-item label="患者年龄">
        <el-col :span="11">
          <el-input v-model="studyForm.patientAgeBegin" />
        </el-col>
        <el-col :span="2" class="text-center">
          <span class="text-gray-500">-</span>
        </el-col>
        <el-col :span="11">
          <el-input v-model="studyForm.patientAgeEnd" />
        </el-col>
      </el-form-item>
      <el-form-item label="检查次序">
        <el-input v-model="studyForm.accessionNumber" />
      </el-form-item>
      <el-form-item label="检查部位">
        <el-input v-model="studyForm.bodyPartExamined" />
      </el-form-item>
      <el-form-item label="检查描述">
        <el-input v-model="studyForm.studyDescription" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="conditionalFilter">查询</el-button>
        <el-button @click="setAllPropertiesToNull(studyForm)">清空</el-button>
      </el-form-item>
    </el-form>
  </el-drawer>
</template>

<style lang="scss" scoped></style>
