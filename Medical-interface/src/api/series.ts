import { http } from '@/utils/http'
import { baseUrlApi } from './utils'
import type { ResponseResult } from '@/types/global'
import type { SeriesInfo } from '@/types/series'

export const getSeriesImagePageApi = (data?: object) => {
  return http.request<any>(
    'post',
    baseUrlApi('study/getSeriesImagePage'),
    { data },
    {
      headers: {
        'Content-Type': 'application/json'
      }
    }
  )
}

export const getSeriesImageByIdApi = (params?: object) => {
  return http.request<any>('get', baseUrlApi('series/getSeriesImageById'), { params })
}

export const viewReaderApi = (data?: object) => {
  return http.request<ResponseResult<SeriesInfo>>(
    'post',
    baseUrlApi('view/reader'),
    { data },
    {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    }
  )
}

export const viewDoctorApi = (data?: object) => {
  return http.request<ResponseResult<SeriesInfo>>(
    'post',
    baseUrlApi('view/doctor'),
    { data },
    {
      headers: {
        'Content-Type': 'application/json'
      }
    }
  )
}

export const addOneSeriesApi = (data?: object) => {
  return http.request<any>(
    'post',
    baseUrlApi('series/addOneSeries'),
    { data },
    {
      headers: {
        'Content-Type': 'application/json'
      }
    }
  )
}

export const deleteOneSeriesApi = (params?: object) => {
  return http.request<ResponseResult<string>>('get', baseUrlApi('series/deleteOneSeries'), {
    params
  })
}
