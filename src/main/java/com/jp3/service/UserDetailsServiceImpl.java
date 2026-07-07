package com.jp3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jp3.entity.User;
import com.jp3.repository.UserRepo;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepo userRepo;

	@Override
	// Spring Securityがフォームのusername値を引数に渡してこのメソッドを自動呼び出す
	// 引数 username : フォームで入力されたユーザーID（String）
	public UserDetails loadUserByUsername(String user_id) throws UsernameNotFoundException {

		// ① Repositoryでusername（ユーザーID）をキーにDBを検索
		// 戻り値はOptional<UserEntity>
		User user = userRepo.findByUserId(user_id)
				.orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません: " + user_id));
		// 見つからない場合はUsernameNotFoundExceptionをthrow
		// SecurityがキャッチしてfailureUrlへリダイレクト

		// ② UserDetailsオブジェクトを生成して返す
		// SecurityがこのオブジェクトのgetPassword()とフォームのpasswordをBCryptで比較する
		return org.springframework.security.core.userdetails.User
				.withUsername(user.getUserId()) // 引数: DBから取得したユーザーID（String）
				.password(user.getPassword()) // 引数: DBから取得したハッシュ済みパスワード（String）
				.roles("USER") // 権限。とりあえず固定でOK
				.build();
		// 戻り値: UserDetailsオブジェクト → Securityが受け取りパスワード比較を行う
	}
}
