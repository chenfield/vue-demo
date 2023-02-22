<template>
  <a-layout>
    <a-layout-sider v-model:collapsed="collapsed" :trigger="null" collapsible>
      <div class="logo" />
      <a-menu v-model:selectedKeys="selectedKeys" 
        @click="linkTo" theme="dark" mode="inline">
        <a-menu-item key="/home">
          <user-outlined />
          <span>Home</span>
        </a-menu-item>
        <a-menu-item key="/table">
          <video-camera-outlined />
          <span>Table</span>
        </a-menu-item>
        <a-menu-item key="/upload">
          <upload-outlined />
          <span>Upload</span>
        </a-menu-item>
      </a-menu>
    </a-layout-sider>
    <a-layout>
      <a-layout-header style="background: #fff; padding-left: 15px">
        <menu-unfold-outlined
          v-if="collapsed"
          class="trigger"
          @click="() => (collapsed = !collapsed)"
        />
        <menu-fold-outlined v-else class="trigger" @click="() => (collapsed = !collapsed)" />
      </a-layout-header>
      <a-layout-content
        :style="{ margin: '24px 16px', padding: '24px', background: '#fff', minHeight: '280px' }"
      >
        <router-view></router-view>
      </a-layout-content>
    </a-layout>
  </a-layout>
</template>
<script>
import { UserOutlined, VideoCameraOutlined, UploadOutlined, MenuUnfoldOutlined, MenuFoldOutlined } from '@ant-design/icons-vue';
import { defineComponent, ref } from 'vue';
export default defineComponent({
  components: {
    UserOutlined,
    VideoCameraOutlined,
    UploadOutlined,
    MenuUnfoldOutlined,
    MenuFoldOutlined,
  },
  setup() {    
    return {
      selectedKeys: ref(['/home']),
      collapsed: ref(false),
    };
  },
    methods:{
        linkTo({item, key, keyPath}){   
            //alert(key);         
            this.$router.push(key)
            //alert(key + "," + keyPath);
            console.debug(item + keyPath);
        }
    },
    mounted(){
      var obj = {item: null, key: '/home', keyPath: null}; 
      this.linkTo(obj);
  },
});
</script>
<style>
#components-layout-demo-custom-trigger .trigger {
  font-size: 18px;
  line-height: 64px;
  padding: 0 24px;
  cursor: pointer;
  transition: color 0.3s;
}

#components-layout-demo-custom-trigger .trigger:hover {
  color: #1890ff;
}

#components-layout-demo-custom-trigger .logo {
  height: 32px;
  background: rgba(255, 255, 255, 0.3);
  margin: 16px;
}

.site-layout .site-layout-background {
  background: #fff;
}
</style>