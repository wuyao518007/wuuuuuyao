<template>
  <div>
    <el-header class="home-header">
      <span class="home_title">
       <router-link tag="a" target="_blank" :to="{path:'/'}" style="color: #ffffff;text-decoration: none;cursor: pointer;">爱家智能家具</router-link>
      </span>
      <div style="display: flex;align-items: center;margin-right: 17px">
        <el-dropdown >
            <span class="el-dropdown-link home_userinfo" style="display: flex;align-items: center">
              {{user.name}}
            </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="logout" divided>注销</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </el-header>
    <el-container>
      <el-main style="padding-left: 50px;padding-top: 0px;margin-top:10px">
        <el-row>
          <el-col :span="14">
             <div style="width: 100%;height: 400px">
           <img :src="ImgUrl" style="width: 100%;height: 100%">
         </div>
        <div class="little_img" >
            <ul v-for="img in imgUrls" style=" padding-left: 5px;float: left;list-style: none;">
                <li @click='getIndex(img)' >
                  <img :src="img" style="width: 100px; height: 100px" >
                </li>
            </ul>
        </div>
          </el-col>
          <el-col :span="10" style="text-align: left;padding-left:20px;font-size:12px">
            <h1>{{imgData.name}}</h1>
            <div style="margin-bottom: 20px;"> <span style="background: #cf9236;padding:6px;color:#ffffff;">{{imgData.type}}</span></div>
            <div  style="margin-bottom: 20px;font-size:16px"> <span><i style="padding:6px;color:#9E9E9E;font-style: normal;">预约定金：</i>{{imgData.price}}</span></div>
            <div style="margin-bottom: 20px;font-size:16px"> <span><i style="padding:6px;color:#9E9E9E;font-style: normal;">定制周期：</i>30天左右（视配送距离而定）</span></div>

            <div style="margin-bottom:20px">
              <el-input type="textarea" v-model="context" style="width: 80%;margin-bottom:20px" :rows="4" placeholder="定制留言。。。。"></el-input>
                  <el-button type="primary"  style="margin-left: 5px"  @click="submitClick(event,imgData.id)">
                     立即定制
                  </el-button>
              <el-button type="primary"  style="margin-left: 5px" @click="submitClick">加入购物车</el-button>
            </div>
          </el-col>
        </el-row>
        <el-row>
          <el-tabs v-model="activeName">
            <el-tab-pane label="详情" name="first">
              <h1>细节展示</h1>
              <ul v-for="img in imgUrls" style=" padding-left: 5px;float: left;list-style: none;">
              <li @click='getIndex(img)' >
                 <img :src="img" style="width: 100%; height: 100%" >
              </li>
           </ul>
            </el-tab-pane>
            <el-tab-pane label="评论" name="second">
              <div>
              <div style="margin-top:20px;    padding: 0px 4%;">
                <el-row style="margin-bottom: 20px;width: 80%;"  v-if="tableyo">
                  <div>
                    暂无评论，快来抢沙发啦啦啦啦啦
                  </div>
                </el-row>
                <el-row style="border-bottom: 1px solid #190a0a3b" v-for="(item,index) in tableData" :key="item.id" v-else>
                  <el-col :span="2" style="padding: 10px;height: 100px;background: #ddd;">
                    <el-row>
                      <img :src="'http://localhost:8089/user/findById?id='+item.uid"
                           style="width: 40px;height: 40px;margin-right: 5px;margin-left: 5px;border-radius: 40px"/>
                    </el-row>
                    <el-row>
                      {{item.name}}
                    </el-row>
                  </el-col>
                  <el-col :span="18" style="padding: 10px;text-align: left">
                    {{item.context}}
                  </el-col>
                </el-row>

              </div>
              <el-pagination
                background
                :page-size="5"
                :current-page="currentPage"
                @current-change="currentChange"
                layout="prev, pager, next"
                :total="totalCount">
              </el-pagination>
              </div>
            </el-tab-pane>
          </el-tabs>

        </el-row>
      </el-main>
    </el-container>
  </div>
</template>
<script>
  export default {
  computed: {
    user(){
      console.log(this.$store.state.user);
      console.log(this.$router.options.routes);
      return this.$store.state.user;
    },
  },
    mounted: function () {
      console.log(this.imgId);
      this.loadData();
      this.initCData(this.currentPage,this.pagesize);
    },

    data() {
      return {
        activeName: 'first',
        context:"",//定制留言
        imgData:{},
        imgId:this.$route.query.id,
        imgUrls: [],
        //大图片默认显示第一张
        ImgUrl:'',
        //默认每页数据量
        pagesize: 5,
        tableData: [],
        totalCount: -1,//默认数据总数
        currentPage: 1, //当前页码
        tableyo:true
      }
    },
    methods:{
      //点击小图片时将图片路径赋值给大图
      getIndex(imgUrl){
        this.ImgUrl = imgUrl;
      },

      //初始化数据
      loadData() {
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


      //提交定制
      submitClick(event,id){
        var _this = this;
        var uid = this.$store.state.user.id;
        if(uid == undefined){
          this.$message({type: "failure", message: "请先登录"});
          return;
        }
        var param  ={imid:_this.imgId,price:this.imgData.price,uid:uid,context:_this.context,state:'待支付'}
        this.postRequest("/order/insert" , param).then(resp=> {
          if (resp && resp.status == 200) {
            var data = resp.data;

            if(id!=undefined){
              console.log(id)
              let routeData =  this.$router.resolve({
                path: '/paying',
                query: {
                  id:id,
                  mark: 'one'
                }
              })
              window.open(routeData.href, '_blank');
            }else{
              this.$message({type: data.returnValue, message: data.message});
            }
          }
        })
      },
      initCData(pageNum, pageAmount){
        var _this = this;
        //var uid = this.$store.state.user.id;
        var param  ={imid:_this.imgId,pageNum:pageNum, pageAmount:pageAmount}
        this.postRequest("/comment/findList" , param).then(resp=> {
          if (resp && resp.status == 200) {
            _this.tableData = resp.data.pagedata;
            _this.tableyo = _this.tableData.length ==0;
            console.log(_this.tableData)
            _this.totalCount = resp.data.total;
          }
        })
      },


      //翻页
      currentChange(currentChange) {
        this.currentPage = currentChange;
        this.initCData(this.currentPage, this.pagesize);
      },
    }
  };
</script>
<style>
  body {
  margin: 0px;
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
    margin-left: 18px;
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
