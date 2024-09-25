package com.document.respond;


public class ServerResponse {
	private int status;
	private String message;
	private Object data;
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	
	public ServerResponse() {
		super();
	}
	
	 public static ServerResponse getServerResponse(Integer responseMsgFail, String message,Object data){
			ServerResponse serverResponse = new ServerResponse();
			serverResponse.setStatus(responseMsgFail);
			serverResponse.setMessage(message);
			serverResponse.setData(data);
			return serverResponse; 
		}
	
	
}
