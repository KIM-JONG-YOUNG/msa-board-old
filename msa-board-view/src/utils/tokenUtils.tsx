import moment from "moment";
import { DATE_TIME_FORMAT } from "../constants/constants";

export function setAccessToken(accessToken: string): void {

  sessionStorage.setItem("accessToken", accessToken);
}
export function setAccessTokenExpiredTime(accessTokenExpiredTime: string): void {

  sessionStorage.setItem("accessTokenExpiredTime", accessTokenExpiredTime);
}

export function setRefreshToken(refreshToken: string): void {

  sessionStorage.setItem("refreshToken", refreshToken);
}

export function setRefreshTokenExpiredTime(refreshTokenExpiredTime: string): void {
  
  sessionStorage.setItem("refreshTokenExpiredTime", refreshTokenExpiredTime);
}

export function getAccessToken(): string | null {

  return sessionStorage.getItem("accessToken");
}

export function getAccessTokenExpiredTime(): Date | null {

  const accessTokenExpiredTime = sessionStorage.getItem("accessTokenExpiredTime");
  const accessTokenExpiredDateTime = moment(accessTokenExpiredTime, DATE_TIME_FORMAT.DATE_TIME).toDate()

  if (isNaN(accessTokenExpiredDateTime.getTime())) {
    sessionStorage.removeItem("accessToken");
    sessionStorage.removeItem("accessTokenExpiredTime");
    return null;
  } else {
    return accessTokenExpiredDateTime;
  }
}

export function getRefreshToken(): string | null {

  return sessionStorage.getItem("refreshToken");
}

export function getRefreshTokenExpiredTime(): Date | null {

  const refreshTokenExpiredTime = sessionStorage.getItem("refreshTokenExpiredTime");
  const refreshTokenExpiredDateTime = moment(refreshTokenExpiredTime, DATE_TIME_FORMAT.DATE_TIME).toDate()

  if (isNaN(refreshTokenExpiredDateTime.getTime())) {
    sessionStorage.removeItem("accessToken");
    sessionStorage.removeItem("accessTokenExpiredTime");
    return null;
  } else {
    return refreshTokenExpiredDateTime;
  }
}

