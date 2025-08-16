package jp.levtech.rookie.tutorial.repository;

import java.util.List;
import java.util.Optional;

import jp.levtech.rookie.tutorial.model.Task;

public interface TaskRepository {

	/**
	 * すべてのタスクを検索する
	 * @return タスク一覧
	 */	
	List<Task>findAll();
	
	/**
	 * タスクを登録する
	 * 
	 * @param task タスク
	 */	
	void register(Task task);
	
	/**
	 * タスクIDからタスクを検索する
	 * 
	 * @param task タスク
	 */
	Optional<Task> findById(long taskId);
	
	/**
	 * タスクを更新する
	 * 
	 * @param task タスク
	 */
	
	void update(Task task);		
	
	/**
	 * タスクを削除する
	 * 
	 * @param task タスク
	 */
	void delete(Task task);
			

}
