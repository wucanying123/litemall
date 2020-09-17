<template>
  <div class="app-container">

    <el-form
      ref="program"
      :rules="rules"
      :model="program"
      status-icon
      label-position="left"
      label-width="100px"
      style="width: 800px; margin-left:50px;"
    >
      <el-form-item label="节目名称" prop="name">
        <el-input v-model="program.name" />
      </el-form-item>
      <el-form-item label="节目宽" prop="width">
        <el-input v-model="program.name" />
      </el-form-item>
      <el-form-item label="节目宽" prop="height">
        <el-input v-model="program.height" />
      </el-form-item>
      <el-form-item label="媒体资源" prop="playSource">
        <el-button style="float:right;" size="mini" type="primary" @click="handleCreate()">创建资源</el-button>
        <!-- 查询结果 -->
        <el-table :data="playSourceList" border fit highlight-current-row>

          <el-table-column align="center" label="资源ID" prop="id" />
          <el-table-column align="center" property="picUrl" label="图片">
            <template slot-scope="scope">
              <img :src="scope.row.picUrl" width="60">
            </template>
          </el-table-column>
          <el-table-column align="center" label="资源名称" prop="name" />
          <el-table-column align="center" label="资源介绍" prop="brief" />
          <el-table-column align="center" label="操作" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button type="danger" size="mini" @click="handleDelete(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-form-item>

    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleCancel">取消</el-button>
      <el-button type="primary" @click="handleConfirm">确定</el-button>
    </div>

    <el-dialog :visible.sync="addVisiable" title="添加资源">
      <div class="search">
        <el-input v-model="listQuery.name" clearable class="filter-item" style="width: 200px;" placeholder="请输入资源名称" />
        <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">查找</el-button>
        <el-table
          v-loading="listLoading"
          :data="list"
          element-loading-text="正在查询中。。。"
          border
          fit
          highlight-current-row
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="55" />
          <el-table-column align="center" label="资源ID" prop="id" />
          <el-table-column align="center" property="picUrl" label="图片">
            <template slot-scope="scope">
              <img :src="scope.row.picUrl" width="40">
            </template>
          </el-table-column>
          <el-table-column align="center" label="资源名称" prop="name" />
        </el-table>
        <pagination
          v-show="total>0"
          :total="total"
          :page.sync="listQuery.page"
          :limit.sync="listQuery.limit"
          @pagination="getList"
        />

      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="addVisiable = false">取消</el-button>
        <el-button type="primary" @click="confirmAdd">确定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<style>
.el-dialog {
  width: 800px;
}

.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar-uploader .el-upload:hover {
  border-color: #20a0ff;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  line-height: 120px;
  text-align: center;
}

.avatar {
  width: 145px;
  height: 145px;
  display: block;
}
</style>

<script>
import { listPlaySource } from '@/api/playSource'
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination
import { getToken } from '@/utils/auth'
import { readProgram, updateSimpleProgramById } from '@/api/program'

export default {
  name: 'ProgramEdit',
  components: { Pagination },
  data() {
    return {
      id: 0,
      program: {},
      playSourceList: [],
      addVisiable: false,
      list: [],
      total: 0,
      listLoading: false,
      listQuery: {
        page: 1,
        limit: 5,
        id: undefined,
        name: undefined,
        sort: 'add_time',
        order: 'desc'
      },
      selectedlist: [],
      rules: {
        title: [
          { required: true, message: '节目名称不能为空', trigger: 'blur' }
        ],
        width: [
          { required: true, message: '节目窗不能为空', trigger: 'blur' }
        ],
        height: [
          { required: true, message: '节目高不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    headers() {
      return {
        'X-Litemall-Admin-Token': getToken()
      }
    }
  },
  created() {
    if (this.$route.query.id == null) {
      return
    }

    this.id = this.$route.query.id
    this.getProgram()
  },
  methods: {
    getProgram() {
      this.listLoading = true
      readProgram({ id: this.id })
        .then(response => {
          this.program = response.data.data.program
          this.playSourceList = response.data.data.playSourceList
          this.listLoading = false
        })
        .catch(() => {
          this.program = {}
          this.playSourceList = []
          this.listLoading = false
        })
    },
    getList() {
      this.listLoading = true
      listPlaySource(this.listQuery).then(response => {
        this.list = response.data.data.list
        this.total = response.data.data.total
        this.listLoading = false
      }).catch(() => {
        this.list = []
        this.total = 0
        this.listLoading = false
      })
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleSelectionChange(val) {
      this.selectedlist = val
    },
    uploadPicUrl: function(response) {
      this.program.picUrl = response.data.url
    },
    handleCreate() {
      this.listQuery = {
        page: 1,
        limit: 5,
        id: undefined,
        name: undefined,
        sort: 'add_time',
        order: 'desc'
      }
      this.list = []
      this.total = 0
      this.selectedlist = []
      this.addVisiable = true
    },
    confirmAdd() {
      const newPlaySourceIds = []
      const newPlaySourceList = []
      this.selectedlist.forEach(item => {
        const id = item.id
        let found = false
        this.program.playSource.forEach(playSourceId => {
          if (id === playSourceId) {
            found = true
          }
        })
        if (!found) {
          newPlaySourceIds.push(id)
          newPlaySourceList.push(item)
        }
      })

      if (newPlaySourceIds.length > 0) {
        this.program.playSource = this.program.playSource.concat(newPlaySourceIds)
        this.playSourceList = this.playSourceList.concat(newPlaySourceList)
      }
      this.addVisiable = false
    },
    handleDelete(row) {
      for (var index = 0; index < this.program.playSource.length; index++) {
        if (row.id === this.program.playSource[index]) {
          this.program.playSource.splice(index, 1)
        }
      }
      for (var index2 = 0; index2 < this.playSourceList.length; index2++) {
        if (row.id === this.playSourceList[index2].id) {
          this.playSourceList.splice(index2, 1)
        }
      }
    },
    handleCancel() {
      this.$router.push({ path: '/promotion/program' })
    },
    handleConfirm() {
      this.$refs['program'].validate(valid => {
        if (valid) {
          updateSimpleProgramById(this.program).then(response => {
            this.$router.push({ path: '/promotion/program' })
          })
            .catch(response => {
              this.$notify.error({
                title: '失败',
                message: response.data.errmsg
              })
            })
        }
      })
    }
  }
}
</script>
