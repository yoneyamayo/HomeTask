package com.example.app.controller;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.example.app.domain.Course;
import com.example.app.domain.Task;
import com.example.app.domain.Teacher;
import com.example.app.service.TeacherService;
import com.example.app.validation.TeacherCreateGroup;
import com.example.app.validation.TeacherLoginGroup;

@Controller
@RequestMapping("/teacher")
public class TeacherController {
		
	@Autowired
	TeacherService service;

	// <新規会員登録>表示
	@GetMapping("/create")
	public String createGet(Model model) throws Exception {
		model.addAttribute("teacher", new Teacher()); // htmlでobjectとして使うため、インスタンス化
		model.addAttribute("title", "新規会員登録");
		return "teacher/create";
	}

	// <新規会員登録>実行 insert
	@PostMapping("/create")
	public String createPost(@Validated(TeacherCreateGroup.class) Teacher teacher, Errors errors, Model model)
			throws Exception {
		// バリデーションエラーの場合、フォームを再表示
		if (errors.hasErrors()) {
			// エラー内容の補足
			List<ObjectError> objList = errors.getAllErrors();
			for (ObjectError obj : objList) {
				System.out.println(obj);
				return "teacher/create";
			}
		}
		service.createTeacher(teacher);
		return "redirect:/teacher/login"; // ブラウザのURLを変えたいとき
	}

	// <ログイン>表示
	@GetMapping("/login")
	public String loginGet(Model model) throws Exception {
		model.addAttribute("teacher", new Teacher()); // htmlでobjectとして使うため、インスタンス化
		return "teacher/login";
	}

	// <ログイン>実行
	@PostMapping("/login")
	public String login(@Validated(TeacherLoginGroup.class) Teacher teacher, 
			Errors errors, HttpSession session,Course course)
			throws Exception {
		// 未入力
		if (errors.hasErrors()) {
			return "teacher/login";
		}
		// IDかパスワードが正しくない
		if (!service.isCorrectIdAndPassword(teacher.getMail(), teacher.getPass())) {
			errors.reject("error.wrong_id_or_password");
			return "teacher/login";
		} // 正しいログイン ID とパスワード
			// セッションにログイン ID を格納し、リダイレクト
		Teacher t = service.getTeacherByMail(teacher.getMail());
		session.setAttribute("id", t.getId());
		session.setAttribute("name", t.getName());
		session.setAttribute("mail", t.getMail());
		session.setAttribute("create", t.getCreated());
			
		return "redirect:/teacher/mypage";
	}

	// <マイページ>表示
	@GetMapping("/mypage")
	public String mypageGet(Model model,HttpSession session) throws Exception {
		int teacherId = (int)session.getAttribute("id");
		System.out.println(teacherId);
		List<Course> courseList = service.getCourseListByTeacherId(teacherId);
		System.out.println(courseList);
		model.addAttribute("courseList", courseList);
		return "teacher/mypage";
	}
		
