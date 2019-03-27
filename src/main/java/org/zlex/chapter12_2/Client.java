/**
 * 2009-12-23
 */
package org.zlex.chapter12_2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLSocket;

/**
 * @author 梁栋
 * @since 1.0
 */
public class Client implements Runnable {
	public static final String CERT_CA = "D:/ca/certs/ca.p12";

	public static final String PASSWORD_CA = "123456";

	public static final String CERT_CLIENT = "D:/ca/certs/client.p12";

	public static final String PASSWORD_CLIENT = "123456";

	public static final String CERT_SERVER = "D:/ca/certs/server.p12";

	public static final String PASSWORD_SERVER = "123456";

	public static final int PORT = 443;

	private SSLSocket socket;
	private InputStream is;
	private OutputStream os;

	public Client() throws Exception {
		Map<String, String> userMap = new HashMap<String, String>();
		userMap.put("Admin", "70682896e24287b0476eff2a14c148f0");
	}

	public void close() {
		try {
			os.close();
			is.close();
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {

		while (true) {

			try {
				socket = SSLSecurity.getSSLClientSocket(CERT_CLIENT,
						PASSWORD_CLIENT, CERT_SERVER, PASSWORD_SERVER,
						"192.168.0.89", PORT);

				is = socket.getInputStream();
				os = socket.getOutputStream();
				PrintWriter pw = new PrintWriter(os);
				pw.println("GET /index.html HTTP/1.0");
				pw.println("Server: www.google.com");
				pw.println("Connection: close");
				pw.println();
				pw.flush();

				BufferedReader bin = new BufferedReader(new InputStreamReader(
						is));
				String ln;
				while ((ln = bin.readLine()) != null) {
					System.err.println(ln);
				}

				// close();
				Thread.sleep(5000);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			Thread t = new Thread(new Client());
			t.start();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

}
