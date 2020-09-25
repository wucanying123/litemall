<template>
  <div class="app-container">

    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <el-button class="filter-item" type="primary" icon="el-icon-edit" @click="handleSearch">添加节目</el-button>
      <!-- 查询结果 -->
      <el-table v-show="false" :data="programList" border fit highlight-current-row>

        <el-table-column align="center" label="名称" prop="name" />
        <el-table-column align="width" label="节目宽" prop="width" />
        <el-table-column align="height" label="节目高" prop="height" />
        <el-table-column align="center" label="修改时间" prop="updateTime">
          <template slot-scope="scope">{{ scope.row.updateTime | timestampToTime }}</template>
        </el-table-column>

        <el-table-column align="center" label="格式" prop="fileExt" />
        <el-table-column align="center" label="操作" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button type="danger" size="mini" @click="handleDeleteSelection(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 查询结果 -->
    <el-table v-loading="listLoading" :data="list" element-loading-text="正在查询中。。。" border fit highlight-current-row>
      <el-table-column align="center" label="节目名称" prop="_program.name" />
      <el-table-column align="center" label="版本" prop="version" />
      <el-table-column align="center" label="重复次数" prop="repeatTimes" />
      <el-table-column align="center" label="优先级" prop="priority" />
      <el-table-column align="center" label="修改时间" prop="updateTime">
        <template slot-scope="scope">{{ scope.row.updateTime | timestampToTime }}</template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="350" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="handleLink(scope.row)">编辑节目详情</el-button>
          <el-button type="primary" size="mini" @click="handleUpdate(scope.row)">编辑</el-button>
          <el-button type="primary" size="mini" @click="handleSchedule(scope.row)">定时</el-button>
          <el-button type="danger" size="mini" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />

    <!-- 添加或修改对话框 -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="dataForm" status-icon label-position="left" label-width="100px" style="width: 400px; margin-left:60px;">
        <el-form-item label="重复次数" prop="repeatTimes">
          <el-input
            v-model="dataForm.repeatTimes"
            type="number"
            min="0"
            step="1"
            size="2"
            on-keypress="return (/[\d]/.test(String.fromCharCode(event.keyCode)))"
          />
        </el-form-item>
        <el-form-item label="优先级" prop="priority">
          <el-input v-model="dataForm.priority" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="createData">确定</el-button>
        <el-button v-else type="primary" @click="updateData">确定</el-button>
      </div>
    </el-dialog>

    <el-dialog :visible.sync="addVisiable" title="添加节目">
      <div class="search">
        <el-input v-model="listQueryProgram.name" clearable class="filter-item" style="width: 200px;" placeholder="请输入节目名称" />
        <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleProgramFilter">查找</el-button>
        <el-table v-loading="listLoadingProgram" :data="listProgram" element-loading-text="正在查询中。。。" border fit highlight-current-row @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55" />
          <!--          <el-table-column align="center" label="节目id" prop="_id" />-->
          <el-table-column align="center" label="名称" prop="name" />
          <el-table-column align="width" label="节目宽" prop="width" />
          <el-table-column align="height" label="节目高" prop="height" />
          <el-table-column align="center" label="修改时间" prop="updateTime">
            <template slot-scope="scope">{{ scope.row.updateTime | timestampToTime }}</template>
          </el-table-column>
        </el-table>
        <pagination v-show="totalProgram>0" :total="totalProgram" :page.sync="listQueryProgram.page" :limit.sync="listQueryProgram.limit" @pagination="getList" />

      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="addVisiable = false">取消</el-button>
        <!--        <el-button type="primary" @click="confirmAddProgram">确定1</el-button>-->
        <el-button type="primary" @click="handleConfirm">确定</el-button>
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
import { listItem, createItem, updateItem, deleteItem } from '@/api/item'
import { updateTaskTotalById } from '@/api/task'
import { getToken } from '@/utils/auth'
import Pagination from '@/components/Pagination'
import { listProgram } from '@/api/program' // Secondary package based on el-pagination

