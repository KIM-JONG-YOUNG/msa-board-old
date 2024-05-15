package com.jong.msa.board.core.transaction.test.service;

import java.util.UUID;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jong.msa.board.core.transaction.aspect.DistributeTransaction;
import com.jong.msa.board.core.transaction.test.entity.TrasactionCoreTestEntity;
import com.jong.msa.board.core.transaction.test.repository.TrasactionCoreTestRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TrasactionCoreTestService {

	private final TrasactionCoreTestRepository repository;
	
	@Transactional
	public UUID create() {
		
		return repository.save(TrasactionCoreTestEntity.builder().build()).getId();
	}

	@DistributeTransaction(prefix = "test::", key = "#id")
	public void increaseCount(UUID id) {
		
		TrasactionCoreTestEntity entity = repository.findById(id).orElseThrow(EntityNotFoundException::new);
		
		repository.save(entity.setCount(entity.getCount() + 1));
	}

	@Transactional(readOnly = true)
	public int getCount(UUID id) {
		
		return repository.findById(id).orElseThrow(EntityNotFoundException::new).getCount();
	}

	
}
