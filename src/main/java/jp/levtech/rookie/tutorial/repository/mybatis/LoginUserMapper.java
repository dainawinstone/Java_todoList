package jp.levtech.rookie.tutorial.repository.mybatis;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.levtech.rookie.tutorial.model.LoginUser;

/**
 * ログインユーザーのマッパー
 */
@Mapper
public interface LoginUserMapper {

    /** 
     * ユーザー名で1件を検索する
     *
     * @param userName 検索対象のユーザー名
     * @return 見付かった場合は、ログインユーザー、
     *          存在しなければ なし
     */
    Optional<LoginUser> findByUserName(@Param("userName") String userName);

    /** 
     * 新規登録する
     * 
     * @param loginUser 
     */
    int register(LoginUser loginUser);

    /** 
     * ユーザー名の重複件数を返す
     * 
     * @param userName ユーザーネーム
     * @return （0:存在しない / 1以上:存在） 
     * */
    int countByUserName(@Param("userName") String userName);

    /** 
     * ユーザー名 から user_id を取得する 
     * 
     * @param userName 検索対象のユーザー名
     * @return 見付かった場合は user_id、存在しない場合はnull
     */
    Long findIdByUserName(@Param("userName") String userName);
}
