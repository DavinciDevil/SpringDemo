package com.landicorp.core.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;


/**
 * @author:chenx
 * @date:2012-9-13
 * @version:V1.0
 * @company:福建联迪商用设备有限公司
 */
public class HttpsUtil {

	private static final int TIME_OUT = 30 * 1000;

	private static HttpURLConnection conn;
	private static X509TrustManager x509TrustManager;
	private static X509TrustManager x509TrustManagers[];
	private static HostnameVerifier hostnameVerifier;
	private static final String CLIENT_AGREEMENT = "TLS";// 使用协议
	private static final String CLIENT_KEY_MANAGER = "X509";// 密钥管理器
	private static final String CLIENT_TRUST_MANAGER = "X509";//
	private static final String CLIENT_KEY_KEYSTORE = "BKS";// 密库，这里用的是BouncyCastle密库
	private static final String CLIENT_TRUST_KEYSTORE = "BKS";//

	static {
		// try {
		// x509TrustManager = new LandiX509TrustManager();
		// } catch (KeyStoreException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (NoSuchAlgorithmException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (CertificateException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (FileNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (NoSuchProviderException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		X509TrustManager ax509trustmanager[] = new X509TrustManager[1];
		ax509trustmanager[0] = x509TrustManager;
		x509TrustManagers = ax509trustmanager;
		hostnameVerifier = new TrustAnyHostnameVerifier();
	}

	/**
	 * 访问url，返回InputStream
	 * 
	 * @param url
	 * @param paramMap
	 *          参数表
	 * @return InputStream
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws MalformedURLException
	 * @throws KeyManagementException
	 */
	public static InputStream sendRequestForInputStram(String url, Map<String, String> paramMap,
			InputStream keyCertificate, InputStream trustCertificate) throws KeyManagementException, MalformedURLException,
			NoSuchAlgorithmException, IOException {
		StringBuilder paramSb = new StringBuilder();
		if (paramMap == null) {
			return sendRequestForInputStram(url, "", keyCertificate, trustCertificate);
		}
		Set<String> set = paramMap.keySet();
		for (String str : set) {
			paramSb.append(str).append("=").append(paramMap.get(str)).append("&");
		}
		// 去掉最后一个&
		if (paramSb.length() < 1) {
			return sendRequestForInputStram(url, "", keyCertificate, trustCertificate);
		}
		return sendRequestForInputStram(url, paramSb.substring(0, paramSb.length() - 1), keyCertificate, trustCertificate);
	}

	/**
	 * 访问url，返回InputStream
	 * 
	 * @param url
	 * @param param
	 * @return
	 * @throws IOException
	 * @throws MalformedURLException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	public static InputStream sendRequestForInputStram(String url, String param, InputStream keyCertificate,
			InputStream trustCertificate) throws MalformedURLException, IOException, NoSuchAlgorithmException,
			KeyManagementException {
		conn = (HttpURLConnection) (new URL(url)).openConnection();
		if (conn instanceof HttpsURLConnection) {
			SSLContext sslContext = SSLContext.getInstance("TLS");

			KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");
			TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");

			try {
				KeyStore ks = KeyStore.getInstance(CLIENT_KEY_KEYSTORE);
				KeyStore tks = KeyStore.getInstance(CLIENT_TRUST_KEYSTORE);

				ks.load(keyCertificate, "1330223901".toCharArray());
				tks.load(trustCertificate, "1330223901".toCharArray());

				kmf.init(ks, "1330223901".toCharArray());
				tmf.init(tks);
				sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
			} catch (KeyStoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CertificateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnrecoverableKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// sslContext.init(new KeyManager[0], x509TrustManagers,
			// new SecureRandom());
			SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
			((HttpsURLConnection) conn).setSSLSocketFactory(sslSocketFactory);
			((HttpsURLConnection) conn).setHostnameVerifier(hostnameVerifier);
		}
		conn.setConnectTimeout(TIME_OUT);
		conn.setDoOutput(true);
		conn.connect();

		OutputStream os = conn.getOutputStream();
		if (StringUtil.notEmpty(param)) {
			os.write(param.getBytes());
		}

		InputStream is = conn.getInputStream();
		return is;

	}

	/**
	 * 访问url，返回InputStream
	 * 
	 * @param url
	 * @param param
	 * @return
	 * @throws IOException
	 * @throws MalformedURLException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	public static InputStream sendRequestForInputStramByXml(String url, String param, SSLContext sslContext)
			throws MalformedURLException, IOException, NoSuchAlgorithmException, KeyManagementException {
		conn = (HttpURLConnection) (new URL(url)).openConnection();
		if (conn instanceof HttpsURLConnection) {
			SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
			((HttpsURLConnection) conn).setSSLSocketFactory(sslSocketFactory);
			((HttpsURLConnection) conn).setHostnameVerifier(hostnameVerifier);
		}
		conn.setConnectTimeout(TIME_OUT);
		conn.setDoOutput(true);
		conn.connect();

		OutputStream os = conn.getOutputStream();
		if (StringUtil.notEmpty(param)) {
			os.write(param.getBytes());
		}

		InputStream is = conn.getInputStream();
		return is;

	}

	/**
	 * 根据InputStream返回字符串
	 * 
	 * @param inStream
	 * @return String
	 * @throws IOException
	 */
	public static String getResponse(InputStream inStream) throws IOException {
		if (inStream == null) {
			return null;
		}
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		int len = -1;
		byte[] buffer = new byte[1024];
		while ((len = inStream.read(buffer)) != -1) {
			outputStream.write(buffer, 0, len);
		}
		byte[] data = outputStream.toByteArray();
		return new String(data);
	}