export default {
  name: 'Item',
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
      cardId: undefined,
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        taskId: undefined
      },
      dataForm: {
        id: undefined,
        repeatTimes: undefined,
        priority: undefined
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
      brandList: [],
      programList: [],
      addVisiable: false,
      listQueryProgram: {
        pageProgram: 1,
        limitProgram: 20,
        type: undefined,
        name: undefined
      },
      listProgram: [],
      totalProgram: 0,
      listLoadingProgram: false,
      selectedlist: [],
      task: {
        items: {
          _program: {}
        }
      },
      item: {
        program: []
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
    if (this.$route.query.taskId == null) {
      return
    }
    this.listQuery.taskId = this.$route.query.taskId
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      listItem(this.listQuery)
        .then(response => {
          this.task = response.data.data
          this.list = response.data.data.items
          this.total = response.data.data.items.length
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
        repeatTimes: undefined,
        priority: undefined
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
          createItem(this.dataForm)
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
    handleSchedule(row) {
      this.$router.push({ path: '/screen/schedule', query: { itemId: row._id }})
    },
    handleConfirm() {
      this.confirmAddProgram()
      updateTaskTotalById(this.task).then(response => {
        location.reload()
      })
        .catch(response => {
          this.$notify.error({
            title: '失败',
            message: response.data.errmsg
          })
        })
    },
    updateData() {
      this.$refs['dataForm'].validate(valid => {
        if (valid) {
          updateItem(this.dataForm)
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
      deleteItem(this.$route.query.taskId, row._id)
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
    },
    handleSearch() {
      this.listQueryProgram = {
        pageProgram: 1,
        limitProgram: 5,
        name: undefined
      }
      this.listProgram = []
      this.totalProgram = 0
      this.selectedlist = []
      this.addVisiable = true
      this.getProgramList()
    },
    handleProgramFilter() {
      this.listQueryProgram.pageProgram = 1
      this.getProgramList()
    },
    handleDeleteSelection(row) {
      for (var index = 0; index < this.item.program.length; index++) {
        if (row._id === this.item.program[index]) {
          this.item.program.splice(index, 1)
        }
      }
      for (var index2 = 0; index2 < this.programList.length; index2++) {
        if (row._id === this.programList[index2]._id) {
          this.programList.splice(index2, 1)
        }
      }
    },
    confirmAddProgram() {
      const newProgramIds = []
      const newProgramList = []
      const newItemList = []
      this.selectedlist.forEach(item => {
        const _id = item._id
        let found = false
        // if (this.item != null) {
        //   if (this.item.program != null) {
        //     this.item.program.forEach(programId => {
        //       if (_id === programId) {
        //         found = true
        //       }
        //     })
        //   }
        // }
        if (this.task != null) {
          if (this.task.items != null) {
            this.task.items.forEach(item => {
              if (_id === item._program._id) {
                found = true
              }
            })
          }
        }
        if (!found) {
          newProgramIds.push(_id)
          newProgramList.push(item)
          var newItem = { _program: item }
          newItemList.push(newItem)
        }
      })

      if (newProgramIds.length > 0) {
        if (this.item.program != null) {
          this.item.program = this.item.program.concat(newProgramIds)
          this.programList = this.programList.concat(newProgramList)
          this.task.items = this.task.items.concat(newItemList)
        } else {
          this.item.program = newProgramIds
          this.programList = newProgramList
          this.task.items = newItemList
        }
      }
      this.addVisiable = false
    },
    getProgramList() {
      this.listLoadingProgram = true
      listProgram(this.listQueryProgram).then(response => {
        this.listProgram = response.data.data.list
        this.totalProgram = response.data.data.total
        this.listLoadingProgram = false
      }).catch(() => {
        this.listProgram = []
        this.totalProgram = 0
        this.listLoadingProgram = false
      })
    },
    handleSelectionChange(val) {
      this.selectedlist = val
    },
    handleLink(row) {
      this.$router.push({ path: '/screen/program-edit', query: { id: row.programId }})
    }
  }
}
</script>
