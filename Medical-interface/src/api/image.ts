import { http } from '@/utils/http'
import { baseUrlApi } from './utils'
import type { ResponseResult } from '@/types/global.d.ts'
import type { ImageModelResult } from '@/types/model.d.ts'
import type { ImageInfo } from '@/types/series'

/**  */
export const uploadImages = (data?: object) => {
  return http.request<any>(
    'post',
    baseUrlApi('series/uploadImages'),
    { data },
    {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    }
  )
}

export const uploadMarkImageFormApi = (data?: object) => {
  return http.request<ResponseResult<ImageInfo>>(
    'post',
    baseUrlApi('markImage/uploadMarkImageForm'),
    { data },
    {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    }
  )
}

export const getImagePageByDoctorId = (data?: object) => {
  return http.request<any>(
    'post',
    baseUrlApi('series/getImagePageByDoctorId'),
    { data },
    {
      headers: {
        'Content-Type': 'application/json'
      }
    }
  )
}

export const deleteImageById = (params?: object) => {
  return http.request<any>('get', baseUrlApi('series/deleteImageById'), { params })
}

export const deleteSingleImageById = (params?: object) => {
  return http.request<any>('get', baseUrlApi('image/deleteSingleImageById'), { params })
}

export const seriesSegmentationOfThyroidNodulesApi = (params?: object) => {
  return http.request<ResponseResult<ImageModelResult[]>>(
    'get',
    baseUrlApi('modelApi/seriesSegment'),
    {
      params
    }
  )
}

export const imageSegmentationOfThyroidNodulesApi = (params?: object) => {
  return http.request<ResponseResult<ImageModelResult>>(
    'get',
    baseUrlApi('modelApi/imageSegment'),
    { params }
  )
}

export const imageClassifyOfThyroidNodulesApi = (params?: object) => {
  return http.request<any>('get', baseUrlApi('modelApi/imageClassify'), { params })
}

export const seriesClassifyOfThyroidNodulesApi = (params?: object) => {
  return http.request<any>('get', baseUrlApi('modelApi/seriesClassify'), { params })
}

export const imageDetectionOfPulmonaryNodulesApi = (params?: object) => {
  return http.request<any>('get', baseUrlApi('modelApi/imageLungDetect'), { params })
}

export const seriesDetectionOfPulmonaryNodulesApi = (params?: object) => {
  return http.request<any>('get', baseUrlApi('modelApi/seriesLungDetect'), { params })
}

export const imageIntestinalPolypsSegmentationApi = (params?: object) => {
  return http.request<ResponseResult<ImageModelResult>>(
    'get',
    baseUrlApi('modelApi/intestinalPolypsImageSegmentation'),
    {
      params
    }
  )
}

export const seriesIntestinalPolypsSegmentationApi = (params?: object) => {
  return http.request<ResponseResult<ImageModelResult[]>>(
    'get',
    baseUrlApi('modelApi/intestinalPolypsSeriesSegmentation'),
    { params }
  )
}

export const addImagesApi = (data?: object) => {
  return http.request<ResponseResult<string>>(
    'post',
    baseUrlApi('image/addImages'),
    { data },
    {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    }
  )
}

export const deleteOneImageApi = (params?: object) => {
  return http.request<ResponseResult<string>>('get', baseUrlApi('image/deleteOneImage'), {
    params
  })
}
