<script setup>
import { useFireBaseStore } from "@/stores/firebase";
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import axios from "axios";
import { useMemberStore } from "@/stores/member";
import BoardView from "./BoardView.vue";
const props = defineProps(["boardItem"]);
const images = ref([]);
const likeCnt = ref(0);
const BoardItem = ref(props.boardItem);
const commentCnt = ref(0);
const memberStore = useMemberStore();
const hasImage = ref(false);
const firebaseStore = useFireBaseStore();
const { getImage } = firebaseStore;
const router = useRouter();
const isLike = ref(false);
const isModalOpen = ref(false);
const getImages = async () => {
  try {
    const response = await axios.get(
      `${import.meta.env.VITE_BACKEND_ADDRESS}/review/${BoardItem.value.rid}/images`
    );
    const data = response.data;
    for (let i = 0; i < data.length; i++) {
      hasImage.value = true;
      if (data[i].length > 10) {
        const image = await getImage(data[i]);
        images.value.push(image);
      }
    }
    console.log(images.value);
  } catch (error) {
    console.error(error);
  }
};
const getCount = () => {
  axios
    .get(import.meta.env.VITE_BACKEND_ADDRESS + `/review/${BoardItem.value.rid}/count`)
    .then((res) => {
      var data = res.data;
      commentCnt.value = data.commentCount;
      likeCnt.value = data.likeCount;
    });
  if (memberStore.userInfo != null) {
    axios
      .get(import.meta.env.VITE_BACKEND_ADDRESS + "/review/like/check", {
        params: {
          user_id: memberStore.userInfo.userId,
          rid: BoardItem.value.rid,
        },
      })
      .then((res) => {
        isLike.value = res.data;
      });
  }
};
onMounted(async () => {
  getImages();
  getCount();
});
const likeToggle = () => {
  if (memberStore.userInfo != null) {
    console.log(isLike.value);
    if (isLike.value) {
      console.log("취소");
      axios
        .delete(import.meta.env.VITE_BACKEND_ADDRESS + `/review/like/delete`, {
          params: {
            user_id: memberStore.userInfo.userId,
            rid: BoardItem.value.rid,
          },
        })
        .then(() => {
          isLike.value = false;
          console.log("성공");
          likeCnt.value--;
        });
    } else {
      console.log("좋아요");
      axios
        .get(import.meta.env.VITE_BACKEND_ADDRESS + `/review/like/add`, {
          params: {
            user_id: memberStore.userInfo.userId,
            rid: BoardItem.value.rid,
          },
        })
        .then(() => {
          isLike.value = true;
          console.log("성공");
          likeCnt.value++;
        });
    }
  } else {
    alert("로그인 이후 이용해주세요");
    router.push("/login");
  }
};

const openModal = () => {
  axios
    .get(import.meta.env.VITE_BACKEND_ADDRESS + `/review/${BoardItem.value.rid}/detail`)
    .then(() => {
      // console.log("조회수 증가" + BoardItem.value.rid);
    });
  console.log("modal open");
  isModalOpen.value = true;
};

const closeModal = () => {
  console.log("modal close");
  isModalOpen.value = false;
};
</script>

<template>
  <div class="whole" @click="closeModal">
    <div v-if="isModalOpen" class="modal">
      <div class="modal-content">
        <board-view :board-item="BoardItem" />
      </div>
    </div>
    <!-- {{ BoardItem }} -->
    <a href="#" @click.stop="openModal">
      <h3 class="writer">{{ BoardItem.nickname }}</h3>
      <p class="rating">⭐️ {{ BoardItem.rating }}</p>
      <!-- <img :src="props.review" alt="" width="346" height="494" /> -->
      <template v-if="hasImage">
        <carousel :items-to-show="0.2" :wrapAround="true">
          <slide v-for="(image, index) in images" :key="index">
            <figure>
              <img height="400px" width="100%" :src="image" alt="" />
            </figure>
          </slide>
          <template #addons>
            <navigation />
            <!-- <pagination class="slider-pagination" /> -->
          </template>
        </carousel>
      </template>
      <template v-else>
        <carousel :items-to-show="0.2" :wrapAround="true">
          <slide class="slide" v-for="index in 1" :key="index">
            <figure>
              <!-- style="float: left" -->
              <img
                style="float: center"
                height="400"
                width="100%"
                :src="BoardItem?.first_image == '' ? '@/assets/s3.jpg' : BoardItem?.first_image"
                alt=""
              />
            </figure>
          </slide>
          <template #addons>
            <navigation />
            <!-- <pagination class="slider-pagination" /> -->
          </template>
        </carousel>
      </template>
      <h4 class="content">
        {{ BoardItem.title }}<br />
        {{ BoardItem.contents }}
      </h4>
      <div class="details">
        <div class="de-left">
          <div class="count-con">
            <div class="count-con-left">
              <img height="30px" width="30px" src="@/assets/comment.png" alt="" />
              <span>{{ commentCnt }}</span>
              <img
                height="30px"
                width="30px"
                id="likeToggle"
                :src="isLike ? './src/assets/heart-after.png' : './src/assets/heart-before.png'"
                alt=""
                @click.stop="likeToggle"
              />
              <span>{{ likeCnt }}</span>
            </div>
            <span>view : {{ BoardItem?.view_count }}</span>
          </div>
        </div>
      </div>
    </a>
  </div>
