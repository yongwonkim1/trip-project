<template>
  <div class="container">
    <h1>MTTI 입력 페이지</h1>
    <iframe src="https://www.metavv.com/ko/content/10850815" height="700px" width="400px"></iframe>
    <div class="input-group"></div>
    <div class="container">
      <div class="button-grid">
        <div :class="A ? 'mtti-btn-sel' : 'mtti-btn'" @click="A ? '' : toggleA()">
          A<br />Activity<br />활동
        </div>
        <div :class="F ? 'mtti-btn-sel' : 'mtti-btn'" @click="F ? '' : toggleF()">
          F<br />Familiar<br />익숙
        </div>
        <div :class="I ? 'mtti-btn-sel' : 'mtti-btn'" @click="I ? '' : toggleI()">
          I<br />Itinerary<br />여행계획표
        </div>
        <div :class="T ? 'mtti-btn-sel' : 'mtti-btn'" @click="T ? '' : toggleT()">
          T<br />Taste<br />체험
        </div>

        <div :class="!A ? 'mtti-btn-sel' : 'mtti-btn'" @click="!A ? '' : toggleA()">
          R<br />Rest<br />휴식
        </div>
        <div :class="!F ? 'mtti-btn-sel' : 'mtti-btn'" @click="!F ? '' : toggleF()">
          S<br />Strange<br />낯섬
        </div>
        <div :class="!I ? 'mtti-btn-sel' : 'mtti-btn'" @click="!I ? '' : toggleI()">
          W<br />Whatever<br />아무거나
        </div>
        <div :class="!T ? 'mtti-btn-sel' : 'mtti-btn'" @click="!T ? '' : toggleT()">
          V<br />Visual<br />시각
        </div>
      </div>
      <button @click="submit" class="submit-btn">입력</button>
    </div>
  </div>
</template>

<script setup>
import axios from "axios";
import { useMemberStore } from "@/stores/member";
import { ref } from "vue";
import { useRouter } from "vue-router";
const router = useRouter();
const memberStore = useMemberStore();
const A = ref(true);
const F = ref(true);
const I = ref(true);
const T = ref(true);

const toggleA = () => {
  A.value = !A.value;
};
const toggleF = () => {
  F.value = !F.value;
};
const toggleI = () => {
  I.value = !I.value;
};
const toggleT = () => {
  T.value = !T.value;
};

const submit = () => {
  var mtti = "";
  if (A.value) {
    mtti += "A";
  } else {
    mtti += "R";
  }
  if (F.value) {
    mtti += "F";
  } else {
    mtti += "S";
  }
  if (I.value) {
    mtti += "I";
  } else {
    mtti += "W";
  }
  if (T.value) {
    mtti += "T";
  } else {
    mtti += "V";
  }
  console.log(mtti);
  axios
    .put(
      import.meta.env.VITE_BACKEND_ADDRESS +
        "/user/changeMtti" +
        `?userId=${memberStore.userInfo.userId}&mtti=${mtti}`
    )
    .then(() => {
      alert("mtti수정이 완료되었습니다");
      router.push("/");
    });
};
</script>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
}

h1 {
  font-size: 24px;
  margin-bottom: 20px;
}

.input-group {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

input {
  padding: 10px;
  font-size: 16px;
  width: 300px;
  margin-right: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.submit-btn {
  padding: 10px 20px;
  font-size: 16px;
  cursor: pointer;
  background-color: rgb(255, 218, 96);
  color: rgb(73, 73, 73);
  border: none;
  border-radius: 4px;
  transition: background-color 0.3s;
  box-shadow: 2px 2px 2px 2px gray;
}

.submit-btn:hover {
  background-color: rgb(255, 208, 53);
}

iframe {
  border: 1px solid #ccc;
  border-radius: 4px;
}

.button-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 10px;
  padding: 20px;
  background: rgba(255, 255, 255, 0.8);
  border-radius: 10px;
}

.mtti-btn {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 20px;
  background: rgb(175, 175, 255);
  color: white;
  font-size: 20px;
  font-weight: bold;
  text-align: center;
  border-radius: 10px;
  cursor: pointer;
  box-shadow: 3px 3px 3px 3px gray;
  transition: background 0.3s ease;
}
.mtti-btn-sel {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 20px;
  color: white;
  font-size: 20px;
  font-weight: bold;
  text-align: center;
  border-radius: 10px;
  cursor: pointer;
  transition: background 0.3s ease;
  box-shadow: 3px 3px 3px 3px gray;
  background-color: rgb(130, 130, 255);
}
.mtti-btn:hover {
  background: rgb(130, 130, 255);
}
</style>
