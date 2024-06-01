<template>
	<div class="rootContainer">
		<loading :loading="isLoading" />
		<div :class="['container', { 'right-panel-active': isActive }]">
			<!-- Sign Up -->
			<div class="container__form container--signup">
				<form @submit.prevent="signup" class="form" id="form1">
					<h2 class="form__title">Sign Up</h2>
					<input
						class="input"
						type="text"
						v-model="userInfo.userId"
						required
						placeholder="아이디"
					/>
					<input
						class="input"
						type="password"
						v-model="userInfo.userPw"
						required
						placeholder="비밀번호"
					/>
					<input
						class="input"
						type="password"
						v-model="confirmPassword"
						required
						placeholder="비밀번호 확인"
					/>
					<input
						class="input"
						type="text"
						v-model="userInfo.userName"
						required
						placeholder="이름"
					/>
					<input
						class="input"
						type="text"
						v-model="userInfo.nickname"
						required
						placeholder="닉네임"
					/>
					<div class="radio-box">
						Gender
						<div class="select">
							<input
								type="radio"
								id="male"
								name="gender"
								v-model="userInfo.gender"
								value="M"
								checked
							/><label for="male">Male</label>
							<input
								type="radio"
								id="female"
								name="gender"
								value="F"
								v-model="userInfo.gender"
							/><label for="female">Female</label>
						</div>
					</div>
					<div class="group">
							<p>Region</p>
							<select
								id="modal-select-box"
								class="input select"
                style="height: 10%; margin-left: 8%;"
								v-model="userInfo.sido_code"
								required
							>
								<option
									class="option"
									:value="key"
									v-for="(value, key) in sido"
									:key="key"
								>
									{{ value }}
								</option>
							</select>
						</div>
					<input
						class="input"
						type="date"
						v-model="userInfo.birth"
						required
						placeholder="생일"
					/>
					프로필 이미지
					<input
						type="file"
						id="image"
						accept="image/*"
						@change="handleImageUpload"
					/>
					<button class="btn signup" type="submit">Sign Up</button>
				</form>
				<p v-if="error" class="error">{{ error }}</p>
			</div>
			<!-- Sign In -->
			<div class="container__form container--signin">
				<form @submit.prevent="login" class="form" id="form2">
					<h2 class="form__title">Sign In</h2>
					<input
						class="input"
						type="text"
						v-model="loginUser.userId"
						required
						placeholder="ID"
					/>
					<input
						class="input"
						type="password"
						v-model="loginUser.userPw"
						required
						placeholder="Password"
					/><br />
					<router-link class="link"
						>Forgot your password?</router-link
					>
					<button class="btn login" type="submit">Sign In</button>
					<p v-if="error" class="error">{{ error }}</p>
				</form>
			</div>

			<!-- Overlay -->
			<div class="container__overlay">
				<div class="overlay">
					<div class="overlay__panel overlay--left">
						<button @click="signIn" class="btn2 third" id="signIn">
							Sign In
						</button>
					</div>
					<div class="overlay__panel overlay--right">
						<button @click="signUp" class="btn2 third" id="signUp">
							Sign Up
						</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<script setup>
import { onMounted, ref } from "vue";
import { useMenuStore } from "@/stores/menu";
import { useMemberStore } from "@/stores/member";
import { useFireBaseStore } from "@/stores/firebase";
import axios from "axios";
import { storeToRefs } from "pinia";
import { useRouter } from "vue-router";
import Loading from "@/components/Loading.vue";
import { useSdStore } from "@/stores/searchData";
const sdStore = useSdStore();
const { sido } = storeToRefs(sdStore);
onMounted(() => {
	const fistForm = document.getElementById("form1");
	const secondForm = document.getElementById("form2");
	fistForm.addEventListener("submit", (e) => e.preventDefault());
	secondForm.addEventListener("submit", (e) => e.preventDefault());
});

const isActive = ref(false);
const signIn = () => {
	isActive.value = false;
};
const signUp = () => {
	isActive.value = true;
};

const router = useRouter();
const error = ref("");
const memberStore = useMemberStore();
const firebaseStore = useFireBaseStore();
const { uploadImage } = firebaseStore;
const { isLogin, isLoginError } = storeToRefs(memberStore);
const { userLogin, getUserInfo } = memberStore;
const { changeMenuState } = useMenuStore();
const isLoading = ref(false);

const loginUser = ref({
	userId: "",
	userPw: "",
});

const login = async () => {
	await userLogin(loginUser.value);
	let token = sessionStorage.getItem("accessToken");
	// console.log(token);
	// console.log("isLogin: " + isLogin.value);
	if (isLogin.value) {
		getUserInfo(token);
		changeMenuState();
		router.replace("/");
	}
};

const imageFile = ref();
const confirmPassword = ref("");
const userInfo = ref({
	userId: "",
	userPw: "",
	userName: "",
	birth: "",
	gender: "M",
	nickname: "",
	profileImg: "",
	sido_code: 0,
});

const handleImageUpload = (event) => {
	const file = event.target.files[0];
	imageFile.value = file;
};

