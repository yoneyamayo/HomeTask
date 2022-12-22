package com.example.app.domain;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class TaskUpload {
 
	private Integer id;
	private Integer userId;
	private Integer taskId;
	private String taskName;
	private Integer courseId;
	private String userName;

	//アップロードとファイル名
	private MultipartFile upfile;
	
	private String fileName;
	private String fileType;
	
	
	private String categoryName;
	private Integer categoryCount;
	
}
