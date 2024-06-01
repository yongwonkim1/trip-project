<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import { useMemberStore } from "@/stores/member";
import Loading from "@/components/Loading.vue";
import { useFireBaseStore } from "@/stores/firebase";
import axios from "axios";
import { usePlanStore } from "@/stores/plan";
const planStore = usePlanStore();
const router = useRouter();
const firebaseStore = useFireBaseStore();
const { uploadImage } = firebaseStore;
const memberStore = useMemberStore();

const isLoading = ref(false);
const newPlan = ref({
  title: "",
  style: "",
  image: "",
  startDate: "",
  duration: 0,
  user_id: memberStore.userInfo.userId,
});
const image = ref();

const addPlan = async () => {
  try {
    isLoading.value = true;
    const fileName = await uploadImage(image.value);
    newPlan.value.image = fileName;
    const response = await axios.post(
      import.meta.env.VITE_BACKEND_ADDRESS + "/plan/add",
      newPlan.value
    ).then((res)=>{
      var data = res.data;
      console.log(data)
      planStore.setPlan(data, 1, newPlan.value.title, newPlan.value.startDate, newPlan.value.duration, newPlan.value.style, newPlan.value.image, memberStore.userInfo.nickname, memberStore.userInfo.profileImg)
      console.log(planStore.selPlan)
    }
  );
    isLoading.value = false;
    alert("일정 생성 완료");
    if (memberStore.userInfo.mtti != null) {
      
      router.replace({
        name: "afterPlanAdd",
        query: {
          title: newPlan.value.title,
          style: newPlan.value.style,
          image: newPlan.value.image,
          startDate: newPlan.value.startDate,
          duration: newPlan.value.duration,
          user_id: newPlan.value.user_id,
        },
      });
    } else {
      router.replace("planSelect");
    }
  } catch (error) {
    isLoading.value = false;
    console.error(error);
  }
};

const handleImageUpload = (event) => {
  const file = event.target.files[0];
  image.value = file;
};
</script>

<template>
  <div class="plan-add-container">
    <Loading :loading="isLoading" />
    <h2>여행 일정 추가</h2>
    <form @submit.prevent="addPlan" class="plan-form">
      <div class="form-group">
        <label for="title">Title</label>
        <input id="title" v-model="newPlan.title" type="text" required />
      </div>
      <div class="form-group">
        <label for="style">Style</label>
        <input id="style" v-model="newPlan.style" type="text" required />
      </div>
      <div class="form-group">
        <label for="image">Image</label>
        <input id="image" type="file" accept="image/*" @change="handleImageUpload" />
      </div>
      <div class="form-group">
        <label for="startDate">Start Date</label>
        <input id="startDate" v-model="newPlan.startDate" type="date" required />
      </div>
      <div class="form-group">
        <label for="dateOffset">Duration (days)</label>
        <input id="dateOffset" v-model="newPlan.duration" type="number" min="1" required />
      </div>
      <button type="submit" class="submit-button">Add Plan</button>
    </form>
  </div>
</template>

<style scoped>
.plan-add-container {
  max-width: 500px;
  margin: 0 auto;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.plan-add-container h2 {
  text-align: center;
  margin-bottom: 20px;
  color: #333;
}

.plan-form {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.form-group {
  display: flex;
  flex-direction: column;
}

.form-group label {
  margin-bottom: 5px;
  font-weight: bold;
  color: #555;
}

.form-group input[type="text"],
.form-group input[type="date"],
.form-group input[type="number"] {
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 16px;
}

.form-group input[type="file"] {
  padding: 5px;
  font-size: 16px;
}

.submit-button {
  padding: 10px 20px;
  background-color: #007bff;
  color: #fff;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.submit-button:hover {
  background-color: #0056b3;
}
</style>
