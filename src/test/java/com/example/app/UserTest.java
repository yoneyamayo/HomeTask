package com.example.app;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.example.app.config.ApplicationConfig;
import com.example.app.domain.User;

@SpringBootTest
@ContextConfiguration(classes = {ApplicationConfig.class})
class UserTest {
	
	User user = new User();
	
	Errors errors = new BindException(user,"user");

	@Autowired
	Validator validator;
	
	@Test
	void all_正常系() {
		
		// Given 前提条件
		user.setName("山田太郎");
		user.setMail("yamada@zdrive.com");
		user.setPass("1111");
		user.setCreated(new Date());
		
		// When
		validator.validate(user, errors);
				
		// Then 期待される結果
		assertThat(errors.getFieldError(), is(nullValue()));
		
	}
	
	@ParameterizedTest
	@CsvSource({"01234567890,10文字以内で入力してください,,必須項目です,123,4字以上で入力してください,,必須項目です",
		",必須項目です,google,メールの形式で入力して下さい,,必須項目です,,必須項目です"})
	void name_異常系(
			String name, String msg1, String mail, String msg2, String pass, String msg3, String date, String msg4) {
		// Given
		user.setName(name);
		user.setName(mail);
		user.setMail(pass);
		user.setCreated(new Date());
	}

}
