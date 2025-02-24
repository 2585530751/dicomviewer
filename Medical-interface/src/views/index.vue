<script setup lang='ts'>
import "@/components/ReIcon/src/offlineIcon";
import appFooter from '@/layouts/components/appFooter.vue';
import navHeader from '@/layouts/components/navHeader.vue';
import appMain from '@/layouts/components/appMain.vue';

</script>

<template>
  
    <div class="common-layout">
    <el-container>
      <el-header >
        <nav-header></nav-header>
      </el-header>
      <el-main >
        <app-main></app-main>
      </el-main>
      <el-footer>
        <app-footer></app-footer>
      </el-footer>
    </el-container>
  </div>
</template>

<style lang='scss' scoped>

</style>
