<template>
  <div class="app-container">

    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>卡号列表</span>
        </div>
        <el-table v-loading="listLoading" :data="cardList" element-loading-text="正在查询中。。。" border fit highlight-current-row @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55" />
          <el-table-column align="center" label="卡号" prop="value" />
        </el-table>
      </el-card>
      <el-input v-model="listQuery.name" clearable class="filter-item" style="width: 200px;" placeholder="请输入直播名称" />
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">查找</el-button>
      <el-button class="filter-item" type="primary" icon="el-icon-edit" @click="handleCreate">添加</el-button>
      <!--      <el-select v-model="cardId" clearable style="width: 200px" class="filter-item" placeholder="选择卡号">-->
      <!--        <el-option v-for="item in cardList" :key="item.value" :label="item.label" :value="item.value" />-->
      <!--      </el-select>-->
      <el-button class="filter-item" type="primary" icon="el-icon-video-pause" @click="stopLive">停止直播</el-button>
    </div>

    <!-- 查询结果 -->
    <el-table v-loading="listLoading" :data="list" element-loading-text="正在查询中。。。" border fit highlight-current-row>

      <el-table-column align="center" label="名称" prop="name" />
      <el-table-column align="center" label="审核状态" prop="passStatus">
        <template slot-scope="scope">{{ scope.row.passStatus | formatPassStatus }}</template>
      </el-table-column>
      <el-table-column align="center" label="直播地址" prop="url" />
      <el-table-column align="center" label="宽" prop="width" />
      <el-table-column align="center" label="高" prop="height" />
      <el-table-column align="center" label="修改时间" prop="updateTime">
        <template slot-scope="scope">{{ scope.row.updateTime | timestampToTime }}</template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="230" class-name="small-padding fixed-width">
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
        <el-form-item label="名称" prop="name">
          <el-input v-model="dataForm.name" />
        </el-form-item>
        <el-form-item label="直播地址" prop="url">
          <el-input v-model="dataForm.url" />
          如：rtmp://58.200.131.2:1935/livetv/hunantv
        </el-form-item>
        <el-form-item label="宽" prop="width">
          <el-input
            v-model="dataForm.width"
            type="number"
            min="0"
            step="1"
            size="2"
            on-keypress="return (/[\d]/.test(String.fromCharCode(event.keyCode)))"
          />
        </el-form-item>
        <el-form-item label="高" prop="height">
          <el-input
            v-model="dataForm.height"
            type="number"
            min="0"
            step="1"
            size="2"
            on-keypress="return (/[\d]/.test(String.fromCharCode(event.keyCode)))"
          />
        </el-form-item>
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
.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}
.clearfix:after {
  clear: both
}

.box-card {
  width: 280px;
}
</style>

<script>
import { listLive, createLive, updateLive, deleteLive, playLive } from '@/api/live'
import { stopLiveVideo } from '@/api/screenDevice'

import { selectOnlineDevice } from '@/api/screenDevice'
import { uploadPath } from '@/api/storage'
import { getToken } from '@/utils/auth'
import Pagination from '@/components/Pagination'
import _ from 'lodash'

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
  name: 'Live',
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
    formatPassStatus(type) {
      for (let i = 0; i < defaultPassStatusOptions.length; i++) {
        if (type === defaultPassStatusOptions[i].value) {
          return defaultPassStatusOptions[i].label
        }
      }
      return ''
    }
  },
  data() {
    return {
      uploadPath,
      list: [],
      cardList: [],
      cardId: undefined,
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        name: undefined
      },
      dataForm: {
        id: undefined,
        name: undefined,
        url: undefined,
        width: undefined,
        height: undefined
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
        url: [
          { required: true, message: '直播地址不能为空', trigger: 'blur' }
        ],
        width: [
          { required: true, message: '宽不能为空', trigger: 'blur' }
        ],
        height: [
          { required: true, message: '高不能为空', trigger: 'blur' }
        ]
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
    this.getList()
    this.handleCardList()
  },
  methods: {
    handleCardList() {
      selectOnlineDevice().then(response => {
        this.cardList = response.data.data.list
      })
    },
    getList() {
      this.listLoading = true
      listLive(this.listQuery)
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
          createLive(this.dataForm)
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
    stopLive() {
      if (this.multipleSelection == null || this.multipleSelection.length === 0) {
        this.$message.error('请选择至少一个卡号')
        return
      }
      const selectCardIds = []
      _.forEach(this.multipleSelection, function(item) {
        selectCardIds.push(item.value)
      })

      stopLiveVideo({ selectCardIds: selectCardIds })
        .then(response => {
          this.$notify.success({
            title: '成功',
            message: '停止成功'
          })
        })
        .catch(response => {
          this.$notify.error({
            title: '失败',
            message: response.data.errmsg
          })
        })
    },
    handlePlay(row) {
      if (this.multipleSelection == null || this.multipleSelection.length === 0) {
        this.$message.error('请选择至少一个卡号')
        return
      }
      const selectCardIds = []
      _.forEach(this.multipleSelection, function(item) {
        selectCardIds.push(item.value)
      })

      playLive({ id: row.id, selectCardIds: selectCardIds })
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
          updateLive(this.dataForm)
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
      this.$confirm('确定删除吗？').then(_ => {
        deleteLive(row.id)
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
      }).catch(_ => {
      })
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    }
  }
}
</script>
