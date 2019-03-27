/**
 * 2009-8-31
 */
package org.zlex.chapter06_6;

import java.security.MessageDigest;

/**
 * 消息摘要组件
 * 
 * @author 梁栋
 * @version 1.0
 * @since 1.0
 */
public abstract class MessageDigestCoder {
	/**
	 * 可选以下多种算法<br>
	 * <b>Message Digest</b>
	 * 
	 * <pre>
	 * MD2: The MD2 message digest algorithm as defined in RFC 1319. 
	 * MD5: The MD5 message digest algorithm as defined in RFC 1321.
	 * </pre>
	 */
	public static final String ALGORITHM = "MD5";

	/**
	 * MD5加密
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static byte[] encodeMD5(byte[] data) throws Exception {

		MessageDigest md = getMD5MessageDigest();
		md.update(data);

		return md.digest();
	}

	/**
	 * @return
	 * @throws Exception
	 */
	private static MessageDigest getMD5MessageDigest() throws Exception {
		return MessageDigest.getInstance(ALGORITHM);
	}
}
