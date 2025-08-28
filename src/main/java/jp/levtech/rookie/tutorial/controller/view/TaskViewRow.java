package jp.levtech.rookie.tutorial.controller.view;

import jp.levtech.rookie.tutorial.model.Task;

/**
 * 画面の1行を表す表示用DTO。
 * level: 0=親、1=子（さらに深くするなら 2,3...）
 */
public record TaskViewRow(Task task, int level) {
    public static TaskViewRow of(Task task, int level) {
        return new TaskViewRow(task, level);
    }
}
