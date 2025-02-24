import { getRenderingEngine, type Types } from '@cornerstonejs/core'
import { utilities as csUtils } from '@cornerstonejs/core'
import { utilities as cstUtils } from '@cornerstonejs/tools'
import vtkColormaps from '@kitware/vtk.js/Rendering/Core/ColorTransferFunction/ColorMaps'
import { annotation } from '@cornerstonejs/tools'
import type { Annotation } from '@cornerstonejs/tools/src/types'
import html2canvas from 'html2canvas'

const { selection, visibility, locking, state } = annotation

function reRenderViewport(renderingEngineId: string, viewportId: string) {
  const renderingEngine = getRenderingEngine(renderingEngineId)
  renderingEngine!.renderViewports([viewportId])
}

function resetOriginal(renderingEngineId: string, viewportId: string) {
  // Get the rendering engine
  const renderingEngine = getRenderingEngine(renderingEngineId)
  // Get the stack viewport
  const viewport = <Types.IStackViewport>renderingEngine!.getViewport(viewportId)
  viewport.resetCamera()
  viewport.render()
}

function flipH(renderingEngineId: string, viewportId: string) {
  // Get the rendering engine
  const renderingEngine = getRenderingEngine(renderingEngineId)
  // Get the stack viewport
  const viewport = <Types.IStackViewport>renderingEngine!.getViewport(viewportId)
  const { flipHorizontal } = viewport.getCamera()
  viewport.setCamera({ flipHorizontal: !flipHorizontal })
  viewport.render()
}

function resetPan(renderingEngineId: string, viewportId: string) {
  // Get the rendering engine
  const renderingEngine = getRenderingEngine(renderingEngineId)
  // Get the stack viewport
  const viewport = <Types.IStackViewport>renderingEngine!.getViewport(viewportId)
  viewport.resetCamera(true, false)
  viewport.render()
}

function resetZoom(renderingEngineId: string, viewportId: string) {
  // Get the rendering engine
  const renderingEngine = getRenderingEngine(renderingEngineId)
  // Get the stack viewport
  const viewport = <Types.IStackViewport>renderingEngine!.getViewport(viewportId)
  viewport.resetCamera(false, true)
  viewport.render()
}

function changeZoom(renderingEngineId: string, viewportId: string, zoomNumber: number) {
  // Get the rendering engine
  const renderingEngine = getRenderingEngine(renderingEngineId)
  // Get the stack viewport
  const viewport = <Types.IStackViewport>renderingEngine!.getViewport(viewportId)
  const zoom = viewport.getZoom()
  viewport.setZoom(zoom * zoomNumber)
  viewport.render()
}

function invert(renderingEngineId: string, viewportId: string) {
  const renderingEngine = getRenderingEngine(renderingEngineId)
  // Get the stack viewport
  const viewport = <Types.IStackViewport>renderingEngine!.getViewport(viewportId)
  const { invert } = viewport.getProperties()
  viewport.setProperties({ invert: !invert })
  viewport.render()
}

function hideSelectedAnnotation(renderingEngineId: string, viewportId: string) {
  const annotationUIDs = selection.getAnnotationsSelected()

  if (annotationUIDs && annotationUIDs.length) {
    for (let i = 0; i < annotationUIDs.length; i++) {
      const annotationUID = annotationUIDs[i]
      visibility.setAnnotationVisibility(annotationUID, false)
    }
    reRenderViewport(renderingEngineId, viewportId)
  }
}

function lockSelectedAnnotation(renderingEngineId: string, viewportId: string) {
  const annotationUIDs = selection.getAnnotationsSelected()

  if (annotationUIDs && annotationUIDs.length) {
    for (let i = 0; i < annotationUIDs.length; i++) {
      const annotationUID = annotationUIDs[i]
      const defaultFrameOfReferenceSpecificAnnotationManager =
        annotation.state.getAnnotationManager()
      const annotation1 =
        defaultFrameOfReferenceSpecificAnnotationManager.getAnnotation(annotationUID)

      locking.setAnnotationLocked(annotation1!, true)
      annotation1!.highlighted = false
    }

    selection.deselectAnnotation()
    reRenderViewport(renderingEngineId, viewportId)
  }
}

