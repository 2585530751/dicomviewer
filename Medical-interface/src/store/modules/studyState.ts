import { defineStore } from 'pinia'
import { reactive, ref } from 'vue'
import { getStudySeriesPageApi } from '@/api/study'
import type { StudyInfo } from '@/types/study'
import { message } from '@/utils/message'

export const useStudyStateStore = defineStore('studyState', () => {
  var studyListTableData = reactive<StudyInfo[]>([])
  const studyFilterCriteria = reactive<Record<string, any>>({
    studyDate: null,
    studyTime: null,
    patientAge: null,
    accessionNumber: null,
    bodyPartExamined: null,
    studyDescription: null,
    dateOfStudyBegin: null,
    dateOfStudyEnd: null,
    patientAgeBegin: null,
    patientAgeEnd: null
  })

  const studyPagination = reactive({
    total: 0,
    currentPage: 1,
    pageSize: 10,
    totalPage: 0
  })

  const studyPatientId = ref<number | null>(null)

  function getStudyListPage() {
    for (var key in studyFilterCriteria) {
      if (studyFilterCriteria.hasOwnProperty(key) && studyFilterCriteria[key] === '') {
        studyFilterCriteria[key] = null
      }
    }
    var params = {
      current: studyPagination.currentPage,
      pageSize: studyPagination.pageSize,
      patientId: studyPatientId.value,
      ...studyFilterCriteria
    }
    console.log(params)
    getStudySeriesPageApi(params).then((res: any) => {
      if (res.success) {
        studyListTableData.length = 0
        studyListTableData.push(...(res.data.records as never[]))
        console.log(res.data)
        studyPagination.total = res.data.total
        studyPagination.currentPage = res.data.current
        studyPagination.pageSize = res.data.size
        studyPagination.totalPage = res.data.totalPage
      } else {
        message('查询为空！请重新设置检索条件', { type: 'error' })
      }
    })
  }

  return { studyListTableData,studyPatientId, studyFilterCriteria, studyPagination, getStudyListPage }
})
