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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.app.domain.Course;
import com.example.app.domain.CourseEntry;
import com.example.app.domain.TaskUpload;
import com.example.app.domain.User;
import com.example.app.service.UserService;
import com.example.app.validation.UserCreateGroup;
import com.example.app.validation.UserLoginGroup;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserService service;

	// <新規会員登録>表示
	@GetMapping("/create")
	public String createGet(Model model) throws Exception {
		model.addAttribute("user", new User()); // htmlでobjectとして使うため、インスタンス化
		model.addAttribute("title", "新規会員登録");
		return "user/create";
	}

	// <新規会員登録>実行 insert
	@PostMapping("/create")
	public String createPost(@Validated(UserCreateGroup.class) User user, Errors errors, Model model) throws Exception {
		// バリデーションエラーの場合、フォームを再表示
		if (errors.hasErrors()) {
			// エラー内容の補足
			List<ObjectError> objList = errors.getAllErrors();
			for (ObjectError obj : objList) {
				System.out.println(obj);
				return "user/create";
			}
		}
		service.createUser(user);
		return "redirect:/user/login"; // ブラウザのURLを変えたいとき
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Integer id,
			RedirectAttributes rd)throws Exception{
		service.deleteUser(id);
		rd.addFlashAttribute("statusMessage","会員情報を削除しました。");
		return "redirect:/user/login";
	}
	
	// <ログイン>表示
	@GetMapping("/login")
	public String loginGet(Model model) throws Exception {
		model.addAttribute("user", new User()); // htmlでobjectとして使うため、インスタンス化
		return "user/login";
	}

	// <ログイン>実行
	@PostMapping("/login")
	public String login(@Validated(UserLoginGroup.class) User user, Errors errors, HttpSession session)
			throws Exception {
		// 入力不備
		if (errors.hasErrors()) {
			return "user/login";
		}
		// パスワードが正しくない
		if (!service.isCorrectIdAndPassword(user.getMail(), user.getPass())) {
			errors.reject("error.wrong_id_or_password");
			return "user/login";
		} // 正しいログイン ID とパスワード
			// セッションにログイン ID を格納し、リダイレクト
		User u = service.getUserByMail(user.getMail());

		session.setAttribute("id", u.getId());
		session.setAttribute("name", u.getName());
		session.setAttribute("mail", u.getMail());
		session.setAttribute("create", u.getCreated());
		return "redirect:/user/mypage";
	}

	// <マイページ>表示
	@GetMapping("/mypage")
	public String mypageGet(Model model, HttpSession session) throws Exception {
	
		List<Course> courseList = service.getCourseByUserId((int) session.getAttribute("id"));
		Integer sumTaskTime = service.getSumTaskTimeByUserId((int) session.getAttribute("id"));
		Integer countTaskUpload = service.getTaskUploadCountByUserId((int) session.getAttribute("id"));
		List<TaskUpload> taskUploadList = service.getUploadCourseCategoryByUserId((int) session.getAttribute("id"));
		
		Integer A = 0;
		if(countTaskUpload != null && sumTaskTime != null) {
			A = 100 * countTaskUpload / sumTaskTime;
		}
		if(sumTaskTime == null) {
			sumTaskTime = 0;
		}
		model.addAttribute("courseList", courseList);
		model.addAttribute("sumTaskTime", sumTaskTime);
		model.addAttribute("countTaskUpload", countTaskUpload);
		model.addAttribute("A", A);
		model.addAttribute("taskUploadList", taskUploadList);
	System.out.println(taskUploadList);
		return "user/mypage";
	}
	
	// <マイページ>ログアウト
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		// セッションを破棄し、トップページへ遷移
		session.invalidate();
		return "redirect:/user/login";
	}

	// <個人情報編集>表示
	@GetMapping("/update/{id}")
	public String editGet(@PathVariable Integer id, Model model) throws Exception {

		model.addAttribute("title", "登録情報の変更");
		model.addAttribute("user", service.getUserById(id));
		return "user/update";
	}

	// <個人情報編集>実行
	@PostMapping("/update/{id}")
	public String updatePost(@Validated(UserCreateGroup.class) User user, Errors errors, @PathVariable Integer id,
			Model model, HttpSession session) throws Exception {
		// バリデーションエラーの場合、フォームを再表示
		if (errors.hasErrors()) {
			// エラー内容の補足
			List<ObjectError> objList = errors.getAllErrors();
			for (ObjectError obj : objList) {
				System.out.println(obj);
				return "user/update";
			}
		}
		service.updateUser(user);
		session.setAttribute("name", user.getName());
		session.setAttribute("mail", user.getMail());
		return "redirect:/user/mypage"; // ブラウザのURLを変えたいとき
	}

	// <全コース>表示
	@GetMapping("/courseList")
	public String courseListGet(Model model,Integer id, HttpSession session) throws Exception {
		List<User> courseList = service.getAllCourse((int) session.getAttribute("id"));

		model.addAttribute("courseList", courseList);
		System.out.println(session.getAttribute("id"));
		return "user/courseList";
	}

	// <全コース>応募
	@GetMapping("/entryCourse/{id}")
	public String courseListPost(@PathVariable Integer id, CourseEntry courseEntry, HttpSession session)
			throws Exception {
		courseEntry.setCourseId(id);
		int userId = (int) session.getAttribute("id");
		courseEntry.setUserId(userId);
		service.courseEntry(courseEntry);
		return "redirect:/user/mypage";
	}

	// <コース詳細ページ> 表示
	@GetMapping("/coursePage/{id}")
	public String courseGet(@PathVariable Integer id, Model model) throws Exception {

		model.addAttribute("course", service.getUserCoursePageByCourseId(id));
		model.addAttribute("taskList", service.getUserTaskListByCourseId(id));
		return "user/coursePage";
	}

