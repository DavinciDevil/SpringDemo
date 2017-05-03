package com.landicorp.core.service.impl;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;

import com.landicorp.core.helper.CommonValue;
import com.landicorp.core.helper.Signature;
import com.landicorp.core.service.IWeChatPayService;
import com.landicorp.core.utils.HttpUtil;
import com.landicorp.core.utils.HttpsUtil;
import com.landicorp.core.utils.SSLSocketUtil;
import com.landicorp.core.utils.XMLUtil;

public class WeChatPayServiceImpl implements IWeChatPayService {

	@Override
	public String createPayment(String url, String xml) {
		String resXml = HttpUtil.postData(url, xml, null);
		return resXml;
	}

	@Override
	public String payNative2() {
		System.out.println("生成订单");
		// TODO 生成订单
		String outTradeNo = "wxPayTest" + System.currentTimeMillis()
                + (long) (Math.random() * 10000000L);
		// 生成订单结束

		// 调用统一下单
		String qrCodeUrl = "";
		HttpServletRequest request = ServletActionContext.getRequest();
		StringBuilder addr = new StringBuilder("http://");
		addr.append(request.getServerName());// 服务器IP地址
		
		if (request.getServerPort() != 80) {
			addr.append(":" + request.getServerPort());// 端口号
		}
		addr.append(request.getContextPath() + "/");// 项目名
		String notifyUrl = "http://16c9357e88.imwork.net/" + "PayDemo/lcc/wxpay!wxNotify.action";
		SortedMap<String, Object> postDate = new TreeMap<String, Object>();
		// 生成订单所需参数
		postDate.put("appid", CommonValue.getWxAppid());// 微信分配的公众账号 ID
		postDate.put("mch_id", CommonValue.getWxMchid());// 微信支付分配的商户号
		postDate.put("nonce_str", "linchuncheng");// 随机字符串，随便填，String(32)
		postDate.put("body", "林春成支付测试");// 商品描述，String(32)
		postDate.put("notify_url",notifyUrl);// 接收微信支付异步通知回调地址
		postDate.put("trade_type", "NATIVE");// 交易类型
		postDate.put("total_fee", "1");// 总费用，整数，单位：分
		postDate.put("product_id", "123456");// 商品id，商户自行定义
		postDate.put("out_trade_no", outTradeNo);// 商户系统内部的订单号,商户自行定义
		postDate.put("spbill_create_ip", "110.87.189.113");// 提交订单的IP（本地ip）
		// 生成签名
		String sign = "";
		sign = Signature.getSign(postDate, CommonValue.getWxKey());
		postDate.put("sign", sign);

		// 将postDate转化成xml
		String xml = XMLUtil.parseToXML(postDate);
		System.out.println("reqXml="+xml);

		String resXml = "";
		resXml = createPayment(CommonValue.getWxGateUrl(),xml);
		System.out.println("resXml="+resXml);
		// 处理结果
		try {
			Document doc = DocumentHelper.parseText(resXml);
			if ("FAIL".equals(doc.getRootElement().element("return_code")
					.getText())) {
				System.out.println(doc.getRootElement().element("return_msg")
						.getText());
			} else if ("SUCCESS".equals(doc.getRootElement()
					.element("return_code").getText())) {
				if ("SUCCESS".equals(doc.selectSingleNode("/xml/result_code")
						.getText())) {
					qrCodeUrl = doc.selectSingleNode("/xml/code_url").getText();
				}else if ("FAIL".equals(doc.selectSingleNode("/xml/result_code").getText())) {
					System.out.println("err_code:" + doc.selectSingleNode("/xml/err_code").getText());
					System.out.println("err_code_des:" + doc.selectSingleNode("/xml/err_code_des").getText());
				}
			}
			return qrCodeUrl;
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void weixin_notify() throws Exception{  
		System.out.println("微信返回");
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        //读取参数  
        InputStream inputStream ;  
        StringBuffer sb = new StringBuffer();  
        inputStream = request.getInputStream();  
        String s ;  
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));  
        while ((s = in.readLine()) != null){  
            sb.append(s);  
        }  
        in.close();  
        inputStream.close();  
  
        //解析xml成map  
        SortedMap<String, Object> m = new TreeMap<String, Object>();
        m = XMLUtil.doXMLParse(sb.toString());  
          
        //过滤空 设置 TreeMap  
        SortedMap<String,Object> packageParams = new TreeMap<String,Object>();        
        Iterator it = m.keySet().iterator();  
        while (it.hasNext()) {  
            String parameter = (String) it.next();  
            Object parameterValue = m.get(parameter);  
              
            String v = "";  
            if(null != parameterValue) {  
                v = parameterValue.toString().trim();  
            }  
            packageParams.put(parameter, v);
            System.out.println(parameter +"="+v);
        }  
          
        // 账号信息  
        String key = CommonValue.getWxKey(); // key  
  
        //判断签名是否正确  
        if(Signature.isWxSign("UTF-8", packageParams)) {  
            //------------------------------  
            //处理业务开始  
            //------------------------------  
            String resXml = "";  
            if("SUCCESS".equals((String)packageParams.get("result_code"))){  
                // 这里是支付成功  
                //////////执行自己的业务逻辑////////////////  
                String mch_id = (String)packageParams.get("mch_id");  
                String openid = (String)packageParams.get("openid");  
                String is_subscribe = (String)packageParams.get("is_subscribe");  
                String out_trade_no = (String)packageParams.get("out_trade_no");  
                  
                String total_fee = (String)packageParams.get("total_fee");  
                  
                System.out.println("mch_id:"+mch_id);  
                System.out.println("openid:"+openid);  
                System.out.println("is_subscribe:"+is_subscribe);  
                System.out.println("out_trade_no:"+out_trade_no);  
                System.out.println("total_fee:"+total_fee);  
                  
                //////////执行自己的业务逻辑////////////////  
                  
                System.out.println("支付成功");  
                //通知微信.异步确认成功.必写.不然会一直通知后台.八次之后就认为交易失败了.  
                resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"  
                        + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";  
                  
            } else {  
            	System.out.println("支付失败,错误信息：" + packageParams.get("err_code"));  
                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"  
                        + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";  
            }  
            //------------------------------  
            //处理业务完毕  
            //------------------------------  
            BufferedOutputStream out = new BufferedOutputStream(  
                    response.getOutputStream());  
            out.write(resXml.getBytes());  
            out.flush();  
            out.close();  
        } else{  
        	System.out.println("通知签名验证失败");  
        }  
          
    }

