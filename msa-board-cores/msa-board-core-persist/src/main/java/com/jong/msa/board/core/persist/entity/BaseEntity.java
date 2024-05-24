package com.jong.msa.board.core.persist.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;

import com.jong.msa.board.common.enums.CodeEnum.State;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@ToString
@MappedSuperclass
@Accessors(chain = true)
public abstract class BaseEntity {

	@Id
	@Column(name = "id", columnDefinition = "BINARY(16)", nullable = false)
	private UUID id = UUID.randomUUID();

	@Column(name = "created_date_time", nullable = false)
	private LocalDateTime createdDateTime;

	@Column(name = "updated_date_time", nullable = false)
	private LocalDateTime updatedDateTime;

	@Setter
	@Column(name = "state", length = 1, nullable = false)
	private State state = State.ACTIVE;
	
	@Setter
	@Transient
	private boolean auditable = true;

	@PrePersist
	public void prePersist() {

		createdDateTime = (createdDateTime == null) ? LocalDateTime.now() : createdDateTime;
		updatedDateTime = (updatedDateTime == null) ? LocalDateTime.now() : updatedDateTime;
	}

	@PreUpdate
	public void preUpdate() {
		
		updatedDateTime = (auditable) ? LocalDateTime.now() : updatedDateTime;
	}

}
