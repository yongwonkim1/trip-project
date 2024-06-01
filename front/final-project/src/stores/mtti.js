import { ref } from "vue";
import { defineStore } from "pinia";

export const useMttiStore = defineStore("mttiStore", () => {
  const isMtti = ref(false);
  return {
    isMtti,
  };
});
