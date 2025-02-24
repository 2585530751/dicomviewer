import StudyInfo from '@/types/study'

export interface PatientInfo {
  patientId: number
  patientName: string
  patientIdCardNumber: string
  patientGender: string
  patientHeight: string
  patientWeight: string
  dateOfBirth: string
  phoneNumber: string
  telephoneNumber: string
  faxNumber: string
  email: string
  address: string
  userId: string
  patientIdDcm: string
  createTime: String | null
  creatorId: number | null
  studyList: StudyInfo[] | null
}

export interface PatientEntity {  
  address: string|null;  
  createTime?: string|null; // 通常这会是 Date 类型，但根据你的数据它是字符串  
  creatorId?: number|null;  
  dateOfBirth: string|null; // 同上，通常会是 Date 类型  
  email: string|null;  
  faxNumber?: string|null;  
  patientGender: string|null; // 可以考虑使用枚举或其他方式来限制可能的值  
  patientHeight: number|null;  
  patientId?: number|null;  
  patientIdCardNumber: string|null;  
  patientIdDcm?: string|null;  
  patientName: string|null;  
  patientWeight: number|null;  
  phoneNumber?: string|null; // 如果有区别，phoneNumber 和 telephoneNumber 可以合并或区分使用场景  
  telephoneNumber: string|null;  
  userId?: number|null;  
}