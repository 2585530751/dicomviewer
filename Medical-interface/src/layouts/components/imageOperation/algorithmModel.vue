<script setup lang="ts">
import arrowDown from '@iconify-icons/ep/arrow-down'
import { useImageOperationStateStore } from '@/store/imageOperationState'
import { imageKeyValueStore } from '@/composables/image/imageKeyValueStore'
import {
  imageSegmentationOfThyroidNodulesApi,
  imageClassifyOfThyroidNodulesApi,
  imageDetectionOfPulmonaryNodulesApi,
  seriesSegmentationOfThyroidNodulesApi,
  seriesDetectionOfPulmonaryNodulesApi,
  seriesClassifyOfThyroidNodulesApi,
  imageIntestinalPolypsSegmentationApi,
  seriesIntestinalPolypsSegmentationApi
} from '@/api/image'
import { message } from '@/utils/message'
import { pushseriesModelsListsSession } from '@/composables/image/utils'

import { computed, onMounted, reactive, ref, watch } from 'vue'

import pieChart from '@/components/ReChart/pieChart.vue'
import type { ImageInfo, SeriesInfo } from '@/types/series'
import type { ImageModelResult } from '@/types/model.d.ts'
import { changeSeriesListWindowsToSession } from '@/composables/image/utils'
import type { SeriesInfoWindows } from '@/types/image'

const dialogVisible = ref(false)

const pieData = reactive([{ value: 0, name: '良性' }])
const pieName = ref('')
const pieDescription = ref('')

const imageOperationStateStore = useImageOperationStateStore()

async function imageSegmentationOfThyroidNodules() {
  const seriesInfoWindows =
    imageOperationStateStore.seriesListWindows[imageOperationStateStore.selectSeriesWindows]
  if (seriesInfoWindows != 0) {
    const params = {
      imageId: imageKeyValueStore.get(
        imageOperationStateStore.viewports[
          imageOperationStateStore.selectSeriesWindows
        ].getCurrentImageId()
      ).imageId
    }
    await imageSegmentationOfThyroidNodulesApi(params)
      .then((data) => {
        if ((data.code = 200)) {
          const dataResult: ImageModelResult = data.data as ImageModelResult
          const seriesInfo: SeriesInfo = JSON.parse(JSON.stringify(seriesInfoWindows.seriesInfo))
          const imageInfo: ImageInfo = Object.assign(
            {},
            seriesInfo.imageList.find((obj: ImageInfo) => obj.imageId === params.imageId)
          )
          imageInfo.imageModelData = dataResult
          imageInfo.modelType = 'model'
          seriesInfo.imageList = [imageInfo]
          seriesInfo.seriesModelType = 'segmentModel'
          seriesInfo.seriesFeature = [dataResult.imageFeature]
          pushseriesModelsListsSession(seriesInfo)
          imageOperationStateStore.pushSeriesModelsList(seriesInfo)

          changeSeriesListWindowsToSessionAndSeriesInfoWindows(seriesInfo, 'segmentModel')
          message(data.msg, { type: 'success' })
        } else {
          message(data.msg, { type: 'error' })
        }
      })
      .catch((error) => {
        message(error, { type: 'error' })
      })
  }
}

