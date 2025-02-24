<script setup lang="ts">
import { isDark } from '@/composables'
import dayIcon from '@/assets/svg/day.svg?component'
import darkIcon from '@/assets/svg/dark.svg?component'
import askAi from '@/assets/svg/ask-ai.svg'
import addSegemntation from '@/assets/svg/addSegmentation.svg'
import segmentationSwitch from '@/assets/svg/segmentationSwitch.svg'
import cpatureImage from '@/assets/svg/capture.svg'
import type { IconifyIconOffline } from '@/components/ReIcon'
import back from '@iconify-icons/ep/back'
import setting from '@iconify-icons/ep/setting'
import Emessage from '@iconify-icons/ep/message'
import { useImageOperationStateStore } from '@/store/imageOperationState'
import { onMounted, reactive, ref } from 'vue'
import downloadImage from '@/layouts/components/imageOperation/downloadImage.vue'
import { message } from '@/utils/message'
import { router } from '@/router'
import {
  imageLoader,
  Enums,
  cache,
  getRenderingEngine,
  utilities as csUtils,
  volumeLoader,
  StackViewport,
  VolumeViewport,
  setVolumesForViewports
} from '@cornerstonejs/core'
import type { Types } from '@cornerstonejs/core'
import * as cornerstoneTools from '@cornerstonejs/tools'
import type {  SeriesInfoWindows } from '@/types/image'

const centerDialogVisible = ref(false)

const imageOperationStateStore =useImageOperationStateStore()
const { ViewportType } = Enums
const {
  PanTool,
  ZoomTool,
  SegmentationDisplayTool,
  ToolGroupManager,
  BrushTool,
  StackScrollMouseWheelTool,
  segmentation,
  Enums: csToolsEnums,
  utilities: cstUtils
} = cornerstoneTools

const segmentationRepresentationType = ref('Contour')
const segmentationRepresentationLabelmaps = reactive([])

onMounted(async () => {
  await addContourSegmentation(imageOperationStateStore.segmentationId + 'Contour')
})
const centerDialogVisible1 = ref(false)

async function switchSegmentationRepresentation() {
  const stackViewport: Types.IStackViewport = imageOperationStateStore.viewports[
    imageOperationStateStore.selectSeriesWindows
  ] as Types.IStackViewport
  const seriesInfoWindows = imageOperationStateStore.seriesListWindows[
    imageOperationStateStore.selectSeriesWindows
  ] as SeriesInfoWindows

  const segmentationId = imageOperationStateStore.segmentationId + seriesInfoWindows.imageInfo.imageId
  const segmentationContourId = imageOperationStateStore.segmentationId + 'Contour'

  if (segmentationRepresentationType.value === 'Contour') {
    segmentation.state.removeSegmentationRepresentation(
      imageOperationStateStore.toolGroup.id,
      segmentationContourId
    )
    imageOperationStateStore.segmentationRepresentationUIDList.delete(segmentationContourId)
    segmentationRepresentationType.value = 'Labelmap'
  } else {
    for (const segmentationRepresentationLabelmap of segmentationRepresentationLabelmaps) {
      segmentation.state.removeSegmentationRepresentation(
        imageOperationStateStore.toolGroup.id,
        segmentationRepresentationLabelmap
      )
      imageOperationStateStore.segmentationRepresentationUIDList.delete(segmentationRepresentationLabelmap)
    }
    segmentationRepresentationLabelmaps.length = 0
    await addContourSegmentation(segmentationContourId)
    segmentationRepresentationType.value = 'Contour'
  }
}

async function checkAddStackSegmentation() {
  const stackViewport: Types.IStackViewport = imageOperationStateStore.viewports[
    imageOperationStateStore.selectSeriesWindows
  ] as Types.IStackViewport
  const seriesInfoWindows = imageOperationStateStore.seriesListWindows[
    imageOperationStateStore.selectSeriesWindows
  ] as SeriesInfoWindows

  const segmentationId:string = imageOperationStateStore.segmentationId + seriesInfoWindows.imageInfo.imageId
  const segmentationContourId = imageOperationStateStore.segmentationId + 'Contour'

  if (
    stackViewport.getCurrentImageId().endsWith('.png') ||
    stackViewport.getCurrentImageId().endsWith('.jpg') ||
    stackViewport.getCurrentImageId().endsWith('.jpeg')
  ) {
    message('暂时无法为PNG/JPG格式切换为Labelmap分割层', { type: 'error' })
    return
  }

  if (imageOperationStateStore.segmentationRepresentationUIDList.get(segmentationId)) {
    message('该视图已经是Labelmap分割层。', { type: 'error' })
    return
  } else {
    segmentationRepresentationLabelmaps.push(segmentationId as never)
    await addStackSegmentation(stackViewport, segmentationId)
    message('成功将分割层切换为Labelmap分割层。！', { type: 'success' })
  }
}

