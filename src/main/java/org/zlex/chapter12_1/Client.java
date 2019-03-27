/**
 * 2009-12-10
 */
package org.zlex.chapter12_1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author 梁栋
 * 
 */
public class Client implements Runnable {

	private Socket socket;

	public Client() {

		try {
			socket = new Socket(InetAddress.getLocalHost(), 9000);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		Client client = new Client();

	}

	@Override
	public void run() {
		try {
			BufferedReader br = getReader(socket);
			PrintWriter pw = getWriter(socket);
			// 打印欢迎信息
			pw.println("已连接到:" + socket.getLocalSocketAddress().toString());
			String msg = null;
			while ((msg = br.readLine()) != null) {
				System.out.println(socket.getRemoteSocketAddress().toString()
						+ ":" + msg);
				pw.println("Server:" + msg);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private PrintWriter getWriter(Socket socket) throws IOException {
		OutputStream socketOut = socket.getOutputStream();
		return new PrintWriter(socketOut, true);
	}

	private BufferedReader getReader(Socket socket) throws IOException {
		InputStream socketIn = socket.getInputStream();
		return new BufferedReader(new InputStreamReader(socketIn));
	}

}
