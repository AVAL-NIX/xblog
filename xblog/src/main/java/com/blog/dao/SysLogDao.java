package com.blog.dao;

import com.blog.model.entity.SysLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource(path = "sys-log")
public interface SysLogDao extends JpaRepository<SysLog, Long> {

}
