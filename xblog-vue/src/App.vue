<template>
  <div id="app">
    <router-view/>
    <van-tabbar v-model="active" @change="change" v-if="$route.meta.showNav">
      <van-tabbar-item icon="home-o" name="0">刷题</van-tabbar-item>
    </van-tabbar>
  </div>
</template>

<script>
export default {
  name: 'App',
  data () {
    return {
      active: 0
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

</style>
