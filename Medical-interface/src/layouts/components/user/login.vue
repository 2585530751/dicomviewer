<script setup lang="ts">
import { computed, reactive, ref, watch, type Ref, onMounted } from 'vue'
import type { FormInstance } from 'element-plus'
import { useUserStoreHook } from '@/store/modules/user'
import { message } from '@/utils/message'
import { loginRules, registerRules, forgetRules } from '@/composables/user/rule'
import { forgetPassword } from '@/api/user'
import { isLoggedIn } from '@/utils/auth'
import securityCode from '@/layouts/components/user/securityCode.vue'
import { departmentOptions } from '@/utils/commonVariables'
import { resetForm } from '@/utils/commonUtils'

const options = departmentOptions
defineOptions({
  name: ''
})

const props = defineProps<{
  loginWindowOpen?: boolean
}>()

const emits = defineEmits<{
  loginWindowClose: [] // 具名元组语法
}>()

let centerDialogVisible = props.loginWindowOpen

const dialog = reactive({
  dialogWidth: 350,
  dialogTitle: '登录'
})

watch(
  () => {
    return props.loginWindowOpen
  },
  (value, prevValue) => {
    centerDialogVisible = value
  }
)

const ruleFormRef = ref<FormInstance>()
const loading = ref(false)
const ruleForm = reactive({
  account: 'admin',
  password: 'admin123'
})
const onLogin = async (formEl: FormInstance | undefined) => {
  loading.value = true
  if (!formEl) return
  await formEl.validate((valid, fields) => {
    if (valid) {
      useUserStoreHook()
        .loginByUsername({ account: ruleForm.account, password: ruleForm.password })
        .then((res) => {
          if (res.success) {
            //获取后端路由
            message(res.msg, { type: 'success' })
            emits('loginWindowClose')
            location.reload()
            isLoggedIn.value = true
          } else {
            message(res.msg, { type: 'error' })
          }
        })
        .catch((err) => {
          message(err, { type: 'error' })
        })
        .finally(() => {
          loading.value = false
        })
    } else {
      Object.keys(fields as object).forEach((key, i) => {
        const propName = fields![key][0].field
        const propMessage = fields![key][0].message as string
        if (i == 0) {
          formEl.resetFields(propName)
          message(propMessage, { type: 'error' })
        }
      })
      loading.value = false
    }
  })
}

const registerRuleFormRef = ref<FormInstance>()
const registerRuleForm = reactive({
  account: '',
  password: '',
  checkPassword: '',
  // email: '',
  phoneNumber: '',
  permissionName: [],
  verificationCode: '',
  inputCode: ''
})
const onRegister = async (formEl: FormInstance | undefined) => {
  loading.value = true
  if (!formEl) return
  await formEl.validate((valid, fields) => {
    if (valid) {
      useUserStoreHook()
        .register({
          account: registerRuleForm.account,
          password: registerRuleForm.password,
          phoneNumber: registerRuleForm.phoneNumber,
          permissionName:
            registerRuleForm.permissionName[registerRuleForm.permissionName.length - 1]
        })
        .then((res) => {
          if (res.success) {
            //获取后端路由
            message('注册成功！请重新登录！', { type: 'success' })
            emits('loginWindowClose')
            resetForm(registerRuleFormRef.value)
          } else {
            message(res.msg, { type: 'error' })
          }
        })
        .catch((err) => {
          message(err, { type: 'error' })
        })
        .finally(() => {
          loading.value = false
        })
    } else {
      Object.keys(fields as object).forEach((key, i) => {
        const propName = fields![key][0].field
        const propMessage = fields![key][0].message as string
        if (i == 0) {
          formEl.resetFields(propName)
          message(propMessage, { type: 'error' })
        }
      })
      loading.value = false
    }
  })
}

const forgetRuleFormRef = ref<FormInstance>()
const forgetRuleForm = reactive({
  account: '',
  phoneNumber: '',
  password: '',
  checkPassword: '',
  verificationCode: '',
  inputCode: ''
})
const onForgetPassword = async (formEl: FormInstance | undefined) => {
  loading.value = true
  if (!formEl) return
  await formEl.validate((valid, fields) => {
    if (valid) {
      forgetPassword({
        account: forgetRuleForm.account,
        password: forgetRuleForm.password,
        phoneNumber: forgetRuleForm.phoneNumber
      })
        .then((res) => {
          if (res.success) {
            //获取后端路由
            message('修改成功！', { type: 'success' })
            dialog.dialogTitle = '登录'
            Object.keys(forgetRuleForm).forEach((key) => {
              forgetRuleForm[key as keyof typeof forgetRuleForm] = ''
            })
          } else {
            message(res.msg, { type: 'error' })
          }
        })
        .catch((err) => {
          message(err, { type: 'error' })
        })
        .finally(() => {
          loading.value = false
        })
    } else {
      Object.keys(fields as object).forEach((key, i) => {
        const propName = fields![key][0].field
        const propMessage = fields![key][0].message as string
        if (i == 0) {
          formEl.resetFields(propName)
          message(propMessage, { type: 'error' })
        }
      })
      loading.value = false
    }
  })
}

