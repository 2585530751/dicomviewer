<script setup lang="ts">
import { reactive, type Ref, ref, watch, onMounted } from 'vue'
import type { StudyEntity } from '@/types/study'
import { dayjs, type FormInstance, type FormRules } from 'element-plus'
import { getPermissionByCurrentUserIdApi } from '@/api/user'
import type { PermissionEntity } from '@/types/user'
import { message } from '@/utils/message'
import { Warning } from '@element-plus/icons-vue'
import { resetForm } from '@/utils/commonUtils'
import { REGEXP_ID_CARD } from '@/composables/user/rule'
import { addOneStudyApi } from '@/api/study'
import { usePatientStateStore } from '@/store/modules/patientState'

defineOptions({
  name: ''
})
const patientStateStore = usePatientStateStore()
const props = defineProps<{
  uploadWindowOpen?: boolean
  dialogPatientId: number
}>()

const emits = defineEmits<{
  uploadWindowClose: [] // 具名元组语法
}>()

let centerDialogVisible = ref(props.uploadWindowOpen)

interface StudyForm extends StudyEntity {
  // 这里可以添加额外的属性，比如departmentIdList
  departmentIdList: number[]
}

type StudyRequired = {
  study: StudyEntity
  departmentIdList: number[]
}

const studyInfo: StudyForm = reactive({
  accessionNumber: null,
  bodyPartExamined: null,
  patientAge: null,
  patientId: props.dialogPatientId,
  studyDate: null, // 假设这是一个日期字符串
  studyDescription: null,
  studyId: null,
  studyTime: null, // 假设这也是一个日期时间字符串
  departmentIdList: []
})

const studyFormRef = ref<FormInstance>()

const departmentOptions: Ref<PermissionEntity[]> = ref([])

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

async function addOneStudy() {
  if (!studyFormRef) return
  await studyFormRef.value!.validate((valid, fields) => {
    if (valid) {
      const studyRequired: StudyRequired = {
        study: studyInfo,
        departmentIdList: studyInfo.departmentIdList
      }
      console.log(studyRequired)
      const studyRequiredJson = JSON.parse(JSON.stringify(studyRequired))
      studyRequiredJson.study.patientId = props.dialogPatientId
      if (studyInfo.studyDate != '' && studyInfo.studyDate != null)
        studyRequiredJson.study.studyDate = dayjs(studyInfo.studyDate).format('YYYY-MM-DD')
      if (studyInfo.studyTime != '' && studyInfo.studyTime != null)
        studyRequiredJson.study.studyTime = dayjs(studyInfo.studyTime).format('HH:mm:ss')
      console.log(studyRequiredJson)
      addOneStudyApi(studyRequiredJson)
        .then((res) => {
          if (res.success) {
            message('添加成功', { type: 'success' })
            resetForm(studyFormRef.value!)
            patientStateStore.getPatientListPage()
            emits('uploadWindowClose')
          } else {
            message(res.msg, { type: 'error' })
          }
        })
        .catch((error) => {
          message(error, { type: 'error' })
        })
    } else {
      message('请检查表单', { type: 'error' })
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
      message: '请输入检查号',
      trigger: 'blur'
    }
  ],
  departmentIdList: [
    {
      required: true,
      message: '请选择科室',
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
    <el-form :model="studyInfo" label-position="top" :rules="rules" ref="studyFormRef">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="检查号" prop="accessionNumber">
            <el-input v-model="studyInfo.accessionNumber" placeholder="请输入检查号" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="检查部位" prop="bodyPartExamined">
            <el-input v-model="studyInfo.bodyPartExamined" placeholder="请输入检查部位" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="检查日期" prop="studyDate">
            <el-date-picker
              v-model="studyInfo.studyDate"
              type="date"
              placeholder="请输入检查日期"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="检查时间" prop="studyTime">
            <el-time-picker v-model="studyInfo.studyTime" placeholder="请输入检查时间" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="患者年龄" prop="patientAge">
            <el-input v-model="studyInfo.patientAge" placeholder="请输入患者年龄" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="科室" prop="departmentIdList">
            <el-select
              v-model="studyInfo.departmentIdList"
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
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="检查描述" prop="studyDescription">
            <el-input
              v-model="studyInfo.studyDescription"
              style="width: 100%"
              :autosize="{ minRows: 5, maxRows: 20 }"
              type="textarea"
              placeholder="请输入检查详情描述"
            />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <template #footer>
      <el-button @click="resetForm(studyFormRef)">重置</el-button>
      <el-button type="primary" @click="addOneStudy()"> 保存 </el-button>
    </template>
  </el-dialog>
</template>

<style lang="scss" scoped></style>
