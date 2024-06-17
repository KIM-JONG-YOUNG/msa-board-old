package com.jong.msa.board.domain.post.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.jong.msa.board.domain.core.entity.BaseEntity;

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
@Accessors(chain = true)
@ToString(callSuper = true)
@Table(name = "tb_post")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PostEntity extends BaseEntity {

	@Setter
	@Column(name = "post_title", length = 300, nullable = false)
	private String title;

	@Setter
	@Column(name = "post_content", columnDefinition = "LONGTEXT", nullable = false)
	private String content;

	@Column(name = "post_writer_id", columnDefinition = "BINARY(16)", nullable = false)
	private UUID writerId;
	
	@Setter
	@Builder.Default
	@Column(name = "post_views", nullable = false)
	private int views = 0;

	@Override
	public PostEntity setAuditable(boolean auditable) {

		return (PostEntity) super.setAuditable(auditable);
	}

	@PrePersist
	public void prePersist() {

		views = (views < 0) ? 0 : views;
		
		super.prePersist();
	}

	@PreUpdate
	public void preUpdate() {

		views = (views < 0) ? 0 : views;
		
		super.preUpdate();
	}

}
