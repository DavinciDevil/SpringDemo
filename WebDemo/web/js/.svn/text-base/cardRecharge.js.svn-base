function cardRecharge(amount) {
	var resultObj = {};
	if (typeof amount != 'string') {
		resultObj.resultCode = "FF";
		resultObj.resultInfo = "金额必须为字符串";
	}
	if (amount.length != 12) {
		resultObj.resultCode = "FF";
		resultObj.resultInfo = "金额必须为12位数字字符串";
	}
	if (resultObj.resultInfo != undefined) {
		return resultObj;
	}
	var transType = "30";
	var czcard = "10000000000000000001";
	var date = '        ';
	var referenceNo = '        ';
	var result = Trans.PosTrans(transType, amount, czcard, date, referenceNo);
	//返回码
	resultObj.resultCode = str_subbyte(result, 6, 2);
	//返回信息
	resultObj.resultInfo = str_subbyte(result, 8, 40);
	if (resultObj.resultInfo.trim() == '') {
		if (resultObj.resultCode == 'D2') {
			resultObj.resultInfo = "与POS机交互失败";
		} else if (resultObj.resultCode != '00') {
			resultObj.resultInfo = "充值失败,错误代码:" + resultObj.resultCode;
		}
		if (resultObj.resultCode != '00') {
			return resultObj;
		}
	}
	resultObj.data = result;
	return resultObj;
}

function cardRechargeCancel(amount,date,referenceNo){
	var resultObj = {};
	if (typeof amount != 'string') {
		resultObj.resultCode = "FF";
		resultObj.resultInfo = "金额必须为字符串";
	}
	if (amount.length != 12) {
		resultObj.resultCode = "FF";
		resultObj.resultInfo = "金额必须为12位数字字符串";
	}
	if(typeof date!='string'){
		resultObj.resultCode = "FF";
		resultObj.resultInfo = "日期必须为字符串";
	}
	if(date.length!=8){
		resultObj.resultCode = "FF";
		resultObj.resultInfo = "日期必须为8位数字字符串";
	}
	if (typeof referenceNo != 'string') {
		resultObj.resultCode = "FF";
		resultObj.resultInfo = "原参考号必须为字符串";
	}
	if (amount.length != 12) {
		resultObj.resultCode = "FF";
		resultObj.resultInfo = "原参考号必须为12位数字字符串";
	}
	if (resultObj.resultInfo != undefined) {
		return resultObj;
	}
	var transType = "34";
	var czcard = "10000000000000000001";
	var result = Trans.PosTrans(transType, amount, czcard,date,referenceNo);
	//返回码
	resultObj.resultCode = str_subbyte(result, 6, 2);
	//返回信息
	resultObj.resultInfo = str_subbyte(result, 8, 40);
	if (resultObj.resultInfo.trim() == '') {
		if (resultObj.resultCode == 'D2') {
			resultObj.resultInfo = "与POS机交互失败";
		} else if (resultObj.resultCode != '00') {
			resultObj.resultInfo = "充值失败,错误代码:" + resultObj.resultCode;
		}
		if (resultObj.resultCode != '00') {
			return resultObj;
		}
	}
	resultObj.data = result;
	return resultObj;
}

function str_subbyte(str, baginByte, lenOfByte) {
	var b_char = 0;
	var b_byte = -1;
	for (b_char = 0; b_char <= baginByte; b_char++) {
		if (str.charCodeAt(b_char) > 255) {
			b_byte += 2;
		} else {
			b_byte++;
		}
		if (b_byte >= baginByte) {
			break;
		}
	}
	//-----------------------------------------------------
	var i = 0;
	var numofbyte = 0;
	var temp = "";
	for (i = b_char; i < str.length; i++) {
		if (str.charCodeAt(i) > 255) {
			numofbyte += 2;
		} else {
			numofbyte++;
		}
		temp += str.charAt(i);
		if (numofbyte == lenOfByte) {
			return temp;
		}
	}
	return temp;
}

function substr(str, len){
	if( ! str || ! len){
		return '';
	}
	// 预期计数：中文2字节，英文1字节
	var a = 0;
	// 循环计数
	var i = 0;
	// 临时字串
	var temp = '';
	for (i = 0; i < str.length; i ++ ){
		if (str.charCodeAt(i) > 255){
			// 按照预期计数增加2
			a += 2;
		} else {
			a ++ ;
		}
		// 如果增加计数后长度大于限定长度，就直接返回临时字符串
		if(a > len) {
			return temp;
		}
		// 将当前内容加到临时字符串
		temp += str.charAt(i);
	}
	// 如果全部是单字节字符，就直接返回源字符串
	return str;
}        