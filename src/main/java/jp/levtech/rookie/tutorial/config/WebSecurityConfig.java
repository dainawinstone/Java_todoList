package jp.levtech.rookie.tutorial.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Webセキュリティに関する設定
 */
@Configuration
public class WebSecurityConfig {

	/**
	 * HTTPリクエストに対するセキュリティを設定するBean
	 *
	 * @param http HTTPセキュリティ
	 * @return セキュリティに関する設定
	 * @throws Exception
	 */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			// formのログインに関する設定
			.formLogin(form -> form				
				.loginPage("/login")
				.defaultSuccessUrl("/todo/", true)
				.failureUrl("/login?error")
				.permitAll()
			)
			
			// ログアウトに関する設定
			.logout(logout -> logout
				.logoutUrl("/logout")
				.logoutSuccessUrl("/login?logout")
				.permitAll()
			)
			// 認可に関する設定
			.authorizeHttpRequests(authorize -> authorize
				// CSS, JavaScriptなど静的リソースへのアクセスを全ユーザーに許可する。
				.requestMatchers(PathRequest.toStaticResources().atCommonLocations())
				.permitAll()
				// ホーム画面・サインイン画面・ログイン画面・エラー画面へのアクセスを全ユーザーに許可する。
				.requestMatchers("/", "/signin","/signin/**","/signup-success", "/login", "/error")
				.permitAll()
				// その他へのアクセスを認証済みのユーザーのみに制限する。
				.anyRequest()
				.authenticated()											
			);
		return http.build();
	}

	/**
	 * パスワードのエンコーダーのBean
	 *
	 * @return パスワードのエンコーダー
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	

}
