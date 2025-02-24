import { ref, reactive, computed, type Ref } from 'vue'
import { defineStore } from 'pinia'
import * as cornerstoneTools from '@cornerstonejs/tools'
import type { IToolGroup } from '@cornerstonejs/tools/src/types'
import { getImagePageByDoctorId, deleteSingleImageById } from '@/api/image'
import { message } from '@/utils/message'
import { storageSession } from '@pureadmin/utils'
import {
  seriesListsSession,
  seriesListWindowsSession,
  seriesModelsListsSession,
  pushseriesModelsListsSession
} from '@/composables/image/utils'

import {
  RenderingEngine,
  Enums,
  imageLoader,
  metaData,
  getRenderingEngine,
  volumeLoader,
  BaseVolumeViewport,
  VideoViewport,
  StackViewport,
  VolumeViewport
} from '@cornerstonejs/core'
import type { Types } from '@cornerstonejs/core'
import type { IStackViewport, IVolumeViewport } from '@cornerstonejs/core/src/types'
import { formatDate } from '@/composables/image/utils'
import type { SeriesInfoWindows } from '@/types/image'

import createTools from '@/composables/toolsManage'
import { utilities as csUtils } from '@cornerstonejs/core'
import { type ViewportColorbar } from '@cornerstonejs/tools/src/utilities/voi/colorbar/ViewportColorbar'
import type { SeriesInfo, ImageInfo } from '@/types/series'

const { Enums: csToolsEnums } = cornerstoneTools
const { MouseBindings } = csToolsEnums

