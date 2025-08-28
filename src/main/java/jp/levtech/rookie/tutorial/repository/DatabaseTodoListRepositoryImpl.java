package jp.levtech.rookie.tutorial.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import jp.levtech.rookie.tutorial.model.TodoList;
import jp.levtech.rookie.tutorial.repository.mybatis.TodoListMapper;
import lombok.RequiredArgsConstructor;

/**
 * リストの永続化を担うリポジトリ
 */
@Repository
@RequiredArgsConstructor
public class DatabaseTodoListRepositoryImpl implements TodoListRepository {

    private final TodoListMapper mapper;
    
    /**
     * userId に紐づく TodoList を 1 件取得する（作成はしない）。
     *
     * @param userId 取得対象のユーザーID
     * @return 存在すれば {@code Optional.of(TodoList)}、存在しなければ {@code Optional.empty()}
     */
    @Override
    public Optional<TodoList> findByUserId(long userId) {
        return mapper.findByUserId(userId);
    }

    /**
     * TodoList を「作成または取得」し、listId を返す。
     * 
     * @param userId 対象ユーザーID
     * @param name   新規作成時のリスト名（null 可。null の場合は Mapper/SQL 側で 'My ToDo' を使用）
     * @return 取得または新規作成された listId
     * @throws IllegalStateException 例外的に ID を取得できなかった場合 
     */
    @Override
    public long createForUser(long userId, String name) {
        Long id = mapper.getOrCreateIdByUserId(userId, name);
        if (id == null) {
            throw new IllegalStateException("Failed to get or create TodoList for userId=" + userId);
        }
        return id;
    }
}
