<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/tagDeclare.jsp"%>
<%@include file="/headDeclare.jsp"%>
<html>
  <head>
    <script type="text/javascript">
    /**
     * 格式化日期（不含时间）
     */
     function formatterDate (date) {
        var datetime = date.getFullYear()
                + "-"// "年"
                + ((date.getMonth() + 1) > 10 ? (date.getMonth() + 1) : "0"
                        + (date.getMonth() + 1))
                + "-"// "月"
                + (date.getDate() < 10 ? "0" + date.getDate() : date
                        .getDate());
        return datetime;
    }
    /**
     * 格式化日期（含时间"00:00:00"）
     */
     function formatterDate2(date) {
        var datetime = date.getFullYear()
                + "-"// "年"
                + ((date.getMonth() + 1) > 10 ? (date.getMonth() + 1) : "0"
                        + (date.getMonth() + 1))
                + "-"// "月"
                + (date.getDate() < 10 ? "0" + date.getDate() : date
                        .getDate()) + " " + "00:00:00";
        return datetime;
    }
     function formatterDateTime(date) {
         var datetime = date.getFullYear()
                 + "-"// "年"
                 + ((date.getMonth() + 1) > 10 ? (date.getMonth() + 1) : "0"
                         + (date.getMonth() + 1))
                 + "-"// "月"
                 + (date.getDate() < 10 ? "0" + date.getDate() : date
                         .getDate())
                 + " "
                 + (date.getHours() < 10 ? "0" + date.getHours() : date
                         .getHours())
                 + ":"
                 + (date.getMinutes() < 10 ? "0" + date.getMinutes() : date
                         .getMinutes())
                 + ":"
                 + (date.getSeconds() < 10 ? "0" + date.getSeconds() : date
                         .getSeconds());
         return datetime;
     }
    function getFormatterDate(){
    	var date = new Date();
    	var datetime = formatterDateTime(date);
    	return datetime;
    }
    
    function init(){
    	var flag = "${flag}"==""?false:"${flag}";
    	if(flag=="true"){
    		$("#image").attr("src","${basePath}qrCode/QrCode.jpg");
     		$("#image").show();
     		$("#message").text(getFormatterDate() + "二维码生成成功。");
    	}else{
    		$("#image").hide();
    		$("#message").text(getFormatterDate() + "二维码生成失败。");
    	}
    	
    }
    	function wxPay(){
//     		$("#image").attr("src","${basePath}images/testImage.jpg");
//     		$("#image").show();
    		window.location.href = "${basePath}lcc/wxpay!payNative2.action";
    	}
    	function aliPay(){
//     		$("#image").attr("src","${basePath}images/testImage.jpg");
//     		$("#image").show();
    		window.location.href = "${basePath}lcc/alipay!tradePrecreate.action";
    	}
    </script>
  </head>
  
  <body onload="init()">
  <c:if test="false">
  	一层
  	<c:if test="true">
  		二层
  	</c:if>
    </c:if>
  	<div id="message"></div>
    <input type="button" value="生成微信二维码" onclick="wxPay()"/>
     <input type="button" value="生成支付宝二维码" onclick="aliPay()"/>
    <br/>
    <img id="image" name="image" alt="图片生成失败" height="400px" width="400px" />
  </body>
</html>
