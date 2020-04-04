<template>
  <div style="margin-top: 10px" >
    <div style="margin-bottom: 10px;display: flex;justify-content: center;align-items: center">
      <el-form :model="ruleForm" :inline="true" ref="addEmpForm" style="margin: 0px;padding: 0px;">
        <el-form-item label="家具类型:"  prop="name" style="margin-left: -5px;">
          <el-select v-model="ruleForm.type" placeholder="请选择类型">
            <el-option
              v-for="item in imgType"
              :key="item.id"
              :label="item.type"
              :value="item.type">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="名称:"  prop="name" style="margin-left: -5px;">
          <el-input
            placeholder="搜索更多..."
            prefix-icon="el-icon-search"
            size="small"
            style="width: 400px;margin-right: 10px"
            v-model="ruleForm.name">
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-button size="small" type="primary" icon="el-icon-search" @click="searchClick">搜索</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div>
      <el-menu
        id="topmenu"
        class="el-menu-demo"
        mode="horizontal"
        @select="handleSelect"
        background-color="#545c64"
        text-color="#fff"
        active-text-color="#ffd04b">
        <el-menu-item index="">全部</el-menu-item>
        <el-submenu index="2">
          <template slot="title">分类</template>
          <el-menu-item
            v-for="item in imgType"
            :index="item.type">
            {{item.type}}
          </el-menu-item >
        </el-submenu>
      </el-menu></div>
    <Slider></Slider>
    <div style="display: flex;justify-content: space-around;flex-wrap: wrap;text-align: left;margin-top: 10px">
      <el-card style="width: 350px;margin-bottom: 20px" v-for="(item,index) in tableData" :key="item.id">
        <div>
          <div style="width: 100%;display: flex;justify-content: center">
            <router-link tag="a" target="_blank" :to="{path:'/imghost',query:{id:item.id}}">
              <img :src="item.url+'img/findById?imgid='+item.id" width="360px" alt="item.name" >
            </router-link>
          </div>
          <!--<div style="text-align: right;">
            <div>
              <span class="user-info" style="color: #f67609;" @click="deletegood(item.id)" v-if="item.fid != 0">
                <i class="fa fa-star"></i>已定制</span>
              <span class="user-info" style="color: #f67609;"  @click="insertgood(item.id)"  v-else>
                <i class="fa fa-star-o"></i>定制</span>
              </div>
          </div>-->
        </div>
        <div  style="text-align: center;padding: 10px">
          <span>{{item.name}} </span>
          <span style="background: #cf9236;padding:2px;color:#ffffff;float: right;font-size: 12px">{{item.type}}</span>
        </div>
      </el-card>
    </div>

    <el-pagination
      background
      :page-size="9"
      :current-page="currentPage"
      @current-change="currentChange"
      layout="prev, pager, next"
      :total="totalCount">
    </el-pagination>
  </div>
</template>
<script>

  import Slider from "../Slider.vue"
  export default{
    components:{
      Slider
    },
    data(){
      return {
        ruleForm:{
          name:'',
          type:''
        },
        imgType:[],//图片类型
        message: '链接',
        name: '',
        //默认每页数据量
        pagesize: 9,
        tableData: [],
        totalCount: -1,//默认数据总数
        currentPage: 1, //当前页码
      }
    },
    mounted: function () {
      this.initCards(this.currentPage,this.pagesize);
      this.loadType();
    },
    methods: {
      searchClick(){
        this.initCards(this.currentPage,this.pagesize);
      },
      handleSelect(key, keyPath) {
        console.log(key, keyPath);
        this.ruleForm.type=key;
        this.initCards(1,10);
      },
      loadType() {
        var _this = this;
        this.postRequest("/imgType/findAllList", {}).then(resp => {
          if (resp && resp.status == 200) {
            var data = resp.data;
            _this.imgType = data;
          }
        })
      },
      initCards(pageNum, pageAmount){
        var _this = this;
        var param  ={name:_this.ruleForm.name,type:_this.ruleForm.type,pageNum:pageNum, pageAmount:pageAmount}
        this.postRequest("/img/findList" , param).then(resp=> {
          if (resp && resp.status == 200) {
            _this.tableData = resp.data.pagedata;
            console.log(_this.tableData)
            _this.totalCount = resp.data.total;
          }
        })
      },

      //添加点赞、收藏
      insertgood(id,m){
        var _this = this;
        var param  ={imid:id};
        this.postRequest("/goodlike/insert" , param).then(resp=> {
          if (resp && resp.status == 200) {
            var data = resp.data;
            if (data.returnValue == 'success') {
              _this.initCards(this.currentPage,this.pagesize);
            }
          }
        })
      },
      //删除点赞、收藏
      deletegood(id,m){
        var _this = this;
        var url ='';
        if(m=='g'){
          url = "/goodlike/delete";
        }
        if(m=='f'){
          url = "/favorite/delete";
        }
        var param  ={imid:id};
        this.postRequest(url,param).then(resp=> {
          if (resp && resp.status == 200) {
            var data = resp.data;
            if (data.returnValue == 'success') {
              _this.initCards(this.currentPage,this.pagesize);
            }
          }
        })
      },
      //翻页
      currentChange(currentChange) {
        this.currentPage = currentChange;
        this.initCards(this.currentPage, this.pagesize);
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
           // window.localStorage.setItem('user', null);
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
    user(){
      console.log(this.$store.state.user);
      this.$store.state.user.id = 0;
      return this.$store.state.user;
     },
   }
  }
</script>
<style>
  .user-info {
    font-size: 12px;
    color: #09c0f6;
    cursor: pointer;
    margin-right: 5px;
  }
  .el-card__body {
    padding: 0px 20px;
  }
  div.el-menu--horizontal{
    height:300px;
    overflow: auto;
  }
</style>
