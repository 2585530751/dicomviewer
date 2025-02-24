<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import {
  provinceAndCityDataPlus,
  provinceAndCityData,
  convertTextToCode,
  regionDataPlus,
  regionData,
  CodeToText
} from '@/utils/chinaArea'
import { getUserInformationApi, postUserInformationApi } from '@/api/user'
import { message } from '@/utils/message'
import { REGEXP_ID_CARD } from '@/composables/user/rule'

interface userRuleForm {
  name: string
  account: string
  email: string
  address: string
  phoneNumber: string
  userName: string
  birthOfDate: string
  gender: string
  userHeight: number | null
  userWeight: number | null
  place: string[]
  idCard: string
}

const formSize = ref('default')
const userRuleFormRef = ref<FormInstance>()
const userRuleForm = reactive<userRuleForm>({
  name: '',
  account: '',
  email: '',
  address: '',
  phoneNumber: '',
  userName: '',
  birthOfDate: '',
  gender: '',
  userHeight: 0,
  userWeight: 0,
  place: ['', '', ''],
  idCard: ''
})

function getUserInformation() {
  //将个人信息界面中用户信息显示到表格中

  getUserInformationApi().then((data) => {
    if (data.success == true) {
      userRuleForm.address = data.data.address
      userRuleForm.birthOfDate = data.data.birthOfDate
      userRuleForm.email = data.data.email
      userRuleForm.gender = data.data.gender
      userRuleForm.idCard = data.data.idCard
      userRuleForm.name = data.data.name
      userRuleForm.phoneNumber = data.data.phoneNumber
      const placeStr = data.data.place
      if (placeStr != undefined) {
        userRuleForm.place[0] = placeStr.substring(0, 2) + '0000'
        if (placeStr.substring(0, 2) == '82' || placeStr.substring(0, 2) == '81') {
          userRuleForm.place[1] = placeStr
          userRuleForm.place.splice(2)
        } else {
          userRuleForm.place[1] = placeStr.substring(0, 4) + '00'
          userRuleForm.place[2] = placeStr
        }
      }

      userRuleForm.userHeight = data.data.userHeight
      userRuleForm.userName = data.data.userName
      userRuleForm.userWeight = data.data.userWeight
      userRuleForm.account = data.data.account
    }
  })
}

onMounted(() => {
  getUserInformation()
})

const updateUserInformation = async (formEl: FormInstance | undefined) => {
  //将个人信息界面中用户信息保存进数据库
  const updatePlace = userRuleForm.place.length == 3 ? userRuleForm.place[2] : userRuleForm.place[1]
  postUserInformationApi({
    account: userRuleForm.account,
    name: userRuleForm.name,
    email: userRuleForm.email,
    phoneNumber: userRuleForm.phoneNumber,
    place: updatePlace,
    address: userRuleForm.address,
    birthOfDate: userRuleForm.birthOfDate==undefined?'':new Date(userRuleForm.birthOfDate),
    gender: userRuleForm.gender,
    userName: userRuleForm.userName,
    idCard: userRuleForm.idCard,
    userHeight: userRuleForm.userHeight,
    userWeight: userRuleForm.userWeight
  }).then((data) => {
    if (data.success == true) {
      message(data.msg, { type: 'success' })
    } else {
      message(data.msg, { type: 'error' })
    }
  })
}

const rules = reactive<FormRules>({
  idCard: [
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
  ]
})
</script>

<template>
  <div class="flex flex-col">
    <div class="flex justify-between ml-3">
      <el-text size="large">基本信息</el-text>
      <el-button type="default" @click="updateUserInformation">保存</el-button>
    </div>
    <div class="m-2">
      <el-form
        ref="userRuleFormRef"
        :model="userRuleForm"
        label-width="auto"
        class="w-full"
        :size="formSize"
        :rules="rules"
        :inline="true"
        status-icon
      >
        <el-form-item label="账号" prop="account">
          <el-input v-model="userRuleForm.account" class="w-96" disabled />
        </el-form-item>
        <el-form-item label="名称" prop="userName">
          <el-input v-model="userRuleForm.userName" class="w-96" />
        </el-form-item>
        <br />
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="userRuleForm.email" class="w-96" />
        </el-form-item>
        <el-form-item label="手机" prop="phoneNumber">
          <el-input v-model="userRuleForm.phoneNumber" class="w-96" />
        </el-form-item>
        <el-form-item label="籍贯" prop="place">
          <el-cascader v-model="userRuleForm.place" :options="regionData" class="w-96" />
        </el-form-item>
        <el-form-item label="住址" prop="address">
          <el-input v-model="userRuleForm.address" class="w-96" />
        </el-form-item>
        <el-form-item label="生日" prop="birthOfDate">
          <el-date-picker
            v-model="userRuleForm.birthOfDate"
            type="date"
            aria-label="选择日期"
            placeholder="选择日期"
          />
        </el-form-item>
        <el-form-item label="性别">
          <el-radio-group v-model="userRuleForm.gender">
            <el-radio value ="男性" border>男性</el-radio>
            <el-radio value ="女性" border>女性</el-radio>
          </el-radio-group>
        </el-form-item>
        <br />
        <el-form-item label="真名" prop="name">
          <el-input v-model="userRuleForm.name" class="w-96" />
        </el-form-item>
        <el-form-item label="身份证号" prop="idCard">
          <el-input v-model="userRuleForm.idCard" class="w-96" />
        </el-form-item>
        <el-form-item label="身高" prop="userHeight">
          <el-input v-model="userRuleForm.userHeight" class="w-96" />
        </el-form-item>
        <el-form-item label="体重" prop="userWeight">
          <el-input v-model="userRuleForm.userWeight" class="w-96" />
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<style lang="scss" scoped></style>
