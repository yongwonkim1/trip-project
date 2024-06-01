import { ref } from "vue";
import { defineStore } from "pinia";

export const usePlanStore = defineStore("planStore", () => {
  const selPlan = ref({
    pid: 0,
    day: 0,
    title: "",
    start: "",
    dateOffset: 0,
    style: "",
    image: "",
    user_nickname: "",
    user_image: "",
  });
  const setPlan = (id, date, title, start, dateOffset, style, image, user_nickname, user_image) => {
    selPlan.value.pid = id;
    selPlan.value.day = date;
    selPlan.value.title = title;
    selPlan.value.start = start;
    selPlan.value.dateOffset = dateOffset;
    selPlan.value.style = style;
    selPlan.value.image = image;
    selPlan.value.user_nickname = user_nickname;
    selPlan.value.dateuser_imageOffset = user_image;
  };
  return {
    selPlan,
    setPlan,
  };
});
