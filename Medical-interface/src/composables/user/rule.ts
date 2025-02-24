import { reactive } from 'vue'
import type { FormRules } from 'element-plus'
import { isAccountExisted } from '@/api/user'

/** 密码正则（密码格式应为8-18位数字、字母、符号的任意两种组合） */
export const REGEXP_PWD =
  /^(?![0-9]+$)(?![a-z]+$)(?![A-Z]+$)(?!([^(0-9a-zA-Z)]|[()])+$)(?!^.*[\u4E00-\u9FA5].*$)([^(0-9a-zA-Z)]|[()]|[a-z]|[A-Z]|[0-9]){8,18}$/

/** 登录校验 */
const loginRules = reactive(<FormRules>{
  password: [
    {
      validator: (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请输入密码'))
        } else if (!REGEXP_PWD.test(value)) {
          callback(new Error('密码格式应为8-18位数字、字母、符号的任意两种组合'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ],
  account: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 4, max: 16, message: '用户名长度在 4 到 16 个字符', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (!REGEXP_USERNAME.test(value)) {
          callback(new Error('用户名只能包含字母、数字和下划线'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
})

// 假设你已经定义了一些正则表达式用于校验
const REGEXP_USERNAME = /^[a-zA-Z0-9_]{4,16}$/ // 用户名规则示例
const REGEXP_EMAIL = /^[^\s@]+@[^\s@]+\.[^\s@]+$/ // 邮箱规则示例
const REGEXP_PHONE_NUMBER = /^1[3456789]\d{9}$/ // 中国大陆手机号码规则示例
export const REGEXP_ID_CARD = /^[1-9]\d{5}(18|19|20)\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/ // 身份证号码规则示例

/** 注册校验 */
const registerRules = reactive(<FormRules>{
  account: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 4, max: 16, message: '用户名长度在 4 到 16 个字符', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (!REGEXP_USERNAME.test(value)) {
          callback(new Error('用户名只能包含字母、数字和下划线'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    },
    {
      validator: (rule, value, callback) => {
        isAccountExisted({ account: value })
          .then((data) => {
            if(data.success){
              callback()
            }else{
              callback(new Error('用户名已存在'))
            }
          })
          .catch((error) => {
            callback(new Error('网络延迟，请稍后再试！'))
          })
      },
      trigger: 'blur'
    }
  ],
  email: [
    { required: true, message: '请输入邮箱地址', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (!REGEXP_EMAIL.test(value)) {
          callback(new Error('请输入正确的邮箱地址'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 8, max: 18, message: '密码长度在 8 到 18 个字符', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (!REGEXP_PWD.test(value)) {
          callback(new Error('密码格式应为8-18位数字、字母、符号的任意两种组合'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ],
  phoneNumber: [
    { required: true, message: '请输入手机号码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (!REGEXP_PHONE_NUMBER.test(value)) {
          callback(new Error('请输入正确的手机号码'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ],
  inputCode: [
    { required: true, message: '请输入验证码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value.length !== 4) {
          callback(new Error('验证码长度为4位'))
        } else if (value.toLowerCase() !== localStorage.getItem('code')?.toLowerCase()) {
          callback(new Error('验证码错误，请重新输入'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
})

/** 忘记密码校验 */
const forgetRules = reactive(<FormRules>{
  account: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 4, max: 16, message: '用户名长度在 4 到 16 个字符', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (!REGEXP_USERNAME.test(value)) {
          callback(new Error('用户名只能包含字母、数字和下划线'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ],
  password: [
    {
      validator: (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请输入密码'))
        } else if (!REGEXP_PWD.test(value)) {
          callback(new Error('密码格式应为8-18位数字、字母、符号的任意两种组合'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ],
  inputCode: [
    { required: true, message: '请输入验证码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value.length !== 4) {
          callback(new Error('验证码长度为4位'))
        } else if (value.toLowerCase() !== localStorage.getItem('code')?.toLowerCase()) {
          callback(new Error('验证码错误，请重新输入'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
})

export { loginRules, registerRules, forgetRules }