</template>

<style scoped>
.whole {
  width: 100%;
  background-color: rgb(70, 75, 73);
}
.thumbnails a {
  -moz-transition: all 0.2s ease;
  -webkit-transition: all 0.2s ease;
  -ms-transition: all 0.2s ease;
  transition: all 0.2s ease;
  border-bottom: none;
  display: block;
  width: 100%;
  margin: 0 0 2em 0;
  text-align: center;
  border-radius: 4px;
  box-shadow: inset 0 0 0 1px rgba(255, 255, 255, 0.25);
  background-color: rgba(255, 255, 255, 0.075);
  cursor: pointer;
  outline: 0;
  overflow: hidden;
  text-decoration-line: none;
  color: #fff;
}

.writer {
  text-align: start;
}

.rating {
  text-align: right;
  margin: -1.5em 1em 0.5em 0;
}
.slide {
  display: flex;
  justify-content: center;
}

.content {
  text-align: left;
  margin: 1em;
}

figure {
  width: 100%;
  margin: 0;
  padding: 0;
}

.thumbnails a img {
  display: block;
  /* width: 100%; */
  border-top-left-radius: 4px;
  border-top-right-radius: 4px;
}

.thumbnails a h3 {
  margin: 1em;
}

.thumbnails a:hover {
  background-color: rgba(255, 255, 255, 0.25);
  box-shadow: inset 0 0 0 1px rgba(255, 255, 255, 0.25), 0 0 0.5em 0 #ff6382;
}

.thumbnails > div {
  display: -moz-flex;
  display: -webkit-flex;
  display: -ms-flex;
  display: flex;
  -moz-flex-direction: row;
  -webkit-flex-direction: row;
  -ms-flex-direction: row;
  flex-direction: row;
  width: 80%;
  padding-left: 2em;
}

.thumbnails > div > :last-child {
  margin-bottom: 0;
}

@media screen and (max-width: 1280px) {
  .thumbnails {
    margin: 0 0 2em -1.5em;
  }

  .thumbnails a {
    margin: 0 0 1.5em 0;
  }

  .thumbnails > div {
    padding-left: 1.5em;
  }
}

@media screen and (max-width: 980px) {
  .thumbnails h3 {
    font-size: 0.8em;
  }
}

@media screen and (max-width: 480px) {
  .thumbnails {
    display: block;
  }

  .thumbnails a {
    margin: 0 0 2em 0 !important;
  }

  .thumbnails > div {
    display: block;
    width: 100%;
  }

  .thumbnails > div:last-child > :last-child {
    margin-bottom: 0 !important;
  }

  .thumbnails h3 {
    font-size: 1em;
  }
}
.count-con {
  display: flex;
  justify-content: space-between;
  margin: 3%;
  align-items: center;
}
.count-con-left {
  display: flex;
  flex-direction: row;
  align-items: center;
}
.count-con-left * {
  margin: 3%;
}
.modal {
  display: flex; /* 모달을 기본적으로 숨김 */
  position: fixed; /* 고정 위치 */
  z-index: 1; /* 다른 요소들보다 위에 표시 */
  left: 0;
  top: 0;
  width: 100%; /* 전체 너비 */
  height: 100%; /* 전체 높이 */
  background-color: rgba(0, 0, 0, 0.4); /* 반투명한 검은 배경 */
  margin-top: 5%;
}

/* 모달 내용 */
.modal-content {
  /* overflow: auto; */
  height: 800px;
  position: relative;
  animation: fadeInUp 0.5s;
  background-color: #fefefe; /* 모달 배경색 */
  margin: 3% auto; /* 상단과 하단에서 모달을 중앙에 위치시킴 */
  margin-left: 15%;
  margin-right: 15%;
  border: 1px solid #888;
  width: 80%; /* 모달 너비 */
}

/* 모달 닫기 버튼 */
.close {
  color: #aaa;
  float: right;
  font-size: 28px;
  font-weight: bold;
}
</style>
