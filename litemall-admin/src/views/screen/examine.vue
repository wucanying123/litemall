<template>
  <div class="app-container">

    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <el-input v-model="listQuery.detailName" clearable class="filter-item" style="width: 200px;" placeholder="请输入名称" />
      <el-select v-model="listQuery.type" clearable style="width: 200px" class="filter-item" placeholder="请选择类型">
        <el-option v-for="type in typeOptions" :key="type.value" :label="type.label" :value="type.value" />
      </el-select>
      <el-select v-model="listQuery.passStatus" clearable style="width: 200px" class="filter-item" placeholder="请选择状态">
        <el-option v-for="passStatus in passStatusOptions" :key="passStatus.value" :label="passStatus.label" :value="passStatus.value" />
      </el-select>
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">查找</el-button>
    </div>

    <!-- 查询结果 -->
    <el-table v-loading="listLoading" :data="list" element-loading-text="正在查询中。。。" border fit highlight-current-row>

      <el-table-column align="center" label="名称" prop="detailName" />
      <el-table-column align="center" label="类型" prop="type">
        <template slot-scope="scope">{{ scope.row.type | formatType }}</template>
      </el-table-column>
      <el-table-column align="center" label="审核状态" prop="passStatus">
        <template slot-scope="scope">{{ scope.row.passStatus | formatPassStatus }}</template>
      </el-table-column>
      <el-table-column align="center" label="提交者" prop="addUserName" />
      <el-table-column align="center" label="一审员" prop="checkUserName1" />
      <el-table-column align="center" label="二审员" prop="checkUserName2" />
      <el-table-column align="center" label="修改时间" prop="updateTime">
        <template slot-scope="scope">{{ scope.row.updateTime | timestampToTime }}</template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="200" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <div v-if="scope.row.passStatus !== 4">
            <el-button v-permission="['POST /admin/screen/examine/updateExamineById']" type="primary" size="mini" @click="handleUpdate(scope.row)">编辑</el-button>
          </div>
          <!--          <el-button v-permission="['POST /admin/screen/examine/deleteById']" type="danger" size="mini" @click="handleDelete(scope.row)">删除</el-button>-->
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />

    <!-- 添加或修改对话框 -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="dataForm" status-icon label-position="left" label-width="100px" style="width: 400px; margin-left:60px;">
        <el-form-item label="名称" prop="detailName">
          <el-input v-model="dataForm.detailName" />
        </el-form-item>

        <div v-if="dataForm.passStatus === 1 || dataForm.passStatus === 3">
          <el-form-item label="一审审核" prop="passStatus1">
            <el-select v-model="dataForm.passStatus1" placeholder="请选择">
              <el-option :value="1" label="未审核" />
              <el-option :value="2" label="通过" />
              <el-option :value="3" label="不通过" />
            </el-select>
          </el-form-item>
          <div v-if="dataForm.passStatus1 === 3">
            <el-form-item label="拒绝原因" prop="rejectReason1">
              <el-input v-model="dataForm.rejectReason1" :rows="8" type="textarea" placeholder="请输入内容" />
            </el-form-item>
          </div>
        </div>
        <div v-if="dataForm.passStatus === 2 || dataForm.passStatus === 5">
          <el-form-item label="二审审核" prop="passStatus2">
            <el-select v-model="dataForm.passStatus2" placeholder="请选择">
              <el-option :value="2" label="未审核" />
              <el-option :value="4" label="通过" />
              <el-option :value="5" label="不通过" />
            </el-select>
          </el-form-item>
          <div v-if="dataForm.passStatus2 === 5">
            <el-form-item label="拒绝原因" prop="rejectReason2">
              <el-input v-model="dataForm.rejectReason2" :rows="8" type="textarea" placeholder="请输入内容" />
            </el-form-item>
          </div>
        </div>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="createData">确定</el-button>
        <el-button v-else type="primary" @click="updateData">确定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<style>
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
import { listExamine, createExamine, updateExamine, deleteExamine } from '@/api/examine'
import { uploadPath } from '@/api/storage'
import { getToken } from '@/utils/auth'
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination

const defaultTypeOptions = [
  {
    label: '',
    value: ''
  },
  {
    label: '节目',
    value: 1
  },
  {
    label: '直播',
    value: 2
  },
  {
    label: '资源',
    value: 3
  }
]
const defaultPassStatusOptions = [
  {
    label: '',
    value: ''
  },
  {
    label: '未审核',
    value: 1
  },
  {
    label: '一审过待二审',
    value: 2
  },
  {
    label: '一审不过',
    value: 3
  }, {
    label: '二审过',
    value: 4
  },
  {
    label: '二审不过',
    value: 5
  }
]

