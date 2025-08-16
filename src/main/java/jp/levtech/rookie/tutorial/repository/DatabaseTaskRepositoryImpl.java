package jp.levtech.rookie.tutorial.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import jp.levtech.rookie.tutorial.model.Task;
import jp.levtech.rookie.tutorial.repository.mybatis.TaskMapper;

@Primary
@Repository
public class DatabaseTaskRepositoryImpl  implements TaskRepository{
	/**
	 * タスクのマッパー
	 */
	private final TaskMapper taskMapper;
	
	/**
	 * タスクをデータベースで管理するリポジトリのコンストラクタ
	 * 
	 * @param taskMapper タスクのマッパー
	 */
	public DatabaseTaskRepositoryImpl(TaskMapper taskMapper) {
		//タスクのマッパーを初期化する
		this.taskMapper = taskMapper;
	}
	
	/**
	 * 全てのタスクをデータベースから検索する
	 */
	@Override
	public List<Task> findAll(){
		return taskMapper.findAll();
	}
	
	/**
	 * タスクIDからデータベース上のタスクを検索する
	 */
	@Override
	public Optional<Task> findById(long taskId){
		return taskMapper.findById(taskId);
	}
	
	
	/**
	 * タスクをデータベースに登録する
	 */
	@Override
	public void register(Task task) {
		taskMapper.register(task);
	}
	
	
	/**
	 * タスクをデータベースに更新する
	 */
	@Override
	public void update(Task task) {
		taskMapper.update(task);
	}
	
	/**
	 * データベース上のタスクを削除する
	 */
	@Override
	public void delete(Task task) {
		taskMapper.delete(task);
	}

}
