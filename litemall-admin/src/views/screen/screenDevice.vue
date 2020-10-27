<template>
  <div class="app-container">

    <!-- 查询和其他操作 -->
    <div class="filter-container" />

    <!-- 查询结果 -->
    <el-table v-loading="listLoading" :data="list" element-loading-text="正在查询中。。。" border fit highlight-current-row>

      <el-table-column align="center" label="卡号" prop="cardId" />
      <el-table-column align="center" label="网络类型" prop="networkType" />
      <el-table-column align="center" label="屏宽" prop="width" />
      <el-table-column align="center" label="屏高" prop="height" />
      <el-table-column align="center" label="音量" prop="volume" />
      <el-table-column align="center" label="屏幕状态" prop="screenOpenStatus">
        <template slot-scope="scope">{{ scope.row.screenOpenStatus == 1 ? "亮屏" : "关屏" }}</template>
      </el-table-column>
      <el-table-column align="center" label="在线状态" prop="onlineStatus">
        <template slot-scope="scope">{{ scope.row.onlineStatus == 1 ? "在线" : "离线" }}</template>
      </el-table-column>
      <el-table-column align="center" label="屏幕亮度" prop="brightness" />
      <el-table-column align="center" label="修改时间" prop="updateTime" width="180">
        <template slot-scope="scope">{{ scope.row.updateTime | timestampToTime }}</template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="650" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="handleScreenshot(scope.row)">截屏</el-button>
          <el-button type="primary" size="mini" @click="handleClearTask(scope.row)">停止任务</el-button>
          <el-button type="primary" size="mini" @click="stopLive(scope.row)">停止直播</el-button>
          <el-button type="primary" size="mini" @click="handleclearScreen(scope.row)">清屏</el-button>
          <el-button
            type="primary"
            size="mini"
            @click="handleUpdate(scope.row)"
          >编辑
          </el-button>
          <el-button type="primary" size="mini" @click="handleReboot(scope.row)">重启</el-button>
          <el-button
            type="danger"
            size="mini"
            @click="handleDelete(scope.row)"
          >删除
          </el-button>
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
        <el-form-item label="音量" prop="volume">
          <el-input v-model="dataForm.volume" />
        </el-form-item>
        <el-form-item label="屏幕亮度" prop="brightness">
          <el-input v-model="dataForm.brightness" />
        </el-form-item>
        <el-form-item label="屏幕状态" prop="screenOpenStatus">
          <el-radio-group v-model="dataForm.screenOpenStatus">
            <el-radio :label="false">关屏</el-radio>
            <el-radio :label="true">亮屏</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="createData">确定</el-button>
        <el-button v-else type="primary" @click="updateData">确定</el-button>
      </div>
    </el-dialog>

    <!-- 截屏 -->
    <el-dialog :visible.sync="screenshotFormVisible" title="截屏">
      <el-form
        ref="screenshotForm"
        :model="screenshotForm"
        status-icon
        label-position="left"
        label-width="100px"
        style="width: 500px;height: 400px; margin-left:50px;"
      >
        <div id="imgContent" />
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="screenshot">截屏</el-button>
        <el-button @click="screenshotFormVisible = false">取消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<style>
.avatar {
  width: 145px;
  height: 145px;
  display: block;
}
</style>

<script>
import {
  listScreenDevice,
  createScreenDevice,
  updateScreenDevice,
  deleteScreenDevice,
  reboot,
  clearScreen,
  clearPlayerTask,
  stopLiveVideo,
  getScreenshot
} from '@/api/screenDevice'
import { getToken } from '@/utils/auth'
import Pagination from '@/components/Pagination'

export default {
  name: 'ScreenDevice',
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
        name: undefined,
        url: undefined,
        volume: undefined,
        brightness: undefined,
        onlineStatus: undefined
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '创建'
      },
      rules: {},
      downloadLoading: false,
      screenshotForm: {
        cardId: undefined
      },
      screenshotFormVisible: false
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
      listScreenDevice(this.listQuery)
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
        volume: undefined,
        brightness: undefined,
        onlineStatus: undefined
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
          createScreenDevice(this.dataForm)
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
          updateScreenDevice(this.dataForm)
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
        deleteScreenDevice(row.id)
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
    handleReboot(row) {
      reboot(row.cardId)
        .then(response => {
          this.$notify.success({
            title: '成功',
            message: '重启成功'
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
    handleclearScreen(row) {
      clearScreen(row.cardId)
        .then(response => {
          this.$notify.success({
            title: '成功',
            message: '清屏成功'
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
    handleClearTask(row) {
      clearPlayerTask({ selectCardIds: [row.cardId] })
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
    stopLive(row) {
      stopLiveVideo({ selectCardIds: [row.cardId] })
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
    handleScreenshot(row) {
      this.screenshotForm = { cardId: row.cardId }
      this.screenshotFormVisible = true
    },
    screenshot() {
      getScreenshot(this.screenshotForm.cardId).then(response => {
        var content = response.data.data.result
        // 给img容器引入base64的图片
        var img = new Image()
        img.src = 'data:image/jpeg;base64,' + content
        document.getElementById('imgContent').innerHTML = ''
        document.getElementById('imgContent').appendChild(img)
        this.$notify.success({
          title: '成功',
          message: '截屏成功'
        })
      })
    }
  }
}
</script>
