<template>
  <div>
    <el-container class="home-container">
      <el-header class="home-header">
        <span class="home_title">爱家智能家具</span>
        <div style="display: flex;align-items: center;margin-right: 7px">
          <span class="el-dropdown-link home_userinfo" style="margin-right: 20px"  v-if="user.name != '未登录'">
             <router-link tag="a" target="_blank" :to="{path:'/paying'}" style="color:#ffffff;text-decoration: none">购物车</router-link>
          </span>
          <span class="el-dropdown-link home_userinfo" style="margin-right: 20px" @click="ismember" v-if="user.name == '未登录'">
             登录/注册
          </span>
          <el-dropdown @command="handleCommand" v-else>
            <span class="el-dropdown-link home_userinfo" style="display: flex;align-items: center">
              {{user.name}}
              <i><img :src="'http://localhost:8089/user/findById?id='+user.id"
                                    style="width: 40px;height: 40px;margin-right: 5px;margin-left: 5px;border-radius: 40px"/></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item :to="{ path: '/UserHost' }">
                <router-link tag="a" target="_blank" :to="{path:'/UserHost'}"  style="text-decoration: none">
                  个人中心
                </router-link>
              </el-dropdown-item>
              <el-dropdown-item @click.native="updatePasswordHandle()">修改密码</el-dropdown-item>
              <el-dropdown-item command="logout" divided>注销</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </el-header>
      <el-container>
        <el-main>
          <sysvue></sysvue>
        </el-main>
      </el-container>
    </el-container>

    <!-- 弹窗, 修改密码 -->
    <UpdatePassword v-if="updatePassowrdVisible" ref="updatePassowrd"></UpdatePassword>
  </div>
</template>
<script>
  import Swiper from "./Swiper.vue"
  import Slider from "./Slider.vue"
  import sysvue from "./system/system.vue"
  import UpdatePassword from './system/update-password.vue'
  export default{
    components:{
      sysvue,
      Slider,
      Swiper,
      UpdatePassword
    },
    mounted: function () {

    },
    methods: {

      // 修改密码
      updatePasswordHandle () {
        this.updatePassowrdVisible = true
        this.$nextTick(() => {
          this.$refs.updatePassowrd.init()
        })
      },
      ismember(){
        this.$router.replace({path: '/login'});
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
            this.$store.state.user.name = '未登录';
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
    data(){
      return {
        updatePassowrdVisible: false,
        name: "未登录",
        isDot: false
      }
    },
    computed: {
      user(){
        console.log(this.$store.state.user);
        return this.$store.state.user;
      },
      routes(){
        return this.$store.state.routes
      }
    }
  }
</script>
<style>
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
