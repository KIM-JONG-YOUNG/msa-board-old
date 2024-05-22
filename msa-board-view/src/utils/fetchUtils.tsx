
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
  readonly body: any
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
  const responseHeader = Object.fromEntries(new Headers(response.headers));
  const responseText = await response.text();

  try {
    return {
      status: responseStatus,
      headers: responseHeader,
      body: JSON.parse(responseText)
    };   
  } catch (error) {
    return {
      status: responseStatus,
      headers: responseHeader,
      body: responseText
    };   
  }
}

export function fetchDataInAuth(option: IFetchOption): Promise<IFetchResponse> {

  const accessToken = sessionStorage.getItem("Access-Token");

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
