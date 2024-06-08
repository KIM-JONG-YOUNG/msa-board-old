import { EMAIL_DOMAIN, ERROR_CODE, GENDER, GROUP, MEMBER_SORT, ORDER, POST_SORT, STATE } from "../constants/constants";

function isGroupKey(key: string): key is keyof typeof GROUP {

    return key in GROUP;
}

function isGenderKey(key: string): key is keyof typeof GENDER {
    
    return key in GENDER;
}

function isStateKey(key: string): key is keyof typeof STATE {
    
    return key in STATE;
}

function isEmailDomainKey(key: string): key is keyof typeof EMAIL_DOMAIN {
    
    return key in EMAIL_DOMAIN;
}

function isMemberSortKey(key: string): key is keyof typeof MEMBER_SORT {
    
    return key in MEMBER_SORT;
}

function isPostSortKey(key: string): key is keyof typeof POST_SORT {
    
    return key in POST_SORT;
}

function isOrderKey(key: string): key is keyof typeof ORDER {
    
    return key in ORDER;
}

function isErrorCode(key: string): key is keyof typeof ERROR_CODE {
    
    return key in ERROR_CODE;
}

function isDateString(dateString: string): boolean {
    
    const dateRegex = /\d{4}-\d{2}-\d{2}/;

    return (dateRegex.test(dateString) && isNaN(new Date(dateString).getTime()));
}

export default {
    isGroupKey,
    isGenderKey,
    isStateKey,
    isEmailDomainKey,
    isMemberSortKey,
    isPostSortKey,
    isOrderKey,
    isErrorCode,
    isDateString
};