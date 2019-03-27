/**
 * 2009-12-25
 */
package org.zlex.chapter12_5;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

/**
 * UDP协议聊天工具
 * 
 * @author 梁栋
 * @since 1.0
 */
public class MainFrame extends JFrame implements Runnable {

	private static final long serialVersionUID = 647944495306233293L;

	/**
	 * 字符集
	 */
	private static final String CHARSET = "UTF-8";

	/**
	 * UDP套接字
	 */
	private UDPSocket socket;

	/**
	 * 初始化对话框
	 */
	private InitDialog initDialog;

	/**
	 * 默认宽度
	 */
	public static final int DEFAULT_WIDTH = 500;

	/**
	 * 默认高度
	 */
	public static final int DEFAULT_HEIGHT = 400;

	/**
	 * 设置拆分窗格
	 */
	private JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

	/**
	 * 发送文本域
	 */
	private JTextArea sendTextArea = new JTextArea();

	/**
	 * 接收文本域
	 */
	private JTextArea receiveTextArea = new JTextArea();

	/**
	 * 按钮面板
	 */
	private JPanel buttonPanel = new JPanel();

	/**
	 * 绑定主机和端口
	 * 
	 * @throws Exception
	 */
	public MainFrame() {

		// 优先启动初始化对话框
		this.initDialog = new InitDialog(this);

		// 若初始化对话框取消，则关闭系统，反之初始化系统
		if (initDialog.isCancelled()) {
			System.exit(0);
		} else {
			initSocket();
			initGUI();
		}
	}

	/**
	 * 初始化套接字
	 */
	public void initSocket() {

		try {

			socket = new UDPSocket(initDialog.getLocalHost(), initDialog
					.getRemoteHost(), initDialog.getReceivePort(), initDialog
					.getSendPort());

		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 初始化用户界面
	 */
	public void initGUI() {
		setTitle("From：" + initDialog.getLocalHost() + " To："
				+ initDialog.getRemoteHost());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));

		getContentPane().add(splitPane, BorderLayout.CENTER);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);

		initReceivePanel();
		initSendPanel();
		initButtonPanel();

		// 窗口在屏幕中间显示
		setLocationRelativeTo(null);

		setVisible(true);
		setResizable(false);
	}

	/**
	 * 初始化按钮面板
	 */
	private void initButtonPanel() {

		JButton sendButton = new JButton("发送(S)");
		sendButton.setMnemonic(KeyEvent.VK_S);
		sendButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					send(sendTextArea.getText());
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}

		});

		JButton exitButton = new JButton("关闭(X)");
		exitButton.setMnemonic(KeyEvent.VK_X);
		exitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				socket.close();
				System.exit(0);
			}
		});

		buttonPanel.setBorder(BorderFactory.createEtchedBorder());
		buttonPanel.add(sendButton);
		buttonPanel.add(exitButton);
	}

	/**
	 * 初始化接受消息面板
	 */
	private void initReceivePanel() {

		receiveTextArea.setEditable(false);

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBorder(BorderFactory.createEtchedBorder());

		JLabel label = new JLabel();
		label.setText("接收到的消息：");
		panel.add(label, BorderLayout.NORTH);

		JScrollPane scrollPane = new JScrollPane();

		scrollPane.getViewport().add(receiveTextArea);

		DefaultCaret caret = (DefaultCaret) receiveTextArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		panel.add(scrollPane, BorderLayout.CENTER);

		panel.setMinimumSize(new Dimension(0, DEFAULT_WIDTH / 3));
		splitPane.add(panel);
	}

	/**
	 * 初始化发送消息面板
	 */
	private void initSendPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBorder(BorderFactory.createEtchedBorder());

		JLabel label = new JLabel();
		label.setText("待发送的消息：");
		panel.add(label, BorderLayout.NORTH);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setWheelScrollingEnabled(true);
		DefaultCaret caret = (DefaultCaret) sendTextArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		sendTextArea.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent event) {

				// Ctrl+Enter组合键
				if ((event.getKeyCode() == KeyEvent.VK_ENTER)
						&& (event.isControlDown())) {

					try {
						send(sendTextArea.getText());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});

		scrollPane.getViewport().add(sendTextArea);
		panel.add(scrollPane, BorderLayout.CENTER);
		splitPane.add(panel);
	}

	/**
	 * 接收消息
	 * 
	 * @throws IOException
	 */
	public void receive() throws IOException {

		// 解密
		byte[] data = Security.decrypt(socket.receive());

		String message = new String(data, CHARSET);

		StringBuilder sb = new StringBuilder();
		sb.append(receiveTextArea.getText());
		sb.append(message);
		receiveTextArea.setText(sb.toString());
	}

	/**
	 * 发送消息
	 * 
	 * @param message
	 *            消息
	 * @throws IOException
	 */
	public void send(String message) throws IOException {

		if (message.isEmpty()) {
			return;
		}

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		StringBuilder sendMessage = new StringBuilder();

		sendMessage.append(df.format(new Date()));
		sendMessage.append(" (" + initDialog.getUsername() + ") ");
		sendMessage.append("\r\n");
		sendMessage.append(message);
		sendMessage.append("\r\n");

		message = sendMessage.toString();

		socket.send(Security.encrypt(message.getBytes(CHARSET)));

		StringBuilder receiveMessage = new StringBuilder(receiveTextArea
				.getText());
		receiveMessage.append(sendMessage);
		receiveTextArea.setText(receiveMessage.toString());
		sendTextArea.setText(null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		// 循环读
		while (true) {
			try {
				receive();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * 总入口
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		MainFrame mainFrame = new MainFrame();
		Thread t = new Thread(mainFrame);
		t.start();
	}

}