const signup = async () => {
	console.log(userInfo);
	isLoading.value = true;
	const upload = async () => {
		userInfo.value.profileImg = await uploadImage(imageFile.value);
	};
	await upload();
	await axios
		.post(
			`${import.meta.env.VITE_BACKEND_ADDRESS}/user/signup`,
			userInfo.value,
			{
				headers: {
					"Content-Type": "application/json",
				},
			}
		)
		.then(() => {
			isLoading.value = false;
			alert("회원가입 완료");
			router.replace("/");
		})
		.catch((error) => {
			isLoading.value = false;
			alert("회원가입 실패");
			console.error(error);
		});

	// console.log(userInfo.value);
};
</script>

<style scoped>
.rootContainer {
	display: flex;
	justify-content: center;
}

:root {
	font-size: 16px;
	font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Oxygen,
		Ubuntu, Cantarell, "Open Sans", "Helvetica Neue", sans-serif;
}

.form__title {
	font-weight: 300;
	margin: 0;
	margin-bottom: 1.25rem;
}

.link {
	color: #333;
	font-size: 0.9rem;
	margin: 1.5rem 0;
	text-decoration: none;
}

.container {
	margin-top: 70px;
	display: flex;
	justify-content: center;
	align-items: center;
	flex-direction: column;

	background-color: #e9e9e9;
	border-radius: 0.7rem;
	box-shadow: 0 0.9rem 1.7rem rgba(0, 0, 0, 0.25),
		0 0.7rem 0.7rem rgba(0, 0, 0, 0.22);
	height: 700px;
	max-width: 758px;
	overflow: hidden;
	position: relative;
	width: 100%;
}

.container__form {
	height: 100%;
	position: absolute;
	top: 0;
	transition: all 0.6s ease-in-out;
}

.container--signin {
	left: 0;
	width: 50%;
	z-index: 2;
}

.container.right-panel-active .container--signin {
	transform: translateX(100%);
}

.container--signup {
	left: 0;
	opacity: 0;
	width: 50%;
	z-index: 1;
}

.container.right-panel-active .container--signup {
	animation: show 0.6s;
	opacity: 1;
	transform: translateX(100%);
	z-index: 3;
}

.container__overlay {
	height: 100%;
	left: 50%;
	overflow: hidden;
	position: absolute;
	top: 0;
	transition: transform 0.6s ease-in-out;
	width: 50%;
	z-index: 4;
}

.container.right-panel-active .container__overlay {
	transform: translateX(-100%);
}

.overlay {
	background-color: rgba(0, 0, 0, 0.95);
	background-attachment: fixed;
	background-position: center;
	background-repeat: no-repeat;
	background-size: cover;
	height: 100%;
	left: -100%;
	position: relative;
	transform: translateX(0);
	transition: transform 0.6s ease-in-out;
	width: 200%;
}

.container.right-panel-active .overlay {
	transform: translateX(50%);
}

.overlay__panel {
	align-items: center;
	display: flex;
	flex-direction: column;
	height: 100%;
	justify-content: center;
	position: absolute;
	text-align: center;
	top: 0;
	transform: translateX(0);
	transition: transform 0.6s ease-in-out;
	width: 50%;
}

.overlay--left {
	transform: translateX(-20%);
}

.container.right-panel-active .overlay--left {
	transform: translateX(0);
}

.overlay--right {
	right: 0;
	transform: translateX(0);
}

.container.right-panel-active .overlay--right {
	transform: translateX(20%);
}

.btn {
	border-radius: 20px;
	/* border: 2px solid #e9e9e9; */
	display: flex;
	justify-content: center;
	align-items: center;
	text-align: center;
	cursor: pointer;
	text-transform: uppercase;
	outline: none;
	overflow: hidden;
	position: relative;
	color: #fff;
	font-weight: bold;
	font-size: 0.8rem;
	background-color: #000;
	padding: 0.9rem 4rem;
	margin: 0 auto;
	box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
	z-index: 1;
}

.btn:after {
	content: "";
	position: absolute;
	left: 0;
	top: 0;
	height: 490%;
	width: 140%;
	background: rgb(195, 195, 252);
	z-index: -1;
	transition: all 0.5s ease-in-out;
	transform: translateX(-98%) translateY(-25%) rotate(45deg);
}

.btn:hover:after {
	transform: translateX(-9%) translateY(-25%) rotate(45deg);
}

.btn:hover {
	color: #000; /* Change text color to black on hover */
}

.form > .btn {
	margin-top: 1.5rem;
}

.btn:active {
	transform: scale(0.95);
}

.btn:focus {
	outline: none;
}

.btn2 {
	box-sizing: border-box;
	appearance: none;
	background-color: transparent;
	border-radius: 20px;
	border: 1px solid rgb(195, 195, 252);
	cursor: pointer;
	display: flex;
	align-self: center;
	font-size: 0.8rem;
	font-weight: bold;
	line-height: 1;
	margin: 20px;
	padding: 0.9rem 4rem;
	text-decoration: none;
	text-align: center;
	text-transform: uppercase;
	font-family: "Montserrat", sans-serif;
	font-weight: 700;
}

