<script setup>
import { ref, onMounted, watch, onUpdated, onUnmounted } from "vue";
import { useRouter, useRoute } from "vue-router";
import axios from "axios";
import BoardItem from "../components/BoardItem.vue";
import StarRating from "@/components/StarRating.vue";
import { KakaoMap } from "vue3-kakao-maps";
import { useMemberStore } from "@/stores/member";
import MapBoardItem from "@/components/MapBoardItem.vue";
import { useMttiStore } from "@/stores/mtti";
import { useSampleImgStore } from "@/stores/sampleImg";
const sampleImgStore = useSampleImgStore();
const mttiStore = useMttiStore();
const router = useRouter();
const route = useRoute();
const aid = ref(0);
const map = ref();
const memberStore = useMemberStore();
const reviews = ref([]);
const markers = ref([]);
const search_opt = ref({
  region: 0,
  keyword: "",
  typeNumbers: [],
});
onUnmounted(() => {
  mttiStore.isMtti = false;
});
onMounted(() => {
  setTimeout(() => {
    aid.value = route.query.id;
    // if (aid != 0) {
    //   selected(aid.value);
    //   checkJjim(aid.value);
    //   getReviews(aid.value);
    // }
  }, 100);
});
watch(aid, async () => {
  if (aid != 0) {
    selected(aid.value);
    checkJjim(aid.value);
    getReviews(aid.value);
  }
});

const getReviews = (id) => {
  axios
    .get(import.meta.env.VITE_BACKEND_ADDRESS + "/attraction/reviewList", {
      params: {
        attractionId: id,
      },
    })
    .then((res) => {
      var data = res.data;
      reviews.value = [];
      for (let i = 0; i < data.length; i++) {
        reviews.value.push(data[i]);
      }
    });
};
const isLike = ref(false);
const checkJjim = (aid) => {
  if (memberStore.userInfo != null) {
    axios
      .get(import.meta.env.VITE_BACKEND_ADDRESS + "/jjim/check", {
        params: {
          user_id: memberStore.userInfo.userId,
          aid: aid,
        },
      })
      .then((res) => {
        isLike.value = res.data;
      });
  }
};
const loadMarkers = () => {
  if (mttiStore.isMtti) {
    axios
      .get(import.meta.env.VITE_BACKEND_ADDRESS + "/attraction/mtti", {
        params: {
          region: search_opt.value.region,
          userId: memberStore.userInfo.userId,
        },
      })
      .then((res) => {
        var data = res.data;
        attractions.value = [];
        for (var i = 0; i < data.length; i++) {
          const attraction = {
            aid: data[i].content_id,
            content_type_id: data[i].content_type_id,
            first_image: data[i].first_image,
            gugun: data[i].gugun,
            lat: data[i].latitude,
            lon: data[i].longitude,
            sido: data[i].sido,
            title: data[i].title,
          };
          attractions.value.push(attraction);
        }
      })
      .then(() => {
        viewMarker();
      });
  } else {
    axios
      .get(
        import.meta.env.VITE_BACKEND_ADDRESS +
          "/attraction/search" +
          `?region=${search_opt.value.region}&keyword=${
            search_opt.value.keyword
          }&typeNumbers=${String(search_opt.value.typeNumbers)}`,
        {
          headers: {
            "Content-Type": "application/json;charset=utf-8",
          },
        }
      )
      .then((res) => {
        var data = res.data;
        attractions.value = [];
        for (var i = 0; i < data.length; i++) {
          const attraction = {
            aid: data[i].content_id,
            content_type_id: data[i].content_type_id,
            first_image: data[i].first_image,
            gugun: data[i].gugun,
            lat: data[i].latitude,
            lon: data[i].longitude,
            sido: data[i].sido,
            title: data[i].title,
          };
          attractions.value.push(attraction);
        }
      })
      .then(() => {
        viewMarker();
      });
  }
};

