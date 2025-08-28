package jp.levtech.rookie.tutorial.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import jp.levtech.rookie.tutorial.controller.view.TaskViewRow;
import jp.levtech.rookie.tutorial.model.Task;

public class TaskHierarchyViewService {
	/*
	 * sortとorderでCompartorを作る
	 */
	public static Comparator<Task> buildComparator(String sort,String order){
		String key = (sort == null || sort.isBlank()) ? "id" : sort;
		Comparator<Task>c;
		switch(key) {
			case "priority" -> c = Comparator.comparingInt(Task::getTaskPriority);
			case "name" -> c = Comparator.comparing(Task::getTaskName,
					Comparator.nullsLast(String::compareToIgnoreCase));
			case "done" -> c = Comparator.comparing(Task::isCompleted);
			case "due" -> c = Comparator.comparing(Task::getDueDate,
					Comparator.nullsLast(LocalDate::compareTo));
			default -> c = Comparator.comparing(
					Task::getTaskId,Comparator.nullsLast(Long::compareTo));
		}
		
		if("desc".equalsIgnoreCase(order)) c = c.reversed();
		
		c = c.thenComparing(Task::getTaskId, Comparator.nullsLast(Long::compareTo));
		
		return c;		
	}
	
	 /** 親→子（子は親の直後）を保ったまま 1 次元に展開 */
	
	public static  List<TaskViewRow> flatten(List<Task> parents, Comparator<Task> cmp){
		if(parents == null) return List.of();
		
		List<Task> sortedParents = new ArrayList<>(parents);
		sortedParents.sort(cmp);
		
		List<TaskViewRow> rows = new ArrayList<>(sortedParents.size()*2);
		for(Task p: sortedParents) {
			rows.add(TaskViewRow.of(p, 0));
			
			 List<Task> children = p.getSubTasks();
	            if (children != null && !children.isEmpty()) {
	                List<Task> sortedChildren = new ArrayList<>(children);
	                sortedChildren.sort(cmp);
	                for (Task c : sortedChildren) {
	                    rows.add(TaskViewRow.of(c, 1));
	                }
	            }
	        }
	        return rows;
	}				
	
	
	 /** 便利オーバーロード（sort/order 文字列で呼べる） */	
	public static List<TaskViewRow>flatten(List<Task> parents, String sort,String order){
			return flatten(parents , buildComparator(sort,order));
	}
		
	
		
			
	
	
	
	

}
