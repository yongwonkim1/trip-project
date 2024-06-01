import { createApp } from "vue";
import { createPinia } from "pinia";
import VueLazyLoad from "vue-lazyload";
// import firebase from "firebase";

import App from "./App.vue";
import router from "./router";
import { useKakao } from "vue3-kakao-maps/@utils";
// import.meta.env.VITE_KAKAO_MAP_API_KEY
useKakao(import.meta.env.VITE_KAKAO_MAP_API_KEY);
const app = createApp(App);
app.use(VueLazyLoad, {
  loading: "./src/assets/loading.gif", // 로딩 중에 표시할 이미지 경로
  error: "./src/assets/error.png", // 로딩 실패 시 표시할 이미지 경로
});
app.use(createPinia());
app.use(router);
app.mount("#app");
