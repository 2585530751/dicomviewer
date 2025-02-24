import { defineStore } from 'pinia'
import { reactive } from 'vue'
import { getPatientStudyPageApi } from '@/api/patient'
import type { PatientInfo } from '@/types/patient'
import { message } from '@/utils/message'

export const usePatientStateStore = defineStore('patientState', () => {
  var patientListTableData = reactive<PatientInfo[]>([])
  const patientFilterCriteria = reactive<Record<string, any>>({
    patientName: null,
    patientGender: null,
    dateOfBirth: null,
    phoneNumber: null,
    email: null,
    address: null,
    dateOfBirthBegin: null,
    dateOfBirthEnd: null
  })

  const patientPagination = reactive({
    total: 0,
    currentPage: 1,
    pageSize: 10,
    totalPage: 0
  })

  function getPatientListPage() {
    for (var key in patientFilterCriteria) {
      if (patientFilterCriteria.hasOwnProperty(key)&&patientFilterCriteria[key]==='') {
        patientFilterCriteria[key] = null
      }
    }
    var params = {
      current: patientPagination.currentPage,
      pageSize: patientPagination.pageSize,
      ...patientFilterCriteria
    }
    console.log(params)
    getPatientStudyPageApi(params).then((res: any) => {
      if (res.success) {
        patientListTableData.length = 0
        patientListTableData.push(...(res.data.records as never[]))
        console.log(res.data)
        patientPagination.total = res.data.total
        patientPagination.currentPage = res.data.current
        patientPagination.pageSize = res.data.size
        patientPagination.totalPage = res.data.totalPage
      } else {
        message('查询为空！请重新设置检索条件', { type: 'error' })
      }
    })
  }

  return { patientListTableData, patientFilterCriteria, patientPagination, getPatientListPage }
})
