<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>

<style>
#app{
    background-color: rgb(246,246,246);
    height:100%;

}
.global_color{
    color: #409EFF!important;
}
.global_back_color{
    background-color: #409EFF!important;
}
a{
         text-decoration: none;
}
</style>
