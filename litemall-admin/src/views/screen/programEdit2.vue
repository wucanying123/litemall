<template>
  <el-container style="border: 1px solid #eee">
    <el-aside
      width="220px"
      style="background-color: rgb(238, 241, 246)"
    >
      <el-menu :default-openeds="['1','2','3']">
        <el-submenu index="1">
          <template slot="title">
            <i class="el-icon-picture" />图片
          </template>

          <div
            v-for="(source, index) in pictureList"
            :id="source.sourceId"
            :key="index"
            :index="source.sourceId"

            :source="JSON.stringify(source)"
            ondblclick="deleteTrackSourceMaterial(event)"
            :smurl="source.url"
            :smtype="source._type"
            class="sliderBlock"
            draggable="true"
            onmouseup="mouseRelease()"
            onmousedown="unboundTrackOnMousedown(event)"
            ondragstart="drag(event)"
          >
            {{ source.name }}{{ source.fileExt }}
          </div>
        </el-submenu>
        <el-submenu index="2">
          <template slot="title">
            <i class="el-icon-video-camera" />视频
          </template>
          <div
            v-for="(source, index) in videoList"
            :id="source.sourceId"
            :key="index"
            :index="source.sourceId"

            :source="JSON.stringify(source)"
            ondblclick="deleteTrackSourceMaterial(event)"
            :smurl="source.url"
            class="sliderBlock"
            style="background-color: #F08080;"
            :smtype="source._type"
            draggable="true"
            onmouseup="mouseRelease()"
            onmousedown="unboundTrackOnMousedown(event)"
            ondragstart="drag(event)"
          >
            {{ source.name }}{{ source.fileExt }}
          </div>

        </el-submenu>
        <el-submenu index="3">
          <template slot="title">
            <i class="el-icon-document" />多行文本
          </template>
          <el-menu-item-group>
            <el-menu-item index="3-1">
              多行文本测试
            </el-menu-item>
          </el-menu-item-group>
        </el-submenu>
      </el-menu>
    </el-aside>

    <el-container style="width: 500px">
      <div style="width: 100%;height: 100%;">
        <div id="package">
          <div id="leftMenuPackage">
            <button
              style="width:95%;height:40px;font-size:16px;"
              @click="addTrack"
            >
              添加轨道
            </button>
            <button
              style="width:95%;height:40px;font-size:16px;"
              @click="addTime(60)"
            >
              追加60秒
            </button>
            <button
              style="width:95%;height:40px;font-size:16px;"
              @click="deleteTime(60)"
            >
              删除60秒
            </button>
            <button
              style="width:95%;height:40px;font-size:16px;"
              title="实现方式为强制替换时间,过程中会出现卡顿,丢帧,不推荐"
              @click="startPlay"
            >
              开始播放(时间)
            </button>
            <button
              style="width:95%;height:40px;font-size:16px;"
              title="按帧，无[按时间]中的缺陷"
              @click="startPlayFrame"
            >
              开始播放(按帧)
            </button>
            <button
              style="width:95%;height:40px;font-size:16px;"
              @click="stopPlay"
            >
              结束播放
            </button>
            <button
              style="width:95%;height:40px;font-size:16px;"
              @click="enlargeSpacing"
            >
              加大间距
            </button>
            <button
              style="width:95%;height:40px;font-size:16px;"
              @click="reduceSpacing"
            >
              减少间距
            </button>
            <button
              style="width:95%;height:40px;font-size:16px;"
              @click="recordScreen"
            >
              录制画面(按帧)
            </button>
            <button
              style="width:95%;height:40px;font-size:16px;"
              @click="exportVideo"
            >
              导出视频
            </button>
          </div>
          <div id="timeLinePackage">
            <div id="marginPackage">
              <div id="spots" />
              <input
                id="progressBar"
                type="range"
                value="0"
                @input="slidingTrigger()"
              >
              <!-- 默认轨道 -->
              <div
                ondblclick="deleteTrack(event)"
                tracklayer="1"
                class="track"
                ondrop="drop(event)"
                ondragover="allowDrop(event)"
              />
            </div>
          </div>
          <div id="videoPackage" />
        </div>
        <div
          id="smallbox1"
          ondblclick="deleteTrackSourceMaterial(event)"
          smurl="./images/111.jpg"
          class="sliderBlock"
          smtype="Image"
          draggable="true"
          onmouseup="mouseRelease()"
          onmousedown="unboundTrackOnMousedown(event)"
          ondragstart="drag(event)"
        >
          Image
        </div>
        <div
          id="smallbox2"
          ondblclick="deleteTrackSourceMaterial(event)"
          smurl="./images/222.jpg"
          class="sliderBlock"
          smtype="Image"
          draggable="true"
          onmouseup="mouseRelease()"
          onmousedown="unboundTrackOnMousedown(event)"
          ondragstart="drag(event)"
        >
          Image
        </div>

        <div
          id="smallbox3"
          ondblclick="deleteTrackSourceMaterial(event)"
          smurl="./images/333.jpg"
          class="sliderBlock"
          smtype="Image"
          draggable="true"
          onmouseup="mouseRelease()"
          onmousedown="unboundTrackOnMousedown(event)"
          ondragstart="drag(event)"
        >
          Image
        </div>
        <div
          id="smallbox4"
          ondblclick="deleteTrackSourceMaterial(event)"
          smurl="./images/444.jpg"
          class="sliderBlock"
          smtype="Image"
          draggable="true"
          onmouseup="mouseRelease()"
          onmousedown="unboundTrackOnMousedown(event)"
          ondragstart="drag(event)"
        >
          Image
        </div>

        <div
          id="smallbox5"
          ondblclick="deleteTrackSourceMaterial(event)"
          smurl="./videos/test.mp4"
          class="sliderBlock"
          style="background-color: #F08080;"
          smtype="Video"
          draggable="true"
          onmouseup="mouseRelease()"
          onmousedown="unboundTrackOnMousedown(event)"
          ondragstart="drag(event)"
        >
          Video
        </div>
        <div
          id="smallbox6"
          ondblclick="deleteTrackSourceMaterial(event)"
          smurl="./videos/test.mp4"
          class="sliderBlock"
          style="background-color: #F08080;"
          smtype="Video"
          draggable="true"
          onmouseup="mouseRelease()"
          onmousedown="unboundTrackOnMousedown(event)"
          ondragstart="drag(event)"
        >
          Video
        </div>

        <div
          id="smallbox7"
          ondblclick="deleteTrackSourceMaterial(event)"
          class="sliderBlock"
          style="background-color: #009393;"
          smtype="MultiText"
          text="测试-左滚动"
          direction="left"
          draggable="true"
          onmouseup="mouseRelease()"
          onmousedown="unboundTrackOnMousedown(event)"
          ondragstart="drag(event)"
        >
          Roll-Left
        </div>
        <div
          id="smallbox8"
          ondblclick="deleteTrackSourceMaterial(event)"
          class="sliderBlock"
          style="background-color: #009393;"
          smtype="MultiText"
          text="测试-右滚动"
          direction="right"
          draggable="true"
          onmouseup="mouseRelease()"
          onmousedown="unboundTrackOnMousedown(event)"
          ondragstart="drag(event)"
        >
          Roll-Right
        </div>
      </div>
    </el-container>
    <div class="app-container">
      <el-form
        ref="program"
        :rules="rules"
        :model="program"
        status-icon
        label-position="left"
        label-width="100px"
        style="width: 500px; margin-left:50px;"
      >
        <el-form-item
          label="节目名称"
          prop="name"
        >
          <el-input v-model="program.name" />
        </el-form-item>
        <el-form-item
          label="节目宽"
          prop="width"
        >
          <el-input v-model="program.width" />
        </el-form-item>
        <el-tabs>
          <el-tab-pane label="所选素材信息" type="card">
            <div v-show="sourceDivVisiable" v-if="currentSource" id="sourceDiv">
              <el-row>
                <el-col :span="12">
                  <el-form-item label="名称">
                    <el-input v-model="currentSource.name" @change="sourceChange" />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="类型">
                    <el-input v-model="currentSource._type" readonly />
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="距左">
                    <el-input v-model="currentSource.left" @change="sourceChange" />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="距顶">
                    <el-input v-model="currentSource.top" @change="sourceChange" />
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="宽度">
                    <el-input v-model="currentSource.width" @change="sourceChange" />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="高度">
                    <el-input v-model="currentSource.height" @change="sourceChange" />
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="开始">
                    <el-input v-model="currentSource.playTime" @change="sourceChange" />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="持续">
                    <el-input v-model="currentSource.timeSpan" @change="sourceChange" />
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="入场特效">
                    <el-input v-model="currentSource.entryEffect" @change="sourceChange" />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="入场时间">
                    <el-input v-model="currentSource.entryEffectTimeSpan" @change="sourceChange" />
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="出场特效">
                    <el-input v-model="currentSource.exitEffect" @change="sourceChange" />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="出场时间">
                    <el-input v-model="currentSource.exitEffectTimeSpan" @change="sourceChange" />
                  </el-form-item>
                </el-col>
              </el-row>
            </div>
          </el-tab-pane>
          <!--          <div-->
          <!--            slot="footer"-->
          <!--            class="dialog-footer"-->
          <!--          >-->
          <!--            <el-button-->
          <!--              type="primary"-->
          <!--              @click="handleConfirmSource"-->
          <!--            >-->
          <!--              修改素材信息-->
          <!--            </el-button>-->
          <!--          </div>-->
        </el-tabs>
      </el-form>
      <div
        slot="footer"
        class="dialog-footer"
      >
        <el-button @click="handleCancel">
          取消
        </el-button>
        <el-button
          type="primary"
          @click="handleConfirm"
        >
          确定
        </el-button>
      </div>

      <el-dialog
        :visible.sync="addVisiable"
        title="添加资源"
      >
        <div class="search">
          <el-select
            v-model="listQuery._type"
            clearable
            style="width: 100px"
            class="filter-item"
            placeholder="请选择类型"
          >
            <el-option
              v-for="_type in typeOptions"
              :key="_type.value"
              :label="_type.label"
              :value="_type.value"
            />
          </el-select>
          <el-input
            v-model="listQuery.name"
            clearable
            class="filter-item"
            style="width: 50px;"
            placeholder="请输入资源名称"
          />
          <el-button
            class="filter-item"
            type="primary"
            icon="el-icon-search"
            @click="handleFilter"
          >
            查找
          </el-button>
          <el-table
            v-loading="listLoading"
            :data="list"
            element-loading-text="正在查询中。。。"
            border
            fit
            highlight-current-row
            @selection-change="handleSelectionChange"
          >
            <el-table-column
              type="selection"
              width="30"
            />
            <el-table-column
              align="center"
              label="名称"
              prop="name"
            />

            <el-table-column
              align="center"
              label="内容"
              prop="url"
            >
              <template slot-scope="scope">
                <div v-if="scope.row._type === 'Video'">
                  <video
                    :src="scope.row.url"
                    controls="controls"
                    width="50"
                    height="90"
                  />
                </div>
                <div v-if="scope.row._type === 'Image'">
                  <img
                    v-if="scope.row.url"
                    :src="scope.row.url"
                    width="50"
                    height="90"
                  >
                </div>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="素材时长"
              prop="maxPlayTime"
            >
              <template slot-scope="scope">
                {{ scope.row.maxPlayTime | secondToDate }}
              </template>
            </el-table-column>

            <el-table-column
              align="center"
              label="类型"
              prop="_type"
            >
              <template slot-scope="scope">
                {{ scope.row._type | formatType }}
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="格式"
              prop="fileExt"
            />
          </el-table>
          <pagination
            v-show="total>0"
            :total="total"
            :page.sync="listQuery.page"
            :limit.sync="listQuery.limit"
            @pagination="getList"
          />
        </div>
        <div
          slot="footer"
          class="dialog-footer"
        >
          <el-button @click="addVisiable = false">
            取消
          </el-button>
          <el-button
            type="primary"
            @click="confirmAdd"
          >
            确定添加
          </el-button>
        </div>
      </el-dialog>
    </div>
  </el-container>
