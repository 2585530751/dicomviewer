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
import { useSeriesStateStore } from '@/store/modules/seriesState'
import seriesUpload from '@/layouts/components/series/seriesUpload.vue'
import rolePermission from '@/components/rolePermission.vue'

defineOptions({
  name: 'seriesSelection'
})

const emits = defineEmits<{
  changeTableSize: [size: string]
  changeTableCols: [cols: string[]]
}>()

const centerDialogVisible = ref(false)

const seriesStateStore = useSeriesStateStore()

const filterSelect = ref('seriesName')
const filterSearch = ref('')

const visible = ref(false)

const seriesForm: Record<string, any> = reactive({
  seriesName: null,
  seriesFormat: null,
  seriesCount: null,
  patientName: null,
  seriesDate: null,
  seriesTime: null,
  seriesDesc: null,
  seriesDateBegin: null,
  seriesDateEnd: null
})

const checkAll = ref(false)
const isIndeterminate = ref(true)
const checkedCols = ref([
  'seriesName',
  'seriesFormat',
  'seriesCount',
  'patientName',
  'seriesDate',
  'seriesTime',
  'seriesDesc'
])

onBeforeMount(() => {
  emits('changeTableCols', checkedCols.value)
})

const cols = [
  { label: '创建时间', prop: 'createTime' },
  { label: '序列名称', prop: 'seriesName' },
  { label: '序列格式', prop: 'seriesFormat' },
  { label: '图像数量', prop: 'seriesCount' },
  { label: '患者姓名', prop: 'patientName' },
  { label: '序列日期', prop: 'seriesDate' },
  { label: '序列时间', prop: 'seriesTime' },
  { label: '序列描述', prop: 'seriesDesc' }
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
  setAllPropertiesToNull(seriesStateStore.seriesFilterCriteria)
  seriesStateStore.seriesFilterCriteria[filterSelect.value] = filterSearch.value
  seriesStateStore.seriesPagination.currentPage = 1
  seriesStateStore.getSeriesListPage()
}

function conditionalFilter() {
  setAllPropertiesToNull(seriesStateStore.seriesFilterCriteria)
  for (const key in seriesForm) {
    if (seriesForm.hasOwnProperty(key) && seriesForm[key] !== null) {
      // 确保属性是对象自身的，而不是原型链上的
      seriesStateStore.seriesFilterCriteria[key] = seriesForm[key] // 将属性和值复制到目标对象
    }
  }
  seriesStateStore.seriesPagination.currentPage = 1
  seriesStateStore.getSeriesListPage()
}

function refreshTable() {
  setAllPropertiesToNull(seriesStateStore.seriesFilterCriteria)
  seriesStateStore.seriesPatientId = null
  seriesStateStore.seriesStudyId = null
  seriesStateStore.seriesPagination.currentPage = 1
  seriesStateStore.getSeriesListPage()
}
</script>

<template>
  <el-card class="box-card">
    <template #header>
      <div class="flex items-center justify-between">
        <div class="flex flex-wrap items-center gap-4 w-30 h-14">
          <a class="text-2xl font-medium tracking-wide text-gray-600">序列列表</a>
        </div>

        <div class="flex flex-wrap items-center gap-5 w-30 h-14">
          <el-input style="width: 500px" placeholder="请输入查询的数据" v-model="filterSearch">
            <template #prepend>
              <el-select placeholder="选择" style="width: 100px" v-model="filterSelect">
                <el-option label="序列名称" value="seriesName" />
                <el-option label="序列格式" value="seriesFormat" />
                <el-option label="图像数量" value="seriesCount" />
                <el-option label="患者姓名" value="patientName" />
                <el-option label="序列日期" value="seriesDate" />
                <el-option label="序列时间" value="seriesTime" />
                <el-option label="序列描述" value="seriesDesc" />
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
      <div class="flex flex-wrap w-auto h-auto gap-1">
        <role-permission :value="['doctor']"
          ><el-button round @click="centerDialogVisible = true">
            <template #icon>
              <IconifyIconOffline :icon="plus"></IconifyIconOffline>
            </template>
            导入DICOM</el-button
          ></role-permission
        >
        <el-button
          round
          :icon="applicationExport"
          @click="
            exportExcel(
              JSON.parse(JSON.stringify(seriesStateStore.seriesListTableData)),
              cols,
              checkedCols,
              '序列列表.xlsx'
            )
          "
          >导出序列</el-button
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
    <el-form :model="seriesForm" label-width="120px">
      <el-form-item label="序列名称">
        <el-input v-model="seriesForm.seriesName" />
      </el-form-item>
      <el-form-item label="序列日期">
        <el-col :span="11">
          <el-date-picker
            v-model="seriesForm.seriesDateBegin"
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
            v-model="seriesForm.seriesDateEnd"
            placeholder="请选择日期范围"
            style="width: 100%"
          />
        </el-col>
      </el-form-item>
      <el-form-item label="序列格式">
        <el-input v-model="seriesForm.seriesFormat" />
      </el-form-item>
      <el-form-item label="图像数量">
        <el-input v-model="seriesForm.seriesCount" />
      </el-form-item>
      <el-form-item label="患者姓名">
        <el-input v-model="seriesForm.patientName" />
      </el-form-item>
      <el-form-item label="序列时间">
        <el-input v-model="seriesForm.seriesTime" />
      </el-form-item>
      <el-form-item label="序列描述">
        <el-input v-model="seriesForm.seriesDesc" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="conditionalFilter">查询</el-button>
        <el-button @click="setAllPropertiesToNull(seriesForm)">清空</el-button>
      </el-form-item>
    </el-form>
  </el-drawer>
  <series-upload
    :upload-window-open="centerDialogVisible"
    @upload-window-close="centerDialogVisible = false"
  ></series-upload>
</template>

<style lang="scss" scoped></style>
