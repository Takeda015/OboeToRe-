/**
 * めにゅーのJS切り分けた
 */

//トグルメニュー
function toggleMenu() {
    const menu = document.getElementById('dropdownMenu');
    menu.classList.toggle('hidden');
}

// メニュー外クリックで閉じる
document.addEventListener('click', function(e) {
    const wrapper = document.querySelector('.menu-wrapper');
    if (!wrapper.contains(e.target)) {
        document.getElementById('dropdownMenu').classList.add('hidden');
    }
})