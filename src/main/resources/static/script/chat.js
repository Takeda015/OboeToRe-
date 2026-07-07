/**
 * chat.htmlのjavascript
 */

//chatBoxを生成する
const chatBox = document.getElementById('chat-box');
chatBox.scrollTop = chatBox.scrollHeight;

//メッセージを追加
function appendMsg(role, text) {
    const div = document.createElement('div');
    div.className = 'msg ' + role;
    div.textContent = text;
    chatBox.appendChild(div);
    chatBox.scrollTop = chatBox.scrollHeight;
}


//時間がかかる処理を止まらずにやる
//ユーザーがメッセージを送信したとき
async function sendMessage() {
    /*HTML全体を見て、Iｄにuser-inputとsend-btnってつけた部分を
    それぞれいい感じの関数にいれる
    */
    const input = document.getElementById('user-input');
    const btn = document.getElementById('send-btn');

    //前後の空白を削除してmessageに格納
    const message = input.value.trim();

    //もじもmessageの中身がなかったら処理を終了。
    if (!message) return;


    //メッセージを追加する、画面に自分のメッセージが表示される
    appendMsg('user', message);

    //インプットの中身をリセット
    input.value = '';

    //下の処理の間ボタンはつかえないようにしとく
    btn.disabled = true;

    //なんかつけとかなきゃいけないらしいcsrfの部分
    const csrfToken = document.getElementById('csrf-token').value;
    const csrfHeader = document.getElementById('csrf-header').value;

    //ここからチャットの応答を入れるとこ
    try {

        //（ChatCtlの）chatにpostでレスポンスを送って返答をもらう
        /*await があるので、HTTPレスポンスのヘッダーが届いた時点で
		返答は res に代入される。
        ただし、この時点で受け取れているのはヘッダーとステータスコードのみ。
        ボディ（実際の返答テキスト）はまだストリーム状態で、
		await res.text() を呼んで初めて全て読み込まれる。 */
        const res = await fetch('/chat', {
            method: 'POST',

            headers: {
                //HTMLのフォームで送りますよ
                'Content-Type': 'application/x-www-form-urlencoded',
                //springsecurityフレームワークのcsrfもつけてますよ
                [csrfHeader]: csrfToken
            },
            //ここで送信します。日本語はエンコードがいるっぽいのでエンコード
            body: 'message=' + encodeURIComponent(message)
        });

        //resがちゃんと定義されてる状態だとres.text()が実行できる

        const reply = await res.text();

        //で受け取ったレスポンスをメッセージに追加する、名前がModelなので、
        //いい名前決まったら後でここも変更かもしれない
        appendMsg('model', reply);


    } catch (e) {
        //あかんかった場合
        appendMsg('model', '通信エラーが発生しました。');
    } finally {
        //最後の処理は送信ボタンをつかえるようにする
        btn.disabled = false;
    }
}

document.getElementById('user-input').addEventListener('keydown', function(e) {
    if (e.key === 'Enter' && !e.shiftKey) {
        e.preventDefault();
        sendMessage();
    }
});