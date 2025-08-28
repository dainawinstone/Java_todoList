package jp.levtech.rookie.tutorial.model;

import java.time.OffsetDateTime;

import lombok.Data; 

/*
 * リストを表すモデル
 */

@Data
public class TodoList{
   /**
	* リストIDを表すプロパティ
	*/
   private Long listId;
   
   /**
    * リスト所有者のユーザーIDを表すプロパティ
    */
   private Long userId;
    
   /**
    * リスト名を表すプロパティ
    */
   private String name;
    
   /**
    * 作成日時を表すプロパティ
    */
   private OffsetDateTime createdAt;
   
   /**
    * 更新日時を表すプロパティ 
    */
   private OffsetDateTime updatedAt;
}