	@Override
	public void orderQuery() throws Exception {
		System.out.println("订单查询");
		// TODO 生成订单
		String outTradeNo = "wxPayTest14936913339665975799";//商户订单号

		// 调用统一下单
		String qrCodeUrl = "";
		SortedMap<String, Object> postDate = new TreeMap<String, Object>();
		// 生成订单所需参数
		postDate.put("appid", CommonValue.getWxAppid());// 微信分配的公众账号 ID
		postDate.put("mch_id", CommonValue.getWxMchid());// 微信支付分配的商户号
		postDate.put("nonce_str", "linchuncheng");// 随机字符串，随便填，String(32)
		postDate.put("out_trade_no", outTradeNo);// 商户系统内部的订单号,商户自行定义
		// 生成签名
		String sign = "";
		sign = Signature.getSign(postDate, CommonValue.getWxKey());
		postDate.put("sign", sign);

		// 将postDate转化成xml
		String xml = XMLUtil.parseToXML(postDate);
		System.out.println("reqXml="+xml);

		String resXml = "";
		resXml = createPayment(CommonValue.getWxOrderQuery(),xml);
		System.out.println("resXml="+resXml);
		SortedMap<String, Object> m = new TreeMap<String, Object>();
        m = XMLUtil.doXMLParse(resXml);  
        //过滤空 设置 TreeMap  
        SortedMap<String,Object> packageParams = new TreeMap<String,Object>();        
        Iterator it = m.keySet().iterator();  
        while (it.hasNext()) {  
            String parameter = (String) it.next();  
            Object parameterValue = m.get(parameter);  
              
            String v = "";  
            if(null != parameterValue) {  
                v = parameterValue.toString().trim();  
            }  
            packageParams.put(parameter, v); 
            System.out.println(parameter +"="+v);
        }  
          
        // 账号信息  
        String key = CommonValue.getWxKey(); // key  
  
        //判断签名是否正确  
        if(Signature.isWxSign("UTF-8", packageParams)) {  
            //------------------------------  
            //处理业务开始  
            //------------------------------  
            if("SUCCESS".equals((String)packageParams.get("result_code"))){  
                // 这里是支付成功  
                //////////执行自己的业务逻辑////////////////  
                String mch_id = (String)packageParams.get("mch_id");  
                String openid = (String)packageParams.get("openid");  
                String is_subscribe = (String)packageParams.get("is_subscribe");  
                String out_trade_no = (String)packageParams.get("out_trade_no");  
                  
                String total_fee = (String)packageParams.get("total_fee");  
                  
                System.out.println("mch_id:"+mch_id);  
                System.out.println("openid:"+openid);  
                System.out.println("is_subscribe:"+is_subscribe);  
                System.out.println("out_trade_no:"+out_trade_no);  
                System.out.println("total_fee:"+total_fee);  
                  
                //////////执行自己的业务逻辑////////////////  
                  
                System.out.println("订单查询成功");  
            } else {  
            	System.out.println("订单查询失败,错误信息：" + packageParams.get("err_code"));  
            }  
            //------------------------------  
            //处理业务完毕  
            //------------------------------  
        } else{  
        	System.out.println("通知签名验证失败");  
        }  
	}

