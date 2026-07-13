// タスクCRUD・ステータス変更・完了時ポイント付与呼び出し
package com.jp3.service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jp3.dto.form.TaskForm;
import com.jp3.entity.Task;
import com.jp3.repository.TaskRepo;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TaskSvc {

	private final TaskRepo taskrepo;

	// タスクの登録
	public void taskRegi(TaskForm tsf) {
		Task tsk = new Task(tsf.getUserId(), tsf.getTitle(), tsf.getDescription(), tsf.getPriority(),
				tsf.getStartDate(), tsf.getStartTime(), tsf.getDueDate(), tsf.getDueTime(), tsf.getLocation());
		taskrepo.save(tsk);
	}

	// ユーザーのタスクを一括取得（DONE を末尾に）
	public List<Task> getTasksByUser(String userId) {
		return taskrepo.findByUserId(userId).stream()
				.sorted(Comparator.comparing(t -> "DONE".equals(t.getStatus()) ? 1 : 0))
				.collect(Collectors.toList());
	}

	// 今日が期限のタスクを取得（ユーザー絞り込み済み）
	public List<Task> getTodayTasksByUser(String userId, LocalDate today) {
		return taskrepo.findByDueDateAndStatusAndUserId(today, "TODO", userId);
	}

	// ユーザーIDを確認したうえで削除
	public void deleteTasks(List<Long> taskIds, String userId) {
		List<Task> ownedTasks = taskrepo.findByTaskIdInAndUserId(taskIds, userId);
		taskrepo.deleteAll(ownedTasks);
	}
	
	//所有者確認してDoneを一括削除
	public void delDoneTasks(String userId) {
		List<Task> doneTasks = taskrepo.findByUserIdAndStatus(userId, "DONE");
		taskrepo.deleteAll(doneTasks);
	}
	

	// タスク内容編集（所有者確認付き）
	public void editTask(TaskForm tsf, String userId) {

		//たぶんコピー使えば一発だけどそれ用に諸々を用意するのがだるいのでゲットしてセットしてる
		//ここで例外投げてるからハンドリングがいるのでは。。。？
		Task tsk = taskrepo.findByTaskIdAndUserId(tsf.getTaskId(), userId)
				.orElseThrow(() -> new IllegalArgumentException("タスクが見つかりません"));
		tsk.setTitle(tsf.getTitle());
		tsk.setDescription(tsf.getDescription());
		tsk.setPriority(tsf.getPriority());
		tsk.setStartDate(tsf.getStartDate());
		tsk.setStartTime(tsf.getStartTime());
		tsk.setDueDate(tsf.getDueDate());
		tsk.setDueTime(tsf.getDueTime());
		tsk.setLocation(tsf.getLocation());
		taskrepo.save(tsk);
	}

	// ステータス一括変更（所有者確認付き）
	public void updateStatus(List<Long> taskIds, String status, String userId) {
		List<Task> ownedTasks = taskrepo.findByTaskIdInAndUserId(taskIds, userId);
		ownedTasks.forEach(tsk -> tsk.setStatus(status));
		taskrepo.saveAll(ownedTasks);
	}
	
	

}