import { defineStore } from 'pinia'
import { reactive, ref } from 'vue'
import { getSeriesImagePageApi } from '@/api/series'
import type { SeriesInfo } from '@/types/series'
import { message } from '@/utils/message'

export const useSeriesStateStore = defineStore('seriesState', () => {
  var seriesListTableData = reactive<SeriesInfo[]>([])
  const seriesFilterCriteria = reactive<Record<string, any>>({
    seriesName: null,
    seriesFormat: null,
    seriesCount: null,
    patientName: null,
    seriesDate: null,
    seriesTime: null,
    seriesDesc: null,
    seriesDateBegin: null,
    seriesDateEnd: null
  })

  const seriesPagination = reactive({
    total: 0,
    currentPage: 1,
    pageSize: 10,
    totalPage: 0
  })

  const seriesPatientId = ref<number | null>(null)
  const seriesStudyId = ref<number | null>(null)

  function getSeriesListPage() {
    for (var key in seriesFilterCriteria) {
      if (seriesFilterCriteria.hasOwnProperty(key) && seriesFilterCriteria[key] === '') {
        seriesFilterCriteria[key] = null
      }
    }
    var params = {
      current: seriesPagination.currentPage,
      pageSize: seriesPagination.pageSize,
      patientId: seriesPatientId.value,
      studyId: seriesStudyId.value,
      ...seriesFilterCriteria
    }
    console.log(params)
    getSeriesImagePageApi(params).then((res: any) => {
      if (res.success) {
        seriesListTableData.length = 0
        seriesListTableData.push(...(res.data.records as never[]))
        console.log(res.data)
        seriesPagination.total = res.data.total
        seriesPagination.currentPage = res.data.current
        seriesPagination.pageSize = res.data.size
        seriesPagination.totalPage = res.data.totalPage
      } else {
        message('查询为空！请重新设置检索条件', { type: 'error' })
      }
    })
  }

  return {
    seriesListTableData,
    seriesPatientId,
    seriesStudyId,
    seriesFilterCriteria,
    seriesPagination,
    getSeriesListPage
  }
})