async function seriesSegmentationOfThyroidNodules() {
  const seriesInfoWindows =
    imageOperationStateStore.seriesListWindows[imageOperationStateStore.selectSeriesWindows]
  if (seriesInfoWindows != 0) {
    const params = {
      seriesId: imageKeyValueStore.get(
        imageOperationStateStore.viewports[
          imageOperationStateStore.selectSeriesWindows
        ].getCurrentImageId()
      ).seriesId
    }

    await seriesSegmentationOfThyroidNodulesApi(params)
      .then((data) => {
        if ((data.code = 200)) {
          const dataResult: ImageModelResult[] = data.data as ImageModelResult[]
          const seriesInfo: SeriesInfo = JSON.parse(JSON.stringify(seriesInfoWindows.seriesInfo))
          const seriesFeature = []
          for (var i = 0; i < dataResult.length; i++) {
            seriesFeature.push(dataResult[i].imageFeature)
            let j = 0
            while (j < seriesInfo.imageList.length) {
              if (seriesInfo.imageList[j].imageId == dataResult[i].imageId) {
                break
              }
              j++
            }
            seriesInfo.imageList[j].imageModelData = dataResult[i]
            seriesInfo.imageList[j].modelType = 'model'
          }
          seriesInfo.seriesModelType = 'segmentModel'
          seriesInfo.seriesFeature = seriesFeature
          pushseriesModelsListsSession(seriesInfo)
          imageOperationStateStore.pushSeriesModelsList(seriesInfo)

          changeSeriesListWindowsToSessionAndSeriesInfoWindows(seriesInfo, 'segmentModel')
          message(data.msg, { type: 'success' })
        } else {
          message(data.msg, { type: 'error' })
        }
      })
      .catch((error) => {
        message(error, { type: 'error' })
      })
  }
}

async function imageClassifyOfThyroidNodules() {
  const seriesInfoWindows =
    imageOperationStateStore.seriesListWindows[imageOperationStateStore.selectSeriesWindows]
  if (seriesInfoWindows != 0) {
    const params = {
      imageId: imageKeyValueStore.get(
        imageOperationStateStore.viewports[
          imageOperationStateStore.selectSeriesWindows
        ].getCurrentImageId()
      ).imageId
    }
    await imageClassifyOfThyroidNodulesApi(params)
      .then((data) => {
        if ((data.code = 200)) {
          console.log(data)
          const dataResult: ImageModelResult = data.data as ImageModelResult
          const messageText = '该图像诊断的甲状腺结节分类为' + dataResult.resultDes + '。'
          message(data.msg + messageText, { type: 'success' })
        } else {
          message(data.msg, { type: 'error' })
        }
      })
      .catch((error) => {
        message(error, { type: 'error' })
      })
  }
}

async function seriesClassifyOfThyroidNodules() {
  const seriesInfoWindows =
    imageOperationStateStore.seriesListWindows[imageOperationStateStore.selectSeriesWindows]
  if (seriesInfoWindows != 0) {
    const params = {
      seriesId: imageKeyValueStore.get(
        imageOperationStateStore.viewports[
          imageOperationStateStore.selectSeriesWindows
        ].getCurrentImageId()
      ).seriesId
    }
    await seriesClassifyOfThyroidNodulesApi(params)
      .then((data) => {
        if ((data.code = 200)) {
          const dataResult: ImageModelResult = data.data as ImageModelResult
          console.log(data)
          var benignNum = 0
          var malignantNum = 0
          for (var i = 0; i < data.data.length; i++) {
            if (dataResult.resultDes == '良性') {
              benignNum++
            } else {
              malignantNum++
            }
          }
          pieData[0] = { value: benignNum, name: '良性' }
          pieData[1] = { value: malignantNum, name: '恶性' }
          pieName.value = '甲状腺结节良恶性分类结果'
          const sum = benignNum + malignantNum
          const benignNumPercentage = ((benignNum / sum) * 100).toFixed(2) // 保留两位小数
          const malignantNumPercentage = ((malignantNum / sum) * 100).toFixed(2) // 保留两位小数
          pieDescription.value =
            '该序列良性结节数量为' +
            benignNum +
            '，恶性结节数量为' +
            malignantNum +
            '，因此，该序列器官的诊断结果有' +
            benignNumPercentage +
            '%的概率为良性，有' +
            malignantNumPercentage +
            '%的概率为恶性。'
          dialogVisible.value = true

          message(data.msg, { type: 'success' })
        } else {
          message(data.msg, { type: 'error' })
        }
      })
      .catch((error) => {
        message(error, { type: 'error' })
      })
  }
}

