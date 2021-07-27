package com.blog.dao;

import com.blog.model.entity.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource(path = "label")
public interface LabelDao extends JpaRepository<Label, Long> {

    Label findByName(String name);
}
