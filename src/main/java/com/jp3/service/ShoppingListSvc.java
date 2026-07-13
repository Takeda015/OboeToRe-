package com.jp3.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jp3.dto.form.SListForm;
import com.jp3.entity.ShoppingList;
import com.jp3.repository.ShoppingListRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShoppingListSvc {

	private final ShoppingListRepo sListRepo;

	//買い物リスト登録
	public void sListRegi(SListForm sListForm) {

		ShoppingList sL = new ShoppingList(
				sListForm.getUserId(),
				sListForm.getItemName(),
				sListForm.getDescription());
		sListRepo.save(sL);
	}

	//削除
	public void deleteSList(List<Long> shoppingIds,String userId) {
		List<ShoppingList> delList = sListRepo.findByShoppingIdInAndUserId(shoppingIds, userId);
		sListRepo.deleteAll(delList);
	}
	
	//未完了買い物リスト表示
	public List<ShoppingList> getSListTodo(String userId) {
		List<ShoppingList> sLL = sListRepo.findByUserIdAndStatus(userId, "TODO");
		return sLL;

	}
	
	//全件買い物リスト表示
	public List<ShoppingList> getSList(String userId){
		List<ShoppingList> sLL=sListRepo.findByUserId(userId).stream()
				.sorted(Comparator.comparing(t -> "DONE".equals(t.getStatus()) ? 1 : 0))
				.collect(Collectors.toList());
		return sLL;
	}
	
	//ステータス変更
	public void updateStatus(List<Long>shoppingIds,String status, String userId) {
		List<ShoppingList> owntedSList=sListRepo.findByShoppingIdInAndUserId(shoppingIds, userId);
		
		//statesを指定のものに変更する
		owntedSList.forEach(slis ->slis.setStatus(status));
		sListRepo.saveAll(owntedSList);
	}
}
