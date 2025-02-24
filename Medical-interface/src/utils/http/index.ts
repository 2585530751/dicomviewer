import Axios, {
  type AxiosInstance,
  type AxiosRequestConfig,
  type AxiosResponse,
  type CustomParamsSerializer
} from 'axios'
import type {
  PureHttpError,
  RequestMethods,
  PureHttpResponse,
  PureHttpRequestConfig
} from './types.d'
import { stringify } from 'qs'
import NProgress from '../progress'
import { getToken, formatToken } from '@/utils/auth'
import { useUserStoreHook } from '@/store/modules/user'
import router from '@/router'

// 相关配置请参考：www.axios-js.com/zh-cn/docs/#axios-request-config-1
const defaultConfig: AxiosRequestConfig = {
  // 请求超时时间
  timeout: 1000000,
  headers: {
    Accept: 'application/json, text/plain, */*',
    'Content-Type': 'application/json',
    'X-Requested-With': 'XMLHttpRequest'
  },
  // 数组格式参数序列化（https://github.com/axios/axios/issues/5142）
  paramsSerializer: {
    serialize: stringify as unknown as CustomParamsSerializer
  }
}

class PureHttp {
  constructor() {
    this.httpInterceptorsRequest()
    this.httpInterceptorsResponse()
  }

  /** token过期后，暂存待执行的请求 */
  private static requests = []

  /** 防止重复刷新token */
  private static isRefreshing = false

  /** 初始化配置对象 */
  private static initConfig: PureHttpRequestConfig = {}

  /** 保存当前Axios实例对象 */
  private static axiosInstance: AxiosInstance = Axios.create(defaultConfig)

  /** 重连原始请求 */
  private static retryOriginalRequest(config: PureHttpRequestConfig) {
    return new Promise((resolve) => {
      PureHttp.requests.push(((token: string) => {
        config.headers!['Authorization'] = formatToken(token)
        resolve(config)
      }) as never)
    })
  }

  /** 请求拦截 */
  private httpInterceptorsRequest(): void {
    PureHttp.axiosInstance.interceptors.request.use(
      async (config: PureHttpRequestConfig): Promise<any> => {
        // 开启进度条动画
        NProgress.start()
        // 优先判断post/get等方法是否传入回调，否则执行初始化设置等回调
        if (typeof config.beforeRequestCallback === 'function') {
          config.beforeRequestCallback(config)
          return config
        }
        if (PureHttp.initConfig.beforeRequestCallback) {
          PureHttp.initConfig.beforeRequestCallback(config)
          return config
        }
        /** 请求白名单，放置一些不需要token的接口（通过设置请求白名单，防止token过期后再请求造成的死循环问题） */
        const whiteList = ['/user/refreshToken', '/user/login']
        return whiteList.some((v) => config.url!.indexOf(v) > -1)
          ? config
          : new Promise((resolve) => {
              const data = getToken()
              if (data) {
                const now = new Date().getTime()
                const expired = parseInt(String(data.expires)) - now <= 0
                if (expired) {
                  if (!PureHttp.isRefreshing) {
                    PureHttp.isRefreshing = true
                    // token过期刷新
                    useUserStoreHook()
                      .handRefreshToken({ refreshToken: data.refreshToken })
                      .then((res) => {
                        const token = res.data.accessToken
                        config.headers!['Authorization'] = formatToken(token)
                        ;(PureHttp.requests as ((token: string) => void)[]).forEach((cb) =>
                          cb(token)
                        )
                        PureHttp.requests = []
                      })
                      .finally(() => {
                        PureHttp.isRefreshing = false
                      })
                  }
                  resolve(PureHttp.retryOriginalRequest(config))
                } else {
                  config.headers!['Authorization'] = formatToken(data.accessToken)
                  resolve(config)
                }
              } else {
                resolve(config)
              }
            })
      },
      (error) => {
        return Promise.reject(error)
      }
    )
  }