//		<タスク詳細>表示
	@GetMapping("/taskPage/{id}")
	public String taskPageGet(@PathVariable Integer id, Model model) throws Exception {
		model.addAttribute("task", service.getUserTaskDetailByTaskId(id));
		return "user/taskPage";

	}

	// <タスク提出> 表示
	@GetMapping("/taskUpload/{id}")
	public String taskUploadGet(@PathVariable Integer id, Model model) throws Exception {
		model.addAttribute("taskUpload", service.getTaskUploadPageByTaskId(id));
		return "user/taskUpload";
	}

	// <タスク提出> 実行
	@PostMapping("/taskUpload/{id}")
	public String taskUploadPost(@PathVariable Integer id, @Valid TaskUpload taskUpload, Errors errors, Model model,
			HttpSession session) throws Exception {

		MultipartFile upfile = taskUpload.getUpfile();
		String fileType = upfile.getContentType();

		if (upfile.isEmpty()) {
			errors.rejectValue("upfile", "error.not_file");
		}
	
		System.out.println("エラー数：" + errors.getErrorCount());
		// バリデーション(①ファイルの有無、②画像の是非⇒非のエラー表示)
		// バリデーションエラーの場合、フォームを再表示
		if (errors.hasErrors()) {
			// エラー内容の補足
			List<ObjectError> objList = errors.getAllErrors();
			for (ObjectError obj : objList) {
				System.out.println(obj);
			}
		}
		
		if (errors.hasErrors()) {
			return "user/taskUpload";
		}
		
		int userId = (int) session.getAttribute("id");
		taskUpload.setUserId(userId);
		taskUpload.setTaskId(id);
		taskUpload.setFileType(fileType);

		Integer courseId = service.getUserTaskDetailByTaskId(id).getCourseId();
		service.addTaskUpload(taskUpload);
		return "redirect:/user/coursePage/" + courseId; // ブラウザのURLを変えたいとき
	}

	// <あるタスクに提出された課題全て> 表示
	@GetMapping("/allUserTask/{id}")
	public String allUserTaskGet(@PathVariable Integer id, Model model) throws Exception {
		model.addAttribute("allTaskList", service.getAllUserUploadByTaskId(id));
		return "user/allUserTask";
	}
}
