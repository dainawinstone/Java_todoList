package jp.levtech.rookie.tutorial.controller.form;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

/**
 * タスクを作成するためのフォーム
 */

@Data
public class CreateTaskForm {
	/**
	 * タスク名を表すプロパティ
	 */
	@NotBlank(message ="タスク名は必須です")
	private String taskName;
	
	/**
	 * タスクの優先度を表すプロパティ
	 */	
	@NotNull(message = "優先度は必須です") 
	private Integer taskPriority;
	
	/*
	 * 締切日を表すプロパティ
	 */
	
	@NotNull(message = "締切日は必須です")
	private LocalDate dueDate;
	
	/*
	 * 親IDを表すプロパティ
	 */
	
	private Long parentTaskId;
	
	/*
	 * タスクが完了したかどうかを表すプロパティ
	 */
	private boolean completed;
	
	

}
