<script setup lang="ts">
import type { SeriesInfoWindows } from '@/types/image'
import { initDemo } from '@/utils/helpers/index.js'
import registerWebImageLoader from '@/utils/helpers/registerWebImageLoader'
import hardcodedMetaDataProvider from '@/utils/helpers//hardcodedMetaDataProvider'
import type { Types } from '@cornerstonejs/core'
import { defineProps, defineOptions, onMounted, watch, ref, reactive, onUnmounted } from 'vue'
import { generateImageUrl } from '@/composables/image/utils'
import { imageKeyValueStore } from '@/composables/image/imageKeyValueStore'
import { createViewportColorbar } from '@/composables/image/imageOperate'
// import {type ViewportColorbar} from '@cornerstonejs/tools/src/utilities/voi/colorbar/ViewportColorbar'
import {
  Enums as csToolsEnums,
  segmentation,
  utilities as cstUtils,
  cancelActiveManipulations,
  annotation
} from '@cornerstonejs/tools'
import {
  RenderingEngine,
  Enums,
  imageLoader,
  metaData,
  getRenderingEngine,
  volumeLoader,
  cache
} from '@cornerstonejs/core'
import { useImageOperationStateStore } from '@/store/imageOperationState'
import { message } from '@/utils/message'
import type { SeriesInfo, ImageInfo } from '@/types/series'

defineOptions({
  name: 'stackViewPortWindows'
})
const props = defineProps<{
  imagesInfoWindows: SeriesInfoWindows | 0
  index: number
}>()
const imageIds: string[] = []
const { ViewportType } = Enums
const imageOperationStateStore = useImageOperationStateStore()
let elementId = ref('cornerstone-element-' + props.index)
const viewportId = 'stackViewPort' + props.index
const renderingEngine = imageOperationStateStore.renderingEngine
let colorbarContainerId = ref('colorbarContainer' + props.index)
var segmentationId = ''
const segmentationContourId = imageOperationStateStore.segmentationId + 'Contour'
const checkLabelmapSegmentationToolName = [
  'CircularBrush',
  'CircularEraser',
  'CircleScissor',
  'RectangleScissor',
  'ThresholdCircle',
  'ScissorsEraser',
  'PaintFill'
]
const checkContourSegmentationToolName = [
  'PlanarFreehandContourSegmentationTool',
  'LivewireContourSegmentationTool',
  'BSplineSegmentation',
  'LinearSplineSegmentation',
  'CatmullRomSplineSegmentation',
  'CardinalSplineSegmentation'
]

onMounted(async () => {
  await renderStackViewport()
})

onUnmounted(() => {
  if (props.imagesInfoWindows != 0) {
    renderingEngine.disableElement(viewportId)
  }
})

watch(
  () => props.imagesInfoWindows,
  (newValue, oldValue) => {
    if (oldValue !== 0) {
      if (JSON.stringify(newValue) != JSON.stringify(oldValue) && newValue != 0) {
        imageOperationStateStore.viewportColorbar[props.index].destroy()
        renderingEngine.disableElement(viewportId)
        imageIds.length = 0
        renderStackViewport()
      }
    } else {
      imageIds.length = 0
      renderStackViewport()
    }
  },
  {
    deep: true
  }
)

function constructImagesId() {
  var singleImageIndex = 0
  if (props.imagesInfoWindows != 0) {
    const temporarySingleImage = props.imagesInfoWindows.imageInfo
    props.imagesInfoWindows.seriesInfo.imageList.forEach((item: ImageInfo) => {
      var temporaryimagePath
      if (item.modelType === 'model') {
        temporaryimagePath = generateImageUrl(item.imageModelData!.resultPath) as string
      } else {
        temporaryimagePath = generateImageUrl(item.imagePath!) as string
      }
      imageKeyValueStore.set(temporaryimagePath, item)
      imageIds.push(temporaryimagePath)
      if (temporarySingleImage && temporarySingleImage.imageId === item.imageId) {
        singleImageIndex = imageIds.length - 1
      }
    })
  }
  return singleImageIndex
}

async function selectSeriesWindowsClick(event: Event) {
  imageOperationStateStore.selectSeriesWindows = props.index
  if (props.imagesInfoWindows != 0) {
    if (checkLabelmapSegmentationToolName.includes(imageOperationStateStore.leftMouseActive)) {
      if (imageOperationStateStore.segmentationRepresentationUIDList.get(segmentationId)) {
        console.log(segmentation.state.getSegmentationIdRepresentations(segmentationId))
        console.log(
          imageOperationStateStore.segmentationRepresentationUIDList.get(
            imageOperationStateStore.segmentationId + props.imagesInfoWindows.imageInfo.imageId
          )
        )
        console.log(
          segmentation.activeSegmentation.getActiveSegmentation(
            imageOperationStateStore.toolGroup.id
          )
        )
        console.log(
          segmentation.state.getSegmentationRepresentations(imageOperationStateStore.toolGroup.id)
        )
      } else {
        message('未添加Labelmap分割层，无需使用该分割工具。', { type: 'error' })
        event.preventDefault()
        event.stopPropagation()
      }
    }
    if (checkContourSegmentationToolName.includes(imageOperationStateStore.leftMouseActive)) {
      if (!imageOperationStateStore.segmentationRepresentationUIDList.get(segmentationContourId)) {
        message('未切换为Contour分割层，无需使用该分割工具。', { type: 'error' })
        event.preventDefault()
        event.stopPropagation()
      }
    }
  }
}

