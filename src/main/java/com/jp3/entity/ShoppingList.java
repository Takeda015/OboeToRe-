//タスクのエンティティ
package com.jp3.entity;

import java.time.LocalDateTime;

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
@Table(name = "shopping_list")
public class ShoppingList {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "shopping_id")
	private Long shoppingId;

	@Column(name = "user_id", nullable = false)
	private String userId;

	//買うもの
	@Column(name = "item_name", nullable = false)
	private String itemName;

	//買い物説明
	@Column(name = "description", columnDefinition = "TEXT")
	private String description;

	//ステータス
	@Column(name = "status", nullable = false)
	private String status;
	
	//作った時間
	@Column(name = "created_at", updatable = false, insertable = false)
	private LocalDateTime createdAt;

	
	@Column(name = "updated_at", insertable = false, updatable = false)
	private LocalDateTime updatedAt;

	public ShoppingList(String userId, String itemName,String description) {
		this.userId = userId;
		this.itemName=itemName;
		this.description = description;
		this.status = "TODO";
	}
}
