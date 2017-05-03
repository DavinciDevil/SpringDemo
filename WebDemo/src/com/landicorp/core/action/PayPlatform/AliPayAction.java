package com.landicorp.core.action.PayPlatform;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;

import com.alipay.api.AlipayResponse;
import com.alipay.api.request.AlipayDataBillDownloadurlGetRequest;
import com.alipay.api.response.AlipayTradeCancelResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.alipay.model.ExtendParams;
import com.alipay.model.GoodsDetail;
import com.alipay.model.builder.AlipayTradeCancelRequestBuilder;
import com.alipay.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.model.builder.AlipayTradeQueryRequestBuilder;
import com.alipay.model.builder.AlipayTradeRefundRequestBuilder;
import com.alipay.model.result.AlipayF2FCancelResult;
import com.alipay.model.result.AlipayF2FPrecreateResult;
import com.alipay.model.result.AlipayF2FQueryResult;
import com.alipay.model.result.AlipayF2FRefundResult;
import com.landicorp.core.action.BaseActionSupport;
import com.landicorp.core.service.IAliPayService;
import com.landicorp.core.service.IWeChatPayService;
import com.landicorp.core.utils.QrcodeUtil;

public class AliPayAction extends BaseActionSupport {

	private boolean flag;
	private IAliPayService aliPayService;

	public String tradePrecreate() {
		System.out.println("alipay tradePrecreate");
//		Configs.init("/com/landicorp/config/envconf.properties");
        // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
        // 需保证商户系统端不能重复，建议通过数据库sequence生成，
        String outTradeNo = "tradeprecreate" + System.currentTimeMillis()
                            + (long) (Math.random() * 10000000L);
		//String outTradeNo = "tradeprecreate";
        // (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
        String subject = "林春成支付宝下单测试";

        // (必填) 订单总金额，单位为元，不能超过1亿元
        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
        String totalAmount = "0.01";

        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
        // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
        String undiscountableAmount = "0";

        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = "";

        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
        String body = "body";

        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        String operatorId = "test_operator_id";

        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        String storeId = "test_store_id";

        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId("2088100200300400500");
        
        // 支付超时，定义为120分钟
        String timeoutExpress = "120m";

        // 商品明细列表，需填写购买商品详细信息，
        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();
        // 创建一个商品信息，参数含义分别为商品id（使用国标）、名称、单价（单位为分）、数量，如果需要添加商品类别，详见GoodsDetail
        GoodsDetail goods1 = GoodsDetail.newInstance("lccgoods_id001", "xxxxiaomianbao", 1000, 1);
        // 创建好一个商品后添加至商品明细列表
        goodsDetailList.add(goods1);

        // 继续创建并添加第一条商品信息，用户购买的产品为“黑人牙刷”，单价为5.00元，购买了两件
        GoodsDetail goods2 = GoodsDetail.newInstance("goods_id002", "xxx", 500, 2);
        goodsDetailList.add(goods2);

        // 创建扫码支付请求builder，设置请求参数
        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
            .setSubject(subject).setTotalAmount(totalAmount).setOutTradeNo(outTradeNo)
            .setUndiscountableAmount(undiscountableAmount).setSellerId(sellerId).setBody(body)
            .setOperatorId(operatorId).setStoreId(storeId).setExtendParams(extendParams)
            .setTimeoutExpress(timeoutExpress)
            //                .setNotifyUrl("http://www.test-notify-url.com")//支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
            .setGoodsDetailList(goodsDetailList)
            ;
        String notifyUrl = "http://16c9357e88.imwork.net/" + "PayDemo/lcc/alipay!aliNotify.action";
        builder.setNotifyUrl(notifyUrl);
        AlipayF2FPrecreateResult result = aliPayService.tradePrecreate(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
               System.out.println("支付宝预下单成功: )");

                AlipayTradePrecreateResponse response = result.getResponse();
                dumpResponse(response);

//                // 需要修改为运行机器上的路径
//                String filePath = String.format("C:\\Users\\Administrator\\Desktop\\abc.png",
//                    response.getOutTradeNo());
//                System.out.println("filePath:" + filePath);
//                ZxingUtils.getQRCodeImge(response.getQrCode(), 256, filePath);
                String path = ServletActionContext.getRequest().getSession()
    					.getServletContext().getRealPath("/")
    					+ "/" + "qrCode";

    			flag = QrcodeUtil.createQrcode(response.getQrCode(), path, "QrCode");
                break;

            case FAILED:
                System.out.println("支付宝预下单失败!!!");
                break;

            case UNKNOWN:
                System.out.println("系统异常，预下单状态未知!!!");
                break;

            default:
                System.out.println("不支持的交易状态，交易返回异常!!!");
                break;
        }
		return SUCCESS;
	}
	/**
	 * 查询订单支付是否成功
	 * @return
	 */
	public String  queryTradeResult(){
		System.out.println("alipay queryTradeResult");
        // 	订单支付时传入的商户订单号,和支付宝交易号不能同时为空。 trade_no,out_trade_no如果同时存在优先取trade_no
        String outTradeNo = "tradeprecreate14937377903297760867";

        // 支付宝交易号，和商户订单号不能同时为空
        String trade_no = "";


        // 创建扫码支付请求builder，设置请求参数
        AlipayTradeQueryRequestBuilder builder = new AlipayTradeQueryRequestBuilder()
            .setOutTradeNo(outTradeNo)
            ;
            AlipayF2FQueryResult result = aliPayService.queryTradeResult(builder);
            switch (result.getTradeStatus()) {//查询是否支付成功
            case SUCCESS:
               System.out.println("支付宝订单支付成功: )");

               AlipayTradeQueryResponse response = result.getResponse();
                dumpResponse(response);
                break;

            case FAILED:
                System.out.println("支付宝订单支付失败!!!");
                break;

            case UNKNOWN:
                System.out.println("系统异常，订单支付状态未知!!!");
                break;

            default:
                System.out.println("不支持的交易状态，交易返回异常!!!");
                break;
        }
    
		return SUCCESS;
	}
	
