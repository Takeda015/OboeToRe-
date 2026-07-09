/**
 * 
 */


//モーダルの開閉
function openSbskModal() {
    document.getElementById("addSbskModal").style.display = "block";
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

    document.getElementById("editModal").style.display = "block";
}



function toggleUpdateMode(radio) {
    const form = radio.closest("form");
    const isDays = form.querySelector('input[name="updateMode"]:checked').value === "days";

    form.querySelector(".daysGroup").style.display = isDays ? "" : "none";
    form.querySelector(".directGroup").style.display = isDays ? "none" : "";
    form.querySelector('[name="updateDays"]').disabled = !isDays;
    form.querySelector('[name="updateAt"]').disabled = isDays;
}