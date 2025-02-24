<script setup lang="ts">
import {
  RenderingEngine,
  Enums,
  metaData,
  volumeLoader,
  imageLoader,
  setVolumesForViewports
} from '@cornerstonejs/core'
import type { Types } from '@cornerstonejs/core'

import { initDemo, setCtTransferFunctionForVolumeActor } from '@/utils/helpers/index.js'
import { onMounted } from 'vue'
import type { RGB } from '@cornerstonejs/core/src/types/'
import hardcodedMetaDataProvider from '@/utils/helpers/hardcodedMetaDataProvider'
import registerWebImageLoader from '@/utils/helpers/registerWebImageLoader'
defineOptions({
  name: ''
})
registerWebImageLoader(imageLoader)
onMounted(async () => {
  const element: HTMLDivElement = document.getElementById('cornerstone-element') as HTMLDivElement
  // Disable right click context menu so we can have right click tools
  element.oncontextmenu = (e) => e.preventDefault()
  await initDemo()
  metaData.addProvider(
    // @ts-ignore
    (type, imageId) => hardcodedMetaDataProvider(type, imageId, imageIds),
    10000
  )
  const { ViewportType } = Enums
  // const imageIds = [
  //   'wadouri:src/assets/dicom/1-01.dcm',
  //   'wadouri:src/assets/dicom/1-02.dcm',
  //   'wadouri:src/assets/dicom/1-03.dcm',
  //   'wadouri:src/assets/dicom/1-04.dcm',
  //   'wadouri:src/assets/dicom/1-05.dcm',
  //   'wadouri:src/assets/dicom/1-06.dcm',
  //   'wadouri:src/assets/dicom/1-07.dcm',
  //   'wadouri:src/assets/dicom/1-08.dcm',
  //   'wadouri:src/assets/dicom/1-09.dcm',
  //   'wadouri:src/assets/dicom/1-10.dcm',
  //   'wadouri:src/assets/dicom/1-11.dcm',
  //   'wadouri:src/assets/dicom/1-12.dcm',
  //   'wadouri:src/assets/dicom/1-13.dcm',
  //   'wadouri:src/assets/dicom/1-14.dcm',
  //   'wadouri:src/assets/dicom/1-15.dcm',
  //   'wadouri:src/assets/dicom/1-16.dcm',
  //   'wadouri:src/assets/dicom/1-17.dcm',
  //   'wadouri:src/assets/dicom/1-18.dcm',
  //   'wadouri:src/assets/dicom/1-19.dcm',
  //   'wadouri:src/assets/dicom/1-20.dcm',
  //   'wadouri:src/assets/dicom/1-21.dcm',
  //   'wadouri:src/assets/dicom/1-22.dcm',
  //   'wadouri:src/assets/dicom/1-23.dcm',
  //   'wadouri:src/assets/dicom/1-24.dcm',
  //   'wadouri:src/assets/dicom/1-25.dcm',
  //   'wadouri:src/assets/dicom/1-26.dcm',
  //   'wadouri:src/assets/dicom/1-27.dcm',
  //   'wadouri:src/assets/dicom/1-28.dcm',
  //   'wadouri:src/assets/dicom/1-29.dcm',
  //   'wadouri:src/assets/dicom/1-30.dcm'
  // ]

  const imageIds = [
    'web:http://127.0.0.1:8080/resources/images/1709280438027963/1709280438027963_adult-business-care.jpeg'
  ]

  // Instantiate a rendering engine
  const renderingEngineId = 'firstRenderingEngine'
  const renderingEngine = new RenderingEngine(renderingEngineId)
  // Create a stack viewport
  const viewportId = 'CT_SAGITTAL_STACK'
  const viewportInputArray = [{
    viewportId,
    type: ViewportType.ORTHOGRAPHIC,
    element: element as HTMLDivElement,
    defaultOptions: {
      background: [0.2, 0, 0.2] as RGB
    }
  }]
  // Get the stack viewport that was created
  const viewport = <Types.IVolumeViewport>renderingEngine.getViewport(viewportInputArray[0].viewportId)
  const volumeId = 'COLOR_VOLUME'
  // Define a volume in memory
  const volume = await volumeLoader.createAndCacheVolume(volumeId, {
    imageIds
  })
  renderingEngine.setViewports(viewportInputArray);
  setVolumesForViewports(renderingEngine, [{ volumeId }], ['CT_SAGITTAL_STACK'])
  // Set the volume to load

  // Set the volume on the viewport
  // viewport.setVolumes([{ volumeId, callback: setCtTransferFunctionForVolumeActor }])
  volume.load()
  renderingEngine.render()
})
</script>

<template>
  <div id="content" class="relative flex justify-center w-full h-full">
    <div id="cornerstone-element" class="w-full h-full"></div>
  </div>
</template>

<style lang="scss" scoped></style>
