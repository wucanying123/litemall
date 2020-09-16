<template>
  <div class="app-container">

    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <el-input v-model="listQuery.name" clearable class="filter-item" style="width: 200px;" placeholder="请输入媒体名称" />
      <el-select v-model="listQuery._type" clearable style="width: 200px" class="filter-item" placeholder="请选择类型">
        <el-option v-for="_type in typeOptions" :key="_type.value" :label="_type.label" :value="_type.value" />
      </el-select>
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">查找</el-button>
      <el-button class="filter-item" type="primary" icon="el-icon-edit" @click="handleCreate">添加到审核</el-button>
    </div>

    <!-- 查询结果 -->
    <el-table v-loading="listLoading" :data="list" element-loading-text="正在查询中。。。" border fit highlight-current-row>

      <el-table-column align="center" label="名称" prop="name" />

      <el-table-column align="center" label="内容" prop="url">
        <template slot-scope="scope">
          <div v-if="scope.row._type === 'Video'">
            <video :src="scope.row.url" controls="controls" width="200" height="90" />
          </div>
          <div v-if="scope.row._type === 'Image'">
            <img v-if="scope.row.url" :src="scope.row.url" width="100" height="90">
          </div>
        </template>
      </el-table-column>

      <!--      <el-table-column align="center" label="播放时长" prop="playTime">-->
      <!--        <template slot-scope="scope">{{ scope.row.playTime | secondToDate }}</template>-->
      <!--      </el-table-column>-->

      <el-table-column align="center" label="素材时长" prop="maxPlayTime">
        <template slot-scope="scope">{{ scope.row.maxPlayTime | secondToDate }}</template>
      </el-table-column>

      <el-table-column align="center" label="类型" prop="_type">
        <template slot-scope="scope">{{ scope.row._type | formatType }}</template>
      </el-table-column>
      <el-table-column align="center" label="格式" prop="fileExt" />
      <el-table-column align="center" label="修改时间" prop="updateTime">
        <template slot-scope="scope">{{ scope.row.updateTime | timestampToTime }}</template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="200" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <!--          <el-button v-permission="['POST /admin/screen/source/updateSourceById']" type="primary" size="mini" @click="handleUpdate(scope.row)">编辑</el-button>-->
          <el-button v-permission="['POST /admin/screen/source/deleteById']" type="danger" size="mini" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />

    <!-- 添加或修改对话框 -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="dataForm" status-icon label-position="left" label-width="100px" style="width: 400px; margin-left:60px;">
        <el-form-item label="名称" prop="name">
          <el-input v-model="dataForm.name" />
        </el-form-item>
        <el-form-item label="类型" prop="_type">
          <el-select v-model="dataForm._type" placeholder="请选择">
            <el-option v-for="_type in typeOptions" :key="_type.value" :label="_type.label" :value="_type.value" />
          </el-select>
        </el-form-item>
        <div v-if="dataForm._type === 'Video' || dataForm._type === 'Image'">
          <div v-if="dataForm._type === 'Video'">
            <el-form-item label="内容" prop="url">
              <el-upload
                :headers="headers"
                :action="uploadPath"
                :show-file-list="false"
                :on-success="uploadUrl"
                :before-upload="checkFileSize"
                class="avatar-uploader"
                accept=".mp4"
              >
                <div v-if="dataForm.url && dataForm._type === 'Video'">
                  <video :src="dataForm.url" controls="controls" width="200" />
                </div>
                <div v-if="dataForm.url && dataForm._type === 'Image'">
                  <img v-if="dataForm.url" :src="dataForm.url" class="avatar">
                </div>

                <i v-else class="el-icon-plus avatar-uploader-icon" />
                <div slot="tip" class="el-upload__tip">只能上传mp4文件，且不超过500M</div>
              </el-upload>
            </el-form-item>
          </div>
          <div v-if="dataForm._type === 'Image'">
            <el-form-item label="内容" prop="url">
              <el-upload
                :headers="headers"
                :action="uploadPath"
                :show-file-list="false"
                :on-success="uploadUrl"
                :before-upload="checkFileSize"
                class="avatar-uploader"
                accept=".jpg,.jpeg,.png,.gif"
              >
                <div v-if="dataForm.url && dataForm._type === 'Video'">
                  <video :src="dataForm.url" controls="controls" width="200" />
                </div>
                <div v-if="dataForm.url && dataForm._type === 'Image'">
                  <img v-if="dataForm.url" :src="dataForm.url" class="avatar">
                </div>

                <i v-else class="el-icon-plus avatar-uploader-icon" />
                <div slot="tip" class="el-upload__tip">只能上传jpg/jpeg/png/gif文件，且不超过500M</div>
              </el-upload>
            </el-form-item>
          </div>
        </div>
        <!--        <el-form-item label="播放时长(秒)" prop="playTime">-->
        <!--          <el-input-->
        <!--            v-model="dataForm.playTime"-->
        <!--            type="number"-->
        <!--            min="0"-->
        <!--            max="60"-->
        <!--            step="1"-->
        <!--            size="2"-->
        <!--            on-keypress="return (/[\d]/.test(String.fromCharCode(event.keyCode)))"-->
        <!--          />-->
        <!--        </el-form-item>-->
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
import { listSource, createSource, updateSource, deleteSource } from '@/api/source'
import { uploadPath } from '@/api/storage'
import { getToken } from '@/utils/auth'
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination

const defaultTypeOptions = [
  {
    label: '',
    value: ''
  },
  {
    label: '视频',
    value: 'Video'
  },
  {
    label: '图片',
    value: 'Image'
  }
  // ,
  // {
  //   label: '时钟',
  //   value: 'AnalogClock'
  // },
  // {
  //   label: '数字时钟',
  //   value: 'DigitalClock'
  // },
  // {
  //   label: '倒计时',
  //   value: 'Countdown'
  // },
  // {
  //   label: 'Flash',
  //   value: 'Flash'
  // },
  // {
  //   label: '天气预报',
  //   value: 'Weather'
  // },
  // {
  //   label: '多行文本',
  //   value: 'MultiText'
  // }
]

export default {
  name: 'Source',
  components: { Pagination },
  filters: {
    formatType(_type) {
      for (let i = 0; i < defaultTypeOptions.length; i++) {
        if (_type === defaultTypeOptions[i].value) {
          return defaultTypeOptions[i].label
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
      uploadPath,
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        name: undefined,
        _type: undefined
      },
      dataForm: {
        id: undefined,
        name: undefined,
        _type: undefined,
        url: undefined
        // playTime: undefined
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '创建'
      },
      rules: {
        name: [
          { required: true, message: '名称不能为空', trigger: 'blur' }
        ],
        _type: [
          { required: true, message: '类型不能为空', trigger: 'blur' }
        ]
        // ,
        // playTime: [
        //   { required: true, message: '播放时长不能为空', trigger: 'blur' }
        // ]
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
      listSource(this.listQuery)
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
        name: undefined,
        _type: '',
        url: undefined
        // playTime: undefined
      }
    },
    handleCreate() {
      this.resetForm()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    uploadUrl: function(response) {
      this.dataForm.url = response.data.url
    },
    checkFileSize: function(file) {
      if (file.size > 524288000) {
        this.$message.error(`${file.name}文件大于500M，请选择小于500M的文件`)
        return false
      }
      return true
    },
    createData() {
      this.$refs['dataForm'].validate(valid => {
        if (valid) {
          createSource(this.dataForm)
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
          updateSource(this.dataForm)
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
      deleteSource(row.id)
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
