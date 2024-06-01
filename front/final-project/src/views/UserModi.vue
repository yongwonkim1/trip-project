<template>
  <div class="user-info">
    <h2>회원정보 조회</h2>
    <div class="profile">
      <img :src="userInfo.profile_img" alt="프로필 이미지" />
    </div>
    <div class="info">
      <div class="item">
        <span class="label">아이디:</span>
        <input type="text" v-model="userInfo.user_id" disabled />
      </div>
      <div class="item">
        <span class="label">비밀번호:</span>
        <input type="password" v-model="userInfo.user_pw" />
      </div>
      <div class="item">
        <span class="label">생년월일:</span>
        <input type="text" v-model="userInfo.birth" />
      </div>
      <div class="item">
        <span class="label">성별:</span>
        <select v-model="userInfo.gender">
          <option value="m">남성</option>
          <option value="f">여성</option>
        </select>
      </div>
      <div class="item">
        <span class="label">닉네임:</span>
        <input type="text" v-model="userInfo.nickname" />
      </div>
      <div class="item">
        <span class="label">이름:</span>
        <input type="text" v-model="userInfo.user_name" />
      </div>
      <button class="update-button" @click="updateUserInfo">정보 수정</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";

const userInfo = ref({
  user_id: "",
  user_pw: "",
  birth: "",
  gender: "m",
  nickname: "",
  user_name: "",
  profile_img: "",
});

onMounted(() => {
  userInfo.value = {
    user_id: "user123",
    user_pw: "password",
    birth: "1990-01-01",
    gender: "m",
    nickname: "닉네임",
    user_name: "홍길동",
  };
});

const updateUserInfo = () => {};
</script>

<style scoped>
.user-info {
  max-width: 600px;
  margin: 0 auto;
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
}

.info {
  border: 1px solid #ccc;
  border-radius: 8px;
  padding: 20px;
}

.item {
  margin-bottom: 10px;
}

.label {
  font-weight: bold;
}

input[type="text"],
input[type="password"],
select {
  width: 100%;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 5px;
  box-sizing: border-box;
}

.update-button {
  display: block;
  width: 100%;
  padding: 10px;
  background-color: #4caf50;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.update-button:hover {
  background-color: #45a049;
}
</style>
