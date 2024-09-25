package com.document.constant;

public class ResponseConst {

    public static String RESPONSE_SUCCESS = "SUCCESS";
    public static String RESPONSE_FAIL = "FAIL";

    public static String DESC_INVALID_USERNAME_PASSWORD = "Invalid username or password";
    
    public static final String RESPONSE_CODE_SUCCESS = "200";
    public static final String RESPONSE_CODE_EMPLOYEE_NOT_FOUND = "DATA_NOT_FOUND";
    public static final String RESPONSE_MSG_UPDATE_SUCCESS = "แก้ไขข้อมูลสำเร็จ";
    public static final String RESPONSE_MSG_CREATE_SUCCESS = "สร้างข้อมูลสำเร็จ";
    public static final String RESPONSE_MSG_DELETE_SUCCESS = "ลบรายการสำเร็จ";
    public static final String RESPONSE_MSG_FAIL = "เกิดข้อขัดข้อง";
    public static final String RESPONSE_MSG_EXISTS_RECORD = "เกิดข้อขัดข้องพบข้อมูลซ้ำซ้อน";
    public static final String RESPONSE_MSG_NULL_FAIL = "เกิดข้อขัดข้องไม่พบข้อมูล";
    public static final String RESPONSE_MSG_QRY_SUCCESS ="เรียกดูข้อมูลสำเร็จ";
  //GENERIC ERROR
  	public static final int ERROR = 600;
  	
  	//SUCCESS CODES
  	public static final int SUCCESS = 200;
  	
  	//Application CODES

  	//BAD REQUEST
  	public static final int BAD_REQUEST = 400;
  	
  	//URL resource available
  	public static final int NOT_FOUND = 404;
  	
  	//Method Not Allowed
  	public static final int METHOD_NOT_ALLOW = 405;
  	
  	//UnSupport media
  	public static final int UNSUPPORT_MEDIA_TYPE = 415;
  	
  	//Internal Error
  	public static final int INTERNAL_ERROR = 500;
  	
}
