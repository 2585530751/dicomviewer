<script setup lang="ts">
import { ref, onBeforeMount } from 'vue'

import dayIcon from '@/assets/svg/day.svg?component'
import darkIcon from '@/assets/svg/dark.svg?component'
import { isDark } from '@/composables'
import login from './user/login.vue'
import rolePermission from '@/components/rolePermission.vue'
import { isLoggedIn, checkAuthStatus } from '@/utils/auth'
import { useUserStoreHook } from '@/store/modules/user'
import { getHeadIconApi } from '@/api/user'
import { basicImageUrl } from '@/api/utils'

const activeIndex = ref('')
const imageUrl = ref('')

const handleSelect = (key: string, keyPath: string[]) => {
  console.log(key, keyPath)
}

const centerDialogVisible = ref(false)
onBeforeMount(() => {
  checkAuthStatus()
  if (isLoggedIn.value == true) {
    getHeadIcon()
  }
})

function getHeadIcon() {
  getHeadIconApi().then((data) => {
    imageUrl.value = basicImageUrl + data.msg
  })
}

const errorHandler = () => {
  imageUrl.value = '/images/emptyAvatar.jpg'
}
</script>

<template>
  <el-menu
    :default-active="activeIndex"
    mode="horizontal"
    :ellipsis="false"
    menu-trigger="hover"
    :unique-opened="true"
    :router="true"
    @select="handleSelect"
    ><el-menu-item index="/">
      <template #title>
        <div class="flex items-center py-2 dark:hidden">
          <img src="@/assets/images/KDLWord1.png" class="h-8" />
          <img src="@/assets/images/KDLLogo1.png" class="h-4 pr-5" />
          <img src="@/assets/images/ncuLogo.png" class="h-10" />
        </div>
        <div class="items-center hidden py-2 dark:visible dark:flex">
          <img src="@/assets/images/KDLWord.png" class="h-8" />
          <img src="@/assets/images/KDLLogo.png" class="h-4 pr-5" />
          <img src="@/assets/images/ncuLogo1.png" class="h-10" />
        </div>
      </template>
    </el-menu-item>

    <div class="flex-grow" />
    <!-- <role-permission :value="['doctor', 'radiologist']">
      <el-menu-item index="/tools">工具箱</el-menu-item>
    </role-permission> -->
    <role-permission :value="['doctor']"
      ><el-menu-item index="/model">算法模型</el-menu-item></role-permission
    >
    <role-permission :value="['doctor', 'radiologist']">
      <el-menu-item index="/patient">患者管理</el-menu-item></role-permission
    >

    <role-permission :value="['radiologist', 'doctor']">
      <el-menu-item index="/study">检查管理</el-menu-item></role-permission
    >
    <role-permission :value="['radiologist', 'doctor']">
      <el-menu-item index="/series">序列管理</el-menu-item>
    </role-permission>
    <role-permission :value="['patient']">
      <el-menu-item index="/study/myPatientStudy">我的检查</el-menu-item>
    </role-permission>
    <role-permission :value="[]">
      <el-menu-item index="/image">图像管理</el-menu-item>
    </role-permission>
    <el-sub-menu index="2">
      <template #title>设置</template>
      <el-menu-item index="/setting/aboutUs">关于我们</el-menu-item>
      <el-menu-item index="/setting">帮助文档</el-menu-item>
      <el-menu-item index="/setting/problemFeedback">问题反馈</el-menu-item>
    </el-sub-menu>

    <div style="display: flex" class="mx-5">
      <el-switch
        style="margin: auto"
        v-model="isDark"
        inline-prompt
        :active-icon="dayIcon"
        :inactive-icon="darkIcon"
      />
    </div>

    <el-sub-menu index="3" v-if="isLoggedIn === true">
      <template #title
        ><el-avatar style="margin: auto" :size="30" :src="imageUrl" @error="errorHandler">
          <img :src="imageUrl" /> </el-avatar
      ></template>
      <el-menu-item index="/user">个人中心</el-menu-item>
      <el-menu-item index="3-3" @click="useUserStoreHook().logOut">退出登录</el-menu-item>
    </el-sub-menu>
    <div v-else class="flex items-center mx-5">
      <div @click="centerDialogVisible = true">
        <el-button type="primary">登录</el-button>
      </div>
    </div>
  </el-menu>
  <login
    :login-window-open="centerDialogVisible"
    @login-window-close="centerDialogVisible = false"
  ></login>
</template>

<style lang="scss" scoped>
.flex-grow {
  flex-grow: 1;
}
</style>