const validateCheckPass = (rule: any, value: any, callback: any) => {
  if (value === '') {
    callback(new Error('请确认密码！'))
  } else if (value !== registerRuleForm.password) {
    callback(new Error('两次密码不相同！'))
  } else {
    callback()
  }
}

const validateCheckPass1 = (rule: any, value: any, callback: any) => {
  if (value === '') {
    callback(new Error('请确认密码！'))
  } else if (value !== forgetRuleForm.password) {
    callback(new Error('两次密码不相同！'))
  } else {
    callback()
  }
}

onMounted(() => {
  makeCode(4)
})

//验证码功能
const isFetching = ref(false)
const timer: Ref<NodeJS.Timer | null> = ref(null)
const countdown = ref(60)

async function fetchVerificationCode() {
  if (isFetching.value) return // 如果正在获取验证码，则直接返回
  isFetching.value = true // 标记为正在获取验证码
  forgetRuleForm.verificationCode = generateRandomCode().toString() // Convert the generated random code to a string
  registerRuleForm.verificationCode = forgetRuleForm.verificationCode // Convert the generated random code to a string
  // 启动倒计时
  startCountdown()
}

function generateRandomCode() {
  // 生成一个六位数的随机验证码
  return Math.floor(100000 + Math.random() * 900000)
}

function startCountdown() {
  // 清除之前的计时器（如果有的话）
  if (timer.value) {
    clearInterval(timer.value)
  }

  timer.value = setInterval(() => {
    if (countdown.value <= 0) {
      if (timer.value) {
        clearInterval(timer.value) // 清除计时器
      }
      countdown.value = 60 // 重置倒计时
      isFetching.value = false // 允许重新获取验证码
      forgetRuleForm.verificationCode = '' // 清空验证码
    } else {
      countdown.value-- // 更新倒计时
    }
  }, 1000)
}

//图像验证码
const identifyCode = ref('')

function refreshCode() {
  forgetRuleForm.inputCode = ''
  registerRuleForm.inputCode = ''
  identifyCode.value = '' //输入框置空
  makeCode(4) //验证码长度为4
}

function makeCode(length: number) {
  let code = ''
  for (let i = 0; i < length; i++) {
    const r = Math.floor(Math.random() * 36)
    if (r < 10) {
      code += r
    } else {
      code += String.fromCharCode(r - 10 + 65)
    }
  }
  identifyCode.value = code
  localStorage.setItem('code', code)
}
</script>

