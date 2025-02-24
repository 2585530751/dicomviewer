<script setup lang="ts">
import dragOutlined from '@/assets/svg/drag-outlined.svg?component'
import flip from '@/assets/svg/flip.svg?component'
import darkThemeFilled from '@/assets/svg/dark-theme-20-filled.svg?component'
import zoom from '@/assets/svg/zoom.svg?component'
import cube3d from '@/assets/svg/cube-3d.svg?component'
import layer from '@/assets/svg/layer.svg?component'
import resetWrench from '@/assets/svg/reset-wrench.svg?component'
import diagnosisOutline from '@/assets/svg/diagnosis-outline.svg?component'
import reverseOperationIn from '@/assets/svg/reverse-operation-in.svg?component'
import playerPlay from '@/assets/svg/player-play.svg?component'
import playerPause from '@/assets/svg/player-pause.svg?component'
import playerStart from '@/assets/svg/player-start.svg?component'
import playerEnd from '@/assets/svg/player-end.svg?component'
import arrowUpDuotone from '@/assets/svg/arrow-up-duotone.svg?component'
import straightPipe from '@/assets/svg/straight-pipe.svg?component'
import menuDotsLineDuotone from '@/assets/svg/menu-dots-line-duotone.svg?component'
import addFill from '@/assets/svg/add-fill.svg?component'
import angle from '@/assets/svg/angle.svg?component'
import cobb from '@/assets/svg/cobb.svg?component'
import iImagingAlternativeCt from '@/assets/svg/i-imaging-alternative-ct.svg?component'
import cardiopulmonary from '@/assets/svg/cardiopulmonary.svg?component'
import ellipses from '@/assets/svg/ellipses.svg?component'
import circleCenter from '@/assets/svg/circleCenter.svg?component'
import scissorsEraser from '@/assets/svg/scissorsEraser.svg?component'
import rectangles from '@/assets/svg/rectangles.svg?component'
import pen from '@/assets/svg/pen.svg?component'
import pencil from '@/assets/svg/pencil.svg?component'
import sphere from '@/assets/svg/sphere.svg?component'
import cuboid from '@/assets/svg/cuboid.svg?component'
import probes from '@/assets/svg/probes.svg?component'
import captureImage from '@/assets/svg/captureImage.svg?component'
import remove from '@/assets/svg/remove.svg?component'
import removals from '@/assets/svg/removals.svg?component'
import copy from '@/assets/svg/copy.svg?component'
import separate from '@/assets/svg/separate.svg?component'
import arrowDown from '@iconify-icons/ep/arrow-down'
import arrowUp from '@iconify-icons/ep/arrow-up'
import videoCamera from '@iconify-icons/ep/video-camera'
import refreshRight from '@iconify-icons/ep/refresh-right'
import CircleScissor from '@/assets/svg/circleScissor.svg?component'
import rectangleScissor from '@/assets/svg/rectangleScissor.svg?component'
import paintFill from '@/assets/svg/paintFill.svg?component'
import circleEmpty from '@/assets/svg/circle.svg?component'
import selectToolname from '@/assets/svg/select.svg?component'
import circleTwoLine from '@/assets/svg/circleTwoLine.svg?component'
import circularBrush from '@/assets/svg/circularBrush.svg?component'
import circularEraser from '@/assets/svg/circularEraser.svg?component'
import thresholdCircle from '@/assets/svg/threSholdCircle.svg?component'
import splineSegmentation from '@/assets/svg/splineSegmentation.svg?component'
import livewireSegmentation from '@/assets/svg/livewireSegmentation.svg?component'
import planarFreehandSegmentation from '@/assets/svg/planarFreehandSegmentation.svg?component'
import livewireContour from '@/assets/svg/livewireContour.svg?component'
import splineROI from '@/assets/svg/splineROI.svg?component'

import { storageSession } from '@pureadmin/utils'
import { seriesListWindowsSession } from '@/composables/image/utils'
import type { SeriesInfoWindows } from '@/types/image'
import type { Types } from '@cornerstonejs/core'
import { getRenderingEngine } from '@cornerstonejs/core'
import type { IconifyIconOffline } from '@/components/ReIcon'
import imageOperation from '@/components/ReButton/imageOperation.vue'
import imageOperationText from '@/components/ReButton/imageOperationText.vue'
import * as cornerstoneTools from '@cornerstonejs/tools'
import type { Types as cstTypes } from '@cornerstonejs/tools'
import { useImageOperationStateStore } from '@/store/imageOperationState'
import {
  resetOriginal,
  flipH,
  resetPan,
  resetZoom,
  changeZoom,
  invert,
  setWindowLevel,
  setViewportColorbar,
  resetToDefaultViewportProperties,
  removeCurrentImageIdProperties,
  changeNextImage,
  changePreviousImage,
  hideSelectedAnnotation,
  showAllAnnotations,
  lockSelectedAnnotation,
  unlockAllAnnotations,
  removeAnnotation,
  selectAllAnnotations,
  reverseSelectionAnnotations,
  revokePreviousAnnotation,
  selectAnnotationsByToolName,
  downloadCanvasAsImage
} from '@/composables/image/imageOperate'
import type { ContourConfig } from '@cornerstonejs/tools/src/types/ContourTypes'
import { reactive, ref, watch } from 'vue'
import vtkColormaps from '@kitware/vtk.js/Rendering/Core/ColorTransferFunction/ColorMaps'

const {
  Synchronizer,

  AnnotationDisplayTool,
  ReferenceCursors,
  ReferenceLines,
  ScaleOverlayTool,

  DragProbeTool,

  BrushTool,
  CircleScissorsTool,
  MIPJumpToClickTool,
  MagnifyTool,
  PaintFillTool,
  RectangleScissorsTool,
  SegmentationDisplayTool,
  SphereScissorsTool,
  StackScrollTool,
  TrackballRotateTool,
  VolumeRotateMouseWheelTool,

  PanTool,
  WindowLevelTool,
  StackScrollMouseWheelTool,
  ZoomTool,
  PlanarRotateTool,

  LengthTool,
  ProbeTool,
  RectangleROITool,
  RectangleROIStartEndThresholdTool,
  RectangleROIThresholdTool,
  EllipticalROITool,
  CircleROITool,
  BidirectionalTool,
  AngleTool,
  CobbAngleTool,
  ArrowAnnotateTool,
  CrosshairsTool,
  PlanarFreehandROITool,
  UltrasoundDirectionalTool,
  LivewireContourTool,
  SplineROITool,

  PlanarFreehandContourSegmentationTool,
  LivewireContourSegmentationTool,
  SplineContourSegmentationTool,

  segmentation,
  utilities: csToolsUtilities,
  Enums: csToolsEnums
} = cornerstoneTools

const imageOperationStateStore = useImageOperationStateStore()
const segmentationTools = ref(true)
const operateTools = ref(true)
const annotationTools = ref(true)
const otherTools = ref(true)

const Splines = {
  CardinalSplineSegmentation: {
    splineType: SplineContourSegmentationTool.SplineTypes.Cardinal
  },
  CatmullRomSplineSegmentation: {
    splineType: SplineContourSegmentationTool.SplineTypes.CatmullRom
  },
  LinearSplineSegmentation: {
    splineType: SplineContourSegmentationTool.SplineTypes.Linear
  },
  BSplineSegmentation: {
    splineType: SplineContourSegmentationTool.SplineTypes.BSpline
  }
}

