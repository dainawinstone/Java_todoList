package jp.levtech.rookie.tutorial.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jp.levtech.rookie.tutorial.model.LoginUser;
import jp.levtech.rookie.tutorial.repository.LoginUserRepository;

/**
 * ログインユーザーの詳細を管理するリポジトリ
 */

@Service
public class CustomUserDetailsService implements UserDetailsService {
	
	/**
	 * ログインユーザーを管理するリポジトリ
	 */
	private final LoginUserRepository loginUserRepository;
	
	/**
	 * ログインユーザーの詳細を管理するサービスのコンストラクタ
	 * 
	 * @param loginUserRepository ログインユーザーを管理するリポジトリ 
	 */
	public CustomUserDetailsService(LoginUserRepository loginUserRepository) {
		this.loginUserRepository = loginUserRepository;		
	}
	
	/**
	 * ユーザー名からログインユーザーの詳細を取得する
	 * 
	 * @param username 入力されたユーザー名
	 * @return フレームワークが扱えるユーザー詳細（権限や有効/無効フラグを含む）
	 * @throws UsernameNotFoundException ユーザーが見つからない場合
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
		//ログインユーザーを表す変数loginUserを定義する
		//ログインユーザーを管理するリポジトリに、ユーザー名からログインユーザーを検索するように依頼する
		Optional<LoginUser> loginUser = loginUserRepository.findByUserName(username);
		
		//ログインユーザーが見つからなかった場合は例外を投げる
		if(loginUser.isEmpty()) {
			throw new UsernameNotFoundException("ログインユーザーが見つかりませんでした");			
		}
		
		//ログインユーザーの詳細を返す
		return User
				//ログインユーザーのユーザー名を設定する
				.withUsername(loginUser.get().getUserName())
				//ログインユーザーのパスワードを設定する
				.password(loginUser.get().getPassword())
				
				.disabled(!loginUser.get().isEnabled())
				.roles("USER")
				.build();
									
	
	}
	
	
	

}
