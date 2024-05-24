package com.jong.msa.board.domain.member.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.jong.msa.board.common.enums.CodeEnum.Gender;
import com.jong.msa.board.common.enums.CodeEnum.Group;
import com.jong.msa.board.common.enums.CodeEnum.State;
import com.jong.msa.board.core.persist.entity.BaseEntity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Entity
@Getter
@Builder
@Table(name = "tb_member")
@Accessors(chain = true)
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED) 
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberEntity extends BaseEntity {

	@Column(name = "member_username", length = 30, nullable = false, unique = true)
	private String username;

	@Setter
	@Column(name = "member_password", length = 60, nullable = false)
	private String password;

	@Setter
	@Column(name = "member_name", length = 30, nullable = false)
	private String name;

	@Setter
	@Column(name = "member_gender", length = 1, nullable = false)
	private Gender gender;

	@Setter
	@Column(name = "member_email", length = 60, nullable = false)
	private String email;

	@Setter
	@Column(name = "member_group", length = 1, nullable = false)
	private Group group;

	@Override
	public MemberEntity setAuditable(boolean auditable) {
		
		return (MemberEntity) super.setAuditable(auditable);
	}

	@Override
	public MemberEntity setState(State state) {

		return (MemberEntity) super.setState(state);
	}
	
}
