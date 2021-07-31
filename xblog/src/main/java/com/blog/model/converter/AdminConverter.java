package com.blog.model.converter;

import com.blog.model.dto.response.AdminDTO;
import com.blog.model.entity.Admin;
import org.springframework.beans.BeanUtils;

public class AdminConverter {


    public static AdminDTO objToDTO(Admin admin) {
        AdminDTO adminDTO = new AdminDTO();
        BeanUtils.copyProperties(admin, adminDTO);
        return adminDTO;
    }


}
