<script setup lang="ts">
import modelCard from '@/layouts/components/model/card.vue'
import modelSelection from '@/layouts/components/model/selection.vue'
import type { ModelInfo } from '@/types/model'
import { reactive, type Ref, ref } from 'vue'

const modelInfo: Ref<ModelInfo> = ref()
const uploadModelInfo = (modelInfoPara: ModelInfo) => {
  modelInfo.value = modelInfoPara
}

var selectedOption = reactive<Record<string, string>>({})
const selectionOptions = (selectionOptionsPara: Record<string, string>) => {
  // 使用Object.keys()方法获取sourceObject的所有键
  const keys = Object.keys(selectionOptionsPara)
  // 遍历所有键
  keys.forEach((key) => {
    // 将sourceObject的属性赋值给targetObject
    selectedOption[key] = selectionOptionsPara[key]
  })
}
</script>

<template>
  <div>
    <model-selection
      @upload-selection-model-info="uploadModelInfo"
      @emit-selected-option="selectionOptions"
    ></model-selection>
    <model-card :upload-model-info="modelInfo" :selected-option="selectedOption"></model-card>
  </div>
</template>

<style lang="scss" scoped></style>
