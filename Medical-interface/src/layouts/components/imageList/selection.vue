<script setup lang="ts">
import { ref, reactive } from 'vue'
import type { IconifyIconOffline } from '@/components/ReIcon'
import plus from '@iconify-icons/ep/circle-plus'
import search from '@iconify-icons/ep/search'
import fold from '@iconify-icons/ep/fold'
import refresh from '@iconify-icons/ep/refresh'
import setUp from '@iconify-icons/ep/set-up'
import rank from '@iconify-icons/ep/rank'
import imageUpload from './imageUpload.vue'

defineOptions({
  name: 'imageSelection'
})
const emit = defineEmits<{
  changeTableSize: [size: string]
}>()

const centerDialogVisible = ref(false)

const formInline = reactive({
  user: '',
  region: '',
  date: ''
})
const tableSize = ref('small')
const onSubmit = () => {
  console.log('submit!')
}
const visible = ref(false)

// do not use same name with ref
const form = reactive({
  patientName: '',
  ageBegin: '',
  ageEnd: '',
  gender: '',
  dateBegin: '',
  dateEnd: '',
  contact: '',
  address: '',
  healthStatus: '',
  reasonForVisit: '',
  diagnosis: ''
})

const checkAll = ref(false)
const isIndeterminate = ref(true)
const checkedCols = ref([
  'name',
  'gender',
  'age',
  'contact',
  'address',
  'healthStatus',
  'reasonForVisit',
  'date',
  'diagnosis'
])
const cols = [
  { label: '姓名', prop: 'name' },
  { label: '性别', prop: 'gender' },
  { label: '年龄', prop: 'age' },
  { label: '联系方式', prop: 'contact' },
  { label: '住址', prop: 'address' },
  { label: '健康状况', prop: 'healthStatus' },
  { label: '就诊原因', prop: 'reasonForVisit' },
  { label: '就诊日期', prop: 'date' },
  { label: '诊断结果', prop: 'diagnosis' }
]

const handleCheckAllChange = (val: boolean) => {
  checkedCols.value = val ? cols.map((item) => item.prop) : []
  isIndeterminate.value = false
}

const handleCheckedColsChange = (value: string[]) => {
  const checkedCount = value.length
  checkAll.value = checkedCount === cols.length
  isIndeterminate.value = checkedCount > 0 && checkedCount < cols.length
}
</script>

<template>
  <el-card class="box-card">
    <template #header>
      <div class="flex items-center justify-between">
        <div class="flex flex-wrap items-center gap-4 w-30 h-14">
          <a class="text-2xl font-medium tracking-wide text-gray-600">图像列表</a>
        </div>

        <div class="flex flex-wrap items-center w-30 h-14">
          <el-form
            :inline="true"
            :model="formInline"
            class="demo-form-inline"
            style="margin-top: 1.125rem"
          >
            <el-form-item>
              <el-input
                style="width: 500px"
                placeholder="请输入查询的数据"
                class="input-with-select"
              >
                <template #prepend>
                  <el-select placeholder="选择" style="width: 100px">
                    <el-option label="姓名" value="patientName" />
                    <el-option label="联系方式" value="contact" />
                    <el-option label="住址" value="address" />
                  </el-select>
                </template>
                <template #append>
                  <el-button round @click="onSubmit">
                    <template #icon>
                      <IconifyIconOffline :icon="search"></IconifyIconOffline>
                    </template>
                  </el-button>
                </template>
              </el-input>
            </el-form-item>
          </el-form>
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
        <el-button round @click="centerDialogVisible = true">
          <template #icon>
            <IconifyIconOffline :icon="plus"></IconifyIconOffline>
          </template>
          上传图像</el-button
        >
      </div>
      <div class="flex flex-wrap items-center w-auto h-auto gap-x-3">
        <el-tooltip content="刷新" placement="top" effect="light">
          <IconifyIconOffline
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
    <el-form :model="form" label-width="120px">
      <el-form-item label="姓名">
        <el-input v-model="form.patientName" />
      </el-form-item>
      <el-form-item label="年龄">
        <el-col :span="11">
          <el-select v-model="form.ageBegin" placeholder="选择年龄范围" style="width: 100%" />
        </el-col>
        <el-col :span="2" class="text-center">
          <span class="text-gray-500">-</span>
        </el-col>
        <el-col :span="11">
          <el-select v-model="form.ageEnd" placeholder="选择年龄范围" style="width: 100%" />
        </el-col>
      </el-form-item>
      <el-form-item label="性别">
        <el-radio-group v-model="form.gender">
          <el-radio label="1" border>男性</el-radio>
          <el-radio label="2" border>女性</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="住址">
        <el-input v-model="form.address" />
      </el-form-item>
      <el-form-item label="就诊日期">
        <el-col :span="11">
          <el-date-picker
            v-model="form.dateBegin"
            type="date"
            placeholder="请选择日期范围"
            style="width: 100%"
          />
        </el-col>
        <el-col :span="2" class="text-center">
          <span class="text-gray-500">-</span>
        </el-col>
        <el-col :span="11">
          <el-date-picker v-model="form.dateEnd" placeholder="请选择日期范围" style="width: 100%" />
        </el-col>
      </el-form-item>
      <el-form-item label="联系方式">
        <el-input v-model="form.contact" />
      </el-form-item>
      <el-form-item label="健康状况">
        <el-input v-model="form.healthStatus" />
      </el-form-item>
      <el-form-item label="就诊原因">
        <el-input v-model="form.reasonForVisit" />
      </el-form-item>
      <el-form-item label="诊断结果">
        <el-input v-model="form.diagnosis" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit">查询</el-button>
        <el-button>清空</el-button>
      </el-form-item>
    </el-form>
  </el-drawer>
  <imageUpload
    :upload-window-open="centerDialogVisible"
    @upload-window-close="centerDialogVisible = false"
  ></imageUpload>
</template>

<style lang="scss" scoped></style>
