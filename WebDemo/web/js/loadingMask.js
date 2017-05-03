var showLoading = function(shade) {
		closeLoading();
		if (shade == null || shade) {
			$("#loading-mask").show();
		}
		$("#loading-content").show();
		$('body').css("overflow","hidden")
	};

	var closeLoading = function() {
		$("#loading-mask").hide();
		$("#loading-content").hide();
		$('body').css("overflow","auto")
	};