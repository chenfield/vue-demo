const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    proxy: {
      '/api': {
        target: 'http://localhost:8888/', // 请求接口地址
        changeOrigin: true,
        ws: true,
        pathRewrite: {
          '^/api': ''
        }
      },
    }
  }
})
