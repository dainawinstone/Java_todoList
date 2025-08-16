/**
 * チェックボックスによってクラスをつける
 */

function toggleComplete(checkbox){
	const row = checkbox.closest("tr");
	if(checkbox.checked){
		row.classList.add("completed");
	}else{
		roe.classList.remove("completed")
	}
}

//ページ読み込み時に初期状態で完了行にクラス付与

document.addEventListener("DOMContentLoaded",function(){
	document.querySelectorAll('input[type="checkbox"][data-task-id]').forEach(function(checkbox){
		if(checkbox.checked){
			checkbox.closest("tr").classList.add("completed")
		}
	});
});	
