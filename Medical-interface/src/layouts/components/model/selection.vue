<script setup lang="ts">
import type { IconifyIconOffline } from '@/components/ReIcon'
import arrowDown from '@iconify-icons/ep/arrow-down'
import arrowUp from '@iconify-icons/ep/arrow-up'
import { reactive, ref } from 'vue'
import modelUpload from '@/layouts/components/model/modelUpload.vue'
import { basicImageUrl } from '@/api/utils'
import type{ ModelInfo } from '@/types/model'
import 'animate.css'

const props = defineProps<{
  uploadWindowOpen?: boolean
}>()

const emits = defineEmits<{
  uploadSelectionModelInfo: [modelInfo: ModelInfo] // 具名元组语法
  emitSelectedOption: [selectedOption: Record<string, string>] // 具名元组语法
}>()

const selectedOption = reactive({
  type: '全部',
  year: '全部',
  sort: '综合'
})

const centerDialogVisible = ref(false)

const collapsed = ref(true)

const toggleCollapsed = () => {
  collapsed.value = !collapsed.value
}

const selectedList = reactive({
  type: {
    name: '类型',
    value: [
      '全部',
      '图像分割',
      '图像分类',
      '目标检测',
      '图像生成',
      '图像去噪',
      '图像修复',
      '图像分析',
      '图像处理',
      '图像增强'
    ]
  },
  year: {
    name: '年份',
    value: [
      '全部',
      '2024',
      '2023',
      '2022',
      '2021',
      '2020',
      '2019',
      '2018',
      '2017',
      '2016',
      '2015',
      '2014',
      '2013'
    ]
  },
  sort: {
    name: '排序',
    value: ['综合', '创建时间', '评分']
  }
})

function uploadModelList(modelInfo: ModelInfo) {
  emits('uploadSelectionModelInfo', modelInfo)
}
</script>

<template>
  <div class="container w-5/6 p-8 mx-auto bg-zinc-50 rounded-2xl dark:bg-zinc-900">
    <div class="flex justify-between h-10">
      <div class="text-xl">算法模型</div>
      <div class="flex items-center justify-center gap-5">
        <el-button type="default" round @click="centerDialogVisible = true">新增模型</el-button>
        <div class="self-center cursor-pointer hover:text-blue-500" @click="toggleCollapsed">
          <div v-if="collapsed">收起<IconifyIconOffline :icon="arrowUp"></IconifyIconOffline></div>
          <div v-else>展开<IconifyIconOffline :icon="arrowDown"></IconifyIconOffline></div>
        </div>
      </div>
    </div>
    <div class="relative h-2 bottom-3"><el-divider border-style="dashed" /></div>
    <Transition name="zoom">
      <div v-if="collapsed">
        <div class="p-2 space-y-4 overflow-x-auto">
          <div
            v-for="(item, key) in selectedList"
            :key="key"
            class="flex text-gray-600 flex-nowrap dark:text-gray-200"
          >
            <div class="w-20">
              <a class="cursor-pointer whitespace-nowrap">{{ item.name }}:</a>
            </div>
            <div class="flex w-full gap-8">
              <a
                v-for="(value, index) in item.value"
                :key="index"
                class="cursor-pointer whitespace-nowrap"
                :class="{ selected: selectedOption[key] === value }"
                @click="selectedOption[key] = value,emits('emitSelectedOption', selectedOption)"
                >{{ value }}</a
              >
            </div>
          </div>
        </div>
      </div>
    </Transition>
  </div>
  <modelUpload
    @upload-model-info-action="uploadModelList"
    :options="selectedList.type.value"
    :upload-window-open="centerDialogVisible"
    @upload-window-close="centerDialogVisible = false"
  ></modelUpload>
</template>

<style lang="scss" scoped>
.selected {
  background-color: #edf1f2;
  color: #444444;
  box-shadow: 0 4px 6px -1px rgb(0 0 0 / 0.1), 0 2px 4px -2px rgb(0 0 0 / 0.1);
}

a {
  padding-left: 8px;
  padding-right: 8px;
  line-height: 30px;
  border-radius: 10px;
}

a:hover {
  color: #42b4e7;
}

.zoom-enter-active {
  animation: zoom-in 0.3s reverse ease-in;
}

.zoom-leave-active {
  animation: zoom-in 0.3s ease-out;
}

@keyframes zoom-in {
  0% {
    transform: scale(1, 1);
    transform-origin: top;
    opacity: 1;
  }

  50% {
    transform: scale(1, 0.5);
    transform-origin: top;
    opacity: 0.5;
  }

  100% {
    transform: scale(1, 0);
    transform-origin: top;
    opacity: 0;
  }
}
</style>
