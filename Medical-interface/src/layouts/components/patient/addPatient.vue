<script setup lang="ts">
import { reactive, type Ref, ref, watch, onMounted } from 'vue'
import type { PatientEntity } from '@/types/patient'
import type { FormInstance, FormRules } from 'element-plus'
import { getPermissionByCurrentUserIdApi } from '@/api/user'
import type { PermissionEntity } from '@/types/user'
import { message } from '@/utils/message'
import { Warning } from '@element-plus/icons-vue'
import { resetForm } from '@/utils/commonUtils'
import { REGEXP_ID_CARD } from '@/composables/user/rule'
import { addOnePatientApi } from '@/api/patient'
import { usePatientStateStore } from '@/store/modules/patientState'

defineOptions({
  name: ''
})
const patientStateStore = usePatientStateStore()
const props = defineProps<{
  uploadWindowOpen?: boolean
}>()

const emits = defineEmits<{
  uploadWindowClose: [] // 具名元组语法
}>()

let centerDialogVisible = ref(props.uploadWindowOpen)

interface PatientForm extends PatientEntity {  
  // 这里可以添加额外的属性，比如departmentIdList  
  departmentIdList: number[];  
}  

type PatientRequired = {
  patient: PatientEntity,
  departmentIdList: number[]
}

const patientInfo: PatientForm = reactive({
  patientName: null,
  patientIdCardNumber: null,
  patientGender: null, // 可以考虑使用枚举或其他方式来限制可能的值
  dateOfBirth: null, // 同上，通常会是 Date 类型
  patientHeight: null,
  patientWeight: null,
  address: null,
  email: null,
  phoneNumber: null,
  departmentIdList: []
})

const patientFormRef = ref<FormInstance>()

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

async function addOnePatient() {
  if (!patientFormRef) return
  await patientFormRef.value!.validate((valid, fields) => {
    if (valid) {
      const PatientRequired: PatientRequired = {
        patient: patientInfo,
        departmentIdList: patientInfo.departmentIdList
      }
      addOnePatientApi(PatientRequired)
        .then((res) => {
          if (res.success) {
            message('添加成功', { type: 'success' })
            resetForm(patientFormRef.value!)
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
  patientName: [{ required: true, message: '请输入患者姓名', trigger: 'blur' }],
  patientIdCardNumber: [
    { required: true, message: '请输入身份证号', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (!REGEXP_ID_CARD.test(value)) {
          callback(new Error('身份证格式不正确！'))
        } else {
          callback()
        }
      },
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
    <el-form :model="patientInfo" label-position="top" :rules="rules" ref="patientFormRef">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="患者姓名" prop="patientName">
            <el-input v-model="patientInfo.patientName" placeholder="请输入患者姓名" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="身份证号" prop="patientIdCardNumber">
            <el-input v-model="patientInfo.patientIdCardNumber" placeholder="请输入患者身份证号" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="患者性别" prop="patientGender">
            <el-radio-group v-model="patientInfo.patientGender">
              <el-radio label="男性" value="男" />
              <el-radio label="女性" value="女" />
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="生日日期" prop="dateOfBirth">
            <el-date-picker
              v-model="patientInfo.dateOfBirth"
              type="date"
              placeholder="请输入患者生日"
              clearable
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="患者体重(kg)" prop="patientWeight">
            <el-input v-model="patientInfo.patientWeight" placeholder="请输入患者体重" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="患者身高(cm)" prop="patientHeight">
            <el-input v-model="patientInfo.patientHeight" placeholder="请输入患者身高" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="患者地址" prop="address">
            <el-input v-model="patientInfo.address" placeholder="请输入患者地址" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="患者邮箱" prop="email">
            <el-input v-model="patientInfo.email" placeholder="请输入患者邮箱" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="科室" prop="departmentIdList">
            <el-select
              v-model="patientInfo.departmentIdList"
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
          <el-form-item label="手机号码" prop="phoneNumber">
            <el-input v-model="patientInfo.phoneNumber" placeholder="请输入手机号码" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <template #footer>
      <el-button @click="resetForm(patientFormRef)">重置</el-button>
      <el-button type="primary" @click="addOnePatient()"> 保存 </el-button>
    </template>
  </el-dialog>
</template>

<style lang="scss" scoped></style>