async function imageDetectionOfPulmonaryNodules() {
  const seriesInfoWindows =
    imageOperationStateStore.seriesListWindows[imageOperationStateStore.selectSeriesWindows]
  if (seriesInfoWindows != 0) {
    const params = {
      imageId: imageKeyValueStore.get(
        imageOperationStateStore.viewports[
          imageOperationStateStore.selectSeriesWindows
        ].getCurrentImageId()
      ).imageId
    }
    await imageDetectionOfPulmonaryNodulesApi(params)
      .then((data) => {
        if ((data.code = 200)) {
          const dataResult: ImageModelResult = data.data as ImageModelResult
          const seriesInfo: SeriesInfo = JSON.parse(JSON.stringify(seriesInfoWindows.seriesInfo))
          const imageInfo: ImageInfo = Object.assign(
            {},
            seriesInfo.imageList.find((obj: ImageInfo) => obj.imageId === params.imageId)
          )
          imageInfo.imageModelData = dataResult
          imageInfo.modelType = 'model'
          seriesInfo.imageList = [imageInfo]
          seriesInfo.seriesModelType = 'detectModel'
          pushseriesModelsListsSession(seriesInfo)
          imageOperationStateStore.pushSeriesModelsList(seriesInfo)

          changeSeriesListWindowsToSessionAndSeriesInfoWindows(seriesInfo, 'detectModel')
          message(data.msg, { type: 'success' })
        } else {
          message(data.msg, { type: 'error' })
        }
      })
      .catch((error) => {
        message(error, { type: 'error' })
      })
  }
}
async function seriesDetectionOfPulmonaryNodules() {
  const seriesInfoWindows =
    imageOperationStateStore.seriesListWindows[imageOperationStateStore.selectSeriesWindows]
  if (seriesInfoWindows != 0) {
    const params = {
      seriesId: imageKeyValueStore.get(
        imageOperationStateStore.viewports[
          imageOperationStateStore.selectSeriesWindows
        ].getCurrentImageId()
      ).seriesId
    }
    await seriesDetectionOfPulmonaryNodulesApi(params)
      .then((data) => {
        if ((data.code = 200)) {
          console.log(data)
          const dataResult: ImageModelResult[] = data.data as ImageModelResult[]
          const seriesInfo: SeriesInfo = JSON.parse(JSON.stringify(seriesInfoWindows.seriesInfo))
          for (var i = 0; i < dataResult.length; i++) {
            let j = 0
            while (j < seriesInfo.imageList.length) {
              if (seriesInfo.imageList[j].imageId == dataResult[i].imageId) {
                break
              }
              j++
            }
            seriesInfo.imageList[j].imageModelData = dataResult[i]
            seriesInfo.imageList[j].modelType = 'model'
          }
          seriesInfo.seriesModelType = 'detectModel'
          pushseriesModelsListsSession(seriesInfo)
          imageOperationStateStore.pushSeriesModelsList(seriesInfo)

          changeSeriesListWindowsToSessionAndSeriesInfoWindows(seriesInfo, 'detectModel')
          message(data.msg, { type: 'success' })
        } else {
          message(data.msg, { type: 'error' })
        }
      })
      .catch((error) => {
        message(error, { type: 'error' })
      })
  }
}

async function imageIntestinalPolypsSegmentation() {
  const seriesInfoWindows =
    imageOperationStateStore.seriesListWindows[imageOperationStateStore.selectSeriesWindows]
  if (seriesInfoWindows != 0) {
    const params = {
      imageId: imageKeyValueStore.get(
        imageOperationStateStore.viewports[
          imageOperationStateStore.selectSeriesWindows
        ].getCurrentImageId()
      ).imageId
    }
    await imageIntestinalPolypsSegmentationApi(params)
      .then((data) => {
        if ((data.code = 200)) {
          const dataResult: ImageModelResult = data.data as ImageModelResult
          const seriesInfo: SeriesInfo = JSON.parse(JSON.stringify(seriesInfoWindows.seriesInfo))
          const imageInfo: ImageInfo = Object.assign(
            {},
            seriesInfo.imageList.find((obj: ImageInfo) => obj.imageId === params.imageId)
          )
          imageInfo.imageModelData = dataResult
          imageInfo.modelType = 'model'
          seriesInfo.imageList = [imageInfo]
          seriesInfo.seriesModelType = 'segmentModel'
          seriesInfo.seriesFeature = [dataResult.imageFeature]
          pushseriesModelsListsSession(seriesInfo)
          imageOperationStateStore.pushSeriesModelsList(seriesInfo)

          changeSeriesListWindowsToSessionAndSeriesInfoWindows(seriesInfo, 'segmentModel')
          message(data.msg, { type: 'success' })
        } else {
          message(data.msg, { type: 'error' })
        }
      })
      .catch((error) => {
        message(error, { type: 'error' })
      })
  }
}

