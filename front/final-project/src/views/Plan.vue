<script setup>
import { onMounted, ref, watch } from "vue";
import { useRoute, useRouter } from "vue-router";
import JjimItem from "@/components/JjimItem.vue";
import { KakaoMap, KakaoMapMarker } from "vue3-kakao-maps";
import StarRating from "@/components/StarRating.vue";
import { storeToRefs } from "pinia";
import { useSdStore } from "@/stores/searchData";
import { useMemberStore } from "@/stores/member";
import { usePlanStore } from "@/stores/plan";
import { useSampleImgStore } from "@/stores/sampleImg";
import axios from "axios";
const sampleImgStore = useSampleImgStore();
const planStore = usePlanStore();
const { selPlan } = storeToRefs(planStore);
const memberStore = useMemberStore();
const sdStore = useSdStore();
const route = useRoute();
const router = useRouter();
const { search_opt, sido } = storeToRefs(sdStore);
const { type } = sdStore;
const planList = ref([]);
const map = ref(null);
const markers = ref([]);

const onLoadKakaoMap = (mapRef) => {
  setTimeout(() => {
    map.value = mapRef;
  }, 100);
};
const coordinate = {
  lat: 37.566826,
  lng: 126.9786567,
};
const jjimRes = ref([]);
const isInPlanList = (attraction) => {
  console.log(planList.value);
  for (var i = 0; i < planList.value.length; i++) {
    if (attraction.aid == planList.value[i].aid) {
      return true;
    }
  }
  return false;
};
watch(
  planList,
  () => {
    viewMarker();
  },
  { deep: true }
);
// onMounted(() => {
//   setTimeout(() => {
//     loadPlan();
//     // viewMarker();
//   }, 100);
// });

const isSearched1 = ref(false);
const isSearched2 = ref(false);
const search = () => {
  axios
    .get(import.meta.env.VITE_BACKEND_ADDRESS + "/jjim/list/plan", {
      params: {
        region: search_opt.value.sido,
        user_id: memberStore.userInfo.userId,
        typeNumbers: Array.isArray(search_opt.value.contentTypeId)
          ? search_opt.value.contentTypeId.join(",")
          : search_opt.value.contentTypeId,
      },
      headers: {
        "Content-Type": "application/json;charset=utf-8",
      },
    })
    .then((res) => {
      var data = res.data;
      jjimRes.value = [];
      for (var i = 0; i < data.length; i++) {
        jjimRes.value.push(data[i]);
      }
    })
    .catch((error) => {
      console.error(error);
    });

  isSearched1.value = true;
  setTimeout(function () {
    isSearched2.value = true;
  }, 200);
};

const back = () => {
  isSearched1.value = false;
  setTimeout(function () {
    isSearched2.value = false;
  }, 200);
};
const addPlan = (attraction) => {
  planList.value.push(attraction);
  viewMarker();
};
const delPlan = (attraction) => {
  for (var i = 0; i < planList.value.length; i++) {
    if (attraction.aid == planList.value[i].aid) {
      planList.value.splice(i, 1);
      setTimeout(() => {
        viewMarker();
      }, 100);
    }
  }
};
const movePlanOrder = () => {
  if (planList.value.length > 0) {
    router.push({ path: "planOrder", state: { planList: JSON.stringify(planList.value) } });
  } else {
    alert("먼저 일정을 추가해주세요");
  }
};
const editDay = (index) => {
  selPlan.value.day = index;
  loadPlan();
};