const Splines1 = {
  CardinalSplineROI: {
    splineType: SplineROITool.SplineTypes.Cardinal
  },
  CatmullRomSplineROI: {
    splineType: SplineROITool.SplineTypes.CatmullRom
  },
  LinearSplineROI: {
    splineType: SplineROITool.SplineTypes.Linear
  },
  BSplineROI: {
    splineType: SplineROITool.SplineTypes.BSpline
  }
}

const splineToolsNames = [...Object.keys(Splines)]
const splineAnnotationToolsNames = [...Object.keys(Splines1)]

const annotationToolsList = [
  { label: '箭头', toolName: ArrowAnnotateTool.toolName },
  { label: '直线', toolName: LengthTool.toolName },
  { label: '十字线', toolName: BidirectionalTool.toolName },
  { label: '角度', toolName: AngleTool.toolName },
  { label: 'Cobb角', toolName: CobbAngleTool.toolName },
  { label: '矩形', toolName: RectangleROITool.toolName },
  { label: '椭圆', toolName: EllipticalROITool.toolName },
  { label: '圆形', toolName: CircleROITool.toolName },
  { label: '平面手绘', toolName: PlanarFreehandROITool.toolName },
  { label: '探测', toolName: ProbeTool.toolName },
  { label: '超声定向', toolName: UltrasoundDirectionalTool.toolName }
]

const layouts = [
  { label: '1X1', rows: 1, columns: 1 },
  { label: '1X2', rows: 1, columns: 2 },
  { label: '2X1', rows: 2, columns: 1 },
  { label: '1X3', rows: 1, columns: 3 },
  { label: '3X1', rows: 3, columns: 1 },
  { label: '2X2', rows: 2, columns: 2 },
  { label: '2X3', rows: 2, columns: 3 },
  { label: '3X2', rows: 3, columns: 2 },
  { label: '3X3', rows: 3, columns: 3 }
]
const setLayout = (r: number, c: number) => {
  imageOperationStateStore.windowRowsColumns.rows = r
  imageOperationStateStore.windowRowsColumns.columns = c
  setTimeout(() => {
    imageOperationStateStore.renderingEngine.resize(true, true)
  }, 1000)
}

const windowLevelPresetValues = [
  { label: 'Soft tissue 400/40', window: 400, level: 40 },
  { label: 'Lung 1500/-600', window: 1500, level: -600 },
  { label: 'Liver 150/90', window: 150, level: 90 },
  { label: 'Bone 2500/480', window: 2500, level: 480 },
  { label: 'Brain 80/40', window: 80, level: 40 },
  { label: 'Abdomen 350/60', window: 360, level: 60 },
  { label: 'Chest 360/40', window: 350, level: 40 }
]

const thresholdCirclePresetValues = [
  { label: 'CT Fat: (-150, -70)', threshold: [-150, -70] },
  { label: 'CT Bone: (200, 1000)', threshold: [200, 1000] }
]

const colormaps = vtkColormaps.rgbPresetNames.map((presetName) =>
  vtkColormaps.getPresetByName(presetName)
)

const playbackFramesPerSecond = ref(1)
const checkPlaybackFramesPerSecond = ref(true)
function playClipPlayer() {
  csToolsUtilities.cine.playClip(
    imageOperationStateStore.viewports[imageOperationStateStore.selectSeriesWindows].element,
    { framesPerSecond: playbackFramesPerSecond.value }
  )
  checkPlaybackFramesPerSecond.value = false
}
function stopClipPlayer() {
  csToolsUtilities.cine.stopClip(
    imageOperationStateStore.viewports[imageOperationStateStore.selectSeriesWindows].element
  )
  checkPlaybackFramesPerSecond.value = true
}
function changeClipPlayerValue() {
  if (!checkPlaybackFramesPerSecond.value) {
    csToolsUtilities.cine.playClip(
      imageOperationStateStore.viewports[imageOperationStateStore.selectSeriesWindows].element,
      { framesPerSecond: playbackFramesPerSecond.value }
    )
  }
}
watch(
  () => imageOperationStateStore.selectSeriesWindows,
  () => {
    if (imageOperationStateStore.viewports[imageOperationStateStore.selectSeriesWindows]) {
      let toolState = csToolsUtilities.cine.getToolState(
        imageOperationStateStore.viewports[imageOperationStateStore.selectSeriesWindows].element
      )
      if (!toolState) {
        checkPlaybackFramesPerSecond.value = true
      } else {
        checkPlaybackFramesPerSecond.value = false
      }
    }
  },
  {
    deep: true
  }
)

const segmentationToolsConfiguration = reactive({
  circularBrush: {
    radius: 10
  },
  circularEraser: {
    radius: 10
  },
  thresholdCircle: {
    radius: 10,
    threshold: [40, 100]
  },
  contourSegmentationTool: {
    outlineWidthActive: 2,
    outlineOpacity: 0.5,
    fillAlpha: 0.5,
    outlineDashActive: 0
  }
})

function changeBrushSizeForToolGroup(radius: number) {
  csToolsUtilities.segmentation.setBrushSizeForToolGroup(
    imageOperationStateStore.toolGroup.id,
    radius
  )
}

function changeBrushThresholdForToolGroup() {
  csToolsUtilities.segmentation.setBrushThresholdForToolGroup(
    imageOperationStateStore.toolGroup.id,
    segmentationToolsConfiguration.thresholdCircle.threshold as Types.Point2
  )
}

function changeThresholdCirclePresetValue(threshold: number[]) {
  segmentationToolsConfiguration.thresholdCircle.threshold[0] = threshold[0]
  segmentationToolsConfiguration.thresholdCircle.threshold[1] = threshold[1]
  changeBrushThresholdForToolGroup()
}

function updateSegmentationConfig(config: ContourConfig) {
  const segmentationRepresentation =
    segmentation.activeSegmentation.getActiveSegmentationRepresentation(
      imageOperationStateStore.toolGroup.id
    )
  const segmentationConfig = getSegmentationConfig(
    imageOperationStateStore.toolGroup.id,
    segmentationRepresentation.segmentationRepresentationUID
  )

  Object.assign(segmentationConfig.CONTOUR as ContourConfig, config)

  segmentation.config.setSegmentationRepresentationSpecificConfig(
    imageOperationStateStore.toolGroup.id,
    segmentationRepresentation.segmentationRepresentationUID,
    segmentationConfig
  )
}
function getSegmentationConfig(
  toolGroupdId: string,
  segmentationRepresentationUID: string
): cstTypes.RepresentationConfig {
  const segmentationConfig =
    segmentation.config.getSegmentationRepresentationSpecificConfig(
      toolGroupdId,
      segmentationRepresentationUID
    ) ?? {}

  // Add CONTOUR object because getSegmentationRepresentationSpecificConfig
  // can return an empty object
  if (!segmentationConfig.CONTOUR) {
    segmentationConfig.CONTOUR = {}
  }

  return segmentationConfig
}

