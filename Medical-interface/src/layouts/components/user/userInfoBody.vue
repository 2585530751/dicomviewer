<script setup lang="ts">
import userInfoBasic from '@/layouts/components/user/userInfoBasic.vue'
import {
  Document,
  Menu as IconMenu,
  Location,
  Setting,
  User,
  Postcard
} from '@element-plus/icons-vue'
import { onMounted, reactive, shallowRef } from 'vue'
import userInfoRealNameVerificationVue from '@/layouts/components/user/userInfoRealNameVerification.vue'
import userInfoSecurity from '@/layouts/components/user/userInfoSecurity.vue'
import userInfoMapVue from '@/layouts/components/user/userInfoMap.vue'
import userInfoRecord from '@/layouts/components/user/userInfoRecord.vue'
import type { UserInfo } from '@/types/user'
import { getUserInformationApi } from '@/api/user'

var currentComp = shallowRef(userInfoBasic)
var userInformation = reactive<UserInfo>({
  userId: 0,
  account: '',
  phoneNumber: '',
  createTime: '',
  birthOfDate: '',
  gender: '',
  place: '',
  idCard: '',
  name: ''
})

function changeCurrentComp(comp: any): void {
  currentComp.value = comp
}

function getUserInformation() {
  //将个人信息界面中用户信息显示到表格中

  getUserInformationApi().then((data) => {
    if (data.success == true) {
      userInformation = reactive(data.data)
    }
  })
}

getUserInformation()
</script>

<template>
  <el-card>
    <el-row>
      <el-col :span="4"
        ><el-menu>
          <el-menu-item index="1" @click="changeCurrentComp(userInfoBasic)">
            <el-icon><user /></el-icon>
            <span>我的信息</span>
          </el-menu-item>
          <el-menu-item index="2" @click="changeCurrentComp(userInfoMapVue)">
            <el-icon><location /></el-icon>
            <span>地址管理</span>
          </el-menu-item>

          <el-menu-item index="3" @click="changeCurrentComp(userInfoSecurity)">
            <el-icon><document /></el-icon>
            <span>账号安全</span>
          </el-menu-item>
          <el-menu-item index="4" @click="changeCurrentComp(userInfoRealNameVerificationVue)">
            <el-icon><postcard /></el-icon>
            <span>实名认证</span>
          </el-menu-item>
          <el-menu-item index="6" @click="changeCurrentComp(userInfoRecord)">
            <el-icon><icon-menu /></el-icon>
            <span>我的记录</span>
          </el-menu-item>
          <el-menu-item index="5" disabled>
            <el-icon><setting /></el-icon>
            <span>通用设置</span>
          </el-menu-item>
        </el-menu></el-col
      >
      <el-col :span="20">
        <component :is="currentComp" :user-info="userInformation" />
      </el-col>
    </el-row>
  </el-card>
</template>

<style lang="scss" scoped></style>
