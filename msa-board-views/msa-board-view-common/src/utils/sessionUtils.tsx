import { GROUP } from "../constants/constants";

export function setAccessToken(accessToken: string): void {

  (!!accessToken) && sessionStorage.setItem("accessToken", accessToken);
}

export function setRefreshToken(refreshToken: string): void {
  
  (!!refreshToken) && sessionStorage.setItem("refreshToken", refreshToken);
}

export function setGroup(group: keyof typeof GROUP): void {

  (!!group) && sessionStorage.setItem("group", group);
}

export function getAccessToken(): string {

  const accessToken = sessionStorage.getItem("accessToken");

  return (!!accessToken) ? accessToken : "";
}

export function getRefreshToken(): string {

  const refreshToken = sessionStorage.getItem("refreshToken");

  return (!!refreshToken) ? refreshToken : "";
}

export function getGroup(): keyof typeof GROUP | "" {

  const group = sessionStorage.getItem("group");

  return (group === "ADMIN" || group === "USER") ? group : "";
}

export function initSessionInfo(): void {

  sessionStorage.removeItem("accessToken");
  sessionStorage.removeItem("refreshToken");
  sessionStorage.removeItem("group");
}