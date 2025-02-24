<script setup lang="ts">
import { onMounted, reactive, ref, watch } from 'vue'
import { useImageOperationStateStore } from '@/store/imageOperationState'
import { Enums } from '@cornerstonejs/core'
import type IStackViewport from '@cornerstonejs/core/src/types/IStackViewport'
import html2canvas from 'html2canvas'
import {
  hideSelectedAnnotation,
  showAllAnnotations,
  selectAllAnnotations
} from '@/composables/image/imageOperate'
import { viewReaderApi } from '@/api/series'
import { imageKeyValueStore } from '@/composables/image/imageKeyValueStore'
import type { ImageFeature } from '@/types/image'
import type { ImageInfo } from '@/types/series'
import type { ImageModelResult } from '@/types/model'
import type { SeriesInfoWindows } from '@/types/image'
import type { SeriesInfo } from '@/types/series'
import {
  changeSeriesListWindowsToSession,
  pushSeriesToSession,
  pushseriesModelsListsSession
} from '@/composables/image/utils'
import { message } from '@/utils/message'
import {StackScrollMouseWheelTool} from '@cornerstonejs/tools'

const props = defineProps<{
  completeViewImageWindowOpen?: boolean
}>()

const emits = defineEmits<{
  completeViewImageWindowClose: [] // 具名元组语法
}>()

let centerDialogVisible = ref(props.completeViewImageWindowOpen)

watch(
  () => {
    return props.completeViewImageWindowOpen
  },
  (value, prevValue) => {
    centerDialogVisible.value = value
  }
)

const imageFileInfo = reactive({
  imageWidth: 512,
  imageHeight: 512,
  imageType: 'jpg',
  imageName: 'image',
  imageAnnotation: true,
  imageDescription: ''
})

const imageOperationStateStore = useImageOperationStateStore()

var parentNode: HTMLElement | null = null
var divElement: HTMLElement | null = null
var divForDownloadViewport: HTMLDivElement | null = null
var ro: ResizeObserver | null = null

function onOpened() {
  divForDownloadViewport = document.querySelector(
    `div[data-viewport-uid='stackViewPort${imageOperationStateStore.selectSeriesWindows}']`
  ) as HTMLDivElement
  parentNode = divForDownloadViewport.parentNode as HTMLDivElement
  divElement = document.getElementById('previewCanvas') as HTMLDivElement
  

  // 创建一个新的 ResizeObserver 实例
  ro = new ResizeObserver((entries) => {
    console.log('ResizeObserver')
    setTimeout(() => {
      // viewport.resize()
      imageOperationStateStore.renderingEngine.resize(true, true)
    }, 100) // 延迟1000毫秒后调用
  })
  ro.observe(divElement)

  divElement.appendChild(divForDownloadViewport)
  divForDownloadViewport!.style.width = imageFileInfo.imageWidth + 'px'
  divForDownloadViewport!.style.height = imageFileInfo.imageHeight + 'px'
  imageOperationStateStore.toolGroup.setToolPassive(StackScrollMouseWheelTool.toolName)
}

function onClosed() {
  imageFileInfo.imageWidth = 512
  imageFileInfo.imageHeight = 512
  divForDownloadViewport!.style.width = '100%'
  divForDownloadViewport!.style.height = '100%'
  ro!.unobserve(divElement!)
  parentNode!.insertBefore(divForDownloadViewport!, parentNode!.firstChild)
  imageOperationStateStore.toolGroup.setToolActive(StackScrollMouseWheelTool.toolName)
}

