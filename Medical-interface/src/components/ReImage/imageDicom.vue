<script setup lang="ts">
import { useImageOperationStateStore } from '@/store/imageOperationState'
import { generateImageUrl } from '@/composables/image/utils'
import type IStackViewport from '@cornerstonejs/core/src/types/IStackViewport'
import { Enums } from '@cornerstonejs/core'
import { ref, onMounted, onUnmounted } from 'vue'
import type { SeriesInfo, ImageInfo } from '@/types/series'

const props = defineProps<{
  imageInfo: ImageInfo
  keyValue: string
}>()
const imageIds: string[] = []
const { ViewportType } = Enums
const imageOperationStateStore = useImageOperationStateStore()
let elementId = ref('cornerstone-element-imageDicom-' + props.keyValue + props.imageInfo.imageId)
const viewportId = 'stackViewPort-imageDicom-' + props.keyValue + props.imageInfo.imageId
const renderingEngine = imageOperationStateStore.renderingEngine
const imagesListUrl = generateImageUrl(props.imageInfo.imagePath as string)

onMounted(() => {
  if (props.imageInfo.imagePath!.endsWith('.dcm')) {
    const element: HTMLDivElement = document.getElementById(elementId.value) as HTMLDivElement
    const viewportInput = {
      viewportId: viewportId,
      type: ViewportType.STACK,
      element: element as HTMLDivElement
    }
    renderingEngine.enableElement(viewportInput)
    const viewport = renderingEngine.getViewport(viewportId) as IStackViewport
    imageIds[0] = imagesListUrl || ''
    viewport.setStack(imageIds).then(() => {
      viewport.render()
    })
  }
})

onUnmounted(() => {
  if (props.imageInfo.imagePath!.endsWith('.dcm')) {
    renderingEngine.disableElement(viewportId)
  }
})
</script>

<template>
  <div>
    <div :id="elementId" class="w-16 h-16"></div>
  </div>
</template>

<style lang="scss" scoped></style>
