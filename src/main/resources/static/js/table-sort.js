
//昇順・降順の状態を保持するオブジェクト

let sortDirection = {};

/**
 * TodoListのテーブルをソートする機能
 */
function sortTable(columnIndex){
	const table=document.getElementById("todoTable");
	const tbody = table.tBodies[0];
	const rows = Array.from(tbody.querySelectorAll("tr"));
	
	//昇順・降順の切り替え	
	const currentDir = sortDirection[columnIndex] || "asc"; 
	const newDir = currentDir === "asc" ? "desc" : "asc";
	sortDirection[columnIndex] = newDir;
	
	//ソート処理
	rows.sort((a,b) => {
		const aText = a.children[columnIndex].textContent.trim();
		const bText = b.children[columnIndex].textContent.trim();
		
		const aNum = parseFloat(aText);
		const bNum = parseFloat(bText);
		const isNumeric = !isNaN(aNum)&& !isNaN(bNum);
		
		if(isNumeric){
			return newDir === "asc" ? aNum - bNum : bNum - aNum;
		}else{
			return newDir ==="asc"
				? aText.localeCompare(bText)
				: bText.localeCompare(aText);		
		}				
	});
	
	rows.forEach(row =>tbody.appendChild(row));	
	
	//矢印マークの更新処理
	const arrows = document.querySelectorAll("th span[id^='arrow']");
	arrows.forEach(span => span.textContent = "");
	const arrow = document.getElementById(`arrow${columnIndex}`);
	arrow.textContent = newDir ==="asc" ? "▲": "▼";
				
	
 }