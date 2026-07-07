package com.jp3.controller;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jp3.dto.form.SListForm;
import com.jp3.entity.ShoppingList;
import com.jp3.service.ShoppingListSvc;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ShoppingListCtl {

	private final ShoppingListSvc shoppingListSvc;

	@GetMapping("/sList")
	public String getShoppingList(Model model) {
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		
		//ここ全部取得じゃないとダメだわ・・・
		List<ShoppingList> sLL = shoppingListSvc.getSList(userId);
		model.addAttribute("todoSLists", sLL);
		

		return "shoppingList";
	}

	//お買い物リストついか
	@PostMapping("/sList")
	private String addSList(SListForm sListForm, @RequestParam String redirectTo) {

		//springSecurityからユーザーIDを取得
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		sListForm.setUserId(userId);

		//ホームから飛んで来たらホームに、リスト詳細から飛んでたら詳細に返す
		String back = "/sList";
		if (redirectTo.equals("homePage")) {
			back = "/home";
		}

		shoppingListSvc.sListRegi(sListForm);
		return "redirect:" + back;
	}

	//お買い物リストのステータス変更
	@PostMapping("/sList/status")
	public String name(@RequestParam List<Long> shoppingIds, @RequestParam String status,@RequestParam String redirectTo) {
		
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		shoppingListSvc.updateStatus(shoppingIds, status, userId);
		
		//ホームから飛んで来たらホームに、詳細から飛んでたら詳細に返す
		String back = "/sList";
		if (redirectTo.equals("homePage")) {
			back = "/home";
		}

		
		return "redirect:" + back;
	}

}