let bounds;
const removeMarkers = () => {
  markers.value.forEach((marker) => marker.setMap(null));
  markers.value.length = 0;
};
const viewMarker = () => {
  if (markers.value.length > 0) {
    removeMarkers();
  }
  bounds = new kakao.maps.LatLngBounds();
  let marker;
  let point;
  attractions.value.forEach((attraction) => {
    point = new kakao.maps.LatLng(attraction.lat, attraction.lon);
    marker = new kakao.maps.Marker({ position: point });

    if (map.value !== undefined) {
      marker.setMap(map.value);
      kakao.maps.event.addListener(marker, "click", () => {
        aid.value = attraction.aid;
      });
      markers.value.push(marker);
    }
    bounds.extend(point);
  });
  setBounds();
};
const onLoadKakaoMap = (mapRef) => {
  map.value = mapRef;
};
const coordinate = ref({
  lat: 37.566826,
  lng: 126.9786567,
});
const setBounds = () => {
  if (map.value !== undefined) {
    map.value.setBounds(bounds);
    coordinate.lat = (bounds.pa + bounds.qa) / 2;
    coordinate.lng = (bounds.ha + bounds.oa) / 2;
  }
};

watch(
  route,
  (newRoute) => {
    if (newRoute.query.sido != null) {
      search_opt.value.keyword = encodeURIComponent(newRoute.query.keyword);
      search_opt.value.typeNumbers = Array.isArray(newRoute.query.cid)
        ? newRoute.query.cid.join(",")
        : newRoute.query.cid;
      search_opt.value.region = newRoute.query.sido;
      loadMarkers();
    }
  },
  { immediate: true }
);

// onMounted(() => {
//   setTimeout(() => {
//     if (route.query.sido != null) {
//       search_opt.value.keyword = encodeURIComponent(route.query.keyword);
//       search_opt.value.typeNumbers = Array.isArray(route.query.cid)
//         ? route.query.cid.join(",")
//         : route.query.cid;
//       search_opt.value.region = route.query.sido;
//       loadMarkers();
//     }
//     if (route.query.id != null) {
//       console.log(route.query.id);
//       aid.value = route.query.id;
//     }
//   }, 100);
// });

// onUpdated(() => {
//   if (route.query.sido != null) {
//     search_opt.value.keyword = encodeURIComponent(route.query.keyword);
//     search_opt.value.typeNumbers = Array.isArray(route.query.cid)
//       ? route.query.cid.join(",")
//       : route.query.cid;
//     search_opt.value.region = route.query.sido;
//     loadMarkers();
//   }
// });

const attractions = ref([]);

const selected = (id) => {
  axios
    .get(import.meta.env.VITE_BACKEND_ADDRESS + "/attraction/detail", {
      params: { attractionId: id },
    })
    .then((res) => {
      var data = res.data;
      attraction.value.content_id = data.content_id;
      attraction.value.content_type_id = data.content_type_id;
      attraction.value.first_image = data.first_image;
      attraction.value.gugun = data.gugun;
      attraction.value.lat = data.latitude;
      attraction.value.lon = data.longitude;
      attraction.value.overview = data.overview;
      attraction.value.rating = data.rating;
      attraction.value.sido = data.sido;
      attraction.value.title = data.title;
      map.value.panTo(new kakao.maps.LatLng(data.latitude, data.longitude));
      let point = new kakao.maps.LatLng(data.latitude, data.longitude);
      let marker = new kakao.maps.Marker({ position: point });
      marker.setMap(map.value);
      kakao.maps.event.addListener(marker, "click", () => {
        aid.value = id;
      });
    })
    .catch((error) => {
      console.log(error);
    });
  //리뷰 조회
  // axios.get(import.meta.env.VITE_BACKEND_ADDRESS + "/attraction/detail)
};
const attraction = ref([
  {
    content_id: 0,
    content_type_id: 0,
    first_image: "",
    gugun: "",
    latitude: 0,
    longitude: 0,
    overview: "",
    rating: 0,
    sido: "",
    title: "",
  },
]);

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

const moveReview = (id) => {
  router.push({ name: "boardAdd", query: { id: id } });
};

