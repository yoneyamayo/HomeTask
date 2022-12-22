package com.example.app.domain;

import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.example.app.validation.TeacherCreateGroup;
import com.example.app.validation.TeacherLoginGroup;

import lombok.Data;

@Data
public class Teacher {
	
	private Integer id;
	
	@NotBlank(message = "名前を入力してください",
			groups = {TeacherCreateGroup.class})
	@Size(max = 10, message = "10字以内で入力してください")
	private String name;
	
	@NotBlank(message = "メールアドレスを入力してください",
			groups = {TeacherCreateGroup.class,TeacherLoginGroup.class})
	@Email
	private String mail;
	
	@NotBlank(message = "パスワードを入力してください",
			groups = {TeacherCreateGroup.class,TeacherLoginGroup.class})
	@Size(min = 4, message = "4字以上で入力してください")
	private String pass;
	
	private Date created; 
	
}