function unlockAllAnnotations() {
  locking.unlockAllAnnotations()
}

function selectAnnotationsByToolName(
  toolName: string,
  renderingEngineId: string,
  viewportId: string
) {
  const annotations: Annotation[] = annotation.state.getAllAnnotations()
  for (let i = 0; i < annotations.length; i++) {
    if (annotations[i].metadata.toolName == toolName && !annotations[i].isLocked) {
      annotations[i].highlighted = true
      selection.setAnnotationSelected(annotations[i].annotationUID!, true, true)
    }
  }
  reRenderViewport(renderingEngineId, viewportId)
}

function revokePreviousAnnotation(renderingEngineId: string, viewportId: string) {
  const annotationUIDs = selection.getAnnotationsSelected()
  selection.deselectAnnotation(annotationUIDs[annotationUIDs.length - 1])
  state.getAnnotation(annotationUIDs[annotationUIDs.length - 1]).highlighted = false
  reRenderViewport(renderingEngineId, viewportId)
}

function removeAnnotation(renderingEngineId: string, viewportId: string) {
  const annotationUIDs = selection.getAnnotationsSelected()
  if (annotationUIDs && annotationUIDs.length) {
    for (let i = 0; i < annotationUIDs.length; i++) {
      const annotationUID = annotationUIDs[i]
      annotation.state.removeAnnotation(annotationUID)
    }
    reRenderViewport(renderingEngineId, viewportId)
  }
}

function showAllAnnotations(renderingEngineId: string, viewportId: string) {
  visibility.showAllAnnotations()
  reRenderViewport(renderingEngineId, viewportId)
}

function selectAllAnnotations(renderingEngineId: string, viewportId: string) {
  const annotations: Annotation[] = annotation.state.getAllAnnotations()
  for (let i = 0; i < annotations.length; i++) {
    if (!annotations[i].isLocked) {
      selection.setAnnotationSelected(annotations[i].annotationUID!, true, true)
      annotations[i].highlighted = true
    }
  }
  reRenderViewport(renderingEngineId, viewportId)
}

function reverseSelectionAnnotations(renderingEngineId: string, viewportId: string) {
  const annotations: Annotation[] = annotation.state.getAllAnnotations()
  for (let i = 0; i < annotations.length; i++) {
    if (
      !selection.isAnnotationSelected(annotations[i].annotationUID!) &&
      !annotations[i].isLocked
    ) {
      selection.setAnnotationSelected(annotations[i].annotationUID!, true, true)
      annotations[i].highlighted = true
    } else {
      selection.deselectAnnotation(annotations[i].annotationUID!)
      annotations[i].highlighted = false
    }
  }
  reRenderViewport(renderingEngineId, viewportId)
}

function setWindowLevel(
  window: number,
  level: number,
  renderingEngineId: string,
  viewportId: string
) {
  // convert to numbers
  const windowWidthNum = Number(window)
  const windowCenterNum = Number(level)
  const renderingEngine = getRenderingEngine(renderingEngineId)
  const viewport = <Types.IStackViewport>renderingEngine!.getViewport(viewportId)
  const { lower, upper } = csUtils.windowLevel.toLowHighRange(windowWidthNum, windowCenterNum)
  console.log(viewport)
  viewport.setProperties({
    voiRange: {
      upper,
      lower
    }
  })
  viewport.render()
}

// Change the colormap of an specific viewport
function setViewportColormap(colormapName: string, renderingEngineId: string, viewportId: string) {
  // Get the rendering engine
  const renderingEngine = getRenderingEngine(renderingEngineId)

  // Get the volume viewport
  const viewport = <Types.IStackViewport>renderingEngine!.getViewport(viewportId)
  viewport.setProperties({ colormap: { name: colormapName } })
  viewport.render()
}

