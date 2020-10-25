<template>
  <div class="app-container">

    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <!--      <el-select v-model="cardId" clearable style="width: 200px" class="filter-item" placeholder="选择卡号">-->
      <!--        <el-option v-for="item in cardList" :key="item.value" :label="item.label" :value="item.value" />-->
      <!--      </el-select>-->
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>卡号列表</span>
        </div>
        <el-table
          v-loading="listLoading"
          :data="cardList"
          element-loading-text="正在查询中。。。"
          border
          fit
          highlight-current-row
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="55" />
          <el-table-column align="center" label="卡号" prop="value" />
        </el-table>
      </el-card>
      <el-input v-model="listQuery.name" clearable class="filter-item" style="width: 200px;" placeholder="请输入搜索名称" />
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">查找</el-button>
      <el-button class="filter-item" type="primary" icon="el-icon-video-pause" @click="handleClearTask">停止节目</el-button>
      <el-container />
    </div>
    <!-- 查询结果 -->
    <el-table v-loading="listLoading" :data="list" element-loading-text="正在查询中。。。" border fit highlight-current-row>

      <el-table-column align="center" label="名称" prop="name" />
      <el-table-column align="center" label="审核状态" prop="passStatus">
        <template slot-scope="scope">{{ scope.row.passStatus | formatPassStatus }}</template>
      </el-table-column>
      <el-table-column align="center" label="修改时间" prop="updateTime">
        <template slot-scope="scope">{{ scope.row.updateTime | timestampToTime }}</template>
      </el-table-column>
      <el-table-column align="center" label="创建者" prop="userName" />
      <el-table-column align="center" label="操作" width="500" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button v-if="scope.row.itemsIds" type="primary" size="mini" icon="el-icon-s-promotion" @click="handlePlay(scope.row)">播放任务
          </el-button>
          <el-button type="primary" size="mini" @click="handleUpdate(scope.row)">编辑任务</el-button>
          <el-button type="danger" size="mini" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
      <el-table-column align="center" label="播放任务进度" width="500" class-name="small-padding fixed-width">
        <template>
          <el-progress :text-inside="true" :stroke-width="20" :percentage="progressBar.progress" status="success" />
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="listQuery.page"
      :limit.sync="listQuery.limit"
      @pagination="getList"
    />

    <!-- 添加或修改对话框 -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form
        ref="dataForm"
        :rules="rules"
        :model="dataForm"
        status-icon
        label-position="left"
        label-width="100px"
        style="width: 400px; margin-left:60px;"
      >
        <el-form-item label="名称" prop="name">
          <el-input v-model="dataForm.name" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="updateData">确定</el-button>
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
import { listTask, updateTask, deleteTask } from '@/api/task'
import { getToken } from '@/utils/auth'
import Pagination from '@/components/Pagination'
import { clearPlayerTask, selectOnlineDevice } from '@/api/screenDevice'
import { playTask } from '@/api/task'
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
  name: 'Task',
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
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        name: undefined
      },
      dataForm: {
        id: undefined,
        name: undefined
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
        ]
      },
      downloadLoading: false,
      cardList: [],
      cardId: undefined,
      progressBar: { taskItemId: undefined, progress: undefined, commandId: undefined }
    }
  },
  computed: {
    headers() {
      return {
        'X-Litemall-Admin-Token': getToken()
      }
    }
  },
  mounted() {
    // WebSocket
    if ('WebSocket' in window) {
      this.websocket = new WebSocket('ws://localhost:8083/websocket')
      this.initWebSocket()
    } else {
      alert('当前浏览器 Not support websocket')
    }
  },
  created() {
    this.getList()
    this.handleCardList()
  },
  methods: {
    getList() {
      this.listLoading = true
      listTask(this.listQuery)
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
    handleCardList() {
      selectOnlineDevice().then(response => {
        this.cardList = response.data.data.list
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
      }
    },
    handleUpdate(row) {
      this.$router.push({ path: '/screen/item', query: { taskId: row._id }})
    },
    updateData() {
      this.$refs['dataForm'].validate(valid => {
        if (valid) {
          updateTask(this.dataForm)
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
        deleteTask(row._id)
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
    handleClearTask() {
      if (this.multipleSelection == null || this.multipleSelection.length === 0) {
        this.$message.error('请选择至少一个卡号')
        return
      }
      const selectCardIds = []
      _.forEach(this.multipleSelection, function(item) {
        selectCardIds.push(item.value)
      })

      clearPlayerTask({ selectCardIds: selectCardIds })
        .then(response => {
          this.$notify.success({
            title: '成功',
            message: '停止成功'
          })
          this.getList()
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

      playTask({ id: row._id, selectCardIds: selectCardIds })
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
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    initWebSocket() {
      // 连接错误
      this.websocket.onerror = this.setErrorMessage

      // 连接成功
      this.websocket.onopen = this.setOnopenMessage

      // 收到消息的回调
      this.websocket.onmessage = this.setOnmessageMessage

      // 连接关闭的回调
      this.websocket.onclose = this.setOncloseMessage

      // 监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
      window.onbeforeunload = this.onbeforeunload
    },
    setErrorMessage() {
      // console.log(
      // "WebSocket连接发生错误状态码："+this.websocket.readyState
      // );
    },
    setOnopenMessage() {
      // console.log("WebSocket连接成功状态码："+this.websocket.readyState);
    },
    setOnmessageMessage(event) {
      // 服务器推送的消息
      console.log('服务端返回：' + event.data)
      // progressBar.progress就是绑定的进度值
      this.progressBar.progress = parseInt(JSON.parse(event.data).progress)
    },
    setOncloseMessage() {
      // console.log("WebSocket连接关闭状态码："+this.websocket.readyState);
    },
    onbeforeunload() {
      this.closeWebSocket()
    },
    closeWebSocket() {
      this.websocket.close()
    }
  }
}
</script>
