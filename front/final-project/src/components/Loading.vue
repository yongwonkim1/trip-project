<script setup>
const props = defineProps(["loading"]);
console.log("로딩" + props.loading);
</script>

<template>
  <div v-if="props.loading" class="loading-modal">
    <div class="loading-spinner"></div>
    <div class="loading-text">Loading...</div>
  </div>
</template>

<style scoped>
.loading-modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 9999;
}

.loading-spinner {
  border: 8px solid #f3f3f3;
  border-top: 8px solid #3498db;
  border-radius: 50%;
  width: 50px;
  height: 50px;
  animation: spin 1s linear infinite;
}

.loading-text {
  color: white;
  font-size: 24px;
  margin-left: 20px;
}
@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}
</style>
