<template>
  <div class="app-container">

    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <el-button class="filter-item" type="primary" icon="el-icon-edit" @click="handleCreate">添加</el-button>
    </div>

    <!-- 查询结果 -->
    <el-table v-loading="listLoading" :data="list" element-loading-text="正在查询中。。。" border fit highlight-current-row>

      <el-table-column align="center" label="定时日期范围" prop="dateType">
        <template slot-scope="scope">{{ scope.row.dateType == "Range" ? "指定日期" : "永久" }}</template>
      </el-table-column>
      <el-table-column align="center" label="定时开始日期" prop="startDate" />
      <el-table-column align="center" label="定时结束日期" prop="endDate" />
      <el-table-column align="center" label="定时日期范围" prop="timeType">
        <template slot-scope="scope">{{ scope.row.timeType == "Range" ? "指定时间" : "全天" }}</template>
      </el-table-column>
      <el-table-column align="center" label="定时开始时间" prop="startTime" />
      <el-table-column align="center" label="定时结束时间" prop="endTime" />
      <el-table-column align="center" label="过滤类型" prop="filterType">
        <template slot-scope="scope">{{ scope.row.filterType | formatFilterType }}</template>
      </el-table-column>
      <el-table-column align="center" label="过滤星期几" prop="weekFilterArray">
        <template slot-scope="scope">
          <el-tag v-for="weekId in scope.row.weekFilterArray" :key="weekId" type="primary" style="margin-right: 20px;"> {{ formatWeek(weekId) }} </el-tag>
        </template>
      </el-table-column>

      <el-table-column align="center" label="修改时间" prop="updateTime">
        <template slot-scope="scope">{{ scope.row.updateTime | timestampToTime }}</template>
      </el-table-column>

      <el-table-column align="center" label="操作" width="330" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" icon="el-icon-s-promotion" @click="handlePlay(scope.row)">播放</el-button>
          <el-button type="primary" size="mini" @click="handleUpdate(scope.row)">编辑</el-button>
          <el-button type="danger" size="mini" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />

    <!-- 添加或修改对话框 -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="dataForm" status-icon label-position="left" label-width="100px" style="width: 400px; margin-left:60px;">
        <el-form-item label="定时日期范围" prop="dateType">
          <el-select v-model="dataForm.dateType" placeholder="请选择">
            <el-option :value="'All'" label="永久" />
            <el-option :value="'Range'" label="指定日期" />
          </el-select>
        </el-form-item>
        <div v-if="dataForm.dateType === 'Range'">
          <el-form-item label="定时开始日期" prop="startDate">
            <el-date-picker
              v-model="dataForm.startDate"
              type="date"
              placeholder="选择开始日期"
              style="width: 60%;"
              value-format="yyyy-MM-dd"
              format="yyyy-MM-dd"
            />
          </el-form-item>
          <el-form-item label="定时结束日期" prop="endDate">
            <el-date-picker
              v-model="dataForm.endDate"
              type="date"
              placeholder="选择结束日期"
              style="width: 60%;"
              value-format="yyyy-MM-dd"
              format="yyyy-MM-dd"
              :picker-options="endDatePicker"
            />
          </el-form-item>
        </div>
        <el-form-item label="定时时间范围" prop="timeType">
          <el-select v-model="dataForm.timeType" placeholder="请选择">
            <el-option :value="'All'" label="全天" />
            <el-option :value="'Range'" label="指定时间" />
          </el-select>
        </el-form-item>
        <div v-if="dataForm.timeType === 'Range'">
          <el-form-item label="定时开始时间" prop="startTime">
            <el-time-picker
              v-model="dataForm.startTime"
              class="date-box"
              format="HH:mm"
              value-format="HH:mm"
              :picker-options="{
                selectableRange:`00:00:00 -${dataForm.endTime ? dataForm.endTime+':00' : '23:59:00'}`
              }"
            />
          </el-form-item>
          <el-form-item label="定时结束时间" prop="endTime">
            <el-time-picker
              v-model="dataForm.endTime"
              format="HH:mm"
              value-format="HH:mm"
              :picker-options="{
                selectableRange: `${dataForm.startTime ? dataForm.startTime+':00' : '00:00:00'}-23:59:00`
              }"
            />
          </el-form-item>
        </div>
        <el-form-item label="过滤类型" prop="filterType">
          <el-select v-model="dataForm.filterType" placeholder="请选择">
            <el-option :value="'None'" label="无指定" />
            <el-option :value="'Week'" label="指定星期" />
          </el-select>
        </el-form-item>
        <div v-if="dataForm.filterType === 'Week'">
          <el-form-item label="过滤星期几" prop="weekFilterArray">
            <el-select v-model="dataForm.weekFilterArray" multiple placeholder="请选择">
              <el-option
                v-for="item in weekOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
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
import { listSchedule, createSchedule, updateSchedule, deleteSchedule, playSchedule } from '@/api/schedule'
import { uploadPath } from '@/api/storage'
import { getToken } from '@/utils/auth'
import Pagination from '@/components/Pagination'

