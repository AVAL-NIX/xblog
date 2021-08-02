<template>
<div>
    <van-nav-bar
      left-text="返回"
      left-arrow
      @click-left="onClickLeft"
    />
    <div  v-if="topicList.length != 0">
      <div>
        <van-steps :active="index" class="van-steps">
          <van-step v-for="(item,index) in topicList" :key="index">{{index + 1}}</van-step>
        </van-steps>
      </div>
      <div>
          <van-divider />
         <div style="font-size:19px;width:90%;margin:5%;"> {{item.title }}</div>
          <van-collapse v-model="activeNames">
          <van-collapse-item name="1" >
            <template #title>
              <div> {{item.intro }}</div>
            </template>
            <div v-html="content" class="markdown"></div>
          </van-collapse-item>
        </van-collapse>
        <div style="height:20vh;width:100%"></div>
      </div>
      <div class="footer">
          <van-button type="info" @click="submit(1)">不在出现</van-button>
          <van-button type="info" @click="submit(2)">继续复习</van-button>
      </div>
    </div>
    <div v-if="topicList.length == 0">
        <van-empty description="做完了哦" />
    </div>
</div>

</template>
<script>
import marked from 'marked'
import '@/assets/markdown.css'
import {getUserinfo} from '@/util/auth'
import { Dialog } from 'vant'
export default {
  data () {
    return {
      index: 0,
      item: {},
      activeNames: ['0'],
      topicList: [],
      showPopup: false,
      admin: {}, // 当前登录用户
      content: ''// 主体内容
    }
  },
  watch: {
    index () {
      this.execDataAnalyze()
    }
  },
  computed: {
    plan () {
      if (this.topicInfo.allCount === 0) {
        return 0
      }
      return this.topicInfo.processCount / this.topicInfo.allCount * 100
    }
  },
  mounted () {
    // 设置markdown属性
    let renderer = new marked.Renderer()
    marked.setOptions({
      renderer: renderer,
      gfm: true,
      tables: true,
      breaks: false,
      pedantic: false,
      sanitize: false,
      smartLists: true,
      smartypants: false
    })
    // 解析数据
    this.random()
  },
  methods: {
    random () {
      this.admin = getUserinfo()
      this.$api.admin.random({
        adminId: this.admin.id,
        label: '',
        count: 10
      }).then(res => {
        if (!res.data || res.data.length === 0) {
          this.$message('没有题目')
        } else {
          this.topicList = res.data
          this.execDataAnalyze()
        }
      })
    },
    execDataAnalyze () {
      if (this.index === this.topicList.length) {
        return
      }
      this.item = this.topicList[this.index]
      let content = marked(this.item.content)
      this.content = content.replace('[TOC]', '')
    },
    onClickLeft () {
      this.$router.push('/topic')
    },
    async submit (type) {
      // 拦截
      if (this.item == null || !this.item.id) {
        return
      }
      if (this.index === (this.topicList.length)) {
        Dialog.confirm({
          message: '已经是最后一题了，点击返回',
          showCancelButton: false
        }).then(res => {
          this.$router.push('/topic')
        })
        return
      }
      // 处理逻辑
      if (type === 1) {
        await this.$api.articleAdmin.save({
          adminId: this.admin.id,
          articleId: this.item.id
        }).then(res => {
          this.$message('恭喜又秒杀一题')
        })
      } else if (type === 2) {
        console.log(' 什么也不做 ')
      }
      this.index++
    }
  }
}
</script>

<style lang="scss" scoped >
.van-steps{
  padding:15px;
  margin: 0px 15px 0px 15px;
}
/deep/ .van-collapse-item__content{
  padding: 0px 10px 0px 10px !important;
}
.footer{
  position: fixed;
  bottom: 10px;
  width: 90%;
  margin:5%;

}
.footer button{
  width:49%;
}
</style>