function setViewportColorbar(
  colormapName: string,
  renderingEngineId: string,
  viewportId: string,
  colorbar: any
) {
  colorbar.activeColormapName = colormapName
  setViewportColormap(colormapName, renderingEngineId, viewportId)
}

function changePreviousImage(renderingEngineId: string, viewportId: string) {
  const renderingEngine = getRenderingEngine(renderingEngineId)
  const viewport = <Types.IStackViewport>renderingEngine!.getViewport(viewportId)
  // Get the current index of the image displayed
  const currentImageIdIndex = viewport.getCurrentImageIdIndex()

  // Increment the index, clamping to the first image if necessary
  let newImageIdIndex = currentImageIdIndex - 1

  newImageIdIndex = Math.max(newImageIdIndex, 0)

  // Set the new image index, the viewport itself does a re-render
  viewport.setImageIdIndex(newImageIdIndex)
}

function changeNextImage(renderingEngineId: string, viewportId: string) {
  const renderingEngine = getRenderingEngine(renderingEngineId)
  const viewport = <Types.IStackViewport>renderingEngine!.getViewport(viewportId)
  // Get the current index of the image displayed
  const currentImageIdIndex = viewport.getCurrentImageIdIndex()

  // Increment the index, clamping to the last image if necessary
  let newImageIdIndex = currentImageIdIndex + 1
  const imageIds = viewport.getImageIds()
  newImageIdIndex = Math.min(newImageIdIndex, imageIds.length - 1)

  // Set the new image index, the viewport itself does a re-render
  viewport.setImageIdIndex(newImageIdIndex)
}

function resetToDefaultViewportProperties(renderingEngineId: string, viewportId: string) {
  const renderingEngine = getRenderingEngine(renderingEngineId)
  const viewport = <Types.IStackViewport>renderingEngine!.getViewport(viewportId)
  // Resets the viewport's camera
  viewport.resetCamera()
  // Resets the viewport's to its default properties
  viewport.resetToDefaultProperties()
  viewport.render()
}

function removeCurrentImageIdProperties(renderingEngineId: string, viewportId: string) {
  const renderingEngine = getRenderingEngine(renderingEngineId)
  const viewport = <Types.IStackViewport>renderingEngine!.getViewport(viewportId)
  viewport.clearDefaultProperties(viewport.getCurrentImageId())
}

function createViewportColorbar(index: number, elementId: string, colorbarContainerId: string) {
  const { ViewportColorbar } = cstUtils.voi.colorbar
  const { ColorbarRangeTextPosition } = cstUtils.voi.colorbar.Enums
  // Convert all VTK colormaps to the one supported by the colorbar which actualy
  // have almost the same properties.
  const colormaps = vtkColormaps.rgbPresetNames.map((presetName) =>
    vtkColormaps.getPresetByName(presetName)
  )
  const element: HTMLDivElement = document.getElementById(elementId) as HTMLDivElement
  const colorbarContainer = document.getElementById(colorbarContainerId)
  const ctColorbar = new ViewportColorbar({
    id: 'ctColorbar' + index,
    element,
    container: colorbarContainer as HTMLElement,
    colormaps,
    activeColormapName: 'Grayscale',
    ticks: {
      position: ColorbarRangeTextPosition.Left,
      style: {
        font: '12px Arial',
        color: 'white',
        maxNumTicks: 8,
        tickSize: 5,
        tickWidth: 1,
        labelMargin: 3
      }
    }
  })
  return ctColorbar
}

function downloadCanvasAsImage(divForDownloadViewportName: string, filename: string) {
  const divForDownloadViewport = document.querySelector(
    divForDownloadViewportName
  ) as HTMLDivElement
  html2canvas(divForDownloadViewport).then((canvas: HTMLCanvasElement) => {
    const link = document.createElement('a')
    link.download = filename + '.jpg'
    link.href = canvas.toDataURL('jpg', 1.0)
    link.click()
  })
}

export {
  resetOriginal,
  flipH,
  resetPan,
  resetZoom,
  changeZoom,
  invert,
  setWindowLevel,
  setViewportColormap,
  createViewportColorbar,
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
}
