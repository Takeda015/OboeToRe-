//Homeに表示するリマインダーの機能
package com.jp3.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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

@RequiredArgsConstructor
@Service
public class ReminderSvc {

	private final TaskSvc taskSvc;
	private final ShoppingListSvc sListSvc;
	private final SubskSvc subskSvc;
	private final ChatHistRepo chatHistRepo;
	private final RestClient geminiRestClient;
	private final UserRepo userRepo;

	//リマインダーを更新する時間を記録してる。あんまGemini使うと無料分消えるので
	private static final double CACHE_HOURS = 0.5;

	public String generateReminder(String userId) {

		//ユーザーIDを取得
		User user = userRepo.findById(userId).orElse(null);

		//ないことないんだけど取得できなかったら特定の文を表示
		if (user == null)
			return "タスク頑張って";

		//ここで前にリマインダー作った時間を取得
		LocalDateTime last = user.getReminderGeneratedAt();

		//一定時間経ってなかったら記録しておいた前のチャットを出す。retuenしてるからこのしたには行かない、はず。
		if (last != null && last.plusMinutes((long) (CACHE_HOURS * 60)).isAfter(LocalDateTime.now())) {
			return user.getReminderCache();
		}

		//チャットヒストリーから直近１０個のチャット履歴を持ってくる
		List<ChatHist> recentChats = chatHistRepo.findTop10ByUserIdOrderByCreatedAtDesc(userId);
		StringBuilder chatSummary = new StringBuilder();

		for (int i = recentChats.size() - 1; i >= 0; i--) {
			ChatHist h = recentChats.get(i);
			chatSummary.append(h.getRole().equals("user") ? "ユーザー：" : "AI：")
					.append(h.getContent()).append("\n");
		}

		//service経由でユーザーの今日のタスクとってくる。
		//serviceにはgetTodayTasksByUserメソッド作っておいた
		List<Task> todayTasks = taskSvc.getTodayTasksByUser(userId, LocalDate.now());
		//今日のタスクを取得
		String taskInfo = buildTaskInfo(todayTasks);

		//サブスクの期限が今日のやつを出す
		List<Subsk> todaySbsk = subskSvc.getTodaySbskByUser(userId, LocalDate.now());
		String sbskInfo = buildSbskInfo(todaySbsk);

		//買い物リストの追加
		List<ShoppingList> SList = sListSvc.getSListTodo(userId);
		String soppingInfo = buildShopInfo(SList);

		String nickname = user.getNickname() != null ? user.getNickname() : "ユーザー";

		// GeminiPromptConstからプロンプト構築
		String prompt = String.format(GeminiPromptConst.REMINDER_SYSTEM, nickname)
				+ "\n【今日が期限のタスク】\n" + taskInfo
				+ "\n【最近のユーザーとの会話（参考）】\n" + chatSummary.toString()
				+ "\n【買い物リスト】\n" + soppingInfo
				+ "\n【サブスク】\n" + sbskInfo
				+ "\n" + GeminiPromptConst.REMINDER_SUFFIX;

		List<Map<String, Object>> contents = List.of(
				Map.of("role", "user", "parts", List.of(Map.of("text", prompt))));
		Map<String, Object> requestBody = Map.of("contents", contents);

		//Geminiが死んでる時に呼び出しでエラー返ってきたら一定の文章を出すようにする
		try {
			Map response = geminiRestClient.post()
					.body(requestBody)
					.retrieve()
					.body(Map.class);

			//コンソールに表示
			//            System.out.println("Gemini Response: " + response);

			String reminder = extractText(response);
			user.setReminderCache(reminder);
			user.setReminderGeneratedAt(LocalDateTime.now());

			//レポジトリに登録
			userRepo.save(user);
			return reminder;
		} catch (Exception e) {
			e.printStackTrace();
			String cached = user.getReminderCache();
			return cached != null ? cached : "休止状態。タスク頑張って";
		}
	}

	//ここでストリングbuilderでタスクのリストを文字列として受け取る
	private String buildTaskInfo(List<Task> tasks) {
		if (tasks.isEmpty())
			return "今日のタスクはなし";
		StringBuilder sb = new StringBuilder();
		for (Task t : tasks) {
			sb.append("・").append(t.getTitle())
					.append("（期限：").append(t.getDueTime() != null ? t.getDueTime() : "未設定")
					.append("、状態：").append(t.getStatus()).append("）\n");
		}
		return sb.toString();
	}

	//ストリングbuilderで買い物リストのリストを文字列として受け取る
	private String buildShopInfo(List<ShoppingList> SList) {
		if (SList.isEmpty())
			return "今日の買い物リストはなし";
		StringBuilder sb2 = new StringBuilder();
		for (ShoppingList s : SList) {
			sb2.append("・").append(s.getItemName());
		}
		return sb2.toString();
	}

	//ストリングbuilderでサブスクのリストを文字列として受け取る
	private String buildSbskInfo(List<Subsk> SbskList) {
		if (SbskList.isEmpty())
			return "今日が更新日のサブスクはなし";
		StringBuilder sb3 = new StringBuilder();
		for (Subsk s : SbskList) {
			sb3.append("・").append(s.getSubskName());
		}
		return sb3.toString();
	}

	//クロード曰く、@SuppressWarningsは必要らしい
	@SuppressWarnings("unchecked")
	private String extractText(Map response) {
		try {
			//candidates:配列を取得
			List<Map> candidates = (List<Map>) response.get("candidates");
			//配列からコンテンツキー情報を取得
			Map content = (Map) candidates.get(0).get("content");
			//でその上方からさらにパーツを出す
			List<Map> parts = (List<Map>) content.get("parts");
			//パーツに含まれるテキスト情報をストリングで返す
			return (String) parts.get(0).get("text");
		} catch (Exception e) {
			return "今日もタスクをやっていこう";
		}
	}
}