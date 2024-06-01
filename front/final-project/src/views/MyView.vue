<script setup>
import { onMounted, ref, watch } from "vue";
import BoardItem from "../components/BoardItem.vue";
import { useRouter } from "vue-router";
import { useMemberStore } from "@/stores/member";
import axios from "axios";
import StarRating from "@/components/StarRating.vue";
const memberStore = useMemberStore();
const reviews = ref([]);
const jjims = ref([]);
const mode = ref("jjim");
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
const jjim = () => {
  axios
    .get(import.meta.env.VITE_BACKEND_ADDRESS + "/jjim/list", {
      params: {
        userId: memberStore.userInfo.userId,
      },
    })
    .then((res) => {
      var data = res.data;
      jjims.value = [];
      for (let i = 0; i < data.length; i++) {
        jjims.value.push(data[i]);
      }
    });
};
const like = () => {
  axios
    .get(import.meta.env.VITE_BACKEND_ADDRESS + "/review/likes", {
      params: {
        user_id: memberStore.userInfo.userId,
      },
    })
    .then((res) => {
      reviews.value = [];
      var data = res.data;
      console.log(data);
      for (let i = 0; i < data.length; i++) {
        reviews.value.push(data[i]);
      }
    });
};
const myBoard = () => {
  axios
    .get(import.meta.env.VITE_BACKEND_ADDRESS + "/review/myList", {
      params: {
        user_id: memberStore.userInfo.userId,
      },
    })
    .then((res) => {
      console.log(res.data);
      reviews.value = [];
      var data = res.data;
      for (let i = 0; i < data.length; i++) {
        reviews.value.push(data[i]);
      }
    });
};
onMounted(() => {
  jjim();
});
watch(
  () => mode.value,
  (value) => {
    if (value == "jjim") {
      jjim();
      console.log("jjim");
    } else if (value == "like") {
      like();
      console.log("like");
    } else {
      myBoard();
      console.log("myBoard");
    }
  }
);
const changeMode = (key) => {
  mode.value = key;
};
</script>

<template>
  <div>
    <div class="link">
      <button
        :style="{ border: mode == 'jjim' ? '3px solid rgb(98, 98, 177)' : '' }"
        class="styled-button"
        @click="changeMode('jjim')"
      >
        내가 찜한 관광지
      </button>
      <button
        :style="{ border: mode == 'like' ? '3px solid rgb(98, 98, 177)' : '' }"
        class="styled-button"
        @click="changeMode('like')"
      >
        좋아요한 리뷰
      </button>
      <button
        :style="{ border: mode == 'myBoard' ? '3px solid rgb(98, 98, 177)' : '' }"
        class="styled-button"
        @click="changeMode('myBoard')"
      >
        내가 쓴 글
      </button>
    </div>
    <div>
      <div class="container">
        <div class="main-container">
          <div class="board" v-if="mode == 'like' || mode == 'myBoard'">
            <div class="main" v-for="review in reviews" :key="review">
              <BoardItem :review="review" />
            </div>
          </div>
          <div v-if="mode == 'jjim'" class="jjim-con">
            <div class="list">
              <div
                class="card"
                v-for="(attraction, index) in jjims"
                :key="index"
                @click="move(attraction.aid)"
              >
                <div class="card-header">
                  <img
                    :src="
                      attraction.first_image == '' ? './src/assets/s3.jpg' : attraction.first_image
                    "
                    alt=""
                  />
                </div>
                <div class="card-body">
                  <div class="card-body-header">
                    <span :class="['tag', colorType[attraction.content_type_id]]">{{
                      type[attraction.content_type_id]
                    }}</span>
                    <StarRating :rating="attraction.rating" />
                  </div>
                  <h4>{{ attraction.title }}</h4>

                  <p>
                    {{ truncateText(attraction.overview, 60) }}
                  </p>
                  <div class="user">
                    {{ attraction.sigungu }}
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.container {
  display: flex;
  width: 100%;
  flex-direction: row;
  justify-content: start;
  align-items: start;
  /* margin-top: 5%; */
}
.jjim-con {
  width: 100%;
  flex: 1;
}
.link {
  width: 100%;
  display: flex;
  justify-content: center;
  flex-direction: row;
  align-items: center;
}
.main-container {
  width: 100%;
  height: 1000px;
  display: flex;
  flex-direction: row;
  justify-content: center;
}
.board {
  width: 70%;
}
.top {
  display: flex;
  align-items: center;
}
.btn-con {
  flex: 1;
}
.main {
  /* margin-top: 15px; */
  width: 100%;
  /* background-color: rgb(70, 75, 73); */
  /* height: 100px; */
  margin: 0;
  padding: 0;
}

.styled-button {
  margin: 3%;
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
.list {
  margin-top: -3%;
  justify-content: center;
  display: flex;
  height: auto;
  flex-wrap: wrap;
  overflow: auto;
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
</style>
