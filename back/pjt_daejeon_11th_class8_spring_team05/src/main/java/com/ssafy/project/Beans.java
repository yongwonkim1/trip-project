package com.ssafy.project;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ssafy.global.util.JWTUtil;

@Configuration
@MapperScan("com.ssafy.project.*.dao")
public class Beans {
	
	@Bean
	public JWTUtil jwtUtil() {
		return new JWTUtil();
	}
}
