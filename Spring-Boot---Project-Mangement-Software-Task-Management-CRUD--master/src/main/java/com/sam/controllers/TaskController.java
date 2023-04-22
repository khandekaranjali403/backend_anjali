package com.sam.controllers;

import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sam.dto.CountType;
import com.sam.model.Task;
import com.sam.services.TaskService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin("*")
@AllArgsConstructor
public class TaskController {
	
	private TaskService taskservice;
	
	@GetMapping("/task/vData/percentcounttype")
	public List<CountType> getPercentageGroupByType(){
		return taskservice.getPercentageGroupByType();
	}
	
	
	@GetMapping("/task")
	public List<Task> getTask(){
		return taskservice.getTasks();
	}
	
	@PostMapping("/task")
	public Task addTask(@RequestBody Task task) {
		return taskservice.save(task);
	}
	
	@GetMapping("/task/{id}")
	public Task getById(@PathVariable Long id) {
		return taskservice.getTaskById(id).orElseThrow(()->new EntityNotFoundException("Requested Task not found"));
	}
	
	@PutMapping("/task/{id}")
	public ResponseEntity<?> addTask(@RequestBody Task taskPara,@PathVariable Long id) {
		if(taskservice.existById(id)) {
			Task task=taskservice.getTaskById(id).orElseThrow(()->new EntityNotFoundException("Requested Task not found"));
			task.setTitle(taskPara.getTitle());
			task.setDueDate(taskPara.getDueDate());
			task.setType(taskPara.getType());
			task.setDescription(taskPara.getDescription());
			taskservice.save(task);
			return ResponseEntity.ok().body(task);
		}else {
			HashMap<String, String>message= new HashMap<>();
			message.put("message", id + " task not found or matched");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
		}
	}
	
	@DeleteMapping("/task/{id}")
	public ResponseEntity<?> deleteTask(@PathVariable Long id) {
		if(taskservice.existById(id)) {
			taskservice.delete(id);
			HashMap<String, String>message= new HashMap<>();
			message.put("message", id + " task removed");
			return ResponseEntity.status(HttpStatus.OK).body(message);
		}else {
			HashMap<String, String>message= new HashMap<>();
			message.put("message", id + " task not found or matched");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
		}
	}
}
