package com.blog.model.converter;

import com.blog.model.entity.Role;
import com.blog.model.form.RoleForm;
import org.springframework.beans.BeanUtils;


/**
 * 转换类
 * 
 * @author zx
 * @date 2019/4/24
 */
public class RoleConverter
{

    public static Role formToObj(RoleForm roleForm) {
        Role role = new Role();
        BeanUtils.copyProperties(roleForm, role);
        return role;
    }

    public static Role formToObj(RoleForm roleForm, Role role) {
        BeanUtils.copyProperties(roleForm, role);
        return role;
    }
}
