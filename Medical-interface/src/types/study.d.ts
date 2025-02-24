import { SeriesInfo } from '@/types/series'

export type StudyInfo = {
  studyId: number | null
  studyIdDcm: string | null
  studyInstanceUid: string | null
  studyDate: string | null
  studyTime: string | null
  accessionNumber: string | null
  modalitiesInStudy: string | null
  bodyPartExamined: string | null
  studyDescription: string | null
  patientAge: string | null // 通常年龄可能是数字或范围，但这里假设为字符串
  patientId: number | null
  createTime: String | null
  creatorId: number | null
  seriesList: SeriesInfo[] | null // 注意这里是一个Image类型的数组，或者null
}

export interface StudyEntity {  
  accessionNumber: string | null;  
  bodyPartExamined: string | null;  
  createTime?: string | null; // 假设这是一个ISO 8601格式的日期时间字符串  
  creatorId?: number | null;  
  modalitiesInStudy?: string | null;  
  patientAge: number | null;  
  patientId: number | null;  
  studyDate: string | null; // 假设这是一个日期字符串  
  studyDescription: string | null;  
  studyId: number | null;  
  studyIdDcm?: string | null;  
  studyInstanceUid?: string | null;  
  studyTime: string | null; // 假设这也是一个日期时间字符串  
} 