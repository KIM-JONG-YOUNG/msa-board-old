package com.jong.msa.board.core.transaction.test.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.jong.msa.board.core.persist.entity.BaseEntity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Entity
@Builder
@ToString
@Table(name = "tb_test")
@Accessors(chain = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class TrasactionCoreTestEntity extends BaseEntity {

	@Setter
	@Builder.Default
	@Column(name = "test_count", nullable = false)
	private int count = 0;
	
}
