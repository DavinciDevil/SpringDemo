var readCardFlag = false;

function checkCardNumber(cardNumber) {
	cardNumber = cardNumber.trim();
	if (isNaN(cardNumber)) {
		alert('卡号必须为数字');
		return false;
	}
	if (cardNumber.length < 5 || cardNumber.length > 20) {
		alert('卡号必须为5-20位数字');
		return false;
	}
	return true;
}

function getRechargeCard() {
	var inputTxt = "32";
	var inputTxt1 = "000000000000";
	var inputTxt2 = "10000000000000000001";
	var inputTxt3 = "        ";
	var inputTxt4 = "        ";
	var RetCode = Trans.PosTrans(inputTxt, inputTxt1, inputTxt2, inputTxt3,
			inputTxt4);
	var resultcode = str_subbyte(RetCode, 6, 2); //返回码
	if (resultcode != "00") {
		if (resultcode == "D2") {
			alert("读卡器连接错误！");
			return "";
		}
		var resultinfo = str_subbyte(RetCode, 8, 40); //返回信息
		alert(resultinfo);
		return "";
	}
	var rechargeCard = str_subbyte(RetCode, 48, 20); //充值卡号
	return trim(rechargeCard);
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