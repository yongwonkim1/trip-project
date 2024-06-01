<script setup>
import { onMounted, ref } from "vue";
import OpenAI from "openai";
import Loading from "@/components/Loading.vue";
import { useMemberStore } from "@/stores/member";
import { usePlanStore } from "@/stores/plan";

const planStore = usePlanStore();
const memberStore = useMemberStore();
const { userInfo } = memberStore;
const planList = ref(JSON.parse(history.state.planList));
const newPlan = ref({});
const mtti = ref("ASIT");
const friendsMtti = ref([""]);
const prefix = ref("안녕하세요! MTTI(My Travel Type Indicator)는 여행 스타일을 알아보기 위한 시스템이에요. 이 시스템은 MBTI라는 성격 유형 지표처럼 여행하는 방식을 분류합니다. MTTI에서는 활동(A) vs 휴식(R), 익숙함(F) vs 낯섬(S), 여행 계획(I) vs 자유로움(W), 그리고 체험(T) vs 시각(V)이라는 네 가지 기준을 사용해요. 이 네 가지 기준을 조합하면 총 16가지 여행 타입이 나옵니다.\n활동(A) vs 휴식(R): 이 기준은 여행하는 동안 즐기는 활동의 양을 나타냅니다. 활동(A)형은 즐거운 활동을 찾고 움직이는 것을 즐기는 사람들이에요. 반면에 휴식(R)형은 여유롭게 휴식을 취하고 자연을 즐기는 것을 선호하는 사람들이죠.\n익숙함(F) vs 낯섬(S): 이 기준은 여행지를 선택할 때 익숙한 곳을 선호하는지, 아니면 새로운 곳을 탐험하는 것을 선호하는지를 나타냅니다. 익숙함(F)형은 익숙한 곳에서 편안함을 느끼고, 낯섬(S)형은 새로운 경험을 즐기며 모험을 찾아 다니는 편이에요.\n여행 계획(I) vs 자유로움(W): 이 기준은 여행을 계획하고 준비하는 것을 좋아하는지, 아니면 그때그때 즉흥적으로 여행하는 것을 선호하는지를 말해요. 여행 계획(I)형은 여행 일정을 세우고 준비하는 것을 좋아하고, 자유로움(W)형은 자유롭게 행동하며 즉흥적으로 여행하는 것을 선호하는 경향이 있어요.\n체험(T) vs 시각(V): 이 기준은 여행을 통해 새로운 경험을 즐기는 것과 아름다운 풍경을 감상하는 것 사이에서 어떤 것을 선호하는지를 나타냅니다. 체험(T)형은 다양한 활동을 통해 새로운 경험을 즐기는 것을 좋아하고, 시각(V)형은 아름다운 풍경이나 문화적인 경관을 감상하는 것을 좋아하는 경향이 있어요.");

onMounted(() => {
  console.log(userInfo.value);
  console.log(planStore.selPlan);
  console.log(planList.value);
});

const prompt2 = () => {
  return `MTTI가 ${mtti.value}인 제가 여행 컨셉(${planStore.selPlan.style})의 ${planStore.selPlan.dateOffset}일 ${planList.value[0].sido_name} 여행에 따른 여행 가방 꾸리기 체크리스트를 만들어 주실 수 있을까요? 여행기간(${planStore.selPlan.dateOffset})과 여행지인 ${planList.value[0].sido_name}의 여행 날짜(${planStore.selPlan.start})의 날씨를 고려하여 체크리스트를 만들어주면 좋겠습니다.`;
};

const prompt3 = () => {
  let prompt = "안녕하세요! MTTI가 ";
  for (let i = 0; i < items.value.length; i++) {
    prompt += items.value[i].text + ", ";
    console.log(items.value[i].text);
  }
  return (
    prompt +
    "인 사람들끼리 여행을 갈 때 주의해야 할 점을 알려주세요. MTTI 16가지 유형 형식에 맞지 않는 MTTI 값은 무시해 주세요. 감사합니다!"
  );
};

const result = ref("");
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
    } else {
      prompt.value = prefix.value + prompt3();
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

    result.value = response.choices[0].message.content;
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
  console.log(11);
  getGPTResponse(3);
};

const checkList = () => {
  getGPTResponse(2);
};

const test = () => {
  console.log(planList.value);
};
</script>

<template>
  <div style="margin-top: 20px">
    <Loading :loading="isLoading" />
    
    <div class="left">
      <!-- 질문 말풍선 => 이거 누르면 답변 말풍선 나오게해야함 -->
      <div class="speech-bubble1" @click="checkList">
        <h2>{{ mtti }} 여행 체크리스트가 궁금해?</h2>
      </div>
      <!-- 답변 말풍선 -->
      <div class="speech-bubble2">
        <h1>Check List!</h1>
        <h2>{{ result }}</h2>
      </div>
    </div>

    <div class="middle">
      <!-- 사용자 성별에 따라 이미지 남자여자 가져오기 -->
      <img :src="userInfo.value.gender === 'male' ? '@/assets/남자1.png' : '@/assets/여자1.png'" alt="User Image" />
    </div>
    
    <div class="right">
      <!-- 다른 MTTI 입력받을 말풍선 -->
      <div class="speech-bubble3">
        <h2>같이 여행가는 사람들의 MTTI를 입력해봐!</h2>
        <div v-for="(item, index) in items" :key="index">
          <input type="text" v-model="item.text" />
          <button @click="remove(index)">Remove</button>
        </div>
      </div>
      <!-- 답변 말풍선-->
      <div class="speech-bubble4">
        <h1>MTTI Match</h1>
        <h2>{{ result }}</h2>
      </div>
    </div>

    <!-- 새로운 div 추가 버튼 -->
    <button @click="add">Add Div</button>
    <button @click="done">완료</button>
    <button @click="test">데이터 확인</button>
  </div>
</template>

<style scoped>
button {
  padding: 10px 20px;
  font-size: 16px;
  cursor: pointer;
}
.speech-bubble1 {
  position: relative;
  background: #c2c2c2;
  border-radius: .4em;
}

.speech-bubble1:after {
  content: '';
  position: absolute;
  right: 0;
  top: 50%;
  width: 0;
  height: 0;
  border: 35px solid transparent;
  border-left-color: #c2c2c2;
  border-right: 0;
  border-bottom: 0;
  margin-top: -17.5px;
  margin-right: -35px;
}

.speech-bubble2 {
  position: relative;
  background: #787878;
  border-radius: .4em;
}

.speech-bubble2:after {
  content: '';
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
  position: relative;
  background: #c2c2c2;
  border-radius: .4em;
}

.speech-bubble3:after {
  content: '';
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
  border-radius: .4em;
}

.speech-bubble4:after {
  content: '';
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
</style>
