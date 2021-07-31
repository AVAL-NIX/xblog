<template>
  <div>
     {{admin.username}}
  </div>
</template>
<script>
import {getUserinfo} from '@/util/auth'
export default {
  data () {
    return {
      admin: {

      }
    }
  },
  mounted () {
    this.admin = getUserinfo()
  },
  methods: {}
}
</script>

<style>

</style>
