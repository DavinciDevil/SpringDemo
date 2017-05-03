package com.landicorp.core.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyManagementException;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

import sun.misc.BASE64Decoder;


public class SSLSocketUtil {

	public static SSLContext getSSLContext(String myKeyFile, String myCertFile, String[] oppsiteCertFiles)
			throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableKeyException,
			KeyManagementException {

		SSLContext sslContext = null;
		// 对方证书
		TrustManagerFactory tmf = null;
		// 读入对方证书
		if (oppsiteCertFiles != null) {
			// 建立对方JKS,android只能用BKS(BouncyCastle密库)，一般java应用参数传JKS(java自带密库)
			KeyStore oppositeKeyStore = null;
			oppositeKeyStore = KeyStore.getInstance("JKS");
			oppositeKeyStore.load(null, null);
			int i = 0;
			for (String oppsiteCertFile : oppsiteCertFiles) {
				PEMParser oppositePemParser = new PEMParser(new FileReader(oppsiteCertFile));
				Object oppositeObject = oppositePemParser.readObject();
				X509CertificateHolder oppositeCacertHolder = (X509CertificateHolder) oppositeObject;
				CertificateFactory oppositeCf = CertificateFactory.getInstance("X.509");
				InputStream oppositeIs = new ByteArrayInputStream(oppositeCacertHolder.toASN1Structure().getEncoded());
				X509Certificate oppositeCacert = (X509Certificate) oppositeCf.generateCertificate(oppositeIs);
				oppositePemParser.close();

				// 导入根证书作为trustedEntry
				KeyStore.TrustedCertificateEntry trustedEntry = new KeyStore.TrustedCertificateEntry(oppositeCacert);
				oppositeKeyStore.setEntry("trustedCert" + i++, trustedEntry, null);
			}
			tmf = TrustManagerFactory.getInstance("X509");// 密钥管理器,一般java应用传SunX509
			tmf.init(oppositeKeyStore);
		}

		KeyManagerFactory kmf = null;
		if (StringUtil.notEmpty(myKeyFile)) {
			// 创建自己空JKS,android只能用BKS(BouncyCastle密库)，一般java应用参数传JKS(java自带密库)
			KeyStore myKeystore = KeyStore.getInstance("JKS");
			myKeystore.load(null, null);
			// 读入自己私钥
			PrivateKey privateKey = null;
			try {
				BufferedReader br = new BufferedReader(new FileReader(myKeyFile));
				String s = br.readLine();
				StringBuffer privatekey = new StringBuffer();
				s = br.readLine();
				while (s.charAt(0) != '-') {
					privatekey.append(s + "\r");
					s = br.readLine();
				}
				BASE64Decoder base64decoder = new BASE64Decoder();
				byte[] keybyte = base64decoder.decodeBuffer(privatekey.toString());

				// 生成私匙
				KeyFactory kf = KeyFactory.getInstance("RSA");
				PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keybyte);
				privateKey = kf.generatePrivate(keySpec);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 读入自己证书
			CertificateFactory myCertFactory = CertificateFactory.getInstance("X.509");
			FileInputStream in = new FileInputStream(myCertFile);
			// 生成一个证书对象并使用从输入流 inStream 中读取的数据对它进行初始化。
			Certificate myCert = myCertFactory.generateCertificate(in);
			myKeystore.setKeyEntry("myKey", privateKey, "".toCharArray(), new Certificate[] { myCert });
			kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			kmf.init(myKeystore, "".toCharArray());
		}

		// 构建SSLContext，此处传入参数为TLS，也可以为SSL
		sslContext = SSLContext.getInstance("TLS");
		sslContext.init(kmf != null ? kmf.getKeyManagers() : null, tmf != null ? tmf.getTrustManagers() : null, null);

		return sslContext;
	}

	public static Socket getSocket(String host, int port, String myKeyFile, String myCertFile, String[] oppsiteCertFiles)
			throws UnknownHostException, IOException, UnrecoverableKeyException, KeyManagementException, KeyStoreException,
			NoSuchAlgorithmException, CertificateException {
		SSLContext context = getSSLContext(myKeyFile, myCertFile, oppsiteCertFiles);
		SSLSocketFactory factory = context.getSocketFactory();
		Socket socket = factory.createSocket(host, port);
		return socket;
	}

}
