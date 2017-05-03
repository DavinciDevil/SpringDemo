package com.landicorp.core.service;

import com.alipay.model.builder.AlipayTradeCancelRequestBuilder;
import com.alipay.model.builder.AlipayTradePayRequestBuilder;
import com.alipay.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.model.builder.AlipayTradeQueryRequestBuilder;
import com.alipay.model.builder.AlipayTradeRefundRequestBuilder;
import com.alipay.model.result.AlipayF2FCancelResult;
import com.alipay.model.result.AlipayF2FPayResult;
import com.alipay.model.result.AlipayF2FPrecreateResult;
import com.alipay.model.result.AlipayF2FQueryResult;
import com.alipay.model.result.AlipayF2FRefundResult;


public interface IAliPayService {
	
	 // 当面付2.0流程支付
    public AlipayF2FPayResult tradePay(AlipayTradePayRequestBuilder builder);

    // 当面付2.0消费查询
    public AlipayF2FQueryResult queryTradeResult(AlipayTradeQueryRequestBuilder builder);

    // 当面付2.0消费退款
    public AlipayF2FRefundResult tradeRefund(AlipayTradeRefundRequestBuilder builder);

    // 当面付2.0预下单(生成二维码)
    public AlipayF2FPrecreateResult tradePrecreate(AlipayTradePrecreateRequestBuilder builder);

    // 当面付2.0消费撤销
	/**
	 * 支付交易返回失败或支付系统超时，调用该接口撤销交易。如果此订单用户支付失败，支付宝系统会将此订单关闭；
	 * 如果用户支付成功，支付宝系统会将此订单资金退还给用户。 
	 * 注意：只有发生支付系统超时或者支付结果未知时可调用撤销，其他正常支付的单如需实现相同功能请调用申请退款API。
	 * 提交支付交易后调用【查询订单API】，没有明确的支付结果再调用【撤销订单API】。
	 * @param builder
	 * @return
	 */
	public AlipayF2FCancelResult tradeCancel1(AlipayTradeCancelRequestBuilder builder);
	
	public void ali_notify() throws Exception;



}
