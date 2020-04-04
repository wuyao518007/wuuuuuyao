<template>
  <div>
    <el-container>
      <el-header style="padding: 0px;display:flex;justify-content:space-between;align-items: center;margin-top:20px">
        <div style="display: inline">
          <el-form :model="form" :inline="true" ref="addEmpForm" style="padding: 0px;">
            <el-form-item label="家具类型:"  prop="name" style="margin-left: -5px;">
              <el-select v-model="form.type" placeholder="请选择类型">
                <el-option
                  v-for="item in imgType"
                  :key="item.id"
                  :label="item.type"
                  :value="item.type">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="商品名:"  prop="name" style="margin-left: -5px;">
              <el-input
                placeholder="商品名"
                clearable
                style="width: 300px;margin: 0px;padding: 0px;"
                prefix-icon="el-icon-search"
                v-model="form.name">
              </el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" size="mini" style="margin-left: 5px" icon="el-icon-search" @click="searchEmp">搜索</el-button>
            </el-form-item>
          </el-form>
        </div>
      </el-header>
      <el-main style="padding-left: 0px;padding-top: 0px">
        <div>
          <el-table
            :data="roles"
            v-loading="tableLoading"
            border
            stripe
            @selection-change="handleSelectionChange"
            size="mini"
            style="width: 100%">
            <el-table-column
              prop="name"
              align="left"
              label="商品名称">
            </el-table-column>
            <el-table-column
              prop="type"
              align="left"
              label="商品类型">
            </el-table-column>
            <el-table-column
              prop="kc"
              align="left"
              label="库存">
            </el-table-column>
            <el-table-column
              prop="kc"
              align="left"
              label="销量">
              <template slot-scope="scope">
                {{scope.row.num-scope.row.kc}}
              </template>
            </el-table-column>
            <el-table-column
              prop="num"
              align="left"
              label="进货量">
            </el-table-column>
            <el-table-column
              prop="jprice"
              align="left"
              label="进价">
            </el-table-column>
            <el-table-column
              prop="price"
              align="left"
              label="定价">
            </el-table-column>
            <el-table-column
              align="left"
              label="盈利"
              width="105">
              <template slot-scope="scope">
                {{(scope.row.price-scope.row.jprice)*(scope.row.num-scope.row.kc)}}
              </template>
            </el-table-column>
          </el-table>

          <div style="display: flex;justify-content: space-between;margin: 2px">
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
  </div>
</template>
<script>
  export default {
    data() {
      return {
        uid : this.$store.state.user.id,
        form:{
          name:'',
          type:'',
          imName:''
        },
        //搜索条件
        criteria: '',
        //默认每页数据量
        pagesize: 10,
        roles: [],
        name: '',
        imgType:[],//家具类型
        dialogTitle: '',
        multipleSelection: [],
        totalCount: -1,//默认数据总数
        currentPage: 1, //当前页码
        dialogVisible: false,
        tableLoading: false,
        role: {
          name: '',//
        },
      };
    },
    mounted: function () {
      this.loadData(this.currentPage,this.pagesize);
    },
    methods: {

      loadData(pageNum, pageAmount) {
        var _this = this;
        this.tableLoading = true;
        var param  ={type:_this.form.type,name:_this.form.name,state:'待支付',pageNum:pageNum, pageAmount:pageAmount}
        this.postRequest("/order/findOrderList", param).then(resp => {
          this.tableLoading = false;
          if (resp && resp.status == 200) {
            var data = resp.data;
            _this.roles = data.pagedata;
            _this.totalCount = data.total;
          }
        });
        //家具类型
        this.postRequest("/imgType/findAllList", {}).then(resp => {
          if (resp && resp.status == 200) {
            var data = resp.data;
            _this.imgType = data;
          }
        })
      },
      // loadData(pageNum, pageAmount) {
      //   var _this = this;
      //   this.tableLoading = true;
      //   var param  ={imName:_this.form.imName,name:_this.form.name,state:'待支付',pageNum:pageNum, pageAmount:pageAmount}
      //   this.postRequest("/order/findOrderList", param).then(resp => {
      //     this.tableLoading = false;
      //     if (resp && resp.status == 200) {
      //       var data = resp.data;
      //       _this.roles = data.pagedata;
      //       _this.totalCount = data.total;
      //     }
      //   })
      // },
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
        this.$confirm('此操作将永久删除该评论, 是否继续?', '提示', {
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
        this.deleteRequest("/order/delete/" + ids).then(resp => {
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
            if (this.role.id) {
              //更新
              this.tableLoading = true;
              this.postRequest("/role/update", this.role).then(resp => {
                _this.tableLoading = false;
                if (resp && resp.status == 200) {
                  var data = resp.data;
                  _this.dialogVisible = false;
                  _this.emptyEmpData();
                  this.$message({type: data.returnValue, message: data.message});
                  _this.loadData(this.currentPage,this.pagesize);
                }
              })
            } else {
              //添加
              this.tableLoading = true;
              this.postRequest("/role/insert", this.role).then(resp => {
                _this.tableLoading = false;
                if (resp && resp.status == 200) {
                  var data = resp.data;
                  _this.dialogVisible = false;
                  _this.emptyEmpData();
                  this.$message({type: data.returnValue, message: data.message});
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
        this.dialogTitle = "编辑类型";
        this.role = row;
        this.dialogVisible = true;
      },
      showAddEmpView() {
        this.dialogTitle = "添加类型";
        this.dialogVisible = true;
        this.emptyEmpData();
      },
      emptyEmpData() {
        this.role = {
          name: '',//类型
        }
      },
      handleCommand(cmd){
        var _this = this;
        if (cmd == 'logout') {
          this.$confirm('注销登录, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            //_this.getRequest("/logout");
            _this.$store.commit('logout');
            //window.localStorage.setItem('user', null);
            _this.$router.replace({path: '/'});
          }).catch(() => {
            _this.$message({
              type: 'info',
              message: '取消'
            });
          });
        }
      }
    },
    computed: {
      user() {
        console.log(this.$store.state.user);
        console.log(this.$router.options.routes);
        return this.$store.state.user;
      },
      routes() {
        console.log(this.$router.options.routes);
        return this.$store.state.routes
      }
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

  .el-submenu .el-menu-item {
    width: 180px;
    min-width: 175px;
  }
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
