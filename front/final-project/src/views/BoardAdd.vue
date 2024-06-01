<script setup>
import { useRoute, useRouter } from "vue-router";
import { ref, computed } from "vue";
import { useFireBaseStore } from "@/stores/firebase";
import { useMemberStore } from "@/stores/member";
import axios from "axios";
import Loading from "@/components/Loading.vue";
const route = useRoute();
const router = useRouter();
const memberStore = useMemberStore();
const { userInfo } = memberStore;
const firebaseStore = useFireBaseStore();
const { uploadImage } = firebaseStore;
const review = ref({
  aid: route.query.id,
  title: "",
  contents: "",
  rating: 0,
});
const modalVisible = ref(false);
function toggleModal() {
  modalVisible.value = !modalVisible.value;
}
const imageFiles = ref([]);
const images = ref([]);
const imageCount = computed(() => {
  return imageFiles.value.length;
});
const loading = ref(false);

const submitReview = async () => {
  console.log(userInfo.userId);
  try {
    loading.value = true;
    const uploadedFiles = [];

    for (let i = 0; i < imageFiles.value.length; i++) {
      const fileName = await uploadImage(imageFiles.value[i]);
      uploadedFiles.push(fileName);
    }

    const response = await axios.post(
      `${import.meta.env.VITE_BACKEND_ADDRESS}/review/add`,
      {
        review: review.value,
        uploadedFiles: uploadedFiles.join(","),
        id: String(userInfo.userId),
      },
      {
        headers: {
          "Content-Type": "application/json;charset=utf-8",
        },
      }
    );
    loading.value = false;
    // 요청 성공 시 알림
    alert("게시글을 작성 완료");
    router.push("board");
  } catch (error) {
    // 오류 발생 시 알림
    loading.value = false;
    console.error(error);
  }
};

const handleImageUpload = (event) => {
  const file = event.target.files[0];
  imageFiles.value.push(file);
};
</script>

<template>
  <div class="container">
    <Loading :loading="loading" />
    <h1>게시글 추가</h1>
    <form @submit.prevent="submitReview" class="form" enctype="multipart/form-data">
      <div class="form-group">
        <label for="aid">관광지:</label>
        <div class="input-box">
          <input type="text" id="aid" v-model="review.aid" required readonly />
        </div>
      </div>
      <div class="form-group">
        <label for="title">제목:</label>
        <div class="input-box">
          <input type="text" id="title" v-model="review.title" required />
        </div>
      </div>
      <div class="form-group">
        <label for="contents">내용:</label>
        <div class="input-box">
          <textarea id="contents" v-model="review.contents" required></textarea>
        </div>
      </div>
      <div class="form-group">
        <label for="rating">별점:</label>
        <div class="input-box">
          <input
            type="number"
            id="rating"
            step="0.1"
            min="0"
            max="5"
            v-model="review.rating"
            required
          />
        </div>
      </div>
      <label for="image">이미지 업로드:</label>
      <div class="form-group" v-for="index in imageCount + 1" :key="index">
        <div class="input-box">
          <input type="file" id="image" accept="image/*" @change="handleImageUpload" />
          <!-- :required="imageCount == 0 ? true : false" -->
        </div>
      </div>
      <button type="submit" class="btn-submit">게시글 작성</button>
    </form>
  </div>
</template>

<style scoped>
.container {
  max-width: 700px;
  margin: auto;
}

.form {
  display: flex;
  flex-direction: column;
}

.form-group {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

label {
  width: 120px;
  font-weight: bold;
}

.input-box {
  flex: 1;
}

input[type="text"],
input[type="number"],
textarea {
  width: 100%;
  padding: 10px;
  font-size: 16px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

textarea {
  height: 150px;
}

.btn-add {
  width: 120px;
  padding: 10px;
  background-color: #28a745;
  color: #fff;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 16px;
  transition: background-color 0.3s;
}

.btn-add:hover {
  background-color: #218838;
}

.btn-submit {
  width: 120px;
  margin-top: 20px;
  align-self: flex-end;
  padding: 10px;
  background-color: #007bff;
  color: #fff;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 16px;
  transition: background-color 0.3s;
}

.btn-submit:hover {
  background-color: #0056b3;
}
</style>
