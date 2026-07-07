# 卒業制作： スケジュールおよびタスク管理アプリ
製作期間：7/21まで

pj名：OboeToRe:

コンセプト：
AIを使用したタスク管理
買い出しのものとか提出物とか予定を忘れるので、対話形式でリマインドを行うものを作成したい
＞＞現状は手動

【コンセプト上絶対必要な機能】
- タスクの登録・編集・削除
- タスク一覧の表示
- ログイン機能
- ユーザー登録
- Geminiによるリマインド
- 買い出し物品の登録・削除
- 買い物リスト一覧の表示


【余裕があればほしい機能】
- googleカレンダーと同期
- 対話モードとボタンモードの切り替え
- googlemapと同期
- ソシャゲのイベントを攻略サイトから読み込む
- サブスクの加入情報と支払日と金額を表示
- ユーザーに読み込むソシャゲのページ指定をさせたい

さらに余裕があれば
・イラストを作成して２Dか３Dで動かす
・アンドロイドアプリを作成して同期

---

# プロジェクトの内容
```
プロジェクト名：OboeToRe
種別：対話型タスク管理Webアプリ
主要言語：Java
FW：Spring Boot 4.0.6
IDE：Eclipse 2026
フロント：Thymeleaf
会話用AI：Gemini API直接（APIキー認証）/ gemini-2.5-flash(無料枠)
DB：MySQL 8.4
セキュリティ：Spring Security
ビルド：Maven
ベースパッケージ：com.jp3
外部連携：Google Calendar
```

# 【機能概要】
- タスク登録・編集・ステータス管理（Phase1）
- ログイン・ユーザー登録・登録情報変更（Phase1）
- キャラクター表示＋AI会話によるタスクリマインド
    - リマインドの表示（Phase1）
    - キャラクター画像表示（Phase2）・動き(Phase3以降)
    - AIによるタスクの登録(Phase3以降)
- 買い出しリスト（Phase2）
- サブスク管理（Phase3以降）
- ソシャゲイベント管理（Phase3以降）
- ガチャ・ポイント・キャラクターカスタマイズ（Phase3以降）


# 【開発フェーズ】

期間と知識が限られてるので、今回は段階に分けて
最低限の機能から実装していく

### Phase1：基礎になる部分とログイン（完了）
ログイン・ユーザー登録・タスク登録・AIリマインド（Gemini API）

※Aiはリマインド、チャットのみに機能を絞って実装

### Phase2：買い出しリストやカレンダーの連携（完了）
Googleカレンダー連携(API,OAuth2)・買い出しリスト
### Phase3：見た目を整える　←★現在進行中
実装した基本機能のページのHTML、CSS、js作成
### 以降：時間が掛かるもの＋追加機能
AI会話からのタスク登録・キャラクターの２D・サブスクの記録・買い出しリストの記録・ガチャ・ポイント等


<br>

## 使用AIと使用方法
クロード：エラー発生時のエラー内容解析、JavaScriptなどの訓練校で学習していない範囲での実装方法検索
Gemini：RestAPIを使用した対話・リマインド文章の作成


## Gemini
チャットとリマインドでは使うデータが違うので、
ChatSvc,ReminderSvcと別々にgeminiを使用。
2.5の無料枠（トークン250kで制限ある）
を使うため各更新を頻繁に行わないようにする
```
chat_histories（DB）
       ↑ 書き込み          ↓ 読み取り
   ChatSvc              ReminderSvc（更新制限）
（chat.htmlの会話）    （home.htmlのリマインド生成）
```
- RestAPIを使うため、GoogleAiStudioからAPIキーを取得
今回は自分のアカウントで取得、ローカルの環境変数（名称固定）に格納
```
GEMINI_API_KEY
```
GeminiPromptConstクラスを作成し、キャラクター設定および
Geminiの応対指示をまとめて記録。

## エラーメモ
- Geminiの制限値まで余裕あるはずなのに制限ですというエラーがでてしまって一回も呼び出せない
＞解決：
そもそもGemini1.5はもうサポートがなくって無料枠が０だからアクセスできない。現行の2.5に変更した。
＞反省：使うツールの現行のモデルはちゃんと調べてから使う

- Geminiにはアクセスできるがすぐに制限値までいってしまってエラーでWhitePage
＞解決：
GeminiをHome.html行くたびに更新してると制限に行くので読み込みを30分に1回に。制限行ったときにも動くようにAPIの送信URLを作るとこにtry-catchを入れて返答なければエラーの時に処理を戻すようにする。
＞反省：APIは外部にアクセスして返答を待つものなので全部try-catchをいれておいたほうがいい・・・

- Jsとcssが反映されない  
＞解決：  
S p r i n g S e c u r i t y フレームワーク、コンフィグで許可出しとく
＞反省：使うだろう拡張子を最初に許可しておけばよかった  

- cssで作ったアニメーションが表示されない・表示が遅い
＞解決：描画開始地点が画面外の遠めの距離に設定されてて映ってなかった。あとｚ軸の制御をcssでかけてなかった。
```
z-index: -1;
通常が０、マイナスに行くにつれて背面
```


## 追加しようかなって機能
・チャットのリセットボタン




### 進捗
CSSを整えながら、
サブスクのエラーを解消中
＞スクレイピングが動的なウェブだと効かないらしい
＞別の方法かもっとシンプルな感じで実装
7/5
一旦サブスクは放置、先に最低限の見た目を整えることに

7/7
サブスクを普通のentityとリポジトリで手動登録する形にすることに。
DBを下記で作り直す
サブスクID、サブスク名、ユーザーID、入会日、更新日
退会と再加入ボタン、再加入時に入会日更新日を再入力させる


## DB~
- users
    - user_id
    - password
    - nickname
    - ast_login_at
    - created_at
    - reminder_cache
    - reminder_generated_at

- tasks
    - task_id
    - user_id
    - title
    - description
    - priority
    - start_date
    - start_time
    - due_date
    - due_time
    - location
    - status
    - google_event_id
    - created_at
    - updated_at

- chat_histories
    - chat_id
    - user_id
    - role
    - content
    - created_at

- shopping_list
    - shopping_id
    - user_id
    - item_name
    - description
    - status
    - created_at
    - updated_at




```
-- =============================================
-- subsk テーブル
-- =============================================
CREATE TABLE IF NOT EXISTS subsk (
    subsk_id    BIGINT          NOT NULL AUTO_INCREMENT,
    subsk_name  VARCHAR(100)    NOT NULL,
    user_id     VARCHAR(255)    NOT NULL,
    joined_at   DATE            NOT NULL,
    update_at  DATE            NOT NULL,
    PRIMARY KEY (subsk_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```
