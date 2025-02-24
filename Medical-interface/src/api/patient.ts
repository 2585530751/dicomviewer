import { http } from '@/utils/http'
import { baseUrlApi } from './utils'
import type { ResponseResult } from '@/types/global.d.ts'

export const getPatientStudyPageApi = (data?: object) => {
  return http.request<any>(
    'post',
    baseUrlApi('study/getPatientStudyPage'),
    { data },
    {
      headers: {
        'Content-Type': 'application/json'
      }
    }
  )
}

export const addOnePatientApi = (data?: object) => {
  return http.request<any>(
    'post',
    baseUrlApi('patient/addOnePatient'),
    { data },
    {
      headers: {
        'Content-Type': 'application/json'
      }
    }
  )
}

export const deleteOnePatientApi = (params?: object) => {
  return http.request<ResponseResult<string>>('get', baseUrlApi('patient/deleteOnePatient'), {
    params
  })
}
