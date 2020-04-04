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
            <el-form-item label="名称:"  prop="name" style="margin-left: -5px;">
              <el-input
                placeholder="通过家具名搜索,记得回车哦..."
                clearable
                style="width: 300px;margin: 0px;padding: 0px;"
                @keyup.enter.native="searchEmp"
                prefix-icon="el-icon-search"
                v-model="form.name">
              </el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" size="mini" style="margin-left: 5px" icon="el-icon-search" @click="searchEmp">搜索</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div style="margin-left: 5px;margin-right: 20px;display: inline">
          <el-button type="primary" size="mini" icon="el-icon-plus"
                     @click="showAddEmpView">
            添加
          </el-button>
        </div>
      </el-header>
      <el-main style="padding-left: 0px;padding-top: 0px">
        <div>
          <el-table
            :data="tableData"
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
              label="名称">
            </el-table-column>
            <el-table-column
              prop="type"
              align="left"
              label="类型">
            </el-table-column>
            <el-table-column
              prop="price"
              align="left"
              label="定价">
            </el-table-column>
            <el-table-column
              prop="kc"
              align="left"
              label="库存">
            </el-table-column>
            <el-table-column
              prop="jprice"
              align="left"
              label="进价">
            </el-table-column>
            <el-table-column
              prop="num"
              align="left"
              label="进货量">
            </el-table-column>
            <el-table-column
              prop="kc"
              align="left"
              label="库存">
            </el-table-column>
            <el-table-column
              prop="remark"
              align="left"
              label="简介">
            </el-table-column>
            <el-table-column
              fixed="right"
              label="操作"
              width="195">
              <template slot-scope="scope">
                <el-button @click="showEditEmpView(scope.row)" style="padding: 3px 4px 3px 4px;margin: 2px"
                           size="mini">编辑
                </el-button>
                <el-button slot="reference" type="primary" size="mini" style="padding: 3px 4px 3px 4px;margin: 2px">
                  <router-link tag="a" target="_blank" :to="{path:'/ImgHost',query:{id:scope.row.id}}"  style="text-decoration: none">
                    详情
                  </router-link>
               </el-button>
                <el-button type="danger" style="padding: 3px 4px 3px 4px;margin: 2px" size="mini"
                           @click="deleteEmp(scope.row)">删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <div style="display: flex;justify-content: space-between;margin: 2px">
            <el-button type="danger" size="mini" v-if="tableData.length>0" :disabled="multipleSelection.length==0"
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
    <el-form :model="ruleForm" :rules="rules" :inline="true" ref="addEmpForm" style="margin: 0px;padding: 0px;width:1%">
      <div style="text-align: left">
        <el-dialog
          :title="dialogTitle"
          style="padding: 0px;"
          :close-on-click-modal="false"
          :visible.sync="dialogVisible"
          width="57%">
            <el-form-item label="名称:"  prop="name" style="margin-left: -5px;">
              <el-input v-model="ruleForm.name" ></el-input>
            </el-form-item>
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
            <el-form-item label="进货价:"  prop="jprice" style="margin-left: -5px;">
              <el-input v-model="ruleForm.jprice" ></el-input>
            </el-form-item>
            <el-form-item label="定价:"  prop="price" style="margin-left: -5px;">
              <el-input v-model="ruleForm.price" ></el-input>
            </el-form-item>
            <el-form-item label="进货量:"  prop="num" style="margin-left: -5px;">
              <el-input v-model="ruleForm.num" ></el-input>
            </el-form-item>
            <el-form-item label="库存:"  prop="kc" style="margin-left: -5px;">
              <el-input v-model="ruleForm.kc" ></el-input>
            </el-form-item>
              <el-form-item label="地址:"  prop="url" style="margin-left: -5px;">
                <el-input v-model="ruleForm.url" ></el-input>
              </el-form-item>
            <el-form-item label="简介:"  prop="remark" style="margin-left: -5px;">
              <el-input v-model="ruleForm.remark" ></el-input>
            </el-form-item>
            <el-form-item prop="picture" style="margin-right: 10px;width: 90%;">
                <el-upload
                  action="aaa"
                  :on-preview="handleImgPreview"
                  :on-remove="handleImgRemove"
                  :before-upload="beforeupload"
                  :on-exceed="handleExceed"
                  accept="image/jpg,image/jpeg"
                >
                  <el-button slot="trigger" size="small" type="primary" style="margin-bottom: 10px"> <i class="el-icon-plus"></i>选取文件</el-button>
                </el-upload>
                <ul class="el-upload-list el-upload-list--picture-card">
                  <li tabindex="0" class="el-upload-list__item is-success" style="float: left" v-for="(item, index) in productPicPathList">
                    <img :src="item.url" alt="" class="el-upload-list__item-thumbnail">
                    <a class="el-upload-list__item-name">
                      <i class="el-icon-document"></i>{{item.name}}
                    </a>
                    <label class="el-upload-list__item-status-label">
                      <i class="el-icon-upload-success el-icon-check"></i>
                    </label>
                    <i class="el-icon-close"></i>
                    <i class="el-icon-close-tip">按 delete 键可删除</i><!---->
                    <span class="el-upload-list__item-actions">
                      <!--<span class="el-upload-list__item-preview">
                        <i class="el-icon-zoom-in"></i>
                      </span>-->
                      <span class="el-upload-list__item-delete" @click="handleImgRemove(item,productPicPathList)">
                        <i class="el-icon-delete"></i>
                      </span>
                    </span>
                  </li>
                </ul>
               <!-- <el-dialog :visible.sync="dialogContImgVisible" append-to-body>
                  <img width="100%" :src="dialogContImgUrl" />
                </el-dialog>-->
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
  import $axios from 'axios'
  export default {
    mounted: function () {
      this.loadData(this.criteria,this.currentPage,this.pagesize);
      this.loadType();
    },
    methods: {
      loadData(criteria,pageNum, pageAmount) {
        var _this = this;
        this.tableLoading = true;
        var param  ={name:_this.form.name,type:_this.form.type,pageNum:pageNum, pageAmount:pageAmount}
        this.postRequest("/img/findList", param).then(resp => {
          this.tableLoading = false;
          if (resp && resp.status == 200) {
            var data = resp.data;
            _this.tableData = data.pagedata;
            _this.totalCount = data.total;

            console.log(this.tableData);
          }
        })
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
      /*家具图片上传*/
      //家具图片删除事件
      handleImgRemove: function (file, fileList,index) {
        if(file.status == 'ready') return;//预览家具时返回
        console.log(file, fileList);
        console.log("删除事件");
        if(file.uid !=null && file.uid !='' && file.uid != undefined){
          this.removeIndex(file.uid,this.productPicPathList);
        }
        console.log(this.ruleForm.listZs);
        if(file.url !=null && file.url !='' && file.url !=undefined){
          if (index > -1) {
            //this.ruleForm.listZs.splice(index, 1);
          }
          let index2 = file.url.lastIndexOf('\/');
          let filename = file.url.substring(index2+1,file.url.length);
          this.fileNameList.push(filename);
        }

        if (index > -1) {
          //this.ruleForm.zsList.splice(index, 1);
          //this.fileName = this.ruleForm.listZs[index].url;
        }
      },
      //家具图片上传超过数量限制
      handleExceed: function () {
        this.$message.error('最多只能上传五张家具图片')
      },
      //家具图片预览事件
      handleImgPreview: function (file) {
        this.dialogContImgUrl = file.url;
        this.dialogContImgVisible = true;
      },
      //阻止upload的自己上传，进行再操作
      beforeupload(file) {
        console.log(file);
        //判断文件格式
        const isJPG = file.type ===  file.type === 'image/jpg' || file.type === 'image/jpeg';
        const isLt2M = file.size / 1024 / 1024 < 2;
        if (!isJPG) {
          this.$message.error('上传家具图片只能是 jpg、jpeg 格式!');
          return false;
        }
        if (!isLt2M) {
          this.$message.error('上传家具图片大小不能超过 2MB!');
          return false;
        }
        //创建临时的路径来展示家具
        var windowURL = window.URL || window.webkitURL;
        this.src=windowURL.createObjectURL(file);
        console.log(this.src);
        this.param.append('files', file, file.name);
        //家具加入预览内容
        this.productPicPathList.push({
          "name": file.name,
          "url": this.src,
          "uid": file.uid
        });
        return false;
      },
      //移除数组中的数据
      removeIndex(val,array) {
        var index = this.indexOf(val,array);
        if (index > -1) {
          array.splice(index, 1);
        }
      },
      indexOf(val,array) {
        for (var i = 0; i < array.length; i++) {
          if (array[i].uid == val) {
            return i;
          }
        }
        return -1;
      },

      downloadImg(row){
          var src='data:image/png;base64,'+ row.picture;
          this.srcImg = src; //家具回显
          var link = document.createElement('a');
          link.href = src;
          link.download =row.name+ ".jpg";
          link.click();
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
        this.deleteRequest("/img/delete/" + ids).then(resp => {
          _this.tableLoading = false;
          if (resp && resp.status == 200) {
            var data = resp.data;
            _this.loadData(this.criteria,this.currentPage,this.pagesize);
          }
        })
      },
      searchEmp() {
        this.loadData(this.criteria,this.currentPage,this.pagesize);
      },
      currentChange(currentChange) {
        this.currentPage = currentChange;
        this.loadData(this.criteria,this.currentPage, this.pagesize);
      },
      addEmp(formName) {
        var _this = this;
        this.$refs[formName].validate((valid) => {
          if (valid) {
            if (this.ruleForm.id) {
              //更新
              this.tableLoading = true;
              this.param.append('id', _this.ruleForm.id);//名称
              this.param.append('name', _this.ruleForm.name);//名称
              this.param.append('price', _this.ruleForm.price);//定价
              this.param.append('jprice', _this.ruleForm.jprice);//进价
              this.param.append('remark', _this.ruleForm.remark);//简介
              this.param.append('num', _this.ruleForm.num);//进货量
              this.param.append('kc', _this.ruleForm.num);//库存
              this.param.append('type', _this.ruleForm.type);//
              this.param.append('url', _this.ruleForm.url);//
              this.uploadFileRequest("/img/update", this.param).then(resp => {
                _this.tableLoading = false;
                if (resp && resp.status == 200) {
                  var data = resp.data;
                  _this.dialogVisible = false;
                  _this.emptyEmpData();
                  _this.loadData(this.criteria,this.currentPage,this.pagesize);
                }
              })
            } else {
              //添加
              this.tableLoading = true;
              this.param.append('name', _this.ruleForm.name);//名称
              this.param.append('price', _this.ruleForm.price);//定价
              this.param.append('jprice', _this.ruleForm.jprice);//进价
              this.param.append('remark', _this.ruleForm.remark);//简介
              this.param.append('num', _this.ruleForm.num);//进货量
              this.param.append('kc', _this.ruleForm.num);//库存
              this.param.append('type', _this.ruleForm.type);//
              this.param.append('url', _this.ruleForm.url);//
              this.uploadFileRequest("/img/insert", this.param).then(resp => {
                _this.tableLoading = false;
                if (resp && resp.status == 200) {
                  var data = resp.data;

                  _this.dialogVisible = false;
                  _this.emptyEmpData();
                  _this.loadData(this.criteria,this.currentPage,this.pagesize);
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
        this.ruleForm = row;
        this.dialogVisible = true;
      },
      showAddEmpView() {
        this.dialogTitle = "添加";
        this.dialogVisible = true;
        this.emptyEmpData();
      },
      emptyEmpData() {
        this.imgurl = '';//家具url
        this.param = new FormData();//表单上传的方法
        this.productPicPathList = [];//家具预览集合
        this.ruleForm = {
          id:'',//主键
          name:'', //名称
          picture:'',//家具
          type:'', //类型
          price:'',
          jprice:'',
          num:'',
          kc:'',
          remark:'',
          url:'http://localhost:8089/'
        }
      }
    },

    data() {
      return {
        dialogImageUrl: '',
        dialogVisible: false,
        fileNameList:[],
        zsList:[],// 上传家具文件描述
        productPicPathList: [],//家具预览集合
        photoList: [],//照片预览集合
        dialogContImgVisible: false,//家具预览是否展示
        dialogContImgUrl:'',//预览家具地址
        disabled: false,
        form:{
          name:'',
          type:''
        },
        //搜索条件
        criteria: '',
        //默认每页数据量
        pagesize: 10,
        tableData: [],
        picture:'',
        name: '',
        subItem:'',
        fileUploadBtnText: '导入数据',
        beginDateScope: '',
        dialogTitle: '',
        multipleSelection: [],
        totalCount: -1,//默认数据总数
        currentPage: 1, //当前页码
        dialogVisible: false,
        tableLoading: false,
        imgType:[],//家具类型
        imgurl:'',//家具url
        param: new FormData(),//表单上传的方法
        //详情修改添加
        ruleForm:{
          id:'',//主键
          name:'', //名称
          price:'',
          jprice:'',
          picture:'',//家具
          type:'', //类型
          num:'',
          kc:'',
          remark:'',
          url:'http://localhost:8089/'
        },
        rules: {
          name: [{required: true, message: '必填', trigger: 'blur'}],
          type: [{required: true, message: '必填', trigger: 'blur'}],
          price: [{required: true, message: '必填', trigger: 'blur'}],
          jprice: [{required: true, message: '必填', trigger: 'blur'}],
          remark: [{required: true, message: '必填', trigger: 'blur'}],
          num: [{required: true, message: '必填', trigger: 'blur'}],
          kc: [{required: true, message: '必填', trigger: 'blur'}]

        }
      };
    },
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
