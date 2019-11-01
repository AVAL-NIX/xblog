<@override name="middle">
<#--markdown-->
<#include "../common/mdHead.ftl">
<article>
    <h1 class="t_nav"><span>您现在的位置是：首页 > ${(article.title)!''}</span>
    </h1>
    <div class="infosbox">
        <div class="newsview">
            <h3 class="news_title">${(article.title)!''}</h3>
            <div class="bloginfo">
                <ul>
                    <li class="author"><a href="">${article.adminName}</a></li>
                    <li class="lmname"><a href="">${article.labels}</a></li>
                    <li class="timer">${article.createDate?date}</li>
                    <li class="view"><span>${(article.viewCount)!''}</span></li>
                    <li class="like" id="upCount" onclick="upCount()">${(article.upCount)!''}</li>
                </ul>
            </div>
            <#-- 内容主体 -->
            <div class="news_con" id="context">
                <textarea style="display:none;" name="test-editormd-markdown-doc">   ${(article.content?html)!''}</textarea>
            </div>
        </div>
        <div style="line-height:20px;height: 20px;"></div>
    </div>
    <!--右边-->
    <div class="sidebar">
    <#include "../common/right.ftl"/>
    </div>
</article>
</@override>
<@extends name="/front/common/main.ftl"></@extends>
<script type="text/javascript">
    testEditormdView2 = editormd.markdownToHTML("context", {
        htmlDecode      : "style,script,iframe",  // you can filter tags decode
        emoji           : true,
        taskList        : true,
        tex             : true,  // 默认不解析
        flowChart       : true,  // 默认不解析
        sequenceDiagram : true,  // 默认不解析
    });

    function upCount(){
        $.ajax({
            url:'${request.contextPath}/home/upCount',
            data:{
                "id":'${article.id}'
            },
            dataType:"json",
            type:"post",
            success:function(r){
                if(r.code>0){
                    $("#upCount").text(Number($("#upCount").text()) +1)
                }
            }
        })
    }
</script>