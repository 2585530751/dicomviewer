<script setup lang="ts">
import { computed, reactive, ref, watch, type Ref, onMounted } from 'vue'
import type { FormInstance } from 'element-plus'
import { useUserStoreHook } from '@/store/modules/user'
import { message } from '@/utils/message'
import { forgetRules } from '@/composables/user/rule'
import { forgetPassword } from '@/api/user'
import { isLoggedIn } from '@/utils/auth'
import securityCode from '@/layouts/components/user/securityCode.vue'
import type { UserInfo } from '@/types/user'

const props = defineProps<{
  userInfo: UserInfo
}>()
const loading = ref(false)
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
const validateCheckPass1 = (rule: any, value: any, callback: any) => {
  if (value === '') {
    callback(new Error('请确认密码！'))
  } else if (value !== forgetRuleForm.password) {
    callback(new Error('两次密码不相同！'))
  } else {
    callback()
  }
}

//验证码功能
const isFetching = ref(false)
const timer: Ref<NodeJS.Timer | null> = ref(null)
const countdown = ref(60)

async function fetchVerificationCode() {
  if (isFetching.value) return // 如果正在获取验证码，则直接返回
  isFetching.value = true // 标记为正在获取验证码
  forgetRuleForm.verificationCode = generateRandomCode().toString() // Convert the generated random code to a string
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

onMounted(() => {
  makeCode(4)
  forgetRuleForm.account = props.userInfo.account
  forgetRuleForm.phoneNumber = props.userInfo.phoneNumber
})
</script>

<template>
  <div class="flex justify-center h-full">
    <el-card style="max-width: 500px" class="self-center w-1/2">
      <div>
        <el-form ref="forgetRuleFormRef" :model="forgetRuleForm" :rules="forgetRules" size="large">
          <el-form-item prop="account">
            <el-input clearable v-model="forgetRuleForm.account" placeholder="账号" disabled />
          </el-form-item>
          <el-form-item prop="phoneNumber">
            <el-input
              clearable
              v-model="forgetRuleForm.phoneNumber"
              placeholder="手机号"
              disabled
            />
          </el-form-item>
          <el-form-item prop="password">
            <el-input
              clearable
              show-password
              v-model="forgetRuleForm.password"
              placeholder="新密码"
            />
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
    </el-card>
  </div>
</template>

<style lang="scss" scoped></style>
