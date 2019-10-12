<template>
    <div class="top">
        <div class="top-div">
            <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="handleSelect">
                <el-menu-item index="1" style="width:100%; text-align:center;"> {{this.title}}</el-menu-item>
            </el-menu>
        </div>
    </div>
</template>

<script>
    import {
        mapGetters,
        mapState,
        mapMutations
    } from 'vuex'
    export default {
        data() {
            return {
                activeIndex: '1',
            };
        },
        computed: {
            ...mapState({
                title: state => state.article.title
            })
        },
        methods: {
            handleSelect(key, keyPath) {
            },
            onSubmit(){

            }
        }
    }
</script>

<style>
.top{
    width: 100%;
    background: #fff;
    position:fixed;
    z-index: 1000;
    margin: 0px;
    padding: 0px;
    top:0px;
}
.top-title{
    float: left;
}
.top-div{
    width: 60%;
    margin:auto;
}
</style>