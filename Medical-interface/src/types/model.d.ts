import { ImageFeature } from './image'

export interface ModelInfo {
    modelId: number
    modelName: string
    modelPattern: string
    modelCodeLink: string
    modelPaperLink: string
    modelImage: string
    modelAbstract: string
    modelDescription: string
    modelCreateTime: string
  }

export interface ImageModelResult {  
  modelResId: number;  
  seriesId: number;  
  imageId: number;  
  creatorId: number;  
  creatorName: string;  
  createTime: string;  
  resData: string; // 这里假设resData是字符串，如果它实际上是JSON对象，则应该使用适当的类型  
  resultPath: string;  
  resultName: string;  
  resultDes: string;  
  modelId: number;  
  imageFeature: ImageFeature;  
}  

