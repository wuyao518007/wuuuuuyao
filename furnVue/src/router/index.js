import Vue from 'vue'
import Router from 'vue-router'
import Login from '@/components/Login'
import Home from '@/components/Home'//管理员登录页
import sys from '@/components/Home2'//用户登录主页
import EmpBasic from '@/components/emp/EmpBasic'//管理员---用户管理
import Img from '@/components/emp/Img'//管理员---家具管理
import ImgType from '@/components/emp/ImgType'//管理员---家具类型管理
import Role from '@/components/emp/Role'//管理员---角色管理
import ImgHost from '@/components/emp/Details'//管理员---详情页
import OrderBasic from '@/components/emp/OrderBasic'//管理员---定制管理
import Count from '@/components/system/Count'//管理员---统计
//import CommentBasic from '@/components/emp/CommentBasic'//管理员---评论

import UserHome from '@/components/system/userHome'
import UserBasic from '@/components/system/UserBasic'//用户---个人主页--个人信息
import paying from '@/components/system/paying'//用户---付款
import Order from '@/components/system/Order'//用户---个人主页--定制管理
//import Comment from '@/components/system/comment'//用户---个人主页--评论
import address from '@/components/system/address'//用户---个人主页--收获地址


Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: Login,
      hidden: true
    }, {
      path: '/home',
      name: '管理员主页',
      component: Home,
      hidden: true,
      meta: {
        requireAuth: true
      },
      children: [
      ]
    }, {
      path: '/UserHost',
      name: '个人中心',
      component: UserHome,
      hidden: true,
      meta: {
        requireAuth: true
      },
      children: [
        {
          path: '/Order',
          name: '定制管理',
          component: Order,
          meta: {
            keepAlive: false
          }
        },{
          path: '/UserAddress',
          name: '收货地址',
          component: address,
          meta: {
            keepAlive: false
          }
        },{
          path: '/UserBasic',
          name: '个人信息',
          component: UserBasic,
          meta: {
            keepAlive: false
          }
        },
      ]
    }, {
      path: '/',
      name: 'sys',
      component: sys,
      hidden: true,
      meta: {
        requireAuth: true
      }
    },{
      path: '/imghost',
      name: '家具详情',
      component: ImgHost,
      hidden: true,
      meta: {
        keepAlive: false
      }
    },
    {
      path: '/home',
      name: '系统管理',
      component: Home,
      children: [
        {
          path: '/EmpBasic',
          name: '用户管理',
          component: EmpBasic,
          meta: {
            keepAlive: false
          }
        },
        {
          path: '/img',
          name: '家具管理',
          component: Img,
          meta: {
            keepAlive: false
          }
        },
        {
          path: '/imgType',
          name: '家具类型',
          component: ImgType,
          meta: {
            keepAlive: false
          }
        },
        {
          path: '/role',
          name: '角色管理',
          component: Role,
          meta: {
            keepAlive: false
          }
        },
        {
          path: '/OrderBasic',
          name: '定制管理',
          component: OrderBasic,
          meta: {
            keepAlive: false
          }
        },
        {
          path: '/Count',
          name: '统计信息',
          component: Count,
          meta: {
            keepAlive: false
          }
        },
      ]
    }, {
      path: '/paying',
      name: '付款',
      component: paying,
      hidden: true,
      meta: {
        requireAuth: true
      },
      children: [
      ]
    }

  ]
})
