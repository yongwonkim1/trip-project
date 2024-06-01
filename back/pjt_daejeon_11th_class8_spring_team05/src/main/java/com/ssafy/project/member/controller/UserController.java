package com.ssafy.project.member.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.global.util.JWTUtil;
import com.ssafy.project.member.dto.UserDTO;
import com.ssafy.project.member.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/user")
@Tag(name = "회원 인증 컨트롤러", description = "로그인 로그아웃, 토큰처리등 회원의 인증관련 처리하는 클래스.")
@Slf4j
@CrossOrigin
public class UserController {
	
	private final UserService userService;
	private final JWTUtil jwtUtil;

	public UserController(UserService userService, JWTUtil jwtUtil) {
		super();
		this.userService = userService;
		this.jwtUtil = jwtUtil;
	}
	
	@Operation(summary = "회원가입", description = "회원가입을 처리합니다.")
	@PostMapping("/signup")
	@CrossOrigin
	public ResponseEntity<Map<String, Object>> signup(
			@RequestBody @Parameter(description = "회원가입 시 필요한 회원정보", required = true) UserDTO userDTO) {
		System.out.println(userDTO);
		log.debug("signup user : {}", userDTO);
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.CREATED;
		try {
			System.out.println(userDTO);
			// 아이디 중복확인
			if(userService.checkDuplicateId(userDTO.getUserId())) {
				resultMap.put("message", "아이디가 이미 존재합니다.");
				status = HttpStatus.CONFLICT;
			} else {
				userService.signup(userDTO);
				resultMap.put("message", "회원가입 성공");
			}
		} catch (Exception e) {
			log.error("회원가입 실패 : {}", e);
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<>(resultMap, status);
	}

	@Operation(summary = "로그인", description = "아이디와 비밀번호를 이용하여 로그인 처리.")
	@PostMapping("/login")
	@CrossOrigin
	public ResponseEntity<Map<String, Object>> login(
			@RequestBody @Parameter(description = "로그인 시 필요한 회원정보(아이디, 비밀번호).", required = true) UserDTO userDTO) {
		log.debug("login user : {}", userDTO);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpStatus status = HttpStatus.ACCEPTED;
		try {
			UserDTO loginUser = userService.login(userDTO);
			if(loginUser != null) {
				String accessToken = jwtUtil.createAccessToken(loginUser.getUserId());
				String refreshToken = jwtUtil.createRefreshToken(loginUser.getUserId());
				log.debug("access token : {}", accessToken);
				log.debug("refresh token : {}", refreshToken);
				
//				발급받은 refresh token 을 DB에 저장.
				userService.saveRefreshToken(loginUser.getUserId(), refreshToken);
				
//				JSON 으로 token 전달.
				resultMap.put("access-token", accessToken);
				resultMap.put("refresh-token", refreshToken);
				
				status = HttpStatus.CREATED;
			} else {
				resultMap.put("message", "아이디 또는 패스워드를 확인해 주세요.");
				status = HttpStatus.UNAUTHORIZED;
			} 
			
		} catch (Exception e) {
			log.debug("로그인 에러 발생 : {}", e);
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@Operation(summary = "회원인증", description = "회원 정보를 담은 Token 을 반환한다.")
	@GetMapping("/info/{userId}")
	@CrossOrigin
	public ResponseEntity<Map<String, Object>> getInfo(
			@PathVariable("userId") @Parameter(description = "인증할 회원의 아이디.", required = true) String userId,
			HttpServletRequest request) {
//		logger.debug("userId : {} ", userId);
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		if (jwtUtil.checkToken(request.getHeader("Authorization"))) {
			log.info("사용 가능한 토큰!!!");
			try {
//				로그인 사용자 정보.
				UserDTO userDTO = userService.userInfo(userId);
				resultMap.put("userInfo", userDTO);
				status = HttpStatus.OK;
			} catch (Exception e) {
				log.error("정보조회 실패 : {}", e);
				resultMap.put("message", e.getMessage());
				status = HttpStatus.INTERNAL_SERVER_ERROR;
			}
		} else {
			log.error("사용 불가능 토큰!!!");
			status = HttpStatus.UNAUTHORIZED;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	@Operation(summary = "로그아웃", description = "회원 정보를 담은 Token 을 제거한다.")
	@GetMapping("/logout/{userId}")
	@CrossOrigin
	public ResponseEntity<?> removeToken(@PathVariable ("userId") @Parameter(description = "로그아웃 할 회원의 아이디.", required = true) String userId) {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		try {
			userService.deleRefreshToken(userId);
			status = HttpStatus.OK;
		} catch (Exception e) {
			log.error("로그아웃 실패 : {}", e);
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	@Operation(summary = "Access Token 재발급", description = "만료된 access token 을 재발급 받는다.")
	@PostMapping("/refresh")
	@CrossOrigin
	public ResponseEntity<?> refreshToken(@RequestBody UserDTO userDTO, HttpServletRequest request)
			throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		String token = request.getHeader("refreshToken");
		log.debug("token : {}, userDTO : {}", token, userDTO);
		if (jwtUtil.checkToken(token)) {
			if (token.equals(userService.getRefreshToken(userDTO.getUserId()))) {
				String accessToken = jwtUtil.createAccessToken(userDTO.getUserId());
				log.debug("token : {}", accessToken);
				log.debug("정상적으로 access token 재발급!!!");
				resultMap.put("access-token", accessToken);
				status = HttpStatus.CREATED;
			}
		} else {
			log.debug("refresh token 도 사용 불가!!!!!!!");
			status = HttpStatus.UNAUTHORIZED;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@Operation(summary = "닉네임 조회", description = "사용자의 id로 닉네임 조회")
	@GetMapping("/nickName")
	public String selectNicknameImgById(@RequestParam String userId) {
		return userService.selectNicknameImgById(userId);
	}
	
	@Operation(summary = "프로필사진 조회", description = "사용자의 id로 프로필사진 조회")
	@GetMapping("/image")
	public String selectImgImgById(@RequestParam String id) {
		return userService.selectImgImgById(id);
	}
	
	@Operation(summary = "기본 정보 수정", description = "사용자의 info 수정")
	@PutMapping("/changeInfo")
	public int updateInfo(@RequestBody UserDTO user) {
		return userService.updateInfo(user);
	}
	@Operation(summary = "mtti 수정", description = "사용자의 mtti 수정")
	@PutMapping("/changeMtti")
	public int updateMtti(@RequestParam String userId, @RequestParam String mtti) {
		return userService.updateMtti(userId,mtti);
	}
}
