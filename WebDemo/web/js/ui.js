(function($) {
	/**
	 * 开关
	 */
	$.fn.ldSwitch = function(options) {
		options = $.extend({
			labels : [ 'YES', 'NO' ]
		}, options);
		return this.each(function() {
			var originalCheckBox = $(this), labels = [];
			if (originalCheckBox.data('on')) {
				labels[0] = originalCheckBox.data('on');
				labels[1] = originalCheckBox.data('off');
			} else
				labels = options.labels;
			var htmlStr = '<span class="tzCheckBox '
					+ (this.checked ? 'checked' : '')
					+ '"><span class="tzCBContent">'
					+ labels[this.checked ? 0 : 1]
					+ '</span><span class="tzCBPart"></span></span>';
			var checkBox = $(htmlStr);
			checkBox.insertAfter(originalCheckBox.hide());//
			if ($(this).attr('disabled') != 'disabled') {
				checkBox.click(function() {
					if(!originalCheckBox.hasClass("disabled")){
						checkBox.toggleClass('checked');
						var isChecked = checkBox.hasClass('checked');
						originalCheckBox.attr('checked', isChecked);
						checkBox.find('.tzCBContent').html(
								labels[isChecked ? 0 : 1]);
					}
				});
				originalCheckBox.bind('change', function() {
					checkBox.click();
				});
			}
		});
	};

	/**
	 * 复选框
	 */
	$.fn.ldCheckbox = function(options) {
		this
				.each(function() {
					var self = $(this);
					$("<label></label>").insertAfter(this);
					$('+label', self).each(function() {
						$(this).addClass('unchecked');// 未选中
						var isDisabled = $(this).prev().is(':disabled');
						var isChecked = $(this).prev().is(':checked');
						if (!isDisabled && isChecked) {// 选中
							$(this).addClass("checked");
						}
						if (isDisabled && isChecked) {// 选中禁用
							$(this).addClass("checkDis");
						}
						if (isDisabled && !isChecked) {// 禁用未选中
							$(this).addClass("uncheckDis");
						}
					});
					if ($(this).hasClass("all")) {
						$(this).next().click(function() {
							var isChecked = $(this).hasClass("checked");
							if (!isChecked) {
								$(this).removeClass('unchecked').addClass("checked");
							} else {
								$(this).removeClass('checked').addClass("unchecked");
							}
							$("[name="+ $(this).prev().attr("name").replace("All","")+ "]:not(:disabled)").each(function() {
									$(this).attr("checked",!isChecked);
									if (!isChecked) {
										$(this).next().removeClass('unchecked').addClass("checked");
									} else {
										$(this).next().removeClass('checked').addClass("unchecked");
									}
								});
							}).prev().hide();
					} else {
						$(this).next().click(function(event) {
							if (!$(this).prev().is(':disabled')) {
								if ($(this).prev().is(':checked')) {
									$(this).removeClass('checked').addClass("unchecked");
									$(this).prev().attr("checked", false);
								} else {
									$(this).removeClass('unchecked').addClass("checked");
									$(this).prev().attr("checked", true);
								}
							}
							var name = $(this).prev().attr("name");
							name = name.replaceAll(".","\\.");
							if ($("[name="+ name+ "]:not(:disabled):checked").length < $("[name="+ name + "]:not(:disabled)").length) {
								$("[name=" + name + "All]").next().removeClass('checked').addClass("unchecked");
							} else {
								$("[name=" + name + "All]").next().removeClass('unchecked').addClass("checked");
							}}).prev().hide();
					};
				});
		this.each(function() {
			if ($(this).hasClass("all")) {
				if ($(this).is(":checked")) {
					$(this).next().click().click();
				}
			}
		});
	};

	/**
	 * 单选框
	 */
	$.fn.ldRadio = function(options) {
		var self = this;
		return $(':radio+label', this).each(function() {
			$(this).addClass('hRadio');
			if ($(this).prev().is("checked"))
				$(this).addClass('hRadio_Checked');
		}).click(function(event) {
			$(this).siblings().removeClass("hRadio_Checked");
			if (!$(this).prev().is(':checked')) {
				$(this).addClass("hRadio_Checked");
				$(this).prev()[0].checked = true;
			}

			event.stopPropagation();
		}).prev().hide();
	}

})(jQuery);