.btn2:hover,
.btn2:focus {
	color: #fff;
	outline: 0;
}

.third {
	border-color: rgb(195, 195, 252);
	color: #000;
	box-shadow: 0 0 40px 40px rgb(195, 195, 252) inset,
		0 0 0 0 rgb(195, 195, 252);
	transition: all 150ms ease-in-out;
}

.third:hover {
	box-shadow: 0 0 10px 0 rgb(195, 195, 252) inset,
		0 0 10px 4px rgb(195, 195, 252);
}

.form {
	background-color: #e9e9e9;
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	padding: 0 3rem;
	height: 100%;
	text-align: center;
}

.input {
	background-color: #fff;
	border: none;
	padding: 0.9rem 0.9rem;
	margin: 0.5rem 0;
	width: 100%;
}
.error {
	color: red;
	margin-top: 10px;
}
.radio-box {
	display: flex;
	align-items: center;
	justify-content: space-around;
}
.select {
	padding: 15px 10px;
}
.select input[type="radio"] {
	display: none;
}
.select input[type="radio"] + label {
	display: inline-block;
	cursor: pointer;
	height: 24px;
	width: 90px;
	border: 1px solid #333;
	line-height: 24px;
	text-align: center;
	font-weight: bold;
	font-size: 13px;
}
.select input[type="radio"] + label {
	background-color: #fff;
	color: #333;
}
.select input[type="radio"]:checked + label {
	background-color: #333;
	color: #fff;
}

router-link {
	margin-top: 10px;
	color: #007bff;
	text-decoration: none;
}

router-link:hover {
	color: #0056b3;
}

@keyframes show {
	0%,
	49.99% {
		opacity: 0;
		z-index: 1;
	}

	50%,
	100% {
		opacity: 1;
		z-index: 3;
	}
}

option {
  border: none;
  cursor: pointer;
  background-color: transparent;
  outline: none;
}

.text-truncate {
  text-overflow: ellipsis;
  overflow: hidden;
  white-space: nowrap;
}

.dropdown-container {
  margin-top: 30vh;
  display: flex;
  flex-direction: column;
  width: 100%;
  max-width: 34rem;
}

.dropdown-title-icon,
.dropdown-arrow {
  display: inline-flex;
}

.dropdown-title {
  margin: 0 auto 0 1.8rem;
  text-transform: capitalize;
}

.dropdown-button {
  font-family: "Roboto", sans-serif;
  font-weight: 400;
  font-size: 1.7rem;
  display: flex;
  align-items: center;
  padding: 0 1.8rem;
}

.dropdown-button svg {
  transition: all var(--transition-time) var(--transition-timing);
  fill: var(--text-color);
}

.dropdown-button svg,
.dropdown-button span {
  pointer-events: none;
}

.dropdown-button:hover,
.dropdown-button:focus {
  color: var(--text-active);
}

.dropdown-button:hover svg,
.dropdown-button:focus svg {
  fill: var(--text-active);
}

.main-button {
  height: 5.2rem;
  border-radius: var(--border-radius);
  color: var(--text-color);
  background-color: var(--primary-bg-color);
  border: 0.1rem solid var(--border-color);
  transition: all var(--transition-time) var(--transition-timing);
}

.main-button:focus {
  border: 0.1rem solid var(--primary-color);
  box-shadow: 0 0 0 0.2rem rgba(44, 98, 246, 0.4);
}

.main-button .dropdown-arrow {
  transition: transform var(--transition-time) var(--transition-timing);
  transform: rotate(var(--rotate-arrow));
  margin-left: 1.8rem;
}

.list-button {
  height: var(--list-button-height);
  transition: color var(--transition-time) var(--transition-timing);
  color: var(--text-color);
  overflow: hidden;
  cursor: none;
}

.dropdown-list-container {
  overflow: hidden;
  max-height: var(--dropdown-height);
  transition: max-height var(--transition-time) var(--transition-timing);
}

.dropdown-list-wrapper {
  margin-top: 1rem;
  padding: 1rem;
  background-color: var(--primary-bg-color);
  border-radius: var(--border-radius);
  border: 0.1rem solid var(--border-color);
  position: relative;
}

ul.dropdown-list {
  position: relative;
  list-style-type: none;
}

ul.dropdown-list::before {
  content: "";
  position: absolute;
  top: 0;
  right: 0;
  left: 0;
  z-index: 0;
  opacity: 0;
  height: var(--list-button-height);
  background-color: var(--button-hover-bg-color);
  transition: all var(--transition-time) linear;
  transform: translateY(var(--translate-value));
  border-radius: var(--border-radius);
  pointer-events: none;
}
ul.dropdown-list:hover::before,
ul.dropdown-list:hover ~ .floating-icon {
  opacity: 1;
}

li.dropdown-list-item {
  display: flex;
  flex-direction: column;
  position: relative;
  z-index: 1;
  opacity: var(--list-opacity);
  transition: opacity 0.8s var(--transition-timing);
}

.group{
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
