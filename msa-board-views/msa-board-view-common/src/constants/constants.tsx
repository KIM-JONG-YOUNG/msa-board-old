export const GROUP = {
    ADMIN: "관리자",
    USER: "사용자"
} as const;

export const GENDER = {
    MAIL: "남자",
    FEMAIL: "여자"
} as const;

export const STATE = {
    ACTIVE: "활성화",
    DEACTIVE: "비활성화"
} as const;

export const EMAIL_DOMAIN = {
    EXAMPLE_DOMAIN: "example.com",
    NAVER_DOMAIN: "naver.com",
    GMAIL_DOMAIN: "gmail.com"
} as const;

export const MEMBER_SORT = {
    USERNAME: "계정",
    NAME: "이름",
    EMAIL: "이메일",
    CREATED_DATE_TIME: "생성일시",
    UPDATED_DATE_TIME: "수정일시"
} as const;

export const POST_SORT = {
    TITLE: "제목",
    CONTENT: "내용",
    WRITER: "작성자 계정",
    VIEWS: "조회수",
    CREATED_DATE_TIME: "생성일시",
    UPDATED_DATE_TIME: "수정일시"
} as const;

export const ORDER = {
    ASC: "오름차순",
    DESC: "내림차순"
} as const;

export const ERROR_CODE = {

    INVALID_PARAMETER: "COMMON-001",
    INACCESSIBLE_URL: "COMMON-002",
    UNCHECKED_ERROR: "COMMON-003",

    NOT_FOUND_MEMBER: "MEMBER-001",
    NOT_FOUND_MEMBER_USERNAME: "MEMBER-002",
    DUPLICATED_MEMBER_USERNAME: "MEMBER-003",
    NOT_MATCHED_MEMBER_PASSWORD: "MEMBER-004",
    NOT_ADMIN_GROUP_MEMBER_USERNAME: "MEMBER-005",
    NOT_USER_GROUP_MEMBER_USERNAME: "MEMBER-006",
    NOT_USER_GROUP_MEMBER: "MEMBER-007",

    NOT_FOUND_POST: "POST-001",
    NOT_FOUND_POST_WRITER: "POST-002",
    NOT_POST_WRITER: "POST-003",
    NOT_ADMIN_GROUP_POST: "POST-004",
    DEACTIVE_POST: "POST-005",

    EXPIRED_ACCESS_TOKEN: "TOKEN-001", 
    REVOKED_ACCESS_TOKEN: "TOKEN-002",
    INVALID_ACCESS_TOKEN: "TOKEN-003",

    EXPIRED_REFRESH_TOKEN: "TOKEN-004",
    REVOKED_REFRESH_TOKEN: "TOKEN-005",
    INVALID_REFRESH_TOKEN: "TOKEN-006",

    NOT_ADMIN_GROUP_REFRESH_TOKEN: "TOKEN-007",
    NOT_USER_GROUP_REFRESH_TOKEN: "TOKEN-008"

} as const;

export const DATE_TIME_FORMAT = {
    DATE: 'yyyy-MM-dd',
    TIME: 'HH:mm:ss',
    DATE_TIME: 'yyyy-MM-dd HH:mm:ss'
} as const;
