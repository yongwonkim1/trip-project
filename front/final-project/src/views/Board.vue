<script setup>
import { onMounted, ref } from "vue";
import BoardItem from "../components/BoardItem.vue";
import { useRouter } from "vue-router";
import axios from "axios";
import { useSdStore } from "@/stores/searchData";
const sdStore = useSdStore();
const router = useRouter();
const sido = ref({});
const link = (url) => {
  router.push("/" + url);
};
const { type } = sdStore;
onMounted(() => {
  searchBoard();
});
const reviewKeyword = ref("");
const search_opt = ref({
  sido: "",
  keyword: "",
  contentTypeId: [],
});

const reviews = ref([]);
const searchBoard = () => {
  axios
    .get(import.meta.env.VITE_BACKEND_ADDRESS + "/review/search", {
      params: {
        keyword: reviewKeyword.value,
      },
    })
    .then((res) => {
      const data = res.data;
      reviews.value = [];
      for (var i = 0; i < data.length; i++) {
        const review = {
          date: data[i].date,
          user_id: data[i].user_id,
          contents: data[i].contents,
          first_image: data[i].first_image,
          rating: data[i].rating,
          nickname: data[i].nickname,
          rid: data[i].rid,
          title: data[i].title,
          aid: data[i].aid,
          view_count: data[i].view_count,
        };
        reviews.value.push(review);
        console.log(reviews.value);
      }
    });
};
const modalVisible = ref(false);

const toggleModal = () => {
  modalVisible.value = !modalVisible.value;
};
function closeModal() {
  modalVisible.value = false;
}
onMounted(() => {
  axios.get(import.meta.env.VITE_BACKEND_ADDRESS + "/city/sidoList").then((res) => {
    var data = res.data;
    sido.value = data;
  });
});

const search = () => {
  router.push({
    name: "map",
    query: {
      sido: search_opt.value.sido,
      keyword: search_opt.value.keyword,
      cid: search_opt.value.contentTypeId,
    },
  });
  modalVisible.value = false;
  search_opt.value.sido = "";
  search_opt.value.keyword = "";
  search_opt.value.contentTypeId = [];
};
const isToggleDetail = ref(false);
const toggleDetail = () => {
  isToggleDetail.value = !isToggleDetail.value;
};
</script>

<template>
  <div>
    <h1>게시판</h1>
    <div id="modal" v-if="modalVisible" @click.self="closeModal">
      <form @submit.prevent="search">
        <div id="modal-content">
          <p style="color: red">여행지를 먼저 선택해주세요</p>
          <div id="modal-search-box">
            <select id="modal-select-box" v-model="search_opt.sido" required>
              <option value="" disabled style="display: none" selected>지역</option>
              <option class="option" :value="key" v-for="(value, key) in sido" :key="key">
                {{ value }}
              </option>
            </select>
            <input
              id="modal-input-box"
              type="text"
              placeholder="검색어를 입력하세요"
              v-model="search_opt.keyword"
            />
            <button type="submit" class="modal-search-btn">
              <img width="30px" src="@/assets/search.png" alt="" />
            </button>
          </div>
          <div style="text-align: right; margin: 10px">
            <a style="color: blue; cursor: pointer" @click="toggleDetail">상세검색</a>
          </div>
          <div
            class="checkbox-container"
            :style="{
              display: isToggleDetail ? 'flex' : 'none',
            }"
          >
            <div v-for="(label, key) in type" :key="key" class="checkbox-item">
              <input type="checkbox" :id="key" :value="key" v-model="search_opt.contentTypeId" />
              <label :for="key">{{ label }}</label>
            </div>
          </div>
        </div>
      </form>
    </div>
    <div class="container">
      <div class="board">
        <div class="top">
          <div class="btn-con">
            <!-- <button class="board-add-btn" @click="link('boardAdd')">글 추가</button> -->
            <button class="board-add-btn" @click="toggleModal">글 추가</button>
          </div>
          <div class="input-con">
            <div class="search-box">
              <input
                style="padding-left: 5%"
                type="text"
                placeholder="리뷰 검색"
                v-model="reviewKeyword"
              />
              <button
                style="padding-right: 5%"
                type="submit"
                class="search-button"
                @click="searchBoard()"
              >
                <img width="30px" src="@/assets/search.png" />
              </button>
            </div>
          </div>
        </div>
        <div class="main" v-for="review in reviews" :key="review" style="height: 400px">
          <BoardItem :review="review" />
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.container {
  height: 1000px;
  display: flex;
  justify-content: center;
}
.board {
  margin-top: 100px;
  width: 70%;
}
.top {
  display: flex;
  align-items: center;
}
.btn-con {
  flex: 1;
}
.board-add-btn {
  display: inline-block;
  padding: 10px 20px;
  font-size: 16px;
  text-align: center;
  text-decoration: none;
  cursor: pointer;
  border-radius: 5px;
  border: none;
  background-color: #f0f0f0;
  color: #333;
  transition: background-color 0.3s, color 0.3s, border-color 0.3s;
}

.board-add-btn:hover {
  background-color: #ddd;
  color: #555;
  border-color: #999;
}
.input-con {
  flex: 2;
  display: flex;
  justify-content: center;
}
.main {
  margin-top: 15px;
  width: 100%;
  background-color: rgb(70, 75, 73);
  height: 100px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
}
.search-box {
  display: flex;
  position: relative;
  width: 300px;
}

.search-box input[type="text"] {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 20px;
  outline: none;
}

.search-box .search-button {
  position: absolute;
  top: 0;
  right: 0;
  padding: 3px;
  border: none;
  background: none;
  cursor: pointer;
  margin-left: 30px;
}
#modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 4000;
  display: flex;
  justify-content: center;
  align-items: center;
}

#modal-content {
  margin-top: -30%;
  animation: fadeInUp 0.5s;
  background-color: #fff;
  margin-top: -30%;
  animation: fadeInUp 0.5s;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
  padding: 20px;
  /* border-radius: 10px;
  width: 90%;
  max-width: 600px; */
}

#modal-search-box {
  display: flex;
  justify-content: center;
  width: 600px;
  height: 50px;
  border: 3px solid rgb(130, 130, 255);
}

#modal-select-box {
  border-right: 1px solid #666;
  flex: 2;
  border: none;
  padding-left: 2%;
}
#modal-select-box:focus {
  outline: none;
}

#modal-input-box {
  flex: 10;
  border: none;
  padding-left: 3%;
}
#modal-input-box:focus {
  outline: none;
  border-radius: 5px;
}

.modal-search-btn {
  flex: 0.5;
  padding: 0px 8px;
  margin: 3px 3px;
  border: none;
  background-color: rgb(130, 130, 255);
  color: white;
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 5px;
  cursor: pointer;
  transition: all ease 0.3s;
}
.modal-search-btn:hover {
  background-color: rgb(175, 175, 255);
}
@keyframes fadeInUp {
  0% {
    opacity: 0;
    transform: translate3d(0, 15%, 0);
  }
  to {
    opacity: 1;
    transform: translateZ(0);
  }
}
.checkbox-container {
  display: flex;
  flex-wrap: wrap;
  width: 100%;
}
</style>
