<%@ page language="java"  pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String ip = "";
	//获取客户端真实IP
	if(request.getHeader("x-forwarded-for")==null){
		ip=request.getRemoteAddr();
	}else{
		ip=request.getHeader("x-forwarded-for");
	}
	
	response.sendRedirect(basePath+"lcc/index.action");
	
	
%>