var returnValue;
var maskId;
var ldDialogTop = art.dialog.top;
var ldDialog = new Object();
var ldCheckbox = new Object();
var ldRadio = new Object();
var ldSwitch = new Object();

/**
 * 警告
 * 
 * @param {String}
 *            消息内容
 * @param {String}
 *            图标 question、succeed、warning、error
 * @param {Function}
 *            确定按钮回调函数
 */
ldDialog.alert = ldDialogTop.art.dialog.alert;

/**
 * 确认
 * 
 * @param {String}
 *            消息内容
 * @param {Function}
 *            确定按钮回调函数
 * @param {Function}
 *            取消按钮回调函数
 */
ldDialog.confirm = function(content, yes, no) {
	var confirm = ldDialogTop.art.dialog.confirm(content, yes, no);
	confirm.shake && confirm.shake();// 调用抖动接口
};

/**
 * 提问
 * 
 * @param {String}
 *            提问内容
 * @param {Function}
 *            回调函数. 接收参数：输入值
 * @param {String}
 *            默认值
 */
ldDialog.prompt = ldDialogTop.art.dialog.prompt;

/**
 * 短暂提示
 * 
 * @param {String}
 *            提示内容
 * @param {Number}
 *            显示时间 (默认1.5秒)
 */
ldDialog.tips = ldDialogTop.art.dialog.tips;

/**
 * 右下角滑动通知
 * 
 * @param {String}
 *            通知标题
 * @param {String}
 *            通知内容
 * @param {String}
 *            必须指定一个像素宽度值或者百分比，否则浏览器窗口改变可能导致artDialog收缩
 * @param {String}
 *            图标 face-smile、face-sad
 * @param {String}
 *            显示时间 (默认5秒)
 */
ldDialog.notice = function(title, content, width, icon, time) {
	ldDialogTop.art.dialog.notice({
		title : title,
		content : content,
		width : width || 220,
		icon : icon || 'face-smile',
		time : time || 5
	})
};

/**
 * 关闭当前
 * 
 * @param {Object}
 *            返回值
 */
ldDialog.close = function(value) {
	returnValue = value;
	var api = art.dialog.open.api;
	api && api.close();
};

/**
 * 打开窗口
 * 
 * @param {String}
 *            链接地址
 * @param {String}
 *            标题
 * @param {String}
 *            窗口关闭回调函数
 * @param {String}
 *            宽度
 * @param {String}
 *            高度
 * @param {String}
 * @param {String}
 */
ldDialog.open = function(url, title, oncloseFn, width, height, top, left) {
	var options = {
		title : title,
		lock : true,
		opacity : .3,
		ok : false,
		width : width || "850px",
		height : height || "500px",
		left : left,
		top : top,
		fixed : true,
		close : oncloseFn
	};
	ldDialogTop.art.dialog.open(url, options, false);
};

ldDialog.openSmall = function(url, title, oncloseFn) {
	ldDialog.open(url, title, oncloseFn, "700px", "400px");
};
ldDialog.openMini = function(url, title, oncloseFn) {
	ldDialog.open(url, title, oncloseFn, "400px", "440px");
};
ldDialog.openMedium = function(url, title, oncloseFn) {
	ldDialog.open(url, title, oncloseFn, "850px", "500px");
};

ldDialog.openLarge = function(url, title, oncloseFn) {
	ldDialog.open(url, title, oncloseFn, "1000px", "600px");
};

ldDialog.openFullScreen = function(url, title, oncloseFn) {
	ldDialog.open(url, title, oncloseFn, "100%", "100%", "0%", "0%");
};

/**
 * 加载遮罩
 * 
 * @param {String}
 *            显示的文字
 */
ldDialog.mask = function(text) {
	maskId = layer.load(text || "正在加载中...");
}

/**
 * 取消加载遮罩
 */
ldDialog.unmask = function() {
	layer.close(maskId);
}

