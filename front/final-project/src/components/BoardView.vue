<script setup>
import axios from "axios";
import { defineProps, onMounted, ref } from "vue";
import "vue3-carousel/dist/carousel.css";
import { Carousel, Slide, Pagination, Navigation } from "vue3-carousel";
import { useMemberStore } from "@/stores/member";
import { useFireBaseStore } from "@/stores/firebase";
import { useSampleImgStore } from "@/stores/sampleImg";
const sampleImgStore = useSampleImgStore();
const firebaseStore = useFireBaseStore();
const memberStore = useMemberStore();
const comments = ref([]);
const props = defineProps({
  boardItem: Object,
  images: Object,
});
const commentContent = ref("");
const userProfile = ref("");
const commentDTO = ref({
  rid: props.boardItem.rid,
  user_id: "",
  content: "",
});
onMounted(() => {
  getComment();
  setTimeout(() => {
    getProfile();
  }, 50);
});
const getProfile = () => {
  axios
    .get(import.meta.env.VITE_BACKEND_ADDRESS + `/user/image?id=${props.boardItem.user_id}`)
    .then(async (res) => {
      if (res.data != null) {
        userProfile.value = await firebaseStore.getImage(res.data);
      }
      console.log(userProfile.value);
    });
};
const getComment = async () => {
  axios
    .get(import.meta.env.VITE_BACKEND_ADDRESS + `/review/${props.boardItem.rid}/comment`)
    .then(async (res) => {
      var data = res.data;
      console.log(data);
      comments.value = [];
      for (let i = 0; i < data.length; i++) {
        var profile = "";
        await axios
          .get(import.meta.env.VITE_BACKEND_ADDRESS + `/user/image?id=${data[i].user_id}`)
          .then(async (res) => {
            if (!(res.data == null || res.data == undefined || res.data == "")) {
              profile = await firebaseStore.getImage(res.data);
            }
          });
        const comment = {
          cid: data[i].cid,
          user_id: data[i].user_id,
          content: data[i].content,
          data: data[i].date,
          profile: profile,
        };
        comments.value.push(comment);
      }
      console.log("여기");
      console.log(comments.value);
    });
};
const commentAdd = () => {
  console.log(commentDTO.value);
  if (memberStore.userInfo != null) {
    commentDTO.value.user_id = memberStore.userInfo.userId;
    axios
      .post(import.meta.env.VITE_BACKEND_ADDRESS + "/review/comment/add", commentDTO.value)
      .then(() => {
        getComment();
      });
  } else {
    alert("로그인 후에 이용해주세요 ");
  }
};
</script>
<template>
  <div class="container" @click.stop="">
    <div class="img-con" slideWidth="90%">
      <carousel :items-to-show="0.4" :wrapAround="true" style="flex: 1" v-if="images.length != 0">
        <slide v-for="(image, index) in props.images" :key="index">
          <div class="carousel-item">
            <figure>
              <img
                style="float: left; object-fit: cover"
                width="600px"
                height="600px"
                :src="image"
                alt=""
              />
            </figure>
          </div>
        </slide>
        <template #addons>
          <!-- <navigation />
          <pagination class="slider-pagination" /> -->
        </template>
      </carousel>
      <carousel :items-to-show="0.4" :wrapAround="true" style="flex: 1" v-else>
        <slide v-for="index in 1" :key="index">
          <div class="carousel-item">
            <figure>
              <img
                style="float: left; object-fit: cover"
                width="600px"
                height="600px"
                v-lazy="
                  boardItem?.first_image == '' ? sampleImgStore.images[28] : boardItem?.first_image
                "
                alt=""
              />
            </figure>
          </div>
        </slide>
        <template #addons>
          <!-- <navigation />
          <pagination class="slider-pagination" /> -->
        </template>
      </carousel>
    </div>
    <div class="main-con">
      <div class="article">
        <div class="owner-userInfo">
          <img
            :src="
              userProfile == '' || userProfile == null ? './src/assets/profile.png' : userProfile
            "
            class="user-profile-image"
          />
          <p class="nickName">{{ props.boardItem.nickname }}</p>
        </div>
        <div class="main-content">
          <div class="board-contents">
            <div class="userInfo">
              <img src="@/assets/789.png" class="user-profile-image" />
              <p class="nickName">ssafy</p>
              &nbsp;&nbsp;
              <p>{{ props.boardItem.title }}</p>
            </div>
            <p class="contents" v-text="props.boardItem.contents"></p>
          </div>
        </div>
        <div class="new-comment">
          <textarea
            name=""
            id=""
            cols="30"
            rows="10"
            placeholder="댓글 작성"
            v-model="commentDTO.content"
          ></textarea>
          <button @click="commentAdd()">작성하기</button>
        </div>
        <div class="comments">
          <div class="comment" v-for="comment in comments" :key="comment.cid">
            <div class="userInfo">
              <img
                :src="
                  comment.profile == '' || comment.profile == null
                    ? './src/assets/profile.png'
                    : comment.profile
                "
                class="user-profile-image"
              />
              <p class="nickName">{{ comment.user_id }}</p>
              &nbsp;&nbsp;
              <p>{{ comment.content }}</p>
            </div>
            <p class="date">{{ comment.date }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.container {
  height: 800px;
  display: flex;
  width: 100%;
  align-content: center;
  align-items: center;
  margin: 0;
  padding: 0;
}
.container * {
  margin: 0;
  padding: 0;
}

.img-con {
  background-color: black;
  flex: 1;
  display: flex;
  align-items: center;
  width: 800px;
  height: 800px;
  position: relative;
  line-height: 800px;
  vertical-align: middle;
}
.main-con {
  display: flex;
  flex: 1;
  height: 100%;
  background-color: black;
  overflow-y: scroll;
  overflow-x: hidden;
}
.main-con::-webkit-scrollbar {
  display: none;
}
.article {
  color: white;
  width: 100%;
}
.owner-userInfo {
  background-color: black;
  z-index: 1;
  position: fixed;
  display: flex;
  width: 32%;
  justify-content: start;
  padding: 15px;
  padding-top: 10px;
  padding-bottom: 10px;
  align-items: center;
}

.main-content {
  display: flex;
  margin-top: 60px;
  border-top: 1px solid white;
  border-bottom: 1px solid white;
}
.board-contents {
  flex: 1;
}
.userInfo {
  display: flex;
  justify-content: start;
  margin: 15px;
  margin-top: 10px;
  align-items: center;
}
.user-profile-image {
  width: 40px;
  height: 40px;
  border-radius: 20px;
  margin-right: 10px;
}
.contents {
  margin: 10%;
  margin-top: 5%;
  margin-bottom: 5%;
  width: 100%;
  text-align: left;
}
.new-comment {
  display: flex;
  height: 50px;
  padding: 3%;
}
.new-comment textarea {
  flex: 5;
  background-color: black;
  border: none;
  resize: none;
  color: white;
  font-size: larger;
  font-weight: bolder;
  display: flex;
  align-items: center;
}
.new-comment textarea::-webkit-scrollbar {
  display: none;
}
.new-comment textarea:focus {
  outline: none;
}
.new-comment button {
  flex: 1;
  cursor: pointer;
  border: none;
  background-color: antiquewhite;
  border-radius: 5px;
  font-weight: bolder;
}
.comments {
  width: 100%;
  flex: 1;
}
.date {
  color: gray;
  margin-left: 10%;
  font-size: small;
  text-align: left;
  margin-top: -5px;
}
</style>
