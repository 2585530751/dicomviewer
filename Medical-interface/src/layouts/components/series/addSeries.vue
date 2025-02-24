<script setup lang="ts">
import { reactive, type Ref, ref, watch, onMounted } from 'vue'
import type { SeriesEntity } from '@/types/series'
import { dayjs, type FormInstance, type FormRules } from 'element-plus'
import { getPermissionByCurrentUserIdApi } from '@/api/user'
import type { PermissionEntity } from '@/types/user'
import { message } from '@/utils/message'
import { Warning } from '@element-plus/icons-vue'
import { resetForm } from '@/utils/commonUtils'
import { addOneSeriesApi } from '@/api/series'
import { useStudyStateStore } from '@/store/modules/studyState'

defineOptions({
  name: ''
})
const studyStateStore = useStudyStateStore()
const props = defineProps<{
  uploadWindowOpen?: boolean
  dialogPatientId: number
  dialogStudyId: number
}>()

const emits = defineEmits<{
  uploadWindowClose: [] // 具名元组语法
}>()

let centerDialogVisible = ref(props.uploadWindowOpen)

interface SeriesForm extends SeriesEntity {
  // 这里可以添加额外的属性，比如departmentIdList
  departmentIdList: number[]
}

type SeriesRequired = {
  series: SeriesEntity
  departmentIdList: number[]
}

const seriesInfo: SeriesForm = reactive({
  patientId: props.dialogPatientId,
  seriesDate: null, // 假设这是一个日期字符串
  seriesDesc: null,
  studyId: props.dialogStudyId,
  seriesTime: null, // 假设这也是一个日期时间字符串
  seriesModality: null,
  departmentIdList: []
})

const seriesFormRef = ref<FormInstance>()

const departmentOptions: Ref<PermissionEntity[]> = ref([])
const seriesModalityOptions: Ref<PermissionEntity[]> = ref([
  {
    permissionId: 72,
    permissionName: 'CT',
    description: 'CT'
  },
  {
    permissionId: 73,
    permissionName: 'MRI',
    description: 'MRI'
  }
])

watch(
  () => {
    return props.uploadWindowOpen
  },
  (value, prevValue) => {
    centerDialogVisible.value = value
  }
)

function obtainDepartmentList() {
  getPermissionByCurrentUserIdApi()
    .then((res) => {
      if (res.success) {
        departmentOptions.value = res.data as PermissionEntity[]
      } else {
        message(res.msg, { type: 'error' })
      }
    })
    .catch((error) => {
      message(error, { type: 'error' })
    })
}

async function addOneSeries() {
  if (!seriesFormRef) return
  await seriesFormRef.value!.validate((valid, fields) => {
    if (valid) {
      const seriesRequired: SeriesRequired = {
        series: seriesInfo,
        departmentIdList: seriesInfo.departmentIdList
      }
      console.log(seriesRequired)
      const seriesRequiredJson = JSON.parse(JSON.stringify(seriesRequired))
      seriesRequiredJson.series.patientId = props.dialogPatientId
      seriesRequiredJson.series.studyId = props.dialogStudyId
      if (seriesInfo.seriesDate != '' && seriesInfo.seriesDate != null)
        seriesRequiredJson.series.seriesDate = dayjs(seriesInfo.seriesDate).format('YYYY-MM-DD')
      if (seriesInfo.seriesTime != '' && seriesInfo.seriesTime != null)
        seriesRequiredJson.series.seriesTime = dayjs(seriesInfo.seriesTime).format('HH:mm:ss')
      console.log(seriesRequiredJson)
      addOneSeriesApi(seriesRequiredJson)
        .then((res) => {
          if (res.success) {
            message('添加成功', { type: 'success' })
            resetForm(seriesFormRef.value!)
            studyStateStore.getStudyListPage()
            emits('uploadWindowClose')
          } else {
            message(res.msg, { type: 'error' })
          }
        })
        .catch((error) => {
          message(error, { type: 'error' })
        })
    } else {
      message('请序列表单', { type: 'error' })
    }
  })
}

onMounted(() => {
  obtainDepartmentList()
})

const rules = reactive<FormRules>({
  accessionNumber: [
    {
      required: true,
      message: '请输入序列号',
      trigger: 'blur'
    }
  ],
  departmentIdList: [
    {
      required: true,
      message: '请选择科室',
      trigger: 'change'
    }
  ],
  seriesModality:[
    {
      required: true,
      message: '请选择序列类型',
      trigger: 'change'
    }
  
  ]
})
</script>

<template>
  <el-dialog
    v-model="centerDialogVisible"
    @close="$emit('uploadWindowClose')"
    title="新增患者"
    width="60%"
    center
  >
    <el-form :model="seriesInfo" label-position="top" :rules="rules" ref="seriesFormRef">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="序列名称" prop="seriesName">
            <el-input v-model="seriesInfo.seriesName" placeholder="请输入序列名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="序列部位" prop="seriesCheckPart">
            <el-input v-model="seriesInfo.seriesCheckPart" placeholder="请输入序列部位" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="序列日期" prop="seriesDate">
            <el-date-picker
              v-model="seriesInfo.seriesDate"
              type="date"
              placeholder="请输入序列日期"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="序列时间" prop="seriesTime">
            <el-time-picker v-model="seriesInfo.seriesTime" placeholder="请输入序列时间" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="科室" prop="departmentIdList">
            <el-select
              v-model="seriesInfo.departmentIdList"
              multiple
              collapse-tags
              collapse-tags-tooltip
              :max-collapse-tags="3"
              placeholder="选择科室"
              class="w-2/3"
            >
              <el-option
                v-for="item in departmentOptions"
                :key="item.permissionId"
                :label="item.description"
                :value="item.permissionId"
              >
                <span style="float: left">{{ item.description }}</span>
                <span
                  class="pl-6"
                  style="float: right; color: var(--el-text-color-secondary); font-size: 13px"
                >
                  {{ item.permissionName }}
                </span>
              </el-option>
            </el-select>
            <el-tooltip content="选择的科室都有权限查看此内容！" placement="bottom">
              <el-icon class="pl-2" color="red" size="16"><Warning /></el-icon>
            </el-tooltip>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="序列类型" prop="seriesModality">
            <el-select
              v-model="seriesInfo.seriesModality"
              collapse-tags
              collapse-tags-tooltip
              :max-collapse-tags="3"
              placeholder="选择序列类型"
              class="w-2/3"
            >
              <el-option
                v-for="item in seriesModalityOptions"
                :key="item.permissionId"
                :label="item.description"
                :value="item.permissionName"
              >
                <span style="float: left">{{ item.description }}</span>
                <span
                  class="pl-6"
                  style="float: right; color: var(--el-text-color-secondary); font-size: 13px"
                >
                  {{ item.permissionName }}
                </span>
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="序列描述" prop="seriesDesc">
            <el-input
              v-model="seriesInfo.seriesDesc"
              style="width: 100%"
              :autosize="{ minRows: 5, maxRows: 20 }"
              type="textarea"
              placeholder="请输入序列详情描述"
            />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <template #footer>
      <el-button @click="resetForm(seriesFormRef)">重置</el-button>
      <el-button type="primary" @click="addOneSeries()"> 保存 </el-button>
    </template>
  </el-dialog>
</template>

<style lang="scss" scoped></style>
