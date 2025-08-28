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
import jp.levtech.rookie.tutorial.service.LoginUserService;

/*
 * ホーム画面を管理するコントローラ
 */

@Controller
public class LoginUserController {
	
	/*
	 * ログインユーザーを管理するコントローラ
	 */
	private final LoginUserService loginUserService;
	
	/*
	 * パスワードのエンコーダー（ハッシュ化用
	 */	
	private final PasswordEncoder passwordEncoder;
	
	/*
	 * 依存をコンストラクタで受け取る
	 */	
	public LoginUserController(LoginUserService loginUserService, PasswordEncoder passwordEncoder) {
		this.loginUserService = loginUserService;
		this.passwordEncoder = passwordEncoder;
	}
	
	/**
	 * 新規登録画面を表示する
	 */
	@GetMapping("/register")
	public String showRegisterForm(Model model) {
		model.addAttribute("createLoginUserForm", new CreateLoginUserForm());
        return "user/register";               
	}
	
	 /**
     * ユーザーを登録する
     */
    
	@PostMapping("/register")
    public String register(
            @ModelAttribute @Valid CreateLoginUserForm form,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "user/register";
        }

        try {
            String encodedPassword = passwordEncoder.encode(form.getPassword());
            LoginUser loginUser = new LoginUser(0L, form.getUserName(), encodedPassword, true);

            loginUserService.register(loginUser); // ← Service 経由で登録
            return "redirect:/login";

        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "user/register";
        }
    }
}
	
	