const defaultWeekOptions = [
  {
    label: '',
    value: ''
  },
  {
    label: '星期一',
    value: '1'
  },
  {
    label: '星期二',
    value: '2'
  },
  {
    label: '星期三',
    value: '3'
  },
  {
    label: '星期四',
    value: '4'
  },
  {
    label: '星期五',
    value: '5'
  },
  {
    label: '星期六',
    value: '6'
  },
  {
    label: '星期日',
    value: '0'
  }
]

const defaultFilterTypeOptions = [
  {
    label: '',
    value: ''
  },
  {
    label: '无指定',
    value: 'None'
  },
  {
    label: '指定星期',
    value: 'Week'
  },
  {
    label: '指定月份',
    value: 'Month'
  }
]
export default {
  name: 'Schedule',
  components: { Pagination },
  filters: {
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
    },
    formatFilterType(timeType) {
      for (let i = 0; i < defaultFilterTypeOptions.length; i++) {
        if (timeType === defaultFilterTypeOptions[i].value) {
          return defaultFilterTypeOptions[i].label
        }
      }
      return ''
    }
  },
  data() {
    return {
      weekOptions: Object.assign({}, defaultWeekOptions),
      uploadPath,
      list: [],
      cardId: undefined,
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        itemId: undefined
      },
      dataForm: {
        dateType: undefined,
        startDate: undefined,
        endDate: undefined,
        timeType: undefined,
        startTime: undefined,
        endTime: undefined,
        filterType: undefined,
        weekFilter: undefined,
        weekFilterArray: undefined
      },
      endDatePicker: this.processDate(),
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '创建'
      },
      rules: {
        // name: [
        //   { required: true, message: '名称不能为空', trigger: 'blur' }
        // ],
      },
      downloadLoading: false,
      brandList: []
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
    if (this.$route.query.itemId == null) {
      return
    }
    this.listQuery.itemId = this.$route.query.itemId
    this.getList()
  },
  methods: {
    formatWeek(weekId) {
      for (let i = 0; i < 8; i++) {
        if (weekId === this.weekOptions[i].value) {
          return this.weekOptions[i].label
        }
      }
      return ''
    },
    processDate() { // 提出结束时间必须大于提出开始时间
      const self = this
      return {
        disabledDate(time) {
          if (self.dataForm.startDate) {
            return new Date(self.dataForm.startDate).getTime() > time.getTime()
          } else {
            return time.getTime() > Date.now()
            // 开始时间不选时，结束时间最大值小于等于当天
          }
        }
      }
    },
    getList() {
      this.listLoading = true
      listSchedule(this.listQuery)
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
    resetForm() {
      this.dataForm = {
        id: undefined,
        name: undefined,
        url: undefined,
        width: undefined,
        height: undefined
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
    createData() {
      this.$refs['dataForm'].validate(valid => {
        if (valid) {
          createSchedule(this.dataForm, this.$route.query.itemId)
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
    handlePlay(row) {
      playSchedule(row.id, this.cardId)
        .then(response => {
          this.$notify.success({
            title: '成功',
            message: '播放成功'
          })
        })
        .catch(response => {
          this.$notify.error({
            title: '失败',
            message: response.data.errmsg
          })
        })
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
          this.dataForm.weekFilter = undefined
          updateSchedule(this.dataForm)
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
      deleteSchedule(row.id, this.$route.query.itemId)
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
