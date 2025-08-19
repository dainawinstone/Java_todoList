package jp.levtech.rookie.tutorial.controller;

import jakarta.validation.Valid;

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
	 * @return テンプレート/リダイレクト
	 */
	@PostMapping("/signin")
	public String register(@ModelAttribute @Valid CreateLoginUserForm createLoginUserForm, BindingResult bindingResult) {
		
		// バリデーションエラーがある場合、再度作成画面を返す。
		if (bindingResult.hasErrors()) {
			return "home/signin";
		}
		// ログインユーザーを表す変数loginUserを初期化する。
		// パスワードを平文で保存するのは危険なので、エンコードする。
		// userIdには無効な値として0を設定する。
		// enabledには利用可能としてtrueを設定する。
		String userName = createLoginUserForm.getUserName();
		String rawPassword = createLoginUserForm.getPassword();
		String encodedPassword = passwordEncoder.encode(rawPassword);
		LoginUser loginUser = new LoginUser(0L, userName, encodedPassword, true);
		// ログインユーザーを管理するリポジトリに、ログインユーザーを作成するよう依頼する。
		loginUserRepository.register(loginUser);
		return "redirect:/login";
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
