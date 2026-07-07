//★★★呼び名変更・パスワード変更・ユーザー情報表示★★★
package com.jp3.controller;

import jakarta.validation.Valid;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.jp3.dto.form.UserNickForm;
import com.jp3.dto.form.UserPwForm;
import com.jp3.service.UserSvc;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserCtl {

	private final UserSvc userSvs;
	
	//profileページ表示===================================
	@GetMapping("/profile")
	private String getProfile(Model model) {
		//ユーザーのIDとニックネームだけ持って来て常に表示したい
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		String nowNickname=userSvs.getNowNickname(userId);
		model.addAttribute("nowNickname",nowNickname);
		model.addAttribute("nowUserId",userId);
		return "profile";
	}

	//ログイン==========================
	// GETのみ。POSTはSecurityが処理するので不要
	@GetMapping("/login")
	public String loginPage() {
		return "login";
	}

	//ニックネーム変更=========================
	@PostMapping("/changeNick")
	public String changeNick(@Valid UserNickForm uNickForm, BindingResult result, Model model) {
		if (result.hasErrors()) {//バリデーション失敗でtrue
			model.addAttribute("openModal", "nick");
			return "profile";
		}
		userSvs.changeNickname(uNickForm);
		return "redirect:/profile";//リダイレクトさせるので、表示も変わる、はず
	}
	
	//Change Password===========
	@PostMapping("/changePass")
	public String changePass(@Valid UserPwForm uPwForm, BindingResult result, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("openModal", "pass");
			return "profile";
		}
		try {
			userSvs.changePassword(uPwForm);
		} catch (IllegalArgumentException e) {
			model.addAttribute("changePasswordError", e.getMessage());
			model.addAttribute("openModal", "pass");
			return "profile";
		}
		return "redirect:/profile";
	}

}