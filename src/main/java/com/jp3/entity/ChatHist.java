//チャットの履歴を格納するchat_historiesテーブルのエンティティ
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
@Table(name = "chat_histories")
public class ChatHist {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "chat_id")
	private Long chatId;//chatに与えるID、主キー
	
	@Column(name="user_id")
	private String userId;
	
	@Column(name="role")
	private String role;

	@Column(columnDefinition = "TEXT", name = "content")
	private String content;
	
	@Column(name = "created_at", updatable = false, insertable = false)
	private LocalDateTime createdAt;

	public ChatHist(String userId, String role, String content) {
	    this.userId = userId;
	    this.role = role;
	    this.content = content;
	}
}
