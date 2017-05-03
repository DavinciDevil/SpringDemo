var lastActivateTabId = "welcome";//最后一次激活Tab的Id

//创建选项卡
function addTab(id,title,url,closebtn){
	var tabId = "tab_" + id;
	var ifId = "if_" + id;
	if($("#" + tabId).length == 0){//未创建
		
		//tab
		var noclose = (closebtn == undefined || closebtn) ? "" : "nav_noclose";
		var display = ((getTotalWidth() + 112) > getDisplayMaxWidth()) ? "none" : "";
		var htmlStr = 
		"<li class=\"nav_itemon " + noclose + "\" id=\"tab_" + id + "\" style=\"display:" + display +"\">" +
			"<div class=\"navtitle\" onclick=\"activateTab('" + id + "');\">" + title + "</div>";
			if(closebtn == undefined || closebtn){
				htmlStr += "<div class=\"nav_closebt\" onclick=\"removeTab('" + id + "');\"></div>";
			}
		htmlStr += "</li>";
		$(".nav ul").append(htmlStr);
		
		//下拉项
		var closeTemp = "noclose";
		var closeclick = "";
		var tbodyId = "#alltabbox-tbd1";
		var separator = "<tr class=\"separator\"><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>";
		if(closebtn == undefined || closebtn){
			closeTemp = "close";
			tbodyId = "#alltabbox-tbd2";
			closeclick = "onclick=\"removeTab('" + id + "');\"";
		}
		if($("#alltabbox-tbd2").find(".alltabbox-item").length > 0 
				|| !(closebtn == undefined || closebtn)){
			separator = "";
		}
		var selectHtmlStr = separator +
			"<tr class=\"alltabbox-item\" id=\"alltabbox-item_" + id + "\">" +
				"<td class=\"alltabbox-item-noicon\">&nbsp;</td>" +
				"<td class=\"alltabbox-item-text\" onclick=\"activateTab('" + id + "');\">" + title + "</td>" +
				"<td class=\"alltabbox-item-" + closeTemp + "\" " + closeclick + ">&nbsp;</td>" +
			"</tr>";
		$(tbodyId).append(selectHtmlStr);
		
		activateTab(id);//激活新建的tab
		$(".loadingmark").show();//显示加载图标
		
		//iframe页面
		htmlStr = "<iframe id=\"if_" + id + "\" class=\"contentFrame tabIframe\" width=\"100%\" height=\"100%\" src=\"" 
			+ url+ "\" frameborder=\"0\" style=\"display: none;\" onload=\"$('.loadingmark').hide();\"></iframe>";
		$("#contentTd").append(htmlStr);
		$("#" + ifId).show();
	} else{//已创建 激活
		activateTab(id);
		$(".loadingmark").show();//显示加载图标
		reloadActivatedTab();
	}
}

//激活选项卡
function activateTab(id){
	var tabId = "tab_" + id;
	var ifId = "if_" + id;
	var alltabboxId = "alltabbox-item_" + id;
	$(".nav ul li").removeClass("nav_itemon").addClass("nav_item");
	$("#" + tabId).removeClass("nav_item").addClass("nav_itemon");
	$(".alltabbox-item-iconselected").removeClass("alltabbox-item-iconselected").addClass("alltabbox-item-noicon");
	$("#" + alltabboxId).find("td:first").removeClass("alltabbox-item-noicon").addClass("alltabbox-item-iconselected");
	$(".tabIframe").hide();
	$("#" + ifId).fadeIn(200);
	lastActivateTabId = id;
	
	if($("#" + tabId).is(":hidden")){//激活的Tab当前处于隐藏状态
		var cloneObj = $("#" + tabId).clone(true);
		$("#" + tabId).remove();
		$(cloneObj).insertAfter($(".nav_noclose:last")).show();
	}
	
	var width = 0;
	$("[id^=tab_]").each(function(){
		width += $(this).width() + 52;
		if((width + 112) > getDisplayMaxWidth() 
				&& $(this).attr("id")!=tabId){//小于总显示宽度
			$(this).hide();
		}else{
			$(this).show();
		}
		var style = "font-weight:" + ($(this).is(":hidden") ? "bold" : "") + ";";
		$("#alltabbox-item_" + $(this).attr("id").replace("tab_","")).find(".alltabbox-item-text").attr("style", style);
	});
}

//关闭选项卡
function removeTab(id){
	var tabId = "tab_" + id;
	var ifId = "if_" + id;
	var alltabboxId = "alltabbox-item_" + id;
	if(lastActivateTabId == id){//关闭当前激活的选项卡时，需要激活另一个选项卡
		var prevId = $("#" + tabId).prev().attr("id").replace("tab_","");
		activateTab(prevId);
	}else{
		activateTab(getActivatedTabId());
	}
	$("#" + tabId).remove();
	$("#" + ifId).remove();
	$("#" + alltabboxId).remove();
	if($("#alltabbox-tbd2").find(".alltabbox-item").length == 0){
		$("#alltabbox-tbd2").empty();
	}
}

//关闭可关闭的所有选项卡
function removeAllTab(){
	$("[id^=tab_]").each(function(){
		if($(this).find(".nav_closebt").length > 0){
			removeTab($(this).attr("id").replace("tab_",""));
		}
	});
	$("#alltabbox-tbd2").empty();
	
}

//重新加载激活的页面
function reloadActivatedTab(url){
	var frameId = "if_" + getActivatedTabId();
	var frame = document.getElementById(frameId);
	frame.contentWindow.location.href = frame.src;
}

//获取当前激活状态的tab的id
function getActivatedTabId(){
	return $(".nav .nav_itemon").attr("id").replace("tab_","");
}

//菜单栏开关
function menuToggle(){
	if($("#menuTd").is(":hidden")){
		$(".loadingmark").css("left","167px");
		$("#menuTd").show().animate({width:'160px'},'fast',function(){
			$(".middlebut div").removeClass("middlebutbg_r").addClass("middlebutbg_l");
		});
	}else{
		$(".loadingmark").css("left","7px");
		$("#menuTd").animate({width:'1px'},'fast',function(){
			$("#menuTd").hide();
			$(".middlebut div").removeClass("middlebutbg_l").addClass("middlebutbg_r");
		});
	}
}

//设置主框架高度
function setContainerHeight(){
	$("#contentTable,#contentTd,.contentFrame").height($(window).height() - 134);
}

//获取可显示tab的最大宽度
function getDisplayMaxWidth(){
	return $(window).width() - 40;
}

//获取已创建并显示的tab的总宽度
function getTotalWidth(){
	var width = 0;
	$("[id^=tab_]").each(function(){
		if($(this).is(":not(:hidden)")){
			width += $(this).width() + 52;
		}
	});
	return width;
}

//显示/隐藏tab下拉列表
function showlist(){
	if($(".alltabbox").is(":hidden")){
		$(".alltabbox").show();
	}else{
		$(".alltabbox").hide();
	}
}

$(function(){
	$(window).resize(function(){
		setContainerHeight();//
		activateTab(getActivatedTabId());//重新激活当前激活的tab以计算宽度并调整位置
	});
	setContainerHeight();
	$(document).bind("mouseover", function(e) {
		var target = $(e.target);
		if (target.closest(".alltabbox").length == 0 
				&& target.closest(".delbody").length == 0
				&& target.closest(".nav_item").length == 0
				&& target.closest(".nav_itemon").length == 0) {
			$(".alltabbox").hide();
		}
	});
});
