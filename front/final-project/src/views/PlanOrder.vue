<script setup>
import { ref, watch } from "vue";
import { KakaoMap } from "vue3-kakao-maps";
import { useRoute, useRouter } from "vue-router";
import draggable from "vuedraggable";
import { usePlanStore } from "@/stores/plan";
import { useMemberStore } from "@/stores/member";
import axios from "axios";
const memberStore = useMemberStore();
const planStore = usePlanStore();
const { selPlan } = planStore;
const route = useRoute();
const router = useRouter();
const map = ref(null);
const planList = ref(JSON.parse(history.state.planList));

let bounds;
let polyline;

const haversineDistance = (coords1, coords2) => {
  const toRad = (x) => (x * Math.PI) / 180;
  const R = 6371; // Earth's radius in km
  const dLat = toRad(coords2.latitude - coords1.latitude);
  const dLon = toRad(coords2.longitude - coords1.longitude);
  const lat1 = toRad(coords1.latitude);
  const lat2 = toRad(coords2.latitude);

  const a =
    Math.sin(dLat / 2) * Math.sin(dLat / 2) +
    Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
  const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

  return R * c;
};

const optimizePlanList = (planList) => {
  const optimizedList = [];
  const remaining = [...planList];

  let currentPoint = remaining.shift();
  optimizedList.push(currentPoint);

  while (remaining.length > 0) {
    let nearestIndex = 0;
    let nearestDistance = haversineDistance(currentPoint, remaining[0]);

    for (let i = 1; i < remaining.length; i++) {
      const distance = haversineDistance(currentPoint, remaining[i]);
      if (distance < nearestDistance) {
        nearestDistance = distance;
        nearestIndex = i;
      }
    }

    currentPoint = remaining.splice(nearestIndex, 1)[0];
    optimizedList.push(currentPoint);
  }

  return optimizedList;
};
const btn = () => {
  confirm("경로 최적화");
  planList.value = optimizePlanList(planList.value);
  redrawMap();
};

const onLoadKakaoMap = (mapRef) => {
  map.value = mapRef;
  redrawMap();
};
const updatePlanList = ref([]);
const redrawMap = () => {
  console.log(updatePlanList.value);
  console.log(planList.value);
  if (!map.value) return;

  bounds = new kakao.maps.LatLngBounds();

  if (polyline) {
    polyline.setMap(null);
  }

  const points = planList.value.map((attraction) => {
    const point = new kakao.maps.LatLng(attraction.latitude, attraction.longitude);
    bounds.extend(point);
    return point;
  });

  polyline = new kakao.maps.Polyline({
    path: points,
    strokeWeight: 3,
    strokeColor: "#FF0000",
    strokeOpacity: 0.8,
    strokeStyle: "solid",
  });

  polyline.setMap(map.value);

  planList.value.forEach((attraction) => {
    const point = new kakao.maps.LatLng(attraction.latitude, attraction.longitude);
    const marker = new kakao.maps.Marker({ position: point });
    marker.setMap(map.value);
    const overlayContent =
      '<div style="margin-bottom:130px; padding: 10px; border-radius: 5px; background-color: #fff; border: 1px solid #ccc; box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);">' +
      '<p style="margin: 0; color: #333; font-weight: bold;">' +
      attraction.title +
      "</p>" +
      "</div>";

    const overlay = new kakao.maps.CustomOverlay({
      content: overlayContent,
      map: map.value,
      position: marker.getPosition(),
    });
    overlay.setMap(map.value);
  });

  map.value.setBounds(bounds);
};
const loading = ref(false);
const addPlan = async () => {
  console.log(selPlan);
  loading.value = true;
  const aids = planList.value.map((plan) => plan.aid).join(",");
  console.log(aids);
  const res = axios
    .delete(`${import.meta.env.VITE_BACKEND_ADDRESS}/plan/detail/delete`, {
      params: {
        pid: String(selPlan.pid),
        day: selPlan.day,
      },
    })
    .then(() => {
      const response = axios
        .post(
          `${import.meta.env.VITE_BACKEND_ADDRESS}/plan/detail/add`,
          {
            day: String(selPlan.day),
            aids: String(aids),
            id: String(memberStore.userInfo.userId),
            pid: String(selPlan.pid),
          },
          {
            headers: {
              "Content-Type": "application/json;charset=utf-8",
            },
          }
        )
        .then(() => {
          alert("경로가 저장되었습니다.");
          if (memberStore.userInfo.mtti != null) {
            router.replace({
              path: "/afterPlanOrder",
              state: { planList: JSON.stringify(planList.value) },
            });
          } else {
            router.push("planSelect");
          }
        });
    })
    .catch(() => {
      console.log("삭제실패");
    });
};
</script>

<template>
  <div class="container">
    <div class="main-con">
      <KakaoMap
        width="70%"
        height="800px"
        :lat="33.450701"
        :lng="126.570667"
        @onLoadKakaoMap="onLoadKakaoMap"
      />
      <div class="planList">
        <draggable class="draggable" :list="planList" @end="redrawMap()" item-key="jid">
          <template #item="{ element }">
            <div class="plan-card" :key="element.jid">
              <div class="plan-card-header">
                <h3>{{ element.title }}</h3>
                <p>{{ element.addr1 }}</p>
              </div>
            </div>
          </template>
        </draggable>
      </div>
    </div>
    <div class="buttons">
      <button @click="btn" class="btn-optimize">최적 경로</button>
      <button @click="addPlan" class="btn-save">경로 저장</button>
    </div>
  </div>
</template>

<style scoped>
.container {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  margin-top: 80px;
  width: 100%;
  max-width: 1200px;
  margin-left: auto;
  margin-right: auto;
}
.main-con {
  width: 100%;
  display: flex;
  gap: 20px;
}
.planList {
  height: 800px;
  width: 30%;
  background-color: #f9f9f9;
  padding: 10px;
  overflow-x: hidden;
  overflow-y: scroll;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}
.planList::-webkit-scrollbar {
  width: 6px;
}
.planList::-webkit-scrollbar-thumb {
  background-color: #ccc;
  border-radius: 3px;
}

.draggable {
  cursor: move;
}

.plan-card {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  margin: 10px 0;
  padding: 10px;
}

.plan-card-header {
  background-color: rgb(175, 175, 255);
  color: white;
  padding: 10px;
  border-radius: 8px 8px 0 0;
}

.plan-card-body {
  padding: 10px;
}

.plan-card-footer {
  background-color: #f8f8f8;
  padding: 10px;
  border-radius: 0 0 8px 8px;
}

.buttons {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin-top: 20px;
}

.btn-optimize,
.btn-save {
  background-color: rgb(175, 175, 255);
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 8px;
  cursor: pointer;
  font-size: 16px;
  transition: background-color 0.3s ease;
}

.btn-optimize:hover,
.btn-save:hover {
  background-color: rgb(130, 130, 255);
}
</style>
