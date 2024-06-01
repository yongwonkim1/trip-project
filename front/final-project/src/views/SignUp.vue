<template>
  <div class="rootContainer">
    <div class="login-wrap">
      <div class="login-html">
        <input id="tab-1" type="radio" name="tab" class="sign-in" checked /><label
          for="tab-1"
          class="tab"
          >UserInfo</label
        >
        <input id="tab-2" type="radio" name="tab" class="sign-up" /><label for="tab-2" class="tab"
          >Modify</label
        >
        <div class="login-form">
          <div class="sign-in-htm">
            <!--userInfo-->
            <div class="profile">
              <img
                :src="
                  memberStore.profileImage == null
                    ? './src/assets/profile.png'
                    : memberStore.profileImage
                "
                alt="프로필 이미지"
              />
            </div>
            <div class="group">
              <label for="user" class="label">ID</label>
              <input id="user" type="text" class="input" :value="userInfo.userId" disabled />
            </div>
            <div class="group">
              <label for="user" class="label">Name</label>
              <input id="user" type="text" class="input" :value="userInfo.userName" disabled />
            </div>
            <div class="group">
              <label for="user" class="label">NickName</label>
              <input id="user" type="text" class="input" :value="userInfo.nickname" disabled />
            </div>
            <div class="group">
              <label for="pass" class="label">Password</label>
              <input id="pass" type="password" class="input" :value="userInfo.userPw" disabled />
            </div>
            <div class="group">
              <label for="user" class="label">Birth</label>
              <input id="user" type="date" class="input" :value="formattedDate" disabled />
            </div>
            <div class="group">
              <label for="user" class="label">Gender</label>
              <!-- <input id="user" type="text" class="input" :value="userInfo.userInfo.gender === 'm' ? '남성' : '여성'" readonly> -->
              <input id="user" type="text" class="input" value="M" disabled />
            </div>
            <div class="group">
              <label for="user" class="label">Region</label>
              <input
                id="user"
                type="text"
                class="input"
                :value="sido[userInfo.sido_code]"
                disabled
              />
            </div>
            <div class="group">
              <label for="user" class="label">MTTI</label>
              <input id="user" type="text" class="input" :value="userInfo.mtti" disabled />
            </div>
          </div>
          <div class="sign-up-htm">
            <!--userInfo modify-->
            <div class="profile">
              <img
                :src="
                  memberStore.profileImage == null
                    ? './src/assets/profile.png'
                    : memberStore.profileImage
                "
                alt="프로필 이미지"
              />
            </div>
            <div class="group">
              <label for="user" class="label">ID</label>
              <input id="user" type="text" class="input" v-model="userInfo.userId" readonly />
            </div>
            <div class="group">
              <label for="user" class="label">Name</label>
              <input id="user" type="text" class="input" v-model="userInfo.userName" />
            </div>
            <div class="group">
              <label for="user" class="label">NickName</label>
              <input id="user" type="text" class="input" v-model="userInfo.nickname" />
            </div>
            <div class="group">
              <label for="pass" class="label">Password</label>

              <input
                id="pass"
                type="password"
                class="input"
                data-type="password"
                v-model="userInfo.userPw"
              />
            </div>
            <div class="group">
              <label for="pass" class="label">Repeat Password</label>
              <input
                id="pass"
                type="password"
                class="input"
                data-type="password"
                v-model="userInfo.userPw"
              />
            </div>
            <div class="group">
              <label for="user" class="label">birth</label>
              <input id="user" type="date" class="input" v-model="formattedDate" />
            </div>
            <div class="radio-box">
              <label for="user" class="label-g">Gender</label>
              <div class="select">
                <input
                  type="radio"
                  id="male"
                  name="gender"
                  v-model="userInfo.gender"
                  value="M"
                  class="input"
                  checked
                /><label for="male">Male</label>
                <input
                  type="radio"
                  id="female"
                  name="gender"
                  value="F"
                  class="input"
                  v-model="userInfo.gender"
                /><label for="female">Female</label>
              </div>
            </div>
            <div class="group">
              <label for="user" class="label">Region</label>
              <select
                id="modal-select-box"
                class="input select custom-select"
                style="height: 10%"
                v-model="userInfo.sido_code"
                required
              >
                <option value="" disabled style="display: none" selected>지역</option>
                <option class="option" :value="key" v-for="(value, key) in sido" :key="key">
                  {{ value }}
                </option>
              </select>
            </div>
            <div class="group">
              <label for="user" class="label">MTTI</label>
              <button
                id="user"
                type="text"
                class="input"
                @click="getMtti()"
                style="cursor: pointer"
              >
                {{ userInfo.mtti }}
              </button>
            </div>
            <div class="group">
              <button type="submit" class="button" @click="updateUserInfo">
                Update Information
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import { useMemberStore } from "@/stores/member";
import { storeToRefs } from "pinia";
import { useSdStore } from "@/stores/searchData";
import axios from "axios";
const sDStore = useSdStore();
const { sido } = sDStore;
const memberStore = useMemberStore();
const { userInfo, profileImage } = storeToRefs(memberStore);
const router = useRouter();
console.log(userInfo.value.birth);
// userInfo.value.birth에서 날짜를 가져옴
const birthDate = new Date(userInfo.value.birth);
// 원하는 형식으로 날짜 포맷팅
const formattedDate = `${birthDate.getFullYear()}-${String(birthDate.getMonth() + 1).padStart(
  2,
  "0"
)}-${String(birthDate.getDate()).padStart(2, "0")}`;
// 포맷팅된 날짜 출력
console.log(formattedDate);

