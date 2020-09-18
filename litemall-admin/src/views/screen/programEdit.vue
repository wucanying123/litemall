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
          <el-table-column align="center" label="素材时长" prop="maxPlayTime">
            <template slot-scope="scope">{{ scope.row.maxPlayTime | secondToDate }}</template>
          </el-table-column>
          <el-table-column align="center" label="类型" prop="_type">
            <template slot-scope="scope">{{ scope.row._type | formatType }}</template>
          </el-table-column>
          <el-table-column align="center" label="格式" prop="fileExt" />
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
          <el-table-column align="center" label="素材时长" prop="maxPlayTime">
            <template slot-scope="scope">{{ scope.row.maxPlayTime | secondToDate }}</template>
          </el-table-column>

          <el-table-column align="center" label="类型" prop="_type">
            <template slot-scope="scope">{{ scope.row._type | formatType }}</template>
          </el-table-column>
          <el-table-column align="center" label="格式" prop="fileExt" />
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
        <el-button type="primary" @click="confirmAdd">确定添加</el-button>
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
import { listSource } from '@/api/source'
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination
import { getToken } from '@/utils/auth'
import { readProgram, updateSimpleProgramById } from '@/api/program'

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
  name: 'ProgramEdit',
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
      id: 0,
      program: {
        playSource: []
      },
      playSourceList: [],
      addVisiable: false,
      list: [],
      total: 0,
      listLoading: false,
      listQuery: {
        page: 1,
        limit: 5,
        name: undefined
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
          this.program.playSource = response.data.data.program.playSource
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
      listSource(this.listQuery).then(response => {
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
        name: undefined
      }
      this.list = []
      this.total = 0
      this.selectedlist = []
      this.addVisiable = true
      this.getList()
    },
    confirmAdd() {
      const newPlaySourceIds = []
      const newPlaySourceList = []
      this.selectedlist.forEach(item => {
        const sourceId = item.sourceId
        let found = false
        if (this.program != null) {
          if (this.program.playSource != null) {
            this.program.playSource.forEach(playSourceId => {
              if (sourceId === playSourceId) {
                found = true
              }
            })
          }
        }
        if (!found) {
          newPlaySourceIds.push(sourceId)
          newPlaySourceList.push(item)
        }
      })

      if (newPlaySourceIds.length > 0) {
        if (this.program.playSource != null) {
          this.program.playSource = this.program.playSource.concat(newPlaySourceIds)
          this.playSourceList = this.playSourceList.concat(newPlaySourceList)
        } else {
          this.program.playSource = newPlaySourceIds
          this.playSourceList = newPlaySourceList
        }
      }
      this.addVisiable = false
    },
    handleDelete(row) {
      for (var index = 0; index < this.program.playSource.length; index++) {
        console.log(this.program.playSource)
        if (row.sourceId === this.program.playSource[index]) {
          this.program.playSource.splice(index, 1)
        }
      }
      for (var index2 = 0; index2 < this.playSourceList.length; index2++) {
        if (row.sourceId === this.playSourceList[index2].sourceId) {
          this.playSourceList.splice(index2, 1)
        }
      }
    },
    handleCancel() {
      this.$router.push({ path: '/screen/program' })
    },
    handleConfirm() {
      this.$refs['program'].validate(valid => {
        if (valid) {
          updateSimpleProgramById(this.program).then(response => {
            this.$router.push({ path: '/screen/program' })
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