const loadPlan = () => {
  console.log(selPlan.value.pid);
  console.log(selPlan.value.day);
  axios
    .get(import.meta.env.VITE_BACKEND_ADDRESS + "/plan/detail/search", {
      params: {
        pid: selPlan.value.pid,
        day: selPlan.value.day,
      },
    })
    .then((res) => {
      const data = res.data;
      console.log(data);
      planList.value = [];
      for (var i = 0; i < data.length; i++) {
        const plan = {
          addr1: data[i].addr1,
          addr2: data[i].addr2,
          aid: data[i].aid,
          content_id: data[i].content_id,
          content_type_id: data[i].content_type_id,
          first_image: data[i].first_image,
          first_image2: data[i].first_image2,
          gugun_code: data[i].gugun_code,
          gugun_name: data[i].gugun_name,
          jid: data[i].jid,
          latitude: data[i].latitude,
          longitude: data[i].longitude,
          mlevel: data[i].mlevel,
          rating: data[i].rating,
          readcount: data[i].readcount,
          sido_code: data[i].sido_code,
          sido_name: data[i].sido_name,
          tel: data[i].tel,
          title: data[i].title,
          user_id: data[i].user_id,
          zipcode: data[i].zipcode,
        };
        planList.value.push(plan);
      }
      console.log(planList.value);
    });
};

let bounds;
const removeMarkers = () => {
  markers.value.forEach((marker) => marker.setMap(null));
  markers.value.length = 0;
};
const viewMarker = () => {
  if (markers.value.length > 0) {
    removeMarkers();
  }
  bounds = new kakao.maps.LatLngBounds();
  let marker;
  let point;
  planList.value.forEach((attraction) => {
    point = new kakao.maps.LatLng(attraction.latitude, attraction.longitude);
    marker = new kakao.maps.Marker({ position: point });

    if (map.value !== undefined) {
      marker.setMap(map.value);
      kakao.maps.event.addListener(marker, "click", () => {
        aid.value = attraction.aid;
      });
      markers.value.push(marker);
    }
    bounds.extend(point);
  });
  if (planList.value.length > 0) {
    setBounds();
  }
};
const setBounds = () => {
  if (map.value !== undefined) {
    map.value.setBounds(bounds);
    // coordinate.lat = (bounds.pa + bounds.qa) / 2;
    // coordinate.lng = (bounds.ha + bounds.oa) / 2;
    const level = map.value.getLevel();
    map.value.setLevel(level + 2);
  }
};
</script>

