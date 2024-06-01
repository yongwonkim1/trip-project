import { createRouter, createWebHistory } from "vue-router";
import Main from "@/views/Main.vue";
import Board from "@/views/Board.vue";
import BoardAdd from "@/views/BoardAdd.vue";
import Map from "@/views/Map.vue";
import Test from "@/views/Test.vue";
import Login from "@/views/Login.vue";
import SignUp from "@/views/SignUp.vue";
import Plan from "@/views/Plan.vue";
import PlanSelect from "@/views/PlanSelect.vue";
import PlanAdd from "@/views/PlanAdd.vue";
import UserInfo from "@/views/UserInfo.vue";
import UserModi from "@/views/UserModi.vue";
import PlanOrder from "@/views/PlanOrder.vue";
import PlanView from "@/views/PlanView.vue";
import MyView from "@/views/MyView.vue";
import GetMtti from "@/views/GetMtti.vue";
import AfterPlanAdd from "@/views/AfterPlanAdd.vue";
import AfterPlanOrder from "@/views/AfterPlanOrder.vue";

import { storeToRefs } from "pinia";
import { useMemberStore } from "@/stores/member";

const onlyAuthUser = async (to, from, next) => {
  const memberStore = useMemberStore();
  const { userInfo, isValidToken } = storeToRefs(memberStore);
  console.log(userInfo.value);
  const { getUserInfo } = memberStore;
  let token = sessionStorage.getItem("accessToken");
  if (userInfo.value != null && token) {
    await getUserInfo(token);
  }
  if (!isValidToken.value || userInfo.value === null) {
    next({ name: "login" });
  } else {
    next();
  }
};

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "main",
      component: Main,
    },
    {
      path: "/board",
      name: "board",
      component: Board,
    },
    {
      path: "/boardAdd",
      name: "boardAdd",
      beforeEnter: onlyAuthUser,
      component: BoardAdd,
    },
    {
      path: "/map",
      name: "map",
      beforeEnter: onlyAuthUser,
      component: Map,
    },
    {
      path: "/plan",
      name: "plan",
      beforeEnter: onlyAuthUser,
      component: Plan,
    },
    {
      path: "/test",
      name: "test",
      component: Test,
    },
    {
      path: "/login",
      name: "login",
      component: Login,
    },
    {
      path: "/signup",
      name: "signup",
      component: SignUp,
    },
    {
      path: "/planSelect",
      name: "planSelect",
      beforeEnter: onlyAuthUser,
      component: PlanSelect,
    },
    {
      path: "/planAdd",
      name: "planAdd",
      beforeEnter: onlyAuthUser,
      component: PlanAdd,
    },
    {
      path: "/userInfo",
      name: "userInfo",
      beforeEnter: onlyAuthUser,
      component: UserInfo,
    },
    {
      path: "/userModi",
      name: "userModi",
      beforeEnter: onlyAuthUser,
      component: UserModi,
    },
    {
      path: "/planOrder",
      name: "planOrder",
      component: PlanOrder,
    },
    {
      path: "/planView",
      name: "planView",
      component: PlanView,
    },
    {
      path: "/myView",
      name: "myView",
      beforeEnter: onlyAuthUser,
      component: MyView,
    },
    {
      path: "/getMtti",
      name: "getMtti",
      beforeEnter: onlyAuthUser,
      component: GetMtti,
    },
    {
      path: "/afterPlanAdd",
      name: "afterPlanAdd",
      beforeEnter: onlyAuthUser,
      component: AfterPlanAdd,
    },
    {
      path: "/afterPlanOrder",
      name: "afterPlanOrder",
      beforeEnter: onlyAuthUser,
      component: AfterPlanOrder,
    },

    // {
    //   path: "/boardAdd",
    //   name: "boardAdd",
    //   component: BoardAdd,
    // },
  ],
});

export default router;
