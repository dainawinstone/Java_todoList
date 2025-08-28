package jp.levtech.rookie.tutorial.model;

import lombok.Value;

/*
 * ログインユーザーを表すモデル
 */
@Value
public class LoginUser {
	
	/*
	 * ユーザーIDを表すプロパティ
	 */
    long userId;
    
    /*
     * ユーザーネームを表すプロパティ
     */
    String userName;
    
    /*
     * パスワードを表すプロパティ
     */
    String password;
    
    /*
     * アカウント有効フラグを表すプロパティ
     */
    boolean enabled;

    // 既存（プリミティブ）
    public LoginUser(long userId, String userName, String password, boolean enabled) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.enabled = enabled;
    }

    // MyBatis が探しに来るラッパー型
    public LoginUser(Long userId, String userName, String password, Boolean enabled) {
        this.userId = userId != null ? userId : 0L;
        this.userName = userName;
        this.password = password;
        this.enabled = enabled != null ? enabled : false;
    }
}
