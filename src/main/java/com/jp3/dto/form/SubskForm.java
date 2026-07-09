package com.jp3.dto.form;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SubskForm {
	//受け取るフィールド
	private Long subskId;
	private String userId;
	private String subskName; 
	
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate joinedAt;
	
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate updateAt;
	
	private  Integer updateDays;
	
}
