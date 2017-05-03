<%@page language="java" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<title></title>
<%
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath() + "/";
	pageContext.setAttribute("basePath", basePath);
%>
<base href="<%=basePath%>" />
<base target="_self"></base>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="Cache-Control" content="no-cache, must-revalidate" />
<meta http-equiv="expires" content="0" />
	
<script type="text/javascript" src="${basePath}js/jquery.min.js"></script>
<script type="text/javascript" src="${basePath}js/jquery.chosen.min.js"></script>
<script type="text/javascript"
	src="${basePath}plugins/artDialog/artDialog.source.js"></script>
<script type="text/javascript"
	src="${basePath}plugins/artDialog/plugins/iframeTools.source.js"></script>
<script type="text/javascript"
	src="${basePath}plugins/layer/layer.js"></script>
<script type="text/javascript"
	src="${basePath}plugins/my97DatePicker/WdatePicker.js"></script>
<script type="text/javascript"
	src="${basePath}plugins/dhtmlxMenu/codebase/dhtmlxmenu.js"></script>
<script type="text/javascript" src="${basePath}js/validator.js"></script>
<script type="text/javascript" src="${basePath}js/ui.js"></script>
<script type="text/javascript" src="${basePath}js/util.js"></script>
<script type="text/javascript" src="${basePath}js/json2.js"></script>
<script type="text/javascript" src="${basePath}plugins/layer/common.js"></script>
<script type="text/javascript">
	var ALERT_BR="<br/>";
	var SUCCESS_MSG="系统处理成功";
	$(function() {
		$.ajaxSetup({
			cache : false,
			beforeSend : function() {
				ldDialog.mask();
			},
			success : function(result, status) {
				ldDialog.unmask();
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				ldDialog.alert("连接服务器失败:" + textStatus);
				ldDialog.unmask();
			},
			complete:function (XMLHttpRequest, status) {
				ldDialog.unmask();
			}
		});
		$("form").submit(function() {
			ldDialog.mask();
		});
	});

	$(function() {
		//IE也能用textarea 
		$("textarea[maxlength]").keyup(function() {
			var area = $(this);
			var max = parseInt(area.attr("maxlength"), 10); //获取maxlength的值 
			if (max > 0) {
				if (area.val().length > max) { //textarea的文本长度大于maxlength 
					area.val(area.val().substr(0, max)); //截断textarea的文本重新赋值 
				}
			}
		});
		//复制的字符处理问题 
		$("textarea[maxlength]").blur(function() {
			var area = $(this);
			var max = parseInt(area.attr("maxlength"), 10); //获取maxlength的值 
			if (max > 0) {
				if (area.val().length > max) { //textarea的文本长度大于maxlength 
					area.val(area.val().substr(0, max)); //截断textarea的文本重新赋值 
				}
			}
		});
	});
</script>
<script type="text/javascript">
	$(function(){$(':checkbox').click(function(event){event=event?event:window.event;event.stopPropagation();});$(':button').click(function(event){event=event?event:window.event;event.stopPropagation();});$('a').click(function(event){event=event?event:window.event;event.stopPropagation();});});
</script>