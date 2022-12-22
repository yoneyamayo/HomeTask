package com.example.app.mapper;

import java.util.List;

import com.example.app.domain.Course;
import com.example.app.domain.Task;
import com.example.app.domain.TaskUpload;
import com.example.app.domain.Teacher;

public interface TeacherMapper {

	//講師関連
	List<Teacher> selectAllTeachers() throws Exception; 
	Teacher selectById(Integer id)throws Exception;
	Teacher selectByMail(String mail)throws Exception;
	void insert(Teacher teacher) throws Exception;
	void update(Teacher teacher)throws Exception;
	void delete(Integer id)throws Exception;
	
	//コース関連
	void insertCourse(Course course) throws Exception;
	List<Course> selectCourseByTeacherId(Integer id) throws Exception;
	Course selectCourseDetailByCourseId(Integer id) throws Exception; 
	void updateCourse(Course course)throws Exception;
	
	//タスク関連
	void insertTask(Task task) throws Exception;
	List<Task> selectTaskByCourseId(Integer id) throws Exception;
	Integer selectCountTaskByCourseId(Integer id)throws Exception;
	List<TaskUpload> selectUserTaskByTaskId(Integer id)throws Exception;
}
