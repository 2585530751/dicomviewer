import { basicImageUrl } from '@/api/utils'
import type { SeriesInfoWindows } from '@/types/image'
import { storageSession } from '@pureadmin/utils'
import type { SeriesInfo } from '@/types/series'

export const seriesListsSession = 'seriesLists'
export const seriesListWindowsSession = 'seriesListWindows'
export const seriesModelsListsSession = 'seriesModelsLists'

export function generateImageUrl(imagePath: String) {
  if (imagePath.endsWith('.png') || imagePath.endsWith('.jpg') || imagePath.endsWith('.jpeg')) {
    return 'web:' + basicImageUrl + imagePath
  } else if (imagePath.endsWith('.dcm')) {
    return 'wadouri:' + basicImageUrl + imagePath
  }
}

export function formatDate(date: Date) {
  const year = date.getFullYear()
  const month = date.getMonth() + 1
  const day = date.getDate()
  //   const hours = date.getHours()
  //   const minutes = date.getMinutes()
  //   const seconds = date.getSeconds()

  return `${year}-${month < 10 ? '0' + month : month}-${day < 10 ? '0' + day : day} `
  //   ${hours}:${minutes}:${seconds}
}

export function pushSeriesToSession(seriesInfo: SeriesInfo) {
  const session = storageSession()
  if (session.getItem(seriesListsSession)) {
    const list: SeriesInfo[] = session.getItem(seriesListsSession)

    // 检查数组中是否存在具有相同imageId的元素
    let existingElement = list.find((element) => element.seriesId === seriesInfo.seriesId)
    if (existingElement) {
      // 如果存在相同imageId的元素，则删除它并添加新元素
      const index = list.indexOf(existingElement)
      list.splice(index, 1) // 删除找到的元素
      list.push(seriesInfo) // 添加新元素
      session.setItem(seriesListsSession, list)
    } else {
      // 如果不存在相同imageId的元素，则直接添加新元素
      list.push(seriesInfo)
      session.setItem(seriesListsSession, list)
    }
  } else {
    session.setItem(seriesListsSession, [seriesInfo])
  }
}

export function pushseriesModelsListsSession(seriesInfo: SeriesInfo) {
  const session = storageSession()
  if (session.getItem(seriesModelsListsSession)) {
    const list: SeriesInfo[] = session.getItem(seriesModelsListsSession)
    for (var i = 0; i < list.length; i++) {
      if (
        list[i].seriesId === seriesInfo.seriesId &&
        list[i].imageList[0].imageModelData!.modelId ===
          seriesInfo.imageList[0].imageModelData!.modelId
      ) {
        if (list[i].imageList.length < seriesInfo.imageList.length) {
          for (var j = 0; j < seriesInfo.imageList.length; j++) {
            if (list[i].imageList[0].imageId == seriesInfo.imageList[j].imageId) {
              list[i].imageList[0] = seriesInfo.imageList[j]
            }
          }
        } else if (list[i].imageList.length > seriesInfo.imageList.length) {
          for (var j = 0; j < list[i].imageList.length; j++) {
            if (list[i].imageList[j].imageId == seriesInfo.imageList[0].imageId) {
              list[i].imageList[j] = seriesInfo.imageList[0]
            }
          }
        } else if (list[i].imageList.length == seriesInfo.imageList.length) {
          if (list[i].imageList[0].imageId == seriesInfo.imageList[0].imageId) {
            list.splice(i, 1)
          }
        }
      }
    }
    list.push(seriesInfo)
    session.setItem(seriesModelsListsSession, list)
  } else {
    session.setItem(seriesModelsListsSession, [seriesInfo])
  }
}

export function changeSeriesListWindowsToSession(
  seriesInfoWindow: SeriesInfoWindows,
  index: number
) {
  const session = storageSession()
  if (session.getItem(seriesListWindowsSession)) {
    const list: (SeriesInfoWindows | 0)[] = session.getItem(seriesListWindowsSession)
    if (JSON.stringify(list[index]) != JSON.stringify(seriesInfoWindow)) {
      console.log(seriesInfoWindow)
      list[index] = seriesInfoWindow
      session.setItem(seriesListWindowsSession, list)
    }
  } else {
    const list: (SeriesInfoWindows | 0)[] = [0, 0, 0, 0, 0, 0, 0, 0, 0]
    list[index] = seriesInfoWindow
    session.setItem(seriesListWindowsSession, list)
  }
}
