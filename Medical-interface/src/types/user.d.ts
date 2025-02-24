export interface PermissionEntity {
  permissionId: number
  permissionParentId?: string | null // 如果parentId可能为null或不存在，可以使用string | null
  permissionParentName?: string // 如果parentName可能不存在，可以使用可选类型
  permissionName: string
  createTime?: string // 假设时间是以ISO 8601格式的字符串表示
  description: string
}

export interface UserResult {
  success: boolean
  code: number
  msg: string
  data: {
    /** 用户名 */
    username: string
    /** 当前登陆用户的角色 */
    roles: Array<string>
    /** `token` */
    accessToken: string

    /** 用于调用刷新`accessToken`的接口时所需的`token` */
    refreshToken: string
    /** `accessToken`的过期时间（格式'xxxx/xx/xx xx:xx:xx'） */
    expires: number
  }
}

export interface RefreshTokenResult {
  success: boolean
  code: number
  msg: string
  data: {
    /** `token` */
    accessToken: string
    /** 用于调用刷新`accessToken`的接口时所需的`token` */
    refreshToken: string
    /** `accessToken`的过期时间（格式'xxxx/xx/xx xx:xx:xx'） */
    expires: number
  }
}

export interface UserInfo {
  userId: number
  account: string
  phoneNumber: string
  createTime: string // 假设这个字段是字符串格式的日期时间
  birthOfDate: string // 同样，假设这个字段也是字符串格式的日期时间
  gender: string
  place: string // 假设place是一个字符串，可能表示地区编码
  idCard: string
  name: string
}
