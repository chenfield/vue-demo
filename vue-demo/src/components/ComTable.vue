<template>
  <a-space>
    <a-button type="primary" @click="showModal()">Add</a-button>
    <a-button @click="exportData()">导出当前页</a-button>
  </a-space>
  <a-divider></a-divider>
  <a-table
    :columns="columns"
    :data-source="tableData"
    :pagination="pagination"
    @change="handleTableChange"
  >
    <template #bodyCell="{ column, record }">
      <template v-if="column.dataIndex === 'action'">
        <a @click="handleEdit(record)">Edit</a>
        <a-divider type="vertical" />
        <a-popconfirm
          title="Sure to delete?"
          @confirm="handleDelete(record.id)"
        >
          <a>Delete</a>
        </a-popconfirm>
      </template>
    </template>
  </a-table>

  <a-modal v-model:visible="visible" title="增加" @ok="handleOk" :footer="null">
    <a-form
      :model="user"
      v-bind="layout"
      name="nest-messages"
      :validate-messages="validateMessages"
      @finish="onFinish"
    >
      <a-form-item name="name" label="Name" :rules="[{ required: true }]">
        <a-input v-model:value="user.name" />
      </a-form-item>
      <a-form-item
        name="age"
        label="Age"
        :rules="[{ type: 'number', min: 0, max: 99 }]"
      >
        <a-input-number v-model:value="user.age" />
      </a-form-item>
      <a-form-item name="sex" label="Sex">
        <a-select
          ref="select"
          v-model:value="user.sex"
          :options="options1"
          @change="handleChange"
        ></a-select>
      </a-form-item>
      <a-form-item>
        <a-date-picker v-model:value="value1" name="date" label="Date"/>
      </a-form-item>
      <a-form-item name="address" label="Address">
        <a-textarea v-model:value="user.address" />
      </a-form-item>
      <a-form-item name="id" label="id" hidden="true">
        <a-textarea v-model:value="user.id" />
      </a-form-item>
      <a-form-item :wrapper-col="{ ...layout.wrapperCol, offset: 8 }">
        <a-space>
          <a-button @click="handleOk">Cancel</a-button>
          <a-button type="primary" html-type="submit">Submit</a-button>
        </a-space>
      </a-form-item>
    </a-form>
  </a-modal>
</template>
  <style>
</style>
  <script>
import { message } from "ant-design-vue";
import { reactive, ref } from "vue";
import * as XLSX from "xlsx";
import dayjs, { Dayjs } from 'dayjs';

const columns = [
  {
    title: "姓名",
    dataIndex: "name",
  },
  {
    title: "年龄",
    dataIndex: "age",
  },
  {
    title: "性别",
    dataIndex: "sex",
  },
  {
    title: "地址",
    dataIndex: "address",
  },
  {
    title: "操作",
    dataIndex: "action",
  },
];

const layout = {
  labelCol: {
    span: 6,
  },
  wrapperCol: {
    span: 16,
  },
};

const validateMessages = {
  required: "${label} is required!",
  types: {
    number: "${label} is not a valid number!",
  },
  number: {
    range: "${label} must be between ${min} and ${max}",
  },
};

const user = reactive({
  id: "",
  name: "",
  age: undefined,
  sex: "",
  address: "",
});

const options1 = ref([
  {
    value: "男",
    label: "男",
  },
  {
    value: "女",
    label: "女",
  },
]);

