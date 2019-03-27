/**
 * 2010-1-4
 */
package org.zlex.chapter12_4;

import org.apache.commons.codec.binary.Base64;

/**
 * 安全组件
 * 
 * @author 梁栋
 * @since 1.0
 */
public abstract class Security {

	/**
	 * 加密
	 * 
	 * @param data
	 *            待加密数据
	 * @return byte[] 加密数据
	 */
	public static byte[] encrypt(byte[] data) {
		return Base64.encodeBase64(data);
	}

	/**
	 * 解密
	 * 
	 * @param data
	 *            待解密数据
	 * @return byte[] 解密数据
	 */
	public static byte[] decrypt(byte[] data) {
		return Base64.decodeBase64(data);
	}
}
