<script setup lang="ts">
import files from '@iconify-icons/ep/files'
import timer from '@iconify-icons/ep/timer'
import folder from '@iconify-icons/ep/folder'
import { basicImageUrl } from '@/api/utils'

import { ref, onMounted, nextTick, onUnmounted } from 'vue'
import { useImageOperationStateStore } from '@/store/imageOperationState'
import type { SeriesInfoWindows } from '@/types/image'
import { changeSeriesListWindowsToSession } from '@/composables/image/utils'
import { Enums } from '@cornerstonejs/core'
import { generateImageUrl } from '@/composables/image/utils'
import type IStackViewport from '@cornerstonejs/core/src/types/IStackViewport'
import type { SeriesInfo } from '@/types/series'

const props = defineProps<{
  imagesModelsList: SeriesInfo
  index: number
}>()

const emit = defineEmits<{
  addSelectImagesModelsLists: [imagesList: SeriesInfo,checked:boolean]
}>()

const checked = ref(false)
const imageIds: string[] = []
const { ViewportType } = Enums
const imageOperationStateStore: any = useImageOperationStateStore()
let elementId = ref('cornerstone-element-imagesModelsList-' + props.index)
const viewportId = 'stackViewPort-imagesModelsList-' + props.index
const renderingEngine = imageOperationStateStore.renderingEngine
const imagesModelsListUrl = generateImageUrl(props.imagesModelsList.imageList[0].imageModelData!.resultPath)
const imagesModelsListUrlCheck = ref(true)
console.log(imagesModelsListUrl)
function selectimagesModelsListToWindows() {
  imageOperationStateStore.selectSeriesModelsList = props.index
  const seriesInfoWindows: SeriesInfoWindows = {
    seriesInfo: props.imagesModelsList,
    imageInfo: props.imagesModelsList.imageList[0]
  }
  //imageOperationStateStore.imagesModelsListWindows[imageOperationStateStore.selectSeriesModelsListWindows] = seriesInfoWindows

  imageOperationStateStore.seriesListWindows = [
    ...imageOperationStateStore.seriesListWindows.slice(0, imageOperationStateStore.selectSeriesWindows),
    seriesInfoWindows,
    ...imageOperationStateStore.seriesListWindows.slice(imageOperationStateStore.selectSeriesWindows + 1)
  ]
  changeSeriesListWindowsToSession(seriesInfoWindows, imageOperationStateStore.selectSeriesWindows)
}

onMounted(async() => {

  if (imagesModelsListUrl && imagesModelsListUrl.endsWith('.dcm')) {
    await nextTick()
    const element: HTMLDivElement = document.getElementById(elementId.value) as HTMLDivElement
    const viewportInput = {
      viewportId: viewportId,
      type: ViewportType.STACK,
      element: element as HTMLDivElement
    }
    renderingEngine.enableElement(viewportInput)
    const viewport = renderingEngine.getViewport(viewportId) as IStackViewport
    imageIds[0] = imagesModelsListUrl
    viewport.setStack(imageIds).then(() => {
      viewport.render()
    })
   
  }else{
    console.log("viewport")
    imagesModelsListUrlCheck.value = false
  }
})

onUnmounted(() => {
  renderingEngine.disableElement(viewportId)
})
</script>

<template>
  <div class="flex justify-center w-5/6 flex-nowrap">
    <div class="w-1/6">
      <el-checkbox v-model="checked" size="large" @change=" $emit('addSelectImagesModelsLists', props.imagesModelsList, checked)"/>
    </div>
    <div
      class="flex flex-col justify-center w-4/6 gap-1"
      :class="{ highlighted: imageOperationStateStore.selectSeriesModelsList === index }"
      @click="selectimagesModelsListToWindows()"
    >
      <div class="flex items-center justify-between">
        <span class="text-xs text-gray-500">
          {{ props.imagesModelsList.seriesModality }}
        </span>
        <span class="text-xs text-gray-500"
          ><IconifyIconOffline
            class="hover:text-blue-500"
            :icon="timer"
            :style="{ fontSize: '10px' }"
          ></IconifyIconOffline
          >{{ props.imagesModelsList.seriesCheckTime }}</span
        >
      </div>
      <div class="border-2 border-solid rounded-lg border-slate-300">
        <el-image
          fit="cover"
          :src="basicImageUrl + props.imagesModelsList.imageList[0].imageModelData?.resultPath"
          :crossorigin="'anonymous'"
          v-show="!imagesModelsListUrlCheck"
        />
        <div >
          <div :id="elementId" class="w-32 h-32" v-show="imagesModelsListUrlCheck"></div>
        </div>
        <!-- <div :id="elementId" class="absolute w-screen h-screen"></div> -->
      </div>
      <div class="flex items-center justify-between">
        <span class="text-xs text-gray-500">
          {{ props.imagesModelsList.patientName }}
        </span>
        <span class="text-xs text-gray-500"
          ><IconifyIconOffline
            class="hover:text-blue-500"
            :icon="files"
            :style="{ fontSize: '10px' }"
          ></IconifyIconOffline
          >{{ props.imagesModelsList.imageList.length }}</span
        >
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.highlighted {
  /* 设置边框样式 */
  border: 3px solid #52559e; /* 边框宽度、样式和颜色 */
  /* 设置圆角 */
  border-radius: 10px; /* 圆角的半径 */
  padding: 4px; /* 内边距 */
}
</style>