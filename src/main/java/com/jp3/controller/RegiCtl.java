package com.jp3.controller;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.jp3.dto.form.UserRegForm;
import com.jp3.service.UserSvc;

import lombok.RequiredArgsConstructor;

//新規ユーザー登録処理

@RequiredArgsConstructor
@Controller
public class RegiCtl {

	private final UserSvc userSvs;

	@GetMapping("/register")
	public String getRegi() {
		return "register";
	}

	//コンストラクタインジェクション
	@PostMapping("/register")
	public String register(@Valid UserRegForm uRegiForm, Model model) {

		try {
			userSvs.register(uRegiForm);
		} catch (IllegalArgumentException e) {
			model.addAttribute("passwordError", e.getMessage());
			return "register";
		}
		return "redirect:/login";
	}
}
