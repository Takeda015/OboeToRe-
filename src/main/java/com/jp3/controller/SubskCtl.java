//サブスク登録のとこ
package com.jp3.controller;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jp3.dto.form.SubskForm;
import com.jp3.entity.Subsk;
import com.jp3.repository.SubskRepo;
import com.jp3.service.SubskSvc;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SubskCtl {

	private final SubskRepo subskRepo;
	private final SubskSvc subskSvc;


	@GetMapping("/subsk")
	public String subskPage(Model model) {
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		//全部取得
		List<Subsk> sbskL = subskSvc.getSbskList(userId);
		model.addAttribute("SbskLists", sbskL);
		return "subsk";
	}

	@PostMapping("/addSbsk")
	public String addSbsk(SubskForm subskForm) {
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		subskForm.setUserId(userId);
		subskSvc.subRegi(subskForm);
		return "redirect:/subsk";
	}

	//内容編集
	@PostMapping("/sbsk/edit")
	public String editSbsk(SubskForm subskForm) {
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		subskSvc.editSbsk(subskForm, userId);
		return "redirect:/subsk";
	}
	
	
	//選択したやつの削除
	@PostMapping("/sbsk/del")
	public String delSbsk(@RequestParam List<Long> subskId) {
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		
		subskSvc.delSbsk(subskId, userId);
		return "redirect:/subsk";
	}

}
