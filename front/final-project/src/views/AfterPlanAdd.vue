<script setup>
import { onMounted, ref } from "vue";
import OpenAI from "openai";
import Loading from "@/components/Loading.vue";
import { useMemberStore } from "@/stores/member";
import { useRoute } from "vue-router";
import "@fortawesome/fontawesome-free/css/all.css";
import "@fortawesome/fontawesome-free/js/all.js";
import { useRouter } from "vue-router";
const router = useRouter()
const memberStore = useMemberStore();
const { userInfo } = memberStore;
const newPlan = ref({});
onMounted(() => {
	newPlan.value.title = route.query.title;
	newPlan.value.style = route.query.style;
	newPlan.value.image = route.query.image;
	newPlan.value.startDate = route.query.startDate;
	newPlan.value.duration = route.query.duration;
	newPlan.value.user_id = route.query.user_id;
	console.log(newPlan.value);
	console.log(newPlan.value.title);
	console.log(userInfo.mtti);
});
const route = useRoute();

const prefix = ref(
	"안녕하세요! MTTI(My Travel Type Indicator)는 여행 스타일을 알아보기 위한 시스템이에요. 이 시스템은 MBTI라는 성격 유형 지표처럼 여행하는 방식을 분류합니다. MTTI에서는 활동(A) vs 휴식(R), 익숙함(F) vs 낯섬(S), 여행 계획(I) vs 자유로움(W), 그리고 체험(T) vs 시각(V)이라는 네 가지 기준을 사용해요. 이 네 가지 기준을 조합하면 총 16가지 여행 타입이 나옵니다.\n활동(A) vs 휴식(R): 이 기준은 여행하는 동안 즐기는 활동의 양을 나타냅니다. 활동(A)형은 즐거운 활동을 찾고 움직이는 것을 즐기는 사람들이에요. 반면에 휴식(R)형은 여유롭게 휴식을 취하고 자연을 즐기는 것을 선호하는 사람들이죠.\n익숙함(F) vs 낯섬(S): 이 기준은 여행지를 선택할 때 익숙한 곳을 선호하는지, 아니면 새로운 곳을 탐험하는 것을 선호하는지를 나타냅니다. 익숙함(F)형은 익숙한 곳에서 편안함을 느끼고, 낯섬(S)형은 새로운 경험을 즐기며 모험을 찾아 다니는 편이에요.\n여행 계획(I) vs 자유로움(W): 이 기준은 여행을 계획하고 준비하는 것을 좋아하는지, 아니면 그때그때 즉흥적으로 여행하는 것을 선호하는지를 말해요. 여행 계획(I)형은 여행 일정을 세우고 준비하는 것을 좋아하고, 자유로움(W)형은 자유롭게 행동하며 즉흥적으로 여행하는 것을 선호하는 경향이 있어요.\n체험(T) vs 시각(V): 이 기준은 여행을 통해 새로운 경험을 즐기는 것과 아름다운 풍경을 감상하는 것 사이에서 어떤 것을 선호하는지를 나타냅니다. 체험(T)형은 다양한 활동을 통해 새로운 경험을 즐기는 것을 좋아하고, 시각(V)형은 아름다운 풍경이나 문화적인 경관을 감상하는 것을 좋아하는 경향이 있어요."
);

const prompt1 = () => {
	return `MTTI(My Travel Type Indicator)를 통해 여행 스타일을 분류하고 여행 팁과 추천 코스를 받아보고 싶어요. 제 여행 타입(${userInfo.mtti})과 제가 이번에 계획하고자 하는 여행컨셉(${newPlan.value.style})에 따라 어떤 활동을 추천하는지 알려주세요.  여행날짜(${newPlan.value.startDate})의 전국적인 날씨 따라 꿀팁이나 필수템, 추천하는 제철 음식이나 지역축제 혹은 해당 날짜의 날씨나 현재 상황이 좋지 않은(현시각 기준 자연재해와 같은 이유로) 지역도 알려주세요. 날씨 정보와 자연재해 정보의 경우 브라우저 제공이 아닌 바로 확인가능하게 알려주면 좋겠습니다.너무 길진않게 1000자 내외로 알려주면 감사하겠습니다!`;
};

