//GoogleカレンダーOAuth2・API呼び出し
package com.jp3.service;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.jp3.entity.Task;
import com.jp3.repository.TaskRepo;

@Service
public class GoogleCalendarSvc {

	//Accessトークンくれ～をGoogleに送る
	public String getAccessToken(String code, String clientId, String clientSecret, String redirectUri) {

		//Googleに送る取得した許可の値とかを入れておく、らしい
		String body = "code=" + code
				+ "&client_id=" + clientId
				+ "&client_secret=" + clientSecret
				+ "&redirect_uri=" + redirectUri
				+ "&grant_type=authorization_code";

		//レストクライアントを自動で登録しないらしいから、
		//buildで新しく作る感じだと。へ～、分からん。
		Map response = RestClient.builder().build()
				.post()
				.uri("https://oauth2.googleapis.com/token")
				.header("Content-Type", "application/x-www-form-urlencoded")
				.body(body)
				.retrieve()
				.body(Map.class);

		//よくわからないけどresponceで作ったURL・・・何・・・？
		//を呼び出すとgetでアクセストークンをとってこれるっぽい
		return (String) response.get("access_token");
	}

	//いよいよGoogleからイベントデータをもってくる
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getEvents(String accessToken) {

		String timeMin = LocalDate.now().atStartOfDay().atOffset(ZoneOffset.UTC)
				.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
		String timeMax = LocalDate.now().plusMonths(1).atStartOfDay().atOffset(ZoneOffset.UTC)
				.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

		URI uri = UriComponentsBuilder
				.fromUriString("https://www.googleapis.com/calendar/v3/calendars/primary/events")
				.queryParam("timeMin", timeMin)
				.queryParam("timeMax", timeMax)
				.queryParam("singleEvents", true)
				.queryParam("orderBy", "startTime")
				.build()
				.toUri();

		Map response = RestClient.create()
				.get()
				.uri(uri)
				.header("Authorization", "Bearer " + accessToken)
				.retrieve()
				.body(Map.class);

		List<Map<String, Object>> items = (List<Map<String, Object>>) response.get("items");
		return items != null ? items : List.of();
	}

	//イベントをインポートする！

	public void importEvents(List<Map<String, Object>> events,
			String userId, TaskRepo taskRepo) {

		//for文で回して、イベントIDをゲットして、イベントIDに被りがないか確認する
		for (Map<String, Object> event : events) {
			String eventId = (String) event.get("id");
			if (taskRepo.findByUserIdAndGoogleEventId(userId, eventId).isPresent()) {
				continue;//被ってたら登録をスキップする
			}

			//で被ってなかったら以下の処理を実行=====================

			//タイトルはGoogleカレンダーのsummaryってとこに入ってるらしい
			String title = event.get("summary")!= null ? (String) event.get("summary") : "無題";

			//startとendの時間をGoogleカレンダーから引っ張ってくる
			Map<String, String> start = (Map<String, String>) event.get("start");
			Map<String, String> end = (Map<String, String>) event.get("end");

			/*時間指定のあるdateTimeと終日のdateの二パターンあるので、
			 * それで判定していきます。startの様式にendも合わせる形なので
			 * 判定は一個
			*/
			LocalDate startDate = null;
			LocalTime startTime = null;
			LocalDate dueDate = null;
			LocalTime dueTime = null;

			if (start.containsKey("dateTime")) {
				String startDateTimeStr = start.get("dateTime");
				String endDateTimeStr = end.get("dateTime");

				//日付部分
				startDate = LocalDate.parse(startDateTimeStr.substring(0, 10));
				dueDate = LocalDate.parse(endDateTimeStr.substring(0, 10));
				//時間部分
				startTime=LocalTime.parse(startDateTimeStr.substring(11, 16));
				dueTime=LocalTime.parse(endDateTimeStr.substring(11, 16));
				

			} else {
				//終日データの場合
				String startDateTimeStr = start.get("date");
				String endDateTimeStr = end.get("date");
				startDate = LocalDate.parse(startDateTimeStr);
				dueDate = LocalDate.parse(endDateTimeStr).minusDays(1);
			}

			String description = (String) event.get("description");
			int priority = 2;
			String location =  (String) event.get("location");
			
			

			Task task = new Task(userId, title, description,
					priority,
					startDate, startTime, dueDate, dueTime,
					location);
			task.setGoogleEventId(eventId);
			taskRepo.save(task);
		}

	}

}