export const useImageOperationStateStore = defineStore('imageOperationState', () => {
  const imageList = reactive<ImageInfo>({
    imageId: null,
    imageName: null,
    imagePath: null,
    type: null,
    seriesId: null,
    imageIdRel: null,
    operateId: null,
    operateName: null,
    operateTime: null,
    imageStatus: null,
    imageEquipment: null,
    imageFormat: null,
    imageDesc: null,
    imageCheckPart: null,
    imageCheckTime: null,
    patientId: null,
    patientName: null,
    isDeleted: null,
    imagePosition: null,
    imageOrientation: null,
    sliceThickness: null,
    sliceLocation: null,
    imageRows: null,
    imageColumns: null,
    pixelSpacing: null,
    bitsAllocated: null,
    bitsStored: null,
    highBit: null,
    pixelRepresentation: null,
    windowCenter: null,
    windowWidth: null,
    rescaleIntercept: null,
    rescaleSlope: null,
    rescaleType: null,
    imageType: null,
    sopInstanceUid: null,
    contentDate: null,
    contentTime: null,
    imageNumber: null,
    samplesPerPixel: null,
    photometricInterpretation: null,
    imageModelData: null,
    modelType: null,
    markImageName: null,
    markImagePath: null,
    markImageDesc: null,
    createTime: null,
    creatorId: null
  })
  const seriesList = reactive<SeriesInfo>({
    seriesId: 0,
    seriesName: null,
    seriesEquipment: null,
    seriesFormat: '',
    seriesPath: '',
    seriesPreviewPath: null,
    seriesCount: 0,
    seriesCheckPart: null,
    seriesCheckTime: null,
    patientId: null,
    patientName: null,
    creatorId: 0,
    creatorName: null,
    createTime: null,
    seriesStatus: null,
    isDeleted: null,
    seriesNumber: '',
    seriesInstanceUid: '',
    seriesModality: null,
    seriesDesc: '',
    seriesDate: '',
    seriesTime: '',
    imagePosition: null,
    spacingBetweenSlices: null,
    mrAcquisitionType: null,
    studyId: 0,
    imageList: [],
    seriesFeature: null,
    seriesModelType: null,
    readerView: null,
    doctorView: null,
    markSeriesPreviewPath: null,
    markSeriesName: null
  })
  const seriesLists = reactive<SeriesInfo[]>(
    storageSession().getItem<SeriesInfo[]>(seriesListsSession)
      ? storageSession().getItem<SeriesInfo[]>(seriesListsSession)
      : []
  )
  const seriesModelsLists = reactive<SeriesInfo[]>(
    storageSession().getItem<SeriesInfo[]>(seriesModelsListsSession)
      ? storageSession().getItem<SeriesInfo[]>(seriesModelsListsSession)
      : []
  )
  const seriesListWindows = reactive<(0 | SeriesInfoWindows)[]>(
    storageSession().getItem<(0 | SeriesInfoWindows)[]>(seriesListWindowsSession)
      ? storageSession().getItem<(0 | SeriesInfoWindows)[]>(seriesListWindowsSession)
      : [0, 0, 0, 0, 0, 0, 0, 0, 0]
  )

  const segmentationRepresentationUIDList = reactive(new Map())

  const tableData = reactive([])
  const pageData = reactive({
    current: 1,
    pageSize: 10,
    userId: 1
  })
  const windowRowsColumns = reactive({ rows: 1, columns: 1 })

  const renderingEngine: Ref<RenderingEngine> = ref(
    new RenderingEngine('stackRenderingEngine')
  ) as Ref<RenderingEngine>
  const viewports: Ref<
    BaseVolumeViewport[] | Types.IStackViewport[] | VideoViewport[] | Types.IVolumeViewport[]
  > = ref([]) as Ref<
    BaseVolumeViewport[] | Types.IStackViewport[] | VideoViewport[] | Types.IVolumeViewport[]
  >
  const viewportColorbar: Ref<ViewportColorbar[]> = ref([])

  const leftMouseActive: Ref<string> = ref('')
  const toolGroup: Ref<IToolGroup> = ref(
    cornerstoneTools.ToolGroupManager.createToolGroup('GroupToolsId') as unknown as IToolGroup
  ) as Ref<IToolGroup>
  const segmentationId = ref('segementationId')

  const selectSeriesList = ref()
  const selectSeriesWindows = ref(0)
  const selectSeriesModelsList = ref()

  function bindLeftMouse(newLeftMouseActive: string) {
    message(`左键当前激活的工具是：${newLeftMouseActive}`, { type: 'success' })
    toolGroup!.value.setToolPassive(leftMouseActive.value)
    leftMouseActive.value = newLeftMouseActive
    toolGroup!.value.setToolActive(leftMouseActive.value, {
      bindings: [
        {
          mouseButton: MouseBindings.Primary // Left Click
        }
      ]
    })
  }

  function bindImageList(imageObject: Record<string, any>) {
    // 遍历普通对象，并将值复制到响应式对象中
    Object.keys(imageObject).forEach((key) => {
      ;(imageList as Record<string, any>)[key as keyof typeof imageList] = imageObject[key]
    })
  }

  function bindSeriesList(imagesObject: Record<string, any>) {
    // 遍历普通对象，并将值复制到响应式对象中
    Object.keys(imagesObject).forEach((key) => {
      ;(seriesList as Record<string, any>)[key as keyof typeof seriesList] = imagesObject[key]
    })
  }

  function pushSeriesList(seriesList: SeriesInfo) {
    let existingElement = seriesLists.find((element) => element.seriesId === seriesList.seriesId)
    if (existingElement) {
      const index = seriesLists.indexOf(existingElement)
      seriesLists.splice(index, 1)
      seriesLists.push(seriesList)
    } else {
      seriesLists.push(seriesList)
    }
  }

  function pushSeriesModelsList(imagesListParameter: SeriesInfo) {
    if (seriesModelsLists.length == 0) {
      seriesModelsLists.push(imagesListParameter)
      return
    }
    for (var i = 0; i < seriesModelsLists.length; i++) {
      if (
        seriesModelsLists[i].seriesId === imagesListParameter.seriesId &&
        seriesModelsLists[i].imageList[0].imageModelData!.modelId ===
          imagesListParameter.imageList[0].imageModelData!.modelId
      ) {
        if (seriesModelsLists[i].imageList.length < imagesListParameter.imageList.length) {
          for (var j = 0; j < imagesListParameter.imageList.length; j++) {
            if (
              seriesModelsLists[i].imageList[0].imageId == imagesListParameter.imageList[j].imageId
            ) {
              seriesModelsLists[i].imageList[0] = imagesListParameter.imageList[j]
            }
          }
        } else if (seriesModelsLists[i].imageList.length > imagesListParameter.imageList.length) {
          for (var j = 0; j < seriesModelsLists[i].imageList.length; j++) {
            if (
              seriesModelsLists[i].imageList[j].imageId == imagesListParameter.imageList[0].imageId
            ) {
              seriesModelsLists[i].imageList[j] = imagesListParameter.imageList[0]
            }
          }
        } else if (seriesModelsLists[i].imageList.length == imagesListParameter.imageList.length) {
          if (
            seriesModelsLists[i].imageList[0].imageId == imagesListParameter.imageList[0].imageId
          ) {
            seriesModelsLists.splice(i, 1)
          }
        }
      } 
    }
    seriesModelsLists.push(imagesListParameter)
  }

  async function getImagesListData() {
    await getImagePageByDoctorId(pageData)
      .then((res) => {
        if (res.success) {
          const data = res.data
          if (data.records) {
            let records = data.records
            records.forEach((item: any) => {
              item.image['imageList'] = item.imageList
              if (item.image.imageCheckTime) {
                item.image.imageCheckTime = formatDate(new Date(item.image.imageCheckTime))
              }
              if (item.image.createTime) {
                item.image.createTime = formatDate(new Date(item.image.createTime))
              }
              tableData.push(item.image)
            })
          }
        } else {
          message(res.msg, { type: 'error' })
        }
      })
      .catch((error) => {
        message(error, { type: 'error' })
      })
  }
  return {
    imageList,

    seriesList,
    seriesLists,
    seriesListWindows,
    segmentationRepresentationUIDList,
    segmentationId,
    seriesModelsLists,

    tableData,
    leftMouseActive,
    toolGroup,
    selectSeriesList,
    selectSeriesWindows,
    selectSeriesModelsList,

    windowRowsColumns,

    renderingEngine,
    viewports,
    viewportColorbar,

    bindLeftMouse,
    bindImageList,
    bindSeriesList,
    getImagesListData,
    pushSeriesList,
    pushSeriesModelsList
  }
})
