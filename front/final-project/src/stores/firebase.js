import { getStorage, ref as storageRef, getDownloadURL, uploadBytes } from "firebase/storage";
import { ref, computed } from "vue";
import { defineStore } from "pinia";
import { initializeApp } from "firebase/app";
// Firebase 프로젝트의 구성 정보

export const useFireBaseStore = defineStore("firebase", () => {
  const firebaseConfig = {
    apiKey: import.meta.env.VITE_FIREBASE_APIKEY,
    authDomain: "ssafytrip-f147c.firebaseapp.com",
    projectId: "ssafytrip-f147c",
    storageBucket: "ssafytrip-f147c.appspot.com",
    messagingSenderId: "602787107213",
    appId: "1:602787107213:web:f7452ef869dffe609dedc4",
    measurementId: "G-7YL8DYRH9D",
  };
  const firebaseApp = initializeApp(firebaseConfig);
  const storage = getStorage(firebaseApp);

  const getImage = async (fileName) => {
    try {
      const fileRef = storageRef(storage, fileName);
      const url = await getDownloadURL(fileRef);
      return url;
    } catch (error) {
      console.error(error);
    }
  };
  const uploadImage = async (file) => {
    try {
      const fileName = new Date().getTime() + "-" + file.name;
      const fileRef = storageRef(storage, fileName);
      await uploadBytes(fileRef, file);
      return fileName;
    } catch (error) {
      console.error(error);
      return null;
    }
  };
  return {
    getImage,
    uploadImage,
  };
});
