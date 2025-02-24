<script setup lang="ts">
import { useImageOperationStateStore } from '@/store/imageOperationState'
import { generateImageUrl } from '@/composables/image/utils'
import type IStackViewport from '@cornerstonejs/core/src/types/IStackViewport'
import { Enums } from '@cornerstonejs/core'
import { ref, onMounted, onUnmounted, nextTick } from 'vue'
import type { SeriesInfo } from '@/types/series'

const props = defineProps<{
  seriesInfo: SeriesInfo
  seriesPath: string
  keyValue: string
}>()
const imageIds: string[] = []
const { ViewportType } = Enums
const imageOperationStateStore = useImageOperationStateStore()
let elementId = ref('cornerstone-element-seriesDicom-' + props.keyValue + props.seriesInfo.seriesId)
const viewportId = 'stackViewPort-seriesDicom-' + props.keyValue + props.seriesInfo.seriesId
const renderingEngine = imageOperationStateStore.renderingEngine
const imagesListUrl = generateImageUrl(props.seriesPath as string)

onMounted(async () => {
  await nextTick()
  if (props.seriesPath!.endsWith('.dcm')) {
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
  if (props.seriesPath!.endsWith('.dcm')) {
    renderingEngine.disableElement(viewportId)
  }
})
</script>

<template>
  <div :id="elementId"></div>
</template>

<style lang="scss" scoped></style>
