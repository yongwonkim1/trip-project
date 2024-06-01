<script setup>
import { RouterLink, RouterView } from "vue-router";
import Header from "@/components/Header.vue";
import SideVar from "./components/SideVar.vue";
</script>

<template>
  <div style="text-align: center">
    <div style="z-index: 1000"></div>
    <Header></Header>
    <div>
      <!-- <SideVar /> -->
    </div>
    <div class="router-container">
      <router-view />
    </div>
  </div>
</template>

<style scoped>
.router-container {
  padding-top: 5%;
  margin-left: 5%;
  margin-right: 5%;
}
</style>
