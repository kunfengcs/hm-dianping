<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="主键" prop="id">
        <el-input
          v-model="queryParams.id"
          placeholder="请输入主键"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="商户id" prop="shopId">
        <el-input
          v-model="queryParams.shopId"
          placeholder="请输入商户id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户id" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入用户id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="标题" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入标题"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="点赞数量" prop="liked">
        <el-input
          v-model="queryParams.liked"
          placeholder="请输入点赞数量"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="评论数量" prop="comments">
        <el-input
          v-model="queryParams.comments"
          placeholder="请输入评论数量"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['dianping_admin:blog:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['dianping_admin:blog:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['dianping_admin:blog:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['dianping_admin:blog:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="blogList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
<!--      <el-table-column label="主键" align="center" prop="id" />-->
<!--      <el-table-column label="商户id" align="center" prop="shopId" />-->
<!--      <el-table-column label="用户id" align="center" prop="userId" />-->
      <el-table-column label="商户" align="center" prop="shopName" />
      <el-table-column label="用户" align="center" prop="userName" />
      <el-table-column label="标题" align="center" prop="title" />
<!--      <el-table-column label="探店的照片，最多9张，多张以隔开" align="center" prop="images" width="100">
        <template slot-scope="scope">
          <image-preview :src="scope.row.images" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="探店的文字描述" align="center" prop="content" />-->
      <el-table-column label="探店的文字描述" align="center">
        <template slot-scope="scope">
          <!-- 使用计算属性或方法决定是否需要显示省略号 -->
          <div
            :class="{ 'text-ellipsis': scope.row.content.length > 20 }"
            :title="scope.row.content"
          >
            {{ scope.row.content.length > 20 ? scope.row.content.slice(0, 20) + '...' : scope.row.content }}
          </div>
        </template>
      </el-table-column>
      <el-table-column label="探店的照片" align="center" prop="images" width="100">
      <template slot-scope="scope">
        <image-preview :src="'http://localhost:8880/' + getImageBeforeComma(scope.row.images)" :width="50" :height="50"/>
      </template>
      </el-table-column>
      <el-table-column label="点赞数量" align="center" prop="liked" />
      <el-table-column label="评论数量" align="center" prop="comments" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['dianping_admin:blog:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['dianping_admin:blog:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改用户博客对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="商户id" prop="shopId">
          <el-input v-model="form.shopId" placeholder="请输入商户id" />
        </el-form-item>
        <el-form-item label="用户id" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户id" />
        </el-form-item>
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入标题" />
        </el-form-item>
        <el-form-item label="探店的文字描述">
          <editor v-model="form.content" :min-height="192"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listBlog, getBlog, delBlog, addBlog, updateBlog } from "@/api/dianping_admin/blog";
import {getUser} from "@/api/dianping_admin/user";
import {getShop} from "@/api/dianping_admin/shop";

export default {
  name: "Blog",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 用户博客表格数据
      blogList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        id: null,
        shopId: null,
        userId: null,
        title: null,
        content: null,
        liked: null,
        comments: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
    this.getallName();
  },
  methods: {
    /** 查询用户博客列表 */
    // getList() {
    //   this.loading = true;
    //   listBlog(this.queryParams).then(response => {
    //     this.blogList = response.rows;
    //     this.total = response.total;
    //     this.loading = false;
    //   });
    // },
    async getList() {
      this.loading = true;
      try {
        const response = await listBlog(this.queryParams);
        this.blogList = response.rows;

        // 定义一个函数来获取名称，无论是用户还是商户
        const getNameById = async (id, type) => {
          let name;
          if (type === 'user') {
            const userData = await getUser(id);
            name = userData.data.nickName;
          } else if (type === 'shop') {
            const shopData = await getShop(id);
            name = shopData.data.name; // 假设shopData的结构是 { name: '商户名称' }
          }
          return name;
        };

        // 并行获取所有需要的名称
        for (let i = 0; i < this.blogList.length; i++) {
          const blog = this.blogList[i];
          blog.userName = await getNameById(blog.userId, 'user');
          blog.shopName = await getNameById(blog.shopId, 'shop');
        }

        this.total = response.total;
        this.loading = false;
      } catch (error) {
        console.error('Error fetching blog list or names', error);
        this.loading = false;
      }
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        shopId: null,
        userId: null,
        title: null,
        images: null,
        content: null,
        liked: null,
        comments: null,
        createTime: null,
        updateTime: null,
        isRead: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加用户博客";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getBlog(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改用户博客";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateBlog(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addBlog(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除用户博客编号为"' + ids + '"的数据项？').then(function() {
        return delBlog(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('dianping_admin/blog/export', {
        ...this.queryParams
      }, `blog_${new Date().getTime()}.xlsx`)
    },
    // 方法实现，如果只需要读取不变更，也可以用computed
    getImageBeforeComma(images) {
      // 使用split方法截取字符串，直到第一个逗号前的部分
      return images ? images.split(',')[0] : '';
      // 注意检查images是否定义，避免undefined.split导致的错误
    }

  },
};
</script>
