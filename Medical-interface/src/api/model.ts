import { http } from '@/utils/http'
import { baseUrlApi } from './utils'

export const getModelInfoByModelIdApi = (params?: object) => {
  return http.request<any>('get', baseUrlApi('modelInfo/getModelInfoByModelId'), { params })
}

export const getAllModelsInfoApi = () => {
  return http.request<any>('get', baseUrlApi('modelInfo/getAllModelInfo'))
}

export const uploadModelInfoApi = (data?: object) => {
  return http.request<any>(
    'post',
    baseUrlApi('modelInfo/uploadModelInfo'),
    { data },
    {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    }
  )
}
