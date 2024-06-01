<script setup>
import { onMounted, ref, watch } from "vue";
import { usePlanStore } from "@/stores/plan";
import axios from "axios";
import { storeToRefs } from "pinia";
import { useSampleImgStore } from "@/stores/sampleImg";
import StarRating from "@/components/StarRating.vue";
const sampleImgStore = useSampleImgStore();
const planStore = usePlanStore();
const { selPlan } = storeToRefs(planStore);
const planList = ref([]);
const days = ref([]);
const planLists = ref([]);
onMounted(() => {
  console.log(selPlan.value);
  for (let i = 0; i < selPlan.value.dateOffset; i++) {
    days.value.push({ active: false });
  }
  setTimeout(() => {
    loadPlan();
    console.log(planLists.value);
  }, 100);
});

const loadPlan = async () => {
  for (let i = 1; i < selPlan.value.dateOffset + 1; i++) {
    await axios
      .get(import.meta.env.VITE_BACKEND_ADDRESS + "/plan/detail/search", {
        params: {
          pid: selPlan.value.pid,
          day: i,
        },
      })
      .then((res) => {
        const data = res.data;
        planList.value = [];
        data.forEach((item) => {
          planList.value.push(item);
        });
      });
    planLists.value.push(planList.value);
  }
};

const cur = ref(0);
const current = ref(0);

const updateContent = (idx, idx2) => {
  cur.value = idx;
  current.value = idx2;
  console.log(planList.value);
};

const toggleEditMode = () => {
  this.editMode = !this.editMode;
};
const toggleDay = (day) => {
  console.log(planLists.value);
  console.log(selPlan.value);
  day.active = !day.active;
};
function addDays(date, days) {
  const clone = new Date(date);
  clone.setDate(date.getDate() + days);
  return clone;
}
</script>

<template>
  <div class="container">
    <div class="left">
      <div class="day-list">
        <div
          v-for="(day, index) in days"
          :key="index"
          class="day-item"
          :class="{ active: day.active }"
          @click="toggleDay(day)"
        >
          <div class="day-con">
            <div class="day-header">
              <span class="day-title">Day {{ index + 1 }}</span>
              <span class="day-date">{{ addDays(selPlan.start, index).toLocaleDateString() }}</span>
              <span class="arrow">&#9662;</span>
            </div>
            <div class="day-content">
              <div
                class="activity"
                v-for="(attraction, aIndex) in planLists[index]"
                :key="aIndex"
                @click.stop="updateContent(index, aIndex)"
              >
                {{ attraction.title }}
              </div>
            </div>
          </div>
        </div>
        <!-- Repeat for other days -->
      </div>
      <!-- <button
        v-for="index in selPlan.dateOffset"
        :key="index"
        @click="editDay(index)"
        :class="['day-btn', { 'day-btn-sel': index == selPlan.day }]"
      >
        {{ index }}일차
      </button> -->
    </div>

    <nav class="horizontal-nav">
      <div class="content-area" v-if="planLists[cur] != null">
        <div class="img-con">
          <img
            width="250px"
            height="250px"
            style="object-fit: cover"
            v-lazy="
              planLists[cur][current]?.first_image == ''
                ? sampleImgStore?.images[planLists[cur][current]?.content_type_id]
                : planLists[cur][current]?.first_image
            "
            alt=""
          />
        </div>
        <div class="content-con">
          <h1>{{ planLists[cur][current]?.title }}</h1>
          <StarRating :rating="planLists[cur][current]?.rating" />
          <p>{{ planLists[cur][current]?.addr1 }}</p>
          <p class="con-text">
            {{ planLists[cur][current]?.overview }}
          </p>
        </div>
      </div>
      <!-- <div class="content-area">
        <h1>{{ planLists[cur][current]?.title }}</h1>
        <StarRating :rating="planLists[cur][current]?.rating" />
        <p>{{ planLists[cur][current]?.addr1 }}</p>
        <img
          width="450px"
          height="300px"
          style="object-fit: cover"
          v-lazy="
            planLists[cur][current]?.first_image == ''
              ? sampleImgStore?.images[planLists[cur][current]?.content_type_id]
              : planLists[cur][current]?.first_image
          "
          alt=""
        />
        <p style="margin: 3%">{{ planLists[cur][current]?.overview }}</p>
      </div> -->
    </nav>
  </div>
</template>

<style scoped>
.container {
  display: flex;
  align-items: flex-start;
  justify-content: center;
  height: 100vh;
  font-family: "Ubuntu", sans-serif;
  box-sizing: border-box;
}

.left {
  display: flex;
  flex: 1.5;
  height: 100vh;
  flex-direction: column;
  justify-content: center;
  align-items: flex-end;
  /* background-color: #27a4fc; */
}
.day-list {
  width: 100%;
  margin: 0;
  padding: 0;
  list-style: none;
}
.day-date {
  font-size: small;
}
.day-item {
  margin: 5%;
  border-radius: 20px;
}
.day-con {
  background-color: #f2ebff;
  border-radius: 10px;
}
.day-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px;
  cursor: pointer;
}

.day-title {
  font-weight: bold;
}

.day-content {
  display: none;
  flex-direction: column;
}

.activity {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 35px;
  border-top: 1.5px solid rgb(184, 183, 183);
  transition: background-color ease 0.5s;
  margin: 0;
  padding: 0;
  cursor: pointer;
}
.activity:hover {
  background-color: #c2a0ff;
}

.day-item.active .day-content {
  display: flex;
}

.arrow {
  transition: transform 0.3s;
}

.day-item.active .arrow {
  transform: rotate(180deg);
}

.horizontal-line {
  border-top: 2px solid black;
  width: 100%;
}
.nav-wrapper {
  display: flex;
  justify-content: center;
  align-items: start;
}

.ordered-nav {
  display: flex;
  justify-content: start;
  align-items: start;
  list-style: none;
  padding: 0;
  margin: 0;
}

.ordered-nav--link {
  display: flex;
  align-items: start;
  justify-content: start;
  position: relative;
  padding: 10px 20px;
  margin: 0 10px;
  cursor: pointer;
  transition: color 0.3s;
}

.ordered-nav--link h2 {
  font-size: 1.2rem;
  margin: 0;
}

.ordered-nav--link::before {
  content: "";
  position: absolute;
  top: 50%;
  left: 50%;
  width: 9px;
  height: 9px;
  background-color: #27a4fc;
  border: 3px solid black;
  border-radius: 50%;
  transform: translate(-50%, -50%);
}

.ordered-nav--link__active::before {
  background-color: white;
}
.horizontal-nav {
  flex: 8;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100vh;
}
.content-area {
  display: flex;
  justify-content: left;
  align-items: center;
  padding: 20px;
  height: 260px;
  background-color: rgb(231, 230, 230);
  border-radius: 10px;
  text-align: center;
  width: 90%;
}
.img-con {
  flex: 1;
  margin: 0;
  padding: 0;
}
.content-con {
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: start;
  margin-left: 3%;
  flex: 3;
  height: 100%;
}
.con-text {
  margin: 1%;
  font-size: smaller;
  overflow-y: scroll;
  overflow-x: hidden;
}

/* 스크롤바 전체 */
.con-text::-webkit-scrollbar {
  width: 12px;
  height: 12px;
}

/* 스크롤바 트랙 */
.con-text::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 10px;
}

/* 스크롤바 핸들 */
.con-text::-webkit-scrollbar-thumb {
  background: #888;
  border-radius: 10px;
}

/* 스크롤바 핸들 호버 시 */
.con-text::-webkit-scrollbar-thumb:hover {
  background: #555;
}
</style>