</template>

<style>
*{
  padding:0px;
  margin:0px;
}
html{
  width:100%;
  height:100%;
}
#package{
  width: 1100px;
  height: 800px;
  overflow: auto;
  border:1px red solid;
  margin:50px 0 0 50px;
}
#leftMenuPackage{
  width: 120px;
  height: 50%;
  float:left;
}
#timeLinePackage{
  height:calc(50% - 20px);
  width:calc(100% - 140px);
  padding:10px 0px 10px 20px;
  overflow: auto;
  float:left;
}
#videoPackage{
  background-color:#000000;
  width:100%;
  height:50%;
  float:left;
  position: relative;
}
#marginPackage{
  height: 50px;
}
#spots{
  width:100%;
  height: 20px;
}
#progressBar {
  -webkit-appearance: none;
  width: calc(100% + 41px);
  margin-left: -20.5px;
  border-radius: 10px; /*这个属性设置使填充进度条时的图形为圆角*/
  background: transparent;
  position:relative;
  top:-18px;
}
#progressBar:focus {
  outline: none;
}
#progressBar::-webkit-slider-thumb {
  -webkit-appearance: none;
  height: 28px;
  width: 42px;
  margin-top: -8.5px; /*使滑块超出轨道部分的偏移量相等*/
  background: url("~@/assets/images/slider_btn.png") no-repeat;
}
.spotPackage{
  height:20px;
  float:left;
}
.spotPackageTitle{
  color:#8f8ba9;
  margin-left:-50%;
  text-align:center;
  width:100%;
  font-size:12px;
  height:10px;
  line-height:10px;
}
.spotPackageLine{
  width:100%;
  height:4px;
  border:1px #6a7584 solid;
  border-width:0 0 0 1px;
  margin-top:4px;
}
.spotPackageAfter{
  width: 0px;
  height:20px;
  float:left;
}
.spotPackageTitleAfter{
  color:#8f8ba9;
  text-align:center;
  width:100%;
  font-size:12px;
  height:10px;
  line-height:10px;
}
.spotPackageLineAfter{
  width:100%;
  margin-left:-1px;
  height:4px;
  border:1px #6a7584 solid;
  border-width:0 0 0 1px;
  margin-top:4px;
}
.track{
  height: 40px;
  width:100%;
  background: #EAEAEC;
  position:relative;
  top:-24px;
}
/*滑动块*/
.sliderBlock{
  margin-left: 3px;
  height: 40px;
  line-height: 40px;
  width:200px;
  background: #46A3FF;
  cursor:move;
  float:left;
  text-align: center;
  color: #ffffff;
  overflow:hidden;
}
/*画面块*/
.pictureBlock{
  width:30%;
  height:30%;
  display:none;
  position:absolute;
  overflow:hidden;
  border:1px #ffffff dashed;
  opacity:0;
}
.rollText{
  position: relative;
  left:0px;
  color: #ffffff;
}
</style>

