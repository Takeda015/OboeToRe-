package com.jp3.service;

import java.time.LocalDateTime;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.jp3.dto.form.UserNickForm;
import com.jp3.dto.form.UserPwForm;
import com.jp3.dto.form.UserRegForm;
import com.jp3.entity.User;
import com.jp3.repository.UserRepo;

@Service
@Validated
public class UserSvc {

	@Autowired
	private UserRepo userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder; // SecurityConfigのBeanが注入される

	public boolean firstLoginCheck(String userId) {
		User user = userRepository.findByUserId(userId)
				.orElseThrow(() -> new IllegalStateException("ユーザーが見つかりません"));

		boolean firstloginGdg = false;
		if (user.getLastLoginAt() == null) {
			firstloginGdg = true;
			user.setLastLoginAt(LocalDateTime.now());
		    userRepository.save(user);
		}

		return firstloginGdg;
	}

	public String Greeting(String userId) {
		User user = userRepository.findByUserId(userId)
				.orElseThrow(() -> new IllegalStateException("ユーザーが見つかりません"));

		String greetingMsg="""
				はじめまして、%s。
				私はオボエちゃん。今から君のタスクをサポートするよ。よろしくね。
				""".formatted(user.getNickname());
		return greetingMsg;
	}
	
	
	//IDとニックネーム表示===================
	public String getNowNickname(String userId) {

		User user = userRepository.findByUserId(userId)
				.orElseThrow(() -> new IllegalStateException("ユーザーが見つかりません"));

		String nowNickname = user.getNickname();
		return nowNickname;

	}

	//ニックネーム変更==============================
	public void changeNickname(UserNickForm uNickForm) {
		//UserIDをsecurityフレームワークからとってくる
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		// そのuserIdでDBを検索
		User user = userRepository.findByUserId(userId)
				.orElseThrow(() -> new IllegalStateException("ユーザーが見つかりません"));
		// 中身がある → そのまま User を返す
		// 中身がない（→ 例外をthrow

		//ユーザーネーム部分を更新
		user.setNickname(uNickForm.getNewNickname());

		//更新したエンティティをDBにセーブ
		userRepository.save(user);

	}

	//password変更==================================
	public void changePassword(@Valid UserPwForm uPwForm) {

		//ｃｔｌでトライキャッチしたうえで照合かけて再入力とあってるか確認
		if (!uPwForm.getPassword().equals(uPwForm.getPassword2())) {
			throw new IllegalArgumentException("パスワードが一致しません");
		}

		//現在Passの確認================
		//ID持ってくる。
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		//IDで検索かけてUserの入ったOptionalエンティティをとってくる
		User user = userRepository.findByUserId(userId)
				.orElseThrow(() -> new IllegalStateException("ユーザーが見つかりません"));

		// matches(平文, DBのハッシュ) で現在PW確認
		if (!passwordEncoder.matches(uPwForm.getCurrentPass(), user.getPassword())) {
			throw new IllegalArgumentException("現在のパスワードが違います");
		}

		//入力された更新後のパスをハッシュ化
		String hashed = passwordEncoder.encode(uPwForm.getPassword());

		//Userのpassword部分を更新
		user.setPassword(hashed);

		//エンティティをDBへsave
		userRepository.save(user);

	}

	//Registory
	//===IDの判定を行う=============================================
	public void register(UserRegForm uRegiForm) {

		if (!uRegiForm.getPassword().equals(uRegiForm.getPassword2())) {
			throw new IllegalArgumentException("パスワードが一致しません");
		}

		// ① 入力されたパスワードをArgon2でハッシュ化
		// 引数: 平文パスワード（String）
		// 戻り値: ハッシュ化済み文字列（String） → "$argon2id$v=19$..."
		String hashed = passwordEncoder.encode(uRegiForm.getPassword());

		// ② UserEntityに詰めてDBへsave
		User user = new User(uRegiForm.getUser_id(), hashed, uRegiForm.getNickname(), null);
		userRepository.save(user);// ハッシュ化済みを渡す
	}

}