	@Override
	public void RefundQuery() throws Exception {
		System.out.println("退款查询");
		// TODO 生成订单
		String outTradeNo = "wxPayTest14936913339665975799";//商户订单号

		// 调用统一下单
		SortedMap<String, Object> postDate = new TreeMap<String, Object>();
		// 生成订单所需参数
		postDate.put("appid", CommonValue.getWxAppid());// 微信分配的公众账号 ID
		postDate.put("mch_id", CommonValue.getWxMchid());// 微信支付分配的商户号
		postDate.put("nonce_str", "linchuncheng");// 随机字符串，随便填，String(32)
		postDate.put("out_trade_no", outTradeNo);// 商户系统内部的订单号,商户自行定义
		// 生成签名
		String sign = "";
		sign = Signature.getSign(postDate, CommonValue.getWxKey());
		postDate.put("sign", sign);

		// 将postDate转化成xml
		String xml = XMLUtil.parseToXML(postDate);
		System.out.println("reqXml="+xml);

		String resXml = "";
		resXml = createPayment(CommonValue.getWxRefundQuery(),xml);
		System.out.println("resXml="+resXml);
		SortedMap<String, Object> m = new TreeMap<String, Object>();
        m = XMLUtil.doXMLParse(resXml);  
        //过滤空 设置 TreeMap  
        SortedMap<String,Object> packageParams = new TreeMap<String,Object>();        
        Iterator it = m.keySet().iterator();  
        while (it.hasNext()) {  
            String parameter = (String) it.next();  
            Object parameterValue = m.get(parameter);  
              
            String v = "";  
            if(null != parameterValue) {  
                v = parameterValue.toString().trim();  
            }  
            packageParams.put(parameter, v); 
            System.out.println(parameter +"="+v);
        }  
          
        // 账号信息  
        String key = CommonValue.getWxKey(); // key  
  
        //判断签名是否正确  
        if(Signature.isWxSign("UTF-8", packageParams)) {  
            //------------------------------  
            //处理业务开始  
            //------------------------------  
            if("SUCCESS".equals((String)packageParams.get("result_code"))){  
                // 这里是支付成功  
                //////////执行自己的业务逻辑////////////////  
                String mch_id = (String)packageParams.get("mch_id");  
                String openid = (String)packageParams.get("openid");  
                String is_subscribe = (String)packageParams.get("is_subscribe");  
                String out_trade_no = (String)packageParams.get("out_trade_no");  
                  
                String total_fee = (String)packageParams.get("total_fee");  
                  
                System.out.println("mch_id:"+mch_id);  
                System.out.println("openid:"+openid);  
                System.out.println("is_subscribe:"+is_subscribe);  
                System.out.println("out_trade_no:"+out_trade_no);  
                System.out.println("total_fee:"+total_fee);  
                  
                //////////执行自己的业务逻辑////////////////  
                  
                System.out.println("退款查询成功");  
            } else {  
            	System.out.println("退款查询失败,错误信息：" + packageParams.get("err_code"));  
            }  
            //------------------------------  
            //处理业务完毕  
            //------------------------------  
        } else{  
        	System.out.println("通知签名验证失败");  
        }  
	}

