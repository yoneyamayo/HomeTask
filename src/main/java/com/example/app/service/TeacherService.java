package com.example.app.service;

import java.util.List;

import com.example.app.domain.Course;
import com.example.app.domain.Task;
import com.example.app.domain.TaskUpload;
import com.example.app.domain.Teacher;

public interface TeacherService {
	
	//講師関連
	List<Teacher> getTeacherList() throws Exception;
	Teacher getTeacherById(Integer id) throws Exception;
	void createTeacher(Teacher teacher) throws Exception;
	void updateTeacher(Teacher teacher) throws Exception;
	void deleteTeacher(Integer id) throws Exception;
	boolean isCorrectIdAndPassword(String mail,String pass)
	throws Exception;
	Teacher getTeacherByMail(String mail) throws Exception;
	
	//コース関連
	void addCourse(Course course) throws Exception;
	void updateCourse(Course course) throws Exception;
	List<Course> getCourseListByTeacherId(Integer id) throws Exception;
	Course getCourseDetailByCourseId(Integer id) throws Exception;
	
	//タスク関連
	void addTask(Task task) throws Exception;
	List<Task> getTaskListByCourseId(Integer id) throws Exception;
	Integer getCountTaskByCourseId(Integer id)throws Exception;
	List<TaskUpload> getUserTaskByTaskId(Integer id)throws Exception;
}
