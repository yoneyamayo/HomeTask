package com.example.app.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class Task {
	
	private Integer id;
	private Integer courseId;
	private Integer courseName;
	
	@NotBlank(message = "名前を入力してください")
	@Size(max = 30, message = "30字以内で入力してください")
	private String name;
	
	@NotBlank(message = "概要を入力してください")
	@Size(max = 500, message = "500字以内で入力してください")
	private String summary;

	//アップロード、イメージファイル	
	private MultipartFile topfile;
	private String imageName;
	
	//アップロード、ファイル名、ファイルタイプ
	private MultipartFile subfile1;
	private MultipartFile subfile2;
	private MultipartFile subfile3;

	private String fileName1;
	private String fileName2;
	private String fileName3;
	
	private String fileType1;
	private String fileType2;
	private String fileType3;
		

}