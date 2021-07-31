<template>
  <div>
    <div v-if="showTopic == 1">
      <van-button type="info" class="classButton" @click="random()">随机开始</van-button>
      <van-divider>刷题进度</van-divider>
      <van-progress :percentage="plan"  class="class90"/>
    </div>
  </div>

</template>
<script>

export default {
  data () {
    return {
      topicInfo: {
        allCount: 0,
        processCount: 0
      },
      topicList: [],
      showTopic: 1
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
    this.$api.admin.topicInfo({
      adminId: 1
    }).then(res => {
      this.topicInfo = res.data
    })
  },
  methods: {
    random () {
      this.$router.push('/topic/list')
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
