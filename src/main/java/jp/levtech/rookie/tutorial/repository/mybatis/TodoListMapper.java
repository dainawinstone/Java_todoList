package jp.levtech.rookie.tutorial.repository.mybatis;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.levtech.rookie.tutorial.model.TodoList;

/**
 * リストを扱うためのマッパー
 */
@Mapper
public interface TodoListMapper {
	
	/**
     * user_id に紐づく Todo リストを 1 件取得する（作成はしない）。
     *
     * @param userId 取得対象のユーザーID
     * @return 存在すれば {@code Optional.of(TodoList)}、存在しなければ {@code Optional.empty()}
     */
    Optional<TodoList> findByUserId(@Param("userId") long userId);

    /**
     * 存在しなければ作成、いずれにせよ list_id を1発で返す。
     * name が null のときは 'My ToDo' を使う。
     * 
     * @param userId 対象ユーザーID
     * @param name   新規作成時に設定するリスト名（null 可。null の場合は 'My ToDo'）
     * @return 取得または作成された list_id
     */
    Long getOrCreateIdByUserId(@Param("userId") long userId,
                               @Param("name") String name);
}