	/**
	 * 访问url直接返回字符串
	 * 
	 * @param url
	 * @param paramMap
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	public static String getResult(String url, Map paramMap, InputStream keyCertificate, InputStream trustCertificate)
			throws IOException, KeyManagementException, NoSuchAlgorithmException {
		return getResponse(sendRequestForInputStram(url, paramMap, keyCertificate, trustCertificate));
	}

	/**
	 * 访问url直接返回字符串
	 * 
	 * @param url
	 * @param param
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	public static String getResult(String url, String param, InputStream keyCertificate, InputStream trustCertificate)
			throws IOException, KeyManagementException, NoSuchAlgorithmException {
		return getResponse(sendRequestForInputStram(url, param, keyCertificate, trustCertificate));
	}

	/**
	 * 访问url直接返回字符串
	 * 
	 * @param url
	 * @param paramMap
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	public static String getResultByXml(String url, String requestXml, SSLContext context) throws IOException,
			KeyManagementException, NoSuchAlgorithmException {
		return getResponse(sendRequestForInputStramByXml(url, requestXml, context));
	}

	private static class TrustAnyTrustManager implements X509TrustManager {

		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

		}

		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[] {};
		}

	}

	private static class TrustAnyHostnameVerifier implements HostnameVerifier {

		@Override
		public boolean verify(String hostname, SSLSession session) {
			System.out.println("Warning: URL host:" + hostname + " vs. " + session.getPeerHost());
			return true;
		}
	}

	public static void main(String[] args) throws KeyManagementException, MalformedURLException,
			NoSuchAlgorithmException, IOException {
		Map paramMap = new HashMap();
		paramMap.put("1", "1");

		String xml = getResult(
				"https://192.168.37.50:8443/vipos/CupMobile",
				// "http://202.101.25.178:8080/Gateway/MobilePayment",
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?><cupMobile application=\"UPCards\" version=\"1.01\"><transaction type=\"BalanceEnquiry.APReq\"><submitTime>20121024113316</submitTime><accountNumber1>B9893CC03B1AD42147ABA2B9ACD9ABCB</accountNumber1><pin>5DA1B87145189C8A</pin><track2Data>5D7D6D9A6913D2D7</track2Data><cardSerialNumber>BCB3E91F2379BF490100</cardSerialNumber><dynamicKeyData>5D7D6D9A6913D2D7</dynamicKeyData><SpId>0009</SpId></transaction><securityChipTp>51</securityChipTp><mac>A4E58C56</mac></cupMobile>",
				new FileInputStream("D:/OpenSSL-Win64/bin/ca2/client.bks"), new FileInputStream(
						"D:/OpenSSL-Win64/bin/ca2/client.bks"));
		System.out.println(xml);
	}
}