<script setup>
import { ref, onMounted, watch } from "vue";
import { useRouter } from "vue-router";
import "vue3-carousel/dist/carousel.css";
import { Carousel, Slide, Pagination, Navigation } from "vue3-carousel";
import axios from "axios";
import { useMemberStore } from "@/stores/member";
import { useSampleImgStore } from "@/stores/sampleImg";
const sampleImgStore = useSampleImgStore();
const mttiNotification = ref("none");
const index = ref(1);
const memberStore = useMemberStore();
const router = useRouter();
const images = ref([
  "./src/assets/s1.jpg",
  "./src/assets/s2.jpg",
  "./src/assets/s3.jpg",
  // "./src/assets/s4.jpg",
  "./src/assets/s5.jpg",
  "./src/assets/s6.jpg",
]);
onMounted(() => {
  setTimeout(() => {
    if (
      memberStore.userInfo != null &&
      (memberStore.userInfo.mtti == null || memberStore.userInfo.mtti == "")
    ) {
      mttiNotification.value = "flex";
      setTimeout(() => {
        mttiNotification.value = "none";
      }, 5000);
    }
  }, 300);
});
onMounted(() => {
  setTimeout(() => {
    getAll();
  }, 100);
});
watch(index, (val) => {
  if (val == 1) {
    getAll();
  } else if (val == 2) {
    getByReview();
  } else {
    getByJjim();
  }
});
const getAll = () => {
  axios
    .get(import.meta.env.VITE_BACKEND_ADDRESS + "/attraction/recommend", {
      params: {
        userId: memberStore.userInfo == null ? "" : memberStore.userInfo.userId,
      },
    })
    .then((res) => {
      var data = res.data;
      console.log(data);
      attractionList.value = [];
      for (let i = 0; i < 12; i++) {
        attractionList.value.push(data[i]);
      }
    });
};
const getByReview = () => {
  axios.get(import.meta.env.VITE_BACKEND_ADDRESS + "/attraction/highRating").then((res) => {
    var data = res.data;
    attractionList.value = [];
    for (let i = 0; i < 12; i++) {
      attractionList.value.push(data[i]);
    }
  });
};
const getByJjim = () => {
  axios.get(import.meta.env.VITE_BACKEND_ADDRESS + "/attraction/manyJjim").then((res) => {
    var data = res.data;
    attractionList.value = [];
    for (let i = 0; i < 12; i++) {
      attractionList.value.push(data[i]);
    }
  });
};
const attractionList = ref([
  // {
  //   aid: 125266,
  //   title: "청태산 자연 휴양림",
  //   image: "http://tong.visitkorea.or.kr/cms/resource/21/2657021_image2_1.jpg",
  //   content:
  //     "해발 1,200m의 청태산을 주봉으로 하여 인공림과 천연림이 잘 조화된 울창한 산림을 바탕으로 한 국유림 경영 시범단지로서 숲속에는 온갖 야생 동식물이 고루 서식하고 있어 자연박물관을 찾은 기분을 느낄 수 있다. 영동고속도로 신갈기점 강릉방향 128km 지점에 위치하고 있어 여름철 동해안 피서객들이 잠시 쉬었다 가기에 편리하고, 청소년의 심신수련을 위한 숲속교실도 설치되어 있으며 울창한 잣나무 숲속의 산림욕장은 한번왔다간 사람은 누구나 매료되어 찾는 곳이기도 하다. * 구역면적 - 403 ha",
  //   contentTypeId: 12,
  //   sigungu: "서울시 송파구",
  //   rating: 4.8,
  // },
  // {
  //   aid: 125405,
  //   title: "토함산 자연",
  //   image: "",
  //   content:
  //     "토함산 자연휴양림은 삼국통일의 위업을 달성한 신라천년의 찬란한 문화유적을 간직한 국제적 관광도시, 경주의 명산 토함산 기슭에 자리잡고 있으며, 불국사, 석굴암, 보문관광단지, 무열왕릉 등 신라 고적관광과 함께 대자연 속에서 조용히 쉬면서 생각할 수 있는 최적의 산림내 휴식공간이다. * 구역면적 - 123 ㏊(37만평)",
  //   contentTypeId: 14,
  //   sigungu: "서울시 송파구",
  //   rating: 3.2,
  // },
]);

