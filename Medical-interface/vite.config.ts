import { fileURLToPath, URL } from 'node:url'

import { defineConfig, loadEnv } from 'vite'
import path from 'path'
import vue from '@vitejs/plugin-vue'
import Icons from 'unplugin-icons/vite'
import svgLoader from 'vite-svg-loader'
import wasm from 'vite-plugin-wasm'

// https://vitejs.dev/config/
export default defineConfig((ConfigEnv) => {
  const config = loadEnv(ConfigEnv.mode, './')
  if (ConfigEnv.mode === 'development') {
    console.log(ConfigEnv.mode)
    return {
      plugins: [vue(), Icons({}), svgLoader()],
      resolve: {
        alias: {
          '@': fileURLToPath(new URL('./src', import.meta.url)),
          '@cornerstonejs/tools': '@cornerstonejs/tools/dist/umd/index.js',
          '@cornerstonejs/dicom-image-loader':
            '@cornerstonejs/dicom-image-loader/dist/dynamic-import/cornerstoneDICOMImageLoader.min.js',
          '@cornerstone-nifti-image-loader':
            '@cornerstonejs/nifti-image-loader/dist/cornerstoneNIFTIImageLoader.min.js',
          '/src/index.worker.64c896c4316fcd506666.worker.js': path.resolve(
            __dirname,
            'node_modules',
            '@cornerstonejs',
            'dicom-image-loader',
            'dist',
            'dynamic-import',
            'index.worker.64c896c4316fcd506666.worker.js'
          )
        }
      },
      server: {
        headers: {
          // 'Cross-Origin-Embedder-Policy': 'require-corp',
          'Cross-Origin-Opener-Policy': 'same-origin'
        },
        proxy: {
          '/api': {
            // 这里填写后端地址
            target: 'http://localhost:8080/',
            changeOrigin: true,
            rewrite: (path) => path.replace(/^\/api/, '')
          }
        }
      }
    }
  } else {
    console.log( ConfigEnv.mode)
    return {
      plugins: [
        vue(),
        Icons({
          /* options */
        }),
        svgLoader(),
        wasm()
      ],
      resolve: {
        alias: {
          '@': fileURLToPath(new URL('./src', import.meta.url)),
          '@cornerstonejs/tools': '@cornerstonejs/tools/dist/umd/index.js',
          '@cornerstonejs/dicom-image-loader':
            '@cornerstonejs/dicom-image-loader/dist/dynamic-import/cornerstoneDICOMImageLoader.min.js',
          '@cornerstone-nifti-image-loader':
            '@cornerstonejs/nifti-image-loader/dist/cornerstoneNIFTIImageLoader.min.js'
        }
      },
      preview: {
        headers: {
          // 'Cross-Origin-Embedder-Policy': 'require-corp',
          'Cross-Origin-Opener-Policy': 'same-origin'
        },
        proxy: {
          '/api': {
            // 这里填写后端地址
            target: 'http://localhost:8080/',
            changeOrigin: true,
            rewrite: (path) => path.replace(/^\/api/, '')
          }
        }
      }
    }
  }
})
