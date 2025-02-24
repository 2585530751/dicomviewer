import { defineStore } from 'pinia'
import { reactive } from 'vue'
import { getUserPatientStudySeriesPage } from '@/api/study'
import type { StudyInfo } from '@/types/study'
import { message } from '@/utils/message'

export const useMyPatientStudyStateStore = defineStore('myPatientStudyState', () => {
  var myPatientStudyListTableData = reactive<StudyInfo[]>([])
  const myPatientStudyFilterCriteria = reactive<Record<string, any>>({
    myPatientStudyName: null,
    myPatientStudyGender: null,
    dateOfBirth: null,
    phoneNumber: null,
    email: null,
    address: null,
    dateOfBirthBegin: null,
    dateOfBirthEnd: null
  })

  const myPatientStudyPagination = reactive({
    total: 0,
    currentPage: 1,
    pageSize: 10,
    totalPage: 0
  })

  function getMyPatientStudyListPage() {
    for (var key in myPatientStudyFilterCriteria) {
      if (myPatientStudyFilterCriteria.hasOwnProperty(key)&&myPatientStudyFilterCriteria[key]==='') {
        myPatientStudyFilterCriteria[key] = null
      }
    }
    var params = {
      current: myPatientStudyPagination.currentPage,
      pageSize: myPatientStudyPagination.pageSize,
      ...myPatientStudyFilterCriteria
    }
    console.log(params)
    getUserPatientStudySeriesPage(params).then((res: any) => {
      if (res.success) {
        myPatientStudyListTableData.length = 0
        myPatientStudyListTableData.push(...(res.data.records as never[]))
        console.log(res.data)
        myPatientStudyPagination.total = res.data.total
        myPatientStudyPagination.currentPage = res.data.current
        myPatientStudyPagination.pageSize = res.data.size
        myPatientStudyPagination.totalPage = res.data.totalPage
      } else {
        message('查询为空！请重新设置检索条件', { type: 'error' })
      }
    })
  }

  return { myPatientStudyListTableData, myPatientStudyFilterCriteria, myPatientStudyPagination, getMyPatientStudyListPage }
})
