<script setup lang="ts">
import type { TableInstance } from 'element-plus'
import { onMounted, reactive, ref } from 'vue'
import editPen from '@iconify-icons/ep/edit-pen'
import deleteUser from '@iconify-icons/ep/delete'
import IonEllipsisHorizontal from '@/assets/svg/IonEllipsisHorizontal.svg?component'
import { getImagePageByDoctorId, deleteSingleImageById, deleteImageById } from '@/api/image'
import router from '@/router'
import { basicImageUrl } from '@/api/utils'
import { useImageOperationStateStore } from '@/store/imageOperationState'
import { changeSeriesListWindowsToSession, pushSeriesToSession } from '@/composables/image/utils'
import type {  SeriesInfoWindows } from '@/types/image'
import imageDicom from '@/components/ReImage/imageDicom.vue'
import { message } from '@/utils/message'

defineProps<{
  tableSize: string
}>()

const imageOperationStateStore =useImageOperationStateStore()

const tableRef = ref<TableInstance>()

onMounted(async () => {
  // imageOperationStateStore.tableData.length=0
  imageOperationStateStore.getImagesListData()
})

function imageOperation(imageList: SingleImage, imagesList: ImageInfo) {
  imageOperationStateStore.bindImageList(imageList)
  imageOperationStateStore.bindSeriesList(imagesList)
  imageOperationStateStore.pushSeriesList(imagesList)
  const seriesInfoWindows: SeriesInfoWindows = {
    imageInfo: imagesList,
    singleImage: imageList
  }
  imageOperationStateStore.seriesListWindows[imageOperationStateStore.selectSeriesWindows] = seriesInfoWindows
  changeSeriesListWindowsToSession(seriesInfoWindows, imageOperationStateStore.selectSeriesWindows)
  pushSeriesToSession(imagesList)
  router.push('/imageOperation')
}

function imagesOperation(imagesList: ImageInfo) {
  imageOperationStateStore.bindSeriesList(imagesList)
  imageOperationStateStore.bindImageList((imagesList as { singleImageList: any }).singleImageList[0])
  imageOperationStateStore.pushSeriesList(imagesList)
  const seriesInfoWindows: SeriesInfoWindows = {
    imageInfo: imagesList,
    singleImage: imagesList.singleImageList[0]
  }
  imageOperationStateStore.seriesListWindows[imageOperationStateStore.selectSeriesWindows] = seriesInfoWindows
  changeSeriesListWindowsToSession(seriesInfoWindows, imageOperationStateStore.selectSeriesWindows)
  pushSeriesToSession(imagesList)
  router.push('/imageOperation')
}

async function singleImageDelete(imageRow: object, imageId: number) {
  const params = { imageId: imageId }
  const seriesId = imageRow.imageId
  await deleteSingleImageById(params)
    .then((data) => {
      let index = 0
      // 遍历数组
      while (index < imageOperationStateStore.tableData.length) {
        const currentObject = imageOperationStateStore.tableData[index]
        // 如果 imageId 等于 imageId，则使用 splice 方法删除该对象
        if (currentObject.seriesId === seriesId) {
          const currentSingleImageList = currentObject.singleImageList!.filter(
            (object) => object.imageId !== imageId
          )
          imageOperationStateStore.tableData[index].singleImageList = currentSingleImageList
          console.log(imageOperationStateStore.tableData[index].singleImageList)
          break
          // imageOperationStateStore.tableData.splice(index, imageId)
        } else {
          // 如果不需要删除，则移动到下一个元素
          index++
        }
      }
      //imageOperationStateStore.tableData[imagesIndex].singleImageList!.splice(imageIndex,1)
    })
    .catch((error) => {
      console.log(error)
    })
  setTimeout(() => {
    tableRef.value!.toggleRowExpansion(imageRow, true)
  }, 100)
}

