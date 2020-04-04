<template>
  <div>
    <el-container>
      <el-header style="padding: 0px;display:flex;justify-content:space-between;align-items: center">
        <div style="display: inline">
          <el-input
            placeholder="地址"
            clearable
            style="width: 300px;margin: 0px;padding: 0px;"
            @keyup.enter.native="searchEmp"
            prefix-icon="el-icon-search"
            v-model="addressName">
          </el-input>
          <el-button type="primary" size="mini" style="margin-left: 5px" icon="el-icon-search" @click="searchEmp">搜索
          </el-button>
        </div>
        <div style="margin-left: 5px;margin-right: 20px;display: inline">
          <el-button type="primary" size="mini" icon="el-icon-plus"
                     @click="showAddEmpView">
            添加地址
          </el-button>
        </div>
      </el-header>
      <el-main style="padding-left: 0px;padding-top: 0px">
        <div>
          <el-table
            :data="addresss"
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
              prop="name"
              align="left"
              label="姓名">
            </el-table-column>
            <el-table-column
              prop="address"
              align="left"
              label="地址">
            </el-table-column>
            <el-table-column
              prop="phone"
              align="left"
              label="手机号">
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
            <el-button type="danger" size="mini" v-if="addresss.length>0" :disabled="multipleSelection.length==0"
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
    <el-form :model="address" :rules="rules" :inline="true" ref="addEmpForm" style="margin: 0px;padding: 0px;width:1%">
      <div style="text-align: left">
        <el-dialog
          :title="dialogTitle"
          style="padding: 0px;"
          :close-on-click-modal="false"
          :visible.sync="dialogVisible"
          width="27%">
          <el-form-item label="姓名:" prop="name">
            <el-input prefix-icon="el-icon-edit" v-model="address.name" size="mini"
                      placeholder="请输入姓名"></el-input>
          </el-form-item>
          <el-form-item label="地址:" prop="address">
            <el-input prefix-icon="el-icon-edit" v-model="address.address" size="mini"
                      placeholder="请输入姓名"></el-input>
          </el-form-item>
          <el-form-item label="设为默认:" prop="statu">
            <el-radio-group v-model="address.statu">
              <el-radio label="是">是</el-radio>
              <el-radio style="margin-left: 15px" label="否">否</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="手机号:" prop="phone">
            <el-input prefix-icon="el-icon-edit" v-model="address.phone" size="mini"
                      placeholder="请输入手机号"></el-input>
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
        uid : this.$store.state.user.id,
        imgurl:'',//图片url
        param: new FormData(),//表单上传的方法
        //搜索条件
        criteria: '',
        //默认每页数据量
        pagesize: 10,
        addresss: [],
        addressName: '',
        beginDateScope: '',
        dialogTitle: '',
        multipleSelection: [],
        totalCount: -1,//默认数据总数
        currentPage: 1, //当前页码
        dialogVisible: false,
        tableLoading: false,
        roleType:[],
        address: {
          name: '',//姓名
          address: '',//地址
          statu: '否',//是否默认
          phone:'' //手机号
        },
        rules: {
          name: [{required: true, message: '必填:姓名', trigger: 'blur'}],
          address: [{required: true, message: '必填:地址', trigger: 'blur'}],
          phone: [
            { required: true, message: '请输入手机号' },
            { min: 11, max: 11, message: '长度为11个字符' }
          ]
        }
      };
    },
    mounted: function () {
      this.loadData(this.currentPage,this.pagesize);
    },
    methods: {
      loadData(pageNum, pageAmount) {
        var _this = this;
        this.tableLoading = true;
        var param  ={address:_this.addressName,uid:_this.uid,pageNum:pageNum, pageAmount:pageAmount}
        this.postRequest("/address/findList", param).then(resp => {
          this.tableLoading = false;
          if (resp && resp.status == 200) {
            var data = resp.data;
            _this.addresss = data.pagedata;
            _this.totalCount = data.total;
          }
        })
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
        this.deleteRequest("/address/delete/" + ids).then(resp => {
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
            this.address.uid = this.$store.state.user.id
            if (this.address.id) {
              //更新
              this.tableLoading = true;
              this.postRequest("/address/update", this.address).then(resp => {
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
              this.postRequest("/address/insert", this.address).then(resp => {
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
        this.dialogTitle = "编辑";
        this.address = row;
        this.dialogVisible = true;
        console.log(this.address)
      },
      showAddEmpView() {
        this.dialogTitle = "添加";
        this.dialogVisible = true;
        this.emptyEmpData();
      },
      emptyEmpData() {
        this.address = {
          name: '',//姓名
          address: '',//地址
          statu: '否',//是否默认
          phone:'' //手机号
        }
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
