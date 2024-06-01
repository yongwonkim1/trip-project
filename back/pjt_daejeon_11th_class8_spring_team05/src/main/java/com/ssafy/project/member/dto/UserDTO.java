package com.ssafy.project.member.dto;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Schema(title = "UserDTO : 회원정보", description = "회원의 상세 정보를 나타낸다.")
public class UserDTO {

	@Schema(description = "아이디", required = true)
	private String userId;

	@Schema(description = "비밀번호", required = true)
	private String userPw;

	@Schema(description = "이름", required = true)
	private String userName;

	@Schema(description = "닉네임", required = true)
	private String nickname;

	@Schema(description = "프로필 사진")
	private String profileImg;

	@Schema(description = "성별", required = true)
	private String gender;

	@Schema(description = "생일", required = true)
	private Date birth;
	
	@Schema(description = "지역")
	private int sido_code;
	
	@Schema(description = "MTTI")
	private String mtti;

	@Schema(description = "refreshToken")
	private String refreshToken;
}
