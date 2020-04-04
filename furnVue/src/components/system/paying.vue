<template>
  <div>
    <el-container class="home-container">
    <el-header class="home-header">
        <span class="home_title">
           <router-link tag="a" target="_blank" :to="{path:'/'}" style="color:#ffffff;text-decoration: none">
               爱家智能家具
            </router-link>
         </span>
        <div style="display: flex;align-items: center;margin-right: 7px">
          <el-dropdown @command="handleCommand">
            <span class="el-dropdown-link home_userinfo" style="display: flex;align-items: center">
              {{user.name}}
               <i><img :src="'http://localhost:8089/user/findById?id='+user.id"
                       style="width: 40px;height: 40px;margin-right: 5px;margin-left: 5px;border-radius: 40px"/></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="logout" divided>注销</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </el-header>
        <div style="display: inline">
          <h1>购物车</h1>
        </div>
      <div style="display: inline">
        <el-form :model="form" :inline="true" ref="addEmpForm" style="padding: 0px;">
          <el-form-item label="收货地址:"  prop="name" style="margin-left: -5px;">
            <el-select v-model="form.address" placeholder="请选择">
              <el-option
                v-for="item in addresss"
                :key="item.id"
                :label="item.address"
                :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>
      </div>
      <el-main style="padding-left: 0px;padding-top: 0px;">
        <div>
          <el-table
            :data="roles"
            v-loading="tableLoading"
            border
            stripe
            @selection-change="handleSelectionChange"
            size="mini"
            style="width: 60%;margin: auto">
            <el-table-column
              type="selection"
              align="left"
              width="30">
            </el-table-column>
            <el-table-column
              align="left"
              fixed
              width="100px"
              label="图片">
              <template slot-scope="scope">
                <img :src="'data:image/jpeg;base64,'+ scope.row.image"  style="width: 70px;height: 70px;margin-right: 5px;margin-left: 5px;"/>
              </template>
            </el-table-column>
            <el-table-column
              prop="imName"
              align="left"
              label="名称">
            </el-table-column>
            <el-table-column
              prop="price"
              align="left"
              width="100px"
              label="价格">
            </el-table-column>
            <el-table-column
              prop="createTime"
              align="left"
              width="134"
              label="定制时间">
            </el-table-column>
          </el-table>
          <div style="display: flex;justify-content: space-between;padding: 10px;width: 60%;margin: auto">
            <el-pagination
              background
              :page-size="10"
              :current-page="currentPage"
              @current-change="currentChange"
              layout="prev, pager, next"
              :total="totalCount">
            </el-pagination>
            <el-button type="danger" size="mini" v-if="roles.length>0" :disabled="multipleSelection.length==0"
                       @click="deleteManyEmps">付款
            </el-button>
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
        imgData:{},
        ImgUrl:'',
        imgId:'',
        mark: '',
        form:{
          address:'',
          name:'',
          keywords:''
        },
        //搜索条件
        criteria: '',
        //默认每页数据量
        pagesize: 10,
        roles: [],
        name: '',
        addresss:[],
        dialogTitle: '',
        multipleSelection: [],
        totalCount: -1,//默认数据总数
        currentPage: 1, //当前页码
        dialogVisible: false,
        tableLoading: false,
        imgids:'',
        role: {
          name: '',//
        },
      };
    },
    mounted: function () {
      this.imgId = this.$route.query.id
      this.mark = this.$route.query.mark;
      this.loadData(this.currentPage,this.pagesize);
    //  this.loadImgData();

    },
    methods: {
      loadData(pageNum, pageAmount) {
        var _this = this;
        this.tableLoading = true;
        var param  ={uid:_this.uid,state:'待支付',keywords:_this.form.keywords,pageNum:pageNum, pageAmount:pageAmount}
        if(this.mark=='one'){
          param  ={uid:_this.uid,state:'待支付',imid:this.imgId,keywords:_this.form.keywords,pageNum:pageNum, pageAmount:pageAmount}

        }
        this.postRequest("/order/findList", param).then(resp => {
          this.tableLoading = false;
          if (resp && resp.status == 200) {
            var data = resp.data;
            _this.roles = data.pagedata;
            _this.totalCount = data.total;
          }
        }),
        this.postRequest("/address/findAllList", {}).then(resp => {
          if (resp && resp.status == 200) {
            var data = resp.data;
            _this.addresss = data;
          }
        })
      },
      //初始化数据
      loadImgData() {
        var _this = this;
        var param  ={imgId:_this.imgId}
        this.postRequest("img/findListImg", param).then(resp => {
          if (resp && resp.status == 200) {
            var param = resp.data;
            this.imgData = param;
            var data = param.picture;
            console.log(param);
            console.log(data);
            this.ImgUrl = 'data:image/jpeg;base64,'+ data[0].picture;
            for(var i in data){
              this.imgUrls.push('data:image/jpeg;base64,'+ data[i].picture);
            }
            console.log(this.imgUrls);
          }
        })
      },
      handleSelectionChange(val) {
        this.multipleSelection = val;
      },
      deleteManyEmps() {

        if(this.form.address==''){
          this.$message({type: 'failure', message: '请选择收货地址'});
          return;
        }
        this.$confirm('确定付款?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          var ids = '';
          for (var i = 0; i < this.multipleSelection.length; i++) {
            ids += this.multipleSelection[i].id + ",";
            this.imgids += this.multipleSelection[i].imid + ",";
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

        this.deleteRequest("/order/update/" + ids).then(resp => {
          _this.tableLoading = false;
          if (resp && resp.status == 200) {
            var data = resp.data;
            //更新库存
            this.updatekc();
            _this.loadData(this.currentPage,this.pagesize);
          }
        })
      },
      //更新库存
      updatekc() {
        var _this = this;
        this.deleteRequest("/img/updatekc/" + this.imgids).then(resp => {
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

  .home-container {
    height: 100%;
    position: absolute;
    top: 0px;
    left: 0px;
    width: 100%;
  }

  .home-header {
    background-color: #20a0ff;
    color: #333;
    text-align: center;
    display: flex;
    align-items: center;
    justify-content: space-between;
    box-sizing: content-box;
    padding: 0px;
  }

  .home-aside {
    background-color: #ECECEC;
  }

  .home-main {
    background-color: #fff;
    color: #000;
    text-align: center;
    margin: 0px;
    padding: 0px;;
  }

  .home_title {
    color: #fff;
    font-size: 22px;
    display: inline;
    margin-left: 8px;
  }

  .home_userinfo {
    color: #fff;
    cursor: pointer;
  }

  .home_userinfoContainer {
    display: inline;
    margin-right: 20px;
  }

  .el-submenu .el-menu-item {
    width: 180px;
    min-width: 175px;
  }
</style>