async function handleMouseEnter() {
  if (props.imagesInfoWindows != 0) {
    if (checkLabelmapSegmentationToolName.includes(imageOperationStateStore.leftMouseActive)) {
      if (imageOperationStateStore.segmentationRepresentationUIDList.get(segmentationId)) {
        const segmentationRepresentationUID =
          imageOperationStateStore.segmentationRepresentationUIDList.get(segmentationId)
        await segmentation.activeSegmentation.setActiveSegmentationRepresentation(
          imageOperationStateStore.toolGroup.id,
          segmentationRepresentationUID
        )
        cstUtils.segmentation.triggerSegmentationRender(imageOperationStateStore.toolGroup.id)
      } else {
        imageOperationStateStore.toolGroup.setToolPassive(imageOperationStateStore.leftMouseActive)
      }
    }
  }
  if (checkContourSegmentationToolName.includes(imageOperationStateStore.leftMouseActive)) {
    if (imageOperationStateStore.segmentationRepresentationUIDList.get(segmentationContourId)) {
      const segmentationRepresentationUID =
        imageOperationStateStore.segmentationRepresentationUIDList.get(segmentationContourId)
      await segmentation.activeSegmentation.setActiveSegmentationRepresentation(
        imageOperationStateStore.toolGroup.id,
        segmentationRepresentationUID
      )
      cstUtils.segmentation.triggerSegmentationRender(imageOperationStateStore.toolGroup.id)
    } else {
      imageOperationStateStore.toolGroup.setToolPassive(imageOperationStateStore.leftMouseActive)
    }
  }
}

function handleMouseLeave() {
  imageOperationStateStore.toolGroup.setToolActive(imageOperationStateStore.leftMouseActive, {
    bindings: [
      {
        mouseButton: csToolsEnums.MouseBindings.Primary // Left Click
      }
    ]
  })
}

async function renderStackViewport() {
  if (props.imagesInfoWindows != 0) {
    const element: HTMLDivElement = document.getElementById(elementId.value) as HTMLDivElement
    const singleImageIndex = constructImagesId()
    var viewport = getRenderingEngine(imageOperationStateStore.renderingEngine.id)!.getViewport(
      viewportId
    ) as Types.IStackViewport
    if (!viewport) {
      const viewportInput = {
        viewportId: viewportId,
        type: ViewportType.STACK,
        element: element as HTMLDivElement
      }
      renderingEngine.enableElement(viewportInput)
      viewport = <Types.IStackViewport>renderingEngine.getViewport(viewportId)
      registerWebImageLoader(imageLoader)
      metaData.addProvider(
        // @ts-ignore
        (type, imageId) => hardcodedMetaDataProvider(type, imageId, imageIds),
        10000
      )
      const colorbar: any = createViewportColorbar(
        props.index,
        elementId.value,
        colorbarContainerId.value
      )
      imageOperationStateStore.viewportColorbar[props.index] = colorbar
      imageOperationStateStore.toolGroup.addViewport(viewportId, renderingEngine.id)
      element.addEventListener(csToolsEnums.Events.KEY_DOWN, (evt) => {
        cancelAndRemoveAnnotation(element)
      })
      // element.addEventListener(MOUSE_WHEEL, ((evt: Types.EventTypes.ImageRenderedEvent) => {
      //   console.log('IMAGE_RENDERED')
      // }) as EventListener)
    }
    await viewport.setStack(imageIds, singleImageIndex)
    viewport.render()
    cstUtils.stackContextPrefetch.enable(viewport.element)
    imageOperationStateStore.viewports[props.index] = viewport
    segmentationId =
      imageOperationStateStore.segmentationId + props.imagesInfoWindows.imageInfo.imageId
  }
}

function cancelAndRemoveAnnotation(temElement: HTMLDivElement) {
  const annotationUID = cancelActiveManipulations(temElement)
  annotation.state.removeAnnotation(annotationUID as string)
}
// const { MOUSE_WHEEL } = csToolsEnums.Events
// const {} =Enums.Events
</script>

<template>
  <div
    class="flex justify-between border-stone-200"
    :class="{ highlighted: imageOperationStateStore.selectSeriesWindows === props.index }"
  >
    <div
      :id="elementId"
      class="w-full h-full"
      @click="selectSeriesWindowsClick"
      @mouseenter="handleMouseEnter"
      @mouseleave="handleMouseLeave"
      @mousemove="null"
    ></div>
    <div :id="colorbarContainerId" class="w-2 h-full colorbar-container"></div>
  </div>
</template>

<style lang="scss" scoped>
/* 定义普通模式下的样式 */
.highlighted {
  /* 设置边框样式 */
  border: 2px dashed #00d3f8a4; /* 边框宽度、样式和颜色 */
  /* 设置圆角 */
  border-radius: 5px; /* 圆角的半径 */
}
.colorbar-container {
  position: relative;
  box-sizing: border-box;
  cursor: initial;
}
</style>