<template>
  <div>
    <h1 style="margin-top: -1%">{{ selPlan.title }}</h1>
    <div>
      <button
        :class="['day-btn', { 'day-btn-sel': index == selPlan.day }]"
        @click="editDay(index)"
        v-for="index in selPlan.dateOffset"
        :key="index"
      >
        {{ index }}일차
      </button>
    </div>
    <div class="container">
      <KakaoMap
        width="90%"
        height="800px"
        :lat="coordinate.lat"
        :lng="coordinate.lng"
        @onLoadKakaoMap="onLoadKakaoMap"
      >
        <!-- <KakaoMapMarker :lat="coordinate.lat" :lng="coordinate.lng"></KakaoMapMarker> -->
      </KakaoMap>
      <div class="left">
        <div class="jjim-list">
          <!-- 검색-->
          <div
            :class="['search-box-con', { 'fade-out': isSearched1 }, { 'fade-in': !isSearched1 }]"
            :style="{
              display: isSearched2 ? 'none' : 'block',
            }"
          >
            <h2>찜 목록</h2>
            <div class="search-box">
              <form @submit.prevent="search">
                <select class="modal-select-box" v-model="search_opt.sido" required>
                  <option value="" disabled style="display: none" selected>지역</option>
                  <option
                    class="option"
                    v-for="(value, key, index) in sido"
                    :key="index"
                    :value="key"
                  >
                    {{ value }}
                  </option>
                </select>
                <div v-for="(label, key) in type" :key="key" class="checkbox-item">
                  <input
                    type="checkbox"
                    :id="key"
                    :value="key"
                    v-model="search_opt.contentTypeId"
                  />
                  <label :for="key">{{ label }}</label>
                </div>
                <p style="text-align: right; font-size: small; color: gray; width: 98%">
                  다중 선택 가능
                </p>
                <br />
                <br />
                <button type="submit" class="custom-button">찜 목록 가져오기</button>
              </form>
            </div>
          </div>
          <!-- 검색 끝-->
          <!-- 찜 리스트 출력-->
          <div
            :class="['list-con', { 'fade-out': !isSearched1 }, { 'fade-in': isSearched1 }]"
            :style="{
              display: !isSearched2 ? 'none' : 'flex',
            }"
          >
            <div class="list-btn-con">
              <button class="list-back-btn" @click="back()">&lt;</button>
              <h2>{{ sido[search_opt.sido] }}</h2>
              <div style="width: 10%"></div>
            </div>
            <div class="horizontal-list">
              <template v-if="jjimRes.length != 0">
                <div
                  class="card"
                  v-for="(attraction, index) in jjimRes"
                  :key="index"
                  @click="isInPlanList(attraction) ? delPlan(attraction) : addPlan(attraction)"
                  :class="{ 'added-to-plan': isInPlanList(attraction) }"
                >
                  <img
                    :src="
                      attraction.first_image != ''
                        ? attraction.first_image
                        : sampleImgStore.images[attraction.content_type_id]
                    "
                    :alt="attraction.title"
                  />
                  <div class="card-body">
                    <h5>{{ attraction.title }}</h5>
                    <StarRating
                      :rating="attraction.rating"
                      style="margin-bottom: -3%; margin-top: -15%"
                    />
                    <p style="margin-top: 20%">{{ attraction.gugun }}</p>
                  </div>
                </div>
              </template>
              <template v-else>
                <h2>찜을 추가해주세요</h2>
              </template>
            </div>
          </div>
        </div>
      </div>
      <div class="right">
        <div class="plan-list">
          <h2>Plan List</h2>
          <ol class="ordered-nav">
            <li class="ordered-nav--link" v-for="(attraction, index) in planList" :key="index">
              <a class="tx-link tx-link_no-underline" style="margin-top: -8px"
                >{{ attraction.title }}
              </a>
              <button class="del-btn" @click="delPlan(attraction)">X</button>
            </li>
          </ol>
          <button class="move-btn" @click="movePlanOrder">플랜 이동</button>
        </div>
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
.search-box-con {
  width: 90%;
  position: absolute;
}
.fade-in {
  animation: fadeIn 0.5s;
}

.fade-out {
  animation: fadeOut 0.2s;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translate3d(15%, 0, 0);
  }
  to {
    opacity: 1;
    transform: translateZ(0);
  }
}

@keyframes fadeOut {
  from {
    opacity: 1;
    transform: translateZ(0);
  }
  to {
    opacity: 0;
    transform: translate3d(-15%, 0, 0);
  }
}
.day-btn {
  background-color: rgb(175, 175, 255);
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;
  font-size: 16px;
  transition: background-color 0.3s;
  margin: 0.5%;
  margin-top: 0;
  margin-bottom: -2%;
}
.day-btn:hover {
  background-color: rgb(130, 130, 255);
}
.day-btn-sel {
  background-color: rgb(130, 130, 255);
}
.search-area {
  width: 85.5%;
  height: 800px;
}
.left {
  z-index: 3;
  position: absolute;
  left: 9.5%;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 18%;
  height: 800px;
}
.jjim-list {
  z-index: 3;
  background-color: white;
  width: 90%;
  height: 95%;
  border-radius: 30px;
  box-shadow: 4px 4px 4px 4px rgba(100, 100, 100, 0.6);
  overflow-x: hidden;
  overflow-y: scroll;
}
.jjim-list::-webkit-scrollbar {
  display: none;
}
.search-box {
  padding: 20px;
}

