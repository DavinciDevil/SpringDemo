package com.landicorp.core.action.PayPlatform;

import org.apache.struts2.ServletActionContext;

import com.landicorp.core.action.BaseActionSupport;
import com.landicorp.core.service.IWeChatPayService;
import com.landicorp.core.utils.QrcodeUtil;
import com.landicorp.core.utils.StringUtil;

public class WxPayAction extends BaseActionSupport {

	private boolean flag;
	private IWeChatPayService weChatPayService;
	
	
	
	/**
	 * @return
	 */
	public String payNative2() {
		String qrCodeUrl = weChatPayService.payNative2();
		//String qrCodeUrl = "";
		if (StringUtil.isEmpty(qrCodeUrl)) {
			flag = false;
		} else {
			String path = ServletActionContext.getRequest().getSession()
					.getServletContext().getRealPath("/")
					+ "/" + "qrCode";

			flag = QrcodeUtil.createQrcode(qrCodeUrl, path, "QrCode");
		}
		return SUCCESS;
	}

	public String wxOrderQuery(){
		try {
			weChatPayService.orderQuery();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public String wxCloseorder(){
		try {
			weChatPayService.Closeorder();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public String wxRefund(){
		try {
			weChatPayService.Refund();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public String wxRefundQuery(){
		try {
			weChatPayService.RefundQuery();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public String wxNotify(){
		try {
			weChatPayService.weixin_notify();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public void setWeChatPayService(IWeChatPayService weChatPayService) {
		this.weChatPayService = weChatPayService;
	}
}
