package com.jp3.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jp3.repository.ChatHistRepo;
import com.jp3.service.ChatSvc;
import com.jp3.service.UserSvc;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatCtl {
	private final ChatSvc chatSvc;
	private final ChatHistRepo chatHistRepo;
	private final UserSvc userSvs;

	@GetMapping("/chat")
	private String getChat(Model model) {
		//ユーザーIDの取得
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();

		//モデルオブジェクトにRepoで出した結果をセットする（今回はList）
		model.addAttribute("histories", chatHistRepo.findByUserIdOrderByCreatedAtAsc(userId));

		String nowNickname = userSvs.getNowNickname(userId);
		model.addAttribute("nickname", nowNickname);

		return "chat";
	}

	// ジャバスクリプトのAjaxから呼ぶ想定。レスポンスはAIの返答テキストのみ
	
	@PostMapping("/chat")
	@ResponseBody
	public String postChat(@RequestParam String message) {
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();

		//こいつ↓がbodyとして返ってく
		return chatSvc.chat(userId, message);
	}
	
	//チャット履歴の全件削除
	@PostMapping("/chat/del")
	public String delChat() {
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		chatSvc.delChat(userId);
		return "redirect:/chat";
	}

}