async function seriesIntestinalPolypsSegmentation() {
  const seriesInfoWindows =
    imageOperationStateStore.seriesListWindows[imageOperationStateStore.selectSeriesWindows]
  if (seriesInfoWindows != 0) {
    const params = {
      seriesId: imageKeyValueStore.get(
        imageOperationStateStore.viewports[
          imageOperationStateStore.selectSeriesWindows
        ].getCurrentImageId()
      ).seriesId
    }

    await seriesIntestinalPolypsSegmentationApi(params)
      .then((data) => {
        if ((data.code = 200)) {
          const dataResult: ImageModelResult[] = data.data as ImageModelResult[]
          const seriesInfo: SeriesInfo = JSON.parse(JSON.stringify(seriesInfoWindows.seriesInfo))
          const seriesFeature = []
          for (var i = 0; i < dataResult.length; i++) {
            seriesFeature.push(dataResult[i].imageFeature)

            let j = 0
            while (j < seriesInfo.imageList.length) {
              if (seriesInfo.imageList[j].imageId == dataResult[i].imageId) {
                break
              }
              j++
            }
            seriesInfo.imageList[j].imageModelData = dataResult[i]
            seriesInfo.imageList[j].modelType = 'model'
          }
          seriesInfo.seriesModelType = 'segmentModel'
          seriesInfo.seriesFeature = seriesFeature
          pushseriesModelsListsSession(seriesInfo)
          imageOperationStateStore.pushSeriesModelsList(seriesInfo)

          changeSeriesListWindowsToSessionAndSeriesInfoWindows(seriesInfo, 'segmentModel')
          message(data.msg, { type: 'success' })
        } else {
          message(data.msg, { type: 'error' })
        }
      })
      .catch((error) => {
        message(error, { type: 'error' })
      })
  }
}

function getSeriesInfoWindows(seriesId: number) {
  return JSON.parse(
    JSON.stringify(
      imageOperationStateStore.seriesLists.find((obj: SeriesInfo) => obj.seriesId == seriesId)
    )
  )
}

function changeSeriesListWindowsToSessionAndSeriesInfoWindows(
  seriesInfo: SeriesInfo,
  seriesModelType: string
) {
  const seriesListWindows = imageOperationStateStore.seriesListWindows
  for (var i = 0; i < seriesListWindows.length; i++) {
    if (seriesListWindows[i] != 0) {
      const seriesListWindow: SeriesInfoWindows = seriesListWindows[i] as SeriesInfoWindows
      if (seriesListWindow.seriesInfo.seriesModelType == seriesModelType) {
        if (
          seriesListWindow.seriesInfo.seriesId == seriesInfo.seriesId &&
          seriesListWindow.seriesInfo.imageList[0].imageModelData?.modelId ==
            seriesInfo.imageList[0].imageModelData?.modelId
        ) {
          if (seriesListWindow.seriesInfo.imageList.length < seriesInfo.imageList.length) {
            for (var j = 0; j < seriesInfo.imageList.length; j++) {
              if (
                seriesListWindow.seriesInfo.imageList[0].imageId == seriesInfo.imageList[j].imageId
              ) {
                seriesListWindow.seriesInfo.imageList[0] = seriesInfo.imageList[j]
              }
            }
          } else if (seriesListWindow.seriesInfo.imageList.length > seriesInfo.imageList.length) {
            for (var j = 0; j < seriesListWindow.seriesInfo.imageList.length; j++) {
              if (
                seriesListWindow.seriesInfo.imageList[j].imageId == seriesInfo.imageList[0].imageId
              ) {
                seriesListWindow.seriesInfo.imageList[j] = seriesInfo.imageList[0]
              }
            }
          } else if (seriesListWindow.seriesInfo.imageList.length == seriesInfo.imageList.length) {
            if (
              seriesListWindow.seriesInfo.imageList[0].imageId == seriesInfo.imageList[0].imageId
            ) {
              seriesListWindow.seriesInfo = seriesInfo
            }
          }
          changeSeriesListWindowsToSession(seriesListWindow, i)
        }
      }
    }
  }
}

