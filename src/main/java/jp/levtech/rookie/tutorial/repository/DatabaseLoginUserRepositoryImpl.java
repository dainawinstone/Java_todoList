package jp.levtech.rookie.tutorial.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import jp.levtech.rookie.tutorial.model.LoginUser;
import jp.levtech.rookie.tutorial.repository.mybatis.LoginUserMapper;
import lombok.RequiredArgsConstructor;

/**
 * ログインユーザーを扱うリポジトリ
 */

@Repository
@RequiredArgsConstructor
public class DatabaseLoginUserRepositoryImpl implements LoginUserRepository {

    private final LoginUserMapper loginUserMapper;
    
    /**
     * ユーザー名からuser_idを取得する
     * 
     * @param userName 取得対象のユーザー名
     * @return 見つかった user_id
     * @throws IllegalStateException 該当ユーザーが存在しない場合
     */
    @Override
    public long findIdByUserName(String userName) {
        Long id = loginUserMapper.findIdByUserName(userName); // ← mapper ではなく loginUserMapper
        if (id == null) {
            throw new IllegalStateException("login_users に user_name=" + userName + " が見つかりません");
        }
        return id;
    }
    
    /**
     * ユーザー名で 1 件検索する。
     *
     * @param userName 検索対象のユーザー名
     * @return 見つかった場合は {@code Optional.of(LoginUser)}、存在しない場合は {@code Optional.empty()}
     */
    @Override
    public Optional<LoginUser> findByUserName(String userName) {
        return loginUserMapper.findByUserName(userName);
    }
    
    
    /**
     * 新規登録する。
     *
     * @param loginUser 登録対象（password はハッシュ済みを想定）
     */
    @Override
    public void register(LoginUser loginUser) {
        loginUserMapper.register(loginUser);
    }
    
    /**
     * ユーザー名の存在有無を返す（0/1 の軽量チェック）。
     *
     * @param userName チェック対象のユーザー名
     * @return {@code true}：既に存在／{@code false}：未使用
     */
    @Override
    public boolean existsByUserName(String userName) {
        return loginUserMapper.countByUserName(userName) > 0;
    }
}
