// keyValueStore.ts  
  
// 定义键值对类型  
type KeyValuePair<T> = {  
  key: string;  
  value: T;  
};  
  
// 定义键值对存储类  
class KeyValueStore<T> {  
  private map: Map<string, T> = new Map();  
  
  // 设置键值对  
  set(key: string, value: T): void {  
    this.map.set(key, value);  
  }  
  
  // 获取键值对  
  get(key: string): T | undefined {  
    return this.map.get(key);  
  }  
  
  // 检查键是否存在  
  has(key: string): boolean {  
    return this.map.has(key);  
  }  
  
  // 删除键值对  
  delete(key: string): void {  
    this.map.delete(key);  
  }  
  
  // 清除所有键值对  
  clear(): void {  
    this.map.clear();  
  }  
  
  // 获取所有键值对  
  getAll(): KeyValuePair<T>[] {  
    return Array.from(this.map.entries()).map(([key, value]) => ({ key, value }));  
  }  
  
  // 遍历键值对  
  forEach(callback: (value: T, key: string, map: Map<string, T>) => void): void {  
    this.map.forEach(callback);  
  }  
}  
  
// 导出键值对存储类的实例  
export const imageKeyValueStore = new KeyValueStore<any>();