function viewReader() {
  html2canvas(divForDownloadViewport as HTMLDivElement).then((canvas: HTMLCanvasElement) => {
    let formData = new FormData()
    let imageData = canvas.toDataURL(imageFileInfo.imageType, 1.0)
    let blob = dataURLtoBlob(imageData)
    let file = new File([blob], imageFileInfo.imageName + '.' + imageFileInfo.imageType, {
      type: imageFileInfo.imageType
    })

    const imageInfo: ImageInfo = imageKeyValueStore.get(
      imageOperationStateStore.viewports[
        imageOperationStateStore.selectSeriesWindows
      ].getCurrentImageId()
    )
    // const imageModelData: ImageModelResult = JSON.parse(JSON.stringify(imageInfo.imageModelData))
    const markImageObject = {
      seriesId: imageInfo.seriesId,
      imageId: imageInfo.imageId,
      readerView: imageFileInfo.imageDescription
    }
    formData.append('file', file)
    formData.append('info', JSON.stringify(markImageObject))
    console.log(formData)
    viewReaderApi(formData).then((res) => {
      if (res.code === 200) {
        console.log(res.data)
        const seriesInfo: SeriesInfo = res.data as SeriesInfo
        const seriesListWindows = imageOperationStateStore.seriesListWindows
        const seriesLists = imageOperationStateStore.seriesLists
        const seriesModelsLists = imageOperationStateStore.seriesModelsLists

        for (var i = 0; i < seriesListWindows.length; i++) {
          if (seriesListWindows[i] != 0) {
            const seriesListWindow: SeriesInfoWindows = seriesListWindows[i] as SeriesInfoWindows
            if (seriesListWindow.seriesInfo.seriesId == seriesInfo.seriesId) {
              seriesListWindow.seriesInfo.markSeriesPreviewPath = seriesInfo.markSeriesPreviewPath
              seriesListWindow.seriesInfo.markSeriesName = seriesInfo.markSeriesName
              seriesListWindow.seriesInfo.readerView = seriesInfo.readerView
              seriesListWindow.seriesInfo.seriesPreviewPath = seriesInfo.seriesPreviewPath
              seriesListWindow.seriesInfo.seriesStatus = seriesInfo.seriesStatus
              changeSeriesListWindowsToSession(seriesListWindow, i)
            }
          }
        }

        for (var i = 0; i < seriesLists.length; i++) {
          const seriesInfomation: SeriesInfo = seriesLists[i] as SeriesInfo
          if (seriesInfo.seriesId == seriesInfo.seriesId) {
            seriesInfomation.markSeriesPreviewPath = seriesInfo.markSeriesPreviewPath
            seriesInfomation.markSeriesName = seriesInfo.markSeriesName
            seriesInfomation.readerView = seriesInfo.readerView
            seriesInfomation.seriesPreviewPath = seriesInfo.seriesPreviewPath
            seriesInfomation.seriesStatus = seriesInfo.seriesStatus
            pushSeriesToSession(seriesInfomation)
            break
          }
        }

        for (var i = 0; i < seriesModelsLists.length; i++) {
          const seriesModelsList: SeriesInfo = seriesModelsLists[i] as SeriesInfo
          if (seriesModelsList.seriesId == seriesInfo.seriesId) {
            seriesModelsList.markSeriesPreviewPath = seriesInfo.markSeriesPreviewPath
            seriesModelsList.markSeriesName = seriesInfo.markSeriesName
            seriesModelsList.readerView = seriesInfo.readerView
            seriesModelsList.seriesPreviewPath = seriesInfo.seriesPreviewPath
            seriesModelsList.seriesStatus = seriesInfo.seriesStatus
            pushseriesModelsListsSession(seriesModelsList)
          }
        }
        message(res.msg, { type: 'success' })
        emits('completeViewImageWindowClose')
      }
    })
  })
}

function dataURLtoBlob(dataurl: string) {
  // 转换函数（这里简化处理，不处理可能的错误）
  const parts = dataurl.split(';base64,')
  const contentType = parts[0].split(':')[1]
  const raw = window.atob(parts[1])
  const rawLength = raw.length
  const uInt8Array = new Uint8Array(rawLength)

  for (let i = 0; i < rawLength; ++i) {
    uInt8Array[i] = raw.charCodeAt(i)
  }

  return new Blob([uInt8Array], { type: contentType })
}

const changeWidthHeight = () => {
  divForDownloadViewport!.style.width = imageFileInfo.imageWidth + 'px'
  divForDownloadViewport!.style.height = imageFileInfo.imageHeight + 'px'
}

function handleSwitchChange(newVal: Boolean) {
  selectAllAnnotations(
    imageOperationStateStore.renderingEngine.id,
    imageOperationStateStore.viewports[imageOperationStateStore.selectSeriesWindows].id
  )
  if (newVal) {
    showAllAnnotations(
      imageOperationStateStore.renderingEngine.id,
      imageOperationStateStore.viewports[imageOperationStateStore.selectSeriesWindows].id
    )
  } else {
    hideSelectedAnnotation(
      imageOperationStateStore.renderingEngine.id,
      imageOperationStateStore.viewports[imageOperationStateStore.selectSeriesWindows].id
    )
  }
}
</script>

<template>
  <el-dialog
    v-model="centerDialogVisible"
    class="overflow-auto"
    style="max-height: 50vh"
    @close="$emit('completeViewImageWindowClose')"
    @opened="onOpened"
    @closed="onClosed"
    title="完成阅片"
    width="50%"
    :modal="false"
    :destroy-on-close="true"
    draggable
    overflow
    center
  >
    <div class="flex flex-col">
      <el-form ref="ruleFormRef" :model="imageFileInfo" label-position="top">
        <el-form-item label="序列描述" prop="imageDescription">
          <el-input
            v-model="imageFileInfo.imageDescription"
            style="width: 100%"
            :autosize="{ minRows: 5, maxRows: 10 }"
            type="textarea"
            placeholder="请输入对于序列的描述"
          />
        </el-form-item>
        <el-form-item label="文件名">
          <el-input v-model="imageFileInfo.imageName" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="宽（px)">
              <el-input-number
                :max="1024"
                :min="32"
                v-model="imageFileInfo.imageWidth"
                @change="changeWidthHeight()"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="高（px)">
              <el-input-number
                :max="1024"
                :min="32"
                v-model="imageFileInfo.imageHeight"
                @change="changeWidthHeight()"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="图片类型" prop="patientWeight">
              <el-radio-group v-model="imageFileInfo.imageType" class="ml-4">
                <el-radio label="png" size="large">png</el-radio>
                <el-radio label="jpg" size="large">jpg</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="显示注释" prop="patientAge">
              <el-switch
                v-model="imageFileInfo.imageAnnotation"
                @change="handleSwitchChange"
                size="large"
                active-text="显示"
                inactive-text="关闭"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>
    <span class="flex justify-center mb-4">
      <el-button @click="centerDialogVisible = false">取消</el-button>
      <el-button type="primary" @click="viewReader()"> 完成阅片 </el-button>
    </span>
    <div id="previewCanvas" class="flex flex-col items-center justify-center">
      <el-text size="large">预览</el-text><br />
    </div>
  </el-dialog>
</template>

<style lang="scss" scoped></style>
