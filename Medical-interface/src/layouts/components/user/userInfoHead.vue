<script setup lang="ts">
import { setHeadIconApi, getHeadIconApi } from '@/api/user'
import { ref, onMounted, reactive } from 'vue'
import { type UploadRequestOptions, type UploadInstance } from 'element-plus'
import { message } from '@/utils/message'
import select from '@/assets/svg/select.svg?component'
import upload from '@/assets/svg/upload.svg?component'
import { Iphone, Location, OfficeBuilding, Tickets, User } from '@element-plus/icons-vue'
import { getUserInformationApi } from '@/api/user'
import { basicImageUrl} from '@/api/utils'
import { CodeToText } from '@/utils/chinaArea'

interface userInfo {
  account: string
  email: string
  address: string
  phoneNumber: string
  userName: string
  place: string[]
}

const codeToText: Record<string, string> = CodeToText as unknown as Record<string, string>

const imageUrl = ref('')
const errorHandler = () => {
  imageUrl.value =  '/images/emptyAvatar.jpg'
}

const uploadRef = ref<UploadInstance>()

const submitUpload = () => {
  uploadRef.value!.submit()
}

const handleChange = (file: any, fileList: any) => {
  imageUrl.value = URL.createObjectURL(file.raw)
} //生成图片url

const uploadAvatar = (options: UploadRequestOptions) => {
  const formData = new FormData()
  formData.append('file', options.file)
  setHeadIconApi(formData)
    .then((data) => {
      if (data.success == true) {
        message(data.msg, { type: 'success' })
      } else {
        message(data.msg, { type: 'error' })
      }
    })
    .catch((error) => {
      message(error, { type: 'error' })
    })
}

const userInfo = reactive<userInfo>({
  account: '',
  email: '',
  address: '',
  phoneNumber: '',
  userName: '',
  place: ['', '', '']
})

onMounted(() => {
  getUserInformation()
  getHeadIcon()
})

function getUserInformation() {
  //将个人信息界面中用户信息显示到表格中
  getUserInformationApi().then((data) => {
    if (data.success == true) {
      userInfo.address = data.data.address
      userInfo.email = data.data.email
      userInfo.phoneNumber = data.data.phoneNumber
      const placeStr = data.data.place
      if (placeStr != undefined) {
        userInfo.place[0] = placeStr.substring(0, 2) + '0000'
        if (placeStr.substring(0, 2) == '82' || placeStr.substring(0, 2) == '81') {
          userInfo.place[1] = placeStr
          userInfo.place.splice(2)
        } else {
          userInfo.place[1] = placeStr.substring(0, 4) + '00'
          userInfo.place[2] = placeStr
        }
      }
      userInfo.userName = data.data.userName
      userInfo.account = data.data.account
    }
  })
}

function getHeadIcon() {
  getHeadIconApi().then((data) => {
    imageUrl.value = basicImageUrl + data.msg
  })
}
</script>

<template>
  <el-card>
    <div class="flex items-center justify-center gap-10">
      <el-descriptions title="用户名" :column="3" size="large" border>
        <el-descriptions-item min-width="100px">
          <template #label>
            <div class="cell-item">
              <el-icon class="text-lg">
                <user />
              </el-icon>
              用户名
            </div>
          </template>
          {{ userInfo.userName }}
        </el-descriptions-item>
        <el-descriptions-item min-width="100px">
          <template #label>
            <div class="cell-item">
              <el-icon class="text-lg">
                <iphone />
              </el-icon>
              电话
            </div>
          </template>
          {{ userInfo.phoneNumber }}
        </el-descriptions-item>
        <el-descriptions-item min-width="100px">
          <template #label>
            <div class="cell-item">
              <el-icon class="text-lg">
                <location />
              </el-icon>
              籍贯
            </div>
          </template>
          {{
            userInfo.place[0] == ''
              ? ''
              : codeToText[userInfo.place[0]] +
                codeToText[userInfo.place[1]] +
                codeToText[userInfo.place[2]]
          }}
        </el-descriptions-item>
        <el-descriptions-item min-width="100px">
          <template #label>
            <div class="cell-item">
              <el-icon class="text-lg">
                <tickets />
              </el-icon>
              邮箱
            </div>
          </template>
          {{ userInfo.email }}
        </el-descriptions-item>
        <el-descriptions-item min-width="100px">
          <template #label>
            <div class="cell-item">
              <el-icon class="text-lg">
                <office-building />
              </el-icon>
              地址
            </div>
          </template>
          {{ userInfo.address }}
        </el-descriptions-item>
      </el-descriptions>

      <div class="flex gap-3">
        <el-avatar
          shape="square"
          :size="150"
          :src="imageUrl"
          @error="errorHandler"
          class="inline-block"
        >
          <img :src="imageUrl" />
        </el-avatar>
        <div class="flex flex-col justify-center gap-4">
          <el-upload
            ref="uploadRef"
            class="upload-demo"
            :on-change="handleChange"
            :auto-upload="false"
            :show-file-list="false"
            :http-request="uploadAvatar"
          >
            <el-button type="default" :icon="select">选择头像</el-button>
          </el-upload>
          <el-button type="default" @click="submitUpload" :icon="upload">上传头像</el-button>
        </div>
      </div>
    </div>
  </el-card>
</template>

<style lang="scss" scoped>
.el-descriptions {
  margin-top: 0px;
  padding-top: 0px;
}

.cell-item {
  display: flex;
  align-items: center;
}
</style>
