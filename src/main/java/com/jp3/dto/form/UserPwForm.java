// パスワード変更フォーム入力値受け取り（現PW確認フィールド含む）
package com.jp3.dto.form;

import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserPwForm {
	
	@NotBlank
	private String currentPass;
	
	@NotBlank
	private String password;
	
	@NotBlank
	private String password2;
}