const result = ref("");
const prompt = ref("");
const isLoading = ref(false);

const getGPTResponse = async () => {
	pushed.value = true;
	isLoading.value = true;
	try {
		const openai = new OpenAI({
			apiKey: import.meta.env.VITE_OPENAI_API_KEY,
			dangerouslyAllowBrowser: true,
		});
		prompt.value = prefix.value + prompt1();
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
const link = ()=>{
  router.push("plan")
}
const pushed = ref(false);
</script>

<!-- <template>
  <div style="margin-top: 20px">
    <Loading :loading="isLoading" />
    <div>{{ mtti }} 여행 체크리스트</div>
    <div>{{ result }}</div>
    <button @click="getGPTResponse">완료</button>
  </div>
</template>

<style scoped>
button {
  padding: 10px 20px;
  font-size: 16px;
  cursor: pointer;
}
</style> -->

<template>
	<div>
    <Loading :loading="isLoading"/>
		<div class="hr-div">
			<b class="hr"></b>
		</div>
		<div class="container">
			<button class="learn-more" @click="getGPTResponse">
				{{ newPlan.title }}<br />
				{{ userInfo.mtti }} - {{ newPlan.style }}
			</button>
			<Loading v-if="isLoading" />
			<div v-if="result" class="recommendation">
				<blockquote>
					<p>{{ result }}</p>
					<cite>OpenAI GPT</cite>
				</blockquote>
			</div>
		</div>
		<div class="hr-div">
			<b class="hr"></b>
		</div>
		<p :class="{ isPushed: pushed }" style="color: gray; margin-top: -5px">
			버튼을 누르면 꿀팁을 알 수 있어요!
		</p>
		<div id="holder" :class="{ isPushed: !pushed }">
			<div class="buttonGo" @click="link()">
				<p class="btnText">READY?</p>
				<div class="btnTwo">
					<p class="btnText2">GO!</p>
				</div>
			</div>
		</div>
	</div>
</template>

<style scoped>
.container {
	/* display: flex; */
	justify-content: center;
	align-items: center;
	max-width: 800px;
	margin: 0 auto;
	padding: 20px;
	font-family: "Arial", sans-serif;
	color: #333;
}

h1 {
	font-size: 2rem;
	color: rgb(130, 130, 255);
	text-align: center;
	margin-bottom: 20px;
}

p {
	font-size: 1.2rem;
	margin-bottom: 10px;
}

.recommendation {
	margin-top: 50px;
}

blockquote {
	position: relative;
	border: 1px solid rgb(130, 130, 255);
	border-radius: 10px;
	padding: 20px;
	background: #f9f9f9;
}

blockquote:before {
	position: absolute;
	font-family: "FontAwesome";
	content: "\f10d";
	font-size: 20px;
	color: rgb(175, 175, 255);
	top: 10px;
	left: 10px;
}

blockquote:after {
	position: absolute;
	font-family: "FontAwesome";
	content: "\f10e";
	font-size: 20px;
	color: rgb(175, 175, 255);
	right: 10px;
	bottom: 10px;
}

blockquote cite {
	display: block;
	font-size: 0.8rem;
	text-align: right;
	color: #d4ded7;
	padding-right: 20px;
}

/* @import url("https://fonts.googleapis.com/css?family=Rubik:700&display=swap"); */

button {
	position: relative;
	display: inline-block;
	cursor: pointer;
	outline: none;
	border: 0;
	vertical-align: middle;
	text-decoration: none;
	font-size: inherit;
	font-family: inherit;
}

button.learn-more {
	font-weight: 600;
	color: #382b22;
	text-transform: uppercase;
	padding: 1.25em 2em;
	background: #e7e2ff;
	border: 2px solid #9f85b1;
	border-radius: 0.75em;
	transform-style: preserve-3d;
	transition: transform 150ms cubic-bezier(0, 0, 0.58, 1),
		background 150ms cubic-bezier(0, 0, 0.58, 1);
}

button.learn-more::before {
	position: absolute;
	content: "";
	width: 100%;
	height: 100%;
	top: 0;
	left: 0;
	right: 0;
	bottom: 0;
	background: #ccbaff;
	border-radius: inherit;
	box-shadow: 0 0 0 2px #9f85b1, 0 0.625em 0 0 #eee2ff;
	transform: translate3d(0, 0.75em, -1em);
	transition: transform 150ms cubic-bezier(0, 0, 0.58, 1),
		box-shadow 150ms cubic-bezier(0, 0, 0.58, 1);
}

button.learn-more:hover {
	background: #e4cffd;
	transform: translate(0, 0.25em);
}

button.learn-more:hover::before {
	box-shadow: 0 0 0 2px #9f85b1, 0 0.5em 0 0 #eee2ff;
	transform: translate3d(0, 0.5em, -1em);
}

button.learn-more:active {
	background: #e4cffd;
	transform: translate(0em, 0.75em);
}

button.learn-more:active::before {
	box-shadow: 0 0 0 2px #9f85b1, 0 0 #eee2ff;
	transform: translate3d(0, 0, -1em);
}
.hr-div {
	display: flex;
	align-items: center;
	justify-content: center;
	margin-left: 13%;
}
.hr {
	width: 80%;
	height: 3px;
	display: flex;
	align-items: center;
	position: relative;
	margin-bottom: 0em;
	padding: 2em 0;
}

.hr::after,
.hr::before {
	content: "";
	position: absolute;
	width: 80%;
	height: 1px;
	bottom: 50%;
	left: 0;
}

.hr::before {
	background: linear-gradient(
		90deg,
		#fff 0%,
		#fff 50%,
		transparent 50%,
		transparent 100%
	);
	background-size: 15px;
	background-position: center;
	z-index: 1;
}

.hr::after {
	transition: opacity 0.3s ease, animation 0.3s ease;
	background: linear-gradient(
		to right,
		#62efab 5%,
		#f2ea7d 15%,
		#f2ea7d 25%,
		#ff8797 35%,
		#ff8797 45%,
		#e1a4f4 55%,
		#e1a4f4 65%,
		#82fff4 75%,
		#82fff4 85%,
		#62efab 95%
	);
	background-size: 200%;
	background-position: 0%;
	animation: bar 15s linear infinite;
}

@keyframes bar {
	0% {
		background-position: 0%;
	}
	100% {
		background-position: 200%;
	}
}

.isPushed {
	display: none;
	visibility: hidden;
}

/* @import url(https://fonts.googleapis.com/css?family=Roboto:700); */

#holder{
  -moz-user-select: -moz-none;
  -khtml-user-select: none;
  -webkit-user-select: none;
  background: white;
  padding: 10px;
  width: 250px;
  margin: 5% auto;
  position: relative;
  top: 50%;
  transform: translateY(-65%);
  border-radius: 2px;
}
.buttonGo {
  background: rgb(175, 175, 255);
  margin : 20px auto;
  width : 200px;
  height : 50px;
  overflow: hidden;
  text-align : center;
  transition : .2s;
  cursor : pointer;
  border-radius: 3px;
}
.btnTwo {
  position : relative;
  width : 200px;
  height : 100px;
  margin-top: -100px;
  padding-top: 2px;
  background : rgb(130, 130, 255);
  left : -250px;
  transition : .3s;
}
.btnText {
  color : white;
  transition : .3s;
}
.btnText2 {
  margin-top : 63px;
  margin-right : -130px;
  color : #FFF;
}
.buttonGo:hover .btnTwo{ /*When hovering over .button change .btnTwo*/
  left: -130px;
}
.buttonGo:hover .btnText{ /*When hovering over .button change .btnText*/
  margin-left : 65px;
}
</style>
