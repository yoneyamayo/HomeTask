package com.example.app.config;

import java.util.ResourceBundle;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.app.filter.AuthFilter;

@Configuration
public class ApplicationConfig implements WebMvcConfigurer {
	// バリデーションメッセージのカスタマイズ
	@Bean
	public Validator getValidator() {
		var validator = new LocalValidatorFactoryBean();
		validator.setValidationMessageSource(messageSource());
		return validator;
	}

	// 認証用フィルタの有効化
	@Bean
	public FilterRegistrationBean<AuthFilter> authFilter() {
		var bean = new FilterRegistrationBean<AuthFilter>(new AuthFilter());
		bean.addUrlPatterns("/user/allUserTask/*");
		bean.addUrlPatterns("/user/courseDetail");
		bean.addUrlPatterns("/user/courseList");
		bean.addUrlPatterns("/user/coursePage");
		bean.addUrlPatterns("/user/mypage");
		bean.addUrlPatterns("/user/taskDetail");
		bean.addUrlPatterns("/user/taskPage");
		bean.addUrlPatterns("/user/taskUpload");
		bean.addUrlPatterns("/user/update");
		bean.addUrlPatterns("/user/uploadTask");
		
		bean.addUrlPatterns("/teacher/addCourse");
		bean.addUrlPatterns("/teacher/addTask");
		bean.addUrlPatterns("/teacher/course");
		bean.addUrlPatterns("/teacher/mypage");
		bean.addUrlPatterns("/teacher/update");
		bean.addUrlPatterns("/teacher/updateCourse");		
		return bean;
	} 

	@Bean
	public ResourceBundleMessageSource messageSource() {
		var messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("validation");
		return messageSource;
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		ResourceBundle bundle = ResourceBundle.getBundle("application");
		String path = bundle.getString("upload.path");
	
		registry.addResourceHandler("/gallery/**")
			.addResourceLocations(path);
	}

}