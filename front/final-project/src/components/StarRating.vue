<script setup>
import { defineProps, ref } from "vue";
const props = defineProps(["rating"]);
</script>

<template>
  <div class="star-ratings">
    <div
      class="star-ratings-fill space-x-2 text-lg"
      :style="{ width: props.rating * 20 + 1.5 + '%' }"
    >
      <span>★</span><span>★</span><span>★</span><span>★</span><span>★</span>
    </div>
    <div class="star-ratings-base space-x-2 text-lg">
      <span>★</span><span>★</span><span>★</span><span>★</span><span>★</span>
    </div>
  </div>
</template>

<style scoped>
.star-ratings {
  color: #aaa9a9;
  position: relative;
  unicode-bidi: bidi-override;
  width: max-content;
  -webkit-text-fill-color: transparent;
  -webkit-text-stroke-width: 1.3px;
  -webkit-text-stroke-color: #2b2a29;
}

.star-ratings-fill {
  color: #fff58c;
  padding: 0;
  position: absolute;
  z-index: 1;
  display: flex;
  top: 0;
  left: 0;
  overflow: hidden;
  -webkit-text-fill-color: gold;
}

.star-ratings-base {
  z-index: 0;
  padding: 0;
}
</style>
