<template>
  <el-container style="border: 1px solid #eee">
    <el-aside
      width="220px"
      style="background-color: rgb(238, 241, 246)"
    >
      <el-menu :default-openeds="['1','2','3','4']">
        <el-submenu index="1">
          <template slot="title">
            <i class="el-icon-picture" />图片
          </template>

          <div
            v-for="(source, index) in pictureList"
            :id="source.uuid"
            :key="index"

            :sourceId="source.sourceId"
            ondblclick="deleteTrackSourceMaterial(event)"
            :smurl="source.url"
            :smtype="source._type"
            class="sliderBlock"
            style="text-align: left"
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
            :id="source.uuid"
            :key="index"

            :sourceId="source.sourceId"
            ondblclick="deleteTrackSourceMaterial(event)"
            :smurl="source.url"
            class="sliderBlock"
            style="background-color: #F08080;text-align: left;"
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
          <div
            :id="defaultMultiText.uuid"
            :sourceId="defaultMultiText.sourceId"
            ondblclick="deleteTrackSourceMaterial(event)"
            class="sliderBlock"
            style="background-color: #009393;text-align: left;"
            smtype="MultiText"
            :text="defaultMultiText.html"
            direction="left"
            draggable="true"
            onmouseup="mouseRelease()"
            onmousedown="unboundTrackOnMousedown(event)"
            ondragstart="drag(event)"
          >
            {{ defaultMultiText.name }}
          </div>
        </el-submenu>
        <el-submenu index="4">
          <template slot="title">
            <i class="el-icon-news" />网址
          </template>
          <div
            :id="defaultWebURL.uuid"
            :sourceId="defaultWebURL.sourceId"
            ondblclick="deleteTrackSourceMaterial(event)"
            class="sliderBlock"
            style="background-color:#6A6AFF;text-align: left;"
            smtype="WebURL"
            draggable="true"
            onmouseup="mouseRelease()"
            onmousedown="unboundTrackOnMousedown(event)"
            ondragstart="drag(event)"
          >
            {{ defaultWebURL.name }}
          </div>
        </el-submenu>
        <div id="alreadySources" style="display: none;" />
      </el-menu>
    </el-aside>

    <el-container>
      <div style="width: 100%;height: 100%;">
        <div id="package">
          (添加素材:左侧单击并拖拽素材到层上，删除素材:双击素材滑块，删除层:双击层滑块)
          <div id="leftMenuPackage">
            <button
              style="width:95%;height:40px;font-size:16px;"
              @click="addTrack"
            >
              添加层
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
          <div id="videoPackage" :style="{width: program.width + 'px',height:program.height + 'px'}" />
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
        style="width: 800px; margin-left:50px;"
      >
        <el-form-item label="节目名称" prop="name"><el-input v-model="program.name" /></el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item label="节目宽" prop="width"><el-input v-model="program.width" readonly />  </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="节目高" prop="height"><el-input v-model="program.height" readonly />  </el-form-item>
          </el-col>
        </el-row>
        <el-tabs>
          <el-tab-pane label="所选素材信息" type="card">
            <div v-show="sourceDivVisiable" v-if="currentSource" id="sourceDiv">
              <el-row>
                <el-col :span="12">
                  <el-form-item label="名称">
                    <el-input id="currentSourceName" v-model="currentSource.name" @change="sourceChange" />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="类型">
                    <el-input id="currentSourceType" v-model="currentSource._type" readonly />
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="距左">
                    <el-input id="currentSourceLeft" v-model="currentSource.left" readonly @change="sourceChange" />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="距顶">
                    <el-input id="currentSourceTop" v-model="currentSource.top" readonly @change="sourceChange" />
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="宽度">
                    <el-input
                      id="currentSourceWidth"
                      v-model="currentSource.width"
                      readonly
                      @change="sourceChange"
                    />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="高度">
                    <el-input id="currentSourceHeight" v-model="currentSource.height" readonly @change="sourceChange" />
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="开始">
                    <el-input id="currentSourcePlayTime" v-model="currentSource.playTime" readonly @change="sourceChange" />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="持续">
                    <el-input id="currentSourceTimeSpan" v-model="currentSource.timeSpan" readonly @change="sourceChange" />
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="入场特效">
                    <el-select v-model="currentSource.entryEffect" placeholder="请选择">
                      <el-option :value="'None'" label="无" />
                      <el-option :value="'ALPHA_IN'" label="淡入" />
                      <el-option :value="'ALPHA_OUT'" label="淡出" />
                      <el-option :value="'RANDOM'" label="随机" />
                      <el-option :value="'MOVING_LEFT'" label="连续左移" />
                      <el-option :value="'MOVING_RIGHT'" label="连续右移" />
                      <el-option :value="'MOVING_TOP'" label="连续上移" />
                      <el-option :value="'MOVING_BOTTOM'" label="连续下移" />
                      <el-option :value="'ZOOM_IN'" label="放大" />
                      <el-option :value="'ZOOM_OUT'" label="缩小" />
                      <el-option :value="'ROTATE_RIGHT'" label="向右旋转" />
                      <el-option :value="'ROTATE_LEFT'" label="向左旋转" />
                      <el-option :value="'ZOOM_IN_LEFT_BOTTOM'" label="左下角放大" />
                      <el-option :value="'ZOOM_IN_LEFT_TOP'" label="左上角放大" />
                      <el-option :value="'ZOOM_IN_RIGHT_TOP'" label="右上角放大" />
                      <el-option :value="'ZOOM_IN_RIGHT_BOTTOM'" label="右下角放大" />
                      <el-option :value="'ZOOM_OUT_LEFT_BOTTOM'" label="左下角缩小" />
                      <el-option :value="'ZOOM_OUT_LEFT_TOP'" label="左上角缩小" />
                      <el-option :value="'ZOOM_OUT_RIGHT_TOP'" label="右上角缩小" />
                      <el-option :value="'ZOOM_OUT_RIGHT_BOTTOM'" label="右下角缩小" />
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="入场时间(秒)">
                    <el-input
                      id="currentSourceEntryEffectTimeSpan"
                      v-model="currentSource.entryEffectTimeSpan"
                      type="number"
                      min="0"
                      step="1"
                      size="2"
                      on-keypress="return (/[\d]/.test(String.fromCharCode(event.keyCode)))"
                      @change="sourceChange"
                    />
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="出场特效">
                    <el-select v-model="currentSource.exitEffect" placeholder="请选择">
                      <el-option :value="'None'" label="无" />
                      <el-option :value="'ALPHA_IN'" label="淡入" />
                      <el-option :value="'ALPHA_OUT'" label="淡出" />
                      <el-option :value="'RANDOM'" label="随机" />
                      <el-option :value="'MOVING_LEFT'" label="连续左移" />
                      <el-option :value="'MOVING_RIGHT'" label="连续右移" />
                      <el-option :value="'MOVING_TOP'" label="连续上移" />
                      <el-option :value="'MOVING_BOTTOM'" label="连续下移" />
                      <el-option :value="'ZOOM_IN'" label="放大" />
                      <el-option :value="'ZOOM_OUT'" label="缩小" />
                      <el-option :value="'ROTATE_RIGHT'" label="向右旋转" />
                      <el-option :value="'ROTATE_LEFT'" label="向左旋转" />
                      <el-option :value="'ZOOM_IN_LEFT_BOTTOM'" label="左下角放大" />
                      <el-option :value="'ZOOM_IN_LEFT_TOP'" label="左上角放大" />
                      <el-option :value="'ZOOM_IN_RIGHT_TOP'" label="右上角放大" />
                      <el-option :value="'ZOOM_IN_RIGHT_BOTTOM'" label="右下角放大" />
                      <el-option :value="'ZOOM_OUT_LEFT_BOTTOM'" label="左下角缩小" />
                      <el-option :value="'ZOOM_OUT_LEFT_TOP'" label="左上角缩小" />
                      <el-option :value="'ZOOM_OUT_RIGHT_TOP'" label="右上角缩小" />
                      <el-option :value="'ZOOM_OUT_RIGHT_BOTTOM'" label="右下角缩小" />
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="出场时间(秒)">
                    <el-input
                      id="currentSourceExitEffectTimeSpan"
                      v-model="currentSource.exitEffectTimeSpan"
                      type="number"
                      min="0"
                      step="1"
                      size="2"
                      on-keypress="return (/[\d]/.test(String.fromCharCode(event.keyCode)))"
                      @change="sourceChange"
                    />
                  </el-form-item>
                </el-col>
              </el-row>
              <div v-show="urlDivVisiable" v-if="currentSource" id="urlDiv">
                <el-row>
                  <el-col :span="24">
                    <el-form-item label="URL">
                      <el-input id="currentSourceUrl" v-model="currentSource.url" @change="sourceChange" />
                    </el-form-item>
                  </el-col>
                </el-row>
              </div>
              <div v-show="textDivVisiable" v-if="currentSource" id="textDiv">
                <el-row>
                  <el-col :span="12">
                    <el-form-item label="翻页间隔(秒)">
                      <el-input
                        id="currentSourceSpeed"
                        v-model="currentSource.speed"
                        type="number"
                        min="0"
                        step="1"
                        size="2"
                        on-keypress="return (/[\d]/.test(String.fromCharCode(event.keyCode)))"
                        @change="sourceChange"
                      />
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="默认行高">
                      <el-input
                        id="currentSourceLineHeight"
                        v-model="currentSource.lineHeight"
                        type="number"
                        min="0"
                        max="100"
                        step="0.1"
                        size="2"
                        on-keypress="return (/[\d]/.test(String.fromCharCode(event.keyCode)))"
                        @change="sourceChange"
                      />
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="12">
                    <el-form-item id="currentSoureCenter" label="垂直居中" @change="sourceChange">
                      <el-select v-model="currentSource.center" placeholder="请选择">
                        <el-option :value="true" label="是" />
                        <el-option :value="false" label="否" />
                      </el-select>
                    </el-form-item>
                    <el-form-item label="背景色">
                      <div v-if="this.currentSource && this.currentSource.backgroundColor">
                        <section>
                          <div
                            class="color_con"
                            :style="{background:currentSource.backgroundColor}"
                            @click="handleShowColor"
                          >颜色
                          </div>
                          <div v-show="colorShow">
                            <sketch-picker v-model="currentSource.backgroundColor" @input="updateValue" />
                          </div>
                        </section>
                      </div>
                      <div v-else>
                        <section>
                          <div class="color_con" :style="{background:color}" @click="handleShowColor">选择颜色</div>
                          <div v-show="colorShow">
                            <sketch-picker v-model="color" @input="updateValue" />
                          </div>
                        </section>
                      </div>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  <el-form-item label="内容" prop="html">
                    <p><span style="color:red">提示: 按Shift+回车换行，按回车换页</span></p>
                    <editor id="currentSourceHtml" v-model="currentSource.html" :init="editorInit" @change="sourceChange" />
                  </el-form-item>
                </el-row>
              </div>
            </div>
          </el-tab-pane>
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
  width: 1000px;
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
  /*width:100%;*/
  /*height:50%;*/
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
  width:400px;
  background: #46A3FF;
  cursor:move;
  float:left;
  text-align: center;
  color: #ffffff;
  overflow:hidden;
}
/*画面块*/
.pictureBlock{
  width:100%;
  height:100%;
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
import { selectDefaultWebURL, selectDefaultMultiText, listSource } from '@/api/source'
import Pagination from '@/components/Pagination'
import { getToken } from '@/utils/auth'
import { readProgram, updateSeniorProgramById } from '@/api/program'
import Editor from '@tinymce/tinymce-vue'
import { Sketch } from 'vue-color'

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
  name: 'SeniorProgramEdit',
  components: { Editor, Pagination, 'sketch-picker': Sketch },
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
        name: [
          { required: true, message: '节目名称不能为空', trigger: 'blur' }
        ]
      },
      pictureList: [],
      videoList: [],
      defaultMultiText: { id: undefined, uuid: undefined, html: undefined },
      defaultWebURL: { id: undefined, uuid: undefined },

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
      pubFrame: 24,

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

      urlDivVisiable: false,

      textDivVisiable: false,

      currentSource: { lineHeight: undefined, speed: undefined, backgroundColor: undefined, html: undefined, id: undefined, url: undefined, uuid: undefined, exitEffectTimeSpan: undefined, exitEffect: undefined, entryEffectTimeSpan: undefined, entryEffect: undefined, timeSpan: undefined, playTime: undefined, sourceId: undefined, name: undefined, maxPlayTime: undefined, _type: undefined, mime: undefined, size: undefined, enabled: undefined, fileExt: undefined, showBg: undefined, showHourScale: undefined, showMinScale: undefined, showScaleNum: undefined, showSecond: undefined, center: false, createTime: undefined, updateTime: undefined, userid: undefined },

      editorInit: {
        language: 'zh_CN',
        convert_urls: false,
        height: 500,
        plugins: [
          'advlist anchor autolink autosave code codesample colorpicker colorpicker contextmenu directionality emoticons fullscreen hr importcss insertdatetime link lists media nonbreaking noneditable pagebreak paste preview print save searchreplace spellchecker tabfocus table template textcolor textpattern visualblocks visualchars wordcount'
        ],
        toolbar: [
          'code forecolor backcolor bold italic underline strikethrough alignleft aligncenter alignright indent  hr bullist numlist charmap emoticons fullscreen removeformat preview'
        ]
      },
      color: 'rgba(0,0,0,1)',
      // 颜色选择器
      colorShow: false,
      colors: {
        hex: '#194d33',
        hsl: { h: 150, s: 0.5, l: 0.2, a: 1 },
        hsv: { h: 150, s: 0.66, v: 0.30, a: 1 },
        rgba: { r: 25, g: 77, b: 51, a: 1 },
        a: 1
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
    this.generateTimeline(this.totalSecond, this.pubFrame)
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
    this.getDefaultMultiText()
    this.getDefaultWebURL()
    if (this.$route.query.id == null) {
      return
    }
    this.id = this.$route.query.id
    this.getProgram()
    this.sourceDivVisiable = false
    this.urlDivVisiable = false
    this.textDivVisiable = false
  },
  methods: {
    getProgram() {
      this.listLoading = true
      readProgram({ id: this.id })
        .then(response => {
          this.program = response.data.data.program
          console.log(this.program)
          console.log(this.program.name)
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
      const newPlaySourceUIds = []
      const newPlaySourceList = []
      this.selectedlist.forEach(item => {
        const sourceUid = item.id
        let found = false
        if (this.program != null) {
          if (this.program.layers != null) {
            if (this.program.layers[0].sources != null) {
              for (let i = 0; i < this.program.layers[0].sources.length; i++) {
                if (sourceUid == this.program.layers[0].sources[i].id) {
                  found = true
                }
              }
            }
          }
        }
        if (!found) {
          newPlaySourceUIds.push(sourceUid)
          newPlaySourceList.push(item)
        }
      })

      if (newPlaySourceUIds.length > 0) {
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
        if (row.id == this.program.layers[0].sources[index].id) {
          this.program.layers[0].sources.splice(index, 1)
        }
      }
      for (let index2 = 0; index2 < this.playSourceList.length; index2++) {
        if (row.id == this.playSourceList[index2].id) {
          this.playSourceList.splice(index2, 1)
        }
      }
    },
    handleCancel() {
      this.$router.push({ path: '/screen/program' })
    },
    handleConfirm() {
      // this.program.layers[0].sources = this.playSourceList
      console.log(this.program)
      this.$refs['program'].validate(valid => {
        if (valid) {
          updateSeniorProgramById(this.program).then(response => {
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
    getDefaultMultiText() {
      selectDefaultMultiText().then(response => {
        this.defaultMultiText = response.data.data
      }).catch(() => {
        this.defaultMultiText = undefined
      })
    },
    getDefaultWebURL() {
      selectDefaultWebURL().then(response => {
        this.defaultWebURL = response.data.data
      }).catch(() => {
        this.defaultWebURL = undefined
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
          const timeStr = this.secondToDate(i)
          html = '<div class="spotPackage" style="width:' + this.pubSecondWidth + 'px">' +
            '<div class="spotPackageTitle">' + timeStr + '</div>' +
            '<div class="spotPackageLine"></div>' +
            '</div>'
        } else {
          const timeStr = this.secondToDate(i)
          // 计算文字往左偏移量，一个数字偏移6px
          const offsetLeft = (i.toString().length) * 4.5
          html = '<div class="spotPackageAfter">' +
            '<div class="spotPackageTitleAfter" style="margin-left:-' + offsetLeft + 'px;">' + timeStr + '</div>' +
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
          console.log('本次删除', thar)
          smEL.remove()
          thar.remove()
          this.pubTimelineStorages.splice(i, 1)
        }
      }
      const currentTracklayer = thar.style.zIndex
      const sourceUid = thar.getAttribute('id')
      for (let j = 0; j < this.program.layers[currentTracklayer - 1].sources.length; j++) {
        if (sourceUid === this.program.layers[currentTracklayer - 1].sources[j].id) {
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
                // console.log('滑块位置', parseFloat(startFrame / this.pubFrame))
                // console.log('滑块结束位置', parseFloat((obj.stopX / (this.pubSecondWidth / this.pubFrame)) / this.pubFrame))
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
        // console.log("偏移",this.pubProgressBarRangePerTime,this.progressBarDOM.value)

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
      console.log(this.currentSlider)
      this.sourceDivVisiable = true
    },
    sourceChange() {
      console.log('改变资源')
      let currentTracklayer = this.currentSlider.style.zIndex
      if (currentTracklayer == null || currentTracklayer == '') {
        currentTracklayer = this.currentSlider.getAttribute('tracklayer')
      }
      if (this.currentSource != null && currentTracklayer != null) {
        for (let j = 0; j < this.program.layers[currentTracklayer - 1].sources.length; j++) {
          if (this.currentSource.id != null && this.currentSource.id === this.program.layers[currentTracklayer - 1].sources[j].id) {
            this.program.layers[currentTracklayer - 1].sources[j] = this.currentSource
          }
        }
      }
      // console.log(this.program)
    },
    // 当前资源同步，解决绑定值失效
    sourceSynchro(newPlaySource) {
      if (newPlaySource != null) {
        this.currentSource = newPlaySource
      }
      if (this.currentSource != null) {
        if (this.currentSource.name != null && document.getElementById('currentSourceName') != null) { document.getElementById('currentSourceName').value = this.currentSource.name }
        if (this.currentSource._type != null && document.getElementById('currentSourceType') != null) { document.getElementById('currentSourceType').value = this.currentSource._type }
        if (this.currentSource.left != null && document.getElementById('currentSourceLeft') != null) { document.getElementById('currentSourceLeft').value = this.currentSource.left }
        if (this.currentSource.top != null && document.getElementById('currentSourceTop') != null) { document.getElementById('currentSourceTop').value = this.currentSource.top }
        if (this.currentSource.width != null && document.getElementById('currentSourceWidth') != null) { document.getElementById('currentSourceWidth').value = this.currentSource.width }
        if (this.currentSource.height != null && document.getElementById('currentSourceHeight') != null) { document.getElementById('currentSourceHeight').value = this.currentSource.height }
        if (this.currentSource.playTime != null && document.getElementById('currentSourcePlayTime') != null) { document.getElementById('currentSourcePlayTime').value = this.currentSource.playTime }
        if (this.currentSource.timeSpan != null && document.getElementById('currentSourceTimeSpan') != null) { document.getElementById('currentSourceTimeSpan').value = this.currentSource.timeSpan }
        if (this.currentSource.entryEffectTimeSpan != null && document.getElementById('currentSourceEntryEffectTimeSpan') != null) { document.getElementById('currentSourceEntryEffectTimeSpan').value = this.currentSource.entryEffectTimeSpan }
        if (this.currentSource.exitEffectTimeSpan != null && document.getElementById('currentSourceExitEffectTimeSpan') != null) { document.getElementById('currentSourceExitEffectTimeSpan').value = this.currentSource.exitEffectTimeSpan }
        if (this.currentSource.url != null && document.getElementById('currentSourceUrl') != null) { document.getElementById('currentSourceUrl').value = this.currentSource.url }
        if (this.currentSource.speed != null && document.getElementById('currentSourceSpeed') != null) { document.getElementById('currentSourceSpeed').value = this.currentSource.speed }
        if (this.currentSource.lineHeight != null && document.getElementById('currentSourceLineHeight') != null) { document.getElementById('currentSourceLineHeight').value = this.currentSource.lineHeight }
        if (this.currentSource.center != null && document.getElementById('currentSoureCenter') != null) { document.getElementById('currentSoureCenter').value = this.currentSource.center }
        if (this.currentSource.backgroundColor != null && document.getElementById('currentSourceBackgroundColor') != null) { document.getElementById('currentSourceBackgroundColor').value = this.currentSource.backgroundColor }
        if (this.currentSource.html != null && document.getElementById('currentSourceHtml') != null) { document.getElementById('currentSourceHtml').value = this.currentSource.html }
      }
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
      const currentTracklayer = sliderParent.getAttribute('tracklayer')
      const sourceUid = this.currentSlider.getAttribute('id')
      const sourceId = this.currentSlider.getAttribute('sourceId')
      const smtype = this.currentSlider.getAttribute('smtype')
      // 素材库
      let newPlaySource// 未做
      if (sourceUid != null && sourceId != null) {
        for (let i = 0; i < this.pictureList.length; i++) {
          if (sourceId === this.pictureList[i].sourceId) {
            newPlaySource = this.pictureList[i]
            newPlaySource.id = sourceUid
          }
        }
        if (newPlaySource == null) {
          for (let i = 0; i < this.videoList.length; i++) {
            if (sourceId === this.videoList[i].sourceId) {
              newPlaySource = this.videoList[i]
              newPlaySource.id = sourceUid
            }
          }
        }
        if (newPlaySource == null) {
          if (smtype == 'MultiText') {
            newPlaySource = this.defaultMultiText
            newPlaySource.id = sourceUid
          }
        }
        if (newPlaySource == null) {
          if (smtype == 'WebURL') {
            newPlaySource = this.defaultWebURL
            newPlaySource.id = sourceUid
          }
        }
      }

      if (this.program.layers == null) {
        this.program.layers = [{}]
      }
      if (this.program.layers[currentTracklayer - 1] != null) {
        if (this.program.layers[currentTracklayer - 1].sources != null) {
          this.program.layers[currentTracklayer - 1].sources = this.program.layers[currentTracklayer - 1].sources.concat(newPlaySource)
        } else {
          this.program.layers[currentTracklayer - 1].sources = [newPlaySource]
        }
      }

      this.updateCurrentSliderRelevantData(ev)

      ev.preventDefault()

      const id = ev.dataTransfer.getData('id')
      const offsetX = (this.currentSliderBrowserX + parseInt(this.timeLinePackageDOM.scrollLeft)) - this.timeLinePackagePadddingLeftAndMarginLeft - this.currentSliderPressHoldOffsetX

      // 滑块操作处理（移动/拉长div）
      this.sliderOperationHandle(id, sliderParent, offsetX)
    },
    sliderOperationHandle(id, sliderParent, offsetX) {
      console.log('添加后', id)
      const thar = this
      const elObj = document.getElementById(id)
      // 克隆滑块
      console.log('克隆', elObj)
      if (elObj == null) { return }
      const elObjClone = elObj.cloneNode(true)
      elObjClone.setAttribute('id', this.createUuid(32, 16))
      elObj.parentNode.insertBefore(elObjClone, elObj.nextSibling)
      elObj.setAttribute('draggable', false)
      smtype = elObj.getAttribute('smtype')
      this.urlDivVisiable = false
      this.textDivVisiable = false
      if (smtype != null) {
        if (smtype == 'WebURL') {
          this.urlDivVisiable = true
        } else if (smtype == 'MultiText') {
          this.textDivVisiable = true
        }
      }

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
      elObj.onmousedown = (e) => {
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
        console.log('滑块down', elObj.offsetLeft, elObj.offsetWidth)
        this.updateSource(elObj)

        document.onmousemove = (e) => {
          e = e || event
          const xx = e.clientX
          // const yy = e.clientY
          if (positionType == 'right') {
            const width = oBoxW + xx - x
            // 验证是否小于最小宽度 和 是否在最大宽度范围内
            if (width > thar.pubSliderMinWidth && width + elObj.offsetLeft < thar.pubTotalWidth) {
              elObj.style.width = width + 'px'
              const pubSecondWidth = this.pubSecondWidth == null ? 40 : this.pubSecondWidth
              const playTime = parseInt(elObj.offsetLeft / pubSecondWidth)
              const timeSpan = parseInt(width / pubSecondWidth)
              const currentTracklayer = elObj.style.zIndex
              const sourceUid = elObj.getAttribute('id')
              if (sourceUid != null && currentTracklayer != null && currentTracklayer != '') {
                for (let j = 0; j < this.program.layers[currentTracklayer - 1].sources.length; j++) {
                  if (this.program.layers[currentTracklayer - 1].sources[j] != null) {
                    if (sourceUid === this.program.layers[currentTracklayer - 1].sources[j].id) {
                      this.currentSource = this.program.layers[currentTracklayer - 1].sources[j]
                    }
                  }
                }
              } else {
                this.currentSource = {}
              }
              this.currentSource.playTime = playTime
              this.currentSource.timeSpan = timeSpan
              this.sourceSynchro()
              // console.log("开始时间2",elObj.offsetLeft,playTime,timeSpan)

              thar.updatePubTimelineStoragesData(elObj)
            }
          }
          return false
        }

        document.onmouseup = () => {
          this.updateSource(elObj)
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
      let smtype = elObj.getAttribute('smtype')
      const smEL = document.createElement('div')
      smEL.setAttribute('class', 'pictureBlock')
      smEL.id = 'sm_' + id
      smEL.style.zIndex = sliderParent.getAttribute('tracklayer')
      smEL.setAttribute('tracklayer', sliderParent.getAttribute('tracklayer'))

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
        // img.setAttribute('id', 'sm_' + id)
        // img.setAttribute('tracklayer', sliderParent.getAttribute('tracklayer'))
        smEL.appendChild(img)
      } else if (smtype == 'MultiText') {
        const rollText = document.createElement('div')
        rollText.setAttribute('class', 'rollText')
        rollText.setAttribute('direction', elObj.getAttribute('direction'))
        rollText.innerText = elObj.getAttribute('text')
        smEL.appendChild(rollText)
      } else if (smtype == 'WebURL') {
        const rollText = document.createElement('div')
        rollText.setAttribute('class', 'rollText')
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
        console.log('画面', sliderParent)
        console.log(sliderParent.style.width, sliderParent.style.height)

        const currentTracklayer = smEL.getAttribute('tracklayer')
        let sourceUid = smEL.getAttribute('id')

        if (sourceUid.indexOf('sm_') != -1) {
          sourceUid = sourceUid.substring(3)
        }
        if (sourceUid != null && currentTracklayer != null && currentTracklayer != '') {
          for (let j = 0; j < this.program.layers[currentTracklayer - 1].sources.length; j++) {
            if (this.program.layers[currentTracklayer - 1].sources[j] != null) {
              if (sourceUid === this.program.layers[currentTracklayer - 1].sources[j].id) {
                this.currentSource = this.program.layers[currentTracklayer - 1].sources[j]
              }
            }
          }
        } else {
          this.currentSource = {}
        }
        this.sourceDivVisiable = true
        this.urlDivVisiable = false
        this.textDivVisiable = false
        if (this.currentSource != null && this.currentSource._type != null) {
          const smtype = this.currentSource._type
          if (smtype == 'WebURL') {
            this.urlDivVisiable = true
          } else if (smtype == 'MultiText') {
            this.textDivVisiable = true
          }
        }
        console.log('再测试', this.currentSource)
        this.sourceSynchro()

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
          this.sourceSynchro()
          // const sliderParent = smEL.querySelector(' div')
          const currentTracklayer = smEL.getAttribute('tracklayer')
          console.log(this.currentSource)
          console.log(this.program.layers[1])
          if (this.currentSource != null && currentTracklayer != null) {
            for (let j = 0; j < this.program.layers[currentTracklayer - 1].sources.length; j++) {
              if (this.currentSource.id === this.program.layers[currentTracklayer - 1].sources[j].id) {
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

        document.onmouseup = (e) => {
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
      console.log('ev.target.id', ev.target)
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
      if (this.program.layers != null) {
        console.log('模拟')
        console.log(this.program)
        this.addTime(400)
        // 轨道数
        const alltrackNum = this.program.layers.length
        for (let i = 0; i < alltrackNum - 1; i++) {
          this.addTrack()
        }
        const alreadySources = document.getElementById('alreadySources')
        let html = ''
        alreadySources.innerHTML = html
        console.log(this.program.layers)
        console.log(this.program.layers.length)
        for (let i = 0; i < this.program.layers.length; i++) {
          if (this.program.layers[i].sources != null) {
            for (let j = 0; j < this.program.layers[i].sources.length; j++) {
              const source = this.program.layers[i].sources[j]
              console.log('打印第' + i + '轨道第' + j + '个')
              console.log(source)
              let styleStr = ''
              const randomColor = this.createColor16()
              styleStr = 'text-align: left;background-color: ' + randomColor + ';'
              let nameFileExt
              if (source.fileExt != null && source.fileExt.length > 0) {
                nameFileExt = source.name + source.fileExt
              } else {
                nameFileExt = source.name
              }

              html = '<div id="' + source.id + '"' +
                  'ondblclick="deleteTrackSourceMaterial(event)" smurl="' + source.url + '" smtype="' + source._type + '"' +
                  'class="sliderBlock" draggable="false" onmouseup="mouseRelease()" style="' + styleStr + '" text ="' + source.html + '"' +
                  'onmousedown="unboundTrackOnMousedown(event)" ondragstart="drag(event)" >' + nameFileExt +
                  '</div>'
              console.log('网页', html)
              alreadySources.innerHTML += html

              const playTime = source.playTime
              const timeSpan = source.timeSpan

              const id = source.id
              const offsetX = (playTime / (this.pubMillisecondFrame / 1000)) * this.pubProgressBarRangePerTime
              const offsetY = this.pubSecondWidth * timeSpan
              console.log(offsetX)
              console.log(offsetY)
              const smELLeft = source.left
              const smELTop = source.top
              const smELWidth = source.width
              const smELHeight = source.height

              const tracks = document.getElementsByClassName('track')
              for (let m = 0; m < tracks.length; m++) {
                if (tracks[m].getAttribute('tracklayer') == i + 1) {
                  const myTrack = tracks[m]
                  const sliderParent = myTrack
                  this.sliderOperationHandle(id, sliderParent, offsetX)
                  setTimeout(() => {
                    // 滑块
                    const eL = document.getElementById(id)
                    console.log('打印滑块', eL)
                    eL.style.width = offsetY + 'px'
                    // 画布
                    const smEL = document.getElementById('sm_' + id)
                    smEL.style.left = smELLeft + 'px'
                    smEL.style.top = smELTop + 'px'
                    smEL.style.width = smELWidth + 'px'
                    smEL.style.height = smELHeight + 'px'

                    this.updatePubTimelineStoragesData(eL)
                  }, 500)
                }
              }
            }
          }
        }
      }
      // console.log('已存', alreadySources.innerHTML)
    },
    createColor16() {
      const r = Math.floor(Math.random() * 256)
      const g = Math.floor(Math.random() * 256)
      const b = Math.floor(Math.random() * 256)
      const color = '#' + r.toString(16) + g.toString(16) + b.toString(16)
      return color
    },
    createUuid(len, radix) {
      const chars = '0123456789abcdefghijklmnopqrstuvwxyz'.split('')
      const uuid = []; let i
      radix = radix || chars.length
      if (len) {
        for (i = 0; i < len; i++) uuid[i] = chars[0 | Math.random() * radix]
      } else {
        var r
        uuid[8] = uuid[13] = uuid[18] = uuid[23] = '-'
        uuid[14] = '4'
        for (i = 0; i < 36; i++) {
          if (!uuid[i]) {
            r = 0 | Math.random() * 16
            uuid[i] = chars[(i == 19) ? (r & 0x3) | 0x8 : r]
          }
        }
      }
      console.log('打印uuid', uuid.join(''))
      return uuid.join('')
    },
    updateSource(elObj) {
      this.sourceDivVisiable = true
      this.urlDivVisiable = false
      this.textDivVisiable = false
      // 同步滑块开始时间和持续时间
      const pubSecondWidth = this.pubSecondWidth == null ? 40 : this.pubSecondWidth
      const playTime = parseInt(elObj.offsetLeft / pubSecondWidth)
      const timeSpan = parseInt(elObj.offsetWidth / pubSecondWidth)
      const currentTracklayer = elObj.style.zIndex
      const sourceUid = elObj.getAttribute('id')
      const smtype = elObj.getAttribute('smtype')
      if (smtype == 'MultiText') {
        this.textDivVisiable = true
        if (this.currentSource == null || this.currentSource.id != sourceUid) {
          this.currentSource = { id: sourceUid, name: '多行文本', _type: 'MultiText' }
          console.log('新建')
        }
      }
      if (smtype == 'WebURL') {
        this.urlDivVisiable = true
        if (this.currentSource == null || this.currentSource.id != sourceUid) {
          this.currentSource = { id: sourceUid, name: '网址', _type: 'WebURL' }
          console.log('新建网址')
        }
      }
      if (sourceUid != null && currentTracklayer != null && currentTracklayer != '') {
        for (let j = 0; j < this.program.layers[currentTracklayer - 1].sources.length; j++) {
          if (this.program.layers[currentTracklayer - 1].sources[j] != null) {
            if (sourceUid === this.program.layers[currentTracklayer - 1].sources[j].id) {
              this.currentSource = this.program.layers[currentTracklayer - 1].sources[j]
            }
          }
        }
      } else {
        this.currentSource = {}
      }
      console.log('此次', this.currentSource)
      this.currentSource.playTime = playTime
      this.currentSource.timeSpan = timeSpan

      // 同步画布宽高
      if (sourceUid != null) {
        const smEL = document.getElementById('sm_' + sourceUid)
        if (smEL != null) {
          const img = smEL.querySelector(' div')
          console.log('同步画布宽高', this.currentSlider, smEL, img)
          let widthFlag = smEL.style.width
          let heightFlag
          let temp
          let smtype
          if (this.currentSlider != null) {
            smtype = this.currentSlider.getAttribute('smtype')
          } else if (this.currentSource != null) {
            smtype = this.currentSource._type
          }
          if (smtype == 'Image') {
            temp = smEL
            if (widthFlag != null && widthFlag.length > 0) {
              widthFlag = temp.style.width == '' ? 0 : parseInt(temp.style.width)
              heightFlag = temp.style.height == '' ? 0 : parseInt(temp.style.height)
            } else {
              widthFlag = this.program.width
              heightFlag = this.program.height
            }
          } else if (smtype == 'MultiText') {
            temp = smEL
            if (widthFlag != null && widthFlag.length > 0) {
              widthFlag = temp.style.width == '' ? 0 : parseInt(temp.style.width)
              heightFlag = temp.style.height == '' ? 0 : parseInt(temp.style.height)
            } else {
              widthFlag = this.program.width
              heightFlag = this.program.height
            }
          } else if (smtype == 'WebURL') {
            temp = smEL
            if (widthFlag != null && widthFlag.length > 0) {
              widthFlag = temp.style.width == '' ? 0 : parseInt(temp.style.width)
              heightFlag = temp.style.height == '' ? 0 : parseInt(temp.style.height)
            } else {
              widthFlag = this.program.width
              heightFlag = this.program.height
            }
          } else if (smtype == 'Video') {
            if (widthFlag != null && widthFlag.length > 0) {
              temp = smEL
              widthFlag = temp.style.width == '' ? 0 : parseInt(temp.style.width)
              heightFlag = temp.style.height == '' ? 0 : parseInt(temp.style.height)
            } else if (img != null) {
              temp = img
              const tempWidth = temp.style.width
              if (tempWidth == '100%') {
                widthFlag = this.program.width
                heightFlag = this.program.height
              } else {
                widthFlag = temp.style.width == '' ? 0 : parseInt(temp.style.width)
                heightFlag = temp.style.height == '' ? 0 : parseInt(temp.style.height)
              }
            }
          }
          if (temp != null) {
            const left = temp.style.left == '' ? 0 : parseInt(temp.style.left)
            const top = temp.style.top == '' ? 0 : parseInt(temp.style.top)
            const width = widthFlag
            const height = heightFlag
            console.log(left, top, width, height, temp.clientWidth, temp.clientHeight)
            if (sourceUid != null && currentTracklayer != null && currentTracklayer != '') {
              for (let j = 0; j < this.program.layers[currentTracklayer - 1].sources.length; j++) {
                if (this.program.layers[currentTracklayer - 1].sources[j] != null) {
                  if (sourceUid === this.program.layers[currentTracklayer - 1].sources[j].id) {
                    this.currentSource = this.program.layers[currentTracklayer - 1].sources[j]
                    this.currentSource.top = top
                    this.currentSource.left = left
                    this.currentSource.width = width
                    this.currentSource.height = height
                  }
                }
              }
            }
          }
        }
      }
      this.sourceSynchro()
    },
    secondToDate(msd) {
      const theTime = parseInt(msd)// 秒
      let result = '' + parseInt(theTime) + 's'
      if (theTime % 60 == 0) {
        const minute = parseInt(theTime / 60)
        result = '' + parseInt(minute) + '分'
      }
      if (theTime % 3600 == 0) {
        const hour = parseInt(theTime / 3600)
        result = '' + parseInt(hour) + '时'
      }
      return result
    },
    // 颜色选择器
    handleShowColor() {
      if (this.colorShow) {
        this.colorShow = false
      } else {
        this.colorShow = true
      }
    },
    // 将hex颜色转成rgb
    hexToRgba(hex, opacity) {
      const RGBA = 'rgba(' + parseInt('0x' + hex.slice(1, 3)) + ',' + parseInt('0x' + hex.slice(3, 5)) + ',' + parseInt('0x' + hex.slice(5, 7)) + ',' + opacity + ')'
      return RGBA
    },
    // 将rgb颜色转成hex
    colorRGB2Hex(color) {
      const rgb = color.split(',')
      const r = parseInt(rgb[0].split('(')[1])
      const g = parseInt(rgb[1])
      const b = parseInt(rgb[2].split(')')[0])

      const hex = '#' + ((1 << 24) + (r << 16) + (g << 8) + b).toString(16).slice(1).toUpperCase()
      return hex
    },
    updateValue(val) {
      this.color = 'rgba(' + val.rgba.r + ',' + val.rgba.g + ',' + val.rgba.b + ',' + val.rgba.a + ')'
      console.log('颜色选择', this.color)
      this.currentSource.backgroundColor = this.color
      console.log('this.currentSource', this.currentSource)
    }
  }
}
</script>
