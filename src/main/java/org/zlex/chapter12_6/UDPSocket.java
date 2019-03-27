/**
 * 2010-1-4
 */
package org.zlex.chapter12_6;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;

/**
 * UDP套接字
 * 
 * @author 梁栋
 * @since 1.0
 */
public class UDPSocket {

	/**
	 * 缓冲
	 */
	private byte[] buffer = new byte[1024];

	/**
	 * 接收套接字
	 */
	private DatagramSocket receiveSocket;

	/**
	 * 发送套接字
	 */
	private DatagramSocket sendSocket;

	/**
	 * 目标主机
	 */
	private String remoteHost;

	/**
	 * 发送端口
	 */
	private int sendPort;

	/**
	 * 初始化
	 * 
	 * @param localHost
	 *            本地主机IP
	 * @param remoteHost
	 *            远程主机IP
	 * @param receivePort
	 *            接收端口
	 * @param sendPort
	 *            发送端口
	 * @throws SocketException
	 */
	public UDPSocket(String localHost, String remoteHost, int receivePort,
			int sendPort) throws SocketException {
		this.remoteHost = remoteHost;
		this.sendPort = sendPort;

		// 初始化套接字
		this.receiveSocket = new DatagramSocket(new InetSocketAddress(
				localHost, receivePort));
		this.sendSocket = new DatagramSocket();
	}

	/**
	 * 接收消息
	 * 
	 * @return String 消息
	 * @throws IOException
	 */
	public byte[] receive() throws IOException {
		DatagramPacket dp = new DatagramPacket(buffer, buffer.length);

		receiveSocket.receive(dp);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write(dp.getData(), 0, dp.getLength());
		byte[] data = baos.toByteArray();
		baos.flush();
		baos.close();
		return data;
	}

	/**
	 * 发送消息
	 * 
	 * @param data
	 *            消息
	 * @throws IOException
	 */
	public void send(byte[] data) throws IOException {
		DatagramPacket dp = new DatagramPacket(buffer, buffer.length,
				InetAddress.getByName(remoteHost), sendPort);
		dp.setData(data);
		sendSocket.send(dp);
	}

	/**
	 * 关闭UDP套接字
	 * 
	 */
	public void close() {
		try {

			// 关闭接收套接字
			if (receiveSocket.isConnected()) {
				receiveSocket.disconnect();
				receiveSocket.close();
			}

			// 关闭发送套接字
			if (sendSocket.isConnected()) {
				sendSocket.disconnect();
				sendSocket.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
