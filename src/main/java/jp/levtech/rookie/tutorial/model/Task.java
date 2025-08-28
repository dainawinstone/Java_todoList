package jp.levtech.rookie.tutorial.model;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * タスクを表すモデル
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {
	/**
	 * タスクIDを表すプロパティ
	 */
	private  Long taskId;
	
	/**
	 * タスクの優先度表すプロパティ
	 */
	private  Integer taskPriority;
	
	/**
	 * タスク名を表すプロパティ
	 */
	private  String taskName;
	
	/**
	 * タスクが完了したかを表すプロパティ
	 */
	private boolean completed;
	
	/**
	 * タスクの締切を表すプロパティ
	 */
	private  LocalDate dueDate;		
	
	/**
	 * 親タスクIDを表すプロパティ
	 */
	private  Long parentTaskId;
	
	/**
	 * 子タスク一覧
	 */
	private  List<Task> subTasks = new ArrayList<>();
	
	/**
	 * 保管フラグのプロパティ
	 */
	private boolean archived;
	
	/**
	 * 保管日時を表すプロパティ
	 */
	private OffsetDateTime archivedAt;
	
	/*
	 * リストIDを表すプロパティ
	 */	
	private Long listId;

}
