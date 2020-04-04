<template>
  <div>
    <el-container>
      <el-header style="padding: 0px;display:flex;justify-content:space-between;align-items: center">
        <div style="display: inline">
          <el-input
            placeholder="通过登录名搜索,记得回车哦..."
            clearable
            style="width: 300px;margin: 0px;padding: 0px;"
            @keyup.enter.native="searchEmp"
            prefix-icon="el-icon-search"
            v-model="loginName">
          </el-input>
          <el-button type="primary" size="mini" style="margin-left: 5px" icon="el-icon-search" @click="searchEmp">搜索
          </el-button>
        </div>
        <div style="margin-left: 5px;margin-right: 20px;display: inline">
         <!-- <el-upload
            :show-file-list="false"
            accept="application/vnd.ms-excel"
            action="/user/import"
            :on-success="fileUploadSuccess"
            :on-error="fileUploadError" :disabled="fileUploadBtnText=='正在导入'"
            :before-upload="beforeFileUpload" style="display: inline">
            <el-button size="mini" type="success" :loading="fileUploadBtnText=='正在导入'">
              <i class="fa fa-lg fa-level-up" style="margin-right: 5px"></i>{{fileUploadBtnText}}
            </el-button>
          </el-upload>-->
          <el-button type="success" size="mini" @click="exportEmps">
            <i class="fa fa-lg fa-level-down" style="margin-right: 5px"></i>导出数据
          </el-button>
          <el-button type="primary" size="mini" icon="el-icon-plus"
                     @click="showAddEmpView">
            添加用户
          </el-button>
        </div>
      </el-header>
      <el-main style="padding-left: 0px;padding-top: 0px">
        <div>
          <el-table
            :data="users"
            v-loading="tableLoading"
            border
            stripe
            @selection-change="handleSelectionChange"
            size="mini"
            style="width: 100%">
            <el-table-column
              type="selection"
              align="left"
              width="30">
            </el-table-column>
            <el-table-column
              align="left"
              fixed
              label="头像">
              <template slot-scope="scope">
                <img :src="'http://localhost:8089/user/findById?id='+scope.row.id"  style="width: 70px;height: 70px;margin-right: 5px;margin-left: 5px;border-radius: 70px"/>
              </template>
            </el-table-column>
            <el-table-column
              prop="name"
              align="left"
              label="姓名">
            </el-table-column>
            <el-table-column
              prop="loginName"
              align="left"
              label="登录名">
            </el-table-column>
            <el-table-column
              prop="sex"
              align="left"
              label="性别">
            </el-table-column>
            <el-table-column
              prop="phone"
              align="left"
              label="手机号">
            </el-table-column>
            <el-table-column
              prop="roleType"
              label="角色类型">
            </el-table-column>
            <el-table-column
              prop="keepAlive"
              label="是否封禁">
            </el-table-column>
            <el-table-column
              prop="updateTime"
              align="left"
              width="195"
              label="日期">
            </el-table-column>
            <el-table-column
              fixed="right"
              label="操作"
              width="105">
              <template slot-scope="scope">
                <el-button @click="showEditEmpView(scope.row)" style="padding: 3px 4px 3px 4px;margin: 2px"
                           size="mini">编辑
                </el-button>
                <el-button type="danger" style="padding: 3px 4px 3px 4px;margin: 2px" size="mini"
                           @click="deleteEmp(scope.row)">删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <div style="display: flex;justify-content: space-between;margin: 2px">
            <el-button type="danger" size="mini" v-if="users.length>0" :disabled="multipleSelection.length==0"
                       @click="deleteManyEmps">批量删除
            </el-button>
            <el-pagination
              background
              :page-size="10"
              :current-page="currentPage"
              @current-change="currentChange"
              layout="prev, pager, next"
              :total="totalCount">
            </el-pagination>
          </div>
        </div>
      </el-main>
    </el-container>
    <el-form :model="user" :rules="rules" :inline="true" ref="addEmpForm" style="margin: 0px;padding: 0px;width:1%">
      <div style="text-align: left">
        <el-dialog
          :title="dialogTitle"
          style="padding: 0px;"
          :close-on-click-modal="false"
          :visible.sync="dialogVisible"
          width="27%">

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
            <el-input prefix-icon="el-icon-edit" v-model="user.name" size="mini"
                      placeholder="请输入姓名"></el-input>
          </el-form-item>
          <el-form-item label="登录名:" prop="name">
            <el-input prefix-icon="el-icon-edit" v-model="user.loginName" size="mini"
                      placeholder="请输入姓名"></el-input>
          </el-form-item>
          <el-form-item label="性别:" prop="sex">
            <el-radio-group v-model="user.sex">
              <el-radio label="男">男</el-radio>
              <el-radio style="margin-left: 15px" label="女">女</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="手机号:" prop="phone">
            <el-input prefix-icon="el-icon-edit" v-model="user.phone" size="mini"
                      placeholder="请输入手机号"></el-input>
          </el-form-item>
          <el-form-item label="角色类型:"  prop="name" style="margin-left: -5px;">
            <el-select v-model="user.roleType" placeholder="请选择类型">
              <el-option
                v-for="item in roleType"
                :key="item.id"
                :label="item.name"
                :value="item.name">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="封禁:" prop="keepAlive">
            <el-radio-group v-model="user.keepAlive">
              <el-radio label="是">是</el-radio>
              <el-radio style="margin-left: 15px" label="否">否</el-radio>
            </el-radio-group>
          </el-form-item>
          <span slot="footer" class="dialog-footer">
              <el-button size="mini" @click="cancelEidt">取 消</el-button>
              <el-button size="mini" type="primary" @click="addEmp('addEmpForm')">确 定</el-button>
            </span>
        </el-dialog>
      </div>
    </el-form>
  </div>