const emit = defineEmits(["close"]);
const link = (path) => {
  router.push(`/${path}`);
  emit("close");
};

const getMtti = () => {
  if (confirm("MTTI를 수정하러 가시겠습니까?")) {
    router.push("getMtti");
  }
};

const updateUserInfo = () => {
  console.log(userInfo.value);
  axios.put(import.meta.env.VITE_BACKEND_ADDRESS + "/user/changeInfo", userInfo.value).then(() => {
    alert("회원정보가 수정되었습니다.");
    router.push("/");
  });
};
</script>

<style scoped>
*,
:after,
:before {
  box-sizing: border-box;
}
.clearfix:after,
.clearfix:before {
  content: "";
  display: table;
}
.clearfix:after {
  clear: both;
  display: block;
}
a {
  color: inherit;
  text-decoration: none;
}

.rootContainer {
  display: flex;
  justify-content: center;
  margin-top: 5%;
  margin-bottom: 5%;
}

.profile {
  text-align: center;
}

.profile img {
  width: 150px;
  height: 150px;
  border-radius: 50%;
  object-fit: cover;
  margin-bottom: 20px;
  margin-top: 10px;
}

.login-wrap {
  margin-top: 70px;
  width: 100%;
  margin: auto;
  max-width: 758px;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  min-height: 1100px;
  position: relative;
  border-radius: 0.7rem;
  /* background:url(https://raw.githubusercontent.com/khadkamhn/day-01-login-form/master/img/bg.jpg) no-repeat center; */
  box-shadow: 0 12px 15px 0 rgba(0, 0, 0, 0.24), 0 17px 50px 0 rgba(0, 0, 0, 0.19);
}
.login-html {
  width: 100%;
  height: 100%;
  position: absolute;
  border-radius: 0.7rem;
  padding: 90px 70px 50px 70px;
  background: #e9e9e9;
}
.login-html .sign-in-htm,
.login-html .sign-up-htm {
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  position: absolute;
  transform: rotateY(180deg);
  backface-visibility: hidden;
  transition: all 0.4s linear;
}
.login-html .sign-in,
.login-html .sign-up,
.login-form .group .check {
  display: none;
}

.label-g,
.login-html .tab,
.login-form .group .label,
.login-form .group .button {
  text-transform: uppercase;
}
.login-html .tab {
  font-size: 22px;
  margin-right: 15px;
  padding-bottom: 5px;
  margin: 0 15px 10px 0;
  display: inline-block;
  border-bottom: 2px solid transparent;
}
.login-html .sign-in:checked + .tab,
.login-html .sign-up:checked + .tab {
  color: #767676;
  border-color: #e9e9e9;
}
.login-form {
  min-height: 345px;
  position: relative;
  perspective: 1000px;
  transform-style: preserve-3d;
}
.login-form .group {
  margin-bottom: 15px;
  display: flex;
  align-items: center;
  flex-direction: column;
}

.login-form .group .label,
.login-form .group .input,
.login-form .group .button {
  width: 70%;
  justify-content: center;
  text-align: center;
  color: #fff;
  display: block;
}

.login-form .group .input,
.login-form .group .button {
  border: none;
  padding: 15px 20px;
  border-radius: 25px;
  background: rgba(0, 0, 0, 0.6);
}
.login-form .group input[data-type="password"] {
  text-security: circle;
  -webkit-text-security: circle;
}

.label-g,
.login-form .group .label {
  color: #aaa;
  font-size: 12px;
}
.login-form .group .button {
  background: #000;
}
.login-form .group label .icon {
  width: 15px;
  height: 15px;
  border-radius: 2px;
  position: relative;
  display: inline-block;
  background: rgba(255, 255, 255, 0.1);
}
.login-form .group label .icon:before,
.login-form .group label .icon:after {
  content: "";
  width: 10px;
  height: 2px;
  background: #fff;
  position: absolute;
  transition: all 0.2s ease-in-out 0s;
}
.login-form .group label .icon:before {
  left: 3px;
  width: 5px;
  bottom: 6px;
  transform: scale(0) rotate(0);
}
.login-form .group label .icon:after {
  top: 6px;
  right: 0;
  transform: scale(0) rotate(0);
}
.login-form .group .check:checked + label {
  color: #fff;
}
.login-form .group .check:checked + label .icon {
  background: #e9e9e9;
}
.login-form .group .check:checked + label .icon:before {
  transform: scale(1) rotate(45deg);
}
.login-form .group .check:checked + label .icon:after {
  transform: scale(1) rotate(-45deg);
}
.login-html .sign-in:checked + .tab + .sign-up + .tab + .login-form .sign-in-htm {
  transform: rotate(0);
}
.login-html .sign-up:checked + .tab + .login-form .sign-up-htm {
  transform: rotate(0);
}

.hr {
  height: 2px;
  margin: 20px 0 10px 0;
  background: rgba(0, 0, 0, 0.2);
}

.radio-box {
  display: flex;
  align-items: center;
  justify-content: center;
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

.custom-select {
  appearance: none; /* 크로스 브라우저 호환 */
  -webkit-appearance: none;
  -moz-appearance: none;
  background: none; /* 배경을 제거하여 기본 화살표를 숨김 */
  border: 1px solid #ccc; /* 원하는 테두리 스타일 */
  padding: 10px;
  font-size: 16px;
  width: 100%; /* wrapper와 동일한 너비 */
}
</style>
