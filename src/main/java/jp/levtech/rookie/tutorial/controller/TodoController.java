package jp.levtech.rookie.tutorial.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import jp.levtech.rookie.tutorial.controller.form.CreateTaskForm;
import jp.levtech.rookie.tutorial.controller.form.UpdateTaskForm;
import jp.levtech.rookie.tutorial.controller.view.TaskViewRow;
import jp.levtech.rookie.tutorial.model.Task;
import jp.levtech.rookie.tutorial.repository.TaskRepository;
import jp.levtech.rookie.tutorial.service.CurrentListService;
import jp.levtech.rookie.tutorial.service.LoginUserService;
import jp.levtech.rookie.tutorial.service.TaskHierarchyViewService;

/*
 * Todo　一覧・登録・編集を管理するコントローラ
 */


@Controller
public class TodoController {

    private final TaskRepository taskRepository;
    private final CurrentListService currentListService;
    private final LoginUserService loginUserService;
   
    public TodoController(TaskRepository taskRepository, 
    					  CurrentListService currentListService,
    					  LoginUserService loginUserService) {
        this.taskRepository = taskRepository;
        this.currentListService = currentListService;
        this.loginUserService = loginUserService;
    }

    /** 未保管(archived = false)の親タスク一覧を表示する
     * 
     * @param sort UIのソートキー
     * @param order ソート順　"asc" or "desc"
     * @param model テンプレートへ値を受け渡す入れ物
     * 
     * @return 未保管一覧テンプレート
     */
    @GetMapping({"/todo", "/todo/"})
    public String showTodoList(
            @RequestParam(name = "sort", required = false) String sort,
            @RequestParam(name = "order", defaultValue = "asc") String order,
            Model model) {

        long listId = currentListService.getOrCreateListIdForCurrentUser();

        // 未保管の親タスク一覧（子は Mapper 側の collection で埋まる想定）
        List<Task> parentTasks = taskRepository.findAllByList(listId);
        
        // 画面表示用にフラット化 + 並び替え
        List<TaskViewRow> rows = TaskHierarchyViewService.flatten(parentTasks, sort, order);

        model.addAttribute("rows", rows);
        model.addAttribute("tasks", parentTasks);
        model.addAttribute("sort", sort);
        model.addAttribute("order", order);
        model.addAttribute("showArchived", false);
        return "TodoHome";
    }

    /** 
     * 保管済(archived=true)の親タスク一覧を表示する
     *  
     * @param sort UIのソートキー
     * @param order ソート順　"asc" or "desc"
     * @param model テンプレートへ値を受け渡す入れ物
     * 
     * @return 保管一覧テンプレート
     */
    @GetMapping({"/todo/archived", "/todo/archived/"})
    public String showArchivedList(
            @RequestParam(name = "sort", required = false) String sort,
            @RequestParam(name = "order", defaultValue = "asc") String order,
            Model model) {

        long listId = currentListService.getOrCreateListIdForCurrentUser();

        List<Task> parentTasks = taskRepository.findAllArchivedByList(listId);
        List<TaskViewRow> rows = TaskHierarchyViewService.flatten(parentTasks, sort, order);

        model.addAttribute("rows", rows);
        model.addAttribute("tasks", parentTasks);
        model.addAttribute("sort", sort);
        model.addAttribute("order", order);
        model.addAttribute("showArchived", true);
        return "TodoArchived";
    }

    /** 
     * 完了チェック（1行の completed トグル）を更新する。
     * 
     * @param taskId 
     * @param payload
     * 
     * @return 成功時: 204 No Content／対象なし・他人のタスク: 404 Not Found
     * 
     */
    @PostMapping("/todo/{taskId}/complete")
    public ResponseEntity<Void> updateCompleted(
            @PathVariable long taskId,
            @RequestBody java.util.Map<String, Boolean> payload) {

        long listId = currentListService.getOrCreateListIdForCurrentUser();
        
        //自分のlistのタスクかどうか確認(存在しない、または他人のなら404
        Optional<Task> existingOpt = taskRepository.findById(taskId);
        if (existingOpt.isEmpty() || !existingOpt.get().getListId().equals(listId)) {
            return ResponseEntity.notFound().build();
        }
        
        boolean completed = Boolean.TRUE.equals(payload.get("completed"));
        Task existing = existingOpt.get();
        existing.setCompleted(completed);
        taskRepository.update(existing); // WHERE に list_id も入る実装
        return ResponseEntity.noContent().build();
    }

