import * as cornerstoneTools from '@cornerstonejs/tools'
import type IToolGroup from '@cornerstonejs/tools/src/types/IToolGroup'
import {useImageOperationStateStore } from '@/store/imageOperationState'
import { volumeLoader } from '@cornerstonejs/core'

const imageOperationStateStore =useImageOperationStateStore()
const {
  Synchronizer,
  segmentation,
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

  ToolGroupManager,
  Enums: csToolsEnums,
  utilities: cstUtils
} = cornerstoneTools
const { segmentation: segmentationUtils } = cstUtils
const { MouseBindings } = csToolsEnums
const brushInstanceNames = {
  CircularBrush: 'CircularBrush',
  CircularEraser: 'CircularEraser',
  SphereBrush: 'SphereBrush',
  SphereEraser: 'SphereEraser',
  ThresholdCircle: 'ThresholdCircle',
  ThresholdSphere: 'ThresholdSphere',
  DynamicThreshold: 'DynamicThreshold',
  ScissorsEraser: 'ScissorsEraser'
}

const brushStrategies = {
  [brushInstanceNames.CircularBrush]: 'FILL_INSIDE_CIRCLE',
  [brushInstanceNames.CircularEraser]: 'ERASE_INSIDE_CIRCLE',
  [brushInstanceNames.SphereBrush]: 'FILL_INSIDE_SPHERE',
  [brushInstanceNames.SphereEraser]: 'ERASE_INSIDE_SPHERE',
  [brushInstanceNames.ThresholdCircle]: 'THRESHOLD_INSIDE_CIRCLE',
  [brushInstanceNames.ThresholdSphere]: 'THRESHOLD_INSIDE_SPHERE',
  [brushInstanceNames.DynamicThreshold]: 'THRESHOLD_INSIDE_CIRCLE',
  [brushInstanceNames.ScissorsEraser]: 'ERASE_INSIDE'
}

async function addSegmentationsToState(volumeId: string, segmentationId: string) {
  // Create a segmentation of the same resolution as the source data
  // using volumeLoader.createAndCacheDerivedVolume.
  await volumeLoader.createAndCacheDerivedVolume(volumeId, {
    volumeId: segmentationId
  })

  // Add the segmentations to state
  segmentation.addSegmentations([
    {
      segmentationId,
      representation: {
        // The type of segmentation
        type: csToolsEnums.SegmentationRepresentations.Labelmap,
        // The actual segmentation data, in the case of labelmap this is a
        // reference to the source volume of the segmentation.
        data: {
          volumeId: segmentationId
        }
      }
    }
  ])
}

