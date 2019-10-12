<template>
    <div class="article-detail">
        <el-col :span="10" :offset="5">
            <el-row :gutter="0">
                <el-col :span="24">
                    <el-card shadow="hover" v-html="content" style="margin:5px;padding:15px;">
                    </el-card>
                </el-col>
            </el-row>
        </el-col>
        <el-col :span="4">
            <el-row :gutter="0">
                <el-col :span="24">
                    <el-card shadow="hover" style="margin:5px;position:fixed;width:16.6%;padding:0px;" id="article-toc" class="article-toc" ref="article-toc">
                        <div class="highlight-title" id="hightline-div" style=""></div>
                        <h1 style="cursor: initial;padding-left: 15px;background-color: #FFF;    font-weight: bold;    border: none;    padding: 8px 12px;    font-size: 16px;">目录</h1>
                        <div id="article-mulu" class="article-mulu">
                        </div>
                    </el-card>
                </el-col>
            </el-row>
        </el-col>
    </div>
</template>
<script>
    import marked from 'marked'
    import Vue from 'vue/dist/vue.js'
    import {
        mapGetters,
        mapState,
        mapMutations
    } from 'vuex'
    export default {
        name: 'ArticleDetail',
        data: function() {
            return {
                tocArr: [],
                tocDom: [],
                index: 0,
                content: '',
                toc: '',
                //内容的div高度
                divHeights: [],
                item: {},
                //标记改变没
                make:true
            }
        },
        computed: {
               ...mapState({
                topShow: state => state.article.top,
                articleTopShow: state => state.article.articleTop,
                title: state => state.article.title,
            })
        },
        created: function() {
        },
        mounted: function() {
            let renderer = new marked.Renderer();
            renderer.heading = (text, level, raw) => {
                let anchor = this.addItem(text, level);
                anchor = anchor.replace("#", "")
                return `<a id=${anchor} class="anchor-fix"></a><h${level}>${text}</h${level}>`;
            };
            marked.setOptions({
                renderer: renderer,
                gfm: true,
                tables: true,
                breaks: false,
                pedantic: false,
                sanitize: false,
                smartLists: true,
                smartypants: false
            });
            this.$get('home/article/' + this.$route.params.id).then(res => {
                this.$resultCheck(res.data, true, true).then(res => {
                    this.item = res.data
                    //数据解析
                    this.execDataAnalyze()
                }).catch(res => {})
            })
            //启动监听
            window.addEventListener("scroll", this.handleScroll);
        },
        components: {},
        methods: {
            ...mapMutations({
                changeTop: 'changeTop',
                setTitle: 'setTitle' //
            }),
            //数据分析
            execDataAnalyze() {
                this.setTitle(this.item.title)
                let content = marked(this.item.content);
                this.title = this.item.title;
                console.log("this", this.title)
                this.content = content.replace("[TOC]", "")
                this.toc = this.toHTML();
                //TOC Vue对象
                let MyComponent = Vue.extend({
                    template: this.toc,
                    methods: {
                        //点击添加样式
                        addClass(e) {
                            let dom = $(this.$refs[e]);
                            if (dom) {
                                dom.parents("#article-mulu").find("li").removeClass("set_color")
                                dom.parent("li").addClass("set_color")
                                $("#hightline-div").css("display", "block")
                                let top = dom.position().top;
                                $("#hightline-div").css("top", top + 68)
                            }
                        },
                    }
                });
                var component = new MyComponent().$mount();
                document.getElementById('article-mulu').appendChild(component.$el);
            },
            //滚动添加样式
            changeClass(id) {
                id = id.replace("#", "")
                if (id) {
                    this.changeClassByDom($("#TOC" + id))
                }
            },
            //dom = a 标签
            changeClassByDom(dom) {
                if (dom) {
                    $("#hightline-div").css("display", "block")
                    let top = dom.position().top;
                    $("#hightline-div").css("top", top + 68)
                    dom.parents("#article-mulu").find("li").removeClass("set_color")
                    dom.parent("li").addClass("set_color")
                }
            },
            handleScroll() {

                this.divHeights = []
                for (let i = 0; i < this.tocDom.length; i++) {
                    this.divHeights.push($(this.tocDom[i]).offset().top);
                }
                let scrollTop =
                    window.pageYOffset ||
                    document.documentElement.scrollTop ||
                    document.body.scrollTop
                let k;
                for (let i = this.divHeights.length - 1; i >= 0; i--) {
                    console.log("当前高度", scrollTop, " div高度", this.divHeights[i] - 50, this.tocDom[i])
                    if (scrollTop > this.divHeights[i] - 50) {
                        this.changeClass(this.tocDom[i])
                        break
                    }
                }
                if(scrollTop > 100 &&this.make){
                    this.changeTop()
                    this.make = false
                }else if(scrollTop <= 100 &&! this.make){
                     this.changeTop()
                    this.make = true
                }
            },
            addItem(text, level) {
                var anchor = `#toc${level}toc${++this.index}`;
                this.tocArr.push({
                    anchor: anchor,
                    level: level,
                    text: text
                });
                this.tocDom.push(
                    anchor
                )
                return anchor;
            },
            toHTML() {
                let levelStack = [];
                let result = '';
                const addStartUL = () => {
                    result += '<ul>';
                };
                const addEndUL = () => {
                    result += '</ul>';
                };
                const addLI = (anchor, text) => {
                    let id = anchor.replace("#", "")
                    result += `<li><a id='TOC${id}' class="title-hide" href=${anchor} ref=${anchor} @click="addClass('${anchor}')">${text}</a></li>`;
                };
                this.tocArr.forEach(function(item) {
                    let levelIndex = levelStack.indexOf(item.level);
                    // 没有找到相应level的ul标签，则将li放入新增的ul中
                    if (levelIndex === -1) {
                        levelStack.unshift(item.level);
                        addStartUL();
                        addLI(item.anchor, item.text);
                    } // 找到了相应level的ul标签，并且在栈顶的位置则直接将li放在此ul下
                    else if (levelIndex === 0) {
                        addLI(item.anchor, item.text);
                    } // 找到了相应level的ul标签，但是不在栈顶位置，需要将之前的所有level出栈并且打上闭合标签，最后新增li
                    else {
                        while (levelIndex--) {
                            levelStack.shift();
                            addEndUL();
                        }
                        addLI(item.anchor, item.text);
                    }
                });
                // 如果栈中还有level，全部出栈打上闭合标签
                while (levelStack.length) {
                    levelStack.shift();
                    addEndUL();
                }
                return result;
            }
        },
        destroyed() {
            window.removeEventListener('scroll', this.handleScroll);
        }
    }