	// <マイページ>ログアウト
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		// セッションを破棄し、トップページへ遷移
		session.invalidate();
		return "redirect:/teacher/login";
	}

	// <個人情報編集>表示
	@GetMapping("/update/{id}")
	public String editGet(@PathVariable Integer id, Model model) throws Exception {

		model.addAttribute("title", "登録情報の変更");
		model.addAttribute("teacher", service.getTeacherById(id));
		return "teacher/update";
	}

	// <個人情報編集>実行
	@PostMapping("/update/{id}")
	public String updatePost(@Validated(TeacherCreateGroup.class) Teacher teacher, Errors errors,
			@PathVariable Integer id, Model model, HttpSession session) throws Exception {
		// バリデーションエラーの場合、フォームを再表示
		if (errors.hasErrors()) {
			// エラー内容の補足
			List<ObjectError> objList = errors.getAllErrors();
			for (ObjectError obj : objList) {
				System.out.println(obj);
				return "teacher/update";
			}
		}
		service.updateTeacher(teacher);
		
		session.setAttribute("id", teacher.getId());
		session.setAttribute("name", teacher.getName());
		session.setAttribute("mail", teacher.getMail());
		
		return "redirect:/teacher/mypage"; // ブラウザのURLを変えたいとき
	}
	
	// <コース>
	// <コース詳細ページ> 表示
	@GetMapping("/course/{id}")
	public String courseGet(@PathVariable Integer id,Model model)throws Exception {		
		model.addAttribute("courseList", service.getCourseDetailByCourseId(id));
		model.addAttribute("taskList", service.getTaskListByCourseId(id));
		model.addAttribute("countTask",service.getCountTaskByCourseId(id));
		return "teacher/course";		
	}
		
	// <コース作成>表示(+ファイルのリスト取得)
	@GetMapping("/addCourse")
	public String addCourseGet(Model model) throws Exception {
		model.addAttribute("course", new Course());
		return "teacher/addCourse";	
	}
	
	// <コース作成>実行 insert
	@PostMapping("/addCourse")
	public String addCoursePost(@Valid Course course, Errors errors, Model model, 
			HttpSession session)
			throws Exception {
		// バリデーション(①ファイルの有無、②画像の是非⇒非のエラー表示)
		 MultipartFile upfile = course.getUpfile();
		 if(!upfile.isEmpty()) {
			 // 画像か否か判定する
			 String type = upfile.getContentType();
						
		 	if(!type.startsWith("image/")) {
		 		// 画像ではない場合、エラーメッセージを表示
		 		errors.rejectValue("imageName", "error.not_image_file");
		 		}
		 	}
		 		
		// バリデーションエラーの場合、フォームを再表示
		if (errors.hasErrors()) {
			// エラー内容の補足
			List<ObjectError> objList = errors.getAllErrors();
			for (ObjectError obj : objList) {
				System.out.println(obj);
				return "teacher/addCourse";
				}
		}		
		int id = (int) session.getAttribute("id");
		course.setTeacherId(id);
		service.addCourse(course);
		return "redirect:/teacher/mypage"; // ブラウザのURLを変えたいとき
	}
	
	// <コース編集>表示(+ファイルのリスト取得)
		@GetMapping("/updateCourse/{id}")
		public String updateCourseGet(@PathVariable Integer id,
				Model model) throws Exception {
			model.addAttribute("course", service.getCourseDetailByCourseId(id));
			return "teacher/updateCourse";		
		}

	@PostMapping("/updateCourse/{id}") //corseId
	public String updateCoursePost(@PathVariable Integer id, @Valid Course course, Errors errors, Model model, 
			HttpSession session)
			throws Exception {
		// バリデーション(①ファイルの有無、②画像の是非⇒非のエラー表示)
		 MultipartFile upfile = course.getUpfile();
		 if(!upfile.isEmpty()) {
			 // 画像か否か判定する
			 String type = upfile.getContentType();
						
		 	if(!type.startsWith("image/")) {
		 		// 画像ではない場合、エラーメッセージを表示
		 		errors.rejectValue("imageName", "error.not_image_file");
		 		}
		 	}
		 		
		// バリデーションエラーの場合、フォームを再表示
		if (errors.hasErrors()) {
			// エラー内容の補足
			List<ObjectError> objList = errors.getAllErrors();
			for (ObjectError obj : objList) {
				System.out.println(obj);
				
				return "teacher/updateCourse";
				}
		}		
		int teacherId = (int) session.getAttribute("id");
		course.setTeacherId(teacherId);
		service.updateCourse(course);
		return "redirect:/teacher/course/{id}"; // ブラウザのURLを変えたいとき
	}
	
	// <タスク>
	// <タスク作成>表示
	@GetMapping("/addTask/{id}")
	public String addTaskGet(@PathVariable Integer id,Model model) throws Exception {
		model.addAttribute("course", service.getCourseDetailByCourseId(id));
		model.addAttribute("task", new Task());
		
		return "teacher/addTask";
	}
	
	// <タスク作成>実行 insert
		@PostMapping("/addTask/{id}")
		public String addTaskPost(@PathVariable Integer id,@Valid Task task, Errors errors, Model model, 
				HttpSession session)
				throws Exception {
				
			 MultipartFile topfile = task.getTopfile();
			 	
			// topfileバリデーション(①ファイルの有無、②画像の是非⇒非のエラー表示)
			 if(!topfile.isEmpty()) {
				 // 画像か否か判定する
				 //まずはデータの種類を取得
				 String type = topfile.getContentType();
							
			 	if(!type.startsWith("image/")) {
			 		// 画像ではない場合、エラーメッセージを表示
			 		errors.rejectValue("imageName", "error.not_image_file");
			 		}
			 	}
			// バリデーションエラーの場合、フォームを再表示
			if (errors.hasErrors()) {
				// エラー内容の補足
				List<ObjectError> objList = errors.getAllErrors();
				for (ObjectError obj : objList) {
					System.out.println(obj);
					return "teacher/addTask/{id}";
					}
			}		
			
			task.setCourseId(id);
			service.addTask(task);
			
			return "redirect:/teacher/course/{id}"; // ブラウザのURLを変えたいとき
		}	
		
		// <あるタスクに提出された課題全て> 表示
		@GetMapping("/allUserTask/{id}")
		public String allUserTaskGet(@PathVariable Integer id, Model model) throws Exception {
			model.addAttribute("allTaskList", service.getUserTaskByTaskId(id));
			return "teacher/allUserTask";
		}
	
}

