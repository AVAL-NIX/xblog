<template>
  <div>
    <div v-if="showTopic == 1">
      <van-button type="info" class="classButton" @click="random()">随机开始</van-button>
      <van-divider>刷题进度</van-divider>
      <van-progress :percentage="plan"  class="class90"/>
         <van-button type="info" class="classButton" @click="clearSubmit()">清除所有已做</van-button>
    </div>
  </div>

</template>
<script>
import {getUserinfo} from '@/util/auth'
export default {
  data () {
    return {
      topicInfo: {
        allCount: 0,
        processCount: 0
      },
      topicList: [],
      showTopic: 1,
      adminId: 0
    }
  },
  computed: {
    plan () {
      if (this.topicInfo.allCount === 0) {
        return 0
      }
      return (this.topicInfo.processCount / this.topicInfo.allCount * 100).toFixed(0)
    }
  },
  mounted () {
    this.adminId = getUserinfo().id
    this.$api.admin.topicInfo({
      adminId: this.adminId
    }).then(res => {
      this.topicInfo = res.data
    })
  },
  methods: {
    random () {
      this.$router.push('/topic/list')
    },
    clearSubmit () {
      this.$api.articleAdmin.delAll({
        adminId: this.adminId
      }).then(res => {
        this.$message('清除成功！')
      })
    }
  }
}
</script>

<style scoped >
.class90{
  width:80%;
  margin:10%;
  text-align: center;
}
.classButton{
  width:80%;
  margin:10%;
  text-align: center;
}
</style>
