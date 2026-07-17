/**
 * 
 */


//モーダルの開閉
function openSbskModal() {
    document.getElementById("addSbskModal").style.display = "flex";
}
function closeSbskModal() {
    document.getElementById("addSbskModal").style.display = "none";
}
//function openEditModal() {
//    document.getElementById("editModal").style.display = "block";
//}
function closeEditModal() {
    document.getElementById("editModal").style.display = "none";
}

//編集ボタンを開いた
function openEditModal(btn) {

    const form = document.querySelector("#editModal form");

    form.querySelector('[name="subskId"]').value = btn.dataset.subskid;
    form.querySelector('[name="subskName"]').value = btn.dataset.subskname;
    form.querySelector('[name="joinedAt"]').value = btn.dataset.joinedat;

    // 既存データは「直接指定」モードで開く
    const directRadio = form.querySelector('input[name="updateMode"][value="direct"]');
    directRadio.checked = true;
    form.querySelector('[name="updateAt"]').value = btn.dataset.updateat;
    toggleUpdateMode(directRadio);

    document.getElementById("editModal").style.display = "flex";
}


//削除セット============================================
//削除するボタン押した
function delSbsk() {
    const ids = getCheckedIds();//IDのリストに中身ある？
    if (ids.length === 0) { alert('削除するサブスクを選択してください'); return; }
    if (confirm(ids.length + '件削除しますか？')) {
        appendIdsToForm('delSbskForm', ids);
        document.getElementById('delSbskForm').submit();
    }
}
function getCheckedIds() {
    return Array.from(document.querySelectorAll('.sbskCheck:checked')).map(cb => cb.value);
}
//選択したIDを渡す。
function appendIdsToForm(formId, ids) {
    const form = document.getElementById(formId);
    form.querySelectorAll('input[name="subskId"]').forEach(el => el.remove());
    ids.forEach(id => {
        const input = document.createElement('input');
        input.type = 'hidden';
        input.name = 'subskId';
        input.value = id;
        form.appendChild(input);
    });
}
//削除セット終わり=======================



//ここで更新日の入力方法を変える処理出してる
function toggleUpdateMode(radio) {
    const form = radio.closest("form");
    const isDays = form.querySelector('input[name="updateMode"]:checked').value === "days";

    form.querySelector(".daysGroup").style.display = isDays ? "" : "none";
    form.querySelector(".directGroup").style.display = isDays ? "none" : "";
    form.querySelector('[name="updateDays"]').disabled = !isDays;
    form.querySelector('[name="updateAt"]').disabled = isDays;
}

//行クリックでチェック切り替え＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝

 function enableRowClickToggle() {
     document.querySelectorAll('table tbody').forEach(tbody => {
         tbody.addEventListener('click', (e) => {
             
 			//以下のクラスは動作を除外
 			if (e.target.closest('.editBtn')) return;

             const row = e.target.closest('tr');
             if (!row) return;

             const checkbox = row.querySelector('.sbskCheck');
             if (!checkbox) return;

             // チェックボックス自体のクリックは標準動作に任せる（二重トグル防止）
             if (e.target === checkbox) return;

             checkbox.checked = !checkbox.checked;
         });
     });
 }

 enableRowClickToggle()