  /** 响应拦截 */
  private httpInterceptorsResponse(): void {
    const instance = PureHttp.axiosInstance
    instance.interceptors.response.use(
      async (response: PureHttpResponse) => {
        const $config = response.config
        // 关闭进度条动画
        NProgress.done()
        // 优先判断post/get等方法是否传入回调，否则执行初始化设置等回调
        if (typeof $config.beforeResponseCallback === 'function') {
          $config.beforeResponseCallback(response)
          return response.data
        }
        if (PureHttp.initConfig.beforeResponseCallback) {
          PureHttp.initConfig.beforeResponseCallback(response)
          return response.data
        }
        if (response.data && response.data.code === 300) {
          if (getToken()) {
            // return new Promise((resolve) => {
            //   PureHttp.refreshtoken1($config).then((res) => {
            //     resolve(res)
            //   })
            // })
            return await PureHttp.refreshtoken2($config)
          }else{
            useUserStoreHook().logOut()
            router.push('/welcome')
            return response.data
          }
        }
        return response.data // 或者你可以返回 refreshtoken 接口的响应数据
      },
      (error: PureHttpError) => {
        const $error = error
        $error.isCancelRequest = Axios.isCancel($error)
        // 关闭进度条动画
        NProgress.done()
        // 所有的响应异常 区分来源为取消请求/非取消请求
        return Promise.reject($error)
      }
    )
  }
//实现的另一种方案
  private static refreshtoken1($config: PureHttpRequestConfig) {
    return new Promise((resolve, reject) => {
      useUserStoreHook()
        .handRefreshToken({ refreshToken: getToken().refreshToken })
        .then((res) => {
          if (res.code === 200) {
            const token = res.data.accessToken
            $config.headers!['Authorization'] = formatToken(token)
            if ($config.data instanceof FormData && $config.headers!['Content-Type'] == false) {
              $config.headers!['Content-Type'] = 'multipart/form-data'
            }
            resolve(
              new Promise((resolve, reject) => {
                PureHttp.axiosInstance($config)
                  .then((resp: any) => {
                    resolve(resp)
                  })
                  .catch((e) => {
                    reject(e)
                  })
              })
            )
          } else {
            useUserStoreHook().logOut()
            resolve(res)
          }
        })
        .catch(async (error) => {
          reject(error)
        })
    })
  }

  private static async refreshtoken2($config: PureHttpRequestConfig) {
    try {
      const res = await useUserStoreHook().handRefreshToken({
        refreshToken: getToken().refreshToken
      })
      if (res.code === 200) {
        const token = res.data.accessToken
        $config.headers!['Authorization'] = formatToken(token)
        if ($config.data instanceof FormData && !$config.headers!['Content-Type']) {
          $config.headers!['Content-Type'] = 'multipart/form-data'
        }
        const resp = await PureHttp.axiosInstance($config)
        return resp
      } else {
        useUserStoreHook().logOut()
        return res
      }
    } catch (error) {
      throw error
    }
  }

  /** 通用请求工具函数 */
  public request<T>(
    method: RequestMethods,
    url: string,
    param?: AxiosRequestConfig,
    axiosConfig?: PureHttpRequestConfig
  ): Promise<T> {
    const config = {
      method,
      url,
      ...param,
      ...axiosConfig
    } as PureHttpRequestConfig
    // 单独处理自定义请求/响应回调
    return new Promise<T>((resolve, reject) => {
      PureHttp.axiosInstance
        .request(config)
        .then((response: any) => {
          resolve(response)
        })
        .catch((error) => {
          reject(error)
        })
    })
  }

  /** 单独抽离的post工具函数 */
  public post<T, P>(
    url: string,
    params?: AxiosRequestConfig<T>,
    config?: PureHttpRequestConfig
  ): Promise<P> {
    return this.request<P>('post', url, params, config)
  }

  /** 单独抽离的get工具函数 */
  public get<T, P>(
    url: string,
    params?: AxiosRequestConfig<T>,
    config?: PureHttpRequestConfig
  ): Promise<P> {
    return this.request<P>('get', url, params, config)
  }
}

export const http = new PureHttp()
