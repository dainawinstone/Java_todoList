package jp.levtech.rookie.tutorial.repository;

import java.util.Optional;

import jp.levtech.rookie.tutorial.model.LoginUser;

/**
 * ログインユーザーを管理するリポジトリ
 */
public interface LoginUserRepository {
	/*
	 * ユーザー名からログインユーザーを検索する
	 * @param userName　ユーザー名
	 * @return ログインユーザー
	 */
	Optional<LoginUser> findByUserName(String userName);
	
	/**
	 * ログインユーザーを登録する
	 * 
	 * @param loginUser ログインユーザー
	 */
	void register(LoginUser loginUser);	
}


