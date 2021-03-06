<!DOCTYPE html>
<html class="x-admin-sm">
<#include '../common/head.ftl'>
<body>
<div class="layui-fluid">
    <div class="layui-row">
        <form action="/back/role/save"  method="post" class="layui-form layui-form-pane">
            <div class="layui-form-item">
                <label for="name" class="layui-form-label">
                    <span class="x-red">*</span>角色名
                </label>
                <div class="layui-input-inline">
                    <input type="text" name="role" value="${(role.role)!''}" lay-verify="required" placeholder="请输入权限" autocomplete="off" class="layui-input">
                </div>
            </div>
            <div class="layui-form-item layui-form-text">
                <label class="layui-form-label">
                    拥有权限
                </label>
                <table  class="layui-table layui-input-block">
                    <tbody>
                       <#list permissionMap?values as value >
                        <tr>
                            <td>
                                <input type="checkbox" lay-skin="primary" lay-filter="father" title="${value[0].permissionTypeName}">
                            </td>
                            <td>
                                <div class="layui-input-block">
                                      <#list value as p >
                                          <input name="permissions" lay-filter="children"  <#if myPermissions?? && myPermissions?seq_contains(p.id)> checked </#if> lay-skin="primary" type="checkbox" title="${(p.description)!''}" value="${(p.id)!''}">
                                      </#list>
                                </div>
                            </td>
                        </tr>
                       </#list>
                    </tbody>
                </table>
            </div>
            <div class="layui-form-item layui-form-text">
                <label for="desc" class="layui-form-label">
                    描述
                </label>
                <div class="layui-input-block">
                    <textarea placeholder="请输入内容" id="description" name="description" class="layui-textarea">
                        ${(role.description)!''}
                    </textarea>
                </div>
            </div>
            <div class="layui-form-item">
                <button class="layui-btn" lay-submit="" lay-filter="save">增加</button>
            </div>
        </form>
    </div>
</div>
<script>
    layui.use(['form','layer'], function(){
        $ = layui.jquery;
        var form = layui.form
                ,layer = layui.layer;

        //监听提交
        form.on('submit(save)', function(data){
            var field = data.field;
            ids = []
            $("input:checkbox[name='permissions']:checked").each(function() {
                ids.push($(this).val())
            });
            field.permissions = ids.toString()

            $.ajax({
                url: "/back/role/edit",
                type: "POST",
                dataType: "json",
                data: field,
                success: function (data) {
                    xadmin.msg(data)
                }
            })
            return false;
        });


        form.on('checkbox(father)', function(data){
            if(data.elem.checked){
                $(data.elem).parent().siblings('td').find('input').prop("checked", true);
                form.render();
            }else{
                $(data.elem).parent().siblings('td').find('input').prop("checked", false);
                form.render();
            }
        });

        form.on('checkbox(children)', function(data){
            var list = $(data.elem).parent().find('input')
            var isCheck = true;
            $.each(list, function(i,e){
                if(!$(e).prop("checked")){
                    isCheck = false
                    return false;
                }
            });
            if(isCheck){
                $(data.elem).parent().parent().siblings('td').find('input').prop("checked", true);
                form.render();
            }else{
                $(data.elem).parent().parent().siblings('td').find('input').prop("checked", false);
                form.render();
            }
        });

        //看father节点是否是选中的
        $("input:checkbox[lay-filter='father']").each(function(i,e) {
            var isCheck = true;
            $.each($(e).parent().siblings('td').find('input'), function(i,obj){
                if(!$(obj).prop("checked")){
                    isCheck = false
                }
            });
            if(isCheck){
                $(e).prop("checked", true);
                form.render();
            }else{
                $(e).attr("checked", false);
                form.render();
            }

        });
    });


</script>
</body>
</html>
