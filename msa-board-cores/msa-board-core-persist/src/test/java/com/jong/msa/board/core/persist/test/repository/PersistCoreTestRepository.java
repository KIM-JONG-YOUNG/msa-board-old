package com.jong.msa.board.core.persist.test.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jong.msa.board.core.persist.test.entity.PersistCoreTestEntity;

@Repository
public interface PersistCoreTestRepository extends JpaRepository<PersistCoreTestEntity, UUID> {

}
