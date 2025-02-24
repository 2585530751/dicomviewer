<script setup lang="ts">
import { ref } from 'vue'
import { RouterView } from 'vue-router'
import { ElConfigProvider } from 'element-plus'

//@ts-ignore
import zhCn from 'element-plus/es/locale/lang/zh-cn.mjs'

const locale = ref(zhCn)
</script>

<template>
<el-config-provider :locale="locale">
  <router-view></router-view>
</el-config-provider>
</template>

<style scoped>

</style>
