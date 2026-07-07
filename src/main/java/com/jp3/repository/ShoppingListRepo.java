//買い物リストを保存するshopping_listテーブルのリポジトリ
package com.jp3.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jp3.entity.ShoppingList;

@Repository
public interface ShoppingListRepo extends JpaRepository<ShoppingList, Long> {

	//ユーザーのタスク一覧取得Listで受け取る、userIdで検索
	List<ShoppingList> findByUserId(String userId);

	// ステータス絞り込み:userIdと、現在のstatusをもとに検索して、Listで受け取る
	List<ShoppingList> findByUserIdAndStatus(String userId, String status);

	// 所有者確認付き一括取得（削除前チェック・ステータス変更用）
	List<ShoppingList> findByShoppingIdInAndUserId(List<Long> shoppingId, String userId);

	// 所有者確認付き単一取得（編集用）
	Optional<ShoppingList> findByShoppingIdAndUserId(Long shoppingId, String userId);

}
