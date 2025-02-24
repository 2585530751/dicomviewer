export const baseUrlApi = (url: string) => `/api/${url}`;

export const basicImageUrl=import.meta.env.DEV?"http://localhost:5173/api/resources/":"http://43.138.144.174:8080/resources/"
export const serverUrl=import.meta.env.DEV?"http://localhost:5173/":"http://43.138.144.174:8080/"
