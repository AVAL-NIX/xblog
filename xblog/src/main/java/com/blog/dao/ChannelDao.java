package com.blog.dao;

import com.blog.model.entity.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource(path="channel")
public interface ChannelDao extends JpaRepository<Channel, Long> {

    Channel findByName(String name);
}
