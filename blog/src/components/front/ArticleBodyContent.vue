<template>
    <div>
        <el-col :span="9" :offset="5">
            <el-row :gutter="0">
                <el-col :span="24">
                    <el-card shadow="hover" style="margin:5px;" v-for="(i,index) in list" :key="index">
                        <el-row :gutter="8">
                            <el-col :span="24">
                                <router-link :to="{ name: 'ArticleIndex', params: { id: i.id}}">
                                    <h2 class="title-hide">{{i.title}}</h2>
                                </router-link>
                            </el-col>
                            <el-col :span="24" class="article-body">
                                {{i.intro}}
                            </el-col>
                            <el-col :span="24">
                                <el-row :gutter="8" class="article-bottom auto">
                                    <el-col>
                                        <i class="el-icon-user-solid"></i>{{i.adminName}}
                                    </el-col>
                                    <el-col>
                                        <i class="el-icon-s-operation"></i>{{i.labels}}
                                    </el-col>
                                    <el-col :span="5">
                                        <i class="el-icon-time"></i>{{i.createDate}}
                                    </el-col>
                                    <el-col :span="4">
                                        <i class="el-icon-view"></i>{{i.viewCount}}
                                    </el-col>
                                    <el-col :span="4">
                                        <i class="el-icon-star-on"></i>{{i.upCount}}
                                    </el-col>
                                </el-row>
                            </el-col>
                        </el-row>
                    </el-card>
                </el-col>
                <el-col :span="24" style="text-align: center;margin-top:10px">
                    <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="page" :page-sizes="[10, 20, 30, 40]" :page-size="size" layout="total, sizes, prev, pager, next, jumper" :total="total">
                    </el-pagination>
                </el-col>
            </el-row>
        </el-col>
    </div>
</template>

<script>
    export default {
        name: 'ArtileBody',
        methods: {
            handleSizeChange(val) {
                this.size = val
                this.getData()
            },
            handleCurrentChange(val) {
                this.page = val
                this.getData()
            },
            getData() {
                this.$get("home/article", {
                    page: this.page,
                    size: this.size
                }).then(res => {
                    this.$resultCheck(res.data, true, true).then(res => {
                        this.list = res.data.records
                        this.total = res.data.total
                    }).catch(res => {})
                })
            }
        },
        created() {
            //获取数据
            this.getData()
        },
        data() {
            return {
                page: 1,
                size: 10,
                total:1,
                list: []
            };
        },
    }
</script>

<style>
    .article-body {
        height: 88px;
        color: #566573;
        overflow: hidden;
    }
    .article-bottom {
        color: #748594;
        overflow: hidden;
    }
    .article-bottom i {
        margin: 0px 7px;
    }
    .article-bottom.auto div {
        width: auto;
        font-size: 13px;
    }
    a {
        color: #000;
        text-decoration: none;
    }
    .title-hide{
        overflow: hidden;
        text-overflow: ellipsis;
        white-space:nowrap;
    }
</style>