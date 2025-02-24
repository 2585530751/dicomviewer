import type { SeriesInfo, ImageInfo } from '@/types/series'

export interface SeriesInfoWindows {
  seriesInfo: SeriesInfo
  imageInfo: ImageInfo
}

export interface ImageFeature {
  featureId?: number | null
  modelResId?: number | null
  file_name?: string | null
  original_firstorder_10Percentile: string | null
  original_firstorder_90Percentile: string | null
  original_firstorder_Energy: string | null
  original_firstorder_Entropy: number | null
  original_firstorder_InterquartileRange: string | null
  original_firstorder_Kurtosis: number | null
  original_firstorder_Maximum: number | null
  original_firstorder_MeanAbsoluteDeviation: number | null
  original_firstorder_Mean: number | null
  original_firstorder_Median: number | null
  original_firstorder_Minimum: number | null
  original_firstorder_Range: number | null
  original_firstorder_RobustMeanAbsoluteDeviation: number | null
  original_firstorder_RootMeanSquared: number | null
  original_firstorder_Skewness: number | null
  original_firstorder_TotalEnergy: string | null
  original_firstorder_Uniformity: number | null
  original_firstorder_Variance: number | null
}

