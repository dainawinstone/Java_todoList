package jp.levtech.rookie.tutorial.repository;

/**
 * タスク永続化の抽象インターフェース。
 */
public interface TaskRepository {

  /**
   * 未保管（archived=false）の親タスク一覧を list 単位で取得する。
   * 
   * @param listId 対象リストID（ログインユーザーに紐づく）
   * @return 親タスク一覧
   */
  java.util.List<jp.levtech.rookie.tutorial.model.Task> findAllByList(long listId);

  /**
   * 保管済（archived=true）の親タスク一覧を list 単位で取得する。
   *
   * @param listId 対象リストID
   * @return 親タスク一覧（保管済）
   */
  java.util.List<jp.levtech.rookie.tutorial.model.Task> findAllArchivedByList(long listId);

  /**
   * taskId で 1 件取得する。
   * 
   * @param taskId タスクID
   * @return 見つかった場合は Optional、存在しない場合は Optional.empty()
   */
  java.util.Optional<jp.levtech.rookie.tutorial.model.Task> findById(long taskId);

  /**
   * タスクを新規登録する。
   * @param task 登録対象
   */
  void register(jp.levtech.rookie.tutorial.model.Task task);

  /**
   * タスクを更新する。
   *
   * @param task 更新内容
   */
  void update(jp.levtech.rookie.tutorial.model.Task task);

  /**
   * タスクを削除する。
   *
   * @param task 削除対象
   */
  void delete(jp.levtech.rookie.tutorial.model.Task task);

  /**
   * 一括削除：完了済（completed=true）かつ未保管（archived=false）のタスクを list 単位で削除する。
   *
   * @param listId 対象リストID
   * @return 削除件数
   */
  int deleteCompleted(long listId);

  /**
   * 一括保管：完了済（completed=true）で未保管（archived=false）のタスクを archived=true に更新する。
   *
   * @param listId 対象リストID
   * @return 更新件数
   */
  int archiveCompleted(long listId);

  /**
   * 復元：保管済（archived=true）のタスクを 1 件 archived=false に戻す。
   *
   * @param listId 対象リストID
   * @param id     復元対象の taskId（命名上は taskId）
   * @return 更新件数（0 の場合は対象なし）
   */
  int restoreArchivedById(long listId, long id);
}
