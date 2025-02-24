import { dayjs, type FormInstance, type UploadUserFile } from 'element-plus'
import XLSX from 'xlsx'
import { ElNotification } from 'element-plus'

export function chineseStandardTimeFormat(date: string): string {
  return dayjs(date).format('YYYY-MM-DD HH:mm:ss')
}

export function setAllPropertiesToNull(obj: Record<string, any>) {
  for (var key in obj) {
    if (obj.hasOwnProperty(key)) {
      obj[key] = null
    }
  }
}

export function exportExcel(
  dataJson: Record<string, any>[],
  cols: Record<string, any>[],
  checkedCols: string[],
  fileName: string
) {
  const selectedSeriesInfoList = dataJson.map((seriesInfo: Record<string, any>) => {
    // 使用reduce来构建一个新的对象，只包含checkedCols中的属性
    const selectedSeriesInfo = cols.reduce((obj: Record<string, string>, col) => {
      // 如果col.prop在checkedCols中，则将其添加到新对象中，并使用col.label作为键名
      if (checkedCols.includes(col.prop)) {
        obj[col.label] = seriesInfo[col.prop]
      }
      return obj
    }, {}) // 初始化为一个空对象

    // 返回处理后的对象
    return selectedSeriesInfo
  })
  const ws = XLSX.utils.json_to_sheet(selectedSeriesInfoList)
  const wb = XLSX.utils.book_new()
  XLSX.utils.book_append_sheet(wb, ws, 'Sheet1')
  XLSX.writeFile(wb, fileName)
}

export function exportJsonToExcel(dataJson: Record<string, any>[], fileName: string) {
  const ws = XLSX.utils.json_to_sheet(dataJson)
  const wb = XLSX.utils.book_new()
  XLSX.utils.book_append_sheet(wb, ws, 'Sheet1')
  XLSX.writeFile(wb, fileName)
}

export const resetForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.resetFields()
}

export function checkFilesType(fileList: UploadUserFile[], fileType: string) {
  const isFileType = fileList.some((file) => file.raw!.type === '' && file.name.endsWith(fileType))
  if (!isFileType) {
    ElNotification({
      title: '警告',
      message: '只能上传' + fileType + '格式的文件！',
      type: 'warning'
    })
    return false
  }
  return true
}