async function createTools(segmentationId: string = '', volumeId: string = '') {
  try {
    // 尝试执行的代码块
    // 如果这里的代码抛出了异常，控制流将转到 catch 块
    cornerstoneTools.addTool(ReferenceLines)
  } catch (error) {
    // 当 try 块中的代码抛出异常时，将执行此块
    return;
  }
  // Add tools to Cornerstone3D
  // cornerstoneTools.addTool(AnnotationDisplayTool)
  // cornerstoneTools.addTool(ReferenceCursors);
  
  // cornerstoneTools.addTool(ScaleOverlayTool);

  cornerstoneTools.addTool(LengthTool)
  cornerstoneTools.addTool(ProbeTool)
  cornerstoneTools.addTool(RectangleROITool)
  cornerstoneTools.addTool(RectangleROIStartEndThresholdTool)
  cornerstoneTools.addTool(RectangleROIThresholdTool)
  cornerstoneTools.addTool(EllipticalROITool)
  cornerstoneTools.addTool(CircleROITool)
  cornerstoneTools.addTool(BidirectionalTool)
  cornerstoneTools.addTool(AngleTool)
  cornerstoneTools.addTool(CobbAngleTool)
  cornerstoneTools.addTool(ArrowAnnotateTool)
  cornerstoneTools.addTool(CrosshairsTool)
  cornerstoneTools.addTool(PlanarFreehandROITool)

  cornerstoneTools.addTool(PanTool)
  cornerstoneTools.addTool(WindowLevelTool)
  cornerstoneTools.addTool(StackScrollMouseWheelTool)
  cornerstoneTools.addTool(ZoomTool)
  cornerstoneTools.addTool(PlanarRotateTool)

  cornerstoneTools.addTool(BrushTool)
  cornerstoneTools.addTool(CircleScissorsTool)
  cornerstoneTools.addTool(MIPJumpToClickTool)
  cornerstoneTools.addTool(MagnifyTool)
  cornerstoneTools.addTool(PaintFillTool)
  cornerstoneTools.addTool(RectangleScissorsTool)
  cornerstoneTools.addTool(SegmentationDisplayTool)
  cornerstoneTools.addTool(SphereScissorsTool)
  cornerstoneTools.addTool(StackScrollTool)
  cornerstoneTools.addTool(TrackballRotateTool)
  cornerstoneTools.addTool(VolumeRotateMouseWheelTool)
  cornerstoneTools.addTool(UltrasoundDirectionalTool)
  cornerstoneTools.addTool(LivewireContourTool)

  cornerstoneTools.addTool(PlanarFreehandContourSegmentationTool)
  cornerstoneTools.addTool(LivewireContourSegmentationTool)
  cornerstoneTools.addTool(SplineContourSegmentationTool)
  cornerstoneTools.addTool(SplineROITool)
  // cornerstoneTools.addTool(DragProbeTool);

  // Define a tool group, which defines how mouse events map to tool commands for
  // Any viewport using the group
  const toolGroup = imageOperationStateStore.toolGroup

  // Add the tools to the tool group
  // toolGroup.addTool(AnnotationDisplayTool.toolName)
  // toolGroup.addTool(ReferenceCursors.toolName)
  toolGroup.addTool(ReferenceLines.toolName)
  // toolGroup.addTool(ScaleOverlayTool.toolName);

  toolGroup.addTool(LengthTool.toolName)
  toolGroup.addTool(ProbeTool.toolName)
  toolGroup.addTool(RectangleROITool.toolName)
  toolGroup.addTool(RectangleROIStartEndThresholdTool.toolName)
  toolGroup.addTool(RectangleROIThresholdTool.toolName)
  toolGroup.addTool(EllipticalROITool.toolName)
  toolGroup.addTool(CircleROITool.toolName)
  toolGroup.addTool(BidirectionalTool.toolName)
  toolGroup.addTool(AngleTool.toolName)
  toolGroup.addTool(CobbAngleTool.toolName)
  toolGroup.addTool(ArrowAnnotateTool.toolName)
  toolGroup.addTool(CrosshairsTool.toolName)
  toolGroup.addTool(PlanarFreehandROITool.toolName)
  // set interpolation agressiveness while adding new annotation (ps: this does not change if interpolation is ON or OFF)
  toolGroup.setToolConfiguration(PlanarFreehandROITool.toolName, {
    interpolation: { knotsRatioPercentageOnAdd: 30 }
  })

  // set interpolation to be ON while editing only
  toolGroup.setToolConfiguration(PlanarFreehandROITool.toolName, {
    interpolation: { interpolateOnAdd: false, interpolateOnEdit: true },
    calculateStats: true
  })

  toolGroup.addTool(WindowLevelTool.toolName)
  toolGroup.addTool(PanTool.toolName)
  toolGroup.addTool(ZoomTool.toolName)
  toolGroup.addTool(StackScrollMouseWheelTool.toolName, { loop: false })
  toolGroup.addTool(PlanarRotateTool.toolName)

  toolGroup.addTool(BrushTool.toolName)
  toolGroup.addTool(CircleScissorsTool.toolName)
  toolGroup.addTool(MIPJumpToClickTool.toolName)
  toolGroup.addTool(MagnifyTool.toolName)
  toolGroup.addTool(PaintFillTool.toolName)
  toolGroup.addTool(RectangleScissorsTool.toolName)
  toolGroup.addTool(SegmentationDisplayTool.toolName)
  toolGroup.addTool(SphereScissorsTool.toolName)
  toolGroup.addTool(StackScrollTool.toolName)
  toolGroup.addTool(TrackballRotateTool.toolName)
  toolGroup.addTool(VolumeRotateMouseWheelTool.toolName)
  toolGroup.addTool(UltrasoundDirectionalTool.toolName)
  toolGroup.addTool(LivewireContourTool.toolName)
  toolGroup.addToolInstance('CardinalSplineROI', SplineROITool.toolName, {
    spline: {
      type: SplineROITool.SplineTypes.Cardinal,
      configuration: {
        [SplineROITool.SplineTypes.Cardinal]: {
          scale: 0.5
        }
      }
    }
  })

  toolGroup.addToolInstance('CatmullRomSplineROI', SplineROITool.toolName, {
    spline: {
      type: SplineROITool.SplineTypes.CatmullRom
    }
  })

  toolGroup.addToolInstance('LinearSplineROI', SplineROITool.toolName, {
    spline: {
      type: SplineROITool.SplineTypes.Linear
    }
  })

  toolGroup.addToolInstance('BSplineROI', SplineROITool.toolName, {
    spline: {
      type: SplineROITool.SplineTypes.BSpline
    }
  })

  toolGroup.addTool(SplineContourSegmentationTool.toolName)
  toolGroup.setToolConfiguration(UltrasoundDirectionalTool.toolName, {
    displayBothAxesDistances: true
  })

  toolGroup.addTool(PlanarFreehandContourSegmentationTool.toolName)
  toolGroup.addTool(LivewireContourSegmentationTool.toolName)
  // toolGroup.addTool(DragProbeTool.toolName);

  toolGroup.addToolInstance(brushInstanceNames.CircularBrush, BrushTool.toolName, {
    activeStrategy: brushStrategies.CircularBrush
  })
  toolGroup.addToolInstance(brushInstanceNames.CircularEraser, BrushTool.toolName, {
    activeStrategy: brushStrategies.CircularEraser
  })
  toolGroup.addToolInstance(brushInstanceNames.SphereBrush, BrushTool.toolName, {
    activeStrategy: brushStrategies.SphereBrush
  })
  toolGroup.addToolInstance(brushInstanceNames.SphereEraser, BrushTool.toolName, {
    activeStrategy: brushStrategies.SphereEraser
  })
  toolGroup.addToolInstance(brushInstanceNames.ThresholdSphere, BrushTool.toolName, {
    activeStrategy: brushStrategies.ThresholdSphere
  })
  toolGroup.addToolInstance(brushInstanceNames.ThresholdCircle, BrushTool.toolName, {
    activeStrategy: brushStrategies.ThresholdCircle
  })
  toolGroup.addToolInstance(brushInstanceNames.DynamicThreshold, BrushTool.toolName, {
    activeStrategy: brushStrategies.DynamicThreshold
  })
  toolGroup.addToolInstance(brushInstanceNames.ScissorsEraser, SphereScissorsTool.toolName, {
    activeStrategy: brushStrategies.ScissorsEraser
  })
  toolGroup.addToolInstance(
    'CatmullRomSplineSegmentation',
    SplineContourSegmentationTool.toolName,
    {
      spline: {
        type: SplineContourSegmentationTool.SplineTypes.CatmullRom
      }
    }
  )

  toolGroup.addToolInstance('LinearSplineSegmentation', SplineContourSegmentationTool.toolName, {
    spline: {
      type: SplineContourSegmentationTool.SplineTypes.Linear
    }
  })

  toolGroup.addToolInstance('BSplineSegmentation', SplineContourSegmentationTool.toolName, {
    spline: {
      type: SplineContourSegmentationTool.SplineTypes.BSpline
    }
  })

  toolGroup.addToolInstance('CardinalSplineSegmentation', SplineContourSegmentationTool.toolName, {
    spline: {
      type: SplineContourSegmentationTool.SplineTypes.Cardinal
    }
  })
  // toolGroup.setToolPassive(AnnotationDisplayTool.toolName)
  // toolGroup.setToolPassive(ReferenceCursors.toolName)
  toolGroup.setToolPassive(ReferenceLines.toolName)
 

  // toolGroup.setToolConfiguration(
  //   ScaleOverlayTool.toolName,
  //   {
  //     scaleLocation: 'bottom',
  //   },
  //   true //overwrite
  // );
  // toolGroup.setToolPassive(ScaleOverlayTool.toolName);

  // Set the initial state of the tools, here we set one tool active on left click.
  // This means left click will draw that tool.
  toolGroup.setToolPassive(LengthTool.toolName)
  // We set all the other tools passive here, this means that any state is rendered, and editable
  // But aren't actively being drawn (see the toolModes example for information)
  toolGroup.setToolPassive(ProbeTool.toolName)
  toolGroup.setToolPassive(RectangleROITool.toolName)
  toolGroup.setToolPassive(RectangleROIStartEndThresholdTool.toolName)
  toolGroup.setToolPassive(RectangleROIThresholdTool.toolName)
  toolGroup.setToolPassive(EllipticalROITool.toolName)
  toolGroup.setToolPassive(CircleROITool.toolName)
  toolGroup.setToolPassive(BidirectionalTool.toolName)
  toolGroup.setToolPassive(AngleTool.toolName)
  toolGroup.setToolPassive(CobbAngleTool.toolName)
  toolGroup.setToolPassive(ArrowAnnotateTool.toolName)
  // toolGroup.setToolPassive(CrosshairsTool.toolName);
  toolGroup.setToolPassive(PlanarFreehandROITool.toolName)

  // As the Stack Scroll mouse wheel is a tool using the `mouseWheelCallback`
  // hook instead of mouse buttons, it does not need to assign any mouse button.
  toolGroup.setToolActive(StackScrollMouseWheelTool.toolName)
  toolGroup.setToolActive(PanTool.toolName, {
    bindings: [
      {
        mouseButton: MouseBindings.Auxiliary // Middle Click
      }
    ]
  })
  toolGroup.setToolActive(ZoomTool.toolName, {
    bindings: [
      {
        mouseButton: MouseBindings.Secondary // Right Click
      }
    ]
  })

  toolGroup.setToolPassive(BrushTool.toolName)
  toolGroup.setToolPassive(CircleScissorsTool.toolName)
  toolGroup.setToolPassive(MIPJumpToClickTool.toolName)
  toolGroup.setToolPassive(MagnifyTool.toolName)
  toolGroup.setToolPassive(PaintFillTool.toolName)
  toolGroup.setToolPassive(RectangleScissorsTool.toolName)
  toolGroup.setToolPassive(SegmentationDisplayTool.toolName)
  toolGroup.setToolPassive(SphereScissorsTool.toolName)
  toolGroup.setToolPassive(StackScrollTool.toolName)
  toolGroup.setToolPassive(TrackballRotateTool.toolName)
  toolGroup.setToolPassive(VolumeRotateMouseWheelTool.toolName)
  toolGroup.setToolPassive(LivewireContourTool.toolName)

  toolGroup.setToolPassive(PlanarFreehandContourSegmentationTool.toolName)
  toolGroup.setToolPassive(LivewireContourSegmentationTool.toolName)

  // toolGroup!.addViewport(viewportId, renderingEngineId)

  imageOperationStateStore.leftMouseActive = ArrowAnnotateTool.toolName
  imageOperationStateStore.bindLeftMouse(LengthTool.toolName)
}

export default createTools