	@Override
	public void Refund() throws Exception {
		System.out.println("退款");
		// TODO 生成订单
		String outTradeNo = "wxPayTest14936913339665975799";//商户订单号
		String out_refund_no = "1217752501201407033233368018";//商户系统内部的退款单号，商户系统内部唯一，只能是数字、大小写字母_-|*@ ，同一退款单号多次请求只退一笔。
		String total_fee = "1";//订单总金额，单位为分，只能为整数
		String refund_fee = "1";//退款金额
		String op_user_id = "00000000";//操作员帐号, 默认为商户号
		// 调用统一下单
		
		SortedMap<String, Object> postDate = new TreeMap<String, Object>();
		// 生成订单所需参数
		postDate.put("appid", CommonValue.getWxAppid());// 微信分配的公众账号 ID
		postDate.put("mch_id", CommonValue.getWxMchid());// 微信支付分配的商户号
		postDate.put("nonce_str", "linchuncheng");// 随机字符串，随便填，String(32)
		postDate.put("out_trade_no", outTradeNo);// 商户系统内部的订单号,商户自行定义
		postDate.put("out_refund_no", out_refund_no);
		postDate.put("total_fee", total_fee);
		postDate.put("refund_fee", refund_fee);
		postDate.put("op_user_id", op_user_id);
		// 生成签名
		String sign = "";
		sign = Signature.getSign(postDate, CommonValue.getWxKey());
		postDate.put("sign", sign);

		// 将postDate转化成xml
		String xml = XMLUtil.parseToXML(postDate);
		System.out.println("reqXml="+xml);

		String resXml = "";
		//resXml = createPayment(CommonValue.getWxRefund(),xml);
//		ClientCustomSSL.doPost(CommonValue.getWxRefund(),
//				this.getClass().getClassLoader().getResource("/").getPath()
//				+ "cert/piclient_cert.p12", 
//				"F:/Java/tomcat/tomcat-8.0.24/webapps/PayDemo/WEB-INF/classes/cert/piclient_cert.p12",
//				postDate, 
//				CommonValue.getDefaultEncoding());
		SSLContext context = SSLSocketUtil.getSSLContext("D:/cert/apiclient_key.pem","D:/cert/apiclient_cert.pem", 
				new String[] {"D:/cert/rootca.pem" });
		resXml = HttpsUtil.getResultByXml(CommonValue.getWxRefund(), xml, context);
		System.out.println("resXml="+resXml);
		SortedMap<String, Object> m = new TreeMap<String, Object>();
        m = XMLUtil.doXMLParse(resXml);  
        //过滤空 设置 TreeMap  
        SortedMap<String,Object> packageParams = new TreeMap<String,Object>();        
        Iterator it = m.keySet().iterator();  
        while (it.hasNext()) {  
            String parameter = (String) it.next();  
            Object parameterValue = m.get(parameter);  
              
            String v = "";  
            if(null != parameterValue) {  
                v = parameterValue.toString().trim();  
            }  
            packageParams.put(parameter, v);  
            System.out.println(parameter +"="+v);
        }  
          
        // 账号信息  
        String key = CommonValue.getWxKey(); // key  
  
        //判断签名是否正确  
        if(Signature.isWxSign("UTF-8", packageParams)) {  
            //------------------------------  
            //处理业务开始  
            //------------------------------  
            if("SUCCESS".equals((String)packageParams.get("result_code"))){  
                // 这里是支付成功  
                //////////执行自己的业务逻辑////////////////  
                String mch_id = (String)packageParams.get("mch_id");  
                String openid = (String)packageParams.get("openid");  
                String is_subscribe = (String)packageParams.get("is_subscribe");  
                String out_trade_no = (String)packageParams.get("out_trade_no");  
                  
                  
                System.out.println("mch_id:"+mch_id);  
                System.out.println("openid:"+openid);  
                System.out.println("is_subscribe:"+is_subscribe);  
                System.out.println("out_trade_no:"+out_trade_no);  
                  
                //////////执行自己的业务逻辑////////////////  
                  
                System.out.println("退款成功");  
            } else {  
            	System.out.println("退款失败,错误信息：" + packageParams.get("err_code"));  
            }  
            //------------------------------  
            //处理业务完毕  
            //------------------------------  
        } else{  
        	System.out.println("通知签名验证失败");  
        }  
	}

