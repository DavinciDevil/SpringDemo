package com.landicorp.core.service;

import java.util.SortedMap;

public interface IWeChatPayService {
	
	/**
	 * 统一下单
	 * @return 微信客户端返回结果
	 */
	public String createPayment(String url, String xml);
	
	/**
	 * 原生支付模式2
	 * @return
	 */
	public String payNative2();
	
	/**
	 * 微信通知回调。
	 * @throws Exception
	 */
	public void weixin_notify() throws Exception;

	/**
	 * 订单查询
	 * @throws Exception
	 */
	public void orderQuery() throws Exception;

	/**
	 * 退款查询
	 * @throws Exception
	 */
	public void RefundQuery() throws Exception;

	/**
	 * 退款
	 * @throws Exception
	 */
	public void Refund() throws Exception;

	/**
	 * 关闭订单
	 * @throws Exception
	 */
	public void Closeorder() throws Exception;
}
