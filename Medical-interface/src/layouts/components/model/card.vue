<script setup lang="ts">
import { RouterLink } from 'vue-router'
import { getAllModelsInfoApi } from '@/api/model'
import { watch, onMounted, ref } from 'vue'
import { basicImageUrl } from '@/api/utils'
import type{ ModelInfo } from '@/types/model'

defineOptions({
  name: 'card'
})

const props = defineProps<{
  uploadModelInfo: ModelInfo
  selectedOption: Record<string, string>
}>()

const modelInfoLists = ref<ModelInfo[]>([])
let modedInfoListsStorage: ModelInfo[] = []

function getAllModelsInfo() {
  getAllModelsInfoApi().then((res: any) => {
    if (res.success) {
      modelInfoLists.value = res.data
      modedInfoListsStorage = [...res.data]
    }
  })
}

onMounted(() => {
  getAllModelsInfo()
})

watch(
  () => props.uploadModelInfo,
  () => {
    modelInfoLists.value.push(props.uploadModelInfo)
  }
)

watch(
  () => props.selectedOption,
  () => {
    modelInfoLists.value.length = 0
    let tempModedInfoLists: ModelInfo[] = [...modedInfoListsStorage]
    if (props.selectedOption.type != '全部') {
      tempModedInfoLists = modedInfoListsStorage.filter(
        (item) => item.modelPattern == props.selectedOption.type
      )
    }
    if (props.selectedOption.year != '全部') {
      tempModedInfoLists = tempModedInfoLists.filter(
        (item) => item.modelCreateTime.split('-')[0] == props.selectedOption.year
      )
    }
    if (props.selectedOption.sort == '创建时间') {
      tempModedInfoLists.sort((a, b) => {
        return new Date(a.modelCreateTime).getTime() - new Date(b.modelCreateTime).getTime()
      })
    }
    modelInfoLists.value = tempModedInfoLists
  },
  {
    deep: true
  }
)
</script>

<template>
  <section class="text-gray-600 body-font">
    <div class="container px-5 py-24 mx-auto">
      <div class="grid grid-cols-3 gap-4">
        <div
          v-for="(modelInfo, index) in modelInfoLists"
          :key="index"
          class="w-4/5 p-8 rounded-2xl bg-slate-50 dark:bg-slate-800"
        >
          <div class="h-full overflow-hidden border-2 border-gray-200 rounded-lg border-opacity-60">
            <img
              class="object-cover object-center w-full lg:h-48 md:h-36"
              :src="basicImageUrl + modelInfo.modelImage"
              alt="blog"
            />
            <div class="p-6">
              <h2 class="mb-1 text-xs font-medium tracking-widest text-gray-400 title-font">
                {{ modelInfo.modelPattern }}
              </h2>
              <h1 class="mb-3 text-lg font-medium text-gray-900 title-font dark:text-white">
                {{ modelInfo.modelName }}
              </h1>
              <p class="mb-3 leading-relaxed line-clamp-6 indent-8">
                {{ modelInfo.modelAbstract }}
              </p>
              <div class="flex flex-wrap items-center">
                <router-link
                  class="flex items-center text-blue-400 no-underline md:mb-2 lg:mb-0"
                  :to="{ path: '/model/modelDescription', query: { modelId: modelInfo.modelId } }"
                  >了解详情<svg
                    class="w-4 h-4 ml-2"
                    viewBox="0 0 24 24"
                    stroke="currentColor"
                    stroke-width="2"
                    fill="none"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                  >
                    <path d="M5 12h14"></path>
                    <path d="M12 5l7 7-7 7"></path></svg
                ></router-link>

                <span
                  class="inline-flex items-center py-1 pr-3 ml-auto mr-3 text-sm leading-none text-gray-400 border-r-2 border-gray-200 lg:ml-auto md:ml-0"
                >
                  <svg
                    class="w-4 h-4 mr-1"
                    stroke="currentColor"
                    stroke-width="2"
                    fill="none"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    viewBox="0 0 24 24"
                  >
                    <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path>
                    <circle cx="12" cy="12" r="3"></circle></svg
                  >1.2K
                </span>
                <span class="inline-flex items-center text-sm leading-none text-gray-400">
                  <svg
                    class="w-4 h-4 mr-1"
                    stroke="currentColor"
                    stroke-width="2"
                    fill="none"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    viewBox="0 0 24 24"
                  >
                    <path
                      d="M21 11.5a8.38 8.38 0 01-.9 3.8 8.5 8.5 0 01-7.6 4.7 8.38 8.38 0 01-3.8-.9L3 21l1.9-5.7a8.38 8.38 0 01-.9-3.8 8.5 8.5 0 014.7-7.6 8.38 8.38 0 013.8-.9h.5a8.48 8.48 0 018 8v.5z"
                    ></path></svg
                  >6
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<style lang="scss" scoped></style>
