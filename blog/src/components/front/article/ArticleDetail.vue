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
                    <el-card shadow="hover" style="margin:5px;position:fixed;width:16.6%" id="article-toc" ref="article-toc">
                    </el-card>
                </el-col>
            </el-row>
        </el-col>
    </div>
</template>
<script>
    import marked from 'marked'
    import Vue from 'vue/dist/vue.js'
    export default {
        name: 'ArticleDetail',
        data: function() {
            return {
                tocArr: [],
                tocDom: [],
                index: 0,
                content: '',
                toc: '',
                divHeights: [],
                item:{}
            }
        },
        created() {
        },
        mounted: function() {
            this.$get('home/article/'+this.$route.params.id).then(res=>{
                 this.$resultCheck(res.data, true, true).then(res => {
                    this.item = res.data
                    //数据解析
                    this.execDataAnalyze()
                }).catch(res => {})
            })
            //marked格式设置
            let renderer = new marked.Renderer();
            renderer.heading = (text, level, raw) => {
                let anchor = this.addItem(text, level);
                // anchor = anchor.replace("#","")
                return `<a id=#${anchor} class="anchor-fix"></a><h${level}>${text}</h${level}>`;
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

        },
        components: {},
        methods: {
            execDataAnalyze(){
                let content = marked(this.item.content);
                this.content = content.replace("[TOC]", "")
                this.toc = this.toHTML();
                let MyComponent = Vue.extend({
                    template: this.toc,
                    methods: {
                        addClass(e) {
                            $("li").removeClass("activti")
                            $(this.$refs[e]).parent("li").addClass("activti")
                        },
                    }
                });
                var component = new MyComponent().$mount();
                document.getElementById('article-toc').appendChild(component.$el);
                //启动监听
                window.addEventListener("scroll", this.handleScroll);
                for (let i = 0; i < this.tocDom.length; i++) {
                    this.divHeights.push($(this.tocDom[i]).offset().top);
                }
            },
            changeClass(id) {
                $("li").removeClass("activti")
                if (id) {
                    $(id).addClass("activti")
                }
            },
            handleScroll() {
                let scrollTop =
                    window.pageYOffset ||
                    document.documentElement.scrollTop ||
                    document.body.scrollTop
                let k;
                for (let i = 0; i < this.divHeights.length; i++) {
                    if (scrollTop > this.divHeights[i] - 50) {
                        this.changeClass(this.tocDom[i])
                        break
                    }
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
                    result += `<li><a id=${id}  href=${anchor}  @click="addClass('${anchor}')">${text}</a></li>`;
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
    .activti {
        background-color: #0050a3;
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