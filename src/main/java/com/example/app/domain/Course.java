package com.example.app.domain;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class Course {

	private Integer id;
	private Integer entryId;

	@NotBlank(message = "名前を入力してください")
	@Size(max = 30, message = "30字以内で入力してください")
	private String name;
	
	@Min(value=1, message = "カテゴリを入力してください")
	private int categoryId;
	
	private String categoryName;
	
	@Min(value=1, message = "カテゴリを入力してください")
	private int taskTime;
	
	@Range(min=0,max=50000, message="50000円以下で入力してください")
	@NotNull(message = "金額を数字で入力してください")
	private int price;
	
	@NotBlank(message = "概要を入力してください")
	@Size(max = 500, message = "500字以内で入力してください")
	private String summary;
	
	private Integer teacherId;
	private Integer userId;
	
	//画像のアップロードとファイル名
	private MultipartFile upfile;
	private String imageName;
}
