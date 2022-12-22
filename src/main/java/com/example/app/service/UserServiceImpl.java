package com.example.app.service;

import java.io.File;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.app.domain.Course;
import com.example.app.domain.CourseEntry;
import com.example.app.domain.Task;
import com.example.app.domain.TaskUpload;
import com.example.app.domain.User;
import com.example.app.mapper.UserMapper;

@Service
@Transactional(rollbackFor = Exception.class)
//Transactional、どの処理を行っても、失敗したら処理前の状態に戻す
//仮に1～4の工程のうち、3で失敗すると、1，2で行われた処理もなかったことになる

public class UserServiceImpl implements UserService {

	@Autowired
	UserMapper userMapper;
	
	@Override
	public List<User> getUserList() throws Exception{
		return userMapper.selectAllUsers();
	}
	
	@Override
	public User getUserById(Integer id) throws Exception {
		return userMapper.selectById(id);
	}
	
	@Override
	public User getUserByMail(String mail) throws Exception {
		return userMapper.selectByMail(mail);
	}

	@Override
	public void createUser(User user) throws Exception {
		user.setPass(BCrypt.hashpw(user.getPass(),BCrypt.gensalt() ));
		userMapper.insert(user);
	}

	@Override
	public void updateUser(User user) throws Exception {
		user.setPass(BCrypt.hashpw(user.getPass(),BCrypt.gensalt() ));
		userMapper.update(user);
		
	}

	@Override
	public void deleteUser(Integer id) throws Exception {
		userMapper.delete(id);
		
	}

	@Override
	public boolean isCorrectIdAndPassword(String mail, String pass) throws Exception {
		User user = userMapper.selectByMail(mail);
		
		//ログインIDの正誤判定⇒否ならばデータは取得せず
		if(user == null) {
			return false;
		}
		
		//パスワードの正誤判定
		if(!BCrypt.checkpw(pass,user.getPass())) {
				return false;
		}
		return true;
}

	@Override
	public List<User> getAllCourse(Integer id) throws Exception {
		return userMapper.selectAllCourse(id);
	}

	@Override
	public List<Course> getCourseByUserId(Integer id) throws Exception {
		return userMapper.selectCourseByUserId(id);
	}

	@Override
	public void courseEntry(CourseEntry courseEntry) throws Exception {
		userMapper.insertEntryCourse(courseEntry);
	}

	@Override
	public List<Task> getUserTaskListByCourseId(Integer id) throws Exception {
		return userMapper.selectUserTaskListByCourseId(id);
	}

	@Override
	public Course getUserCoursePageByCourseId(Integer id) throws Exception {
		return userMapper.selectUserCoursePageByCourseId(id);
	}

	@Override
	public Task getUserTaskDetailByTaskId(Integer id) throws Exception {
		return userMapper.selectUserTaskDetailByTaskId(id);
	}
	
	@Override
	public TaskUpload getTaskUploadPageByTaskId(Integer id) throws Exception {
		return userMapper.selectTaskUploadPageByTaskId(id);
	}

	@Override
	public void addTaskUpload(TaskUpload taskUpload) throws Exception {

		MultipartFile upfile = taskUpload.getUpfile();
	
		// TOPファイルが選択されている場合の処理
		if (!upfile.isEmpty()) {

			// ファイル名を取得
			String tp = upfile.getOriginalFilename();

			// ファイル名の最後の[.]以降を取得=拡張子名
			String topExt = tp.substring(tp.lastIndexOf(".") + 1);

			// 確認
			System.out.println("file name : " + tp);
			System.out.println("extension : " + topExt);

			// System.currentTimeMillis()で、ファイル名に現在の時間を足す
			tp = "file" + System.currentTimeMillis() + "." + topExt;
			File file = new File("/home/trainee/gallery/" + tp);

			//開発環境　C:/Users/user/gallery/
			//本番環境	/home/trainee/gallery/
			
			taskUpload.setFileName(tp);
			upfile.transferTo(file);
			
		}
		userMapper.insertTaskUpload(taskUpload);
	}

	@Override
	public List<TaskUpload> getAllUserUploadByTaskId(Integer id) throws Exception {
		return userMapper.selectAllUserUploadByTaskId(id);
	}

	@Override
	public Integer getSumTaskTimeByUserId(Integer id) throws Exception {
		return userMapper.selectSumTaskTimeByUserId(id);
	}

	@Override
	public Integer getTaskUploadCountByUserId(Integer id) throws Exception {
		return userMapper.selectTaskUploadCountByUserId(id);
	}

	@Override
	public List<CourseEntry> getCourseEntryByUserId(Integer id) throws Exception {
		return userMapper.selectCourseEntryByUserId(id);
	}

	@Override
	public List<TaskUpload> getUploadCourseCategoryByUserId(Integer id) throws Exception {
		return userMapper.selectUploadCourseCategoryByUserId(id);
	}

}