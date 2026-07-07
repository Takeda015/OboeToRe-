// タスク登録・編集フォーム入力値受け取り
package com.jp3.dto.form;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class TaskForm {

	// HTMLフォームから受け取るフィールド
	private String title;
	private String description;
	private int priority; // 1:低 2:中 3:高

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate startDate;

	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime startTime;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate dueDate;

	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime dueTime;

	private String location;

	// 編集時にControllerがセット（新規登録時は不要）
	private Long taskId;

	// Controllerが後からセット（フォームから受け取らない）
	private String userId;
}