<script setup lang="ts">
import {
  RenderingEngine,
  Enums,
  imageLoader,
  metaData,
  getRenderingEngine,
  volumeLoader
} from '@cornerstonejs/core'
import type { Types } from '@cornerstonejs/core'

import { initDemo, setCtTransferFunctionForVolumeActor } from '@/utils/helpers/index.js'
import { computed, onMounted, onUnmounted, reactive, watch } from 'vue'
import type { RGB } from '@cornerstonejs/core/src/types/'
import OrientationAxis from '@cornerstonejs/core/src/enums/OrientationAxis'
import createTools from '@/composables/toolsManage'
import hardcodedMetaDataProvider from '@/utils/helpers//hardcodedMetaDataProvider'
import registerWebImageLoader from '@/utils/helpers/registerWebImageLoader'
import { useImageOperationStateStore } from '@/store/imageOperationState'
import { generateImageUrl } from '@/composables/image/utils'
import { imageKeyValueStore } from '@/composables/image/imageKeyValueStore'
import stackViewPortWindows from '@/layouts/components/imageOperation/stackViewPortWindows.vue'
import {
  Enums as csToolsEnums,
  segmentation,
  utilities as cstUtils,
  cancelActiveManipulations,
  annotation
} from '@cornerstonejs/tools'

const imageOperationStateStore = useImageOperationStateStore()

onMounted(() => {
  const element: HTMLDivElement = document.getElementById('imageOperationView') as HTMLDivElement
 

  element.oncontextmenu = (e) => e.preventDefault()
  // 创建一个新的 ResizeObserver 实例
  let ro = new ResizeObserver((entries) => {
    console.log('ResizeObserver')
    setTimeout(() => {
      // viewport.resize()
      imageOperationStateStore.renderingEngine.resize(true, true)
    }, 100) // 延迟1000毫秒后调用
  })
  ro.observe(element)
})

createTools()

const gridStyle = computed(() => ({
  display: 'grid',
  gridTemplateRows: `repeat(${imageOperationStateStore.windowRowsColumns.rows}, 1fr)`,
  gridTemplateColumns: `repeat(${imageOperationStateStore.windowRowsColumns.columns}, 1fr)`,
  gap: '8px' // 格子间隔
}))
const totalCells = computed(() => {
  return (
    imageOperationStateStore.windowRowsColumns.rows *
    imageOperationStateStore.windowRowsColumns.columns
  )
})

const imagesInfoWindows = computed(() => {
  return imageOperationStateStore.seriesListWindows
})


</script>

<template>
  <div class="w-full navHeight" :style="gridStyle" id="imageOperationView">
    <stackViewPortWindows
      class="border-2 border-solid rounded-md dark:border-blue-950 border-cyan-950"
      v-for="(item, index) in imagesInfoWindows.slice(0, totalCells)"
      :key="index"
      :index="index"
      :imagesInfoWindows="item"
    ></stackViewPortWindows>
  </div>
</template>

<style lang="scss" scoped>
.navHeight {
  height: calc(100vh - 50px);
}
</style>
