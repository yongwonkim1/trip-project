import { ref, onMounted } from "vue";
import { defineStore } from "pinia";
import axios from "axios";

export const useSdStore = defineStore("sdStore", () => {
  onMounted(() => {
    axios.get(import.meta.env.VITE_BACKEND_ADDRESS + "/city/sidoList").then((res) => {
      var data = res.data;
      sido.value = data;
    });
  });
  const sido = ref({});
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
  const search_opt = ref({
    sido: "",
    keyword: "",
    contentTypeId: [],
  });
  return {
    sido,
    type,
    search_opt,
  };
});
