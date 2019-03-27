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

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;

/**
 * @author 梁栋
 * @since 1.0
 */
public class Server implements Runnable {
	public static final String CERT_CA = "D:/ca/certs/ca.p12";

	public static final String PASSWORD_CA = "123456";

	public static final String CERT_CLIENT = "D:/ca/certs/client.p12";

	public static final String PASSWORD_CLIENT = "123456";

	public static final String CERT_SERVER = "D:/ca/certs/server.p12";

	public static final String PASSWORD_SERVER = "123456";

	public static final int PORT = 443;

	private static SSLServerSocket serverSocket;
	private SSLSocket socket;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		int b = 1;

		while (true) {
			try {
				System.err.println("-----------------------" + b++);

				socket = (SSLSocket) serverSocket.accept();
				try {
					socket.startHandshake();
				} catch (Exception ex) {
					System.out.println("Handshake failed: " + ex);
				}

				socket.startHandshake();
				OutputStream os = socket.getOutputStream();
				InputStream in = socket.getInputStream();

				PrintWriter out = new PrintWriter(os);
				BufferedReader bin = new BufferedReader(new InputStreamReader(
						in));
				String ln;
				while ((ln = bin.readLine()) != null) {
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

				out.flush();
				out.close();
				in.close();
				socket.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	public static void main(String[] args) {
		try {

			Thread t = new Thread(new Server());
			t.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Server() throws Exception {
		serverSocket = SSLSecurity.getSSLServerSocket(CERT_SERVER,
				PASSWORD_SERVER, CERT_CA, PASSWORD_CA, PORT);

	}
}
