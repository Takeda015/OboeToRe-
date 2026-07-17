/**
 *shoppingList.htmlのJavascript 
 */

 
// クラスsListCheckで選択されたものを全部取得して配列にする
 function getCheckedIds() {
     return Array.from(document.querySelectorAll('.sListCheck:checked')).map(cb => cb.value);
 }
 
 function delSList(){
	const ids = getCheckedIds();//IDのリストに中身ある？
	if (ids.length === 0) { alert('削除する物品を選択してください'); return; }
	if (confirm(ids.length + '件削除しますか？')) {
	         appendIdsToForm('delSLForm', ids);
	         document.getElementById('delSLForm').submit();
	     }
 }
 
 
 //選択したIDを渡す。
 function appendIdsToForm(formId, ids) {
     const form = document.getElementById(formId);
     form.querySelectorAll('input[name="shoppingIds"]').forEach(el => el.remove());
     ids.forEach(id => {
         const input = document.createElement('input');
         input.type = 'hidden';
         input.name = 'shoppingIds';
         input.value = id;
         form.appendChild(input);
     });
 }
 
 
 //ステータス変更するタスクの確認
 function changeStatus(status) {
     const ids = getCheckedIds();
     if (ids.length === 0) { alert('変更する物品を選択してください'); return; }
     const label = status === 'DONE' ? '完了' : '未完了';
     if (confirm(ids.length + '件を「' + label + '」に変更しますか？')) {
         document.getElementById('statusValue').value = status;
         appendIdsToForm('statusForm', ids);
         document.getElementById('statusForm').submit();
     }
 }
 
 
 // DONE行をグレーアウト
 window.addEventListener('DOMContentLoaded', () => {
     document.querySelectorAll('tbody tr').forEach(row => {
         if (row.children[1].innerText.trim() === 'DONE') {
             row.style.backgroundColor = '#e0e0e0';
             row.style.color = '#777';
         }
     });
 });
 
 
 
 //買い物リストモーダル
 function openSListModal() {
     document.getElementById("sListModal").style.display = "flex";
 }
 function closeSListModal() {
     document.getElementById("sListModal").style.display = "none";
 }
 
 
 //行クリックでチェック切り替え＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝

 function enableRowClickToggle() {
     document.querySelectorAll('table tbody').forEach(tbody => {
         tbody.addEventListener('click', (e) => {
             
 			//以下のクラスは動作を除外
 			if (e.target.closest('a, .editBtn')) return;

             const row = e.target.closest('tr');
             if (!row) return;

             const checkbox = row.querySelector('.taskCheck, .sListCheck');
             if (!checkbox) return;

             // チェックボックス自体のクリックは標準動作に任せる（二重トグル防止）
             if (e.target === checkbox) return;

             checkbox.checked = !checkbox.checked;
         });
     });
 }

 enableRowClickToggle()