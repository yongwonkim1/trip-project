import { onMounted, ref, watch } from "vue";
import { useRouter } from "vue-router";
import { defineStore } from "pinia";
import { jwtDecode } from "jwt-decode";
import { userConfirm, findById, tokenRegeneration, logout } from "@/api/user";
import { httpStatusCode } from "@/util/http-status";
import { useFireBaseStore } from "./firebase";
import axios from "axios";

export const useMemberStore = defineStore("memberStore", () => {
  const firebaseStore = useFireBaseStore();
  const router = useRouter();
  const isLogin = ref(false);
  const isLoginError = ref(false);
  const userInfo = ref(null);
  const isValidToken = ref(false);
  const profileImage = ref(null);

  watch(
    () => userInfo,
    (newUserInfo) => {
      getProfile();
    },
    { deep: true }
  );

  const userLogin = async (loginUser) => {
    await userConfirm(
      loginUser,
      (response) => {
        if (response.status === httpStatusCode.CREATE) {
          let { data } = response;
          let accessToken = data["access-token"];
          let refreshToken = data["refresh-token"];
          isLogin.value = true;
          isLoginError.value = false;
          isValidToken.value = true;
          sessionStorage.setItem("accessToken", accessToken);
          sessionStorage.setItem("refreshToken", refreshToken);
        }
      },
      (error) => {
        console.log("로그인 실패!!!!");
        isLogin.value = false;
        isLoginError.value = true;
        isValidToken.value = false;
        console.error(error);
      }
    );
  };
  const getProfile = async () => {
    if (userInfo.value.profileImg != null) {
      var image = await firebaseStore.getImage(userInfo.value.profileImg);
      profileImage.value = image;
    }
  };

  const getUserInfo = async (token) => {
    let decodeToken = jwtDecode(token);
    await findById(
      decodeToken.userId,
      (response) => {
        if (response.status === httpStatusCode.OK) {
          userInfo.value = response.data.userInfo;
        } else {
          console.log("유저 정보 없음!!!!");
        }
      },
      async (error) => {
        console.error(
          "g[토큰 만료되어 사용 불가능.] : ",
          error.response.status,
          error.response.statusText
        );
        isValidToken.value = false;

        await tokenRegenerate();
      }
    );
  };

  const tokenRegenerate = async () => {
    await tokenRegeneration(
      JSON.stringify(userInfo.value),
      (response) => {
        if (response.status === httpStatusCode.CREATE) {
          let accessToken = response.data["access-token"];
          sessionStorage.setItem("accessToken", accessToken);
          isValidToken.value = true;
        }
      },
      async (error) => {
        // HttpStatus.UNAUTHORIZE(401) : RefreshToken 기간 만료 >> 다시 로그인!!!!
        if (error.response.status === httpStatusCode.UNAUTHORIZED) {
          // 다시 로그인 전 DB에 저장된 RefreshToken 제거.
          await logout(
            userInfo.value.userid,
            (response) => {
              profileImage.value = null;
              if (response.status === httpStatusCode.OK) {
                console.log("리프레시 토큰 제거 성공");
              } else {
                console.log("리프레시 토큰 제거 실패");
              }
              alert("RefreshToken 기간 만료!!! 다시 로그인해 주세요.");
              isLogin.value = false;
              userInfo.value = null;
              isValidToken.value = false;
              router.push({ name: "user-login" });
            },
            (error) => {
              console.error(error);
              isLogin.value = false;
              userInfo.value = null;
            }
          );
        }
      }
    );
  };

  const userLogout = async () => {
    profileImage.value = null;
    // console.log("로그아웃 아이디 : " + userInfo.value.userId);
    await logout(
      userInfo.value.userId,
      (response) => {
        if (response.status === httpStatusCode.OK) {
          isLogin.value = false;
          userInfo.value = null;
          isValidToken.value = false;

          sessionStorage.removeItem("accessToken");
          sessionStorage.removeItem("refreshToken");
        } else {
          console.error("유저 정보 없음!!!!");
        }
      },
      (error) => {
        console.log(error);
      }
    );
  };

  return {
    isLogin,
    isLoginError,
    userInfo,
    isValidToken,
    profileImage,
    userLogin,
    getUserInfo,
    tokenRegenerate,
    userLogout,
    getProfile,
  };
});
