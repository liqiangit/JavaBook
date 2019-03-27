/**
 * 2009-12-23
 */
package org.zlex.chapter12_2;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.SecureRandom;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * @author 梁栋
 * @since 1.0
 */
public class SSLSecurity {
	public static final String CERT_TYPE = "SunX509";

	public static final String PROTOCOL = "TLS";

	public static final String STORE_TYPE = "PKCS12";

	/**
	 * 取得密钥库/信任库
	 * 
	 * @param path
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static KeyStore getKeyStore(String path, String password)
			throws Exception {
		FileInputStream fis = new FileInputStream(path);
		KeyStore keyStore = KeyStore.getInstance(STORE_TYPE);
		keyStore.load(fis, password.toCharArray());
		fis.close();
		return keyStore;
	}

	/**
	 * 取得SSLServerSocket
	 * 
	 * @param serverStorePath
	 * @param serverStorePassword
	 * @param caStorePath
	 * @param caStorePassword
	 * @param port
	 * 
	 * @return
	 * @throws Exception
	 */
	public static SSLServerSocket getSSLServerSocket(String serverStorePath,
			String serverStorePassword, String caStorePath,
			String caStorePassword, int port) throws Exception {
		SSLContext ctx = getSSLContext(serverStorePath, serverStorePassword,
				caStorePath, caStorePassword);

		SSLServerSocketFactory factory = ctx.getServerSocketFactory();
		SSLServerSocket serverSocket = (SSLServerSocket) factory
				.createServerSocket(port);

		serverSocket.setNeedClientAuth(true);
		return serverSocket;
	}

	/**
	 * 取得SSLContext
	 * 
	 * @param keyStorePath
	 * @param keyStorePassword
	 * @param trustStorePath
	 * @param trustStorePassword
	 * @return
	 * @throws Exception
	 */
	public static SSLContext getSSLContext(String keyStorePath,
			String keyStorePassword, String trustStorePath,
			String trustStorePassword) throws Exception {

		SSLContext ctx = SSLContext.getInstance(PROTOCOL);

		KeyManagerFactory kmf = KeyManagerFactory.getInstance(CERT_TYPE);

		KeyStore ks = getKeyStore(keyStorePath, keyStorePassword);

		kmf.init(ks, keyStorePassword.toCharArray());

		KeyStore trustStore = getKeyStore(trustStorePath, trustStorePassword);

		TrustManagerFactory tmf = TrustManagerFactory.getInstance(CERT_TYPE);

		tmf.init(trustStore);

		ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(),
				new SecureRandom());
		return ctx;

	}

	/**
	 * 取得SSLSocket
	 * 
	 * @param clientStorePath
	 * @param clientStorePassword
	 * @param serverStorePath
	 * @param serverStorePassword
	 * 
	 * @return
	 * @throws Exception
	 */
	public static SSLSocket getSSLClientSocket(String clientStorePath,
			String clientStorePassword, String serverStorePath,
			String serverStorePassword, String ip, int port) throws Exception {
		SSLContext context = getSSLContext(clientStorePath,
				clientStorePassword, serverStorePath, serverStorePassword);

		SSLSocketFactory factory = context.getSocketFactory();
		SSLSocket socket = (SSLSocket) factory.createSocket(ip, port);
		return socket;
	}
}
