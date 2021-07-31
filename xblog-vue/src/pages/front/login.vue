<template>
  <div>
    <van-form @submit="onSubmit">
      <van-field
        v-model="username"
        name="用户名"
        label="用户名"
        placeholder="用户名"
        :rules="[{ required: true, message: '请填写用户名' }]"
      />
      <van-field
        v-model="password"
        type="password"
        name="密码"
        label="密码"
        placeholder="密码"
        :rules="[{ required: true, message: '请填写密码' }]"
      />
      <div style="margin: 16px;">
        <van-button round block type="info" native-type="submit">提交</van-button>
      </div>
    </van-form>
  </div>
</template>
<script>
import {setToken, setUserinfo} from '@/util/auth'

export default {
  data () {
    return {
      username: null,
      password: null,
      redirect: undefined,
      otherQuery: {},
      logo: require('../../../static/logo.png')
    }
  },
  watch: {
    $route: {
      handler: function (route) {
        this.redirect = route.query && route.query.redirect
        this.otherQuery = route.query
      },
      immediate: true
    }
  },
  methods: {
    onSubmit () {
      this.$api.admin.login({
        username: this.username,
        password: this.password
      }).then(res => {
        let user = res.data
        setToken(user.token)
        setUserinfo(user)
        this.$router.push({path: this.redirect || '/', query: this.otherQuery})
      })
    }
  }
}
</script>

<style>

</style>
