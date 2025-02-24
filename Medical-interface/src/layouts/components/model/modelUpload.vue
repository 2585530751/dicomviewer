<script setup lang="ts">
import { reactive, ref, watch } from 'vue'
import { UploadFilled } from '@element-plus/icons-vue'
import { type UploadRequestOptions, type UploadProps, type UploadRawFile } from 'element-plus'
import { message } from '@/utils/message'
import type { UploadUserFile } from 'element-plus'
import type { UploadInstance } from 'element-plus'
import { uploadImages } from '@/api/image'
import { Delete, Download, Plus, ZoomIn } from '@element-plus/icons-vue'
import { uploadModelInfoApi } from '@/api/model'
import { genFileId } from 'element-plus'
import {type ModelInfo } from '@/types/model'

defineOptions({
  name: ''
})

const props = defineProps<{
  uploadWindowOpen?: boolean
  options: string[]
}>()

const emits = defineEmits<{
  uploadWindowClose: [] // 具名元组语法
  uploadModelInfoAction: [modelInfo: ModelInfo] // 具名元组语法
}>()

const imageViewCheck = ref(false)

const modelInfo = reactive({
  modelName: '',
  modelPattern: '',
  modelCodeLink: '',
  modelPaperLink: '',
  modelImage: '',
  modelAbstract: '',
  modelDescription: ''
})
const uploadRef = ref<UploadInstance>()
const handleChange = (file: any, fileList: any) => {
  modelInfo.modelImage = URL.createObjectURL(file.raw)
}

const handleExceed: UploadProps['onExceed'] = (files) => {
  uploadRef.value!.clearFiles()
  const file = files[0] as UploadRawFile
  file.uid = genFileId()
  uploadRef.value!.handleStart(file)
}

const submitUpload = () => {
  if (modelInfo.modelImage === '') {
    message('请上传模型图片', { type: 'error' })
    return
  }
  uploadRef.value!.submit()
}

const headers: Record<string, any> = {
  'Content-Type': 'multipart/form-data'
}

let centerDialogVisible = ref(props.uploadWindowOpen)

watch(
  () => {
    return props.uploadWindowOpen
  },
  (value, prevValue) => {
    centerDialogVisible.value = value
  }
)

function uploadModelInfo(options: UploadRequestOptions) {
  const formData = new FormData()
  formData.append('modelImage', options.file)
  formData.append('modelInfo', JSON.stringify(modelInfo))
  uploadModelInfoApi(formData)
    .then((res) => {
      if (res.success) {
        message(res.msg, { type: 'success' })
        URL.revokeObjectURL(modelInfo.modelImage)
        emits('uploadWindowClose')
        emits('uploadModelInfoAction', res.data)
      } else {
        message(res.msg, { type: 'error' })
      }
    })
    .catch((error) => {
      message(error, { type: 'error' })
    })
}
</script>

<template>
  <el-dialog v-model="centerDialogVisible" @close="$emit('uploadWindowClose')" title="上传图像" width="60%" center>
    <el-form ref="ruleFormRef" :model="modelInfo" label-position="top">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="模型名称" prop="modelName">
            <el-input v-model="modelInfo.modelName" placeholder="请输入模型名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="模型类型" prop="modelPattern">
            <el-select v-model="modelInfo.modelPattern" placeholder="请输入模型类型" class="w-full">
              <el-option v-for="(item, index) in props.options" :key="index" :label="item" :value="item" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="模型代码链接" prop="modelCodeLink">
            <el-input v-model="modelInfo.modelCodeLink" placeholder="请输入模型代码链接" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="模型论文链接" prop="modelPaperLink">
            <el-input v-model="modelInfo.modelPaperLink" placeholder="请输入模型论文链接" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="模型显示图片" prop="modelImage">
            <el-upload ref="uploadRef" :limit="1" :on-exceed="handleExceed" :on-change="handleChange" :auto-upload="false"
              :show-file-list="false" :http-request="uploadModelInfo" :headers="headers"
              v-show="modelInfo.modelImage === ''">
              <template #trigger>
                <div
                  class="flex items-center justify-center w-24 h-24 text-lg border border-gray-500 border-dashed rounded hover:text-blue-400 hover:border-blue-400">
                  <el-icon>
                    <Plus />
                  </el-icon>
                </div>
              </template>

              <template #tip>
                <div class="el-upload__tip">上传小于500kb的jpg/png文件</div>
              </template>
            </el-upload>
            <div v-show="modelInfo.modelImage !== ''"
              class="flex items-center justify-center gap-2 text-2xl bg-cover border border-gray-500 border-dashed rounded w-36 h-36 bg-origin-border bg-inherit"
              :style="{ backgroundImage: `url(${modelInfo.modelImage})` }">
              <el-icon class="hover:text-blue-400" @click="imageViewCheck = true"><zoom-in /></el-icon>
              <el-icon class="hover:text-blue-400" @click="modelInfo.modelImage = ''">
                <Delete />
              </el-icon>
              <el-image-viewer :url-list="[modelInfo.modelImage]" v-if="imageViewCheck" @close="imageViewCheck = false" />
            </div>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="模型摘要" prop="modelAbstract">
            <el-input v-model="modelInfo.modelAbstract" style="width: 100%" :autosize="{ minRows: 5, maxRows: 10 }"
              type="textarea" placeholder="请输入模型摘要" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="模型详情" prop="modelDescription">
            <el-input v-model="modelInfo.modelDescription" style="width: 100%" :autosize="{ minRows: 5, maxRows: 20 }"
              type="textarea" placeholder="请输入模型详情" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="centerDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitUpload()"> 上传 </el-button>
      </span>
    </template>
  </el-dialog>
</template>

<style lang="scss" scoped>
.dialog-footer button:first-child {
  margin-right: 10px;
}
</style>
