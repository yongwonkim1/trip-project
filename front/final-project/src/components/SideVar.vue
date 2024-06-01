<template>
  <div>
    <div class="sidebar" :class="{ open: isOpen }">
      <nav class="list">
        <ul>
          <li @click.stop="link('')">
            <div class="groupL">
              <img width="22px" src="@/assets/home.gif" :class="{ imgOpen: isOpen }" />
              <p visibility: hidden :class="{ textOpen: isOpen }" >HOME</p>
            </div>
          </li>
          <li @click.stop="link('board')">
            <div class="groupL">
              <img width="22px" src="@/assets/review.gif" />
              <p visibility: hidden :class="{ textOpen: isOpen }" >REVIEW</p>
            </div>
          </li>
          <li @click="link('planSelect')">
            <div class="groupL">
              <img width="22px" src="@/assets/plan.gif" />
              <p visibility: hidden :class="{ textOpen: isOpen }" >PLAN</p>
            </div>
          </li>
          <li @click="link('map')">
            <div class="groupL">
              <img width="22px" src="@/assets/mtti.gif" />
              <p visibility: hidden :class="{ textOpen: isOpen }" >MTTI</p>
            </div>
          </li>
          <!-- <li @click="link('test')">
            <div class="groupL">
              <img width="22px" src="@/assets/plan.png" />
              <p visibility: hidden :class="{ textOpen: isOpen }" >TEST</p>
            </div>
          </li> -->
        </ul>
      </nav>
    </div>
    <div class="overlay" v-if="isOpen" @click="$emit('close')"></div>
  </div>
</template>

<script setup>
import { useRouter } from "vue-router";
import { useMttiStore } from "@/stores/mtti";

const mttiStore = useMttiStore();

const emit = defineEmits(["close"]);
const props = defineProps({
  isOpen: Boolean,
});

const router = useRouter();
const link = (path) => {
  if (path == "map") {
    mttiStore.isMtti = true;
  }
  router.push(`/${path}`);
  emit("close");
};
</script>

<style scoped>
.sidebar {
  position: fixed;
  top: 0;
  left: 0;
  margin-top: 70px;
  width: 65px; /* 사이드바가 닫혀 있을 때 보이는 너비 */
  height: 100%;
  background-color: white;
  /* box-shadow: 2px 0 5px rgba(0, 0, 0, 0.5); */
  transform: translateX(0); /* 기본 상태에서 사이드바를 보이게 설정 */
  transition: width 0.3s ease;
  z-index: 2000;
}
.sidebar.open {
  width: 250px; /* 사이드바가 열렸을 때의 너비 */
}
.sidebar.open .list li {
  padding-left: 20px; /* 사이드바가 열렸을 때의 padding */
}

.overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 1000;
}

.imgOpen{
  display: flex;
  align-items: start;
}

.list {
  /* margin-top: 30%; */
}
.list ul {
  padding: 0;
  list-style: none;
  margin:0
}

.list li {
  padding: 10px;
  cursor: pointer;
  /* display: flex;
  align-items: start; */
  font-weight: bold;
  font-size: 22px;
  transition: padding-left 0.3s;
}


.list li img {
  display: flex;
  width: 40px;
  height: 40px;
  align-items: start;
  justify-content: center;
}

.textOpen {
  display: flex;
  text-align: center;
  align-items: center;
  justify-content: center;
  margin: 0;
  margin-left: 20%;
}

.close-btn {
  position: absolute;
  top: 10px;
  right: 10px;
  background: none;
  border: none;
  font-size: 20px;
  cursor: pointer;
}
.overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  /* background-color: rgba(0, 0, 0, 0.5); */
  z-index: 1000;
}
nav ul {
  list-style-type: none;
  padding: 0;
}
nav li {
  padding: 20px;
  cursor: pointer;
  font-weight: bold;
  font-size: 22px;
}
.groupL{
  display: flex;
  align-items: center;
}
</style>
