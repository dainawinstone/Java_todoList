package jp.levtech.rookie.tutorial.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.levtech.rookie.tutorial.model.Task;
import jp.levtech.rookie.tutorial.repository.mybatis.TaskMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskMapper mapper;
    private final CurrentListService currentListService;

    /** 一覧取得（未保管/保管） */
    public List<Task> list(boolean showArchived) {
        long listId = currentListService.getOrCreateListIdForCurrentUser();
        return showArchived
                ? mapper.findAllArchivedByList(listId)
                : mapper.findAllByList(listId);
    }

    /** 完了→保管（一括） */
    @Transactional
    public int archiveCompleted() {
        long listId = currentListService.getOrCreateListIdForCurrentUser();
        return mapper.archiveCompleted(listId);
    }

    /** 完了→削除（一括） */
    @Transactional
    public int deleteCompleted() {
        long listId = currentListService.getOrCreateListIdForCurrentUser();
        return mapper.deleteCompleted(listId);
    }

    /** 保管→復元（1件） */
    @Transactional
    public int restore(long taskId) {
        long listId = currentListService.getOrCreateListIdForCurrentUser();
        return mapper.restoreArchivedById(listId, taskId);
    }

    /** 新規/更新（listId を必ず保持して保存） */
    @Transactional
    public void save(Task t) {
        long listId = currentListService.getOrCreateListIdForCurrentUser();
        t.setListId(listId); // 新規も更新も、自分のリストIDを必ず持たせる

        // Task#taskId が Long の場合は null チェック。primitive long なら 0 判定に変えてください。
        if (t.getTaskId() == null) {
            mapper.register(t);
        } else {
            mapper.update(t); // update の WHERE に list_id 条件が入っている前提
        }
    }
}
