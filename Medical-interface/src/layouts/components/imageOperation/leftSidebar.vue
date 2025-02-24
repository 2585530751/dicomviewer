<script setup lang="ts">
import biTextIndentRight from '@/assets/svg/BiTextIndentRight.svg?component'

import type { IconifyIconOffline } from '@/components/ReIcon'
import arrowRight from '@iconify-icons/ep/arrow-right'
import { ref, shallowRef } from 'vue'

import imagesListsDisplay from '@/layouts/components/imageOperation/imagesListsDisplay.vue'
defineOptions({
  name: ''
})

var currentComp = shallowRef(imagesListsDisplay)
var display = ref(true)

function changeCurrentComp(comp: any): void {
  currentComp.value = comp
}
</script>

<template>
  <div v-show="display" class="divide-x-0 divide-y-4 w-60 divide-double divide-slate-400/25 ">
    <div
      class="flex items-center justify-between flex-nowrap bg-stone-50 dark:border-gray-700 dark:bg-gray-800"
    >
      <div>
        <el-button-group>
          <el-button text @click="changeCurrentComp(imagesListsDisplay)"> 序列 </el-button>
          <el-button text> 关联 </el-button>
        </el-button-group>
      </div>

      <div class="flex-none">
        <el-button text size="small" @click="display = !display">
          <biTextIndentRight style="height: 25px; width: 25px"></biTextIndentRight>
        </el-button>
      </div>
    </div>
    <component :is="currentComp" />
  </div>
  <div
    v-show="!display"
    @click="display = !display"
    class="fixed left-0 flex items-center w-4 h-10 bg-gray-300 rounded cursor-pointer top-80 dark:bg-gray-600 hover:dark:bg-gray-300 hover:bg-gray-600"
  >
    <IconifyIconOffline
      class="hover:text-blue-500"
      :icon="arrowRight"
      :style="{ fontSize: '20px' }"
    ></IconifyIconOffline>
  </div>
</template>

<style lang="scss" scoped></style>
