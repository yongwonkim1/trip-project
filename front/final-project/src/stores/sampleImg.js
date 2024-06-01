import { ref } from "vue";
import { defineStore } from "pinia";

export const useSampleImgStore = defineStore("sampleImgStore", () => {
  const images = ref({
    28: "./src/assets/leisureSports.jpg",
    32: "./src/assets/hotel.jpg",
    38: "./src/assets/shopping.jpg",
    39: "./src/assets/restaurant.jpg",
    12: "./src/assets/attraction.jpg",
    15: "./src/assets/festival.jpg",
    25: "./src/assets/travelCourse.jpg",
    14: "./src/assets/culture.jpg",
  });
  return {
    images,
  };
});