const addJjiim = (id) => {
  axios.post(import.meta.env.VITE_BACKEND_ADDRESS + "/jjim/add", {
    params: {
      user_id: memberStore.userInfo.userId,
      aid: id,
    },
  });
};
const likeToggle = () => {
  if (memberStore.userInfo != null) {
    if (isLike.value) {
      axios
        .delete(import.meta.env.VITE_BACKEND_ADDRESS + `/jjim/delete`, {
          params: {
            user_id: memberStore.userInfo.userId,
            aid: aid.value,
          },
        })
        .then(() => {
          isLike.value = false;
        });
    } else {
      axios
        .post(import.meta.env.VITE_BACKEND_ADDRESS + `/jjim/add`, {
          user_id: memberStore.userInfo.userId,
          aid: aid.value,
        })
        .then(() => {
          isLike.value = true;
        });
    }
  } else {
    alert("로그인 이후 이용해주세요");
    router.push("/login");
  }
};
</script>

<template>
  <div class="container">
    <div class="right">
      <div class="detail-con" v-if="aid != 0">
        <p>{{ type[attraction.content_type_id] }}</p>
        <div class="rating-con">
          <StarRating :rating="attraction.rating" />
        </div>
        <h2 style="margin: -10px">{{ attraction.title }}</h2>
        <p>{{ attraction.sido }} {{ attraction.gugun }}</p>
        <img
          style="opacity: 1"
          width="90%"
          :src="
            attraction.first_image == ''
              ? sampleImgStore.images[attraction.content_type_id]
              : attraction.first_image
          "
          alt=""
        />
        <p style="margin: 20px">{{ attraction.overview }}</p>
        <div @click="likeToggle()" style="cursor: pointer">
          <img
            width="40px"
            height="40px;"
            :src="isLike ? './src/assets/scrap.png' : './src/assets/unscrap.png'"
            alt=""
          />
        </div>
        <button @click="moveReview(attraction.content_id)">리뷰작성</button>
        <!-- Thumbnails -->
        <section class="thumbnails">
          <div class="main" v-for="review in reviews" :key="review.rid">
            <MapBoardItem :boardItem="review"></MapBoardItem>
          </div>
        </section>
      </div>
      <div v-else class="detail-con">
        <h1>여행지를 선택해주세요</h1>
      </div>
    </div>
    <KakaoMap
      width="90%"
      height="800px"
      :lat="coordinate.lat"
      :lng="coordinate.lng"
      @onLoadKakaoMap="onLoadKakaoMap"
    >
      <!-- <KakaoMap :lat="33.450701" :lng="126.570667" @onLoadKakaoMap="onLoadKakaoMap" /> -->

      <!-- <KakaoMapMarker
        :lat="coordinate.lat"
        :lng="coordinate.lng"
        @onLoadKakaoMap="onLoadKakaoMap"
      ></KakaoMapMarker> -->
    </KakaoMap>
  </div>
</template>

<style scoped>
.container {
  display: flex;
  justify-content: center;
}
.right {
  z-index: 3;
  position: absolute;
  right: 9.3%;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 23%;
  height: 800px;
}
.detail-con {
  min-width: 350px;
  z-index: 3;
  background-color: rgba(0, 0, 0, 0.85);
  color: white;
  width: 90%;
  height: 95%;
  border-radius: 30px;
  overflow-x: hidden;
  overflow-y: scroll;
}
.detail-con::-webkit-scrollbar {
  display: none;
}
.main {
  margin-top: 10px;
  width: 100%;
}
.rating-con {
  width: 100%;
  margin-top: -5%;
  margin-bottom: 5%;
  display: flex;
  align-items: center;
  justify-content: center;
}
.right-title {
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0;
  padding: 0;
}

.thumbnails {
  display: -moz-flex;
  display: -webkit-flex;
  display: -ms-flex;
  display: flex;
  -moz-flex-direction: column;
  -webkit-flex-direction: column;
  -ms-flex-direction: column;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  position: relative;
  /* margin: 0 0 2em -2em; */
}
</style>
