import { http } from '@/utils/http'
import { baseUrlApi } from './utils'
import type { ResponseResult } from '@/types/global'

export const getStudySeriesPageApi = (data?: object) => {
  return http.request<any>(
    'post',
    baseUrlApi('study/getStudySeriesPage'),
    { data },
    {
      headers: {
        'Content-Type': 'application/json'
      }
    }
  )
}

export const getUserPatientStudySeriesPage = (data?: object) => {
  return http.request<any>(
    'post',
    baseUrlApi('study/getUserPatientStudySeriesPage'),
    { data },
    {
      headers: {
        'Content-Type': 'application/json'
      }
    }
  )
}


export const uploadSeriesApi = (data?: object) => {
  return http.request<any>(
    'post',
    baseUrlApi('study/uploadSeries'),
    { data },
    {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    }
  )
}

export const addOneStudyApi = (data?: object) => {
  return http.request<any>(
    'post',
    baseUrlApi('study/addOneStudy'),
    { data },
    {
      headers: {
        'Content-Type': 'application/json'
      }
    }
  )
}

export const deleteOneStudyApi = (params?: object) => {
  return http.request<ResponseResult<string>>('get', baseUrlApi('study/deleteOneStudy'), {
    params
  })
}
