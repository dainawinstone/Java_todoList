/**
 * チェックボックスによって完了クラスを付け外し＋サーバーに保存
 */
function toggleComplete(checkbox) {
  const row = checkbox.closest("tr");
  const done = checkbox.checked;

  // 先にUI反映
  row.classList.toggle("completed", done);

  // 保存（POST）用の情報
  const taskId = checkbox.dataset.taskId;
  const tokenEl  = document.querySelector('meta[name="_csrf"]');
  const headerEl = document.querySelector('meta[name="_csrf_header"]');
  const token  = tokenEl  ? tokenEl.content  : "";
  const header = headerEl ? headerEl.content : "X-CSRF-TOKEN";

  // サーバーへ保存
  fetch(`/todo/${taskId}/complete`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      [header]: token
    },
    body: JSON.stringify({ completed: done })
  })
  .then(res => {
    if (!res.ok) throw new Error("save failed");
  })
  .catch(err => {
    // 失敗したら元に戻す
    checkbox.checked = !done;
    row.classList.toggle("completed", !done);
    alert("完了状態の保存に失敗しました。");
    console.error(err);
  });
}

// ページ初期化時：既に完了の行にクラス付与
document.addEventListener("DOMContentLoaded", function () {
  document
    .querySelectorAll('input[type="checkbox"][data-task-id]')
    .forEach(function (checkbox) {
      checkbox.closest("tr").classList.toggle("completed", checkbox.checked);
    });
});