const annotationToolsConfiguration = reactive({
  cardinalSplineROI: {
    resolution: 10,
    scale: 0.5
  },
  catmullRomSplineROI: {
    resolution: 10
  },
  bSplineROI: {
    resolution: 10
  }
})

function changeSplineResolutionForToolGroup(splineToolName: string, resolution: number) {
  const toolGroup = imageOperationStateStore.toolGroup
  const { splineType } = Splines1[splineToolName as keyof typeof Splines1]
  const splineConfig = toolGroup.getToolConfiguration(splineToolName, 'spline')
  splineConfig.configuration[splineType].resolution = resolution
  toolGroup.setToolConfiguration(splineToolName, { spline: splineConfig })
}

function changeSplineScaleForToolGroup(splineToolName: string, scale: number) {
  const toolGroup = imageOperationStateStore.toolGroup
  const { splineType } = Splines1[splineToolName as keyof typeof Splines1]
  const splineConfig = toolGroup.getToolConfiguration(splineToolName, 'spline')
  splineConfig.configuration[splineType].scale = scale
  toolGroup.setToolConfiguration(splineToolName, { spline: splineConfig })
}

function deleteSeriesListWindowsToSession(
  index: number,
  renderingEngineId: string,
  viewportId: string
) {
  const session = storageSession()
  const renderingEngine = getRenderingEngine(renderingEngineId)
  // const viewport = <Types.IStackViewport>renderingEngine!.getViewport(viewportId)
  if (session.getItem(seriesListWindowsSession)) {
    const list: (SeriesInfoWindows | 0)[] = session.getItem(seriesListWindowsSession)
    list[index] = 0
    session.setItem(seriesListWindowsSession, list)
    imageOperationStateStore.viewports[index] = 0 as any
    imageOperationStateStore.viewportColorbar[index].destroy()
    imageOperationStateStore.viewportColorbar[index] = 0 as any
    imageOperationStateStore.seriesListWindows[index] = 0
    renderingEngine!.disableElement(viewportId)
  }
}

function removeAllSeriesListWindowsToSession(renderingEngineId: string) {
  imageOperationStateStore.seriesListWindows.forEach((item, index) => {
    if (item != 0) {
      deleteSeriesListWindowsToSession(
        index,
        renderingEngineId,
        imageOperationStateStore.viewports[index].id
      )
    }
  })
}
</script>

