package com.jong.msa.board.domain.post.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jong.msa.board.domain.post.entity.PostEntity;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, UUID> {

}