    /** 
     * 登録画面 
     * 
     * @param model モデル
     * @return TodoCreate 
     * 
     */
    @GetMapping("/todo/create")
    public String ShowTodoCreate(Model model) {
        CreateTaskForm form = new CreateTaskForm();
        form.setDueDate(LocalDate.now());
        model.addAttribute("createTaskForm", form);
        return "TodoCreate";
    }

    /** 
     * 登録処理
     * 
     * @param form 入力フォーム
     * @param bindingResult
     * @return 成功時は一覧へリダイレクト
     * 
     */
    @PostMapping("/todo/register")
    public String register(
            @ModelAttribute("createTaskForm") @Valid CreateTaskForm form,
            BindingResult bindingResult,
            @AuthenticationPrincipal(expression = "username") String username
    ) {
    	
    	//入力エラーがあれば作成画面に戻る
        if (bindingResult.hasErrors()) {
            return "TodoCreate";
        }

        long listId = currentListService.getOrCreateListIdForCurrentUser();
        
        Long userId = loginUserService.findIdByUserName(username);
        if (userId == null) {
            throw new IllegalStateException("ログインユーザーが取得できませんでした: " + username);
        }
        
        //フォームの値をエンティティへ移し替え        
        Task task = new Task();
        task.setTaskPriority(form.getTaskPriority());
        task.setTaskName(form.getTaskName());
        task.setCompleted(false);
        task.setDueDate(form.getDueDate());
        task.setParentTaskId(form.getParentTaskId());
        task.setSubTasks(List.of());
        task.setArchived(false);
        task.setArchivedAt(null);
        task.setListId(listId);
        task.setUserId(userId);

        taskRepository.register(task);
        return "redirect:/todo/";
    }
        
        
    

    /** 
     * 編集画面 表示
     * 
     * @param taskId
     * @param model
     * 
     * @return TodoEdit
     * 
     */
    @GetMapping("/todo/{taskId}/edit")
    public String showEdit(@PathVariable long taskId, Model model) {
        long listId = currentListService.getOrCreateListIdForCurrentUser();
        
        // 自分の list のタスクのみ編集可。見つからなければ 404 相当の例外に
        Task task = taskRepository.findById(taskId)
                .filter(t -> t.getListId().equals(listId))
                .orElseThrow(() -> new TaskNotFoundException("ID:" + taskId + "のタスクが見つかりません"));

        UpdateTaskForm f = new UpdateTaskForm(
                task.getTaskName(),
                task.isCompleted(),
                task.getTaskPriority(),
                task.getDueDate()
        );
        model.addAttribute("task", task);
        model.addAttribute("updateTaskForm", f);
        model.addAttribute("taskId", taskId);
        return "TodoEdit";
    }

    /** 
     * 更新処理 
     * 
     * @param taskId タスクID
     * @param form 画面入力
     * @param bindingResult 入力検証の結果
     * @param model モデル
     * 
     * @return 成功時は一覧へリダイレクト/エラー時は"TodoEdit"
     * 
     * */
    @PostMapping("/todo/{taskId}/update")
    public String update(
            @PathVariable long taskId,
            @Validated @ModelAttribute("updateTaskForm") UpdateTaskForm form,
            BindingResult bindingResult,
            Model model) {

        long listId = currentListService.getOrCreateListIdForCurrentUser();
        
        // 入力エラー時は元の task 情報を再度 Model に詰めて編集画面へ
        if (bindingResult.hasErrors()) {
            model.addAttribute("taskId", taskId);
            taskRepository.findById(taskId)
                    .filter(t -> t.getListId().equals(listId))
                    .ifPresent(t -> model.addAttribute("task", t));
            return "TodoEdit";
        }

        // 対象タスクの存在・権限をチェック
        Task existing = taskRepository.findById(taskId)
                .filter(t -> t.getListId().equals(listId))
                .orElseThrow(() -> new TaskNotFoundException("ID:" + taskId + "のタスクを更新できません"));

        // 変更点を反映
        existing.setTaskPriority(form.getTaskPriority());
        existing.setTaskName(form.getTaskName());
        existing.setCompleted(form.isCompleted());
        existing.setDueDate(form.getDueDate());

        taskRepository.update(existing);
        return "redirect:/todo/";
    }

