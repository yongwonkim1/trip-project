<script setup>
import { ref, watch, onMounted, computed, defineProps } from "vue";
import { Carousel, Slide, Pagination, Navigation } from "vue3-carousel";
import { useFireBaseStore } from "@/stores/firebase";
import BoardView from "./BoardView.vue";
import axios from "axios";
import { useRouter } from "vue-router";
import { formatDistanceToNow } from "date-fns";
import { useSampleImgStore } from "@/stores/sampleImg";
import profileImg from "@/assets/profile.png";
const sampleImgStore = useSampleImgStore();
const props = defineProps(["review"]);
console.log(props.review);
const firebaseStore = useFireBaseStore();
const { getImage } = firebaseStore;
const router = useRouter();
// const BoardItem = {
//   user_id: "",
//   rid: 1,
//   place: "청태산 자연 휴양림",
//   date: "2024-05-13:10:53",
//   title: "청태산 자연 휴양림 여행 후기",
//   contents: "청태산 자연 휴양림 여행 후기",
//   view_count: 10,
//   comments_count: 3,
//   images: ["./src/assets/123.jpg", "./src/assets/789.png", "./src/assets/456.png"],
// };
const BoardItem = ref(props.review);
const images = ref([]);
const likeCnt = ref(0);
const commentCnt = ref(0);
const getImages = async () => {
  try {
    const response = await axios.get(
      `${import.meta.env.VITE_BACKEND_ADDRESS}/review/${props.review.rid}/images`
    );
    const data = response.data;
    for (let i = 0; i < data.length; i++) {
      hasImage.value = true;
      if (data[i].length > 10) {
        const image = await getImage(data[i]);
        images.value.push(image);
      }
    }
    BoardItem.value.images = images.value;
    // console.log(images.value);
    // console.log(hasImage.value);
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
const userProfile = ref("");
onMounted(async () => {
  getProfile();
  getImages();
  getCount();
  const reviewDate = new Date(props.review.date);
  formattedDate.value = formatDistanceToNow(reviewDate, { addSuffix: true });
});
const getProfile = () => {
  axios
    .get(import.meta.env.VITE_BACKEND_ADDRESS + `/user/image?id=${BoardItem.value.user_id}`)
    .then(async (res) => {
      console.log(BoardItem.value.user_id);
      userProfile.value = await getImage(res.data);
      console.log(userProfile.value);
    });
};
const totalImages = computed(() => BoardItem.images.length);
const curPos = ref(0);
const isModalOpen = ref(false);
const isLike = ref(false);
const hasImage = ref(false);
import "vue3-carousel/dist/carousel.css";
import { useMemberStore } from "@/stores/member";
const memberStore = useMemberStore();
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
const formattedDate = ref("");
// if (isModalOpen) {
//   document.body.style.overflow = "hidden";
// } else {
//   document.body.style.overflow = "auto";
// }
const truncateText = (text, maxLength) => {
  if (text?.length > maxLength) {
    return text.slice(0, maxLength) + "...";
  } else {
    return text;
  }
};

const copyBtn = ref("none");
// const share = (link) => {
//   window.navigator.clipboard.writeText(link).then(() => {
//     copyBtn.value = "block"; // 알림 표시
//     setTimeout(function () {
//       copyBtn.value = "none"; // 2초 후에 숨김
//     }, 2000);
//   });
// };

const declaration = () => {
  console.log(BoardItem.value);
  if (confirm("신고하시겠습니까?")) {
    alert("신고되었습니다.");
  }
};

const del = () => {
  if (memberStore.userInfo != null && BoardItem.value.user_id == memberStore.userInfo.userId) {
    if (confirm("삭제하시겠습니까?")) {
      axios
        .delete(
          import.meta.env.VITE_BACKEND_ADDRESS + "/review/delete",
          {
            data: {
              rid: String(BoardItem.value.rid),
              id: String(memberStore.userInfo.userId),
            },
          },
          {
            headers: {
              "Content-Type": "application/json;charset=utf-8",
            },
          }
        )
        .then(() => {
          alert("삭제되었습니다.");
          router.go();
        })
        .catch(() => alert("삭제실패"));
    }
  } else {
    alert("삭제 할 수 없습니다!");
  }
  // if(BoardItem.value.user_id!=useMemberStore)
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
  <div class="container" @click="closeModal">
    <div class="copy-notification" :style="{ display: copyBtn }" id="copyNotification">
      복사되었습니다.
    </div>
    <div class="img-con" slideWidth="100%" style="margin: 0">
      <template v-if="hasImage">
        <carousel :items-to-show="0.2" :wrapAround="true" style="margin: 0px">
          <slide v-for="(image, index) in images" :key="index">
            <figure>
              <img style="float: left;margin-top:-3.5%" height="400px" :src="image" alt="" />
            </figure>
          </slide>
          <template #addons>
            <navigation />
            <!-- <pagination class="slider-pagination" /> -->
          </template>
        </carousel>
      </template>
      <template v-else>
        <carousel :items-to-show="0.2" :wrapAround="true" style="margin: 0px">
          <slide v-for="index in 1" :key="index">
            <figure>
              <img
                style="float: left;margin-top:-3.5%"
                height="400px"
                v-lazy="
                  BoardItem?.first_image == ''
                    ? sampleImgStore.images[BoardItem.aid]
                    : BoardItem?.first_image
                "
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
    </div>
    <div class="main-con">
      <div class="userInfo">
        <div class="profile">
          <img
            style="width: 50px; height: 50px; border-radius: 10px"
            :src="userProfile == '' || userProfile == null ? profileImg : userProfile"
            alt=""
          />
          <div class="info">
            <p>{{ BoardItem?.nickname }}</p>
            <p>{{ formattedDate }}</p>
          </div>
        </div>
        <div class="share">
          <div class="dropdown">
            <span class="dropbtn_icon"></span>
            <img
              style="width: 30px; height: 30px; cursor: pointer"
              src="@/assets/details.png"
              alt=""
            />
            <div class="dropdown-content">
              <a @click="declaration()">신고하기</a>
              <a @click="del()">삭제하기</a>
            </div>
          </div>
        </div>
      </div>
      <div class="contents" @click.stop="openModal">
        <h1>
          <a @click.stop="openModal" class="title">
            {{ truncateText(BoardItem?.title, 30) }}
          </a>
        </h1>
        <p>
          <a @click.stop="openModal" class="article">
            {{ truncateText(BoardItem?.contents, 100) }}
          </a>
        </p>
      </div>
      <div class="details">
        <div class="de-left">
          <span>view : {{ BoardItem?.view_count }}</span>
          &nbsp;&nbsp;&nbsp;
          <span>comments: {{ commentCnt }}</span>
        </div>
        <div class="de-right">
          <label style="margin-top: 4%; cursor: pointer" for="likeToggle">
            <img
              width="40px"
              id="likeToggle"
              :src="isLike ? './src/assets/heart-after.png' : './src/assets/heart-before.png'"
              alt=""
              @click="likeToggle"
            />
            {{ likeCnt }}
          </label>
        </div>
      </div>
    </div>
    <div v-if="isModalOpen" class="modal">
      <div class="modal-content">
        <board-view :board-item="BoardItem" :images="images" />
      </div>
    </div>
  </div>
</template>

<style scoped>
.container {
  min-width: 300px;
  width: 100%;
  display: flex;
}
.img-con-con {
  display: flex;
  flex: 1;
}
.img-con {
  background-color: white;
  flex: 1;
  height: 400px;
  position: relative;
}
.slider-pagination {
  margin-top: -70px;
}
.img {
  width: 100%;
  height: 400px;
  overflow: hidden;
  object-fit: cover;
}
.main-con {
  flex: 1.2;
  background-color: rgb(255, 255, 255);
  display: flex;
  flex-direction: column;
  height: 370px;
  padding-top: 10px;
  padding-bottom: 20px;
}
.userInfo {
  flex: 1.8;
  display: flex;
}
.profile {
  display: flex;
  align-items: center;
  flex-direction: row;
  height: auto;
  flex: 1;
  margin-left: 10px;
}
.info {
  margin-left: 20px;
  font-size: small;
}
.share {
  display: flex;
  flex: 1;
  justify-content: end;
  align-items: center;
  margin-right: 10px;
}

.dropdown {
  position: relative;
  display: inline-block;
}

.dropbtn {
  border: 1px solid rgb(37, 37, 37);
  border-radius: 4px;
  background-color: #f5f5f5;
  font-weight: 400;
  color: rgb(37, 37, 37);
  padding: 12px;
  width: 200px;
  text-align: left;
  cursor: pointer;
  font-size: 12px;
}
.dropdown-content {
  /* margin-left: -50%; */
  display: none;
  position: absolute;
  z-index: 1; /*다른 요소들보다 앞에 배치*/
  font-weight: 400;
  background-color: #f9f9f9;
  min-width: 200px;
  cursor: pointer;
}

.dropdown-content a {
  display: block;
  text-decoration: none;
  color: rgb(37, 37, 37);
  font-size: 12px;
  padding: 12px 20px;
}

.dropdown-content a:hover {
  background-color: #ececec;
}

.dropdown:hover .dropdown-content {
  display: block;
  /* margin-left: -100px; */
}

.contents {
  margin: 5%;
  margin-top: 0;
  text-align: left;
  flex: 6;
}
.title {
  cursor: pointer;
}
.article {
  cursor: pointer;
}
.details {
  flex: 2;
  border-top: 1px solid black;
  display: flex;
  align-items: center;
  align-content: center;
}
.de-left {
  margin-left: 5%;
  flex: 1;
  display: flex;
  justify-content: start;
}
.de-right {
  margin-right: 5%;
  display: flex;
  justify-content: end;
  flex: 1;
  align-items: center;
}
.copy-notification {
  z-index: 2;
  display: none;
  position: fixed;
  top: 50px;
  left: 50%;
  transform: translateX(-50%);
  background-color: #f0f0f0;
  padding: 10px 20px;
  border-radius: 5px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
  animation: fadeInOut 2s ease;
}
@keyframes fadeInOut {
  0% {
    opacity: 0;
  }
  25% {
    opacity: 1;
  }
  75% {
    opacity: 1;
  }
  100% {
    opacity: 0;
  }
}
.modal {
  display: flex;
  position: fixed;
  z-index: 6;
  left: 0;
  top: 0;
  width: 100%; /* 전체 너비 */
  height: 100%; /* 전체 높이 */
  background-color: rgba(0, 0, 0, 0.4); /* 반투명한 검은 배경 */
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

@keyframes fadeInUp {
  0% {
    opacity: 0;
    transform: translate3d(0, 7%, 0);
  }
  to {
    opacity: 1;
    transform: translateZ(0);
  }
}
</style>
