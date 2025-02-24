<script setup lang="ts">
import biTextIndentLeft from '@/assets/svg/BiTextIndentLeft.svg?component'
import commonTools from '@/layouts/components/imageOperation/commonTools.vue'
import algorithmModel from '@/layouts/components/imageOperation/algorithmModel.vue'
import imageFeatureOperate from '@/layouts/components/imageOperation/imageFeatureOperate.vue'
import { ref, shallowRef } from 'vue'
import arrowLeft from '@iconify-icons/ep/arrow-left'

var currentComp = shallowRef(commonTools)
var display = ref(true)

function changeCurrentComp(comp: any): void {
  currentComp.value = comp
}

</script>

<template>
  <div v-show="display" class="divide-double divide-y-4 w-[15.8rem] divide-slate-400/25 divide-x-0">
    <div
      class="flex flex-wrap items-center justify-between bg-stone-50 dark:border-gray-700 dark:bg-gray-800"
    >
      <div>
        <el-button text size="small" @click="display = !display">
          <biTextIndentLeft style="height: 25px; width: 25px"></biTextIndentLeft>
        </el-button>
      </div>
      <div>
        <el-button-group>
          <el-button text @click="changeCurrentComp(commonTools)"> 工具 </el-button>
          <el-button text @click="changeCurrentComp(algorithmModel)"> AI辅助 </el-button>
          <el-button text @click="changeCurrentComp(imageFeatureOperate)"> 操作 </el-button>
        </el-button-group>
      </div>
    </div>
    <component :is="currentComp" />
  </div>
  <div v-show="!display" @click="display = !display" class="fixed right-0 flex items-center w-4 h-10 bg-gray-300 rounded cursor-pointer top-80 dark:bg-gray-600 hover:dark:bg-gray-300 hover:bg-gray-600">
    <IconifyIconOffline
      class="hover:text-blue-500"
      :icon="arrowLeft"
      :style="{ fontSize: '20px' }"
    ></IconifyIconOffline>
  </div>
</template>

<style lang="scss" scoped></style>