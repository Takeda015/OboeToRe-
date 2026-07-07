/**
 * registerのJavaScript
 * 
 */

//入力されたIDがすでに登録されてるかどうかをリアタイで返す
//＝＝＝＝送信ボタンの制御（共通）＝＝＝＝＝	
let isUserIdValid = false;
let isPasswordValid = false;

const submitBtn = document.getElementById('submitBtn');

function updateSubmitButton() {
    submitBtn.disabled = !(isUserIdValid && isPasswordValid);
}


//==========IDの既存判定==================

const usernameInput = document.getElementById('user_id');
const IdErrorMsg = document.getElementById('usernameError');
const IdLenErrorMsg = document.getElementById('uIDLengthError');

// 英字以外をリアルタイムで削除
usernameInput.addEventListener('input', function() {
    this.value = this.value.replace(/[^A-Za-z0-9]/g, '');
});


// inputイベント: 文字を入力するたびに実行
usernameInput.addEventListener('input', async function() {
    const user_id = this.value;


    //なんか入力したとき、４文字以下だとメッセージ
    if (user_id.length > 0 && user_id.length < 4) {
        IdLenErrorMsg.style.display = 'block';
        isUserIdValid = false;
        updateSubmitButton();
        return;
    } else {
        IdLenErrorMsg.style.display = 'none';
    }


    // 未入力の場合はチェックしない
    if (user_id === '') {
        IdErrorMsg.style.display = 'none';
        isUserIdValid = false;
        updateSubmitButton();
        return;
    }


    // DBサーバーへ問い合わせ
    // 引数: /HTML名/check-user_id?user_id=入力値
    //★ここでrepositorのメソッドを呼び出して参照して判定してる
    const response = await fetch(`/register/check-user_id?user_id=${encodeURIComponent(user_id)}`);

    // 戻り値: {"exists": true/false}
    const data = await response.json();

    if (data.exists) {//data.existsがtrue
        // 重複あり: 警告表示 + 送信ボタン無効化
        IdErrorMsg.style.display = 'block';
        isUserIdValid = false;
    } else {
        // 重複なし: 警告非表示 + 送信ボタン有効化
        IdErrorMsg.style.display = 'none';
        isUserIdValid = true;
    }
    updateSubmitButton();
});


//＝＝＝＝再入力のパスワードがパスワードと一緒かどうか＝＝＝＝＝＝

//String型で入力内容を受け取る
const passwordInput = document.querySelector('input[name="password"]');
const password2Input = document.querySelector('input[name="password2"]');

const PassErrorMsg = document.getElementById('passwordError');
const PLErrorMsg = document.getElementById('passwordLengthError');


// inputイベント: 文字を入力するたびに実行
function checkPasswordMatch() {
    const pw1 = passwordInput.value;
    const pw2 = password2Input.value;

    //なんか入力したとき、４文字以下だとメッセージ
    if (pw1.length > 0 && pw1.length < 4) {
        PLErrorMsg.style.display = 'block';
        PassErrorMsg.style.display = 'none';
        isPasswordValid = false;
        updateSubmitButton();
        return;
    } else {
        PLErrorMsg.style.display = 'none';
    }


    // 未入力の場合はチェックしない
    if (pw1 === '' || pw2 === '') {
        PassErrorMsg.style.display = 'none';
        isPasswordValid = false
        updateSubmitButton();
        return;
    }

    if (pw1 !== pw2) {//入力が違う

        // 重複あり: 警告表示 + 送信ボタン無効化
        PassErrorMsg.style.display = 'block';
        isPasswordValid = false;
    } else {
        // 重複なし: 警告非表示 + 送信ボタン有効化
        PassErrorMsg.style.display = 'none';
        isPasswordValid = true;
    }
    updateSubmitButton();
}

// パスワード入力時にチェック
passwordInput.addEventListener('input', checkPasswordMatch);
password2Input.addEventListener('input', checkPasswordMatch);