	@Override
	public void Closeorder() throws Exception {
		System.out.println("关闭订单");
		// TODO 生成订单
		String outTradeNo = "wxPayTest14936913339665975799";//商户订单号

		// 调用统一下单
		
		SortedMap<String, Object> postDate = new TreeMap<String, Object>();
		// 生成订单所需参数
		postDate.put("appid", CommonValue.getWxAppid());// 微信分配的公众账号 ID
		postDate.put("mch_id", CommonValue.getWxMchid());// 微信支付分配的商户号
		postDate.put("nonce_str", "linchuncheng");// 随机字符串，随便填，String(32)
		postDate.put("out_trade_no", outTradeNo);// 商户系统内部的订单号,商户自行定义
		// 生成签名
		String sign = "";
		sign = Signature.getSign(postDate, CommonValue.getWxKey());
		postDate.put("sign", sign);

		// 将postDate转化成xml
		String xml = XMLUtil.parseToXML(postDate);
		System.out.println("reqXml="+xml);

		String resXml = "";
		resXml = createPayment(CommonValue.getWxCloseOrder(),xml);
		System.out.println("resXml="+resXml);
		SortedMap<String, Object> m = new TreeMap<String, Object>();
        m = XMLUtil.doXMLParse(resXml);  
        //过滤空 设置 TreeMap  
        SortedMap<String,Object> packageParams = new TreeMap<String,Object>();        
        Iterator it = m.keySet().iterator();  
        while (it.hasNext()) {  
            String parameter = (String) it.next();  
            Object parameterValue = m.get(parameter);  
              
            String v = "";  
            if(null != parameterValue) {  
                v = parameterValue.toString().trim();  
            }  
            packageParams.put(parameter, v); 
            System.out.println(parameter +"="+v);
        }  
          
        // 账号信息  
        String key = CommonValue.getWxKey(); // key  
  
        //判断签名是否正确  
        if(Signature.isWxSign("UTF-8", packageParams)) {  
            //------------------------------  
            //处理业务开始  
            //------------------------------  
            if("SUCCESS".equals((String)packageParams.get("result_code"))){  
                // 这里是支付成功  
                //////////执行自己的业务逻辑////////////////  
                String mch_id = (String)packageParams.get("mch_id");  
                String openid = (String)packageParams.get("openid");  
                String is_subscribe = (String)packageParams.get("is_subscribe");  
                String out_trade_no = (String)packageParams.get("out_trade_no");  
                  
                String total_fee = (String)packageParams.get("total_fee");  
                  
                System.out.println("mch_id:"+mch_id);  
                System.out.println("openid:"+openid);  
                System.out.println("is_subscribe:"+is_subscribe);  
                System.out.println("out_trade_no:"+out_trade_no);  
                System.out.println("total_fee:"+total_fee);  
                  
                //////////执行自己的业务逻辑////////////////  
                  
                System.out.println("关闭订单成功");  
            } else {  
            	System.out.println("关闭订单失败,错误信息：" + packageParams.get("err_code"));  
            }  
            //------------------------------  
            //处理业务完毕  
            //------------------------------  
        } else{  
        	System.out.println("通知签名验证失败");  
        }  
	} 

}