<template>
  <div class="divide-x-0 divide-y-2 divide-slate-400/50 divide-solid">
    <div class="flex flex-wrap justify-between bg-stone-50 dark:border-gray-700 dark:bg-gray-800">
      <div class="flex items-center">
        <p class="inline ml-3 text-sm text-gray-600 dark:text-white">序列布局</p>
        <el-dropdown trigger="click">
          <el-button text size="small">
            <IconifyIconOffline
              class="hover:text-blue-500"
              :icon="arrowDown"
              :style="{ fontSize: '20px' }"
            ></IconifyIconOffline>
          </el-button>

          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item
                v-for="layout in layouts"
                :key="layout.label"
                @click="setLayout(layout.rows, layout.columns)"
              >
                {{ layout.label }}
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
      <div class="flex items-center">
        <p class="inline ml-3 text-sm text-gray-600 dark:text-white">视图状态</p>
        <el-dropdown trigger="click">
          <el-button text size="small">
            <IconifyIconOffline
              class="hover:text-blue-500"
              :icon="arrowDown"
              :style="{ fontSize: '20px' }"
            ></IconifyIconOffline>
          </el-button>

          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item
                @click="
                  resetToDefaultViewportProperties(
                    imageOperationStateStore.renderingEngine.id,
                    imageOperationStateStore.viewports[imageOperationStateStore.selectSeriesWindows]
                      .id
                  )
                "
                >重置视图默认属性</el-dropdown-item
              >
              <el-dropdown-item
                @click="
                  removeCurrentImageIdProperties(
                    imageOperationStateStore.renderingEngine.id,
                    imageOperationStateStore.viewports[imageOperationStateStore.selectSeriesWindows]
                      .id
                  )
                "
                >去除视图已有属性</el-dropdown-item
              >
              <el-dropdown-item
                @click="
                  setViewportColorbar(
                    'Grayscale',
                    imageOperationStateStore.renderingEngine.id,
                    imageOperationStateStore.viewports[imageOperationStateStore.selectSeriesWindows]
                      .id,
                    imageOperationStateStore.viewportColorbar[
                      imageOperationStateStore.selectSeriesWindows
                    ]
                  )
                "
                >重置视图默认彩条</el-dropdown-item
              >
              <el-dropdown-item
                @click="
                  resetToDefaultViewportProperties(
                    imageOperationStateStore.renderingEngine.id,
                    imageOperationStateStore.viewports[imageOperationStateStore.selectSeriesWindows]
                      .id
                  ),
                    setViewportColorbar(
                      'Grayscale',
                      imageOperationStateStore.renderingEngine.id,
                      imageOperationStateStore.viewports[
                        imageOperationStateStore.selectSeriesWindows
                      ].id,
                      imageOperationStateStore.viewportColorbar[
                        imageOperationStateStore.selectSeriesWindows
                      ]
                    )
                "
                >恢复视图初始状态</el-dropdown-item
              >
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>
    <div class="flex flex-wrap justify-between bg-stone-50 dark:border-gray-700 dark:bg-gray-800">
      <div class="flex items-center">
        <p class="inline ml-3 text-sm text-gray-600 dark:text-white">预设窗位值</p>
        <el-dropdown trigger="click">
          <el-button text size="small">
            <IconifyIconOffline
              class="hover:text-blue-500"
              :icon="arrowDown"
              :style="{ fontSize: '20px' }"
            ></IconifyIconOffline>
          </el-button>

          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item
                v-for="(windowLevelPresetValue, index) in windowLevelPresetValues"
                :key="index"
                @click="
                  setWindowLevel(
                    windowLevelPresetValue.window,
                    windowLevelPresetValue.level,
                    imageOperationStateStore.renderingEngine.id,
                    imageOperationStateStore.viewports[imageOperationStateStore.selectSeriesWindows]
                      .id
                  )
                "
              >
                {{ windowLevelPresetValue.label }}
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
      <div class="flex items-center">
        <p class="inline ml-3 text-sm text-gray-600 dark:text-white">伪彩色方案</p>
        <el-dropdown trigger="click" :max-height="200">
          <el-button text size="small">
            <IconifyIconOffline
              class="hover:text-blue-500"
              :icon="arrowDown"
              :style="{ fontSize: '20px' }"
            ></IconifyIconOffline>
          </el-button>

          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item
                v-for="(colormap, index) in colormaps"
                :key="index"
                @click="
                  setViewportColorbar(
                    colormap.Name,
                    imageOperationStateStore.renderingEngine.id,
                    imageOperationStateStore.viewports[imageOperationStateStore.selectSeriesWindows]
                      .id,
                    imageOperationStateStore.viewportColorbar[
                      imageOperationStateStore.selectSeriesWindows
                    ]
                  )
                "
                >{{ colormap.ColorSpace }} {{ colormap.Name }}</el-dropdown-item
              >
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>
    <div class="p-2 bg-stone-50 dark:border-gray-700 dark:bg-gray-800">
      <div
        class="flex items-center justify-between h-5 py-1 pl-2 bg-gray-100 rounded-md dark:bg-gray-700"
      >
        <a class="inline tracking-widest text-gray-600 text- dark:text-white">操作工具</a>
        <el-button text size="small" @click="operateTools = !operateTools">
          <IconifyIconOffline
            class="hover:text-blue-500"
            :icon="operateTools ? arrowUp : arrowDown"
            :style="{ fontSize: '20px' }"
          ></IconifyIconOffline>
        </el-button>
      </div>
      <div v-show="operateTools" class="flex flex-wrap gap-1 pt-1 pb-1">
        <imageOperation
          operation="镜像"
          @click="
            flipH(
              imageOperationStateStore.renderingEngine.id,
              imageOperationStateStore.viewports[imageOperationStateStore.selectSeriesWindows].id
            )
          "
        >
          <flip style="height: 30px; width: 30px"></flip
        ></imageOperation>
        <imageOperation
          operation="调窗"
          @click="imageOperationStateStore.bindLeftMouse(WindowLevelTool.toolName)"
        >
          <darkThemeFilled style="height: 30px; width: 30px"></darkThemeFilled>
        </imageOperation>
        <imageOperation
          operation="移动"
          @click="imageOperationStateStore.bindLeftMouse(PanTool.toolName)"
        >
          <dragOutlined style="height: 30px; width: 30px"></dragOutlined>
        </imageOperation>
        <div class="flex items-center h-16 bg-gray-100 rounded-lg w-14 dark:bg-gray-700">
          <div
            class="flex flex-col items-center justify-center w-10 h-16 rounded-sm cursor-pointer hover:bg-gray-300 dark:hover:bg-cyan-900"
            @click="imageOperationStateStore.bindLeftMouse(ZoomTool.toolName)"
          >
            <zoom style="height: 30px; width: 30px"></zoom>
            <span class="text-sm text-gray-500 dark:text-white">缩放</span>
          </div>
          <div class="flex flex-col items-center h-full text-lg font-extrabold">
            <div
              style="border-left-width: 1px; border-bottom-width: 1px"
              class="flex w-full h-8 border-0 border-solid rounded-sm cursor-pointer border-slate-300 hover:bg-gray-300 dark:hover:bg-cyan-900"
            >
              <a
                class="self-center"
                @click="
                  changeZoom(
                    imageOperationStateStore.renderingEngine.id,
                    imageOperationStateStore.viewports[imageOperationStateStore.selectSeriesWindows]
                      .id,
                    1.05
                  )
                "
                >+</a
              >
            </div>
            <div
              style="border-left-width: 1px; border-top-width: 1px"
              class="flex justify-center w-full h-8 border-0 border-solid rounded-sm cursor-pointer border-slate-300 hover:bg-gray-300 dark:hover:bg-cyan-900"
            >
              <a
                class="self-center"
                @click="
                  changeZoom(
                    imageOperationStateStore.renderingEngine.id,
                    imageOperationStateStore.viewports[imageOperationStateStore.selectSeriesWindows]
                      .id,
                    0.95
                  )
                "
                >-</a
              >
            </div>
          </div>
        </div>
        <imageOperation
          operation="旋转"
          @click="imageOperationStateStore.bindLeftMouse(PlanarRotateTool.toolName)"
        >
          <IconifyIconOffline
            :icon="refreshRight"
            :style="{ fontSize: '30px' }"
          ></IconifyIconOffline>
        </imageOperation>
        <imageOperation
          operation="反片"
          @click="
            invert(
              imageOperationStateStore.renderingEngine.id,
              imageOperationStateStore.viewports[imageOperationStateStore.selectSeriesWindows].id
            )
          "
        >
          <reverseOperationIn style="height: 30px; width: 30px"></reverseOperationIn>
        </imageOperation>
        <imageOperation operation="放大镜">
          <IconifyIconOffline
            @click="imageOperationStateStore.bindLeftMouse(MagnifyTool.toolName)"
            :icon="videoCamera"
            :style="{ fontSize: '30px' }"
          ></IconifyIconOffline>
        </imageOperation>
        <div class="flex items-center h-16 bg-gray-100 rounded-lg w-14 dark:bg-gray-700">
          <div
            class="flex flex-col items-center justify-center w-10 h-16 rounded-sm cursor-pointer hover:bg-gray-300 dark:hover:bg-cyan-900"
            @click="
              resetOriginal(
                imageOperationStateStore.renderingEngine.id,
                imageOperationStateStore.viewports[imageOperationStateStore.selectSeriesWindows].id
              )
            "
          >
            <resetWrench style="height: 30px; width: 30px"></resetWrench>
            <span class="text-sm text-gray-500 dark:text-white">重置</span>
          </div>
          <div
            style="border-left-width: 1px"
            class="flex items-center h-full text-lg font-extrabold border-0 border-solid rounded-sm cursor-pointer hover:bg-gray-300 border-slate-300"
          >
            <el-dropdown trigger="click">
              <IconifyIconOffline
                class="hover:text-blue-500"
                :icon="arrowDown"
                :style="{ fontSize: '15px' }"
              ></IconifyIconOffline>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item
                    @click="
                      resetPan(
                        imageOperationStateStore.renderingEngine.id,
                        imageOperationStateStore.viewports[
                          imageOperationStateStore.selectSeriesWindows
                        ].id
                      )
                    "
                    >重置移动</el-dropdown-item
                  >
                  <el-dropdown-item
                    @click="
                      resetZoom(
                        imageOperationStateStore.renderingEngine.id,
                        imageOperationStateStore.viewports[
                          imageOperationStateStore.selectSeriesWindows
                        ].id
                      )
                    "
                    >重置大小</el-dropdown-item
                  >
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </div>
    </div>

    <div class="flex flex-col gap-px h-15 bg-stone-50 dark:border-gray-700 dark:bg-gray-800">
      <div class="flex items-center justify-center pt-2">
        <el-button-group>
          <el-button text size="small"
            ><playerStart
              style="height: 25px; width: 25px"
              @click="
                changePreviousImage(
                  imageOperationStateStore.renderingEngine.id,
                  imageOperationStateStore.viewports[imageOperationStateStore.selectSeriesWindows]
                    .id
                )
              "
            ></playerStart
          ></el-button>
          <el-button text size="small"
            ><playerPlay
              v-show="checkPlaybackFramesPerSecond"
              style="height: 25px; width: 25px"
              @click="playClipPlayer()"
            ></playerPlay
            ><playerPause
              v-show="!checkPlaybackFramesPerSecond"
              style="height: 25px; width: 25px"
              @click="stopClipPlayer()"
            ></playerPause>
          </el-button>

          <el-button text size="small"
            ><playerEnd
              style="height: 25px; width: 25px"
              @click="
                changeNextImage(
                  imageOperationStateStore.renderingEngine.id,
                  imageOperationStateStore.viewports[imageOperationStateStore.selectSeriesWindows]
                    .id
                )
              "
            ></playerEnd
          ></el-button>
        </el-button-group>
        <el-dropdown>
          <div
            class="flex items-center w-20 rounded-md justify-evenly bg-stone-200 dark:bg-gray-700"
          >
            <span class="flex items-center justify-center h-6">{{ playbackFramesPerSecond }}</span>

            <IconifyIconOffline
              class="hover:text-blue-500"
              :icon="arrowDown"
              :style="{ fontSize: '15px' }"
            ></IconifyIconOffline>
          </div>

          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click=";(playbackFramesPerSecond = 5), changeClipPlayerValue()"
                >5</el-dropdown-item
              >
              <el-dropdown-item @click=";(playbackFramesPerSecond = 10), changeClipPlayerValue()"
                >10</el-dropdown-item
              >
              <el-dropdown-item @click=";(playbackFramesPerSecond = 15), changeClipPlayerValue()"
                >15</el-dropdown-item
              >
              <el-dropdown-item @click=";(playbackFramesPerSecond = 20), changeClipPlayerValue()"
                >20</el-dropdown-item
              >
              <el-dropdown-item @click=";(playbackFramesPerSecond = 25), changeClipPlayerValue()"
                >25</el-dropdown-item
              >
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
      <div class="mx-4">
        <el-slider
          v-model="playbackFramesPerSecond"
          :max="30"
          :min="1"
          placement="bottom"
          size="small"
          @change="changeClipPlayerValue()"
        />
      </div>
    </div>

    <div class="p-2 bg-stone-50 dark:border-gray-700 dark:bg-gray-800">
      <div
        class="flex items-center justify-between h-5 py-1 pl-2 bg-gray-100 rounded-md dark:bg-gray-700"
      >
        <a class="inline text-base tracking-widest text-gray-600 dark:text-white">注释工具</a>
        <el-button text size="small" @click="annotationTools = !annotationTools">
          <IconifyIconOffline
            class="hover:text-blue-500"
            :icon="annotationTools ? arrowUp : arrowDown"
            :style="{ fontSize: '20px' }"
          ></IconifyIconOffline>
        </el-button>
      </div>
      <div
        v-show="annotationTools"
        class="flex flex-col gap-1 divide-x-0 divide-y-2 divide-slate-400/30 divide-dashed"
      >
        <div class="flex flex-wrap gap-1 pt-1 pb-1">
          <imageOperation
            operation="箭头"
            @click="imageOperationStateStore.bindLeftMouse(ArrowAnnotateTool.toolName)"
            ><arrowUpDuotone style="height: 30px; width: 30px"></arrowUpDuotone>
          </imageOperation>
          <imageOperation
            operation="直线"
            @click="imageOperationStateStore.bindLeftMouse(LengthTool.toolName)"
          >
            <straightPipe style="height: 30px; width: 30px"></straightPipe>
          </imageOperation>

          <imageOperation
            operation="十字线"
            @click="imageOperationStateStore.bindLeftMouse(BidirectionalTool.toolName)"
          >
            <addFill style="height: 30px; width: 30px"></addFill>
          </imageOperation>
          <imageOperation
            operation="角度"
            @click="imageOperationStateStore.bindLeftMouse(AngleTool.toolName)"
          >
            <angle style="height: 30px; width: 30px"></angle>
          </imageOperation>
          <imageOperation
            operation="Cobb"
            @click="imageOperationStateStore.bindLeftMouse(CobbAngleTool.toolName)"
          >
            <cobb style="height: 30px; width: 30px; fill: currentColor"></cobb>
          </imageOperation>

          <imageOperation
            operation="椭圆"
            @click="imageOperationStateStore.bindLeftMouse(EllipticalROITool.toolName)"
          >
            <ellipses style="height: 30px; width: 30px; fill: currentColor"></ellipses>
          </imageOperation>
          <imageOperation
            operation="圆形"
            @click="imageOperationStateStore.bindLeftMouse(CircleROITool.toolName)"
          >
            <circleEmpty style="height: 30px; width: 30px; fill: currentColor"></circleEmpty>
          </imageOperation>
          <imageOperation
            operation="矩形"
            @click="imageOperationStateStore.bindLeftMouse(RectangleROITool.toolName)"
          >
            <rectangles style="height: 30px; width: 30px; fill: currentColor"></rectangles>
          </imageOperation>

          
          <imageOperation
            operation="探测"
            @click="imageOperationStateStore.bindLeftMouse(ProbeTool.toolName)"
          >
            <circleCenter style="height: 30px; width: 30px; fill: currentColor"></circleCenter>
          </imageOperation>
          <imageOperation
            operation="超声定向"
            @click="imageOperationStateStore.bindLeftMouse(UltrasoundDirectionalTool.toolName)"
          >
            <circleTwoLine style="height: 30px; width: 30px; fill: currentColor"></circleTwoLine
          ></imageOperation>
          <imageOperation
            operation="平面手绘"
            @click="imageOperationStateStore.bindLeftMouse(PlanarFreehandROITool.toolName)"
          >
            <pencil style="height: 30px; width: 30px; fill: currentColor"></pencil>
          </imageOperation>
          
          <imageOperation
            operation="动态轮廓"
            @click="imageOperationStateStore.bindLeftMouse(LivewireContourTool.toolName)"
          >
            <livewireContour style="height: 30px; width: 30px; fill: currentColor"></livewireContour
          ></imageOperation>
          <div class="flex items-center h-16 bg-gray-100 rounded-lg w-14 dark:bg-gray-700">
            <div
              class="flex flex-col items-center justify-center w-10 h-16 rounded-sm cursor-pointer hover:bg-gray-300 dark:hover:bg-cyan-900"
              @click="imageOperationStateStore.bindLeftMouse(splineAnnotationToolsNames[0])"
            >
              <splineROI style="height: 30px; width: 30px; fill: currentColor"></splineROI>
              <span class="text-sm text-gray-500 dark:text-white">样条</span>
            </div>
            <div
              style="border-left-width: 1px"
              class="flex items-center h-full text-lg font-extrabold border-0 border-solid rounded-sm cursor-pointer hover:bg-gray-300 border-slate-300"
            >
              <el-dropdown trigger="click">
                <IconifyIconOffline
                  class="hover:text-blue-500"
                  :icon="arrowDown"
                  :style="{ fontSize: '15px' }"
                ></IconifyIconOffline>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item
                      v-for="splineAnnotationToolsName in splineAnnotationToolsNames"
                      @click="imageOperationStateStore.bindLeftMouse(splineAnnotationToolsName)"
                      :key="splineAnnotationToolsName"
                      >{{ splineAnnotationToolsName }}</el-dropdown-item
                    >
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </div>
          </div>
          <div class="flex items-center h-16 bg-gray-100 rounded-lg w-14 dark:bg-gray-700">
            <div
              class="flex flex-col items-center justify-center w-10 h-16 rounded-sm cursor-pointer hover:bg-gray-300 dark:hover:bg-cyan-900"
              @click="
                selectAnnotationsByToolName(
                  imageOperationStateStore.leftMouseActive,
                  imageOperationStateStore.renderingEngine.id,
                  imageOperationStateStore.viewports[imageOperationStateStore.selectSeriesWindows]
                    .id
                )
              "
            >
              <selectToolname style="height: 30px; width: 30px"></selectToolname>
              <span class="text-sm text-gray-500 dark:text-white">选取</span>
            </div>
            <div
              style="border-left-width: 1px"
              class="flex items-center h-full text-lg font-extrabold border-0 border-solid rounded-sm cursor-pointer hover:bg-gray-300 border-slate-300"
            >
              <el-dropdown trigger="click">
                <IconifyIconOffline
                  class="hover:text-blue-500"
                  :icon="arrowDown"
                  :style="{ fontSize: '15px' }"
                ></IconifyIconOffline>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item
                      v-for="annotationTools in annotationToolsList"
                      @click="
                        selectAnnotationsByToolName(
                          annotationTools.toolName,
                          imageOperationStateStore.renderingEngine.id,
                          imageOperationStateStore.viewports[
                            imageOperationStateStore.selectSeriesWindows
                          ].id
                        )
                      "
                      :key="annotationTools.label"
                      >{{ annotationTools.label }}</el-dropdown-item
                    >
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </div>
          </div>
        </div>
        <div class="flex flex-wrap gap-1 pt-1 pb-1">
          <imageOperationText
            operation="隐藏"
            @click="
              hideSelectedAnnotation(
                imageOperationStateStore.renderingEngine.id,
                imageOperationStateStore.viewports[imageOperationStateStore.selectSeriesWindows].id
              )
            "
          ></imageOperationText>
          <imageOperationText
            operation="显示"
            @click="
              showAllAnnotations(
                imageOperationStateStore.renderingEngine.id,
                imageOperationStateStore.viewports[imageOperationStateStore.selectSeriesWindows].id
              )
            "
          ></imageOperationText>
          <imageOperationText
            operation="锁定"
            @click="
              lockSelectedAnnotation(
                imageOperationStateStore.renderingEngine.id,
                imageOperationStateStore.viewports[imageOperationStateStore.selectSeriesWindows].id
              )
            "
          ></imageOperationText>
          <imageOperationText operation="解锁" @click="unlockAllAnnotations()"></imageOperationText>
          <imageOperationText
            operation="删除"
            @click="
              removeAnnotation(
                imageOperationStateStore.renderingEngine.id,
                imageOperationStateStore.viewports[imageOperationStateStore.selectSeriesWindows].id
              )
            "
          ></imageOperationText>
          <imageOperationText
            operation="全选"
            @click="
              selectAllAnnotations(
                imageOperationStateStore.renderingEngine.id,
                imageOperationStateStore.viewports[imageOperationStateStore.selectSeriesWindows].id
              )
            "
          ></imageOperationText>
          <imageOperationText
            operation="反选"
            @click="
              reverseSelectionAnnotations(
                imageOperationStateStore.renderingEngine.id,
                imageOperationStateStore.viewports[imageOperationStateStore.selectSeriesWindows].id
              )
            "
          ></imageOperationText>
          <imageOperationText
            operation="撤销"
            @click="
              revokePreviousAnnotation(
                imageOperationStateStore.renderingEngine.id,
                imageOperationStateStore.viewports[imageOperationStateStore.selectSeriesWindows].id
              )
            "
          ></imageOperationText>
        </div>
        <div
          class="px-2 pt-1 pb-1"
          v-show="imageOperationStateStore.leftMouseActive == splineAnnotationToolsNames[0]"
        >
          <div class="flex flex-col">
            <div class="flex justify-between">
              <el-text>Cardinal Resolution</el-text
              ><el-input-number
                :min="1"
                :max="10"
                @change="
                  changeSplineResolutionForToolGroup(
                    splineAnnotationToolsNames[0],
                    annotationToolsConfiguration.cardinalSplineROI.resolution
                  )
                "
                :controls="false"
                v-model="annotationToolsConfiguration.cardinalSplineROI.resolution"
                style="width: 45px; height: 20px"
              />
            </div>
            <div>
              <el-slider
                :min="1"
                :max="10"
                @change="
                  changeSplineResolutionForToolGroup(
                    splineAnnotationToolsNames[0],
                    annotationToolsConfiguration.cardinalSplineROI.resolution
                  )
                "
                v-model="annotationToolsConfiguration.cardinalSplineROI.resolution"
              />
            </div>
          </div>
          <div class="flex flex-col">
            <div class="flex justify-between">
              <el-text>Cardinal Scale</el-text
              ><el-input-number
                :min="0"
                :max="1"
                :step="0.01"
                @change="
                  changeSplineScaleForToolGroup(
                    splineAnnotationToolsNames[0],
                    annotationToolsConfiguration.cardinalSplineROI.scale
                  )
                "
                :controls="false"
                v-model="annotationToolsConfiguration.cardinalSplineROI.scale"
                style="width: 60px; height: 20px"
              />
            </div>
            <div>
              <el-slider
                :min="0"
                :max="1"
                :step="0.01"
                @change="
                  changeSplineScaleForToolGroup(
                    splineAnnotationToolsNames[0],
                    annotationToolsConfiguration.cardinalSplineROI.scale
                  )
                "
                v-model="annotationToolsConfiguration.cardinalSplineROI.scale"
              />
            </div>
          </div>
        </div>
        <div
          class="px-2 pt-1 pb-1"
          v-show="imageOperationStateStore.leftMouseActive == splineAnnotationToolsNames[1]"
        >
          <div class="flex flex-col">
            <div class="flex justify-between">
              <el-text>CatmullRom Resolution</el-text
              ><el-input-number
                :min="1"
                :max="10"
                @change="
                  changeSplineResolutionForToolGroup(
                    splineAnnotationToolsNames[1],
                    annotationToolsConfiguration.catmullRomSplineROI.resolution
                  )
                "
                :controls="false"
                v-model="annotationToolsConfiguration.catmullRomSplineROI.resolution"
                style="width: 45px; height: 20px"
              />
            </div>
            <div>
              <el-slider
                :min="1"
                :max="10"
                @change="
                  changeSplineResolutionForToolGroup(
                    splineAnnotationToolsNames[1],
                    annotationToolsConfiguration.catmullRomSplineROI.resolution
                  )
                "
                v-model="annotationToolsConfiguration.catmullRomSplineROI.resolution"
              />
            </div>
          </div>
        </div>
        <div
          class="px-2 pt-1 pb-1"
          v-show="imageOperationStateStore.leftMouseActive == splineAnnotationToolsNames[3]"
        >
          <div class="flex flex-col">
            <div class="flex justify-between">
              <el-text>bSplineROI resolution</el-text
              ><el-input-number
                :min="1"
                :max="10"
                @change="
                  changeSplineResolutionForToolGroup(
                    splineAnnotationToolsNames[3],
                    annotationToolsConfiguration.bSplineROI.resolution
                  )
                "
                :controls="false"
                v-model="annotationToolsConfiguration.bSplineROI.resolution"
                style="width: 45px; height: 20px"
              />
            </div>
            <div>
              <el-slider
                :min="1"
                :max="10"
                @change="
                  changeSplineResolutionForToolGroup(
                    splineAnnotationToolsNames[3],
                    annotationToolsConfiguration.bSplineROI.resolution
                  )
                "
                v-model="annotationToolsConfiguration.bSplineROI.resolution"
              />
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="p-2 bg-stone-50 dark:bg-gray-800">
      <div
        class="flex items-center justify-between h-5 py-1 pl-2 bg-gray-100 rounded-md dark:bg-gray-700"
      >
        <a class="inline text-base tracking-widest text-gray-600 dark:text-white">分割工具</a>
        <el-button text size="small" @click="segmentationTools = !segmentationTools">
          <IconifyIconOffline
            class="hover:text-blue-500"
            :icon="segmentationTools ? arrowUp : arrowDown"
            :style="{ fontSize: '20px' }"
          ></IconifyIconOffline>
        </el-button>
      </div>
      <div
        v-show="segmentationTools"
        class="flex flex-col gap-1 divide-x-0 divide-y-2 divide-slate-400/30 divide-dashed"
      >
        <div class="flex flex-wrap gap-1 pt-1 pb-1">
          <imageOperation
            operation="矩形分割"
            @click="imageOperationStateStore.bindLeftMouse(RectangleScissorsTool.toolName)"
          >
            <rectangleScissor
              style="height: 30px; width: 30px; fill: currentColor"
            ></rectangleScissor>
          </imageOperation>
          <imageOperation
            operation="圆形分割"
            @click="imageOperationStateStore.bindLeftMouse(CircleScissorsTool.toolName)"
          >
            <circleScissor style="height: 30px; width: 30px; fill: currentColor"></circleScissor>
          </imageOperation>
          <imageOperation
            operation="圆形擦除"
            @click="imageOperationStateStore.bindLeftMouse('ScissorsEraser')"
          >
            <scissorsEraser style="height: 30px; width: 30px; fill: currentColor"></scissorsEraser>
          </imageOperation>
          <imageOperation
            operation="填充"
            @click="imageOperationStateStore.bindLeftMouse(PaintFillTool.toolName)"
          >
            <paintFill style="height: 30px; width: 30px; fill: currentColor"></paintFill>
          </imageOperation>

          <imageOperation
            operation="毛刷"
            @click="imageOperationStateStore.bindLeftMouse('CircularBrush')"
            @mouseup="
              changeBrushSizeForToolGroup(segmentationToolsConfiguration.circularBrush.radius)
            "
          >
            <circularBrush style="height: 30px; width: 30px; fill: currentColor"></circularBrush>
          </imageOperation>

          <imageOperation
            operation="橡皮"
            @click="imageOperationStateStore.bindLeftMouse('CircularEraser')"
            @mouseup="
              changeBrushSizeForToolGroup(segmentationToolsConfiguration.circularEraser.radius)
            "
          >
            <circularEraser style="height: 30px; width: 30px; fill: currentColor"></circularEraser>
          </imageOperation>

          <imageOperation
            operation="阈值"
            @click="imageOperationStateStore.bindLeftMouse('ThresholdCircle')"
            @mouseup="
              changeBrushSizeForToolGroup(segmentationToolsConfiguration.thresholdCircle.radius)
            "
          >
            <thresholdCircle
              style="height: 30px; width: 30px; fill: currentColor"
            ></thresholdCircle>
          </imageOperation>

          <imageOperation
            operation="动态轮廓"
            @click="
              imageOperationStateStore.bindLeftMouse(LivewireContourSegmentationTool.toolName)
            "
          >
            <livewireSegmentation
              style="height: 30px; width: 30px; fill: currentColor"
            ></livewireSegmentation>
          </imageOperation>

          <div class="flex items-center h-16 bg-gray-100 rounded-lg w-14 dark:bg-gray-700">
            <div
              class="flex flex-col items-center justify-center w-10 h-16 rounded-sm cursor-pointer hover:bg-gray-300 dark:hover:bg-cyan-900"
              @click="imageOperationStateStore.bindLeftMouse(splineToolsNames[0])"
            >
              <splineSegmentation
                style="height: 30px; width: 30px; fill: currentColor"
              ></splineSegmentation>
              <span class="text-sm text-gray-500 dark:text-white">样条</span>
            </div>
            <div
              style="border-left-width: 1px"
              class="flex items-center h-full text-lg font-extrabold border-0 border-solid rounded-sm cursor-pointer hover:bg-gray-300 border-slate-300"
            >
              <el-dropdown trigger="click">
                <IconifyIconOffline
                  class="hover:text-blue-500"
                  :icon="arrowDown"
                  :style="{ fontSize: '15px' }"
                ></IconifyIconOffline>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item
                      v-for="splineToolsName in splineToolsNames"
                      @click="imageOperationStateStore.bindLeftMouse(splineToolsName)"
                      :key="splineToolsName"
                      >{{ splineToolsName }}</el-dropdown-item
                    >
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </div>
          </div>
          <imageOperation
            operation="手绘轮廓"
            @click="
              imageOperationStateStore.bindLeftMouse(PlanarFreehandContourSegmentationTool.toolName)
            "
          >
            <planarFreehandSegmentation
              style="height: 30px; width: 30px; fill: currentColor"
            ></planarFreehandSegmentation>
          </imageOperation>
        </div>
        <div
          class="px-2 pt-1 pb-1"
          v-show="imageOperationStateStore.leftMouseActive == 'CircularBrush'"
        >
          <div class="flex justify-between">
            <el-text>毛刷半径(mm)</el-text
            ><el-input-number
              :min="1"
              :max="100"
              @change="
                changeBrushSizeForToolGroup(segmentationToolsConfiguration.circularBrush.radius)
              "
              :controls="false"
              v-model="segmentationToolsConfiguration.circularBrush.radius"
              style="width: 45px; height: 20px"
            />
          </div>
          <div>
            <el-slider
              @change="
                changeBrushSizeForToolGroup(segmentationToolsConfiguration.circularBrush.radius)
              "
              v-model="segmentationToolsConfiguration.circularBrush.radius"
            />
          </div>
        </div>
        <div
          class="px-2 pt-1 pb-1"
          v-show="imageOperationStateStore.leftMouseActive == 'CircularEraser'"
        >
          <div class="flex justify-between">
            <el-text>橡皮半径(mm)</el-text
            ><el-input-number
              :min="1"
              :max="100"
              @change="
                changeBrushSizeForToolGroup(segmentationToolsConfiguration.circularEraser.radius)
              "
              :controls="false"
              v-model="segmentationToolsConfiguration.circularEraser.radius"
              style="width: 45px; height: 20px"
            />
          </div>
          <div>
            <el-slider
              @change="
                changeBrushSizeForToolGroup(segmentationToolsConfiguration.circularEraser.radius)
              "
              v-model="segmentationToolsConfiguration.circularEraser.radius"
            />
          </div>
        </div>
        <div
          class="px-2 pt-1 pb-1"
          v-show="imageOperationStateStore.leftMouseActive == 'ThresholdCircle'"
        >
          <div class="flex justify-between">
            <el-text>阈值半径(mm)</el-text
            ><el-input-number
              :min="1"
              :max="100"
              @change="
                changeBrushSizeForToolGroup(segmentationToolsConfiguration.thresholdCircle.radius)
              "
              :controls="false"
              v-model="segmentationToolsConfiguration.thresholdCircle.radius"
              style="width: 45px; height: 20px"
            />
          </div>
          <div>
            <el-slider
              @change="
                changeBrushSizeForToolGroup(segmentationToolsConfiguration.thresholdCircle.radius)
              "
              v-model="segmentationToolsConfiguration.thresholdCircle.radius"
            />
          </div>
          <div class="flex justify-between">
            <el-text>阈值</el-text>
            <el-dropdown trigger="click">
              <el-button text size="small">
                <IconifyIconOffline
                  class="hover:text-blue-500"
                  :icon="arrowDown"
                  :style="{ fontSize: '20px' }"
                ></IconifyIconOffline>
              </el-button>

              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item
                    v-for="(thresholdCirclePresetValue, index) in thresholdCirclePresetValues"
                    :key="index"
                    @click="changeThresholdCirclePresetValue(thresholdCirclePresetValue.threshold)"
                  >
                    {{ thresholdCirclePresetValue.label }}
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
            <div class="flex gap-1">
              <el-input-number
                :min="-1000"
                :max="1000"
                @change="changeBrushThresholdForToolGroup()"
                :controls="false"
                v-model="segmentationToolsConfiguration.thresholdCircle.threshold[0]"
                style="width: 65px; height: 20px"
              />
              <el-input-number
                :min="-1000"
                :max="1000"
                @change="changeBrushThresholdForToolGroup()"
                :controls="false"
                v-model="segmentationToolsConfiguration.thresholdCircle.threshold[1]"
                style="width: 60px; height: 20px"
              />
            </div>
          </div>
          <div>
            <el-slider
              range
              @change="changeBrushThresholdForToolGroup()"
              v-model="segmentationToolsConfiguration.thresholdCircle.threshold"
              :min="-1000"
              :max="1000"
            />
          </div>
        </div>
        <div
          class="px-2 pt-1 pb-1"
          v-show="
            imageOperationStateStore.leftMouseActive == LivewireContourSegmentationTool.toolName ||
            imageOperationStateStore.leftMouseActive ==
              PlanarFreehandContourSegmentationTool.toolName ||
            splineToolsNames.includes(imageOperationStateStore.leftMouseActive)
          "
        >
          <div class="flex flex-col">
            <div class="flex justify-between">
              <el-text>轮廓厚度</el-text
              ><el-input-number
                :min="1"
                :max="20"
                @change="
                  updateSegmentationConfig({
                    outlineWidthActive: Number(
                      segmentationToolsConfiguration.contourSegmentationTool.outlineWidthActive
                    )
                  })
                "
                :controls="false"
                v-model="segmentationToolsConfiguration.contourSegmentationTool.outlineWidthActive"
                style="width: 45px; height: 20px"
              />
            </div>
            <div>
              <el-slider
                :min="1"
                :max="20"
                @change="
                  updateSegmentationConfig({
                    outlineWidthActive: Number(
                      segmentationToolsConfiguration.contourSegmentationTool.outlineWidthActive
                    )
                  })
                "
                v-model="segmentationToolsConfiguration.contourSegmentationTool.outlineWidthActive"
              />
            </div>
          </div>
          <div class="flex flex-col">
            <div class="flex justify-between">
              <el-text>轮廓透明度</el-text
              ><el-input-number
                :min="0"
                :max="1"
                @change="
                  updateSegmentationConfig({
                    outlineOpacity: Number(
                      segmentationToolsConfiguration.contourSegmentationTool.outlineOpacity
                    )
                  })
                "
                :controls="false"
                v-model="segmentationToolsConfiguration.contourSegmentationTool.outlineOpacity"
                style="width: 50px; height: 20px"
              />
            </div>
            <div>
              <el-slider
                :min="0"
                :max="1"
                :step="0.01"
                @change="
                  updateSegmentationConfig({
                    outlineOpacity: Number(
                      segmentationToolsConfiguration.contourSegmentationTool.outlineOpacity
                    )
                  })
                "
                v-model="segmentationToolsConfiguration.contourSegmentationTool.outlineOpacity"
              />
            </div>
          </div>
          <div class="flex flex-col">
            <div class="flex justify-between">
              <el-text>轮廓虚线</el-text
              ><el-input-number
                :min="0"
                :max="20"
                @change="
                  updateSegmentationConfig({
                    outlineDashActive: String(
                      segmentationToolsConfiguration.contourSegmentationTool.outlineDashActive
                    )
                  })
                "
                :controls="false"
                v-model="segmentationToolsConfiguration.contourSegmentationTool.outlineDashActive"
                style="width: 50px; height: 20px"
              />
            </div>
            <div>
              <el-slider
                :min="0"
                :max="20"
                :step="1"
                @change="
                  updateSegmentationConfig({
                    outlineDashActive: String(
                      segmentationToolsConfiguration.contourSegmentationTool.outlineDashActive
                    )
                  })
                "
                v-model="segmentationToolsConfiguration.contourSegmentationTool.outlineDashActive"
              />
            </div>
          </div>
          <div class="flex flex-col">
            <div class="flex justify-between">
              <el-text>覆盖Alpha</el-text
              ><el-input-number
                :min="0"
                :max="1"
                @change="
                  updateSegmentationConfig({
                    fillAlpha: Number(
                      segmentationToolsConfiguration.contourSegmentationTool.fillAlpha
                    )
                  })
                "
                :controls="false"
                v-model="segmentationToolsConfiguration.contourSegmentationTool.fillAlpha"
                style="width: 50px; height: 20px"
              />
            </div>
            <div>
              <el-slider
                :min="0"
                :max="1"
                :step="0.01"
                @change="
                  updateSegmentationConfig({
                    fillAlpha: Number(
                      segmentationToolsConfiguration.contourSegmentationTool.fillAlpha
                    )
                  })
                "
                v-model="segmentationToolsConfiguration.contourSegmentationTool.fillAlpha"
              />
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="p-2 bg-stone-50 dark:bg-gray-800">
      <div
        class="flex items-center justify-between h-5 py-1 pl-2 bg-gray-100 rounded-md dark:bg-gray-700"
      >
        <a class="inline text-base tracking-widest text-gray-600 dark:text-white">其他</a>

        <el-button text size="small" @click="otherTools = !otherTools">
          <IconifyIconOffline
            class="hover:text-blue-500"
            :icon="otherTools ? arrowUp : arrowDown"
            :style="{ fontSize: '20px' }"
          ></IconifyIconOffline>
        </el-button>
      </div>
      <div v-show="otherTools" class="flex flex-wrap gap-1 pt-1 pb-1">
        <imageOperation
          operation="截屏"
          @click="
            downloadCanvasAsImage(
              `div[data-viewport-uid='stackViewPort${imageOperationStateStore.selectSeriesWindows}']`,
              `image${imageOperationStateStore.selectSeriesWindows}`
            )
          "
        >
          <captureImage style="height: 30px; width: 30px; fill: currentColor"></captureImage>
        </imageOperation>
        <imageOperation operation="删除视图">
          <remove
            style="height: 30px; width: 30px; fill: currentColor"
            @click="
              deleteSeriesListWindowsToSession(
                imageOperationStateStore.selectSeriesWindows,
                imageOperationStateStore.renderingEngine.id,
                imageOperationStateStore.viewports[imageOperationStateStore.selectSeriesWindows].id
              )
            "
          ></remove>
        </imageOperation>
        <imageOperation
          operation="清空视图"
          @click="removeAllSeriesListWindowsToSession(imageOperationStateStore.renderingEngine.id)"
        >
          <removals style="height: 30px; width: 30px; fill: currentColor"></removals>
        </imageOperation>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped></style>
