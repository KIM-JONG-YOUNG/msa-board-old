export const GROUP = {
    ADMIN: "관리자",
    USER: "사용자"
} as const

export const GENDER = {
    MAIL: "남자",
    FEMAIL: "여자"
} as const
 
export const STATE = {
    ACTIVE: "활성화",
    DEACTIVE: "비활성화"
} as const 

export const EMAIL_DOMAIN = {
    NAVER_DOMAIN: "naver.com",
    GMAIL_DOMAIN: "gmail.com"
} as const

export const MEMBER_SORT = {
    USERNAME: "계정",
    NAME: "이름",
    EMAIL: "이메일",
    CREATED_DATE_TIME: "생성일시",
    UPDATED_DATE_TIME: "수정일시"
} as const

export const POST_SORT = {
    TITLE: "제목",
    CONTENT: "내용",
    WRITER: "작성자 계정",
    VIEWS: "조회수",
    CREATED_DATE_TIME: "생성일시",
    UPDATED_DATE_TIME: "수정일시"
} as const

export const ORDER = {
    ASC: "오름차순",
    DESC: "내림차순"
} as const

export const ERROR_CODE = {
    INVALID_PARAMETER: "COMMON-001",
    UNCHECKED_ERROR: "COMMON-999",
    
    NOT_FOUND_MEMBER: "MEMBER-001",
    NOT_FOUND_MEMBER_USERNAME: "MEMBER-002",
    DUPLICATE_MEMBER_USERNAME: "MEMBER-003",
    INVALID_MEMBER_PASSWORD: "MEMBER-004",
    
    NOT_FOUND_POST: "POST-001",
    NOT_FOUND_POST_WRITER: "POST-002",

    EXPIRED_ACCESS_TOKEN: "TOKEN-001",
    REVOKED_ACCESS_TOKEN: "TOKEN-002",
    INVALID_ACCESS_TOKEN: "TOKEN-003",
    EXPIRED_REFRESH_TOKEN: "TOKEN-011",
    REVOKED_REFRESH_TOKEN: "TOKEN-012",
    INVALID_REFRESH_TOKEN: "TOKEN-013",

    UNAUTHORIZED_MEMBER: "SECURITY-001",
    NOT_ACCESSIBLE_MEMBER: "SECURITY-002",

    NOT_ADMIN_GROUP_USERNAME: "ADMIN-001",
    NOT_ADMIN_GROUP_POST: "ADMIN-002",
    NOT_USER_GROUP_MEMBER: "ADMIN-003",

    NOT_USER_GROUP_USERNAME: "USER-001",
    NOT_POST_WRITER: "USER-002",
} as const

export const DATE_TIME_FORMAT = {
    DATE: 'yyyy-MM-dd',
    TIME: 'HH:mm:ss',
    DATE_TIME: 'yyyy-MM-dd HH:mm:ss'
} as const
