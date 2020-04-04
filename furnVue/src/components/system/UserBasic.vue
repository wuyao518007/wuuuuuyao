<template>
  <el-main>
    <el-form :model="user" :rules="rules" :inline="true" ref="addEmpForm" style="margin: 0px;padding: 0px;width:50%">
      <div style="text-align: left">
        <el-row>
          <el-form-item prop="picture" style="margin-right: 10px;width: 130px;">
            <el-upload
              class="avatar-uploader"
              action="aaa"
              list-type="picture-card"
              :on-remove="handleImgRemovephoto"
              :before-upload="beforeuploadphoto"
              :show-file-list="false"
            >
              <img v-if="imgurl" :src=imgurl style="width: 120px;" @error="imgurl=false">
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
          </el-form-item>
        </el-row>
        <el-row>
          <el-form-item label="姓名:" prop="name">
            <el-input prefix-icon="el-icon-edit" v-model="user.name" size="mini"
                      placeholder="请输入姓名"></el-input>
          </el-form-item>
        </el-row>
        <el-row>
          <el-form-item label="手机号:" prop="phone">
            <el-input prefix-icon="el-icon-edit" v-model="user.phone" size="mini"
                      placeholder="请输入手机号"></el-input>
          </el-form-item>
        </el-row>
        <el-row>
          <el-form-item label="登录名:" prop="name">
            <el-input prefix-icon="el-icon-edit" v-model="user.loginName" size="mini"
                      placeholder="请输入姓名"></el-input>
          </el-form-item>
        </el-row>
        <el-row>
          <el-form-item >
            <el-button size="mini" type="primary" @click="addEmp('addEmpForm')">确 定</el-button>
          </el-form-item >
        </el-row>
      </div>
    </el-form>
  </el-main>
</template>
<script>
  export default {
    data() {
      return {
        imgurl:'',//图片url
        param: new FormData(),//表单上传的方法
        user: this.$store.state.user,
        rules: {
          name: [{required: true, message: '必填:姓名', trigger: 'blur'}],
          phone: [
            { required: true, message: '请输入手机号' },
            { min: 11, max: 11, message: '长度为11个字符' }
          ],
          loginName: [{required: true, message: '必填:登录名', trigger: 'blur'}]
        }
      };
    },
    mounted: function () {
      console.log(this.$store.state.user);
      console.log(this.user);
      this.imgurl = 'http://localhost:8089/user/findById?id='+this.user.id;
    },
    methods: {
      addEmp(formName) {
        var _this = this;
        this.$refs[formName].validate((valid) => {
          if (valid) {
            if (this.user.id) {
              //更新
              this.param.append('id', _this.user.id);//名称
              this.param.append('name', _this.user.name);//名称
              this.param.append('loginName', _this.user.loginName);//名称
              this.param.append('phone', _this.user.phone);//名称

              this.uploadFileRequest("/user/update", this.param).then(resp => {
                this.emptyEmpData();
                _this.tableLoading = false;
                if (resp && resp.status == 200) {
                  var data = resp.data;
                  this.$message({type: data.returnValue, message: data.message});
                  _this.emptyEmpData();
                }
              })
            }
          } else {
            return false;
          }
        });
      },
      emptyEmpData() {
        this.param =  new FormData();//表单上传的方法
      },

      /*图片上传*/
      //图片删除事件
      handleImgRemovephoto: function (file, fileList) {
        if(file.status == 'ready') return;//预览图片时返回
        console.log(file, fileList);
        console.log("删除事件");
        this.imgurl = '';
      },
      //阻止upload的自己上传，进行再操作
      beforeuploadphoto(file) {
        const isJPG = file.type === 'image/png' || file.type === 'image/gif' || file.type === 'image/jpg' || file.type === 'image/jpeg';
        const isLt2M = file.size / 1024 < 5000;

        if (!isJPG) {
          this.$message.error('上传图片只能是 png、gif、jpg、jpeg 格式!');
          return false;
        }
        if (!isLt2M) {
          this.$message.error('上传图片大小不能超过 50KB!');
          return false;
        }
        //创建临时的路径来展示图片
        var windowURL = window.URL || window.webkitURL;
        var src = windowURL.createObjectURL(file);
        this.param.append('file', file, file.name);
        //图片加入预览内容
        this.imgurl = src;
        return false;
      },
    }
  };
</script>
