<template>
  <div>
    <!--登录-->
    <el-form :rules="rules" class="login-container" label-position="left" :model="loginForm"
             label-width="0px" v-loading="loading">
      <h3 class="login_title">系统登录</h3>
      <el-form-item prop="loginName">
        <el-input type="text" v-model="loginForm.loginName"
                  auto-complete="off" placeholder="账号"></el-input>
      </el-form-item>
      <el-form-item prop="passWord">
        <el-input type="password" v-model="loginForm.passWord"
                  auto-complete="off" placeholder="密码"></el-input>
      </el-form-item>
      <el-form-item style="width: 100%">
        <el-button type="primary" style="width: 100%" @click="submitClick">登录</el-button>
      </el-form-item>
      <el-form-item style="width: 100%">
       <!-- <a type="primary" style="float:right;width: 20%;cursor: pointer" @click="forgetPass">重置密码</a>-->
        <a type="primary" style="float:right;width: 20%;cursor: pointer" @click="resign">注册</a>
      </el-form-item>
    </el-form>

    <!--注册-->
    <el-dialog
      :title="title"
      style="padding: 0px;"
      :visible.sync="dialogVisible"
      width="27%">
      <el-form :model="resignForm" ref="resignForm"  :rules="rules" :inline="true"  style="margin: 0px;padding: 0px; text-align: left">
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
        <el-form-item label="姓名:" prop="name">
          <el-input prefix-icon="el-icon-edit" v-model="resignForm.name"   placeholder="请输入姓名"></el-input>
        </el-form-item>
        <el-form-item label="手机号:" prop="phone">
          <el-input prefix-icon="el-icon-edit" v-model="resignForm.phone" size="mini"
                    placeholder="请输入手机号"></el-input>
        </el-form-item>
        <el-form-item label="登录名:" prop="loginName">
          <el-input prefix-icon="el-icon-edit" v-model="resignForm.loginName"  placeholder="请输入登录名"></el-input>
        </el-form-item>
        <el-form-item label="性别:" prop="sex">
          <el-radio-group v-model="resignForm.sex">
            <el-radio label="男">男</el-radio>
            <el-radio style="margin-left: 15px" label="女">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="密码:" prop="passWord">
          <el-input prefix-icon="el-icon-edit" v-model="resignForm.passWord" type="password"  placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item label="确认密码:" prop="passWord">
          <el-input prefix-icon="el-icon-edit" v-model="resignForm.passWord2" type="password"  placeholder="请输入密码"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button  @click="cancelEidt">取 消</el-button>
        <el-button type="primary" @click="addresignForm('resignForm')">确 定</el-button>
      </div>
    </el-dialog>

    <!--重置密码-->
    <el-dialog
      :title="title"
      style="padding: 0px;"
      :visible.sync="dialogVisible1"
      width="27%">
      <el-form :model="resignForm" :rules="rules" ref="restPassFrom" :inline="true"  style="margin: 0px;padding: 0px; text-align: left">
        <el-form-item label="登录名:" prop="loginName">
          <el-input prefix-icon="el-icon-edit" v-model="resignForm.loginName"  placeholder="请输入登录名"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button  @click="cancelEidt">取 消</el-button>
        <el-button type="primary" @click="restPass('restPassFrom')">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  export default{
    data(){
      var validatePass = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请输入密码'));
        } else {
          if (this.resignForm.password2 !== '') {
            this.$refs.resignForm.validateField('password2');
          }
          callback();
        }
      };
      var validatePass2 = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请再次输入密码'));
        } else if (value !== this.resignForm.password) {
          callback(new Error('两次输入密码不一致!'));
        } else {
          callback();
        }
      };
      return {
        imgurl:'',//图片url
        param: new FormData(),//表单上传的方法
        rules: {
          name: [{required: true, message: '请输入姓名', trigger: 'blur'}],
          loginName: [{required: true, message: '请输入登录名', trigger: 'blur'}],
          phone: [
            { required: true, message: '请输入手机号' },
            { min: 11, max: 11, message: '长度为11个字符' }
          ],
          password: [
            { required: true,validator: validatePass, trigger: 'blur' }
          ],
          password2: [
            { required: true,validator: validatePass2, trigger: 'blur' }
          ],
        },
        checked: true,
        loginForm: {
          loginName: 'admin',
          passWord: '12345'
        },
        loading: false,title:'',
        restForm:{
          loginName:'', //登录名
          passWord:'',
          passWord2:''
        },
        resignForm:{
          name:'', //姓名
          loginName:'', //登录名
          sex:'',
          phone:'',
          passWord:'', //密码
          passWord2:'',
          photo:'',
        },
        dialogVisible:false,
        dialogVisible1:false,
      }
    },
    methods: {
      submitClick: function () {
        var _this = this;
        this.loading = true;
        this.postRequest('/user/login', {
          loginName: this.loginForm.loginName,
          passWord: this.loginForm.passWord
        }).then(resp=> {
          _this.loading = false;
          if (resp && resp.status == 200) {
            console.log(resp);
            var data = resp.data;
            if(data.returnValue == "failure"){
              this.$message({type: data.returnValue, message: data.message});
              return;
            }
            if(data!=""){
              _this.$store.commit('login', data.obj);
              var path = _this.$route.query.redirect;
              //管理端
              if(data.obj.roleType == '管理员' ){
                path = path == '/' || path == undefined ? '/home' : path;
                _this.$router.replace({path: path});
              }else{
                path = path == '/' || path == undefined ? '/' : path;
                _this.$router.replace({path: path});
              }

            }
          }
        });
      },
      resign: function () {
        this.title = "注册";
        this.dialogVisible = true;
      },
      addresignForm: function (formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            var _this = this;
          /*  if(_this.imgurl == false){
              _this.$alert('请上传头像', '提示', {
                confirmButtonText: '确定',
                callback: action => {
                }
              });
            }*/
            this.param.append('name', _this.resignForm.name);//名称
            this.param.append('loginName', _this.resignForm.loginName);//名称
            this.param.append('passWord', _this.resignForm.passWord);//
            this.param.append('phone', _this.resignForm.phone);//
            this.param.append('sex', _this.resignForm.sex);//

            this.uploadFileRequest('/user/resign', this.param).then(resp=> {
              if (resp && resp.status == 200) {
                var data = resp.data;
                console.log(data);
                this.$message({type: data.returnValue, message: data.message});
                this.cancelEidt();
              }
            });
          }
        })

      },
      cancelEidt() {
        this.dialogVisible = false;
        this.dialogVisible1 = false;

        this.param = new FormData();//表单上传的方法
        this.resignForm = {
          name:'', //姓名
          loginName:'', //登录名
          phone:'',
          sex:'',
          passWord:'', //密码
          photo:'',
        };
      },
      forgetPass:function(){
        console.log(123);
        this.title = "重置密码";
        this.dialogVisible1 = true;
      },
      restPass:function(formName){
        console.log(123);

        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.$confirm('此操作将密码重置为初始密码{12345}, 是否继续?', '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
            }).then(() => {
              this.doPass();
            }).catch(() => {
            });
          }
        })
      },
      doPass(){
        console.log(this.resignForm.loginName);
        this.postRequest('/user/resetPassWord', {
          loginName: this.resignForm.loginName
        }).then(resp=> {
          //_this.loading = false;
          if (resp && resp.status == 200) {
            console.log(resp);
            var data = resp.data;
            this.$message({type: data.returnValue, message: data.message});
            this.cancelEidt();
          }
        });
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
  }
</script>
<style>
  .login-container {
    border-radius: 15px;
    background-clip: padding-box;
    margin: 10% auto;
    width: 350px;
    padding: 35px 35px 15px 35px;
    background: #fff;
    border: 1px solid #eaeaea;
    box-shadow: 0 0 25px #cac6c6;
  }
  .login_title {
    margin: 0px auto 40px auto;
    text-align: center;
    color: #505458;
  }
  .login_remember {
    margin: 0px 0px 35px 0px;
    text-align: left;
  }
</style>
