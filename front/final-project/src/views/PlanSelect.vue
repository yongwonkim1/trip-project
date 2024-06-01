<script setup>
import { useRouter } from "vue-router";
import { onMounted, ref } from "vue";
import { usePlanStore } from "@/stores/plan";
import { useMemberStore } from "@/stores/member";
import { useFireBaseStore } from "@/stores/firebase";
import axios from "axios";
import Loading from "@/components/Loading.vue";
const firebaseStore = useFireBaseStore();
const memberStore = useMemberStore();
const planStore = usePlanStore();
const { setPlan, selPlan } = planStore;
const date = new Date();
const router = useRouter();
const isSelected = ref(false);
const isLoading = ref(false);
const endDate = (startDate, dateOffset) => {
  let date = new Date(startDate);
  date.setDate(startDate.getDate() + dateOffset);
  return date;
};
onMounted(() => {
  loadPlan();
});
const loadPlan = async () => {
  isLoading.value = true;
  try {
    const res = await axios.get(import.meta.env.VITE_BACKEND_ADDRESS + "/plan/search", {
      params: {
        id: memberStore.userInfo.userId,
      },
    });

    const data = res.data;
    console.log(data);

    const planPromises = data.map(async (item) => {
      const imageUrl = await firebaseStore.getImage(item.image);
      const profileImgUrl = await firebaseStore.getImage(item.profile_img);
      return {
        ...item,
        image: imageUrl,
        profile_img: profileImgUrl,
        startDate: new Date(item.startDate),
      };
    });
    isLoading.value = false;
    planList.value = await Promise.all(planPromises);

    console.log(planList.value);
  } catch (error) {
    console.error("Error loading plans:", error);
  }
};

const planList = ref([
  // {
  //   planId: 1,
  //   title: "~~여행",
  //   style: "맛집투어",
  //   image: "./src/assets/s3.jpg",
  //   startDate: new Date("2024-05-30"),
  //   dateOffset: 3,
  //   user_nickname: "ssafy",
  //   user_image: "./src/assets/123.jpg",
  // },
  // {
  //   planId: 2,
  //   title: "~~여행2",
  //   style: "맛집투어2",
  //   image: "./src/assets/s2.jpg",
  //   startDate: new Date("2024-05-30"),
  //   dateOffset: 6,
  //   user_nickname: "ssafy",
  //   user_image: "./src/assets/789.png",
  // },
]);
onMounted(() => {
  selPlan.pid = 0;
  selPlan.day = 0;
});
const selected = ref(false);
const selectedPlanIndex = ref(null);
const select = (index) => {
  selectedPlanIndex.value = index;
  setTimeout(function () {
    selected.value = true;
  }, 50);
};
const addPlan = () => {
  router.push("/planAdd");
};
const back = () => {
  selPlan.pid = 0;
  selectedPlanIndex.value = null;
  selected.value = false;
};
const planView = (id, date_, title, start, dateOffset, style, image, user_nickname, user_image) => {
  setPlan(id, date_, title, start, dateOffset, style, image, user_nickname, user_image);
  console.log(selPlan);
  isSelected.value = true;
  // router.push({
  //   name: "plan",
  //   query: {
  //     pid: String(id),
  //     day: String(index),
  //   },
  // });
};
const link = (to) => {
  router.push(`/${to}`);
};
</script>

<template>
  <div class="container">
    <Loading :loading="isLoading" />
    <div class="contents">
      <h2
        :style="{
          display: selectedPlanIndex === null ? 'block' : 'none',
        }"
      >
        일정을 선택해주세요
      </h2>
      <div
        @click="back"
        :style="{
          display: selectedPlanIndex === null ? 'none' : 'block',
        }"
        style="cursor: pointer"
      >
        <img width="50px;" src="@/assets/back.png" alt="" />
      </div>
      <div class="plan-eles">
        <div
          class="card"
          :class="['card', { 'no-hover': selected }]"
          :style="{
            display: selectedPlanIndex === null || selectedPlanIndex === index ? 'block' : 'none',
          }"
          @click="select(index)"
          v-for="(plan, index) in planList"
          :key="index"
        >
          <div class="card-header">
            <img :src="plan?.image" alt="" />
          </div>
          <div class="card-body">
            <span class="tag tag-teal">{{ plan.style }}</span>
            <h4>{{ plan.title }}</h4>
            <p>
              {{ plan.startDate.toLocaleDateString() }} ~
              {{ endDate(plan.startDate, plan.Duration).toLocaleDateString() }}
            </p>
            <div class="user">
              <img :src="plan.profile_img" />
              <div class="user-info">
                <h5>{{ plan.nickname }}</h5>
                <small></small>
              </div>
            </div>
          </div>
          <div
            class="day-select"
            :style="{
              display: selected === true ? 'block' : 'none',
            }"
          >
            <div class="day-btn-con" v-for="index in plan.Duration" :key="index">
              <div
                class="day-btn"
                :style="{ backgroundColor: index == selPlan.day ? 'rgb(175, 175, 255)' : '' }"
                @click="
                  planView(
                    plan.pid,
                    index,
                    plan.title,
                    plan.startDate,
                    plan.Duration,
                    plan.style,
                    plan.image,
                    plan.nickname,
                    plan.profile_img
                  )
                "
              >
                {{ index }}일차
              </div>
            </div>
          </div>
        </div>

        <div
          :style="{
            display: selectedPlanIndex === null || selectedPlanIndex === index ? 'flex' : 'none',
          }"
          class="card-add"
          @click="addPlan()"
          v-if="!isLoading"
        >
          <div class="card-add-header">
            <img src="@/assets/newPlan.png" alt="" />
          </div>
          <div class="card-body">
            <h2>일정 추가</h2>
          </div>
        </div>
      </div>
      <div class="link" v-if="selPlan.pid != 0">
        <button class="styled-button" @click="link('plan')">일정 짜기</button>
        <button class="styled-button" @click="link('planView')">일정 보기</button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.container {
  margin-top: 50px;
  display: flex;
  justify-content: center;
}
.contents {
  width: 90%;
  height: 800px;
}

