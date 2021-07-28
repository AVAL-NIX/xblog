<template>
  <div id="app">
    <router-view/>
    <van-tabbar v-model="active" @change="change" v-if="$route.meta.showNav">
      <van-tabbar-item icon="home-o">刷题</van-tabbar-item>
      <van-tabbar-item icon="friends-o">我</van-tabbar-item>
    </van-tabbar>
  </div>
</template>

<script>
export default {
  name: 'App',
  data () {
    return {
      active: 1
    }
  },
  mounted () {
  },
  methods: {
    change (v) {
      switch (v) {
        case 0:
          this.$router.push('/topic')
          break
        case 1:
          this.$router.push('/user')
          break
      }
    }
  }
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