const type = {
  12: "관광지",
  14: "문화시설",
  15: "행사/공연/축제",
  25: "여행코스",
  28: "레포츠",
  32: "숙박",
  38: "쇼핑",
  39: "음식점",
};
const colorType = {
  12: "tag-teal",
  14: "tag-purple",
  15: "tag-pink",
  25: "tag-indigo",
  28: "tag-orange",
  32: "tag-green",
  38: "tag-blue",
  39: "tag-yellow",
};
const truncateText = (text, maxLength) => {
  if (text.length > maxLength) {
    return text.slice(0, maxLength) + "...";
  } else {
    return text;
  }
};
const ratingToPercent = (rating) => {
  const score = +rating * 20;
  return score + 1.5;
};
const move = (aid) => {
  console.log(aid);
  router.push({
    name: "map",
    query: {
      id: aid,
    },
  });
};
const fetchMtti = () => {
  router.push("getMtti");
};
const toggleBtn = (i) => {
  index.value = i;
};
</script>

<template>
  <div class="container">
    <div class="mtti-notification" :style="{ display: mttiNotification }">
      <div class="notification">
        <div class="notification-header">
          <span class="app-name">MTTI</span>
        </div>
        <div class="notification-body">
          <p>MTTI를 설정하면 더 적합한 여행지들을 추천받을 수 있습니다.</p>
          <button @click="fetchMtti">MTTI설정하러 가기</button>
        </div>
      </div>
    </div>
    <div class="top-banner">
      <carousel :items-to-show="0.8" :wrapAround="true" :autoplay="5000">
        <slide v-for="(image, index) in images" :key="index" style="height: 500px">
          <img width="100%" :src="image" alt="" style="object-fit: cover; border-radius: 10px" />
        </slide>
        <template #addons>
          <navigation />
        </template>
      </carousel>
    </div>
    <main class="main-content">
      <div class="select-box">
        <button
          :class="index == 1 ? 'tag-sel-btn' : 'tag-btn'"
          v-if="
            memberStore.userInfo == null ||
            memberStore.userInfo.mtti == null ||
            memberStore.userInfo.mtti == ''
          "
          @click="toggleBtn(1)"
        >
          전체
        </button>
        <button :class="index == 1 ? 'tag-sel-btn' : 'tag-btn'" @click="toggleBtn(1)" v-else>
          MTTI
        </button>
        <button :class="index == 2 ? 'tag-sel-btn' : 'tag-btn'" @click="toggleBtn(2)">
          리뷰많은순
        </button>
        <button :class="index == 3 ? 'tag-sel-btn' : 'tag-btn'" @click="toggleBtn(3)">
          찜많은순
        </button>
      </div>
      <div class="title">
        <button
          class="mtti"
          v-if="memberStore.userInfo != null && memberStore.userInfo.mtti != '' && index == 1"
        >
          MTTI
        </button>
        <div style="margin-top: 50px" v-else></div>
        <h1 style="margin-top: -0.3%">
          {{ memberStore.userInfo != null && index == 1 ? memberStore.userInfo.mtti : "" }} 추천
          여행지
        </h1>
      </div>
      <div class="list">
        <div
          class="card"
          v-for="(attraction, index) in attractionList"
          :key="index"
          @click="move(attraction.content_id)"
        >
          <div class="card-header">
            <img
              v-lazy="
                attraction?.first_image == ''
                  ? sampleImgStore.images[attraction?.content_type_id]
                  : attraction?.first_image
              "
              alt=""
            />
          </div>
          <div class="card-body">
            <div class="card-body-header">
              <span :class="['tag', colorType[attraction?.content_type_id]]">{{
                type[attraction?.content_type_id]
              }}</span>
              <div class="star-ratings">
                <div
                  class="star-ratings-fill space-x-2 text-lg"
                  :style="{ width: ratingToPercent(attraction?.rating) + '%' }"
                >
                  <span>★</span><span>★</span><span>★</span><span>★</span><span>★</span>
                </div>
                <div class="star-ratings-base space-x-2 text-lg">
                  <span>★</span><span>★</span><span>★</span><span>★</span><span>★</span>
                </div>
              </div>
            </div>
            <h4>{{ attraction?.title }}</h4>

            <p>
              {{ truncateText(attraction?.overview, 60) }}
            </p>
            <div class="user">{{ attraction?.sido_name }} {{ attraction?.gugun_name }}</div>
          </div>
        </div>
        <!-- <div class="card" v-for="index in 9" :key="index">
          <img width="100%" src="@/assets/123.jpg" alt="" />
          <p>관광지 이름</p>
          <a href="#" class="btn">자세히 보기</a>
        </div> -->
      </div>
    </main>
  </div>
