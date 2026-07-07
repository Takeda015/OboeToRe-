//タスクのエンティティ
package com.jp3.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "tasks")
public class Task {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "task_id")
	private Long taskId;

	@Column(name = "user_id", nullable = false)
	private String userId;

	//タスク名
	@Column(name = "title", nullable = false)
	private String title;

	//タスク説明
	@Column(name = "description", columnDefinition = "TEXT")
	private String description;

	// 1:低 2:中 3:高の優先度
	@Column(name = "priority", nullable = false)
	private int priority;

	//開始時
	@Column(name = "start_date")
	private LocalDate startDate;

	@Column(name = "start_time")
	private LocalTime startTime;

	//終了期限
	@Column(name = "due_date")
	private LocalDate dueDate;

	@Column(name = "due_time")
	private LocalTime dueTime;

	@Column(name = "location")
	private String location;

	@Column(name = "status", nullable = false)
	private String status;

	@Column(name = "google_event_id")
	private String googleEventId;
	
	
	@Column(name = "created_at", updatable = false, insertable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", insertable = false, updatable = false)
	private LocalDateTime updatedAt;

	public Task(String userId, String title, String description, int priority,
			LocalDate startDate, LocalTime startTime, LocalDate dueDate, LocalTime dueTime,
			String location) {
		this.userId = userId;
		this.title = title;
		this.description = description;
		this.priority = priority;
		this.startDate = startDate;
		this.startTime = startTime;
		this.dueTime = dueTime;
		this.dueDate = dueDate;
		this.location = location;
		this.status = "TODO";
	}
}
