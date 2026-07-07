//usersテーブルのエンティティ
package com.jp3.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@Table(name = "users")
public class User {
	
	public User(String userId, String password, String nickname, LocalDateTime lastLoginAt) {
		super();
		this.userId = userId;
		this.password = password;
		this.nickname = nickname;
		this.lastLoginAt = lastLoginAt;
	}

	@Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "password")
    private String password;  // DBにはハッシュ化した値を保存
    
    @Column(name = "nickname")
    private String nickname;  
    
    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;
    
    @Column(name = "created_at", updatable = false, insertable = false)
    private LocalDateTime createdAt;

    @Column(name = "reminder_cache", columnDefinition = "TEXT")
    private String reminderCache;

    //リマインダーの追加に伴って追加、あとでＳＱＬを変更しとく
    @Column(name = "reminder_generated_at")
    private LocalDateTime reminderGeneratedAt;

}
