import { getAccessToken } from "./tokenUtils";

export interface IFetchOption {
  readonly url: string,
  readonly method: string,
  readonly headers?: HeadersInit,
  readonly query?: string,
  readonly body?: BodyInit,
}

export interface IFetchResponse {
  readonly status: number,
  readonly headers: HeadersInit,
  readonly body?: BodyInit
  readonly error?: IFetchErrorResponse
}

export interface IFetchErrorResponse {
  readonly errorCode: string,
  readonly errorMessage: string,
  readonly errorDetailsList?: Array<IFetchErrorResponseDetails>
}

export interface IFetchErrorResponseDetails {
  readonly field?: string,
  readonly message: string,
}

function parse(responseText: string) {

  try {
    return JSON.parse(responseText);
  } catch (error) {
    return null;
  }
}

export async function fetchData(option: IFetchOption): Promise<IFetchResponse> {

  const response = await fetch(
    !!option.query ? `${option.url}?${option.query}` : option.url, 
    {
      method: option.method,
      headers: option.headers,
      body: option.body,
    });
  const responseStatus = response.status;
  const responseHeaders = Object.fromEntries(new Headers(response.headers));
  const responseText = await response.text();
  const responseBody = parse(responseText);

  const fetchResponse: IFetchResponse = {
    status: responseStatus,
    headers: responseHeaders
  };

  if (response.ok) {
    return {
      ...fetchResponse,
      body: (!!responseBody) ? responseBody : responseText
    }
  } else {
    return {
      ...fetchResponse,
      error: (!!responseBody) ? responseBody : responseText
    }
  }
}

export function fetchDataInAuth(option: IFetchOption): Promise<IFetchResponse> {

  const accessToken = getAccessToken();

  if (!accessToken) {
    return Promise.reject(new Error("인증되지 않은 회원입니다."));
  } else {
    return fetchData({
      ...option,
      headers: {
        ...option.headers,
        "Access-Token": accessToken
      }
    });
  }
}

