package com.jp3.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.jp3.entity.ShoppingList;
import com.jp3.entity.Task;
import com.jp3.service.ReminderSvc;
import com.jp3.service.ShoppingListSvc;
import com.jp3.service.TaskSvc;
import com.jp3.service.UserSvc;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeCtl {

	private final TaskSvc taskSvc;
	private final ShoppingListSvc shoppingListSvc;
	private final ReminderSvc reminderSvc;
	private final UserSvc userSvc;

	@GetMapping("/home")
	public String homePage(Model model) {
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		LocalDate today = LocalDate.now();

		//       初回ログイン判定
		boolean firstLoginChk = userSvc.firstLoginCheck(userId);
		model.addAttribute("firstLoginChk",firstLoginChk);

		//今日の日付で完了になってないものを出す
		List<Task> tasks = taskSvc.getTodayTasksByUser(userId, today);

		//日付の指定がないものを出す
		List<Task> noLimTasks = taskSvc.getTodayTasksByUser(userId, null);

		//リマインダー呼び出し
		  // 初回時は挨拶文に差し替え。通常のリマインド生成(Gemini呼び出し)はスキップ
		String reminder = firstLoginChk
	            ? userSvc.Greeting(userId) 
	            : reminderSvc.generateReminder(userId);
		
		//買い物リスト表示
		List<ShoppingList> sLL = shoppingListSvc.getSListTodo(userId);

		model.addAttribute("todoSLists", sLL);

		model.addAttribute("tasks", tasks);
		model.addAttribute("noLimTasks", noLimTasks);
		model.addAttribute("today", today);
		model.addAttribute("reminder", reminder); // テンプレートの ${reminder} に対応
		return "home";
	}
}