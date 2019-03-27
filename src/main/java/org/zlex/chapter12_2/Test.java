/**
 * 2009-11-5
 */
package org.zlex.chapter12_2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

/**
 * 
 * @author 梁栋
 * @version 1.0
 * @since 1.0
 */
public class Test extends Thread {

	public static final String CERT_CA = "D:/ca/certs/ca.p12";

	public static final String PASSWORD_CA = "123456";

	public static final String CERT_CLIENT = "D:/ca/certs/client.p12";

	public static final String PASSWORD_CLIENT = "123456";

	public static final String CERT_SERVER = "D:/ca/certs/server.p12";

	public static final String PASSWORD_SERVER = "123456";

	public static final int PORT = 443;

	@Override
	public void run() {
		try {
			sleep(100);

			SSLSocket socket = SSLSecurity.getSSLClientSocket(CERT_CLIENT,
					PASSWORD_CLIENT, CERT_SERVER, PASSWORD_SERVER, "127.0.0.1",
					PORT);

			showCerts(socket.getSession());

			PrintWriter pw = new PrintWriter(socket.getOutputStream());
			pw.println("GET /index.html HTTP/1.0");
//			pw.println("Server: www.google.com");
			pw.println("Connection: close");
			pw.println();
			pw.flush();

			BufferedReader in = new BufferedReader(new InputStreamReader(socket
					.getInputStream()));
			String ln;
			while ((ln = in.readLine()) != null) {
				System.err.println(ln);
			}

		} catch (Exception ex) {
			Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public static void showCerts(SSLSession session) {
		X509Certificate cert = null;
		try {
			cert = (X509Certificate) session.getPeerCertificates()[0];
		} catch (SSLPeerUnverifiedException e) {
			e.printStackTrace();
			System.err.println(session.getPeerHost()
					+ " did not present a valid certificate");

			return;
		}
		System.out.println(session.getPeerHost()
				+ " has presented a certificate belonging to" + "["
				+ cert.getSubjectDN() + "]\n"
				+ "The certificate was issued by: \t" + "["
				+ cert.getIssuerDN() + "]");

	}

	public static void main(String[] args) throws Exception {

		SSLServerSocket serverSocket = SSLSecurity.getSSLServerSocket(
				CERT_SERVER, PASSWORD_SERVER, CERT_CA, PASSWORD_CA, PORT);

		new Test().start();

		SSLSocket socket = (SSLSocket) serverSocket.accept();
		try {
			socket.startHandshake();
		} catch (Exception ex) {
			System.out.println("Handshake failed: " + ex);
		}

		showCerts(socket.getSession());

		socket.startHandshake();

		PrintWriter out = new PrintWriter(socket.getOutputStream());
		BufferedReader in = new BufferedReader(new InputStreamReader(socket
				.getInputStream()));
		String ln;
		while ((ln = in.readLine()) != null) {
			System.out.println(ln);
			if (ln.equals("")) {
				break;
			}
		}
		out.println("HTTP/1.1 200 OK");
		out.println("Cache-Control: no-cache");
		out.println("Pragma: no-cache");
		out.println("Expires: Fri, 01 Jan 1990 00:00:00 GMT");
		out.println("Content-Type: text/html; charset=UTF-8");
		out.println("Date: Tue, 01 Jul 2008 11:56:42 GMT");
		out.println("Server: BWS");
		out.println("X-Powered-By: BWS");
		out.println();
		out.println("<html><body><h1>hello world</h1></body></html>");
		out.close();
		socket.close();
	}
}
