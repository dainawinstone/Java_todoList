package jp.levtech.rookie.tutorial.controller;

import jakarta.validation.Valid;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jp.levtech.rookie.tutorial.controller.form.CreateLoginUserForm;
import jp.levtech.rookie.tutorial.model.LoginUser;
import jp.levtech.rookie.tutorial.repository.LoginUserRepository;

/**
 * ホーム画面を管理するコントローラー
 */
@Controller
public class HomeController {

	/**
	 * ログインユーザーを管理するリポジトリ
	 */
	private final LoginUserRepository loginUserRepository;

	/**
	 * パスワードのエンコーダー
	 */
	private final PasswordEncoder passwordEncoder;

	/**
	 * ホーム画面を管理するコントローラーのコンストラクタ
	 *
	 * @param loginUserRepository ログインユーザーを管理するリポジトリ
	 * @param passwordEncoder パスワードのエンコーダー
	 */
	public HomeController(LoginUserRepository loginUserRepository, PasswordEncoder passwordEncoder) {
		// ログインユーザーを管理するリポジトリ、パスワードのエンコーダーを初期化する。
		this.loginUserRepository = loginUserRepository;
		this.passwordEncoder = passwordEncoder;
	}

	/**
	 * ホーム画面を扱う。
	 *
	 * @return テンプレート
	 */
	@GetMapping("/")
	public String index() {
		// レンダリングに利用するテンプレート名を返す。
		return "home/index";
	}

	/**
	 * サインイン画面を扱う。
	 *
	 * @param model モデル
	 * @return テンプレート
	 */
	@GetMapping("/signin")
	public String create(Model model) {
		CreateLoginUserForm createLoginUserForm = new CreateLoginUserForm();
		model.addAttribute("createLoginUserForm", createLoginUserForm);
		return "home/signin";
	}

	/**
	 * ログインユーザーを登録する。
	 *
	 * @param createLoginUserForm ログインユーザーを作成するためのフォーム
	 * @param bindingResult バリデーションの結果
	 * @param model モデル（エラーメッセージ表示用）
	 * @return テンプレート/リダイレクト
	 */
	@PostMapping("/signin")
	public String register(@ModelAttribute @Valid CreateLoginUserForm createLoginUserForm, 
			BindingResult bindingResult,
			Model model) {
		
		// バリデーションエラーがある場合、再度作成画面を返す。
		if (bindingResult.hasErrors()) {
			return "home/signin";
		}
		String userName = createLoginUserForm.getUserName();
		
		if(loginUserRepository.existsByUserName(userName)) {
			bindingResult.rejectValue("userName", "duplicate", "そのユーザー名は既に使われています");
			return "home/signin";			
		}
		
		
		try {
			//登録処理
			String rawPassword = createLoginUserForm.getPassword();
			String encodedPassword = passwordEncoder.encode(rawPassword);
			LoginUser loginUser = new LoginUser(0L, userName, encodedPassword, true);
			loginUserRepository.register(loginUser);
			
			// 登録完了メッセージを表示するための情報を渡す
	        model.addAttribute("userName", userName);
	        return "home/signup-success";
		}catch(DataIntegrityViolationException e){
			bindingResult.rejectValue("userName", "duplicate", "そのユーザー名は既に使われています");
			return "home/signin";						       
	        
		}catch(IllegalArgumentException e){
			bindingResult.rejectValue("userName", "duplicate", "そのユーザー名は既に使われています");
			return "home/signin";						       	        			
			
		}			
	
	}

	/**
	 * ログイン画面を扱う。
	 *
	 * @return テンプレート
	 */
	@GetMapping("/login")
	public String login() {
		return "home/login";
	}

}
