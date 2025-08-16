package jp.levtech.rookie.tutorial.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import jp.levtech.rookie.tutorial.controller.form.CreateTaskForm;
import jp.levtech.rookie.tutorial.controller.form.UpdateTaskForm;
import jp.levtech.rookie.tutorial.model.Task;
import jp.levtech.rookie.tutorial.repository.TaskRepository;


/**
 * Todoを管理するコントローラー
 */

@Controller
public class TodoController {
	
	/**
	 * タスクを管理するリポジトリ
	 */
	private final TaskRepository taskRepository;
	
	/**
	 * Todoを管理するコントローラのコンストラクト
	 */
	public TodoController(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}
	
	
	/**
	 * Todo一覧画面を表示する
	 * 
	 * @param model
	 * @return TodoHome
	 * 
	 */
	@GetMapping("/todo/")
	public String showTodoList(Model model) {
		List<Task> allTasks = taskRepository.findAll();	
		
		//子タスクをparentTaskIdごとにグループ化
		Map<Long, List<Task>> groupedByParent =allTasks.stream()
			.filter(task ->task.getParentTaskId()!=null)
			.collect(Collectors.groupingBy(Task::getParentTaskId));
		
		//親タスクに子タスクをセット
		for(Task task : allTasks) {
			if(groupedByParent.containsKey(task.getTaskId())) {
				task.setSubTasks(groupedByParent.get(task.getTaskId()));
			}else {
				task.setSubTasks(List.of());
			}
		}
		
		//親タスクのみビューを渡す
		List<Task> parentTasks =allTasks.stream()
			.filter(task -> task.getParentTaskId()==null)
			.collect(Collectors.toList());
		
		model.addAttribute("tasks",parentTasks); 
		return "TodoHome";		
	}
	/**
	 * Todo登録画面を表示する
	 * 
	 * @param model モデル
	 * @return TodoCreate
	 * 
	 */
	@GetMapping("/todo/create")
	public String ShowTodoCreate(Model model) {
		CreateTaskForm createTaskForm = new CreateTaskForm(); /*初期値を空にする*/
		createTaskForm.setDueDate(LocalDate.now());
		model.addAttribute("createTaskForm",createTaskForm);
		return "TodoCreate";		
	}	
	
	/**
	 * タスクを登録する
	 */
	@PostMapping("/todo/register")
	public String register(
			@ModelAttribute("createTaskForm")@Valid CreateTaskForm createTaskForm,
			BindingResult bindingResult,
			Model model){
		
		/*入力エラーがある場合、作成画面に再表示する*/
		if (bindingResult.hasErrors()) {
			return "TodoCreate";
		}
		
		Task task = new Task(
				0L,
				createTaskForm.getTaskPriority(),
				createTaskForm.getTaskName(),
				false,
				createTaskForm.getDueDate(),
				createTaskForm.getParentTaskId(),
				List.of()								
				);
		
		taskRepository.register(task);
		return "redirect:/todo/";
		
	}
	
	/**
	 * タスクを編集する
	 * 
	 * @param taskId タスクID
	 * @param model モデル
	 * @return TodoEdit
	 */
	@GetMapping("/todo/{taskId}/edit")
	public String showEdit(@PathVariable long taskId,Model model) {
		Optional<Task>taskOptional = taskRepository.findById(taskId);
		
		if (taskOptional.isEmpty()) {
			throw new TaskNotFoundException("ID:" + taskId+"のタスクが見つかりません");
		}
		
		Task task= taskOptional.get();
		
		UpdateTaskForm updateTaskForm =new UpdateTaskForm(
				task.getTaskName(),
		        task.isCompleted(),
		        task.getTaskPriority(),
		        task.getDueDate()			
				);
		model.addAttribute("task",taskOptional.get());
		model.addAttribute("updateTaskForm",updateTaskForm);
		model.addAttribute("taskId",taskId);
		
		return "TodoEdit";
	}	
		
	
	/**
	 * タスクを更新する
	 * @param taskId タスクID
	 */
	@PostMapping("/todo/{taskId}/update")
	public String update(
		@PathVariable long taskId,
		@Validated @ModelAttribute("updateTaskForm")UpdateTaskForm updateTaskForm,
		BindingResult bindingResult,
		Model model){
			if(bindingResult.hasErrors()) {
				model.addAttribute("taskId",taskId);
				Optional<Task> taskOptional = taskRepository.findById(taskId);
		        taskOptional.ifPresent(task -> model.addAttribute("task", task));
				return "TodoEdit";
			}
		
		 
			Optional<Task> existingTaskOptional = taskRepository.findById(taskId);
			
			if(existingTaskOptional.isEmpty()) {
				throw  new TaskNotFoundException("ID:" + taskId+"のタスクを更新できません");
			}
			
			Task existingTask = existingTaskOptional.get();
		
			Task updateTask = new Task(
					taskId,
					updateTaskForm.getTaskPriority(),
					updateTaskForm.getTaskName(),
					updateTaskForm.isCompleted(),
					updateTaskForm.getDueDate(),
					existingTask.getParentTaskId(),
					List.of()
			);			
			model.addAttribute("updateTaskForm",updateTaskForm);
			model.addAttribute("taskId",taskId);
		
		//DB更新
		taskRepository.update(updateTask);
		return "redirect:/todo/";
				
						
					
	}
	
	/*
	 * @RequestParam String taskName,
		@RequestParam(defaultValue="3")Integer taskPriority,
		@RequestParam(defaultValue = "false")boolean completed,
		@RequestParam(required=false) LocalDate dueDate)
	 */
		
	
	/**
	 * サブタスクを追加する
	 */
	@GetMapping("/todo/{parentId}/subtask/create")
	public String createSubtask(@PathVariable long parentId, Model model) {
	    CreateTaskForm createTaskForm = new CreateTaskForm();
	    createTaskForm.setParentTaskId(parentId); // 親タスクIDを事前にセット
	    model.addAttribute("createTaskForm", createTaskForm); // ★フォーム名に注意！
	    return "TodoSubTask";
	}
				 	
	
	
	/**
	 * タスクを削除する
	 * 
	 * @param taskId タスクID
	 * @return リダイレクト 
	 */
	@PostMapping("/todo/{taskId}/delete")
	public String delete(@PathVariable long taskId) {
		Optional<Task> task = taskRepository.findById(taskId);
		if(task.isEmpty()) {
			throw new TaskNotFoundException("ID:"+taskId+"のタスクを削除できません");						
		}
		taskRepository.delete(task.get());
		return "redirect:/todo/";
	}
	
							
	
	/**
	 * タスクが存在しなかったことを表す例外
	 */
	@ResponseStatus(value = HttpStatus.NOT_FOUND,reason = "タスクが見つかりません")
	private class TaskNotFoundException extends RuntimeException{
		public TaskNotFoundException(String message) {
			super(message);
		}	
	}
	
	


}