<script>
import { listSource } from '@/api/source'
import Pagination from '@/components/Pagination'
import { getToken } from '@/utils/auth'
import { readProgram, updateComplexProgramById } from '@/api/program'

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
  name: 'ProgramEdit2',
  components: { Pagination },
  filters: {
    formatType(_type) {
      for (let i = 0; i < defaultTypeOptions.length; i++) {
        if (_type == defaultTypeOptions[i].value) {
          return defaultTypeOptions[i].label
        }
      }
      return ''
    },
    secondToDate(msd) {
      let theTime = parseInt(msd)// 秒
      let middle = 0// 分
      let hour = 0// 小时

      if (theTime > 60) {
        middle = parseInt(theTime / 60)
        theTime = parseInt(theTime % 60)
        if (middle > 60) {
          hour = parseInt(middle / 60)
          middle = parseInt(middle % 60)
        }
      }
      let result = '' + parseInt(theTime) + '秒'
      if (middle > 0) {
        result = '' + parseInt(middle) + '分' + result
      }
      if (hour > 0) {
        result = '' + parseInt(hour) + '小时' + result
      }
      return result
    },
    timestampToTime(timestamp) {
      if (timestamp != null) {
        const date = new Date(timestamp * 1000)// 时间戳为10位需*1000，时间戳为13位的话不需乘1000
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
      id: 0,
      program: {
        layers: {
          sources: {}
        }
      },
      playSourceList: [],
      addVisiable: false,
      list: [],
      total: 0,
      listLoading: false,
      listQuery: {
        page: 1,
        limit: 5,
        name: undefined,
        _type: undefined
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
      },
      pictureList: [],
      videoList: [],

      timeLinePackageDOM: null,
      spotsDOM: null,
      progressBarDOM: null,
      marginPackageDOM: null,
      videoPackageDOM: null,

      // 录制存储图片集合
      imgs: new Whammy.Video(24),

      // 滑块按住中
      sliderPressHold: false,
      timeLinePackagePadddingLeftAndMarginLeft: null,

      // 每秒多少帧
      pubFrame: 0,

      // 总秒数(默认20)
      totalSecond: 20,

      // (预览使用) 多少毫秒一帧
      pubMillisecondFrame: 0,

      // 所有总宽度(px)
      pubTotalWidth: 0,

      // 进度条每次滑动范围(px)
      pubProgressBarRangePerTime: 0,

      // 滑块最小宽度(px), 设置0则根据一帧最小单位
      pubSliderMinWidth: 0,

      // 渲染箱最小宽高(px)
      renderBlocksBox: { width: 10, height: 10 },

      // 每秒占用宽度(px)
      pubSecondWidth: 40,

      // 当前滑块元素
      currentSlider: null,

      // 当前滑块按住时的偏移位置X
      currentSliderPressHoldOffsetX: 0,

      // 当前滑块在浏览器中的位置X
      currentSliderBrowserX: 0,

      // 当前轨道层级
      trackLayer: 1,

      // 时间轴存储
      pubTimelineStorages: [],

      // 动画特效存储
      animationEffectsStorages: [],

      // 是否启用播放
      playing: false,

      // 播放定时器
      playTimer: null,

      // 无限定时器(一直轮询)
      infiniteTimer: null,

      stopPlayFrame: false,

      sourceDivVisiable: false,

      currentSource: { sourceId: undefined, name: undefined, maxPlayTime: undefined, _type: undefined, mime: undefined, size: undefined, enabled: undefined, fileExt: undefined, showBg: undefined, showHourScale: undefined, showMinScale: undefined, showScaleNum: undefined, showSecond: undefined, center: undefined, createTime: undefined, updateTime: undefined, userid: undefined }

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
    const requestAnimationFrame = window.requestAnimationFrame || window.mozRequestAnimationFrame ||
      window.webkitRequestAnimationFrame || window.msRequestAnimationFrame
    window.requestAnimationFrame = requestAnimationFrame

    const thar = this
    // 元素对象
    this.timeLinePackageDOM = document.getElementById('timeLinePackage')
    this.spotsDOM = document.getElementById('spots')
    this.progressBarDOM = document.getElementById('progressBar')
    this.marginPackageDOM = document.getElementById('marginPackage')
    this.videoPackageDOM = document.getElementById('videoPackage')

    window.deleteTrackSourceMaterial = this.deleteTrackSourceMaterial
    window.mouseRelease = this.mouseRelease
    window.unboundTrackOnMousedown = this.unboundTrackOnMousedown
    window.drag = this.drag
    window.drop = this.drop
    window.allowDrop = this.allowDrop
    window.deleteTrack = this.deleteTrack

    // 距离轨道的左内外边距
    this.timeLinePackagePadddingLeftAndMarginLeft = this.marginPackageDOM.offsetLeft

    window.onmousemove = function(event) {
      if (thar.currentSlider == null) { return }
      const bool = thar.currentSlider.getAttribute('draggable') != 'true'
      if (thar.sliderPressHold && bool && event.target.style.cursor == 'move') {
        const sliderClientX = event.clientX - thar.currentSliderBrowserX
        const moreThan = thar.pubProgressBarRangePerTime - ((event.clientX - thar.timeLinePackagePadddingLeftAndMarginLeft) % thar.pubProgressBarRangePerTime)
        thar.currentSlider.style.left = (sliderClientX + moreThan) + 'px'
        if (parseInt(thar.currentSlider.style.left) < 0) {
          thar.currentSlider.style.left = 0
        }

        const parentWidth = thar.currentSlider.parentNode.offsetWidth
        if (parseInt(thar.currentSlider.style.left) > (parentWidth - thar.currentSlider.offsetWidth)) {
          thar.currentSlider.style.left = (parentWidth - thar.currentSlider.offsetWidth) + 'px'
        }

        // 更新pubTimelineStoragesData数据
        thar.updatePubTimelineStoragesData(thar.currentSlider)

        // 刷新画面
        thar.slidingTrigger()
      }
    }

    window.onmouseup = this.mouseRelease

    // 生成时间轴
    this.generateTimeline(this.totalSecond, 24)
    const rollTexts = document.getElementsByClassName('rollText')
    this.infiniteTimer = setInterval(() => {
      for (let i = 0; i < rollTexts.length; i++) {
        const rollText = rollTexts[i]
        if (rollText.getAttribute('direction') == 'left') {
          const left = (rollText.style.left == '') ? 0 : parseInt(rollText.style.left)
          if (rollText.offsetLeft > -rollText.offsetWidth) {
            rollText.style.left = (left - 1) + 'px'
          } else {
            rollText.style.left = (rollText.parentNode.offsetWidth) + 'px'
          }
        }
        if (rollText.getAttribute('direction') == 'right') {
          if (rollText.offsetLeft < rollText.parentNode.offsetWidth) {
            rollText.style.left = (rollText.offsetLeft + 1) + 'px'
          } else {
            rollText.style.left = (-rollText.offsetLeft) + 'px'
          }
        }
      }
    }, 10)
  },
  created() {
    this.defaultPictureList()
    this.defaultVideoList()
    // if (this.$route.query.id == null) {
    //   return
    // }
    // this.id = this.$route.query.id
    this.getProgram()
    this.sourceDivVisiable = false
    // this.imitateData()
  },
  methods: {
    getProgram() {
      this.listLoading = true
      readProgram({ id: this.id })
        .then(response => {
          this.program = response.data.data.program
          // console.log(this.program)
          this.program.layers = response.data.data.program.layers
          this.playSourceList = response.data.data.playSourceList
          this.listLoading = false
          this.imitateData()
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
        name: undefined,
        _type: undefined
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
          if (this.program.layers != null) {
            if (this.program.layers[0].sources != null) {
              for (let i = 0; i < this.program.layers[0].sources.length; i++) {
                if (sourceId == this.program.layers[0].sources[i].sourceId) {
                  found = true
                }
              }
            }
          }
        }
        if (!found) {
          newPlaySourceIds.push(sourceId)
          newPlaySourceList.push(item)
        }
      })

      if (newPlaySourceIds.length > 0) {
        if (this.program.layers != null) {
          this.playSourceList = this.playSourceList.concat(newPlaySourceList)
          if (this.program.layers[0].sources != null) {
            this.program.layers[0].sources = this.program.layers[0].sources.concat(newPlaySourceList)
          } else {
            this.program.layers[0].sources = newPlaySourceList
          }
        } else {
          this.playSourceList = newPlaySourceList
          this.program.layers = [{ sources: newPlaySourceList }]
        }
      }
      this.addVisiable = false
    },
    handleDelete(row) {
      for (let index = 0; index < this.program.layers[0].sources.length; index++) {
        if (row.sourceId == this.program.layers[0].sources[index].sourceId) {
          this.program.layers[0].sources.splice(index, 1)
        }
      }
      for (let index2 = 0; index2 < this.playSourceList.length; index2++) {
        if (row.sourceId == this.playSourceList[index2].sourceId) {
          this.playSourceList.splice(index2, 1)
        }
      }
    },
    handleCancel() {
      this.$router.push({ path: '/screen/program' })
    },
    handleConfirm() {
      // this.program.layers[0].sources = this.playSourceList
      this.$refs['program'].validate(valid => {
        if (valid) {
          updateComplexProgramById(this.program).then(response => {
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
    },
    defaultPictureList() {
      const sourceQuery = { _type: 'Image' }
      listSource(sourceQuery).then(response => {
        this.pictureList = response.data.data.list
      }).catch(() => {
        this.pictureList = []
      })
    },
    defaultVideoList() {
      const sourceQuery = { _type: 'Video' }
      listSource(sourceQuery).then(response => {
        this.videoList = response.data.data.list
      }).catch(() => {
        this.videoList = []
      })
    },
    // 开始播放(实现方式为强制替换时间,过程中会出现卡顿,丢帧,不推荐)
    startPlay() {
      this.stopPlay()
      this.progressBarDOM.setAttribute('disabled', 'disabled')
      this.playing = true
      let currentNum = 0
      this.playTimer = window.setInterval(() => {
        const progressBarValue = parseInt(this.progressBarDOM.value)
        if (currentNum < parseInt(this.progressBarDOM.max)) {
          currentNum = progressBarValue
          this.progressBarDOM.value = progressBarValue + 1
          this.slidingTrigger()
        } else {
          this.stopPlay()
          this.startPlay()
        }
      }, this.pubMillisecondFrame)
    },
    // 开始播放(按帧，无[按时间]中的缺陷)
    startPlayFrame() {
      if (this.stopPlayFrame) { return }

      if (parseInt(this.progressBarDOM.value) < parseInt(this.progressBarDOM.max)) {
        this.playing = true

        // 获取当前毫秒数
        const startMillisecond = new Date().getMilliseconds()
        let stopMillisecond = 0

        // 帧
        this.slidingTrigger()

        stopMillisecond = new Date().getMilliseconds()

        // 延时时间为：每帧毫秒数 - 渲染所用时间
        setTimeout(() => {
          this.progressBarDOM.value = parseInt(this.progressBarDOM.value) + 1
          requestAnimationFrame(this.startPlayFrame)
        }, this.pubMillisecondFrame - ((stopMillisecond - startMillisecond) / 1000))
      } else {
        this.stopPlay()
        this.playing = false
      }
    },
    execStopPlay(el) {
      const video = el.querySelector(' video')
      video.currentTime = 0
      video.pause()
      video.currentTime = 0
      video.pause()
    },
    stopPlay() {
      this.stopPlayFrame = true

      this.progressBarDOM.removeAttribute('disabled')
      this.progressBarDOM.value = 0
      clearInterval(this.playTimer)
      this.playing = false

      if (this.pubTimelineStorages != null && this.pubTimelineStorages.length > 0) {
        // 暂停所有视频
        for (let i = 0; i < this.pubTimelineStorages.length; i++) {
          const obj = this.pubTimelineStorages[i]
          if (obj != null) {
            const el = document.getElementById('sm_' + obj.id)
            if (el == null) { return }

            if (obj.smtype == 'Video') {
              this.execStopPlay(el)
            }
          }
        }
      }

      // 更新画面
      this.slidingTrigger()
    },
    enlargeSpacing() {
      this.pubSecondWidth += 15
      this.generateTimeline(this.totalSecond, this.pubFrame)
    },
    reduceSpacing() {
      if (this.pubSecondWidth <= 31) {
        return
      }
      this.pubSecondWidth -= 15
      this.generateTimeline(this.totalSecond, this.pubFrame)
    },
    generateTimeline(second, frame) {
      this.pubFrame = frame
      this.pubTotalWidth = second * this.pubSecondWidth
      this.pubProgressBarRangePerTime = this.pubSecondWidth / frame

      // 最小滑动块范围，交给系统自动分配时，系统设置为一帧占位px
      if (this.pubSliderMinWidth == 0) { this.pubSliderMinWidth = this.pubProgressBarRangePerTime }

      let html = ''
      this.spotsDOM.innerHTML = html
      // 计算每帧多少毫秒
      this.pubMillisecondFrame = 1000 / frame
      for (let i = 0; i <= second; i++) {
        if (i != second) {
          html = '<div class="spotPackage" style="width:' + this.pubSecondWidth + 'px">' +
            '<div class="spotPackageTitle">' + i + 's</div>' +
            '<div class="spotPackageLine"></div>' +
            '</div>'
        } else {
          // 计算文字往左偏移量，一个数字偏移6px
          const offsetLeft = (i.toString().length) * 4.5
          html = '<div class="spotPackageAfter">' +
            '<div class="spotPackageTitleAfter" style="margin-left:-' + offsetLeft + 'px;">' + i + 's</div>' +
            '<div class="spotPackageLineAfter"></div>' +
            '</div>'
        }
        this.spotsDOM.innerHTML += html
      }

      // 进度条长度设置
      this.progressBarDOM.style.width = (this.pubTotalWidth + 41) + 'px'

      // 秒点长度设置
      this.spotsDOM.style.width = this.pubTotalWidth + 'px'

      // 外边距包长度设置 (由于出现滚动条后，右内边距无法生效，不得已才加的外边距包)
      this.marginPackageDOM.style.width = (this.pubTotalWidth + 20) + 'px'

      // 轨道长度设置
      this.updateTracksWidth()

      // 配置进度条最大值
      this.progressBarDOM.setAttribute('max', second * frame)
    },
    updateTracksWidth() {
      const tracks = document.getElementsByClassName('track')
      for (let i = 0; i < tracks.length; i++) {
        const item = tracks[i]
        item.style.width = this.pubTotalWidth + 'px'
      }
    },
    addTrack() {
      this.trackLayer++
      const track = document.createElement('div')
      track.setAttribute('class', 'track')
      track.setAttribute('ondblclick', 'deleteTrack(event);')
      track.setAttribute('ondrop', 'drop(event);')
      track.setAttribute('ondragover', 'allowDrop(event);')
      track.style.marginBottom = '1px'
      track.setAttribute('tracklayer', this.trackLayer)
      // marginPackageDOM.appendChild(track);
      this.progressBarDOM.parentNode.insertBefore(track, this.progressBarDOM.nextSibling)

      // 轨道长度设置
      this.updateTracksWidth()
      this.program.layers.push({})
      // console.log(this.program.layers)
    },
    deleteTrack(event) {
      event.target.remove()
      this.trackLayer--
      const sliderParent = event.target
      // 当前选择第几个轨道
      const currentTracklayer = sliderParent.getAttribute('tracklayer')
      console.log(currentTracklayer)
      this.program.layers.splice(currentTracklayer - 1, 1)
      console.log('删除轨道')
      console.log(this.program)
    },
    addTime(second) {
      this.totalSecond += second
      this.generateTimeline(this.totalSecond, this.pubFrame)
    },
    deleteTime(second) {
      if ((this.totalSecond - second) < 20) {
        alert('不能再删除了')
        return
      }

      this.totalSecond -= second

      // 更新总长度(px)
      this.pubTotalWidth = this.totalSecond * this.pubSecondWidth

      for (let i = 0; i < this.pubTimelineStorages.length; i++) {
        const obj = this.pubTimelineStorages[i]
        const el = document.getElementById(obj.id)
        const smEL = document.getElementById('sm_' + obj.id)
        if (this.pubTotalWidth < obj.startX || this.pubTotalWidth < obj.stopX) {
          el.remove()
          smEL.remove()
          this.pubTimelineStorages.splice(i, 1)
        }
      }

      this.generateTimeline(this.totalSecond, this.pubFrame)
    },
    deleteTrackSourceMaterial(event) {
      const thar = event.target
      const smEL = document.getElementById('sm_' + thar.id)
      for (let i = 0; i < this.pubTimelineStorages.length; i++) {
        const obj = this.pubTimelineStorages[i]
        if (thar.id == obj.id) {
          console.log(thar)
          smEL.remove()
          thar.remove()
          this.pubTimelineStorages.splice(i, 1)
        }
      }
      let currentTracklayer = this.currentSlider.style.zIndex
      if (currentTracklayer == null || currentTracklayer == '') {
        currentTracklayer = this.currentSlider.getAttribute('tracklayer')
      }
      for (let j = 0; j < this.program.layers[currentTracklayer - 1].sources.length; j++) {
        if (this.currentSource.sourceId === this.program.layers[currentTracklayer - 1].sources[j].sourceId) {
          this.program.layers[currentTracklayer - 1].sources.splice(j, 1)
        }
      }
      console.log('删除元素后')
      console.log(this.program)
      // 阻止父类div做出事件响应
      this.stopPropagation()
    },
    frameHandle(type, currentOffsetX) {
      for (let i = 0; i < this.pubTimelineStorages.length; i++) {
        const obj = this.pubTimelineStorages[i]
        if (obj != null) {
          const el = document.getElementById('sm_' + obj.id)
          if (el == null) { return }

          if (type == 1) {
            el.style.display = 'none'
          } else if (type == 2) {
            if (currentOffsetX >= obj.startX && currentOffsetX <= obj.stopX) {
              el.style.display = 'block'
              el.style.opacity = '100'

              // 特效处理方法
              this.specialEffectsHandle(obj, el)
              if (obj.smtype == 'Video') {
                const video = el.querySelector(' video')
                const img = el.querySelector(' div')

                // 当前滑块起始帧
                const startFrame = obj.startX / (this.pubSecondWidth / this.pubFrame)
                // 定位秒(带小数位，可控制帧)
                const positionSecond = parseFloat((this.progressBarDOM.value - startFrame) / this.pubFrame)
                // console.log("滑块位置", parseFloat(startFrame / this.pubFrame));
                // console.log("滑块结束位置", parseFloat((obj.stopX / (this.pubSecondWidth / this.pubFrame)) / this.pubFrame));
                video.currentTime = positionSecond
                if (img != null) {
                  const canvas = document.createElement('canvas')
                  canvas.width = el.offsetWidth
                  canvas.height = el.offsetHeight
                  const ctx = canvas.getContext('2d')
                  ctx.drawImage(video, 0, 0, el.offsetWidth, el.offsetHeight)
                  img.style.width = el.offsetWidth + 'px'
                  img.style.height = el.offsetHeight + 'px'
                  img.style.background = 'url(' + canvas.toDataURL() + ') no-repeat'
                  img.style.backgroundSize = '100% 100%'
                }
                // generateImages();
              }
            } else if (currentOffsetX > obj.stopX) {
              if (obj.smtype == 'Video') {
                if (this.playing) {
                  this.execStopPlay(el)
                }
              }
            }
          }
        }
      }
    },
    specialEffectsHandle(parentObj, el) {
      const id = parentObj.id
      for (let i = 0; i < this.animationEffectsStorages.length; i++) {
        const obj = this.animationEffectsStorages[i]
        // 持续时间
        const duration = obj.duration
        // 特效总共帧
        const totalFrame = (duration / 1000) * this.pubFrame
        // 当前滑块起始帧
        const startSliderFrame = parentObj.startX / (this.pubSecondWidth / this.pubFrame)
        // 当前进度（帧）
        const currentFrame = this.progressBarDOM.value
        // 每帧显示百分比（opacity以1为单位)
        const perFramePercentage = (100 / totalFrame) / 100

        if (obj.id == id) {
          // 淡入
          if (obj.animationType == 'fadein') {
            // 特效结束帧
            const endFrame = startSliderFrame + totalFrame
            if (currentFrame >= startSliderFrame && currentFrame <= endFrame) {
              el.style.opacity = ((currentFrame - startSliderFrame) * parseFloat(perFramePercentage))
            }
          } else if (obj.animationType == 'fadeout') { // 淡出
            // 特效结束帧
            const endFrame = ((parentObj.stopX - parentObj.startX) / (this.pubSecondWidth / this.pubFrame))
            // 特效开始帧
            const startFrame = endFrame - totalFrame
            if (currentFrame >= startFrame && currentFrame <= endFrame) {
              el.style.opacity = (endFrame - currentFrame) * parseFloat(perFramePercentage)
            }
          }
        }
      }
    },
    slidingTrigger() {
      setTimeout(() => {
        // 当前偏移位置X
        const currentOffsetX = this.pubProgressBarRangePerTime * this.progressBarDOM.value

        // 先全部隐藏
        this.frameHandle(1)

        // 再部分显示
        this.frameHandle(2, currentOffsetX)
      }, 10)
    },
    generateImages() {
      const thar = this
      html2canvas(this.videoPackageDOM, { useCORS: true }).then(function(canvas) {
        // const ctx = canvas.getContext('2d')
        thar.imgs.add(canvas)
        thar.progressBarDOM.value = parseInt(thar.progressBarDOM.value) + 1
        requestAnimationFrame(thar.recordScreen)
      })
    },
    videoPackageBorderOperation(showBool) {
      const els = this.videoPackageDOM.getElementsByClassName(' pictureBlock')
      for (let i = 0; i < els.length; i++) {
        const el = els[i]
        if (showBool) {
          el.style.borderWidth = '1px'
        } else {
          el.style.borderWidth = '0px'
        }
      }
    },
    recordScreen() {
      if (parseInt(this.progressBarDOM.value) < parseInt(this.progressBarDOM.max)) {
        // 去除边框
        this.videoPackageBorderOperation(false)

        this.playing = true
        // 帧
        this.slidingTrigger()

        // 生成图片
        this.generateImages()
      } else {
        // 去除边框
        this.videoPackageBorderOperation(true)

        this.stopPlay()
        this.playing = false
      }
    },
    exportVideo() {
      if (this.imgs.frames.length == 0) {
        alert('请先录制画面')
        return
      }

      const output = this.imgs.compile()
      const url = webkitURL.createObjectURL(output)

      const exportVideoA = document.createElement('a')
      exportVideoA.setAttribute('target', '_blank')
      exportVideoA.href = url
      exportVideoA.click()

      // 播放（该div以删除，测试请新建）
      /* const video2 = document.getElementById("video2");
      video2.src = url;*/
    },
    unboundTrackOnMousedown(event) {
      this.currentSliderPressHoldOffsetX = event.offsetX
      this.updateCurrentSliderRelevantData(event)
    },
    updateCurrentSliderRelevantData(event) {
      this.sliderPressHold = true
      this.currentSlider = event.target
      this.currentSliderBrowserX = event.clientX - (this.currentSlider.style.left == '' ? 0 : parseInt(this.currentSlider.style.left))
      console.log('点击滑条')

      this.sourceDivVisiable = true
      // // const left = smEL.style.left == '' ? 0 : parseInt(smEL.style.left)
      // // const top = smEL.style.top == '' ? 0 : parseInt(smEL.style.top)
      // // const width = parseInt(smEL.clientWidth)
      // // const height = parseInt(smEL.clientHeight)
      // // console.log(left)
      // // console.log(top)
      // // console.log(width)
      // // console.log(height)
      // //
      // console.log(this.currentSlider)
      // const currentTracklayer = this.currentSlider.style.zIndex
      // console.log("当前轨道")
      // console.log(currentTracklayer)
      // // console.log(this.program)
      // const newPlaySource = JSON.parse(this.currentSlider.getAttribute('source'))
      // if (newPlaySource != null && currentTracklayer != null && currentTracklayer != '') {
      //     for (let j = 0; j < this.program.layers[currentTracklayer - 1].sources.length; j++) {
      //       if (newPlaySource.sourceId === this.program.layers[currentTracklayer - 1].sources[j].sourceId) {
      //         this.currentSource = this.program.layers[currentTracklayer - 1].sources[j]
      //         // this.currentSource.top = top
      //         // this.currentSource.left = left
      //         // this.currentSource.width = width
      //         // this.currentSource.height = height
      //       }
      //     }
      // }
      // console.log(JSON.stringify(this.currentSource))
      // console.log(this.program)
    },
    sourceChange() {
      let currentTracklayer = this.currentSlider.style.zIndex
      if (currentTracklayer == null || currentTracklayer == '') {
        currentTracklayer = this.currentSlider.getAttribute('tracklayer')
      }
      // console.log(JSON.stringify(this.currentSource))
      if (this.currentSource != null && currentTracklayer != null) {
        for (let j = 0; j < this.program.layers[currentTracklayer - 1].sources.length; j++) {
          if (this.currentSource.sourceId === this.program.layers[currentTracklayer - 1].sources[j].sourceId) {
            this.program.layers[currentTracklayer - 1].sources[j] = this.currentSource
          }
        }
      }
      // console.log(this.program)
    },
    updatePubTimelineStoragesData(thar) {
      for (let i = 0; i < this.pubTimelineStorages.length; i++) {
        const obj = this.pubTimelineStorages[i]
        if (obj.id == thar.id) {
          const obj2 = {
            id: thar.id,
            smtype: thar.getAttribute('smtype'),
            startX: parseInt(thar.style.left),
            stopX: parseInt(thar.style.left) + thar.offsetWidth
          }
          this.pubTimelineStorages[i] = obj2
        }
      }
    },
    mouseRelease() {
      this.sliderPressHold = false
    },
    drop(ev) {
      // 当前元素（滑动块的父级）
      const sliderParent = ev.target
      // 防止叠加在子div中
      if (sliderParent.getAttribute('class') != 'track') {
        alert('不能重叠')
        return
      }

      console.log(this.currentSlider)
      // 当前选择第几个轨道
      const currentTracklayer = sliderParent.getAttribute('tracklayer')
      const newPlaySource = JSON.parse(this.currentSlider.getAttribute('source'))
      this.currentSource = newPlaySource
      if (this.program.layers == null) {
        this.program.layers = [{}]
      }
      if (this.program.layers[currentTracklayer - 1].sources != null) {
        this.program.layers[currentTracklayer - 1].sources = this.program.layers[currentTracklayer - 1].sources.concat(newPlaySource)
      } else {
        this.program.layers[currentTracklayer - 1].sources = [newPlaySource]
      }

      this.updateCurrentSliderRelevantData(ev)

      ev.preventDefault()

      const id = ev.dataTransfer.getData('id')
      const offsetX = (this.currentSliderBrowserX + parseInt(this.timeLinePackageDOM.scrollLeft)) - this.timeLinePackagePadddingLeftAndMarginLeft - this.currentSliderPressHoldOffsetX

      // 滑块操作处理（移动/拉长div）
      this.sliderOperationHandle(id, sliderParent, offsetX)
    },
    sliderOperationHandle(id, sliderParent, offsetX) {
      console.log('添加后')
      // console.log(this.program)
      const thar = this
      const elObj = document.getElementById(id)
      // 克隆滑块
      const elObjClone = elObj.cloneNode(true)
      elObjClone.setAttribute('id', Math.random())
      elObj.parentNode.insertBefore(elObjClone, elObj.nextSibling)
      elObj.setAttribute('draggable', false)
      console.log(elObj)

      // const currentTracklayer = elObj.getAttribute('tracklayer')
      // if (this.currentSource != null && currentTracklayer != null) {
      //   for (let j = 0; j < this.program.layers[currentTracklayer - 1].sources.length; j++) {
      //     if (this.currentSource.sourceId === this.program.layers[currentTracklayer - 1].sources[j].sourceId) {
      //       this.currentSource = this.program.layers[currentTracklayer - 1].sources[j]
      //       this.currentSource.top = top
      //       this.currentSource.left = left
      //       this.currentSource.width = width
      //       this.currentSource.height = height
      //     }
      //   }
      // }
      console.log(this.currentSource)
      console.log(this.program)

      // 滑块鼠标悬停时，更换相关指针图标
      elObj.onmousemove = (e) => {
        e = e || event
        const x = e.clientX
        // const y = e.clientY
        // 外距离 + 内边距
        const oBoxL = elObj.offsetLeft - parseInt(thar.timeLinePackageDOM.scrollLeft) + thar.timeLinePackagePadddingLeftAndMarginLeft
        // const oBoxT = elObj.offsetTop - parseInt(thar.timeLinePackageDOM.scrollTop)
        const oBoxW = elObj.offsetWidth
        // const oBoxH = elObj.offsetHeight

        if (x > oBoxL && x < oBoxL + (oBoxW / 2)) {
          elObj.style.cursor = 'move'
        } else if (x > oBoxL + (oBoxW / 2) && x < oBoxL + oBoxW) {
          elObj.style.cursor = 'w-resize'
        }
      }

      // 滑块拉长处理
      elObj.onmousedown = function(e) {
        e = e || event
        const x = e.clientX
        // const y = e.clientY
        const oBoxL = elObj.offsetLeft - parseInt(thar.timeLinePackageDOM.scrollLeft) + thar.timeLinePackagePadddingLeftAndMarginLeft
        // const oBoxT = elObj.offsetTop - parseInt(thar.timeLinePackageDOM.scrollTop)
        const oBoxW = elObj.offsetWidth
        // const oBoxH = elObj.offsetHeight

        let positionType
        if (x > oBoxL && x < oBoxL + (oBoxW / 2)) {
          thar.unboundTrackOnMousedown(e)
        } else if (x > oBoxL + (oBoxW / 2) && x < oBoxL + oBoxW) {
          positionType = 'right'
        }

        document.onmousemove = function(e) {
          e = e || event
          const xx = e.clientX
          // const yy = e.clientY
          if (positionType == 'right') {
            const width = oBoxW + xx - x
            // 验证是否小于最小宽度 和 是否在最大宽度范围内
            if (width > thar.pubSliderMinWidth && width + elObj.offsetLeft < thar.pubTotalWidth) {
              elObj.style.width = width + 'px'
              // 更新pubTimelineStoragesData数据
              thar.updatePubTimelineStoragesData(elObj)
            }
          }

          return false
        }

        document.onmouseup = function() {
          document.onmousemove = null
          document.onmouseup = null
        }

        if (e.preventDefault) {
          e.preventDefault()
        }
      }

      sliderParent.appendChild(elObj)
      this.sliderPressHold = false
      elObj.style.position = 'absolute'
      elObj.style.top = '0px'
      const left = offsetX < 0 ? 0 : ((offsetX + elObj.offsetWidth) > thar.pubTotalWidth ? (thar.pubTotalWidth - elObj.offsetWidth) : offsetX)
      elObj.style.left = left + 'px'
      elObj.style.float = 'left'
      elObj.style.marginLeft = '0px'
      elObj.style.zIndex = sliderParent.getAttribute('tracklayer')

      // 时间轴存储
      const pubTimelineStorage = {
        id: id,
        smtype: elObj.getAttribute('smtype'),
        startX: left,
        stopX: left + elObj.offsetWidth
      }
      thar.pubTimelineStorages.push(pubTimelineStorage)

      // 特效存储
      const animationEffectsStorageHandle = (id, animationType, duration) => {
        const obj = {
          id,
          animationType,
          duration
        }
        thar.animationEffectsStorages.push(obj)
      }

      if (elObj.getAttribute('fadein') != null) { animationEffectsStorageHandle(id, 'fadein', elObj.getAttribute('fadein')) }

      if (elObj.getAttribute('fadeout') != null) { animationEffectsStorageHandle(id, 'fadeout', elObj.getAttribute('fadeout')) }

      // 刷新画面
      thar.slidingTrigger()

      const smurl = elObj.getAttribute('smurl')
      const smtype = elObj.getAttribute('smtype')
      const smEL = document.createElement('div')
      smEL.setAttribute('class', 'pictureBlock')
      smEL.id = 'sm_' + id
      smEL.style.zIndex = sliderParent.getAttribute('tracklayer')
      smEL.setAttribute('tracklayer', sliderParent.getAttribute('tracklayer'))
      smEL.setAttribute('source', elObj.getAttribute('source'))

      if (smtype == 'Image') {
        smEL.setAttribute('crossorigin', 'anonymous')
        smEL.style.background = 'url(' + smurl + ') no-repeat'
        smEL.style.backgroundSize = '100% 100%'
      } else if (smtype == 'Video') {
        const video = document.createElement('video')
        video.setAttribute('crossOrigin', 'anonymous')
        video.src = smurl
        video.style = 'width:0px; height:0px; display:none;'
        smEL.appendChild(video)

        const img = document.createElement('div')
        img.style = 'width:100%; height:100%;'
        img.setAttribute('tracklayer', sliderParent.getAttribute('tracklayer'))
        img.setAttribute('source', elObj.getAttribute('source'))
        smEL.appendChild(img)
      } else if (smtype == 'MultiText') {
        const rollText = document.createElement('div')
        rollText.setAttribute('class', 'rollText')
        rollText.setAttribute('direction', elObj.getAttribute('direction'))
        rollText.innerText = elObj.getAttribute('text')

        smEL.appendChild(rollText)
      }

      smEL.onmousemove = (e) => {
        e = e || event
        const x = e.offsetX
        const y = e.offsetY
        // 外距离 + 内边距
        const oBoxW = smEL.offsetWidth
        const oBoxH = smEL.offsetHeight

        if (x > 0 && x <= (thar.renderBlocksBox.width / 2)) {
          smEL.style.cursor = 'w-resize'
        } else if (x > (oBoxW - (thar.renderBlocksBox.width / 2)) && x < oBoxW) {
          smEL.style.cursor = 'w-resize'
        } else if (y > 0 && y <= (thar.renderBlocksBox.height / 2)) {
          smEL.style.cursor = 'n-resize'
        } else if (y > (oBoxH - (thar.renderBlocksBox.height / 2)) && y < oBoxH) {
          smEL.style.cursor = 'n-resize'
        } else {
          smEL.style.cursor = 'move'
        }
      }
      smEL.onmousedown = (e) => {
        e = e || event
        const x = e.clientX
        const y = e.clientY
        const offsetX = e.offsetX
        const offsetY = e.offsetY
        const oBoxW = smEL.offsetWidth
        const oBoxH = smEL.offsetHeight

        let currentsmELBrowserX
        let currentsmELBrowserY
        let positionType
        if (offsetX > 0 && offsetX <= (thar.renderBlocksBox.width / 2)) {
          positionType = 'left'
        } else if (offsetX > (oBoxW - (thar.renderBlocksBox.width / 2)) && offsetX < oBoxW) {
          positionType = 'right'
        } else if (offsetY > 0 && offsetY <= (thar.renderBlocksBox.height / 2)) {
          positionType = 'top'
        } else if (offsetY > (oBoxH - (thar.renderBlocksBox.height / 2)) && offsetY < oBoxH) {
          positionType = 'bottom'
        } else {
          positionType = 'move'
          currentsmELBrowserX = e.clientX - (smEL.style.left == '' ? 0 : parseInt(smEL.style.left))
          currentsmELBrowserY = e.clientY - (smEL.style.top == '' ? 0 : parseInt(smEL.style.top))
        }

        console.log('按下画布')
        const sliderParent = e.target
        const currentTracklayer = sliderParent.getAttribute('tracklayer')
        const newPlaySource = JSON.parse(sliderParent.getAttribute('source'))
        if (newPlaySource != null && currentTracklayer != null && currentTracklayer != '') {
          for (let j = 0; j < this.program.layers[currentTracklayer - 1].sources.length; j++) {
            if (newPlaySource.sourceId === this.program.layers[currentTracklayer - 1].sources[j].sourceId) {
              this.currentSource = this.program.layers[currentTracklayer - 1].sources[j]
            }
          }
        }

        document.onmousemove = (e) => {
          e = e || event
          const xx = e.clientX
          const yy = e.clientY
          const left = smEL.style.left == '' ? 0 : parseInt(smEL.style.left)
          const top = smEL.style.top == '' ? 0 : parseInt(smEL.style.top)
          const bottom = thar.videoPackageDOM.clientHeight - (top + parseInt(smEL.clientHeight))
          const right = thar.videoPackageDOM.clientWidth - (left + parseInt(smEL.clientWidth))
          const width = parseInt(smEL.clientWidth)
          const height = parseInt(smEL.clientHeight)
          // console.log(top, bottom, left, right)
          if (positionType == 'left') {
            const width = oBoxW + x - xx
            if (left < 0 || right < 0 || width < thar.renderBlocksBox.width) {
              return
            }
            smEL.style.width = width + 'px'
            smEL.style.left = ((xx - thar.videoPackageDOM.offsetLeft) <= -1 ? 0 : (xx - thar.videoPackageDOM.offsetLeft)) + 'px'
          } else if (positionType == 'right') {
            const width = oBoxW + xx - x
            if (left < 0 || right < 0 || width < thar.renderBlocksBox.width) {
              return
            }
            smEL.style.width = width + 'px'
          } else if (positionType == 'top') {
            const height = oBoxH + y - yy
            if (top < 0 || bottom < 0 || height < thar.renderBlocksBox.height) {
              return
            }
            smEL.style.height = height + 'px'
            smEL.style.top = ((yy - thar.videoPackageDOM.offsetTop) <= -1 ? 0 : (yy - thar.videoPackageDOM.offsetTop)) + 'px'
            // smEL.style.top = parseInt(smEL.style.top) - 1 + 'px';
          } else if (positionType == 'bottom') {
            const height = oBoxH + yy - y
            if (top < 0 || bottom < 0 || height < thar.renderBlocksBox.height) {
              return
            }
            smEL.style.height = height + 'px'
          } else if (positionType == 'move') {
            const X = (e.clientX - currentsmELBrowserX)
            const Y = (e.clientY - currentsmELBrowserY)
            const parentNodeWidth = smEL.parentNode.offsetWidth
            const parentNodeHeight = smEL.parentNode.offsetHeight
            if (X > -1 && X < (parentNodeWidth - smEL.offsetWidth + 1)) {
              smEL.style.left = (e.clientX - currentsmELBrowserX) + 'px'
            }
            if (Y > -1 && Y < (parentNodeHeight - smEL.offsetHeight + 1)) {
              smEL.style.top = (e.clientY - currentsmELBrowserY) + 'px'
            }
          }
          console.log('移动画布')
          const sliderParent = e.target
          const currentTracklayer = sliderParent.getAttribute('tracklayer')
          if (this.currentSource != null && currentTracklayer != null) {
            for (let j = 0; j < this.program.layers[currentTracklayer - 1].sources.length; j++) {
              if (this.currentSource.sourceId === this.program.layers[currentTracklayer - 1].sources[j].sourceId) {
                this.currentSource = this.program.layers[currentTracklayer - 1].sources[j]
                this.currentSource.top = top
                this.currentSource.left = left
                this.currentSource.width = width
                this.currentSource.height = height
              }
            }
          }
          console.log(this.currentSource)
          console.log(this.program)
          return false
        }

        document.onmouseup = function() {
          document.onmousemove = null
          document.onmouseup = null
        }

        if (e.preventDefault) {
          e.preventDefault()
        }
      }

      thar.videoPackageDOM.appendChild(smEL)
    },
    allowDrop(ev) {
      ev.preventDefault()
    },
    drag(ev) {
      ev.dataTransfer.setData('id', ev.target.id)
    },
    stopPropagation(e) {
      e = e || window.event
      if (e.stopPropagation) { // W3C阻止冒泡方法
        e.stopPropagation()
      } else {
        e.cancelBubble = true // IE阻止冒泡方法
      }
    },
    // 动态追加模拟数据
    imitateData() {
      console.log('模拟')
      console.log(this.program)
      this.addTime(360)
      // 轨道数
      const alltrackNum = this.program.layers.length
      for (let i = 0; i < alltrackNum - 1; i++) {
        this.addTrack()
      }
      if (this.program.layers != null) {
        console.log(this.program.layers)
        console.log(this.program.layers.length)
        for (let i = 0; i < this.program.layers.length; i++) {
          if (this.program.layers[i].sources != null) {
            for (let j = 0; j < this.program.layers[i].sources.length; j++) {
              const source = this.program.layers[i].sources[j]
              console.log('打印第' + i + '轨道第' + j + '个')
              console.log(source)
              const playTime = source.playTime
              const timeSpan = source.timeSpan

              const id = source.sourceId
              const offsetX = (playTime / (this.pubMillisecondFrame / 1000)) * this.pubProgressBarRangePerTime
              const offsetY = offsetX + this.pubSecondWidth * timeSpan
              console.log(offsetX)
              console.log(offsetY)
              const smELTop = source.top
              const smELLeft = source.left

              const tracks = document.getElementsByClassName('track')
              for (let m = 0; m < tracks.length; m++) {
                if (tracks[m].getAttribute('tracklayer') == i + 1) {
                  const myTrack = tracks[m]
                  const sliderParent = myTrack
                  this.sliderOperationHandle(id, sliderParent, offsetX)
                  setTimeout(() => {
                    const eL = document.getElementById(id)
                    eL.style.width = offsetY + 'px'
                    const smEL = document.getElementById('sm_' + id)
                    smEL.style.left = smELLeft + 'px'
                    smEL.style.top = smELTop + 'px'

                    this.updatePubTimelineStoragesData(eL)
                  }, 500)
                }
              }
            }
          }
        }
      }
    }
  }
}
</script>
