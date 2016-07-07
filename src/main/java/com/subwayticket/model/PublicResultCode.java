package com.subwayticket.model;

/**
 * @author buptsse-zero <GGGZ-1101-28@Live.cn>
 */
public class PublicResultCode {
    public static final int SUCCESS = 0;

    /*Account*/
    public static final int PHONE_NUM_INVALID = 100001;
    public static final int PHONE_NUM_REGISTERED = 100002;
    public static final int PASSWORD_LENGTH_ILLEGAL = 100003;
    public static final int PASSWORD_FORMAT_ILLEGAL = 100004;

    public static final int PHONE_CAPTCHA_SEND_FAILED = 100101;
    public static final int PHONE_CAPTCHA_INTERVAL_ILLEGAL = 100102;
    public static final int PHONE_CAPTCHA_INCORRECT = 100103;
    public static final int PHONE_CAPTCHA_INVALIDATE = 100104;

    public static final int LOGIN_SUCCESS_WITH_PRE_OFFLINE = 100201;
    public static final int USER_NOT_EXIST = 100202;
    public static final int PASSWORD_INCORRECT = 100203;
    public static final int USER_TOKEN_NOT_MATCH = 100204;

    /*Subway Info*/
    public static final int RESULT_NOT_FOUND = 100301;

    /*Order*/
    public static final int ORDER_SUBMIT_ERROR = 100401;
    public static final int ORDER_SUBMIT_ROUTE_NOT_EXIST = 100402;

    public static final int ORDER_NOT_EXIST = 100410;
    public static final int ORDER_CANCEL_NOT_CANCELABLE = 100411;

    public static final int ORDER_PAY_NOT_PAYABLE = 100420;

    public static final int ORDER_REFUND_NOT_REFUNDABLE = 100430;

    public static final int TICKET_EXTRACT_NOT_EXTRACTABLE = 100440;
    public static final int TICKET_EXTRACT_AMOUNT_TOO_BIG = 100441;
}