.modal-select-box {
  width: 100%;
  padding: 10px;
  margin-bottom: 20px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.checkbox-item {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.checkbox-item input[type="checkbox"] {
  display: none;
}

.checkbox-item label {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  background-color: #f0f0f0;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.checkbox-item input[type="checkbox"]:checked + label {
  background-color: #aaf;
  border-color: #00f;
}

.checkbox-item label:hover {
  background-color: #ddd;
}
.custom-button {
  padding: 15px 40px;
  font-size: 16px;
  font-weight: bold;
  color: #fff;
  background-color: #946ffa;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.3s ease;
}

.custom-button:hover {
  background-color: #0056b3;
}

.custom-button:active {
  transform: scale(0.95);
}

.custom-button:focus {
  outline: none;
  box-shadow: 0 0 0 2px rgba(0, 123, 255, 0.5);
}
.list-con {
  width: 90%;
  position: absolute;
  text-align: left;
  display: flex;
  align-items: center;
  flex-direction: column;
  justify-content: center;
}
.list-btn-con {
  display: flex;
  justify-content: space-between;
  margin-top: 5%;
  width: 90%;
}

.list-back-btn {
  cursor: pointer;
  background-color: white;
  border: none;
  font-size: x-large;
}
.horizontal-list {
  display: flex;
  flex-direction: column;
  overflow-x: hidden;
  overflow-y: hidden;
}
.horizontal-list::-webkit-scrollbar {
  display: none;
}

.card {
  display: flex;
  width: 90%;
  overflow: hidden;
  height: 110px;
  padding: 0;
  margin: 3% 5% 2% 5%;
  border-radius: 10px;
  box-shadow: 4px 4px 4px 1px rgb(165, 165, 165);
  transition: ease 0.5s;
}
.added-to-plan {
  background-color: gray;
  color: rgb(176, 176, 176);
}
.added-to-plan:hover {
  transform: translateX(0px);
}

.card:hover {
  transform: translateX(10px);
}

.card img {
  width: 40%;
  height: auto;
  border-radius: 10px;
}

.card-body {
  width: 50%;
  margin-top: -8%;
  padding: 10px;
}

.right {
  z-index: 2;
  position: absolute;
  right: 9.5%;
  display: flex;
  justify-content: center;
  width: 23%;
  height: 800px;
  background-color: black;
  border-radius: 10px;
}
.plan-list {
  width: 100%;
  height: 100%;
  border-radius: 10px;
  padding: 20px;
  overflow-y: auto;
  text-align: left;
  margin-left: 5%;
}

.plan-list h2 {
  margin-bottom: 20px;
  text-align: center;
  color: white;
}

.ordered-nav {
  position: relative;
  list-style: none;
  margin: 0;
  padding: 0;
  display: block;
}

.ordered-nav::before {
  content: "";
  position: absolute;
  z-index: -1;
  left: 6px;
  top: 8px;
  bottom: 16px;
  width: 3px;
  background-color: #27a4fc;
}

.ordered-nav--link {
  padding-left: 1.5rem;
  margin: 0.75rem 0;
  position: relative;
  display: flex;
  align-items: center;
}

.ordered-nav--link::before {
  z-index: 4;
  content: "";
  position: absolute;
  top: calc(50% - 6px);
  left: 0;
  width: 9px;
  height: 9px;
  border-color: #27a4fc;
  border-width: 3px;
  border-style: solid;
  border-radius: 50%;
}

.ordered-nav--link:first-child {
  margin-top: 0;
}

.ordered-nav--link:first-child::before {
  top: 2px;
}

.ordered-nav--link:last-child {
  margin-bottom: 0;
}

.ordered-nav--link__active .tx-link {
  color: white;
}

.ordered-nav--link__active::before {
  background-color: #27a4fc;
}

.tx-link {
  font-family: "Ubuntu", sans-serif;
  color: #a5e2fd;
  text-decoration: underline;
  text-underline-position: under;
}

.del-btn {
  background-color: transparent;
  border: none;
  color: red;
  font-size: large;
  cursor: pointer;
}
.move-btn {
  position: absolute;
  bottom: 50px;
  right: 50px;
  border-radius: 10px;
  /* border: 2px solid ; */
  background-color: white;
  width: 100px;
  height: 50px;
  cursor: pointer;
  transition: ease 0.5s;
}
.move-btn:hover {
  background-color: rgb(209, 207, 207);
}
</style>
