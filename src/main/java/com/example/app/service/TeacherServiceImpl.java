package com.example.app.service;

import java.io.File;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.app.domain.Course;
import com.example.app.domain.Task;
import com.example.app.domain.TaskUpload;
import com.example.app.domain.Teacher;
import com.example.app.mapper.TeacherMapper;

@Service
@Transactional(rollbackFor = Exception.class)
//Transactional、どの処理を行っても、失敗したら処理前の状態に戻す
//仮に1～4の工程のうち、3で失敗すると、1，2で行われた処理もなかったことになる

public class TeacherServiceImpl implements TeacherService {

	@Autowired
	TeacherMapper teacherMapper;

	@Override
	public List<Teacher> getTeacherList() throws Exception {
		return teacherMapper.selectAllTeachers();
	}

	@Override
	public Teacher getTeacherById(Integer id) throws Exception {
		return teacherMapper.selectById(id);
	}

	@Override
	public Teacher getTeacherByMail(String mail) throws Exception {
		return teacherMapper.selectByMail(mail);
	}

	@Override
	public List<Course> getCourseListByTeacherId(Integer id) throws Exception {
		return teacherMapper.selectCourseByTeacherId(id);
	}

	@Override
	public void createTeacher(Teacher teacher) throws Exception {
		teacher.setPass(BCrypt.hashpw(teacher.getPass(), BCrypt.gensalt()));
		teacherMapper.insert(teacher);
	}

	@Override
	public void updateTeacher(Teacher teacher) throws Exception {
		teacher.setPass(BCrypt.hashpw(teacher.getPass(), BCrypt.gensalt()));
		teacherMapper.update(teacher);

	}

	@Override
	public void deleteTeacher(Integer id) throws Exception {
		teacherMapper.delete(id);

	}

	@Override
	public boolean isCorrectIdAndPassword(String mail, String pass) throws Exception {
		Teacher teacher = teacherMapper.selectByMail(mail);

		// ログインIDの正誤判定⇒否ならばデータは取得せず
		if (teacher == null) {
			return false;
		}

		// パスワードの正誤判定
		if (!BCrypt.checkpw(pass, teacher.getPass())) {
			return false;
		}
		return true;
	}

	@Override
	public void addCourse(Course course) throws Exception {

		// 画像が選択されている場合の処理
		MultipartFile upfile = course.getUpfile();
		if (!upfile.isEmpty()) {

			// System.currentTimeMillis()で現在の時間を取ってきて、ファイル名に足す
			String fileName = upfile.getOriginalFilename();

			// 画像ファイルの保存
			String ext = fileName.substring(fileName.lastIndexOf(".") + 1);

			System.out.println("file name : " + fileName);
			System.out.println("extension : " + ext);
			fileName = "file" + System.currentTimeMillis() + "." + ext;
			File file = new File("/home/trainee/gallery/" + fileName);

			course.setImageName(fileName);
			upfile.transferTo(file);
		}

		teacherMapper.insertCourse(course);
	}

	@Override
	public void addTask(Task task) throws Exception {
		MultipartFile topfile = task.getTopfile();
		MultipartFile subfile1 = task.getSubfile1();
		MultipartFile subfile2 = task.getSubfile2();
		MultipartFile subfile3 = task.getSubfile3();

		// TOPファイルが選択されている場合の処理
		if (!topfile.isEmpty()) {

			// ファイル名を取得
			String tp = topfile.getOriginalFilename();

			// ファイル名の最後の[.]以降を取得=拡張子名
			String topExt = tp.substring(tp.lastIndexOf(".") + 1);

			// 確認
			System.out.println("file name : " + tp);
			System.out.println("extension : " + topExt);

			// System.currentTimeMillis()で、ファイル名に現在の時間を足す
			tp = "file" + System.currentTimeMillis() + "." + topExt;
			File file = new File("/home/trainee/gallery/" + tp);

			task.setImageName(tp);
			topfile.transferTo(file);

			// subファイル1が選択されている場合の処理
			if (!subfile1.isEmpty()) {
				String sf1 = subfile1.getOriginalFilename();
				String subExt1 = sf1.substring(sf1.lastIndexOf(".") + 1);

				System.out.println("file name : " + sf1);
				System.out.println("extension : " + subExt1);

				sf1 = "file" + System.currentTimeMillis() + "." + subExt1;
				File subFile1 = new File("/home/trainee/gallery/" + sf1);

				task.setFileName1(sf1);
				subfile1.transferTo(subFile1);

				String type = subfile1.getContentType();
				task.setFileType1(type);

				// subファイル2が選択されている場合の処理
				if (!subfile2.isEmpty()) {

					String sf2 = subfile2.getOriginalFilename();

					String subExt2 = sf2.substring(sf2.lastIndexOf(".") + 1);

					System.out.println("file name : " + sf2);
					System.out.println("extension : " + subExt2);

					sf2 = "file" + System.currentTimeMillis() + "." + subExt2;
					File subFile2 = new File("/home/trainee/gallery/" + sf2);

					task.setFileName2(sf2);
					subfile2.transferTo(subFile2);

					String type2 = subfile2.getContentType();
					task.setFileType2(type2);

					// subファイル3が選択されている場合の処理
					if (!subfile3.isEmpty()) {

						String sf3 = subfile3.getOriginalFilename();

						String subExt3 = sf3.substring(sf3.lastIndexOf(".") + 1);

						System.out.println("file name : " + sf3);
						System.out.println("extension : " + subExt3);

						sf3 = "file" + System.currentTimeMillis() + "." + subExt3;
						File file2 = new File("/home/trainee/gallery/" + sf3);

						task.setFileName3(sf3);
						subfile3.transferTo(file2);

						String type3 = subfile3.getContentType();
						task.setFileType3(type3);

					}
				}
			}
		}

		teacherMapper.insertTask(task);
	}

	@Override
	public Course getCourseDetailByCourseId(Integer id) throws Exception {
		return teacherMapper.selectCourseDetailByCourseId(id);
	}

	@Override
	public List<Task> getTaskListByCourseId(Integer id) throws Exception {
		return teacherMapper.selectTaskByCourseId(id);
	}

	@Override
	public void updateCourse(Course course) throws Exception {
		
		// 画像が選択されている場合の処理
		MultipartFile upfile = course.getUpfile();
		if (!upfile.isEmpty()) {

			// System.currentTimeMillis()で現在の時間を取ってきて、ファイル名に足す
			String fileName = upfile.getOriginalFilename();

			// 画像ファイルの保存
			String ext = fileName.substring(fileName.lastIndexOf(".") + 1);

			System.out.println("file name : " + fileName);
			System.out.println("extension : " + ext);
			fileName = "file" + System.currentTimeMillis() + "." + ext;
			File file = new File("/home/trainee/gallery/" + fileName);

			course.setImageName(fileName);
			upfile.transferTo(file);
		}
		teacherMapper.updateCourse(course);
	}

	@Override
	public Integer getCountTaskByCourseId(Integer id) throws Exception {
		return teacherMapper.selectCountTaskByCourseId(id);
	}

	@Override
	public List<TaskUpload> getUserTaskByTaskId(Integer id) throws Exception {
		return teacherMapper.selectUserTaskByTaskId(id);
	}

}
