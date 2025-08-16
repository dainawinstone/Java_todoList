package jp.levtech.rookie.tutorial.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jp.levtech.rookie.tutorial.model.Task;

/**
 * タスクをインメモリで管理するリポジトリ
 */
//@Repository
public class InMemoryTaskRepositoryImpl implements TaskRepository {
	private final List<Task>tasks = new ArrayList<>(List.of(
			new Task(1L,1,"勉強する",true,LocalDate.of(2025, 7, 16),null,List.of())			
			));
	
			//次のIDを表すプロパティ
			private long nextId = 2;
			
			/**
			 * 全てのタスクをインメモリから検索する
			 */
			@Override
			public List<Task> findAll(){
				//インメモリで保持しているタスク一覧を返す。
				return new ArrayList<>(tasks);				
			}
			
			/**
			 * タスクIDからインメモリ上のタスクを検索する
			 */
			@Override
			public Optional<Task> findById(long taskId){
				for (Task task : tasks) {
					if(task.getTaskId() ==taskId) {
						return Optional.of(task);
					}
				}
				return Optional.empty();
				
			}
			
			
			
			/**
			 * タスクをインメモリに登録する
			 */
			@Override
			public void register(Task task) {
				Task newTask = new Task(
						nextId++,
						task.getTaskPriority(),
						task.getTaskName(),
						task.isCompleted(),
						task.getDueDate(),
						task.getParentTaskId(),
						task.getSubTasks()
						);
				tasks.add(newTask);
			}
			
			/**
			 * インメモリ上のタスクを更新する
			 */
			@Override
			public void update(Task task) {
				//タスク一覧からタスクIDが等しいタスクを探し出す
				for(int i =0; i<tasks.size();i++) {
					Task oldTask = tasks.get(i);
					if(oldTask.getTaskId() ==task.getTaskId()) {
						tasks.set(i, task);
						break;
					}
				}
			}
			
			/**
			 * インメモリ上からタスクを削除する
			 */
			@Override
			public void delete(Task task) {
				for(int i = 0; i<tasks.size();i++) {
					Task oldTask = tasks.get(i);
					if (oldTask.getTaskId()==task.getTaskId()) {
						tasks.remove(i);
						break;
					}
				}
				
			}
			
			

	
	

}
