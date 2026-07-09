//サブスクのエンティティ
package com.jp3.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "subsk")
public class Subsk {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "subsk_id")
	private Long subskId;

	@Column(name = "subsk_name", nullable = false)
	private String subskName;

	@Column(name = "user_id", nullable = false)
	private String userId;

	@Column(name = "joined_at", nullable = false)
	private LocalDate joinedAt;

	@Column(name = "update_at", nullable = false)
	private LocalDate updateAt;

	@Column(name = "update_days")
	private Integer updateDays;

	public Subsk(String userId, String subskName, LocalDate joinedAt, LocalDate updateAt, Integer updateDays) {
		this.userId = userId;
		this.subskName = subskName;
		this.joinedAt = joinedAt;
		this.updateAt = updateAt;
		this.updateDays = updateDays;
	}
}
