<template>
    <div>
         <transition name="fade">
        <Top v-show="topShow"></Top>
          </transition>
        <transition name="fade">
        <ArticleTop v-show="articleTopShow"></ArticleTop>
         </transition>
        <el-container>
            <el-main>
                <ArticleDetail></ArticleDetail>
            </el-main>
            <el-footer>
                <Backtop></Backtop>
            </el-footer>
        </el-container>
    </div>
</template>

<script>
    import ArticleDetail from '@/components/front/article/ArticleDetail'
    import Top from '@/components/front/Top'
    import ArticleTop from '@/components/front/article/ArticleTop'
    import Backtop from '@/components/front/Backtop'
    import {
        mapGetters,
        mapState,
        mapMutations
    } from 'vuex'
    export default {
        name: 'ArticleIndex',
        components: {
            ArticleDetail,
            Top,
            Backtop,
            ArticleTop
        },
        computed: {
            ...mapState({
                topShow: state => state.article.top,
                articleTopShow: state => state.article.articleTop,
            })
        },
    }
</script>

<style>
.fade-enter-active, .fade-leave-active {
  transition: opacity .5s;
}
.fade-enter, .fade-leave-to /* .fade-leave-active below version 2.1.8 */ {
  opacity: 0;
}
</style>