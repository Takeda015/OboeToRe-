//タスクの蓄積を行うtaskテーブルのリポジトリ
package com.jp3.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jp3.entity.Task;

@Repository
public interface TaskRepo extends JpaRepository<Task, Long> {

	//ユーザーのタスク一覧取得Listで受け取る、userIdで検索
	List<Task> findByUserId(String userId);

	// ステータス絞り込み:userIdと、現在のstatusをもとに検索して、Listで受け取る
	List<Task> findByUserIdAndStatus(String userId, String status);

	// 所有者確認付き一括取得（削除前チェック・ステータス変更用）
	List<Task> findByTaskIdInAndUserId(List<Long> taskIds, String userId);

	//今日の日付で選んで持ってくる
	List<Task> findByDueDate(LocalDate date);
	//今日の日付かつTODO、で、ユーザーIDも判別する
	List<Task> findByDueDateAndStatusAndUserId(LocalDate date, String status, String userId);

	// 所有者確認付き単一取得（編集用）
	Optional<Task> findByTaskIdAndUserId(Long taskId, String userId);

	//GoogleカレンダーのIDをこう、なんかする。
	Optional<Task> findByUserIdAndGoogleEventId(String userId, String googleEventId);
}
