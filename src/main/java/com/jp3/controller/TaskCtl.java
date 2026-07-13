// タスク登録・タスクのステータス変更・完了時ポイント付与トリガー
package com.jp3.controller;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jp3.dto.form.TaskForm;
import com.jp3.service.TaskSvc;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class TaskCtl {

	private final TaskSvc taskSvc;

	//個人のIDで検索かけて全体のタスクを持ってくる
	@GetMapping("/task")
	public String getTask(Model model) {
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("tasks", taskSvc.getTasksByUser(userId));
		return "tasks";
	}

	//タスクの追加
	@PostMapping("/addTask")
	public String postTask(TaskForm taskForm, @RequestParam String redirectTo) {
		//springSecurityからユーザーIDを取得
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		taskForm.setUserId(userId);

		String back = "/task";
		if (redirectTo.equals("homePage")) {
			back = "/home";
		}

		taskSvc.taskRegi(taskForm);
		return "redirect:" + back;
	}

	// タスク内容編集
	@PostMapping("/task/edit")
	public String editTask(TaskForm taskForm) {
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		taskSvc.editTask(taskForm, userId);
		return "redirect:/task";
	}

	// ステータス一括変更
	@PostMapping("/task/status")
	public String updateStatus(@RequestParam List<Long> taskIds, @RequestParam String status,
			@RequestParam String redirectTo) {
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		taskSvc.updateStatus(taskIds, status, userId);

		String back = "/task";
		if (redirectTo.equals("homePage")) {
			back = "/home";
		}
		return "redirect:" + back;
	}

	//タスクの削除
	@PostMapping("/task/delete")
	public String deleteTasks(@RequestParam List<Long> taskIds) {
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		taskSvc.deleteTasks(taskIds, userId);
		return "redirect:/task";
	}
	
	@PostMapping("/task/doneDelete")
	public String deleteDoneTasks() {
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		taskSvc.delDoneTasks(userId);
		return "redirect:/task";
	}
	
}