async function addStackSegmentation(viewport: Types.IStackViewport, segmentationId: string) {
  const { imageIds: segmentationImageIds } = await imageLoader.createAndCacheDerivedImages(
    viewport.getImageIds()
  )
  if (!segmentation.state.getSegmentation(segmentationId)) {
    await segmentation.addSegmentations([
      {
        segmentationId,
        representation: {
          type: csToolsEnums.SegmentationRepresentations.Labelmap,
          data: {
            imageIdReferenceMap: cstUtils.segmentation.createImageIdReferenceMap(
              viewport.getImageIds(),
              segmentationImageIds
            )
          }
        }
      }
    ])
  }
  const segmentationRepresentationUID = await segmentation.addSegmentationRepresentations(
    imageOperationStateStore.toolGroup.id,
    [
      {
        segmentationId,
        type: csToolsEnums.SegmentationRepresentations.Labelmap
      }
    ]
  )
  cstUtils.segmentation.triggerSegmentationRender(imageOperationStateStore.toolGroup.id)
  imageOperationStateStore.segmentationRepresentationUIDList.set(
    segmentationId,
    segmentationRepresentationUID[0]
  )
  segmentation.activeSegmentation.setActiveSegmentationRepresentation(
    imageOperationStateStore.toolGroup.id,
    segmentationRepresentationUID[0]
  )
}

async function addContourSegmentation(segmentationId: string) {
  if (!segmentation.state.getSegmentation(segmentationId)) {
    // Add a segmentation that will contains the contour annotations
    segmentation.addSegmentations([
      {
        segmentationId,
        representation: {
          type: csToolsEnums.SegmentationRepresentations.Contour
        }
      }
    ])
  }
  // Create a segmentation representation associated to the toolGroupId
  const segmentationRepresentationUIDs = await segmentation.addSegmentationRepresentations(
    imageOperationStateStore.toolGroup.id,
    [
      {
        segmentationId,
        type: csToolsEnums.SegmentationRepresentations.Contour
      }
    ]
  )
  cstUtils.segmentation.triggerSegmentationRender(imageOperationStateStore.toolGroup.id)
  // Make the segmentation created as the active one
  segmentation.activeSegmentation.setActiveSegmentationRepresentation(
    imageOperationStateStore.toolGroup.id,
    segmentationRepresentationUIDs[0]
  )
  imageOperationStateStore.segmentationRepresentationUIDList.set(
    segmentationId,
    segmentationRepresentationUIDs[0]
  )
}

function removeStackSegmentation(segmentationId: string) {
  // segmentation.state.removeSegmentationRepresentation(
  //   imageOperationStateStore.toolGroup.id,
  //   imageOperationStateStore.segmentationRepresentationUIDList[imageOperationStateStore.selectSeriesWindows]
  // )
  segmentation.state.removeSegmentationRepresentations(imageOperationStateStore.toolGroup.id)
  segmentation.state.removeSegmentation(segmentationId)
}

// async function switchStackViewportToVolumeViewport() {
//   if (
//     !segmentation.state.getSegmentation(
//       imageOperationStateStore.segmentationId + imageOperationStateStore.selectSeriesWindows
//     )
//   ) {
//     await addStackSegmentation(
//       imageOperationStateStore.viewports[imageOperationStateStore.selectSeriesWindows] as Types.IStackViewport,
//       imageOperationStateStore.segmentationId + imageOperationStateStore.selectSeriesWindows
//     )
//   }

//   const segmentationId = imageOperationStateStore.segmentationId + imageOperationStateStore.selectSeriesWindows
//   const viewport = imageOperationStateStore.viewports[imageOperationStateStore.selectSeriesWindows]
//   let newViewport: Types.IVolumeViewport | Types.IStackViewport
//   console.log(viewport.type)
//   if (viewport.type === ViewportType.STACK) {
//     const volumeId = 'cornerstoneStreamingImageVolume' + imageOperationStateStore.selectSeriesWindows
//     console.log(volumeId)
//     newViewport = (await csUtils.convertStackToVolumeViewport({
//       viewport: viewport as Types.IStackViewport,
//       options: {
//         background: <Types.Point3>[0, 0.4, 0],
//         volumeId: volumeId
//       }
//     })) as Types.IVolumeViewport

//     if (imageOperationStateStore.toolGroup) {
//       imageOperationStateStore.toolGroup.addViewport(newViewport.id, imageOperationStateStore.renderingEngine.id)
//     }
//     await segmentation.convertStackToVolumeSegmentation({
//       segmentationId,
//       options: {
//         toolGroupId: imageOperationStateStore.toolGroup.id,
//         volumeId: volumeId
//       }
//     })
//     if (imageOperationStateStore.toolGroup) {
//       imageOperationStateStore.toolGroup.addViewport(newViewport.id, imageOperationStateStore.renderingEngine.id)
//     }
//     console.log(cache.getVolume(volumeId))
//   } else {
//     // await segmentation.state.removeSegmentationRepresentation(
//     //   imageOperationStateStore.toolGroup.id,
//     //   imageOperationStateStore.segmentationRepresentationUIDList[imageOperationStateStore.selectSeriesWindows]
//     // )
//     segmentation.state.removeSegmentationRepresentations(imageOperationStateStore.toolGroup.id)
//     setTimeout(async () => {
//       segmentation.convertVolumeToStackSegmentation({
//         segmentationId,
//         options: {
//           toolGroupId: imageOperationStateStore.toolGroup.id
//         }
//       })

