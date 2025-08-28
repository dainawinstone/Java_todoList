package jp.levtech.rookie.tutorial.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import jp.levtech.rookie.tutorial.model.Task;
import jp.levtech.rookie.tutorial.repository.mybatis.TaskMapper;
import lombok.RequiredArgsConstructor;

/**
 * タスクを扱うリポジトリの実相
 */
@Primary
@Repository
@RequiredArgsConstructor
public class DatabaseTaskRepositoryImpl implements TaskRepository {

    private final TaskMapper taskMapper;

    /* ====== 取得 ====== */
    
    /**
     * 未保管（archived=false）の親タスクを list 単位で取得する。
     * 子タスクは Mapper 側で collection により合流させる想定。
     *
     * @param listId 取得対象のリストID
     * @return 親タスク一覧
     */
    @Override
    public List<Task> findAllByList(long listId) {
        return taskMapper.findAllByList(listId);
    }

    /**
     * 保管済（archived=true）の親タスクを list 単位で取得する。
     *
     * @param listId 取得対象のリストID
     * @return 親タスク一覧（保管済）
     */
    @Override
    public List<Task> findAllArchivedByList(long listId) {
        return taskMapper.findAllArchivedByList(listId);
    }
    
    /**
     * taskId で 1 件取得する。
     *
     * @param taskId タスクID
     * @return 該当があれば Optional、なければ Optional.empty()
     */
    @Override
    public Optional<Task> findById(long taskId) {
        return taskMapper.findById(taskId);
    }

    /* ====== 変更 ====== */
    
    /**
     * タスクを新規登録する。
     * 
     * @param task 新規登録
     */
    @Override
    public void register(Task task) {
        taskMapper.register(task);
    }
    
    /**
     * タスクを更新する
     * 
     * @param task 更新内容
     */
    @Override
    public void update(Task task) {
        taskMapper.update(task);
    }
    
    /**
     * タスクを削除する
     * 
     * @param task 削除対象
     */
    @Override
    public void delete(Task task) {
        taskMapper.delete(task);
    }

    /* ====== 一括処理（list 単位） ====== */
    
    /**
     * 完了済み（completed=true）で未保管（archived=false）のタスクを list 単位で一括削除する。
     *
     * @param listId 対象リストID
     * @return 削除件数
     */
    @Override
    public int deleteCompleted(long listId) {
        return taskMapper.deleteCompleted(listId);
    }
    
    /**
     * 完了済みタスクを一括で保管（archived=true に更新）する。
     *
     * @param listId 対象リストID
     * @return 更新件数
     */
    @Override
    public int archiveCompleted(long listId) {
        return taskMapper.archiveCompleted(listId);
    }
    
    /**
     * 保管済みタスクを 1 件復元（archived=false）する。
     * 
     * @param listId 対象リストID
     * @param id     復元対象の taskId
     * @return 更新件数（0 の場合は対象なし）
     */
    @Override
    public int restoreArchivedById(long listId, long id) {
        return taskMapper.restoreArchivedById(listId, id);
    }
}
