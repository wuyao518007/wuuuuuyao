<template>
  <div>
    <el-container>
      <el-header style="padding: 0px;display:flex;justify-content:space-between;align-items: center;margin-top:20px">
        <div style="display: inline">
          <el-form :model="form" :inline="true" ref="addEmpForm" style="padding: 0px;">
            <el-form-item label="用户姓名:"  prop="name" style="margin-left: -5px;">
              <el-input
                placeholder="请输入用户姓名"
                clearable
                style="width: 300px;margin: 0px;padding: 0px;"
                prefix-icon="el-icon-search"
                v-model="form.name">
              </el-input>
            </el-form-item>
            <el-form-item label="关键字:"  prop="name" style="margin-left: -5px;">
              <el-input
                placeholder="关键字"
                clearable
                style="width: 300px;margin: 0px;padding: 0px;"
                prefix-icon="el-icon-search"
                v-model="form.keywords">
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
              type="selection"
              align="left"
              width="30">
            </el-table-column>
            <el-table-column
              prop="id"
              align="left"
              fixed
              label="id">
            </el-table-column>
            <el-table-column
              prop="name"
              align="left"
              label="姓名">
            </el-table-column>
            <el-table-column
              prop="imName"
              align="left"
              label="定制商品">
            </el-table-column>
            <el-table-column
              prop="createTime"
              align="left"
              width="134"
              label="定制时间">
            </el-table-column>
            <el-table-column
              prop="state"
              align="left"
              label="定制状态"
              width="105">
            </el-table-column>
            <el-table-column
              prop="context"
              align="left"
              width="295"
              label="定制留言">
            </el-table-column>
            <el-table-column
              fixed="right"
              label="操作"
              width="175">
              <template slot-scope="scope">
                <el-button slot="reference" type="primary" size="mini" style="padding: 3px 4px 3px 4px;margin: 2px"
                           @click="updateEmp(scope.row)">
                  {{scope.row.state}}
                </el-button>
                <el-button type="danger" style="padding: 3px 4px 3px 4px;margin: 2px" size="mini"
                           @click="deleteEmp(scope.row)">删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <div style="display: flex;justify-content: space-between;margin: 2px">
            <el-button type="danger" size="mini" v-if="roles.length>0" :disabled="multipleSelection.length==0"
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
  </div>
</template>
<script>
  export default {
    data() {
      return {
        form:{
          name:'',
          keywords:''
        },
        //搜索条件
        criteria: '',
        //默认每页数据量
        pagesize: 10,
        roles: [],
        name: '',
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
        var param  ={name:_this.form.name,keywords:_this.form.keywords,pageNum:pageNum, pageAmount:pageAmount}
        this.postRequest("/order/findList", param).then(resp => {
          this.tableLoading = false;
          if (resp && resp.status == 200) {
            var data = resp.data;
            _this.roles = data.pagedata;
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


      updateEmp(row) {
        var _this = this;
       if(row.state == '待发货'){
          row.state = '已发货'
        }else if(row.state == '已发货'){
          row.state = '待收货'
        }else if(row.state == '待收货'){
          row.state = '已收货'
        }else if(row.state == '已收货'){
          row.state = '订单完成'
        }
        this.postRequest("/order/updateState", row).then(resp => {
          if (resp && resp.status == 200) {
            var data = resp.data;
            this.$message({type: data.returnValue, message: data.message});
          }
        })
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
