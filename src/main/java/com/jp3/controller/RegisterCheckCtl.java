
//入力値のリアルタイム判定用のcontroller

package com.jp3.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jp3.repository.UserRepo;


/*このクラスは REST API を返すコントローラですよのアノテーション
 * ようはretuenでHTMLではなく、jsonデータだけ戻すcontroller
 */
@RestController  
public class RegisterCheckCtl {
	@Autowired
	private UserRepo userRepo;

	// 引数: URLパラメータのuserId（String）
	@GetMapping("/register/check-user_id")
	public Map<String, Boolean> checkUsername(@RequestParam String user_id) {
	    
	// UserRepositoryでDBを検索
		boolean exists = userRepo.findByUserId(user_id).isPresent();
	 
		// 戻り値: {"exists": true/false} のJSON
		//trueならかぶりがあるということ
	    return Map.of("exists", exists);
	}
}