<template>
  <el-dialog
    v-model="centerDialogVisible"
    @close="$emit('loginWindowClose')"
    :title="dialog.dialogTitle"
    :width="dialog.dialogWidth"
    center
  >
    <div v-show="dialog.dialogTitle === '登录'">
      <el-form ref="ruleFormRef" :model="ruleForm" :rules="loginRules" size="large">
        <el-form-item prop="account">
          <el-input clearable v-model="ruleForm.account" placeholder="账号" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input clearable show-password v-model="ruleForm.password" placeholder="密码" />
        </el-form-item>
      </el-form>
      <div class="flex justify-between py-3">
        <div
          class="text-blue-300 cursor-pointer hover:text-red-400"
          @click="dialog.dialogTitle = '忘记密码'"
        >
          忘记密码
        </div>
        <div
          class="text-blue-300 cursor-pointer hover:text-red-400"
          @click="dialog.dialogTitle = '注册'"
        >
          注册
        </div>
      </div>
      <el-button
        class="w-full"
        size="large"
        type="primary"
        :loading="loading"
        @click="onLogin(ruleFormRef)"
      >
        登录
      </el-button>
    </div>
    <div v-show="dialog.dialogTitle === '注册'">
      <el-form
        ref="registerRuleFormRef"
        :model="registerRuleForm"
        :rules="registerRules"
        size="large"
      >
        <el-form-item prop="account">
          <el-input clearable v-model="registerRuleForm.account" placeholder="请输入账号" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            clearable
            show-password
            v-model="registerRuleForm.password"
            placeholder="密码"
          />
        </el-form-item>
        <el-form-item
          :rules="[
            {
              required: true,
              validator: validateCheckPass,
              trigger: 'blur'
            }
          ]"
          prop="checkPassword"
        >
          <el-input
            clearable
            v-model="registerRuleForm.checkPassword"
            show-password
            placeholder="确定密码"
          />
        </el-form-item>
        <!-- <el-form-item prop="email">
          <el-input clearable v-model="registerRuleForm.email" placeholder="邮箱" />
        </el-form-item> -->
        <el-form-item prop="phoneNumber">
          <el-input clearable v-model="registerRuleForm.phoneNumber" placeholder="电话" />
        </el-form-item>
        <el-form-item prop="verificationCode">
          <el-input clearable v-model="registerRuleForm.verificationCode" placeholder="手机验证码">
            <template #append>
              <el-button
                :size="isFetching ? 'small' : 'default'"
                :type="isFetching ? 'success' : 'primary'"
                :disabled="isFetching"
                @click="fetchVerificationCode"
              >
                {{ isFetching ? countdown + 's后重发' : '获取验证码' }}
              </el-button>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="inputCode">
          <el-input
            clearable
            v-model="registerRuleForm.inputCode"
            autocomplete="off"
            placeholder="验证码"
          >
            <template #append>
              <securityCode :identifyCode="identifyCode" @click="refreshCode()"></securityCode>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="身份：">
          <el-cascader
            :options="options"
            :show-all-levels="false"
            v-model="registerRuleForm.permissionName"
          />
        </el-form-item>
      </el-form>
      <div class="flex justify-between py-3">
        <div
          class="text-blue-300 cursor-pointer hover:text-red-400"
          @click="dialog.dialogTitle = '登录'"
        >
          已有账号，立即登录
        </div>
        <div class="text-green-600 hover:text-red-400">注册即同意注册协议</div>
      </div>
      <el-button
        class="w-full"
        size="large"
        type="primary"
        :loading="loading"
        @click="onRegister(registerRuleFormRef)"
      >
        注册
      </el-button>
    </div>
    <div v-show="dialog.dialogTitle === '忘记密码'">
      <el-form ref="forgetRuleFormRef" :model="forgetRuleForm" :rules="forgetRules" size="large">
        <el-form-item prop="account">
          <el-input clearable v-model="forgetRuleForm.account" placeholder="账号" />
        </el-form-item>
        <el-form-item prop="phoneNumber">
          <el-input clearable v-model="forgetRuleForm.phoneNumber" placeholder="手机号" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input clearable show-password v-model="forgetRuleForm.password" placeholder="密码" />
        </el-form-item>
        <el-form-item
          :rules="[
            {
              required: true,
              validator: validateCheckPass1,
              trigger: 'blur'
            }
          ]"
          prop="checkPassword"
        >
          <el-input
            clearable
            v-model="forgetRuleForm.checkPassword"
            show-password
            placeholder="确定密码"
          />
        </el-form-item>
        <el-form-item prop="verificationCode">
          <el-input clearable v-model="forgetRuleForm.verificationCode" placeholder="手机验证码">
            <template #append>
              <el-button
                :size="isFetching ? 'small' : 'default'"
                :type="isFetching ? 'success' : 'primary'"
                :disabled="isFetching"
                @click="fetchVerificationCode"
              >
                {{ isFetching ? countdown + 's后重发' : '获取验证码' }}
              </el-button>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="inputCode">
          <el-input
            clearable
            v-model="forgetRuleForm.inputCode"
            autocomplete="off"
            placeholder="验证码"
          >
            <template #append>
              <securityCode
                :identifyCode="identifyCode"
                canvasId="forgetCanvas"
                @click="refreshCode()"
              ></securityCode>
            </template>
          </el-input>
        </el-form-item>
      </el-form>
      <div class="flex justify-between py-3">
        <div
          class="text-blue-300 cursor-pointer hover:text-red-400"
          @click="dialog.dialogTitle = '登录'"
        >
          登录
        </div>
        <div
          class="text-blue-300 cursor-pointer hover:text-red-400"
          @click="dialog.dialogTitle = '注册'"
        >
          注册
        </div>
      </div>
      <el-button
        class="w-full"
        size="large"
        type="primary"
        :loading="loading"
        @click="onForgetPassword(forgetRuleFormRef)"
      >
        修改密码
      </el-button>
    </div>
  </el-dialog>
</template>

<style lang="scss" scoped></style>
