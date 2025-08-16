package jp.levtech.rookie.tutorial.controller.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;

/**
 * ログインユーザーを作成するためのフォーム
 */
@Data
public class CreateLoginUserForm {
	/**
	 * ユーザー名を表すプロパティ
	 */
	@NotBlank(message = "ユーザー名は必須です")
	@Size(min = 4 ,message="ユーザー名は最小4文字必要です")
	private String userName;
	
	/**
	 * パスワードを表すプロパティ
	 */
	@NotBlank(message = "パスワードは必須です")
	@Size(min = 4 , message="パスワードは最小4文字必要です")
	private String password;	
}
