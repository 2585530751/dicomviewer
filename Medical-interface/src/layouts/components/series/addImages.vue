<script setup lang="ts">
import { reactive, type Ref, ref, watch, onMounted } from 'vue'
import { type UploadUserFile, type UploadFile } from 'element-plus'
import { message } from '@/utils/message'
import { Delete, ZoomIn, Download, UploadFilled } from '@element-plus/icons-vue'
import { addImagesApi } from '@/api/image'
import { useSeriesStateStore } from '@/store/modules/seriesState'
import { checkFilesType } from '@/utils/commonUtils'

defineOptions({
  name: ''
})

const seriesStateStore = useSeriesStateStore()
const props = defineProps<{
  uploadWindowOpen?: boolean
  dialogPatientId: number
  dialogStudyId: number
  dialogSeriesId: number
  dialogSeriesFileType: string
}>()

const emits = defineEmits<{
  uploadWindowClose: [] // 具名元组语法
}>()

let centerDialogVisible = ref(props.uploadWindowOpen)

const imageInfo = reactive({
  patientId: props.dialogPatientId,
  studyId: props.dialogStudyId,
  seriesId: props.dialogSeriesId
})

watch(
  () => {
    return props.uploadWindowOpen
  },
  (value, prevValue) => {
    centerDialogVisible.value = value
  }
)

async function addImages() {
  if (props.dialogSeriesFileType != null && props.dialogSeriesFileType != '') {
    if (!checkFilesType(imagesList.value, props.dialogSeriesFileType)) return
  }

  let formData = new FormData()
  imagesList.value.forEach((file) => {
    formData.append('files', file.raw!)
  })
  imageInfo.patientId = props.dialogPatientId
  imageInfo.studyId = props.dialogStudyId
  imageInfo.seriesId = props.dialogSeriesId
  formData.append('info', JSON.stringify(imageInfo))
  addImagesApi(formData)
    .then((res) => {
      if (res.success) {
        message('添加成功', { type: 'success' })
        imagesList.value.length = 0
        seriesStateStore.getSeriesListPage()
        emits('uploadWindowClose')
      } else {
        message(res.msg, { type: 'error' })
      }
    })
    .catch((error) => {
      message(error, { type: 'error' })
    })
}

const headers: Record<string, any> = {
  'Content-Type': 'multipart/form-data'
}
const disabled = ref(false)
const imagesList = ref<UploadUserFile[]>([])
const dialogImageUrl = ref('')
const dialogVisible = ref(false)
const handlePictureCardPreview = (file: UploadFile) => {
  dialogImageUrl.value = file.url!
  dialogVisible.value = true
}
function handleDownload(file: UploadFile) {
  console.log('file', file)
  var link = document.createElement('a') //定义一个a标签
  link.download = file.name //下载后的文件名称
  link.href = file.url ?? '' //需要生成一个 URL 来实现下载
  link.click() //模拟在按钮上实现一次鼠标点击
  window.URL.revokeObjectURL(link.href)
}
const handleRemove = (file: UploadFile) => {
  console.log(file)
  let index = imagesList.value.indexOf(file)
  imagesList.value.splice(index, 1)
}
</script>

<template>
  <el-dialog
    v-model="centerDialogVisible"
    @close="$emit('uploadWindowClose')"
    title="新增患者"
    width="60%"
    center
  >
    <el-upload
      list-type="picture-card"
      :auto-upload="false"
      v-model:file-list="imagesList"
      ref="uploadRef"
      :headers="headers"
      show-file-list
      multiple
      drag
    >
      <el-icon class=""><upload-filled /></el-icon>
      <div class="el-upload__text">Drop file here or <em>click to upload</em></div>
      <template #tip>
        <div class="el-upload__tip">jpg/png files with a size less than 500kb</div>
      </template>
      <template #file="{ file }">
        <div @click="console.log(file.raw)">
          <img
            class="el-upload-list__item-thumbnail"
            :src="file.url"
            :alt="file.name"
            v-show="!file.name.endsWith('dcm')"
          />
          <div class="text-lg text-justify pt-14">{{ file.name }}</div>
          <span class="el-upload-list__item-actions">
            <span
              class="el-upload-list__item-preview"
              v-show="!file.name.endsWith('dcm')"
              @click="handlePictureCardPreview(file)"
            >
              <el-icon><zoom-in /></el-icon>
            </span>
            <span
              v-if="!disabled"
              class="el-upload-list__item-delete"
              @click="handleDownload(file)"
            >
              <el-icon><Download /></el-icon>
            </span>
            <span v-if="!disabled" class="el-upload-list__item-delete" @click="handleRemove(file)">
              <el-icon><Delete /></el-icon>
            </span>
          </span>
        </div>
      </template>
    </el-upload>
    <template #footer>
      <el-button>重置</el-button>
      <el-button type="primary" @click="addImages()"> 保存 </el-button>
    </template>
  </el-dialog>
</template>

<style lang="scss" scoped></style>
