//新規ユーザー登録フォーム入力値受け取り
//バリデーションチェック：何文字以上
package com.jp3.dto.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRegForm {
	
	//フィールド：アノテーション
	@NotBlank
	@Size(min = 4, message = "ユーザーIDは4文字以上で入力してください")
	@Pattern(regexp = "^[a-zA-Z0-9]+$", message = "ユーザーIDは半角英数字のみ使用できます")
	private String user_id;
	
	@NotBlank
	private String nickname;
	
	@NotBlank
	private String password;
	
	@NotBlank
	private String password2;
	
	
}
