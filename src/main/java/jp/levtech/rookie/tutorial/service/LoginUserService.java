package jp.levtech.rookie.tutorial.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.levtech.rookie.tutorial.model.LoginUser;
import jp.levtech.rookie.tutorial.repository.LoginUserRepository;

/**
 * ログインユーザーに関するアプリケーションサービス。
 */
@Service
public class LoginUserService {
	
	private final LoginUserRepository loginUserRepository;
	
	public LoginUserService(LoginUserRepository loginUserRepository) {
		this.loginUserRepository = loginUserRepository;
	}
	
	/**
	 * 新しいユーザーを登録する
	 * 
	 * @param ログインユーザー
	 * @throws llegalArgumentException すでに同じユーザー名が存在する場合
	 */
	
	@Transactional
	public void register(LoginUser loginuser) {
		//重複チェック
		if(loginUserRepository.existsByUserName(loginuser.getUserName())) {
			throw new IllegalArgumentException("すでに同じユーザー名が存在します");
			
		}	
		loginUserRepository.register(loginuser);								
	}
		
	/**
	 * ユーザー名から検索する
	 * 
	 * @param userName 検索対象のユーザー名
	 * @return 該当ユーザー
	 */
	public LoginUser findByUserName(String userName) {
		return loginUserRepository.findByUserName(userName)
				.orElse(null);
	}
	
	/**
	 * ユーザー名からユーザーIDを取得する
	 * 
	 * @param userName ユーザー名
	 * @Return userId ユーザーID
	 */
	@Transactional(readOnly = true)
	public Long findIdByUserName(String userName) {
        LoginUser user = findByUserName(userName);
        return (user != null) ? user.getUserId() : null;
	}
}
