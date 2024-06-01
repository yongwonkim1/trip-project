<script setup>
import { onMounted, ref } from "vue";
import OpenAI from "openai";
import Loading from "@/components/Loading.vue";
import { useMemberStore } from "@/stores/member";
import { usePlanStore } from "@/stores/plan";
import manImage from "@/assets/man1.png";
import femaleImage from "@/assets/female1.png";
import manImage2 from "@/assets/man2.png";
import femaleImage2 from "@/assets/female2.png";
import manImage3 from "@/assets/man3.png";
import femaleImage3 from "@/assets/female3.png";

const planStore = usePlanStore();
const memberStore = useMemberStore();
const { userInfo } = memberStore;
const planList = ref(JSON.parse(history.state.planList));
const newPlan = ref({});
// const mtti = ref("ASIT");
const friendMtti = ref();
const mode = ref(0);
const prefix = ref(
  "안녕하세요! MTTI(My Travel Type Indicator)는 여행 스타일을 알아보기 위한 시스템이에요. 이 시스템은 MBTI라는 성격 유형 지표처럼 여행하는 방식을 분류합니다. MTTI에서는 활동(A) vs 휴식(R), 익숙함(F) vs 낯섬(S), 여행 계획(I) vs 자유로움(W), 그리고 체험(T) vs 시각(V)이라는 네 가지 기준을 사용해요. 이 네 가지 기준을 조합하면 총 16가지 여행 타입이 나옵니다.\n활동(A) vs 휴식(R): 이 기준은 여행하는 동안 즐기는 활동의 양을 나타냅니다. 활동(A)형은 즐거운 활동을 찾고 움직이는 것을 즐기는 사람들이에요. 반면에 휴식(R)형은 여유롭게 휴식을 취하고 자연을 즐기는 것을 선호하는 사람들이죠.\n익숙함(F) vs 낯섬(S): 이 기준은 여행지를 선택할 때 익숙한 곳을 선호하는지, 아니면 새로운 곳을 탐험하는 것을 선호하는지를 나타냅니다. 익숙함(F)형은 익숙한 곳에서 편안함을 느끼고, 낯섬(S)형은 새로운 경험을 즐기며 모험을 찾아 다니는 편이에요.\n여행 계획(I) vs 자유로움(W): 이 기준은 여행을 계획하고 준비하는 것을 좋아하는지, 아니면 그때그때 즉흥적으로 여행하는 것을 선호하는지를 말해요. 여행 계획(I)형은 여행 일정을 세우고 준비하는 것을 좋아하고, 자유로움(W)형은 자유롭게 행동하며 즉흥적으로 여행하는 것을 선호하는 경향이 있어요.\n체험(T) vs 시각(V): 이 기준은 여행을 통해 새로운 경험을 즐기는 것과 아름다운 풍경을 감상하는 것 사이에서 어떤 것을 선호하는지를 나타냅니다. 체험(T)형은 다양한 활동을 통해 새로운 경험을 즐기는 것을 좋아하고, 시각(V)형은 아름다운 풍경이나 문화적인 경관을 감상하는 것을 좋아하는 경향이 있어요."
);

onMounted(() => {
  console.log(userInfo.gender);
  console.log(planStore.selPlan);
  console.log(planList.value);
});

const prompt2 = () => {
  return `MTTI가 ${userInfo.mtti}인 제가 여행 컨셉(${planStore.selPlan.style})의 ${planStore.selPlan.dateOffset}일 ${planList.value[0].sido_name} 여행에 따른 여행 가방 꾸리기 체크리스트를 만들어 주실 수 있을까요? 여행기간(${planStore.selPlan.dateOffset})과 여행지인 ${planList.value[0].sido_name}의 여행 날짜(${planStore.selPlan.start})의 날씨를 고려하여 체크리스트를 만들어주면 좋겠습니다.`;
};

