/**
 * 2009-12-4
 */
package org.zlex.chapter12_1;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * @author 梁栋
 * 
 */
public class Server extends JFrame {

	public static final String CHARACTER_ENCODING = "UTF-8";
	public static final String METHOD_POST = "POST";
	public static final String METHOD_GET = "GET";
	public static final String CONTENT_TYPE = "Content-Type";

	private static final long serialVersionUID = -800124424966940064L;

	/**
	 * 回复文本域
	 */
	private JTextArea responseTextArea = new JTextArea();

	/**
	 * 请求文本域
	 */
	private JTextArea requestTextArea = new JTextArea();

	/**
	 * 设置拆分窗格
	 */
	private JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

	/**
	 * 容器
	 */
	private Container container = getContentPane();
	private JPanel buttonPanel = new JPanel();

	private JTextField addressTextField = new JTextField(Messages
			.getString("Server.url"));

	/**
	 * 默认宽度
	 */
	public static final int DEFAULT_WIDTH = 450;

	/**
	 * 默认高度
	 */
	public static final int DEFAULT_HEIGHT = 400;

	/**
	 * 初始化
	 */
	private void init() {

		// 初始化主体面板
		initBodyPanel();
		// 设置标题
		this.setTitle(Messages.getString("Server.title.main"));
		// 设置关闭方式
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		// 设置窗口尺寸
		this.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		// 窗口可见
		this.setVisible(true);
		// 窗口在屏幕中间显示
		this.setLocationRelativeTo(null);
		// 窗口尺寸不可调
		this.setResizable(false);
	}

	/**
	 * 初始化主体面板
	 */
	private void initBodyPanel() {

		initRequestPanel();
		initResponsePanel();
		initButtonPanel();
		container.add(splitPane, BorderLayout.CENTER);
		container.add(buttonPanel, BorderLayout.NORTH);

	}

	private void initButtonPanel() {

		buttonPanel.setLayout(new BorderLayout());

		buttonPanel.setBorder(BorderFactory.createEtchedBorder());

		JButton sendButton = new JButton(Messages
				.getString("Server.button.send"));
		buttonPanel.add(sendButton, BorderLayout.EAST);

		buttonPanel.add(addressTextField, BorderLayout.CENTER);

		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				send();
			}
		});

	}

	private void initRequestPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		JLabel label = new JLabel();
		label.setText(Messages.getString("Server.title.request"));
		panel.add(label, BorderLayout.NORTH);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().add(requestTextArea);
		panel.add(scrollPane, BorderLayout.CENTER);
		panel.setMinimumSize(new Dimension(0, DEFAULT_WIDTH / 3));
		splitPane.add(panel);
	}

	private void initResponsePanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		JLabel label = new JLabel();
		label.setText(Messages.getString("Server.title.response"));
		panel.add(label, BorderLayout.NORTH);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().add(responseTextArea);
		panel.add(scrollPane, BorderLayout.CENTER);

		splitPane.add(panel);
	}

	/**
	 * 
	 */
	private void send() {

		String request = requestTextArea.getText().trim();
		if (!request.isEmpty()) {

			try {

				String protocolResponse = new String(
						httpPost(addressTextField.getText().trim(), request
								.getBytes(CHARACTER_ENCODING)),
						CHARACTER_ENCODING);

				responseTextArea.setText(protocolResponse);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, e.toString());
			}
		}
	}

	/**
	 * 以POST方式向指定地址发送数据包请求,并取得返回的数据包
	 * 
	 * @param urlString
	 * @param requestData
	 * @return 返回的数据包
	 * @throws Exception
	 */
	public static byte[] httpPost(String urlString, byte[] requestData)
			throws Exception {
		Properties requestProperties = new Properties();
		requestProperties.setProperty(CONTENT_TYPE,
				"application/octet-stream; charset=utf-8");

		return httpPost(urlString, requestData, requestProperties);
	}

	/**
	 * 以POST方式向指定地址发送数据包请求,并取得返回的数据包
	 * 
	 * @param urlString
	 * @param requestData
	 * @param requestProperties
	 * @return 返回的数据包
	 * @throws IOException
	 */
	public static byte[] httpPost(String urlString, byte[] requestData,
			Properties requestProperties) throws Exception {

		byte[] responseData = null;

		HttpURLConnection con = null;

		try {
			URL url = new URL(urlString);
			con = (HttpURLConnection) url.openConnection();

			if ((requestProperties != null) && (!requestProperties.isEmpty())) {
				for (Map.Entry<Object, Object> entry : requestProperties
						.entrySet()) {
					String key = String.valueOf(entry.getKey());
					String value = String.valueOf(entry.getValue());
					con.setRequestProperty(key, value);
				}
			}

			con.setRequestMethod(METHOD_POST);

			con.setDoOutput(true);
			con.setDoInput(true);

			if (requestData != null) {
				DataOutputStream dos = new DataOutputStream(con
						.getOutputStream());
				dos.write(requestData);
				dos.flush();
				dos.close();
			}

			int length = con.getContentLength();
			if (length > 0) {
				DataInputStream dis = new DataInputStream(con.getInputStream());
				responseData = new byte[length];
				dis.readFully(responseData);
				dis.close();
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (con != null) {
				con.disconnect();
				con = null;
			}
		}

		return responseData;
	}

	/**
	 * 
	 */
	public Server() {
		init();

	}

	public static void main(String args[]) {
		new Server();

	}
}
