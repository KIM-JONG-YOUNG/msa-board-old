package com.jong.msa.board.core.persist.test.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.jong.msa.board.common.enums.CodeEnum.Gender;
import com.jong.msa.board.common.enums.CodeEnum.Group;
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
public class PersistCoreTestEntity extends BaseEntity {

	@Setter
	@Column(name = "test_gender", length = 1, nullable = false)
	private Gender gender;
	
	@Setter
	@Column(name = "test_group", length = 1, nullable = false)
	private Group group;

	@Override
	public PersistCoreTestEntity setAuditable(boolean auditable) {

		return (PersistCoreTestEntity) super.setAuditable(auditable);
	}
	
}
