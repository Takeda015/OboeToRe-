package com.jp3.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.jp3.constant.GeminiPromptConst;
import com.jp3.entity.ChatHist;
import com.jp3.entity.ShoppingList;
import com.jp3.entity.Subsk;
import com.jp3.entity.Task;
import com.jp3.entity.User;
import com.jp3.repository.ChatHistRepo;
import com.jp3.repository.UserRepo;

import lombok.RequiredArgsConstructor;

/*なんかこのｓｌｆ４ｊってやつをつけるとロムボックのlogがつかえるようになって
 * logに一時的にエラー内容を記録するlog.errorを利用できるっぽい
 * */
@lombok.extern.slf4j.Slf4j
@RequiredArgsConstructor
@Service
public class ChatSvc {

	//チャット履歴のリポジトリ
	private final ChatHistRepo chatHistRepo;
	//タスク取得
	private final TaskSvc taskSvc;

	private final ShoppingListSvc sListSvc;
	private final SubskSvc subskSvc;

	//Geminiのクライアント情報
	@Qualifier("geminiRestClient")
	private final RestClient geminiRestClient;
	//ユーザー情報取得
	private final UserRepo userRepo;

	//コントロールから呼び出す対話のメインメソッド
	public String chat(String userId, String userMessage) {

		// 会話履歴取得（古い順）
		//		List<ChatHist> history = chatHistRepo.findByUserIdOrderByCreatedAtAsc(userId);
		//  会話履歴取得（直近20件のみ、古い順に並べ替え）
		List<ChatHist> history = chatHistRepo.findTop20ByUserIdOrderByCreatedAtDesc(userId);
		Collections.reverse(history);

		// タスク情報取得
		List<Task> tasks = taskSvc.getTasksByUser(userId);
		String taskInfo = buildTaskInfo(tasks);
		
		//買い物リスト情報取得
		List<ShoppingList> SList = sListSvc.getSListTodo(userId);
		String soppingInfo = buildShopInfo(SList);

		//サブスクの期限が今日のやつを出す
				List<Subsk> sbskList = subskSvc.getSbskList(userId);
				String sbskInfo = buildSbskInfo(sbskList);
		
		// nickname取得
		String nickname = userRepo.findById(userId).map(User::getNickname).orElse("ユーザー");

		// contents構築、アレイリスト型のcontentsを用意
		List<Map<String, Object>> contents = new ArrayList<>();
		
		// システムプロンプトをuser/modelターンで先頭挿入
		contents.add(Map.of("role", "user", "parts", List.of(Map.of("text",
				String.format(GeminiPromptConst.CHAT_SYSTEM, nickname) + taskInfo +
						"\n【買い物リスト】\n" + soppingInfo
						+"\n【加入中のサブスクリスト】\n" + sbskInfo))));
		contents.add(Map.of("role", "model", "parts", List.of(Map.of("text", "了解。タスク情報を把握した。何でも聞いて"))));

		// 会話履歴を追加
		for (ChatHist h : history) {
			contents.add(Map.of("role", h.getRole(), "parts", List.of(Map.of("text", h.getContent()))));
		}

		// 今回のユーザーメッセージ
		contents.add(Map.of("role", "user", "parts", List.of(Map.of("text", userMessage))));

		// ④ API送信
		String aiReply = null;
		boolean apiFailed = false;
		try {
			Map response = geminiRestClient.post()
					.body(Map.of("contents", contents))
					.retrieve()
					.body(Map.class);
			aiReply = extractText(response);
		} catch (Exception e) {
			e.printStackTrace();

			//ここでエラーメッセージをlogの方にしまっておく
			log.error("Gemini API呼び出しに失敗しました userId={}", userId, e);

			aiReply = "sleep:オボエは休止中です";
			apiFailed = true;
		}

		// ⑤ DBに保存

		chatHistRepo.save(new ChatHist(userId, "user", userMessage));
		//エラーが出てない時だけ生成した発言内容をしまっておくことで
		//エラーの時の休止メッセージをログに含めないようにする
		if (!apiFailed) {
			chatHistRepo.save(new ChatHist(userId, "model", aiReply));
		}
		//		chatHistRepo.save(new ChatHist(userId, "model", aiReply));

		return aiReply;
	}

	//タスクのリストを文字列にしてる
	private String buildTaskInfo(List<Task> tasks) {
		if (tasks.isEmpty())
			return "登録タスクなし\n";
		StringBuilder sb = new StringBuilder();
		for (Task t : tasks) {
			sb.append("・").append(t.getTitle())
					.append("（期限：").append(t.getDueDate() != null ? t.getDueDate() : "未設定")
					.append("、状態：").append(t.getStatus()).append("）\n");
		}
		return sb.toString();
	}

	//ここでストリングbuilderで買い物のリストを文字列として受け取る
	private String buildShopInfo(List<ShoppingList> SList) {
		if (SList.isEmpty())
			return "買い物リストはなし";
		StringBuilder sb = new StringBuilder();
		for (ShoppingList s : SList) {
			sb.append("・").append(s.getItemName());
		}
		return sb.toString();
	}

	//ストリングbuilderでサブスクのリストを文字列として受け取る
	private String buildSbskInfo(List<Subsk> SbskList) {
		if (SbskList.isEmpty())
			return "登録してるサブスクはなし";
		StringBuilder sb3 = new StringBuilder();
		for (Subsk s : SbskList) {
			sb3.append("・").append(s.getSubskName())
			.append("更新日（期限）：").append(s.getUpdateAt() != null ? s.getUpdateAt() : "未設定");
		}
		return sb3.toString();
	}

	@SuppressWarnings("unchecked")
	private String extractText(Map response) {
		try {
			List<Map> candidates = (List<Map>) response.get("candidates");
			Map content = (Map) candidates.get(0).get("content");
			List<Map> parts = (List<Map>) content.get("parts");
			return (String) parts.get(0).get("text");
		} catch (Exception e) {
			return "返答の取得に失敗しました。";
		}
	}
	
	//チャット履歴の全消去
	public void delChat(String userId) {
		List<ChatHist> delAllhist = chatHistRepo.findByUserId(userId);
		chatHistRepo.deleteAll(delAllhist);
	}
	
}