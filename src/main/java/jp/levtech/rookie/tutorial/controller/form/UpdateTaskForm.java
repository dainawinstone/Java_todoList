package jp.levtech.rookie.tutorial.controller.form;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateTaskForm {
	/**
	 * タスク名を表すプロパティ
	 */
	@NotBlank(message="タスク名は必須です")
	private String taskName;
	
	/**
	 * タスクが完了したかを表すプロパティ
	 */
	private boolean completed;
	
	/**
	 * タスクの優先度を表すプロパティ
	 */	
	@NotNull(message = "優先度は必須です") 
	private Integer taskPriority;
	
	/**
	 * タスクの期日を表すプロパティ
	 */
	@NotNull(message = "期限日は必須です")
	private LocalDate dueDate;

}
