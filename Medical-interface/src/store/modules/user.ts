import { defineStore } from 'pinia'
import { store } from '@/store'
import type { userType } from './types'
import { router, resetRouter } from '@/router'
import { storageSession } from '@pureadmin/utils'
import { getLogin, refreshTokenApi, register } from '@/api/user'
import type { UserResult, RefreshTokenResult } from '@/types/user'
import { type DataInfo, setToken, removeToken, sessionKey } from '@/utils/auth'

export const useUserStore = defineStore({
  id: 'pure-user',
  state: (): userType => ({
    // 用户名
    username: storageSession().getItem<DataInfo<number>>(sessionKey)?.userName ?? '',
    // 页面级别权限
    roles: storageSession().getItem<DataInfo<number>>(sessionKey)?.roles ?? []
  }),
  actions: {
    /** 存储用户名 */
    SET_USERNAME(username: string | undefined) {
      this.username = username
    },
    /** 存储角色 */
    SET_ROLES(roles: Array<string> | undefined) {
      this.roles = roles
    },
    /** 登入 */
    async loginByUsername(data: any) {
      return new Promise<UserResult>((resolve, reject) => {
        getLogin(data)
          .then((data) => {
            if (data.success) {
              setToken(data.data)
            }
            resolve(data)
          })
          .catch((error) => {
            reject(error)
          })
      })
    },
    async register(data: any) {
      return new Promise<UserResult>((resolve, reject) => {
        register(data)
          .then((data) => {
            // if (data.success) {
            //   setToken(data.data)
            // }
            resolve(data)
          })
          .catch((error) => {
            reject(error)
          })
      })
    },
    /** 前端登出（不调用接口） */
    logOut() {
      this.username = ''
      this.roles = []
      removeToken()
      resetRouter()
      console.log(router.options.routes)
      router.push('/welcome')
      // location.reload()
    },
    /** 刷新`token` */
    async handRefreshToken(data: any) {
      return new Promise<RefreshTokenResult>((resolve, reject) => {
        refreshTokenApi(data)
          .then((data) => {
            if (data) {
              if (data.success) {
                setToken(data.data)
                console.log(data.data)
              }
              resolve(data)
            }
          })
          .catch((error) => {
            reject(error)
          })
      })
    }
  }
})

export function useUserStoreHook() {
  return useUserStore(store)
}
