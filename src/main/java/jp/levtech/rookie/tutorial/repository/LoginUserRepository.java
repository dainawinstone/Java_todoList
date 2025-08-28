package jp.levtech.rookie.tutorial.repository;

import java.util.Optional;

import jp.levtech.rookie.tutorial.model.LoginUser;

/**
 * ログインユーザーを管理するリポジトリ
 */
public interface LoginUserRepository {
	/**
	 * ユーザー名からログインユーザーを検索する
	 * @param userName　ユーザー名
	 * @return ログインユーザー
	 */
	Optional<LoginUser> findByUserName(String userName);
	
	/**
	 * ログインユーザーを登録する
	 * @param loginUser ログインユーザー
	 */
	void register(LoginUser loginUser);	
	
	 /**
	  *  同名ユーザーが既に存在するか 
	  *  @param loginUser ログインユーザー
	  *  
	  */
    boolean existsByUserName(String userName);
    
    /**
	 * ユーザー名から user_id を取得する。
	 *
	 * @param userName 検索対象のユーザー名
	 * @return 見つかった user_id
	 */
    long findIdByUserName(String userName);
}