.plan-eles {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  height: 700px;
  align-items: center;
  transition: transform 0.5s ease, opacity 0.5s ease;
}
.plan-ele {
  display: block;
}
.plan-ele-con {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 250px;
  height: 250px;
  padding-top: 10px;
  border: 1px solid black;
  border-radius: 10px;
  margin: 2%;
}
.plan-ele-img {
  width: 100px;
  height: 100px;
}

.plan-ele.selected {
  transform: scale(1.2);
  opacity: 1;
}
.day-btn-con {
  padding: 3%;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
}
.day-btn {
  width: 80%;
  border-radius: 10px;
  height: 40px;
  cursor: pointer;
  vertical-align: middle;
  line-height: 40px;
}
.day-btn:focus {
  background-color: rgb(97, 97, 252);
}
.day-btn:hover {
  background-color: rgb(175, 175, 255);
}
.day-select {
  animation: fadeInUp 0.8s;
  overflow-y: scroll;
  overflow-x: hidden;
  max-height: 150px;
}
.day-select::-webkit-scrollbar {
  width: 10px; /* Width of the scrollbar */
}

.day-select::-webkit-scrollbar-thumb {
  background-color: #888; /* Color of the scrollbar thumb */
  border-radius: 5px; /* Roundness of the scrollbar thumb */
}

.day-select::-webkit-scrollbar-thumb:hover {
  background-color: #555; /* Color of the scrollbar thumb on hover */
}

.day-select::-webkit-scrollbar-track {
  background-color: #f0f0f0; /* Background color of the scrollbar track */
  border-radius: 5px; /* Roundness of the scrollbar track */
}
@keyframes fadeInUp {
  0% {
    opacity: 0;
    transform: translate3d(0, 20%, 0);
  }
  to {
    opacity: 1;
    transform: translateZ(0);
  }
}
.card {
  background-color: #fff;
  border-radius: 10px;
  box-shadow: 0 2px 20px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  width: 300px;
  transition: transform 0.3s ease;
  margin: 3%;
}
.card-add {
  border-radius: 10px;
  box-shadow: 0 2px 20px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  background-color: white;
  transition: transform 0.3s ease;
  width: 300px;
  margin: 3%;
  height: 403px;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.card-add-header img {
  margin-top: 30%;
  width: 100%;
  height: 200px;
  object-fit: cover;
}
.card:hover {
  transform: translateY(-10px);
}
.card-add:hover {
  transform: translateY(-10px);
}
.no-hover:hover {
  transform: translateY(0px);
}

.card-header img {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.card-body {
  display: flex;
  flex-direction: column;
  align-items: start;
  padding: 20px;
  min-height: 150px;
}
.tag {
  background-color: #ccc;
  color: #fff;
  border-radius: 50px;
  font-size: 12px;
  margin: 0;
  padding: 2px 10px;
  text-transform: uppercase;
}
.tag-teal {
  background-color: #92d4e4;
}
.tag-purple {
  background-color: #3d1d94;
}
.tag-pink {
  background-color: #c62bcd;
}
.card-body h4 {
  margin: 10px 0;
}
.card-body p {
  font-size: 14px;
  margin: 0 0 40px 0;
  font-weight: 500;
  color: rgb(70, 68, 68);
}
.user {
  display: flex;
  margin-top: auto;
}
.user img {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin-right: 10px;
  object-fit: cover;
}
.user-info h5 {
  margin: 0;
}
@media (max-width: 940px) {
  .container {
    grid-template-columns: 1fr;
    justify-items: center;
  }
}
.link {
  display: flex;
  justify-content: center;
  gap: 10px;
}

.styled-button {
  background-color: rgb(175, 175, 255);
  color: white;
  border: none;
  border-radius: 5px;
  padding: 10px 20px;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.styled-button:hover {
  background-color: rgb(130, 130, 255);
}

.styled-button:focus {
  outline: none;
  box-shadow: 0 0 0 3px rgba(0, 123, 255, 0.5);
}
</style>
