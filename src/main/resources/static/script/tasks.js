/**
 * tasks.htmlのjavascript
 */

//タスクチェックでチェック入れたやつをもってくる
function getCheckedIds() {
    return Array.from(document.querySelectorAll('.taskCheck:checked')).map(cb => cb.value);
}

//タスクIDを渡す。
function appendIdsToForm(formId, ids) {
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

//削除するタスクの確認
function confirmDelete() {
    const ids = getCheckedIds();//IDのリストに中身ある？
    if (ids.length === 0) { alert('削除するタスクを選択してください'); return; }
    if (confirm(ids.length + '件のタスクを削除しますか？')) {
        appendIdsToForm('deleteForm', ids);
        document.getElementById('deleteForm').submit();
    }
}

//doneのタスクを削除する
function allDoneDelete() {

    if (confirm('完了済みのタスクをすべて削除しますか？')) {
        document.getElementById('doneDeleteForm').submit();
    }

}


//ステータス変更するタスクの確認
function changeStatus(status) {
    const ids = getCheckedIds();
    if (ids.length === 0) { alert('変更するタスクを選択してください'); return; }
    const label = status === 'DONE' ? '完了' : '未完了';
    if (confirm(ids.length + '件を「' + label + '」に変更しますか？')) {
        document.getElementById('statusValue').value = status;
        appendIdsToForm('statusForm', ids);
        document.getElementById('statusForm').submit();
    }
}

//ボタン押したときに出すやつ、これはタスク追加するモーダル
function openModal() { document.getElementById('taskModal').style.display = 'flex'; }
function closeModal() { document.getElementById('taskModal').style.display = 'none'; }

//編集するボタンのモーダル
function openEditModal(taskId, title, description, priority, startDate, startTime, dueDate, dueTime, location) {
    document.getElementById('editTaskId').value = taskId;
    document.getElementById('editTitle').value = title;
    document.getElementById('editDescription').value = description;
    document.getElementById('editPriority').value = priority;
    document.getElementById('editStartDate').value = startDate;
    document.getElementById('editStartTime').value = startTime;
    document.getElementById('editDueDate').value = dueDate;
    document.getElementById('editDueTime').value = dueTime;
    document.getElementById('editLocation').value = location;
    document.getElementById('editModal').style.display = 'flex';
}
function closeEditModal() { document.getElementById('editModal').style.display = 'none'; }

function openEditModalFromBtn(btn) {
    openEditModal(
        btn.dataset.taskId,
        btn.dataset.title,
        btn.dataset.description || '',
        btn.dataset.priority,
        btn.dataset.startDate || '',
        btn.dataset.startTime || '',
        btn.dataset.dueDate || '',
        btn.dataset.dueTime || '',
        btn.dataset.location || ''
    );
}



// DONE行をグレーアウト
window.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('tbody tr').forEach(row => {
        if (row.children[1].innerText === 'DONE') {
            row.style.backgroundColor = '#e0e0e0';
            row.style.color = '#777';
        }
    });
});