    /** 
     * サブタスク作成画面 を表示
     * 
     * @param parentId 親タスクId
     * @param model
     * 
     * @return "TodoSubTask"
     * 
     */
    @GetMapping("/todo/{parentId}/subtask/create")
    public String createSubtask(@PathVariable long parentId, Model model) {
        // 親が自分のリストであることを軽く確認（見つからなくても画面は出せるが安全のため）
        long listId = currentListService.getOrCreateListIdForCurrentUser();
        
        // 親が自分の list のタスクであることを確認（越権作成の防止）
        taskRepository.findById(parentId)
                .filter(t -> t.getListId().equals(listId))
                .orElseThrow(() -> new TaskNotFoundException("親タスク(ID:" + parentId + ")が見つかりません"));
        
        // 親 ID を埋めたフォームを渡す
        CreateTaskForm form = new CreateTaskForm();
        form.setParentTaskId(parentId);
        model.addAttribute("createTaskForm", form);
        return "TodoSubTask";
    }

    /** 
     * 単体削除 
     * 
     * @param taskId 削除対象のtaskId
     * 
     * @return 一覧へリダイレクト
     * 
     */
    @PostMapping("/todo/{taskId}/delete")
    public String delete(@PathVariable long taskId) {
        long listId = currentListService.getOrCreateListIdForCurrentUser();
        
        // 自分の list のタスクのみ削除可
        Task task = taskRepository.findById(taskId)
                .filter(t -> t.getListId().equals(listId))
                .orElseThrow(() -> new TaskNotFoundException("ID:" + taskId + "のタスクを削除できません"));

        taskRepository.delete(task);
        return "redirect:/todo/";
    }

    /* ===== 一括アクション／復元（list 単位） ===== */
    
    /**
     * 一括：完了済みを保管(archived = true)にする
     * 
     * @return 一覧へリダイレクト
     */

    @PostMapping("/todo/archive-completed")
    public String archiveCompleted() {
        long listId = currentListService.getOrCreateListIdForCurrentUser();
        taskRepository.archiveCompleted(listId);
        return "redirect:/todo/";
    }
    
    /**
     * 一括;完了済みを削除する
     * 
     * @return 一覧へリダイレクト
     * 
     */
    @PostMapping("/todo/delete-completed")
    public String deleteCompleted() {
        long listId = currentListService.getOrCreateListIdForCurrentUser();
        taskRepository.deleteCompleted(listId);
        return "redirect:/todo/";
    }
    
    /**
     * 保管済みタスクを1件　復元する
     * 
     * @param taskId 復元対象の taskId
     * 
     * @return 保管一覧へリダイレクト
     * 
     */
    @PostMapping("/todo/{taskId}/restore")
    public String restore(@PathVariable long taskId) {
        long listId = currentListService.getOrCreateListIdForCurrentUser();
        // listId を条件に復元（他人の taskId を弾く）
        int updated = taskRepository.restoreArchivedById(listId, taskId);
        if (updated == 0) throw new TaskNotFoundException("ID:" + taskId + "のタスクを復元できません");
        return "redirect:/todo/archived";
    }

    /** 404 例外 */
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "タスクが見つかりません")
    private static class TaskNotFoundException extends RuntimeException {
        public TaskNotFoundException(String message) { super(message); }
    }
}