export default {
  name: 'Examine',
  components: { Pagination },
  filters: {
    formatType(type) {
      for (let i = 0; i < defaultTypeOptions.length; i++) {
        if (type === defaultTypeOptions[i].value) {
          return defaultTypeOptions[i].label
        }
      }
      return ''
    },
    formatPassStatus(type) {
      for (let i = 0; i < defaultPassStatusOptions.length; i++) {
        if (type === defaultPassStatusOptions[i].value) {
          return defaultPassStatusOptions[i].label
        }
      }
      return ''
    },
    secondToDate(msd) {
      var time = msd
      // eslint-disable-next-line eqeqeq
      if (time != null && time != '') {
        if (time > 60 && time < 60 * 60) {
          time = parseInt(time / 60.0) + '分钟' + parseInt((parseFloat(time / 60.0) -
            parseInt(time / 60.0)) * 60) + '秒'
        } else if (time >= 60 * 60 && time < 60 * 60 * 24) {
          time = parseInt(time / 3600.0) + '小时' + parseInt((parseFloat(time / 3600.0) -
            parseInt(time / 3600.0)) * 60) + '分钟' +
            parseInt((parseFloat((parseFloat(time / 3600.0) - parseInt(time / 3600.0)) * 60) -
              parseInt((parseFloat(time / 3600.0) - parseInt(time / 3600.0)) * 60)) * 60) + '秒'
        } else if (time >= 60 * 60 * 24) {
          time = parseInt(time / 3600.0 / 24) + '天' + parseInt((parseFloat(time / 3600.0 / 24) -
            parseInt(time / 3600.0 / 24)) * 24) + '小时' + parseInt((parseFloat(time / 3600.0) -
            parseInt(time / 3600.0)) * 60) + '分钟' +
            parseInt((parseFloat((parseFloat(time / 3600.0) - parseInt(time / 3600.0)) * 60) -
              parseInt((parseFloat(time / 3600.0) - parseInt(time / 3600.0)) * 60)) * 60) + '秒'
        } else {
          time = parseInt(time) + '秒'
        }
      }
      return time
    },
    timestampToTime(timestamp) {
      if (timestamp != null) {
        var date = new Date(timestamp * 1000)// 时间戳为10位需*1000，时间戳为13位的话不需乘1000
        const y = date.getFullYear()
        let MM = date.getMonth() + 1
        MM = MM < 10 ? ('0' + MM) : MM
        let d = date.getDate()
        d = d < 10 ? ('0' + d) : d
        let h = date.getHours()
        h = h < 10 ? ('0' + h) : h
        let m = date.getMinutes()
        m = m < 10 ? ('0' + m) : m
        let s = date.getSeconds()
        s = s < 10 ? ('0' + s) : s
        return y + '-' + MM + '-' + d + ' ' + h + ':' + m + ':' + s
      }
    }
  },
  data() {
    return {
      typeOptions: Object.assign({}, defaultTypeOptions),
      passStatusOptions: Object.assign({}, defaultPassStatusOptions),
      uploadPath,
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        detailName: undefined,
        type: undefined,
        passStatus: undefined
      },
      dataForm: {
        id: undefined,
        detailName: undefined,
        type: undefined,
        url: undefined,
        passStatus: undefined,
        passStatus1: undefined,
        passStatus2: undefined
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '创建'
      },
      rules: {
        detailName: [
          { required: true, message: '名称不能为空', trigger: 'blur' }
        ],
        type: [
          { required: true, message: '类型不能为空', trigger: 'blur' }
        ]
      },
      downloadLoading: false
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
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      listExamine(this.listQuery)
        .then(response => {
          this.list = response.data.data.list
          this.total = response.data.data.total
          this.listLoading = false
        })
        .catch(() => {
          this.list = []
          this.total = 0
          this.listLoading = false
        })
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    resetForm() {
      this.dataForm = {
        id: undefined,
        detailName: undefined,
        type: '',
        url: undefined,
        passStatus: undefined,
        passStatus1: undefined,
        passStatus2: undefined
      }
    },
    createData() {
      this.$refs['dataForm'].validate(valid => {
        if (valid) {
          createExamine(this.dataForm)
            .then(response => {
              this.list.unshift(response.data.data)
              this.dialogFormVisible = false
              this.$notify.success({
                title: '成功',
                message: '创建成功'
              })
              this.getList()
            })
            .catch(response => {
              this.$notify.error({
                title: '失败',
                message: response.data.errmsg
              })
            })
        }
      })
    },
    handleDetail(row) {
      this.userDetail = row
      this.userDialogVisible = true
    },
    handleUpdate(row) {
      this.dataForm = Object.assign({}, row)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    updateData() {
      this.$refs['dataForm'].validate(valid => {
        if (valid) {
          updateExamine(this.dataForm)
            .then(() => {
              for (const v of this.list) {
                if (v.id === this.dataForm.id) {
                  const index = this.list.indexOf(v)
                  this.list.splice(index, 1, this.dataForm)
                  break
                }
              }
              this.dialogFormVisible = false
              this.$notify.success({
                title: '成功',
                message: '更新成功'
              })
              this.getList()
            })
            .catch(response => {
              this.$notify.error({
                title: '失败',
                message: response.data.errmsg
              })
            })
        }
      })
    },
    handleDelete(row) {
      deleteExamine(row.id)
        .then(response => {
          this.$notify.success({
            title: '成功',
            message: '删除成功'
          })
          this.getList()
        })
        .catch(response => {
          this.$notify.error({
            title: '失败',
            message: response.data.errmsg
          })
        })
    }
  }
}
</script>
