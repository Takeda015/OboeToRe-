//サブスク登録のとこ
package com.jp3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SubskCtl {

	@GetMapping("/subsk")
	public String subskPage() {

		return "subsk";
	}
}
