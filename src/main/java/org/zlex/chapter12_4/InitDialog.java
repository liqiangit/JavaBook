/**
 * 2010-1-4
 */
package org.zlex.chapter12_4;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * @author 梁栋
 * @since 1.0
 */
public class InitDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8482349275221329655L;

	/**
	 * 默认宽度
	 */
	private static final int DEFAULT_WIDTH = 200;

	/**
	 * 默认高度
	 */
	private static final int DEFAULT_HEIGHT = 210;

	/**
	 * 接收端口
	 */
	private int receivePort;

	/**
	 * 发送端口
	 */
	private int sendPort;

	/**
	 * 用户昵称
	 */
	private String username;

	/**
	 * 目标主机
	 */
	private String remoteHost;

	/**
	 * 本地主机
	 */
	private String localHost;

	/**
	 * 取消状态
	 */
	private boolean cancelled = true;

	/**
	 * @return the localHost
	 */
	public String getLocalHost() {
		return localHost;
	}

	/**
	 * @return the cancelled
	 */
	public boolean isCancelled() {
		return cancelled;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return the receivePort
	 */
	public int getReceivePort() {
		return receivePort;
	}

	/**
	 * @return the sendPort
	 */
	public int getSendPort() {
		return sendPort;
	}

	/**
	 * @return the remoteHost
	 */
	public String getRemoteHost() {
		return remoteHost;
	}

	/**
	 * @param owner
	 */
	public InitDialog(Frame owner) {
		super(owner, "初始化对话框", true);

		// 初始化文本输入字段

		String local;

		try {
			local = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			local = "localhost";
		}

		final JTextField remoteHostField = new JTextField(local, 10);
		final JTextField localHostField = new JTextField(local, 10);
		final JTextField receivePortField = new JTextField("8001", 10);
		final JTextField sendPortField = new JTextField("8002", 10);
		final JTextField usernameField = new JTextField("zlex", 10);

		// 构建输入面板
		JPanel inputPanel = new JPanel();

		inputPanel.setMinimumSize(new Dimension(80, 120));
		inputPanel.setBorder(BorderFactory.createEtchedBorder());

		inputPanel.add(new JLabel("目标主机："));
		inputPanel.add(remoteHostField);

		inputPanel.add(new JLabel("本地主机："));
		inputPanel.add(localHostField);

		inputPanel.add(new JLabel("接收端口："));
		inputPanel.add(receivePortField);

		inputPanel.add(new JLabel("发送端口："));
		inputPanel.add(sendPortField);

		inputPanel.add(new JLabel("用户昵称："));
		inputPanel.add(usernameField);

		// 构建确认按钮
		JButton okButton = new JButton("确定");

		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// 赋值
				remoteHost = remoteHostField.getText();
				localHost = localHostField.getText();
				receivePort = Integer.parseInt(receivePortField.getText());
				sendPort = Integer.parseInt(sendPortField.getText());
				username = usernameField.getText();
				cancelled = false;
				InitDialog.this.dispose();
			}
		});

		// 构建取消按钮
		JButton cancelButton = new JButton("取消"); //$NON-NLS-1$
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				InitDialog.this.dispose();
			}
		});

		// 构建按钮面板
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);

		getContentPane().add(inputPanel, BorderLayout.CENTER);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);

		// 设置最小尺寸
		setMinimumSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));

		// 设置窗口大小不可调
		setResizable(false);

		// 窗口在屏幕中间显示
		setLocationRelativeTo(null);

		// 显示
		setVisible(true);
	}

}
