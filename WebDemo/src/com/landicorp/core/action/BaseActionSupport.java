package com.landicorp.core.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class BaseActionSupport extends ActionSupport implements ServletRequestAware, ServletResponseAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Logger mLogger = Logger.getLogger(this.getClass());
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String error;
	private String nextUrl;
	private String successInfo;
	private boolean operateSuccess;
	private String returnValue;
	protected ObjectMapper objectMapper;


	// 获取服务器的真实路径
	protected String getRealPath(String path) {
		return ServletActionContext.getRequest().getSession().getServletContext().getRealPath(path);
	}


	public Logger getLogger() {
		return Logger.getLogger(getClass());
	}

	public boolean isOperateSuccess() {
		return operateSuccess;
	}

	public void setOperateSuccess(boolean operateSuccess) {
		this.operateSuccess = operateSuccess;
	}

	protected void addError(String error) {
		this.error = error;
		getParameters().put("error", error);
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getSuccessInfo() {
		return successInfo;
	}

	public void setSuccessInfo(String successInfo) {
		this.successInfo = successInfo;
	}

	protected Map<String, Object> getParameters() {
		return ActionContext.getContext().getParameters();
	}


	/**
	 * Chrome中的showModalDialog方法显示的并不是模态对话框，
	 * 就像新打开一个页面一样，父窗口仍然可以随意获取焦点，并可以打开多个窗体， 而且返回值returnValue也无法返回，一直是undefined
	 * 
	 * 该方法配合window.js，兼容chrome与ie的窗口返回值实现
	 * 
	 * @param returnValue
	 */
	public void setWindowReturnValue(String returnValue) {
		this.returnValue = returnValue;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	protected HttpServletRequest getRequest() {
		return request;
	}

	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	protected HttpServletResponse getResponse() {
		return response;
	}

	public String getReturnValue() {
		return returnValue;
	}

	public void setReturnValue(String returnValue) {
		this.returnValue = returnValue;
	}

	public ObjectMapper getObjectMapper() {
		return objectMapper;
	}

	public void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}


	public String getNextUrl() {
		return nextUrl;
	}


	public void setNextUrl(String nextUrl) {
		this.nextUrl = nextUrl;
	}


	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}


	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

}
