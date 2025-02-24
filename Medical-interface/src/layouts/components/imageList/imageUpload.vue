<script setup lang="ts">
import { reactive, ref, watch } from 'vue'
import { Delete, ZoomIn, Download } from '@element-plus/icons-vue'
import { UploadFilled } from '@element-plus/icons-vue'
import { type UploadFile } from 'element-plus'
import { message } from '@/utils/message'
import type { UploadUserFile } from 'element-plus'
import type { UploadInstance } from 'element-plus'
import { uploadImages } from '@/api/image'
import { useImageOperationStateStore } from '@/store/imageOperationState'

const imageOperationStateStore =useImageOperationStateStore()

const dialogImageUrl = ref('')
const dialogVisible = ref(false)
const disabled = ref(false)

const imagesList = ref<UploadUserFile[]>([])
const isShow = ref(true)
const imagesInfo = reactive({
  patientName: '',
  patientIdCardNumber: '',
  patientGender: '',
  patientWeight: '',
  patientAge: '',
  imageCheckPart: '',
  imageCheckTime: '',
  imageDesc: '',
  imageEquipment: ''
})
//hello
const handleRemove = (file: UploadFile) => {
  console.log(file)
  let index = imagesList.value.indexOf(file)
  imagesList.value.splice(index, 1)
}

const headers: Record<string, any> = {
  'Content-Type': 'multipart/form-data'
}

const handlePictureCardPreview = (file: UploadFile) => {
  dialogImageUrl.value = file.url!
  dialogVisible.value = true
}
const uploadRef = ref<UploadInstance>()

// const submitUpload = () => {
//   uploadRef.value!.submit()
// }

const props = defineProps<{
  uploadWindowOpen?: boolean
}>()

const emits = defineEmits<{
  uploadWindowClose: [] // 具名元组语法
}>()

let centerDialogVisible = ref(props.uploadWindowOpen)

watch(
  () => {
    return props.uploadWindowOpen
  },
  (value, prevValue) => {
    centerDialogVisible.value = value
  }
)

// 提交上传文件
function submitFileForm() {
  // 创建新的数据对象
  let formData = new FormData()
  // 将上传的文件放到数据对象中
  imagesList.value.forEach((file) => {
    formData.append('files', file.raw!)
  })
  // 文件名
  formData.append('imagesInfo', JSON.stringify(imagesInfo))
  uploadImages(formData)
    .then((res) => {
      if (res.success) {
        imageOperationStateStore.tableData.length = 0
        imageOperationStateStore.getImagesListData()
        emits('uploadWindowClose')
      } else {
        message(res.msg, { type: 'error' })
      }
    })
    .catch((error) => {
      message(error, { type: 'error' })
    })
}

function handleDownload(file: UploadFile) {
  console.log('file', file)
  var link = document.createElement('a') //定义一个a标签
  link.download = file.name //下载后的文件名称
  link.href = file.url ?? '' //需要生成一个 URL 来实现下载
  link.click() //模拟在按钮上实现一次鼠标点击
  window.URL.revokeObjectURL(link.href)
}
</script>

<template>
  <el-dialog
    v-model="centerDialogVisible"
    @close="$emit('uploadWindowClose')"
    title="上传图像"
    width="60%"
    center
  >
    <div v-show="isShow">
      <el-form ref="ruleFormRef" :model="imagesInfo" label-position="top">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="患者姓名" prop="patientName">
              <el-input v-model="imagesInfo.patientName" placeholder="请输入患者姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="身份证号" prop="patientIdCardNumber">
              <el-input v-model="imagesInfo.patientIdCardNumber" placeholder="请输入患者身份证号" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="患者性别" prop="patientGender">
              <el-radio-group v-model="imagesInfo.patientGender">
                <el-radio label="男性" value="男" />
                <el-radio label="女性" value="女" />
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="检查时间" prop="imageCheckTime">
              <el-date-picker
                v-model="imagesInfo.imageCheckTime"
                type="date"
                placeholder="请输入成像时间"
                clearable
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="患者体重(KG)" prop="patientWeight">
              <el-input v-model="imagesInfo.patientWeight" placeholder="请输入患者体重" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="患者年龄(Y)" prop="patientAge">
              <el-input v-model="imagesInfo.patientAge" placeholder="请输入患者年龄" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="成像部位" prop="imageCheckPart">
              <el-input v-model="imagesInfo.imageCheckPart" placeholder="请输入成像部位" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="成像设备" prop="imageEquipment">
              <el-input v-model="imagesInfo.imageEquipment" placeholder="请输入成像设备" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="图像描述" prop="imageDesc">
          <el-input v-model="imagesInfo.imageDesc" type="textarea" placeholder="请输入成像描述" />
        </el-form-item>
      </el-form>
    </div>
    <div v-show="!isShow">
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
            <img class="el-upload-list__item-thumbnail" :src="file.url" :alt="file.name" v-show="!file.name.endsWith('dcm')"/>
            <div class="text-lg text-justify pt-14 ">{{ file.name }}</div>
            <span class="el-upload-list__item-actions">
              <span class="el-upload-list__item-preview" v-show="!file.name.endsWith('dcm')" @click="handlePictureCardPreview(file)">
                <el-icon><zoom-in /></el-icon>
              </span>
              <span
                v-if="!disabled"
                class="el-upload-list__item-delete"
                @click="handleDownload(file)"
              >
                <el-icon><Download /></el-icon>
              </span>
              <span
                v-if="!disabled"
                class="el-upload-list__item-delete"
                @click="handleRemove(file)"
              >
                <el-icon><Delete /></el-icon>
              </span>
            </span>
          </div>
        </template>
      </el-upload>

      <el-dialog v-model="dialogVisible" width="50%" center>
        <img class="w-full" :src="dialogImageUrl" alt="Preview Image" />
      </el-dialog>
    </div>
    <template #footer>
      <div v-show="isShow">
        <span class="dialog-footer">
          <el-button @click="centerDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="isShow = false"> 下一步 </el-button>
        </span>
      </div>
      <div v-show="!isShow">
        <span class="dialog-footer">
          <el-button @click="isShow = true">上一步</el-button>
          <el-button type="primary" @click="submitFileForm()"> 上传 </el-button>
        </span>
      </div>
    </template>
  </el-dialog>
</template>

<style lang="scss" scoped>
.dialog-footer button:first-child {
  margin-right: 10px;
}
</style>