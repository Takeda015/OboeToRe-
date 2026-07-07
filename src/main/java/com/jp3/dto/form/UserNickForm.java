//ニックネーム変更用のフォーム

package com.jp3.dto.form;

import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserNickForm {
	
	@NotBlank
	private String newNickname;

}