const prompt3 = () => {
  // let prompt = "안녕하세요! MTTI가 ";
  // for (let i = 0; i < items.value.length; i++) {
  //   prompt += items.value[i].text + ", ";
  //   console.log(items.value[i].text);
  // }
  return `MTTI가 ${userInfo.mtti}인 사람이 MTTI가 ${friendMtti.value}인 사람과 여행을 갈 때 주의해야 할 점을 알려주세요. 글자수는 500자 이내로 부탁드려요. 감사합니다!`;
  // . MTTI 16가지 유형 형식에 맞지 않는 MTTI 값을 받으면 '잘못된 MTTI 입력형식입니다' 라고 출력해주세요.
};

const result2 = ref("");
const result3 = ref([]);
const prompt = ref("");
const isLoading = ref(false);

const getGPTResponse = async (num) => {
  isLoading.value = true;
  try {
    const openai = new OpenAI({
      apiKey: import.meta.env.VITE_OPENAI_API_KEY,
      dangerouslyAllowBrowser: true,
    });
    if (num == 2) {
      prompt.value = prefix.value + prompt2();
      pushed.value = true;
      pushed2.value = false;
    } else {
      prompt.value = prefix.value + prompt3();
      pushed2.value = true;
      pushed3.value = false;
    }
    const response = await openai.chat.completions.create({
      messages: [
        {
          role: "user",
          content: prompt.value,
        },
      ],
      model: "gpt-3.5-turbo",
    });
    if (num == 2) {
      result2.value = response.choices[0].message.content;
    } else {
      result3.value.push(response.choices[0].message.content);
    }
    mode.value++;
    console.log(mode.value);
    console.log(result3.value.length);
    isLoading.value = false;
  } catch (error) {
    console.error("chatGPT: 🚨 에러가 발생했습니다.", error);
    isLoading.value = false;
  }
};

const items = ref([{ text: "" }]);
const add = () => {
  items.value.push({ text: "" });
  console.log(items.value);
};

const remove = (index) => {
  items.value.splice(index, 1);
};

const done = () => {
  friendMtti.value = "";
  if (A.value) {
    friendMtti.value += "A";
  } else {
    friendMtti.value += "R";
  }
  if (F.value) {
    friendMtti.value += "F";
  } else {
    friendMtti.value += "S";
  }
  if (I.value) {
    friendMtti.value += "I";
  } else {
    friendMtti.value += "W";
  }
  if (T.value) {
    friendMtti.value += "T";
  } else {
    friendMtti.value += "V";
  }
  getGPTResponse(3);
};

const checkList = () => {
  getGPTResponse(2);
  mode.value++;
};
const sendMtti = ref("");
const pushed = ref(false);
const pushed2 = ref(true);
const pushed3 = ref(true);
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
</script>

<template>
  <!-- <img src="../assets/female1.png" alt=""> -->

  <div class="whole-div" style="margin-top: 20px">
    <Loading :loading="isLoading" />

    <div class="left">
      <!-- 질문 말풍선 => 이거 누르면 답변 말풍선 나오게해야함 -->
      <div class="speech-bubble1" @click="mode == 0 ? checkList() : ''">
        <h2 style="padding: 10px; cursor: pointer;">{{ mtti }} 여행 체크리스트가 궁금해?</h2>
      </div>
      <!-- 답변 말풍선 -->
      <div class="speech-bubble2" v-if="mode > 0" style="padding: 10px;">
        <h1>Check List!</h1>
        <h3 style="padding: 5%">{{ result2 }}</h3>
      </div>
    </div>

    <div class="middle">
      <!-- 사용자 성별에 따라 이미지 남자여자 가져오기 -->
      <img
        :src="userInfo.gender === 'M' ? manImage : femaleImage"
        alt="Character"
        :class="{ isPushed: pushed }"
      />
      <img
        :src="userInfo.gender === 'M' ? manImage2 : femaleImage2"
        alt="Character"
        :class="{ isPushed: pushed2 }"
      />
      <img
        :src="userInfo.gender === 'M' ? manImage3 : femaleImage3"
        alt="Character"
        :class="{ isPushed: pushed3 }"
      />
    </div>

    <div class="right">
      <!-- 다른 MTTI 입력받을 말풍선 -->
      <template v-if="mode > 1">
        <div class="speech-bubble3" style="padding: 10px; margin-bottom: 20px;">
          <h2 >같이 여행가는 사람들의 MTTI를 입력해봐!</h2>
          <div class="container" v-if="result3.length == 0">
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
          </div>
          <div
            class="speech-bubble1"
            @click="done"
            style="cursor: pointer;"
          >
            <h4 style="text-decoration-line: underline;">우리 여행 괜찮을까?</h4>
          </div>
        </div>
        <template v-for="(value, index) in result3" :key="index" >
          <div class="speech-bubble4" style="padding: 10px; margin-bottom: 20px;">
            <h1>MTTI Match</h1>
            <h4 v-html="result3"></h4>
          </div>
          <div class="speech-bubble3" style="padding: 10px;margin-bottom: 20px;">
            <h2>또 다른 사람들의 MTTI도 입력해봐!</h2>
            <div class="container" v-if="index == result3.length - 1">
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
            </div>
            <div class="speech-bubble1" style="cursor: pointer; " @click="done">
              <h4 style="text-decoration-line: underline;">우리 여행 괜찮을까?</h4>
            </div>
          </div>
        </template>
      </template>
      <!-- 답변 말풍선-->
    </div>
  </div>
