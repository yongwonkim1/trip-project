<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import { useMemberStore } from "@/stores/member";
import { storeToRefs } from "pinia";
import { useSdStore } from "@/stores/searchData";
import { useFireBaseStore } from "@/stores/firebase";
import axios from "axios";
import SideBar from "@/components/SideVar.vue";
import { useMttiStore } from "@/stores/mtti";

const mttiStore = useMttiStore();
const firebaseStore = useFireBaseStore();
const sdStore = useSdStore();
const memberStore = useMemberStore();
const router = useRouter();
const modalVisible = ref(false);
const isSidebarOpen = ref(false);
const { userLogout, isLogin, getProfile, userInfo, profileImage } = memberStore;
const { search_opt, sido } = storeToRefs(sdStore);
const { type } = sdStore;

const toggleModal = () => {
  modalVisible.value = !modalVisible.value;
};
function closeModal() {
  modalVisible.value = false;
}
const link = (path) => {
  router.push(`/${path}`);
};
const logoutBtn = ref("none");
const logout = () => {
  userLogout();
  console.log("ss");
  router.replace("/");
  logoutBtn.value = "flex";
  console.log;
  setTimeout(function () {
    logoutBtn.value = "none";
  }, 1900);
};
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

const closeSidebar = () => {
  isSidebarOpen.value = false;
};
const openSidebar = () => {
  isSidebarOpen.value = true;
};
</script>

<template>
  <div class="container">
    <div class="logout-notification" :style="{ display: logoutBtn }" id="logOutNotification">
      로그아웃 되었습니다.
    </div>
    <div class="left">
      <button
        type="button"
        aria-expanded="true"
        aria-haspopup="menu"
        aria-controls="wa_header_menu"
        class="Header_button_menu__8f3_9"
        @click.stop="openSidebar"
      >
        <span class="blind"><img width="30px" src="@/assets/menu.png" /></span>
      </button>
      <h1 class="logo">
        <a class="Header_link_navertv__TqTR3" @click="link(``)"
          ><span class="blind">VOYAGER</span></a
        >
      </h1>
    </div>
    <div class="mid">
      <div id="search-box" @click="toggleModal">
        <input type="text" placeholder="  관광지 검색" readonly />
        <button style="margin-right: -4%" type="submit" id="search-button">
          <img width="30px" src="@/assets/search.png" />
        </button>
      </div>
    </div>
    <div class="right">
      {{ memberStore.userInfo != null ? "Welcome! " + memberStore.userInfo.nickname : "" }}
      <div class="dropdown" @click="memberStore.isLogin ? '' : link('login')">
        <span class="dropbtn_icon">
          <img
            width="60px"
            height="60px"
            style="border-radius: 50%"
            :src="
              memberStore.profileImage == null
                ? './src/assets/profile.png'
                : memberStore.profileImage
            "
          />
        </span>
        <div class="dropdown-content" v-if="memberStore.isLogin">
          <a @click="logout()">로그아웃</a>
          <a @click="link('signUp')">회원정보</a>
          <a @click="link(`myView`)">모아보기</a>
          <a @click="link(`getMtti`)">MTTI</a>
        </div>
      </div>
    </div>
    <SideBar :isOpen="isSidebarOpen" @close="closeSidebar" />
    <div id="modal" v-if="modalVisible" @click.self="closeModal">
      <form @submit.prevent="search" v-if="mttiStore.isMtti">
        <div id="modal-content" style="display: flex; flex-direction: row">
          <select id="modal-select-box" v-model="search_opt.sido" required>
            <option value="" disabled style="display: none" selected>지역</option>
            <option class="option" :value="key" v-for="(value, key) in sido" :key="key">
              {{ value }}
            </option>
          </select>
          <button type="submit" class="modal-search-btn">
            <img width="30px" src="@/assets/search.png" alt="" />
          </button>
        </div>
      </form>
      <form @submit.prevent="search" v-else>
        <div id="modal-content">
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
  </div>
</template>

<style scoped>
.container {
  width: 100%;
  position: fixed;
  justify-content: space-around;
  display: flex;
  align-items: center;
  z-index: 5;
  text-align: center;
  height: 70px;
  display: flex;
  /* border-bottom: 2px solid black; */
  background-color: white;
}
/* .container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
} */
/* .left {
  display: inline-flex;
  position: relative;
  z-index: 1900;
  padding-left: 8px;
  align-items: center;
  flex: 1;
} */
.left,
.mid,
.right {
  display: flex;
  align-items: center;
  margin-right: 15px;
}

.left {
  flex: 1;
}

.mid {
  flex: 2;
  justify-content: center;
}

.right {
  flex: 1;
  justify-content: flex-end;
}
.blind {
  display: flex;
  align-items: center;
}

.Header_logo_area__dpgTc {
  display: inline-flex;
  order: 1;
  align-items: center;
}
.Header_link_navertv__TqTR3 {
  cursor: pointer;
  display: block;
  padding: 15px 3px;
  color: var(--color-white);
}
.Header_header__JAW_Z.Header_channel_header__4mguP
  .Header_left_wrap__aVIXl.Header_on__BJC_2
  .Header_button_menu__8f3_9 {
  color: var(--color-background);
}
.Header_button_menu__8f3_9 {
  display: inline-flex;
  padding: 13px 15px;
  color: var(--color-greywhite-90);
  order: 0;
}
button {
  background-color: inherit;
  border: none;
  box-shadow: none;
  border-radius: 0;
  padding: 0;
  overflow: visible;
  cursor: pointer;
}
#search-box {
  position: relative;
  width: 300px;
}
#search-box input[type="text"] {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 20px;
  outline: none;
}

#search-box #search-button {
  position: absolute;
  top: 0;
  right: 0;
  padding: 3px;
  border: none;
  background: none;
  cursor: pointer;
}

#search-box #search-button i {
  color: #666;
}
.option-group {
  border: 1px;
}

.logout-notification {
  text-align: center;
  align-items: center;
  z-index: 2;
  display: none; /* 초기에는 숨김 */
  position: fixed;
  top: 50px;
  left: 50%;
  width: 300px;
  height: 70px;
  transform: translateX(-50%);
  background-color: rgb(175, 175, 255);
  font-size: larger;
  color: white;
  border-radius: 10px;
  justify-content: center;
  animation: fadeInOut 2s ease;
}
@keyframes fadeInOut {
  0% {
    transform: translate3d(0, -20%, 0);
    opacity: 0;
  }
  25% {
    transform: translate3d(0, 0, 0);
    opacity: 1;
  }
  75% {
    transform: translate3d(0, 0, 0);
    opacity: 1;
  }
  100% {
    transform: translate3d(0, -20%, 0);
    opacity: 0;
  }
}
.dropdown {
  z-index: 4;
  position: relative;
  display: inline-block;
  cursor: pointer;
  margin-left: 3%;
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
  right: 0; /* 오른쪽 정렬 */
  border: 1px solid #ccc;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
  border-radius: 10px;
}

.dropdown:hover .dropdown-content {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.dropbtn_icon img {
  width: 40px;
  height: 40px;
  border-radius: 50%;
}

.dropdown-content a {
  width: 75%;
  border-radius: 10px;
  color: black;
  padding: 12px 16px;
  text-decoration: none;
  display: block;
  margin-top: 3%;
  margin-bottom: 3%;
}

.dropdown-content a:hover {
  background-color: #ddd;
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

.checkbox-item {
  margin-right: 0px;
}
.sidebar-enter-active,
.sidebar-leave-active {
  transition: transform 0.3s ease;
}
.sidebar-enter,
.sidebar-leave-to {
  transform: translateX(-100%);
}
</style>
