/**
 * 2010-1-4
 */
package org.zlex.chapter12_6;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * 安全组件
 * 
 * @author 梁栋
 * @since 1.0
 */
public abstract class Security {

	/**
	 * 密钥算法
	 */
	public static final String ALGORITHM = "AES";

	/**
	 * 密钥
	 */
	private static final String KEY = "1486c5dc751a54ce3a58701ba537ecc8e257bf66127837e9401acdaceb6023f8";

	/**
	 * 转换密钥
	 * 
	 * @throws Exception
	 */
	private static Key getKey() throws Exception {

		byte[] key = Hex.decodeHex(KEY.toCharArray());

		// 实例化AES密钥材料
		SecretKey secretKey = new SecretKeySpec(key, ALGORITHM);

		return secretKey;
	}

	/**
	 * 解密
	 * 
	 * @param data
	 *            待解密数据
	 * @return byte[] 解密数据
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] data) {

		try {
			// 实例化
			Cipher cipher = Cipher.getInstance(ALGORITHM);

			// 初始化，设置为解密模式
			cipher.init(Cipher.DECRYPT_MODE, getKey());

			// 执行操作
			return cipher.doFinal(data);
		} catch (Exception e) {
			return data;
		}

	}

	/**
	 * 加密
	 * 
	 * @param data
	 *            待加密数据
	 * @return byte[] 加密数据
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] data) {

		try {
			// 实例化
			Cipher cipher = Cipher.getInstance(ALGORITHM);

			// 初始化，设置为加密模式
			cipher.init(Cipher.ENCRYPT_MODE, getKey());

			// 执行操作
			return cipher.doFinal(data);

		} catch (Exception e) {

			return data;
		}
	}

	/**
	 * 生成密钥
	 * 
	 * @return byte[] 二进制密钥
	 * @throws Exception
	 */
	public static byte[] initKey() throws Exception {

		// 实例化
		KeyGenerator kg = KeyGenerator.getInstance(ALGORITHM);

		kg.init(256);

		// 生成秘密密钥
		SecretKey secretKey = kg.generateKey();

		// 获得密钥的二进制编码形式
		return secretKey.getEncoded();
	}

	/**
	 * 生成密钥
	 * 
	 * @return String 十六进制字符串密钥
	 * @throws Exception
	 */
	public static String initKeyHex() throws Exception {
		return Hex.encodeHexString(initKey());
	}

	/**
	 * 对数据进行摘要处理
	 * 
	 * @param data
	 *            待摘要的数据
	 * @return String 十六进制摘要串
	 */
	public static String md5Hex(byte[] data) {
		return DigestUtils.md5Hex(data);
	}

	/**
	 * 验证摘要
	 * 
	 * @param data
	 *            待验证的数据
	 * @param messageDigest
	 *            十六进制摘要串
	 * @return boolean 验证成功则为true，反之则为false。
	 */
	public static boolean validate(byte[] data, byte[] messageDigest) {
		return md5Hex(data).equals(messageDigest);

	}
}
