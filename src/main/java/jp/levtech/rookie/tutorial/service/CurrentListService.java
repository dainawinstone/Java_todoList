package jp.levtech.rookie.tutorial.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.levtech.rookie.tutorial.repository.LoginUserRepository;
import jp.levtech.rookie.tutorial.repository.TodoListRepository;
import lombok.RequiredArgsConstructor;


/**
 * 「現在ログイン中のユーザー」に紐づく ToDo リスト（listId）を取得／作成するサービス。
 */
@Service
@RequiredArgsConstructor
public class CurrentListService {
	
  private final TodoListRepository todoListRepository;
  private final LoginUserRepository loginUserRepository;
  
  /**
   * 現在のログインユーザーに対応するリストIDを返す（なければ作成）。
   *
   * @return 取得または新規作成された {@code listId}
   */
  @Transactional
  public long getOrCreateListIdForCurrentUser() {
	  
    String userName = org.springframework.security.core.context.SecurityContextHolder
        .getContext().getAuthentication().getName();
    
    long userId = loginUserRepository.findIdByUserName(userName);
   
    // 既存があればそれ、無ければ作成して listId を返す
    return todoListRepository.findByUserId(userId)
        .map(l -> l.getListId())
        .orElseGet(() -> todoListRepository.createForUser(userId, "My ToDo"));
  }
}
