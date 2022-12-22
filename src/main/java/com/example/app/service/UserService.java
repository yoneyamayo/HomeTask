package com.example.app.service;

import java.util.List;

import com.example.app.domain.Course;
import com.example.app.domain.CourseEntry;
import com.example.app.domain.Task;
import com.example.app.domain.TaskUpload;
import com.example.app.domain.User;

public interface UserService {

	//ユーザー関連
	List<User> getUserList() throws Exception;
	User getUserById(Integer id) throws Exception;
	void createUser(User user) throws Exception;
	void updateUser(User user) throws Exception;
	void deleteUser(Integer id) throws Exception;
	boolean isCorrectIdAndPassword(String mail,String pass)
	throws Exception;
	User getUserByMail(String mail) throws Exception;
	
	//コース関連
	List<User> getAllCourse(Integer id) throws Exception; 
	List<Course> getCourseByUserId(Integer id) throws Exception;
	void courseEntry(CourseEntry courseEntry) throws Exception;
	Course getUserCoursePageByCourseId(Integer id) throws Exception;
	List<CourseEntry> getCourseEntryByUserId(Integer id) throws Exception;
	
	//タスク関連
	List<Task> getUserTaskListByCourseId(Integer id) throws Exception;
	Task getUserTaskDetailByTaskId(Integer id)throws Exception;	
	TaskUpload getTaskUploadPageByTaskId(Integer id)throws Exception;
	void addTaskUpload(TaskUpload taskUpload) throws Exception;
	List<TaskUpload> getAllUserUploadByTaskId(Integer id) throws Exception;
	
	Integer getSumTaskTimeByUserId(Integer id) throws Exception;
	Integer getTaskUploadCountByUserId(Integer id) throws Exception;
	List<TaskUpload> getUploadCourseCategoryByUserId(Integer id) throws Exception;
}