</script>

<style>
    .article-detail {
        height: 100vh;
        width: 100%;
        margin-top: 35px;
    }
    .el-card__body {
        padding: 0px;
    }
    #article-mulu {
        position: relative;
        z-index: 2;
    }
    .article-toc ul li {
        padding-left: 5px;
        margin: 0;
        list-style-type: square;
    }
    .article-toc ul li a {
        display: block;
        padding: 3px 0;
        color: #000;
        text-decoration: none;
        z-index: 2;
    }
    #hightline-div {
        z-index: 1;
        display: none;
        top: 0px;
        height: 27px;
        position: absolute;
        left: 0;
        right: 0;
        background: #f3f3f3;
        border-left: 2px solid #409EFF;
        transition: all .2s ease;
        opacity: 1;
    }
    .set_color a {
        color: #409EFF!important;
    }
    .set_color {
        color: #409EFF!important;
        transition: all .2s ease;
    }
    .title-hide {
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
    }
    /* h1,
                                        h2,
                                        h3,
                                        h4,
                                        h5,
                                        h6,
                                        p,
                                        blockquote {
                                            margin: 0;
                                            padding: 0;
                                        }
                                        body {
                                            font-family: "Helvetica Neue", Helvetica, "Hiragino Sans GB", Arial, sans-serif;
                                            font-size: 13px;
                                            line-height: 18px;
                                            color: #737373;
                                            background-color: white;
                                            margin: 10px 13px 10px 13px;
                                        }
                                        table {
                                            margin: 10px 0 15px 0;
                                            border-collapse: collapse;
                                        }
                                        td,
                                        th {
                                            border: 1px solid #ddd;
                                            padding: 3px 10px;
                                        }
                                        th {
                                            padding: 5px 10px;
                                        }
                                        a {
                                            color: #0069d6;
                                        }
                                        a:hover {
                                            color: #0050a3;
                                            text-decoration: none;
                                        }
                                        a img {
                                            border: none;
                                        }
                                        p {
                                            margin-bottom: 9px;
                                        }
                                        h1,
                                        h2,
                                        h3,
                                        h4,
                                        h5,
                                        h6 {
                                            color: #404040;
                                            line-height: 36px;
                                        }
                                        h1 {
                                            margin-bottom: 18px;
                                            font-size: 30px;
                                        }
                                        h2 {
                                            font-size: 24px;
                                        }
                                        h3 {
                                            font-size: 18px;
                                        }
                                        h4 {
                                            font-size: 16px;
                                        }
                                        h5 {
                                            font-size: 14px;
                                        }
                                        h6 {
                                            font-size: 13px;
                                        }
                                        hr {
                                            margin: 0 0 19px;
                                            border: 0;
                                            border-bottom: 1px solid #ccc;
                                        }
                                        blockquote {
                                            padding: 13px 13px 21px 15px;
                                            margin-bottom: 18px;
                                            font-family: georgia, serif;
                                            font-style: italic;
                                        }
                                        blockquote:before {
                                            content: "\201C";
                                            font-size: 40px;
                                            margin-left: -10px;
                                            font-family: georgia, serif;
                                            color: #eee;
                                        }
                                        blockquote p {
                                            font-size: 14px;
                                            font-weight: 300;
                                            line-height: 18px;
                                            margin-bottom: 0;
                                            font-style: italic;
                                        }
                                        code,
                                        pre {
                                            font-family: Monaco, Andale Mono, Courier New, monospace;
                                        }
                                        code {
                                            background-color: #fee9cc;
                                            color: rgba(0, 0, 0, 0.75);
                                            padding: 1px 3px;
                                            font-size: 12px;
                                            -webkit-border-radius: 3px;
                                            -moz-border-radius: 3px;
                                            border-radius: 3px;
                                        }
                                        pre {
                                            display: block;
                                            padding: 14px;
                                            margin: 0 0 18px;
                                            line-height: 16px;
                                            font-size: 11px;
                                            border: 1px solid #d9d9d9;
                                            white-space: pre-wrap;
                                            word-wrap: break-word;
                                        }
                                        pre code {
                                            background-color: #fff;
                                            color: #737373;
                                            font-size: 11px;
                                            padding: 0;
                                        }
                                        sup {
                                            font-size: 0.83em;
                                            vertical-align: super;
                                            line-height: 0;
                                        }
                                        * {
                                            -webkit-print-color-adjust: exact;
                                        }
                                        @media screen and (min-width: 914px) {
                                            body {
                                                width: 854px;
                                                margin: 10px auto;
                                            }
                                        }
                                        @media print {
                                            body,
                                            code,
                                            pre code,
                                            h1,
                                            h2,
                                            h3,
                                            h4,
                                            h5,
                                            h6 {
                                                color: black;
                                            }
                                            table,
                                            pre {
                                                page-break-inside: avoid;
                                            }
                                        } */
</style>