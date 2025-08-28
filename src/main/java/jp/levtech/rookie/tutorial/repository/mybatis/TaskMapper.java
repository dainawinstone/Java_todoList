package jp.levtech.rookie.tutorial.repository.mybatis;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.levtech.rookie.tutorial.model.Task;

/**
 * タスクを扱うためのマッパー
 */

@Mapper
public interface TaskMapper {

    /**
     *  親タスク一覧(保管済)を取得する
     *  
     *  @param listId 取得対象のリストID
     *  
     *  @return 親タスクのリスト
     */
    List<Task> findAllByList(@Param("listId") long listId);

    /**
     * 親タスク一覧(保管済)を取得する
     *  
     *  @param listId 取得対象のリストID
     *  
     *  @return 親タスク(archived=trueのリスト
     */    
    List<Task> findAllArchivedByList(@Param("listId") long listId);

    
    /**
     * タスクIDで 1 件取得する
     * 
     * @param taskId　タスクID
     * 
     */
    Optional<Task> findById(@Param("taskId") long taskId);

    
    /**
     * 親IDに紐づく子タスク(未保管)を取得する
     * 
     * @param listId 絞り込み対象のリストID
     * @param parentId 親タスクID
     * 
     * @return 指定親直下の子タスク一覧
     */
    List<Task> findByParentId(@Param("listId") long listId,
                              @Param("parentId") long parentId);

    /**
     * タスクを新規登録する。
     *
     * @param task 登録対象（listId は必須）
     */
    void register(Task task);
    
    /**
     * タスクを更新する
     * @param task　更新内容
     */
    void update(Task task);
    
    /**
     * タスクを削除する
     * @param task　削除対象
     */
    void delete(Task task);

    /* ===== 一括操作（list 単位） ===== */
    
    /**
     * 完了済みタスクを一括削除する（未保管のみ対象）。
     *
     * @param listId 対象リストID
     * @return 削除件数
     */
    int deleteCompleted(@Param("listId") long listId);
    
    /**
     * 完了済みタスクを一括保管（archived=true に更新）する。
     *
     * @param listId 対象リストID
     * @return 更新件数
     */
    int archiveCompleted(@Param("listId") long listId);
    
    /**
     * 保管済みタスクを 1 件復元する（list スコープを満たすもののみ）。
     *
     * @param listId 対象リストID
     * @param id     復元するタスクID
     * @return 更新件数（0 の場合は対象なし）
     */
    int restoreArchivedById(@Param("listId") long listId, @Param("id") long id);
}
