package com.blog.dao;

import com.blog.model.entity.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;


@RepositoryRestResource(path = "label")
public interface LabelDao extends JpaRepository<Label, Long> {

    Label findByName(String name);

    @Query(nativeQuery = true, value = " select * from label where channel_id = ?1 ")
    List<Label> listTopicType(Long channelId);

}