const computedCheckWindowsType = computed(() => {
  const selectSeriesWindow =
    imageOperationStateStore.seriesListWindows[imageOperationStateStore.selectSeriesWindows]
  if (selectSeriesWindow != 0) {
    if (
      selectSeriesWindow.seriesInfo.seriesModelType == 'segmentModel' ||
      selectSeriesWindow.seriesInfo.seriesModelType == 'detectModel'
    ) {
      return false
    } else {
      return true
    }
  }else{
    return false
  }
})
</script>

<template>
  <div
    class="divide-x-0 divide-y-2 divide-slate-400/50 divide-solid"
    v-show="computedCheckWindowsType"
  >
    <div class="flex flex-wrap justify-center bg-stone-50 dark:border-gray-700 dark:bg-gray-800">
      <div class="flex items-center">
        <p class="inline ml-3 text-sm text-gray-600 dark:text-white">甲状腺结节图像分割</p>
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
              <el-dropdown-item @click="imageSegmentationOfThyroidNodules"
                >图像分割</el-dropdown-item
              >
              <el-dropdown-item @click="seriesSegmentationOfThyroidNodules"
                >序列分割</el-dropdown-item
              >
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>
    <div class="flex flex-wrap justify-center bg-stone-50 dark:border-gray-700 dark:bg-gray-800">
      <div class="flex items-center">
        <p class="inline ml-3 text-sm text-gray-600 dark:text-white">肺结节图像检测</p>
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
              <el-dropdown-item @click="imageDetectionOfPulmonaryNodules"
                >图像检测</el-dropdown-item
              >
              <el-dropdown-item @click="seriesDetectionOfPulmonaryNodules"
                >序列检测</el-dropdown-item
              >
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>
    <div class="flex flex-wrap justify-center bg-stone-50 dark:border-gray-700 dark:bg-gray-800">
      <div class="flex items-center">
        <p class="inline ml-3 text-sm text-gray-600 dark:text-white">甲状腺结节良恶分类</p>
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
              <el-dropdown-item @click="imageClassifyOfThyroidNodules">图像分类</el-dropdown-item>
              <el-dropdown-item @click="seriesClassifyOfThyroidNodules">序列分类</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>

    <div class="flex flex-wrap justify-center bg-stone-50 dark:border-gray-700 dark:bg-gray-800">
      <div class="flex items-center">
        <p class="inline ml-3 text-sm text-gray-600 dark:text-white">肠道息肉图像分割</p>
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
              <el-dropdown-item @click="imageIntestinalPolypsSegmentation"
                >图像分类</el-dropdown-item
              >
              <el-dropdown-item @click="seriesIntestinalPolypsSegmentation"
                >序列分类</el-dropdown-item
              >
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>

    <el-dialog v-model="dialogVisible" title="甲状腺结节良性恶性分类结果" width="500">
      <pie-chart :seriesName="pieName" :data="pieData" :description="pieDescription"></pie-chart>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="dialogVisible = false"> 确定 </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<style lang="scss" scoped></style>
