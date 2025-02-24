<script setup lang="ts">
defineOptions({
  name: ''
})
import type { IconifyIconOffline } from '@/components/ReIcon'
import { onMounted, reactive, watch } from 'vue'
import { getModelInfoByModelIdApi } from '@/api/model'
import { useRoute } from 'vue-router'
import { basicImageUrl } from '@/api/utils'

import link from '@iconify-icons/ep/link'
import camera from '@iconify-icons/ep/camera'
import { message } from '@/utils/message'

interface information {
  modelName: string
  modelAbstract: string[]
  modelDescription: string[]
  modelImage: string
  modelPaperLink: string
  modelCodeLink: string
}

const route = useRoute()

const modelInformation = reactive<information>({
  modelName: '',
  modelAbstract: [],
  modelDescription: [],
  modelImage: '',
  modelPaperLink: '',
  modelCodeLink: ''
})



function getModelInfoByModelId(modelId: number) {
  const param = { modelId: modelId }
  getModelInfoByModelIdApi(param).then((res: any) => {
    if (res.success) {
      console.log(res.data)
      const abstractList = res.data.modelAbstract.split(/\r?\n|\r/)
      const descriptionList = res.data.modelDescription.split(/\r?\n|\r/)
      const filterAbstractList = abstractList.filter((item: string) => item.trim() !== '')
      const filterDescriptionList = descriptionList.filter((item: string) => item.trim() !== '')
      modelInformation.modelAbstract = filterAbstractList
      modelInformation.modelDescription = filterDescriptionList
      modelInformation.modelName = res.data.modelName
      modelInformation.modelImage = res.data.modelImage
      modelInformation.modelPaperLink = res.data.modelPaperLink
      modelInformation.modelCodeLink = res.data.modelCodeLink
      
    } else {
      message(res.msg, { type: 'error' })
    }
  })
}

function navigateToLink(link: string) {
  window.location.href = link
}

onMounted(() => {
  const modelId = route.query.modelId as any
  getModelInfoByModelId(modelId)
  
})

watch(
  () => route.query.modelId,
  (value) => {
    getModelInfoByModelId(value as any)
  }
)
</script>

<template>
  <div
    class="relative px-6 py-24 overflow-hidden bg-white isolate dark:bg-gray-800 sm:py-32 lg:overflow-visible lg:px-0"
  >
    <div class="absolute inset-0 overflow-hidden -z-10">
      <svg
        class="absolute left-[max(50%,25rem)] top-0 h-[64rem] w-[128rem] -translate-x-1/2 stroke-gray-200 [mask-image:radial-gradient(64rem_64rem_at_top,white,transparent)]"
        aria-hidden="true"
      >
        <defs>
          <pattern
            id="e813992c-7d03-4cc4-a2bd-151760b470a0"
            
            height="200"
            x="50%"
            y="-1"
            patternUnits="userSpaceOnUse"
          >
            <path d="M100 200V.5M.5 .5H200" fill="none" />
          </pattern>
        </defs>
        <svg x="50%" y="-1" class="overflow-visible fill-gray-50">
          <path
            d="M-100.5 0h201v201h-201Z M699.5 0h201v201h-201Z M499.5 400h201v201h-201Z M-300.5 600h201v201h-201Z"
            stroke-width="0"
          />
        </svg>
        <rect
          width="100%"
          height="100%"
          stroke-width="0"
          fill="url(#e813992c-7d03-4cc4-a2bd-151760b470a0)"
        />
      </svg>
    </div>
    <div
      class="grid max-w-2xl grid-cols-1 mx-auto gap-x-8 gap-y-16 lg:mx-0 lg:max-w-none lg:grid-cols-2 lg:items-start lg:gap-y-10"
    >
      <div
        class="lg:col-span-2 lg:col-start-1 lg:row-start-1 lg:mx-auto lg:grid lg:w-full lg:max-w-7xl lg:grid-cols-2 lg:gap-x-8 lg:px-8"
      >
        <div class="lg:pr-4">
          <div class="lg:max-w-lg">
            <h1
              class="mt-2 text-3xl font-bold tracking-tight text-center text-gray-900 dark:text-white sm:text-4xl"
            >
              {{ modelInformation.modelName }}
            </h1>
            <p class="text-base font-semibold leading-7 text-center text-indigo-600">摘要</p>
            <p
              v-for="(paragraph, index) in modelInformation.modelAbstract"
              :key="index"
              class="text-xl text-left text-gray-700 dark:text-white indent-8"
            >
              {{ paragraph }}
            </p>
            <div class="flex justify-end pt-10">
              <el-button
                v-if="modelInformation.modelPaperLink != ''"
                type="primary"
                @click="navigateToLink(modelInformation.modelPaperLink)"
                ><template #icon> <IconifyIconOffline :icon="link"> </IconifyIconOffline> </template
                >文章链接</el-button
              >
              <el-button
                v-if="modelInformation.modelCodeLink != ''"
                type="primary"
                @click="navigateToLink(modelInformation.modelCodeLink)"
                ><template #icon>
                  <IconifyIconOffline :icon="camera"> </IconifyIconOffline> </template
                >算法模型</el-button
              >
            </div>
          </div>
        </div>
      </div>
      <div
        class="p-5 -mt-12 -ml-12 lg:sticky lg:top-4 lg:col-start-2 lg:row-span-1 lg:row-start-1 lg:overflow-hidden"
      >
        <img
          class="w-[48rem] max-w-none rounded-xl bg-gray-900 shadow-xl ring-1 ring-gray-400/10 sm:w-[57rem]"
          :src="basicImageUrl + modelInformation.modelImage"
          alt=""
        />
      </div>
      <div
        class="lg:col-span-2 lg:col-start-1 lg:row-start-2 lg:mx-auto lg:grid lg:w-full lg:max-w-7xl lg:gap-x-8 lg:px-8"
      >
        <div class="lg:pr-4">
          <div class="text-base leading-7 text-gray-700 dark:text-white">
            <p
              class="text-xl text-left indent-8"
              v-for="(paragraph, index) in modelInformation.modelDescription"
              :key="index"
            >
              {{ paragraph }}
            </p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped></style>
