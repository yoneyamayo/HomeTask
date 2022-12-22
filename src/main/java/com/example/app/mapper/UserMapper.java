package com.example.app.mapper;

import java.util.List;

import com.example.app.domain.Course;
import com.example.app.domain.CourseEntry;
import com.example.app.domain.Task;
import com.example.app.domain.TaskUpload;
import com.example.app.domain.User;

public interface UserMapper {
	
	//ユーザー関連
	List<User> selectAllUsers() throws Exception; 
	User selectById(Integer id)throws Exception;
	User selectByMail(String mail)throws Exception;
	void insert(User user) throws Exception;
	void update(User user)throws Exception;
	void delete(Integer id)throws Exception;
	
	//コース関連
	List<User> selectAllCourse(Integer id) throws Exception; 
	List<Course> selectCourseByUserId(Integer id) throws Exception;	
	void insertEntryCourse(CourseEntry courseEntry) throws Exception;
	Course selectUserCoursePageByCourseId(Integer id) throws Exception; 
	List<CourseEntry> selectCourseEntryByUserId(Integer id) throws Exception;
	
	//タスク関連
	List<Task> selectUserTaskListByCourseId(Integer id) throws Exception;
	Task selectUserTaskDetailByTaskId(Integer id)throws Exception;	
	TaskUpload selectTaskUploadPageByTaskId(Integer id)throws Exception;
	void insertTaskUpload(TaskUpload taskUpload) throws Exception;
	List<TaskUpload> selectAllUserUploadByTaskId(Integer id) throws Exception;
	TaskUpload selectUploadPageByTaskId(Integer id) throws Exception;
	
	Integer selectSumTaskTimeByUserId(Integer id) throws Exception;
	Integer selectTaskUploadCountByUserId(Integer id) throws Exception;
	List<TaskUpload> selectUploadCourseCategoryByUserId(Integer id) throws Exception;
	
}
