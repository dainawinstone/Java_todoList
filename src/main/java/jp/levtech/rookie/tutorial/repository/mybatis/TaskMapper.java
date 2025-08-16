package jp.levtech.rookie.tutorial.repository.mybatis;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import jp.levtech.rookie.tutorial.model.Task;

/**
 * タスクのマッパー
 */

@Mapper
public interface TaskMapper {
	/**
	 * 全てのタスクを検索する
	 */
	List<Task> findAll();
	
	/**
	 * タスクIDからタスクを検索する
	 */
	Optional<Task> findById(long taskId);
	
	/**
	 * タスクを登録する
	 */
	void register(Task task);
	
	/**
	 * タスクを更新する
	 */
	void update(Task task);
	
	/**
	 * タスクを削除する
	 */
	void delete(Task task);
	
	/**
	 * 親子構造を含めた全タスクを再帰的に取得する
	 */
	List<Task> findAllWithChildren();		

}