export default {
  data() {
    return {
      value1: ref<Dayjs>(dayjs('2015-06-06')),
      user,
      options1,
      layout,
      validateMessages,
      columns,
      visible: false,
      tableData: [],
      search: "",
      dialogVisible: false,
      dialogUpdate: false,
      pageSize: 5,
      currentPage: ref(1),
      total: 0,
      pagination: { current: 1, pageSize: 2, total: 0, showQuickJumper: true, showLessItems:true, showTotal: total => `Total ${total} items` },
      disablePage: false,
    };
  },
  methods: {
    exportData(){
   
      let head = {
          name: '姓名',
          age: '年龄',
          sex: '性别',
          address: '地址',
        }

        var objs = this.tableData;
        const list = objs.map(item => {
        const obj = {}
        for (const k in item) {
          if (head[k]) {
            obj[head[k]] = item[k]
          } 
        }
        return obj
      })

      const data = XLSX.utils.json_to_sheet(list)//此处tableData.value为表格的数据
      const wb = XLSX.utils.book_new()
      XLSX.utils.book_append_sheet(wb, data, 'data')//test-data为自定义的sheet表名
      XLSX.writeFile(wb,'test.xlsx')//test.xlsx为自定义的文件名
    },
    handleChange(val) {
      this.user.sex = val;
    },
    handleEdit(row) {
      this.visible = true;
      this.user = row; //这句是关键！！！
    },
    onFinish(values) {
      console.log(values);
      if (this.user.id != "") {
        this.updateUser();
      } else {
        this.addUser();
      }
    },
    showModal() {
      this.user = {
        id: "",
        name: "",
        age: undefined,
        sex: "",
        address: "",
      };

      this.visible = true;
    },
    handleOk() {
      this.user = {
        id: "",
        name: "",
        age: undefined,
        sex: "",
        address: "",
      };

      this.visible = false;
    },
    handleTableChange(page) {
      this.pagination.current = page.current;
      //this.pagination.pageSize = pageSize;
      this.handleCurrentChange();
    },
    handleDelete(id) {
      //alert(id);
      let postData = this.qs.stringify({
        id: id,
      });
      this.axios({
        method: "post",
        url: "/delete",
        data: postData,
      })
        .then((response) => {
          this.getPages();
          this.handleCurrentChange();
          message.success("This is a normal message" + response.data);
          console.log(response);
        })
        .catch((error) => {
          console.log(error);
        });
    },
    handleCurrentChange() {
      console.log(`当前页: ${this.currentPage}`);
      let postData = this.qs.stringify({
        page: this.pagination.current,
        size: this.pagination.pageSize
      });
      this.axios({
        method: "post",
        url: "/page",
        data: postData,
      })
        .then((response) => {
          this.tableData = response.data;
        })
        .catch((error) => {
          console.log(error);
        });
    },
    addUser() {
      let postData = this.qs.stringify({
        name: this.user.name,
        age: this.user.age,
        sex: this.user.sex,
        address: this.user.address,
      });
      this.axios({
        method: "post",
        url: "/add",
        data: postData,
      })
        .then((response) => {
          this.pagination.current = 1;
          this.getPages();
          this.handleCurrentChange();
          this.handleOk();
          //this.dialogVisible = false
          console.log(response);
        })
        .catch((error) => {
          console.log(error);
        });
    },
    updateUser() {
      let postData = this.qs.stringify({
        id: this.user.id,
        name: this.user.name,
        age: this.user.age,
        sex: this.user.sex,
        address: this.user.address,
      });
      this.axios({
        method: "post",
        url: "/update",
        data: postData,
      })
        .then((response) => {
          this.handleCurrentChange();
          this.handleOk();
          message.success("This is a normal message");
          console.log(response);
        })
        .catch((error) => {
          console.log(error);
        });
    },
    onSearch() {
      let postData = this.qs.stringify({
        name: this.search,
      });
      this.axios({
        method: "post",
        url: "/findByName",
        data: postData,
      })
        .then((response) => {
          this.tableData = response.data;
          this.disablePage = true;
        })
        .catch((error) => {
          console.log(error);
        });
    },
    getPages() {
      this.axios
        .post("/rows")
        .then((response) => {
          this.total = response.data;
          this.pagination.total = this.total;
          console.log(this.total);
        })
        .catch((error) => {
          console.log(error);
        });
    },
    refreshData() {
      location.reload();
    },
  },
  created() {
    this.getPages();
    this.handleCurrentChange();
  },
};
</script>