	public String tradeRefund(){
		System.out.println("alipay tradeRefund");
        // 	订单支付时传入的商户订单号,和支付宝交易号不能同时为空。 trade_no,out_trade_no如果同时存在优先取trade_no
        String outTradeNo = "tradeprecreate14924121955963249145";

        // 支付宝交易号，和商户订单号不能同时为空
        String trade_no = "";
        String refund_amount = "0.01";//必须	需要退款的金额，该金额不能大于订单金额,单位为元，支持两位小数	200.12
        String refund_reason	= "正常退款";//	必须		退款的原因说明	正常退款(内容任意，不能为空就行)
        String  out_request_no	= "";//	可选		标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传。	HZ01RF001
        String store_id	= "";//	可选		商户的门店编号	NJ_S_001
        String terminal_id = ""	;//	可选		商户的终端编号	NJ_T_001

        // 创建扫码支付请求builder，设置请求参数
        AlipayTradeRefundRequestBuilder builder = new AlipayTradeRefundRequestBuilder()
            .setOutTradeNo(outTradeNo)
            .setRefundAmount(refund_amount)
            .setRefundReason(refund_reason)
            .setOutRequestNo(out_request_no)
            .setStoreId(store_id)
            .setTerminalId(terminal_id);
            ;
        AlipayF2FRefundResult result = aliPayService.tradeRefund(builder);
            switch (result.getTradeStatus()) {//查询是否支付成功
            case SUCCESS:
               System.out.println("支付宝订单退款成功: )");

                AlipayTradeRefundResponse response = result.getResponse();
                dumpResponse(response);
                break;

            case FAILED:
                System.out.println("支付宝订单退款失败!!!");
                break;

            case UNKNOWN:
                System.out.println("系统异常，订单支付退款未知!!!");
                break;

            default:
                System.out.println("不支持的交易状态，交易返回异常!!!");
                break;
        }
    
		return SUCCESS;
	}
	
	public String tradeCancel(){
		System.out.println("alipay tradeCancel");
        // 	订单支付时传入的商户订单号,和支付宝交易号不能同时为空。 trade_no,out_trade_no如果同时存在优先取trade_no
        String outTradeNo = "tradeprecreate14937787590131167205";

        // 支付宝交易号，和商户订单号不能同时为空
        String trade_no = "";

        // 创建扫码支付请求builder，设置请求参数
        AlipayTradeCancelRequestBuilder builder = new AlipayTradeCancelRequestBuilder()
            .setOutTradeNo(outTradeNo)
            ;
        AlipayF2FCancelResult result = aliPayService.tradeCancel1(builder);
            switch (result.getTradeStatus()) {//查询是否支付成功
            case SUCCESS:
               System.out.println("支付宝订单撤销成功: )");

                AlipayTradeCancelResponse response = result.getResponse();
                dumpResponse(response);
                break;

            case FAILED:
                System.out.println("支付宝订单撤销失败!!!");
                break;

            case UNKNOWN:
                System.out.println("系统异常，订单支付撤销未知!!!");
                break;

            default:
                System.out.println("不支持的交易状态，交易返回异常!!!");
                break;
        }
    
		return SUCCESS;
	}
	
	public String aliNotify(){
		System.out.println("支付宝返回");
		try {
			aliPayService.ali_notify();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private void dumpResponse(AlipayResponse response) {
        if (response != null) {
            System.out.println(String.format("code:%s, msg:%s", response.getCode(), response.getMsg()));
            if (StringUtils.isNotEmpty(response.getSubCode())) {
                System.out.println(String.format("subCode:%s, subMsg:%s", response.getSubCode(),
                    response.getSubMsg()));
            }
            System.out.println("body:" + response.getBody());
        }
    }
	
	
	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public void setAliPayService(IAliPayService aliPayService) {
		this.aliPayService = aliPayService;
	}
}
