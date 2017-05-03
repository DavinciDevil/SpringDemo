package com.landicorp.core.helper;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;


public class CommonValue {
	private static Configuration configs;
    
	private static String defaultEncoding ; 
	//微信参数
	private static String wxAppid;//微信公众号id
	private static String wxAppsecret;//微信Appsecret
	private static String wxMchid;//微信商户号id
	private static String wxKey;//微信秘钥
	
	private static String wxGateUrl;//统一下单地址
	private static String wxOrderQuery;//订单查询
	private static String wxCloseOrder;//关闭订单
	private static String wxRefund;//退款
	private static String wxRefundQuery;//查询退款
	
	
	//支付宝参数
	private static String openApiDomain;   // 支付宝openapi域名
	private static String mcloudApiDomain;  // 支付宝mcloudmonitor域名
	private static String pid;             // 商户partner id
	private static String appid;           // 商户应用id

	private static String privateKey;      // RSA私钥，用于对商户请求报文加签
	private static String publicKey;       // RSA公钥，仅用于验证开发者网关
	private static String alipayPublicKey; // 支付宝RSA公钥，用于验签支付宝应答
	private static String signType;     // 签名类型  

	private static int maxQueryRetry;   // 最大查询次数
	private static long queryDuration;  // 查询间隔（毫秒）

	private static int maxCancelRetry;  // 最大撤销次数
	private static long cancelDuration; // 撤销间隔（毫秒）

	private static long heartbeatDelay ; // 交易保障线程第一次调度延迟（秒）
	private static long heartbeatDuration ; // 交易保障线程调度间隔（秒）
    
    
	static{
    	CommonValue.init("/com/landicorp/config/envconf.properties");
    }
	// 根据文件名读取配置文件，文件后缀名必须为.properties
    public synchronized static void init(String filePath) {
        if (configs != null) {
            return;
        }

        try {
            configs = new PropertiesConfiguration(filePath);
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }

        if (configs == null) {
            throw new IllegalStateException("can`t find file by path:" + filePath);
        }
        
        defaultEncoding = configs.getString("DEFAULT_ENCODING"); 
        //微信参数
        wxAppid = configs.getString("wxAppid");//微信公众号id
    	wxAppsecret = configs.getString("wxAppsecret");//微信Appsecret
    	wxMchid = configs.getString("wxMchid");//微信商户号id
    	wxKey = configs.getString("wxKey");//微信秘钥
    	wxGateUrl = configs.getString("wxgateUrl");//统一下单
    	wxOrderQuery = configs.getString("wxorderquery");//订单查询
    	wxCloseOrder = configs.getString("wxcloseorder");//关闭订单
    	wxRefund = configs.getString("wxrefund");//退款
    	wxRefundQuery = configs.getString("wxrefundquery");//查询退款
        //支付宝参数
        openApiDomain = configs.getString("open_api_domain");
        mcloudApiDomain = configs.getString("mcloud_api_domain");

        pid = configs.getString("pid");
        appid = configs.getString("appid");

        // RSA
        privateKey = configs.getString("private_key");
        publicKey = configs.getString("public_key");
        alipayPublicKey = configs.getString("alipay_public_key");
        signType = configs.getString("sign_type");

        // 查询参数
        maxQueryRetry = configs.getInt("max_query_retry");
        queryDuration = configs.getLong("query_duration");
        maxCancelRetry = configs.getInt("max_cancel_retry");
        cancelDuration = configs.getLong("cancel_duration");

        // 交易保障调度线程
        heartbeatDelay = configs.getLong("heartbeat_delay");
        heartbeatDuration = configs.getLong("heartbeat_duration");

    }
	public static Configuration getConfigs() {
		return configs;
	}
	public static String getDefaultEncoding() {
		return defaultEncoding;
	}
	public static String getWxAppid() {
		return wxAppid;
	}
	public static String getWxAppsecret() {
		return wxAppsecret;
	}
	public static String getWxMchid() {
		return wxMchid;
	}
	public static String getWxKey() {
		return wxKey;
	}
	public static String getOpenApiDomain() {
		return openApiDomain;
	}
	public static String getMcloudApiDomain() {
		return mcloudApiDomain;
	}
	public static String getPid() {
		return pid;
	}
	public static String getAppid() {
		return appid;
	}
	public static String getPrivateKey() {
		return privateKey;
	}
	public static String getPublicKey() {
		return publicKey;
	}
	public static String getAlipayPublicKey() {
		return alipayPublicKey;
	}
	public static String getSignType() {
		return signType;
	}
	public static int getMaxQueryRetry() {
		return maxQueryRetry;
	}
	public static long getQueryDuration() {
		return queryDuration;
	}
	public static int getMaxCancelRetry() {
		return maxCancelRetry;
	}
	public static long getCancelDuration() {
		return cancelDuration;
	}
	public static long getHeartbeatDelay() {
		return heartbeatDelay;
	}
	public static long getHeartbeatDuration() {
		return heartbeatDuration;
	}
	public static String getWxGateUrl() {
		return wxGateUrl;
	}
	public static String getWxOrderQuery() {
		return wxOrderQuery;
	}
	public static String getWxCloseOrder() {
		return wxCloseOrder;
	}
	public static String getWxRefund() {
		return wxRefund;
	}
	public static String getWxRefundQuery() {
		return wxRefundQuery;
	}
}