</template>
<script>
  export default {
    data() {
      return {
        imgurl:'',//图片url
        param: new FormData(),//表单上传的方法
        //搜索条件
        criteria: '',
        //默认每页数据量
        pagesize: 10,
        users: [],
        loginName: '',
        fileUploadBtnText: '导入数据',
        beginDateScope: '',
        dialogTitle: '',
        multipleSelection: [],
        totalCount: -1,//默认数据总数
        currentPage: 1, //当前页码
        dialogVisible: false,
        tableLoading: false,
        roleType:[],
        user: {
          name: '',//姓名
          loginName: '',//登录名
          roleType:'',
          keepAlive: '否',//封禁状态
          sex: '',//性别
          phone:'' //手机号
        },
        rules: {
          name: [{required: true, message: '必填:姓名', trigger: 'blur'}],
          loginName: [{required: true, message: '必填:登录名', trigger: 'blur'}],
          phone: [
            { required: true, message: '请输入手机号' },
            { min: 11, max: 11, message: '长度为11个字符' }
          ],
          roleType: [{required: true, message: '必填:角色类型', trigger: 'blur'}],
          sex: [{required: true, message: '请选择', trigger: 'blur'}],
          keepAlive: [{required: true, message: '请选择', trigger: 'blur'}]
        }
      };
    },
    mounted: function () {
      this.loadData(this.currentPage,this.pagesize);
      this.loadRoleType();
    },
    methods: {
      loadRoleType() {
        var _this = this;
        this.postRequest("/role/findAllList", {}).then(resp => {
          if (resp && resp.status == 200) {
            var data = resp.data;
            _this.roleType = data;
          }
        })
      },
      loadData(pageNum, pageAmount) {
        var _this = this;
        this.tableLoading = true;
        var param  ={loginName:_this.loginName,pageNum:pageNum, pageAmount:pageAmount}
        this.postRequest("/user/findList", param).then(resp => {
          this.tableLoading = false;
          if (resp && resp.status == 200) {
            var data = resp.data;
            _this.users = data.pagedata;
            _this.totalCount = data.total;
          }
        })
      },
      fileUploadSuccess(response, file, fileList) {
        console.log(response);
        if (response) {
          this.$message({type: response.returnValue, message: response.message});
        }
        this.loadData(this.currentPage,this.pagesize);
        this.fileUploadBtnText = '导入数据';
      },
      fileUploadError(err, file, fileList) {
        this.$message({type: 'error', message: "导入失败!"});
        this.fileUploadBtnText = '导入数据';
      },
      beforeFileUpload(file) {
        this.fileUploadBtnText = '正在导入';
      },
      exportEmps() {
        window.open("/user/exportEmp", "_parent");
      },
      handleSelectionChange(val) {
        this.multipleSelection = val;
      },
      deleteManyEmps() {
        this.$confirm('此操作将删除[' + this.multipleSelection.length + ']条数据, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          var ids = '';
          for (var i = 0; i < this.multipleSelection.length; i++) {
            ids += this.multipleSelection[i].id + ",";
          }
          this.doDelete(ids);
        }).catch(() => {
        });
      },
      deleteEmp(row) {
        this.$confirm('此操作将永久删除[' + row.name + '], 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.doDelete(row.id);
        }).catch(() => {
        });
      },
      doDelete(ids) {
        this.tableLoading = true;
        var _this = this;
        this.deleteRequest("/user/delete/" + ids).then(resp => {
          _this.tableLoading = false;
          if (resp && resp.status == 200) {
            var data = resp.data;
            _this.loadData(this.currentPage,this.pagesize);
          }
        })
      },
      searchEmp() {
        this.loadData(this.currentPage,this.pagesize);
      },
      currentChange(currentChange) {
        this.currentPage = currentChange;
        this.loadData(this.currentPage, this.pagesize);
      },
      addEmp(formName) {
        var _this = this;
        this.$refs[formName].validate((valid) => {
          if (valid) {
            console.log( this.param);
            console.log( this.param.get('file'));

            if (this.user.id) {
              //更新
              this.tableLoading = true;
              this.param.append('id', _this.user.id);//名称
              this.param.append('name', _this.user.name);//
              this.param.append('loginName', _this.user.loginName);//
              this.param.append('roleType', _this.user.roleType);//
              this.param.append('keepAlive', _this.user.keepAlive);//
              this.param.append('sex', _this.user.sex);//
              this.param.append('phone', _this.user.phone);//
              this.uploadFileRequest("/user/update", this.param).then(resp => {
                _this.tableLoading = false;
                if (resp && resp.status == 200) {
                  var data = resp.data;
                  _this.dialogVisible = false;
                  _this.emptyEmpData();
                  _this.loadData(this.currentPage,this.pagesize);
                }
              })
            } else {
              //添加
              this.tableLoading = true;
              if(_this.imgurl == false){
                _this.$alert('请上传头像', '提示', {
                  confirmButtonText: '确定',
                  callback: action => {
                  }
                });
              }
              this.param.append('name', _this.user.name);//名称
              this.param.append('loginName', _this.user.loginName);//
              this.param.append('roleType', _this.user.roleType);//
              this.param.append('keepAlive', _this.user.keepAlive);//
              this.param.append('sex', _this.user.sex);//
              this.param.append('phone', _this.user.phone);//
              this.uploadFileRequest("/user/insert", this.param).then(resp => {
                _this.tableLoading = false;
                if (resp && resp.status == 200) {
                  var data = resp.data;

                  _this.dialogVisible = false;
                  _this.emptyEmpData();
                  _this.loadData(this.currentPage,this.pagesize);
                }
              })
            }
          } else {
            return false;
          }
        });
      },
      cancelEidt() {
        this.dialogVisible = false;
        this.emptyEmpData();
      },
      showEditEmpView(row) {
        console.log(row)
        this.dialogTitle = "编辑用户";
        this.user = row;
        this.dialogVisible = true;
        console.log(this.user)
      },
      showAddEmpView() {
        this.dialogTitle = "添加用户";
        this.dialogVisible = true;
        this.emptyEmpData();
      },
      emptyEmpData() {
        this.param =  new FormData();//表单上传的方法
        this.imgurl = "";
        this.user = {
          name: '',//姓名
          loginName: '',//登录名
          roleType:'',
          keepAlive: '是',//封禁状态
        }
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
<style>
  .el-dialog__body {
    padding-top: 0px;
    padding-bottom: 0px;
  }

  .slide-fade-enter-active {
    transition: all .8s ease;
  }

  .slide-fade-leave-active {
    transition: all .8s cubic-bezier(1.0, 0.5, 0.8, 1.0);
  }

  .slide-fade-enter, .slide-fade-leave-to {
    transform: translateX(10px);
    opacity: 0;
  }
</style>
