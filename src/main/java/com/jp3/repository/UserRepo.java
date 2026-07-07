// ユーザー登録・呼び名変更・パスワード変更・最終ログイン更新のリポジトリ
package com.jp3.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jp3.entity.User;


@Repository
public interface UserRepo extends JpaRepository<User, String> {
	
	// userIdで検索するメソッド
    // 引数のStringがWHERE user_id = ? に対応
    Optional<User> findByUserId(String userId);
    
}