function imageDelete(seriesId: number) {
  const params = { imageId: imageId }
  deleteImageById(params).then((res) => {
    if (res.success) {
      let index = 0
      // 遍历数组
      while (index < imageOperationStateStore.tableData.length) {
        const currentObject = imageOperationStateStore.tableData[index]
        // 如果 imageId 等于 imageId，则使用 splice 方法删除该对象
        if (currentObject.imageId === imageId) {
          imageOperationStateStore.tableData.splice(index, 1)
          break
        } else {
          // 如果不需要删除，则移动到下一个元素
          index++
        }
      }
      message('删除成功', { type: 'success' })
    } else {
      message(res.msg, { type: 'error' })
    }
  })
}

</script>

<template>
  <el-card class="box-card">
    <el-table
      ref="tableRef"
      :data="imageOperationStateStore.tableData"
      style="width: 100%"
      :default-sort="{ prop: 'imageCheckTime', order: 'descending' }"
      :size="tableSize"
    >
      <el-table-column type="expand">
        <template #default="props">
          <el-button type="primary">基础阅片</el-button>
          <el-button type="primary">显微镜</el-button>
          
          <div m="4">
            <h3>随访记录</h3>
            <el-table :data="props.row.singleImageList">
              <el-table-column label="图片" width="150px">
                <template #default="scope">
                  <el-image
                    :src="basicImageUrl + scope.row.imagePath"
                    :crossorigin="'anonymous'"
                    v-show="scope.row.imagePath.endsWith('.png')||scope.row.imagePath.endsWith('.jpg')||scope.row.imagePath.endsWith('.jpeg')"
                  />
                  <imageDicom :singleImage="scope.row" v-show="scope.row.imagePath.endsWith('.dcm')"/>
                </template>
              </el-table-column>
              <el-table-column label="图像ID" prop="imageId" />
              <el-table-column label="图像名称" prop="singleImageName" />
              <el-table-column fixed="right" label="操作" >
                <template #default="scope">
                  <el-button
                    link
                    type="primary"
                    size="small"
                    @click="imageOperation(scope.row, props.row)"
                    ><template #icon>
                      <IconifyIconOffline :icon="editPen"></IconifyIconOffline>
                    </template>
                    查看</el-button
                  >
                  <el-popconfirm
                    title="你确定要删除他吗?"
                    @confirm="singleImageDelete(props.row, scope.row.imageId)"
                  >
                    <template #reference>
                      <el-button link type="primary" size="small">
                        <template #icon>
                          <IconifyIconOffline :icon="deleteUser"></IconifyIconOffline>
                        </template>
                        删除</el-button
                      >
                    </template>
                  </el-popconfirm>

                  <el-button :icon="IonEllipsisHorizontal" link type="primary" />
                </template>
              </el-table-column>
            </el-table>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="检查时间" prop="imageCheckTime" sortable />
      <el-table-column label="患者姓名" prop="patientName" />
      <el-table-column label="检查部位" prop="imageCheckPart" />
      <el-table-column label="成像设备" prop="imageEquipment" />
      <el-table-column label="图像描述" prop="imageDesc" />
      <el-table-column label="序列数量" prop="imageCount" />
      <el-table-column label="上传医生" prop="creatorName" />
      <el-table-column label="上传时间" prop="createTime" />
      <el-table-column fixed="right" label="操作" >
        <template #default="scope">
          <el-button link type="primary" size="small" @click="imagesOperation(scope.row)"
            ><template #icon>
              <IconifyIconOffline :icon="editPen"></IconifyIconOffline>
            </template>
            查看</el-button
          >
          <el-popconfirm
            title="你确定要删除他吗?"
            @confirm.prevent="imageDelete(scope.row.imageId)"
          >
            <template #reference>
              <el-button link type="primary" size="small">
                <template #icon>
                  <IconifyIconOffline :icon="deleteUser"></IconifyIconOffline>
                </template>
                删除</el-button
              >
            </template>
          </el-popconfirm>
          <el-button :icon="IonEllipsisHorizontal" link type="primary" />
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<style lang="scss" scoped></style>