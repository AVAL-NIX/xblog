package com.blog.dao;

import com.blog.model.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource(path = "admin")
public interface AdminDao extends JpaRepository<Admin, Long> {

    Admin findBySubmitToken(String submitToken);

    Admin findByUsername(String username);
}