</template>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}
.mtti-notification {
  justify-content: center;
  align-items: center;
  z-index: 10;
  display: none;
  position: fixed;
  right: 20px;
  bottom: 20px;
  padding: 10px;
  height: 200px;
  border: 1px solid #ccc;
  border-radius: 5px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  transform: translate3d(200%);
  animation: fadeInOut 5s ease;
}
.notification {
  width: 300px;
  padding: 20px;
  height: 80%;
  border: 1px solid #ddd;
  border-radius: 8px;
  background-color: #f9f9f9;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.notification-header {
  margin-top: 20px;
  display: flex;
  align-items: center;
}

.app-logo {
  width: 24px;
  height: 24px;
  margin-right: 10px;
}

.app-name {
  font-weight: bold;
}

.notification-body {
  margin-top: 10px;
}

.notification-body h3 {
  margin: 0 0 5px;
  font-size: 1.2em;
}

.notification-body p {
  margin: 0 0 10px;
  color: #555;
}

.notification-body button {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  background-color: #0078d4;
  color: white;
  cursor: pointer;
  transition: background-color 0.3s;
}

.notification-body button:hover {
  background-color: #005a9e;
}
@keyframes fadeInOut {
  0% {
    transform: translate3d(0, 150%, 0);
  }
  15% {
    transform: translate3d(0, 0, 0);
  }
  75% {
    transform: translate3d(0, 0, 0);
  }
  100% {
    transform: translate3d(0, 150%, 0);
  }
}
.mtti-btn {
  background-color: #007bff;
  color: white;
  border: none;
  padding: 10px 20px;
  cursor: pointer;
  border-radius: 5px;
}

.mtti-btn:hover {
  background-color: #0056b3;
}

.top-banner {
  height: 500px;
  margin-top: 0;
  width: 95%;
  background-color: beige;
  border-radius: 10px;
}
.mtti {
  margin-top: 2%;
  margin-bottom: 0.8%;
  background-color: orange;
  border: none;
  padding: 7px;
  border-radius: 50px;
  margin-left: 1.5%;
}
.title {
  margin-top: 0;
  width: 84%;
  text-align: left;
}
.top-banner * {
  margin: 0;
  padding: 0;
}
.main-content {
  width: 100%;
  align-items: center;
  display: flex;
  flex-direction: column;
  flex: 2;
}
.select-box {
  display: flex;
  justify-content: start;
  width: 80%;
  flex: 1;
  align-content: center;
  padding-left: 3%;
  margin-top: 30px;
}
.list {
  margin-top: -3%;
  justify-content: center;
  display: flex;
  height: auto;
  flex-wrap: wrap;
  overflow: auto;
}
.tag-sel-btn {
  margin-left: 30px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 120px;
  height: 43px;
  border: none;
  border-radius: 50px;
  background-color: #303031;
  color: #fff;
  text-align: center;
  text-decoration: none;
  font-size: 20px;
  cursor: pointer;
  font-weight: 500;
}
.tag-btn {
  margin-left: 30px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 120px;
  height: 43px;
  border: none;
  border-radius: 50px;
  background-color: #ededed;
  color: black;
  text-align: center;
  text-decoration: none;
  font-size: 20px;
  cursor: pointer;
}
.tag-btn:hover {
  transition: all 0.2s ease;
  background-color: #cfcdcd;
}
.tag-btn:not(:hover) {
  transition: all 0.2s ease;
  background-color: #ededed;
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
.card-body-header {
  width: 100%;
  display: flex;
  justify-content: space-between;
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
.star-ratings {
  color: #aaa9a9;
  position: relative;
  unicode-bidi: bidi-override;
  width: max-content;
  -webkit-text-fill-color: transparent; /* Will override color (regardless of order) */
  -webkit-text-stroke-width: 1.3px;
  -webkit-text-stroke-color: #2b2a29;
}

.star-ratings-fill {
  color: #fff58c;
  padding: 0;
  position: absolute;
  z-index: 1;
  display: flex;
  top: 0;
  left: 0;
  overflow: hidden;
  -webkit-text-fill-color: gold;
}

.star-ratings-base {
  z-index: 0;
  padding: 0;
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
.tag-blue {
  background-color: #92d4e4;
}
.tag-indigo {
  background-color: #3d1d94;
}
.tag-orange {
  background-color: #ff7f0e;
}
.tag-green {
  background-color: #2b9348;
}
.tag-yellow {
  color: black;
  background-color: #fffb00;
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

.btn {
  display: inline-block;
  padding: 10px 20px;
  margin-top: 15px;
  background-color: #007bff;
  color: #fff;
  text-decoration: none;
  border-radius: 5px;
}

.btn:hover {
  background-color: #0056b3;
}
</style>