//       newViewport = (await csUtils.convertVolumeToStackViewport({
//         viewport: viewport as Types.IVolumeViewport,
//         options: {
//           background: <Types.Point3>[0, 0, 0]
//         }
//       })) as Types.IStackViewport

//       // Set the tool group on the viewport
//       if (imageOperationStateStore.toolGroup) {
//         imageOperationStateStore.toolGroup.addViewport(newViewport.id, imageOperationStateStore.renderingEngine.id)
//       }
//       imageOperationStateStore.viewports[imageOperationStateStore.selectSeriesWindows] = newViewport
//     }, 1000)
//     // cache.getVolume('cornerstoneStreamingImageVolume' + imageOperationStateStore.selectSeriesWindows).destroy()
//   }
// }
</script>

<template>
  <div
    class="flex justify-between w-full h-10 border border-t-0 border-gray-200 border-solid shadow-md border-x-0 dark:border-gray-700 dark:bg-gray-800"
  >
    <div class="flex flex-wrap items-center justify-center h-10 gap-1 pl-2">
      <el-button text @click="router.go(-1)">
        <template #icon>
          <IconifyIconOffline :icon="back"></IconifyIconOffline>
        </template>
        返回
      </el-button>
    </div>
    <div class="flex items-center justify-center">
      <el-button-group>
        <el-tooltip content="明暗" placement="bottom" effect="light">
          <el-button text size>
            <el-switch
              style="margin: auto"
              v-model="isDark"
              inline-prompt
              :active-icon="dayIcon"
              :inactive-icon="darkIcon"
            />
          </el-button>
        </el-tooltip>
        <el-tooltip content="AI" placement="bottom" effect="light">
          <el-button text @click="addContourSegmentation('segementationIdContour')">
            <askAi style="height: 25px; width: 25px"></askAi>
          </el-button>
        </el-tooltip>

        <el-tooltip
          :content="segmentationRepresentationType == 'Contour' ? '切换Labelmap' : '切换Contour'"
          placement="bottom"
          effect="light"
        >
          <el-button text @click="centerDialogVisible1 = true">
            <segmentationSwitch style="height: 25px; width: 25px"></segmentationSwitch>
          </el-button>
        </el-tooltip>
        <el-tooltip  content="添加分割层" placement="bottom" effect="light">
          <el-button text @click="checkAddStackSegmentation()" v-show="segmentationRepresentationType == 'Labelmap'">
            <addSegemntation style="height: 25px; width: 25px"></addSegemntation>
          </el-button>
        </el-tooltip>
        <el-tooltip content="下载" placement="bottom" effect="light">
          <el-button text>
            <cpatureImage
              style="height: 25px; width: 25px"
              @click="centerDialogVisible = true"
            ></cpatureImage>
          </el-button>
        </el-tooltip>
      </el-button-group>
    </div>

    <div class="flex items-center justify-center h-10">
      <el-button-group>
        <el-tooltip content="意见反馈" placement="bottom" effect="light">
          <el-button text>
            <IconifyIconOffline
              class="hover:text-blue-500"
              :icon="Emessage"
              :style="{ fontSize: '20px' }"
            ></IconifyIconOffline>
          </el-button>
        </el-tooltip>
        <el-tooltip content="设置" placement="bottom" effect="light">
          <el-button text>
            <IconifyIconOffline
              class="hover:text-blue-500"
              :icon="setting"
              :style="{ fontSize: '20px' }"
            ></IconifyIconOffline>
          </el-button>
        </el-tooltip>
      </el-button-group>
    </div>
    <downloadImage
      :download-window-open="centerDialogVisible"
      @download-window-close="centerDialogVisible = false"
    ></downloadImage>
    <el-dialog v-model="centerDialogVisible1" title="提示" width="500" center>
      <span v-show="segmentationRepresentationType==='Contour'"> 将分割层切化为Labelmap形式，会使原来的Contour形式的分割层的内容转变为注释形式，并且需要您选择性的为视图添加Labelmap分割层。您确定要进行切化为Labelmap分割层吗？ </span>
      <span v-show="segmentationRepresentationType==='Labelmap'"> 将分割层切化为Contour形式，会固定原来的Labelmap形式的分割层的内容，使之不可修改，同时为所有视图添加Contour分割层。您确定要进行切化为Contour分割层吗？</span>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="centerDialogVisible1 = false">取消</el-button>
          <el-button type="primary" @click="centerDialogVisible1 = false,switchSegmentationRepresentation()"> 确定 </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<style lang="scss" scoped></style>
