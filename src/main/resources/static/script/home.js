/**
 * homeのjavaScript
 */



function updateDatetimeTicker() {
    const now = new Date();
    const weekdays = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];
    const text = 'Japan’s current date and time　:　'+
	`${now.getFullYear()}／${now.getMonth() + 1}／${now.getDate()}　${weekdays[now.getDay()]}　${String(now.getHours()).padStart(2, '0')}:${String(now.getMinutes()).padStart(2, '0')}`;


    document.getElementById('datetimeText').textContent = text;
}

updateDatetimeTicker();
setInterval(updateDatetimeTicker, 1000);



//モーダル＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝－

//タスクモーダル
function openTaskModal() {
    document.getElementById("taskModal").style.display = "flex";
}
function closeTaskModal() {
    document.getElementById("taskModal").style.display = "none";
}

//買い物リストモーダル
function openSListModal() {
    document.getElementById("sListModal").style.display = "flex";
}
function closeSListModal() {
    document.getElementById("sListModal").style.display = "none";
}





//==============================================

//タスクを完了にする==============================

//タスクIDはいってます？
function getCheckedTaskIds() {
    return Array.from(document.querySelectorAll('.taskCheck:checked')).map(cb => cb.value);
}


function appendIdsToTaskForm(formId, ids) {
    const form = document.getElementById(formId);
    form.querySelectorAll('input[name="taskIds"]').forEach(el => el.remove());
    ids.forEach(id => {
        const input = document.createElement('input');
        input.type = 'hidden';
        input.name = 'taskIds';
        input.value = id;
        form.appendChild(input);
    });
}

//ステータス変更するタスクの確認(タスクの方)
function changeTaskStatus(status) {
    const ids = getCheckedTaskIds();
    if (ids.length === 0) { alert('変更するタスクを選択してください'); return; }
    const label = status === 'DONE' ? '完了' : '未完了';
    if (confirm(ids.length + '件を「' + label + '」に変更しますか？')) {
        document.getElementById('statusValue').value = status;
        appendIdsToTaskForm('statusForm', ids);
        document.getElementById('statusForm').submit();
    }
}

//＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝－
//class="sListCheck" th:value="${todoSLists.shoppingId}

//タスクIDはいってます？
function getCheckedSpngIds() {
    return Array.from(document.querySelectorAll('.sListCheck:checked')).map(cb => cb.value);
}

function appendIdsToSpngForm(formId, ids) {
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
//ステータス変更するタスクの確認(タスクの方)
function changeSpngStatus(status) {
    const ids = getCheckedSpngIds();
    if (ids.length === 0) { alert('変更するもの選択してください'); return; }
    const label = status === 'DONE' ? '完了' : '未完了';
    if (confirm(ids.length + '件を「' + label + '」に変更しますか？')) {
        document.getElementById('sListStatusValue').value = status;
        appendIdsToSpngForm('sListStatusForm', ids);
        document.getElementById('sListStatusForm').submit();
    }
}

//行クリックでチェック切り替え＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝

function enableRowClickToggle() {
    document.querySelectorAll('table tbody').forEach(tbody => {
        tbody.addEventListener('click', (e) => {
            // リンク（グーグルマップ等）クリック時は何もしない
            if (e.target.tagName === 'A') return;

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

;