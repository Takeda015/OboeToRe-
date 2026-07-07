/**
 * peofileのJavaScript
 */
//モーダル出したりしまったりする
function changeNickModal() {
    document.getElementById("nickModal").style.display = "block";
}
function closeChangeNickModal() {
    document.getElementById("nickModal").style.display = "none";
}

function changePassModal() {
    document.getElementById("passModal").style.display = "block";
}
function closeChangePassModal() {
    document.getElementById("passModal").style.display = "none";
}

// ＝＝＝＝ ニックネームフォームのバリデーション ＝＝＝＝
document.getElementById('nickForm').addEventListener('submit', function(e) {
    const nickname = document.getElementById('newNickname').value.trim();
    const nickEmptyError = document.getElementById('nickEmptyError');

    //とりあえず空入力はダメ
    if (nickname === '') {
        nickEmptyError.style.display = 'block';
        e.preventDefault();
    } else {
        nickEmptyError.style.display = 'none';
    }
});

// ＝＝＝＝ パスワードフォームのバリデーション ＝＝＝＝
const passwordInput = document.querySelector('input[name="password"]');
const password2Input = document.querySelector('input[name="password2"]');
const PassErrorMsg = document.getElementById('changePasswordError');
const PLErrorMsg = document.getElementById('passwordLengthError');

//正規表現のパターン：今回は大文字含むアルファベットと数字
const alphanumericPattern = /^[a-zA-Z0-9]+$/;

// 入力中のリアルタイムチェック
function checkPasswordMatch() {
    const pw1 = passwordInput.value;
    const pw2 = password2Input.value;

    //４文字以下かつ０文字以上（なんか入力してる）
    if (pw1.length > 0 && pw1.length < 4) {
        PLErrorMsg.style.display = 'block';//エラー出す
        PassErrorMsg.style.display = 'none';
        return;


    } else {
        //入ってるならエラー出さない
        PLErrorMsg.style.display = 'none';
    }

    //何も入力してない
    if (pw1 === '' || pw2 === '') {
        PassErrorMsg.style.display = 'none';
        return;
    }

    PassErrorMsg.style.display = (pw1 !== pw2) ? 'block' : 'none';
}

passwordInput.addEventListener('input', checkPasswordMatch);
password2Input.addEventListener('input', checkPasswordMatch);

// 送信時の最終チェック
document.getElementById('passForm').addEventListener('submit', function(e) {
    const currentPass = document.querySelector('input[name="currentPass"]').value;
    const pw1 = passwordInput.value;
    const pw2 = password2Input.value;
    let hasError = false;

    // 現在のパスワード未入力
    if (currentPass === '') {
        document.getElementById('currentPassError').style.display = 'block';
        hasError = true;
    } else {
        document.getElementById('currentPassError').style.display = 'none';
    }

    // 半角英数字チェック・4文字以上チェック
    if (!alphanumericPattern.test(pw1) || pw1.length < 4) {
        PLErrorMsg.style.display = 'block';
        hasError = true;
    } else {
        PLErrorMsg.style.display = 'none';
    }

    // 一致チェック
    if (pw1 !== pw2) {
        PassErrorMsg.style.display = 'block';
        hasError = true;
    } else {
        PassErrorMsg.style.display = 'none';
    }

    if (hasError) e.preventDefault();
});

// ＝＝＝＝ サーバーエラー時のモーダル再表示 ＝＝＝＝
const openModal = /*[[${openModal}]]*/ null;
if (openModal === 'nick') changeNickModal();
if (openModal === 'pass') changePassModal();