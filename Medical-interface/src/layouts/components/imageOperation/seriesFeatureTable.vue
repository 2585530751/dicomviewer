<script setup lang="ts">
import type { ImageFeature } from '@/types/image'
import { imageFeaturePropertyTranslations } from '@/utils/commonVariables'

const props = defineProps<{
  seriesFeature: ImageFeature[]
}>()
</script>

<template>
  <div class="w-full">
    <el-table :data="props.seriesFeature" style="width: 100%" >
      <el-table-column
        prop="original_firstorder_10Percentile"
        :label="imageFeaturePropertyTranslations['original_firstorder_10Percentile']"
      />
      <el-table-column
        prop="original_firstorder_90Percentile"
        :label="imageFeaturePropertyTranslations['original_firstorder_90Percentile']"
      />
      <el-table-column
        prop="original_firstorder_Energy"
        :label="imageFeaturePropertyTranslations['original_firstorder_Energy']"
      />
      <el-table-column
        prop="original_firstorder_Entropy"
        :label="imageFeaturePropertyTranslations['original_firstorder_Entropy']"
      />
      <el-table-column
        prop="original_firstorder_InterquartileRange"
        :label="imageFeaturePropertyTranslations['original_firstorder_InterquartileRange']"
      />
      <el-table-column
        prop="original_firstorder_Kurtosis"
        :label="imageFeaturePropertyTranslations['original_firstorder_Kurtosis']"
      />
      <el-table-column
        prop="original_firstorder_Maximum"
        :label="imageFeaturePropertyTranslations['original_firstorder_Maximum']"
      />
      <el-table-column
        prop="original_firstorder_Mean"
        :label="imageFeaturePropertyTranslations['original_firstorder_Mean']"
      />
      <el-table-column
        prop="original_firstorder_MeanAbsoluteDeviation"
        :label="imageFeaturePropertyTranslations['original_firstorder_MeanAbsoluteDeviation']"
      />
      <el-table-column
        prop="original_firstorder_Median"
        :label="imageFeaturePropertyTranslations['original_firstorder_Median']"
      />
      <el-table-column
        prop="original_firstorder_Minimum"
        :label="imageFeaturePropertyTranslations['original_firstorder_Minimum']"
      />
      <el-table-column
        prop="original_firstorder_Range"
        :label="imageFeaturePropertyTranslations['original_firstorder_Range']"
      />
      <el-table-column
        prop="original_firstorder_RobustMeanAbsoluteDeviation"
        :label="imageFeaturePropertyTranslations['original_firstorder_RobustMeanAbsoluteDeviation']"
      />
      <el-table-column
        prop="original_firstorder_RootMeanSquared"
        :label="imageFeaturePropertyTranslations['original_firstorder_RootMeanSquared']"
      />
      <el-table-column
        prop="original_firstorder_Skewness"
        :label="imageFeaturePropertyTranslations['original_firstorder_Skewness']"
      />
      <el-table-column
        prop="original_firstorder_TotalEnergy"
        :label="imageFeaturePropertyTranslations['original_firstorder_TotalEnergy']"
      />
      <el-table-column
        prop="original_firstorder_Uniformity"
        :label="imageFeaturePropertyTranslations['original_firstorder_Uniformity']"
      />
      <el-table-column
        prop="original_firstorder_Variance"
        :label="imageFeaturePropertyTranslations['original_firstorder_Variance']"
      />
    </el-table>
  </div>
</template>

<style lang="scss" scoped></style>
