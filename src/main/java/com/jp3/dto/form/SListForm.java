//Shopping ListのForm
package com.jp3.dto.form;


import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SListForm {

	@NotNull
	private String userId;
	
	@NotNull
	private String itemName;
	private String description;
	
	private String status="TODO";
	
}
