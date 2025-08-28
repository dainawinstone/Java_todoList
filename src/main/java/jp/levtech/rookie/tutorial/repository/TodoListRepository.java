package jp.levtech.rookie.tutorial.repository;

import java.util.Optional;

import jp.levtech.rookie.tutorial.model.TodoList; 

/**
 * Listのインターフェース。
 */
public interface TodoListRepository {
	
	/**
	 * userId に紐づく TodoList を 1 件取得する（作成はしない）。
	 *
	 * @param userId 取得対象のユーザーID
	 * @return 存在すれば {@code Optional.of(TodoList)}、存在しなければ {@code Optional.empty()}
	 */
	Optional<TodoList> findByUserId(long userId);
	
	/**
	 * TodoList を「作成または取得」し、その listId を返す。
	 *
	 * @param userId 対象ユーザーID
	 * @param name   新規作成時に設定するリスト名（{@code null} 可）
	 * @return 取得または作成されたリストの ID（listId）
	 */
	long createForUser(long userId,String name);

}