</template>

<style scoped>
.whole-div {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  height: 90vh;
}
.left {
  flex: 1;
  margin: 3%;
}
.middle {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}
.right {
  flex: 1;
}
.buttons {
  display: flex;
  justify-content: space-around;
}

.speech-bubble1 {
  position: relative;
  background: #c2c2c2;
  border-radius: 0.4em;
}

.speech-bubble1:after {
  content: "";
  position: absolute;
  right: 0;
  top: 50%;

  width: 0;
  height: 0;
  border: 13px solid transparent;
  border-left-color: #c2c2c2;
  border-right: 0;
  border-bottom: 0;
  margin-top: -6.5px;
  margin-right: -13px;
}

.speech-bubble2 {
  position: relative;
  background: #787878;
  border-radius: 0.4em;
}

.speech-bubble2:after {
  content: "";
  position: absolute;
  left: 0;
  top: 50%;
  width: 0;
  height: 0;
  border: 35px solid transparent;
  border-right-color: #787878;
  border-left: 0;
  border-top: 0;
  margin-top: -17.5px;
  margin-left: -35px;
}

.speech-bubble3 {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  position: relative;
  background: #c2c2c2;
  border-radius: 0.4em;
}

.speech-bubble3:after {
  content: "";
  position: absolute;
  left: 0;
  top: 50%;
  width: 0;
  height: 0;
  border: 35px solid transparent;
  border-right-color: #c2c2c2;
  border-left: 0;
  border-bottom: 0;
  margin-top: -17.5px;
  margin-left: -35px;
}

.speech-bubble4 {
  position: relative;
  background: #787878;
  border-radius: 0.4em;
}

.speech-bubble4:after {
  content: "";
  position: absolute;
  right: 0;
  top: 50%;
  width: 0;
  height: 0;
  border: 35px solid transparent;
  border-left-color: #787878;
  border-right: 0;
  border-top: 0;
  margin-top: -17.5px;
  margin-right: -35px;
}

.isPushed {
  display: none;
  visibility: hidden;
}
.container {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  width: 60%;
  height: 150px;
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
  background: rgb(175, 175, 255);
  color: white;
  font-size: 12px;
  font-weight: bold;
  text-align: center;
  border-radius: 10px;
  cursor: pointer;
  box-shadow: 3px 3px 3px 3px gray;
  transition: background 0.3s ease;
  height: 50px;
}
.mtti-btn-sel {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  color: white;
  font-size: 12px;
  font-weight: bold;
  text-align: center;
  border-radius: 10px;
  cursor: pointer;
  transition: background 0.3s ease;
  box-shadow: 3px 3px 3px 3px gray;
  height: 50px;
  background-color: rgb(130, 130, 255);
}
.mtti-btn:hover {
  background: rgb(130, 130, 255);
}
</style>
