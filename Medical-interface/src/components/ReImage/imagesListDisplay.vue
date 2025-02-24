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
  seriesList: SeriesInfo
  index: number
}>()

const emit = defineEmits<{
  addSelectImagesLists: [imagesList: SeriesInfo, checked: boolean]
}>()

const checked = ref(false)
const imageIds: string[] = []
const { ViewportType } = Enums
const imageOperationStateStore = useImageOperationStateStore()
let elementId = ref('cornerstone-element-imagesList-' + props.index)
const viewportId = 'stackViewPort-imagesList-' + props.index
const renderingEngine = imageOperationStateStore.renderingEngine
const imagesListUrl = generateImageUrl(props.seriesList.imageList[0].imagePath as string)
const imagesListUrlCheck = ref(true)

function selectImagesListToWindows() {
  imageOperationStateStore.selectSeriesList = props.seriesList.seriesId
  const seriesInfoWindows: SeriesInfoWindows = {
    seriesInfo: props.seriesList,
    imageInfo: props.seriesList.imageList[0]
  }
  //imageOperationStateStore.seriesListWindows[imageOperationStateStore.selectSeriesWindows] = seriesInfoWindows

  imageOperationStateStore.seriesListWindows = [
    ...imageOperationStateStore.seriesListWindows.slice(
      0,
      imageOperationStateStore.selectSeriesWindows
    ),
    seriesInfoWindows,
    ...imageOperationStateStore.seriesListWindows.slice(
      imageOperationStateStore.selectSeriesWindows + 1
    )
  ]
  changeSeriesListWindowsToSession(seriesInfoWindows, imageOperationStateStore.selectSeriesWindows)
}

function checkInputCheckbox() {
  emit('addSelectImagesLists', props.seriesList, checked.value)
}

onUnmounted(() => {
  renderingEngine.disableElement(viewportId)
})

onMounted(async () => {
  await nextTick()
  if (imagesListUrl && imagesListUrl.endsWith('.dcm')) {
    const element: HTMLDivElement = document.getElementById(elementId.value) as HTMLDivElement
    const viewportInput = {
      viewportId: viewportId,
      type: ViewportType.STACK,
      element: element as HTMLDivElement
    }
    renderingEngine.enableElement(viewportInput)
    const viewport = renderingEngine.getViewport(viewportId) as IStackViewport
    imageIds[0] = imagesListUrl
    viewport.setStack(imageIds).then(() => {
      viewport.render()
    })
  } else {
    imagesListUrlCheck.value = false
  }
})
</script>

<template>
  <div class="flex justify-center w-5/6 flex-nowrap">
    <div class="w-1/6">
      <el-checkbox v-model="checked" size="large" @change="checkInputCheckbox" />
    </div>
    <div
      class="flex flex-col justify-center w-4/6 gap-1"
      :class="{
        highlighted: imageOperationStateStore.selectSeriesList === props.seriesList.seriesId
      }"
      @click="selectImagesListToWindows()"
    >
      <div class="flex items-center justify-between">
        <span class="text-xs text-gray-500">
          {{ props.seriesList.seriesModality }}
        </span>
        <span class="text-xs text-gray-500"
          ><IconifyIconOffline
            class="hover:text-blue-500"
            :icon="timer"
            :style="{ fontSize: '10px' }"
          ></IconifyIconOffline
          >{{ props.seriesList.seriesCheckTime }}</span
        >
      </div>
      <div class="border-2 border-solid rounded-lg border-slate-300">
        <el-image
          fit="cover"
          :src="basicImageUrl + props.seriesList.imageList[0].imagePath"
          :crossorigin="'anonymous'"
          v-show="!imagesListUrlCheck"
        />
        <div v-show="imagesListUrlCheck">
          <div :id="elementId" class="w-32 h-32"></div>
        </div>
      </div>
      <div class="flex items-center justify-between">
        <span class="text-xs text-gray-500">
          {{ props.seriesList.patientName }}
        </span>
        <span class="text-xs text-gray-500"
          ><IconifyIconOffline
            class="hover:text-blue-500"
            :icon="files"
            :style="{ fontSize: '10px' }"
          ></IconifyIconOffline
          >{{ props.seriesList.seriesCount }}</span
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
