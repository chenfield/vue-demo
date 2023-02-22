<template>
    <div class="upload-modal-content">
      <a-upload
        :file-list="uploadedFileList"
        :multiple="false"
        :customRequest="uploadFiles"
        :remove="deleteFileItem"
      >
        <a-button> <a-icon type="upload" /> 上传文件 </a-button>
      </a-upload>
    </div>
  </template>
   
  <script>
  import { message } from 'ant-design-vue';

  export default {
    name: "ComUpload",
    data() {
      return {
        //已经上传的文件列表
        uploadedFileList: [],
      };
    },
    methods: {
      /******************上传文件方法******************/
      async uploadFiles(info) {
        //初始化文件信息
        const fileInfo = {
          uid: info.file.uid,
          name: info.file.name,
          status: "uploading",
          response: "",
          url: "",
        };
        //放入上传列表中，以便于显示上传进度
        this.uploadedFileList.push(fileInfo);
        //开始真正上传
        //上传接口
        let uploadApiUrl = "/upload";
        //调用公共上传方法
        const res = await this.uploadFilesToServer(
          uploadApiUrl,
          "file",
          info.file
        );
        //根据服务端返回的结果判断成功与否，设置文件条目的状态
        if (res.status === "0") {
          fileInfo.status = "done";
          fileInfo.response = res.msg;
          fileInfo.url = res.file;
          message.success("上传成功！");
        } else {
          fileInfo.status = "error";
          fileInfo.response = res.msg;
          message.error("上传失败！");
        }
      },
      /******************删除文件记录条目******************/
      deleteFileItem(file) {
        //找到当前文件所在列表的索引
        let index = this.uploadedFileList.indexOf(file);
        //从列表中移除该文件
        this.uploadedFileList.splice(index, 1);
        return true;
      },
      /******************上传文件公共方法******************/
      uploadFilesToServer(uploadApiUrl, fileName, files) {
        let formData = new FormData();
        formData.append(fileName, files);
        //添加请求头
        const headers = {
          "Content-Type": "multipart/form-data",
        };
        //配置头
        const request = this.axios.create({
          headers: headers,
        });
        //开始上传
        return request
          .post(uploadApiUrl, formData)
          .then((response) => {
            var re = {status:"0", msg:"success", file:response.data};
            return Promise.resolve(re);
          })
          .catch((error) => {
            var re = {status:"1", msg:"error", file:error};
            return Promise.reject(re);
          });
      },
    },
  };
  </script>
   
  <style lang="scss" scoped>
  </style>