/**
 * 设置复选框选中
 * 
 * @param {String}
 *            选择器
 * @param {String}
 *            是否选中
 */
ldCheckbox.checked = function(selector, isChecked) {
	$(selector).each(function() {
		var self = $(this);
		if (!self.is(":disabled")) {// 非禁用状态
			self.attr("checked", isChecked);
			if (isChecked) {
				self.next().removeClass('unchecked').addClass("checked");
			} else {
				self.next().removeClass('checked').addClass("unchecked");
			}
		}
	});
};

/**
 * 设置复选框禁用/启用
 * 
 * @param {String}
 *            选择器
 * @param {String}
 *            是否禁用
 */
ldCheckbox.disabled = function(selector, isDisabled) {
	$(selector).each(function() {
		$(this).attr("disabled", isDisabled);
		var isChecked =  $(this).is(":checked");//是否选中
		if(isDisabled){
			$(this).next().removeClass().addClass(isChecked ? "checked checkDis" : "unchecked uncheckDis");
		}else{
			$(this).next().removeClass().addClass(isChecked ? "unchecked checked" : "unchecked");
		}
	});
};

/**
 * 开关打开
 * 
 * @param {String}
 *            选择器
 */
ldSwitch.on = function(selector){
	$(selector).each(function() {
		var ls = $(this).next();
		if(ls.hasClass("tzCheckBox") && !ls.hasClass("checked")){
			$(ls).click();
		}
	});
};

/**
 * 开关关闭
 * 
 * @param {String}
 *            选择器
 */
ldSwitch.off = function(selector){
	$(selector).each(function() {
		var ls = $(this).next();
		if(ls.hasClass("tzCheckBox") && ls.hasClass("checked")){
			$(ls).click();
		}
	});
};

/**
 * 开关禁用/启用
 * 
 * @param {String}
 *            选择器
 * @param {String}
 *            是否禁用           
 */
ldSwitch.disabled = function(selector,isDisabled){
	$(selector).each(function() {
		$(this).attr("disabled", isDisabled);
		if(isDisabled){//禁用
			$(this).addClass("disabled");
		}else{
			$(this).removeClass("disabled");
		}
	});
};

$(function() {
	try {
		addTableCss(); // 对表格样式进行样式添加
		initSeclet(); // 初始化下拉框
		// initDate(); //初始化日期控件
		initCheckboxAndRadio();// 初始化单选复选框
	} catch (e) {
		// TODO: handle exception
	}
});

var addTableCss = function() {
	// 表格单双行颜色设定
	$(".ld-datagrid tr:even").addClass("evenTrBgColor");
	$(".ld-datagrid tr:odd").addClass("oddTrBgColor");
	// 行选中颜色设定
	$(".ld-datagrid tr:gt(0)").click(function() {
		$(".ld-datagrid tr:gt(0)").attr("style", "cursor:;background-color:;");
		$(this).attr("style", "cursor:pointer;background-color: #fef5e1;");
	});
};

var initSeclet = function() {
	var config = {
		'.ldSelect' : {
			disable_search : true,
			allow_single_deselect : true,
			placeholder_text_multiple : "请选择...",
			placeholder_text_single : "请选择..."
		},
		'.ldSearchSelect' : {
			disable_search : false,
			allow_single_deselect : true,
			placeholder_text_multiple : "请选择...",
			placeholder_text_single : "请选择...",
			no_results_text : "没有匹配结果:"
		}
	}
	for ( var selector in config) {
		$(selector).chosen(config[selector]);
	}
};

var initDate = function() {
	$('.ldDate').focus(function() {
		WdatePicker({
			skin : 'twoer',
			dateFmt : 'yyyy-MM-dd'
		});
	});
	$('.ldDateTime').focus(function() {
		WdatePicker({
			skin : 'twoer',
			dateFmt : 'yyyy-MM-dd HH:mm:ss'
		});
	});
}

var initCheckboxAndRadio = function() {
	var aaa = $(".ldSwitch").ldSwitch({
		labels : [ '有效', '无效' ]
	});

	$(aaa[0]).text();

	$(".ldCheckbox").ldCheckbox();
	// $(".hradio").hradio();
}
