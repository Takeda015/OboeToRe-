//Googleカレンダーの認可フロー・インポート
package com.jp3.controller;

import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jp3.repository.TaskRepo;
import com.jp3.service.GoogleCalendarSvc;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class GoogleCalendarCtl {

	final GoogleCalendarSvc googleCalendarSvc;
	private final TaskRepo taskRepo;

	@Value("${google.oauth.client-id}")
	private String clientId;

	@Value("${google.oauth.redirect-uri}")
	private String redirectUri;

	@Value("${google.oauth.client-secret}")
	private String clientSecret;

	@GetMapping("/google/auth")
	public String googleAuth() {
		//Googleで取得しといたクライアントID

		//今回は環境変数じゃなくて平文で打ってますが本当は諸々変数にするべき感じのものらしい
		String authURL = "https://accounts.google.com/o/oauth2/v2/auth"
				+ "?client_id=" + clientId
				+ "&redirect_uri=" + redirectUri
				+ "&response_type=code"
				+ "&scope=https://www.googleapis.com/auth/calendar.readonly"
				+ "&access_type=online";

		return "redirect:" + authURL;
	}

	//認証で戻ってきたやつを格納する(戻ってくるのは認可コード)
	@GetMapping("/google/callback")
	public String googleCallback(@RequestParam String code, HttpSession session) {

		// 認可コード → アクセストークン交換
		String accessToken = googleCalendarSvc.getAccessToken(code, clientId, clientSecret, redirectUri);

		// セッションに一時保存
		session.setAttribute("googleAccessToken", accessToken);

		// アクセストークンとってこれたら別の処理に回す。
		//次でDBに中身を登録する
		return "redirect:/google/import";
	}
	
	//受け取る（↑でリダイレクトしたのでgetで）、受け取ったのを登録に投げる。
	@GetMapping("/google/import")
	private String googleEvents(HttpSession session) {
		
		//いれといたアクセストークンを持ってくる
		String accessToken=(String)session.getAttribute("googleAccessToken");
	    // セッションにトークンがなければ再認証
	    if (accessToken == null) {
	        return "redirect:/google/auth";
	    }
	    
	    //serviceに処理を投げてイベントを取得
	    List<Map<String,Object>> events=googleCalendarSvc.getEvents(accessToken);

	    //でユーザーIDも持ってくると
	    String userId = SecurityContextHolder.getContext().getAuthentication().getName();
	    
	    //取得したイベントとユーザーIDをserviceに投げてタスクに登録する
	    googleCalendarSvc.importEvents(events, userId, taskRepo);
	    
	    //使い終わったトークンを破棄
	    session.removeAttribute("googleAccessToken");

	    
	    
	    return "redirect:/task";
		
	}
	

}
