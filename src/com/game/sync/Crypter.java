package com.game.sync;

import java.security.Key;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * 加密/解密工具 (AES/ECB/PKCS7Padding )
 * @author issGame.com
 */
public class Crypter {
	private static final String ALGO = "AES/ECB/PKCS7Padding";
	private static final String HEX = "0123456789ABCDEF";
	private static boolean isAddProvider = false;

	/*
	 * 加密字符串,返回Base64字符串
	 * content 带加密的字符串
	 * Password 秘钥(必须为16位的倍数)
	 */
	public static String encryptBase64(String content, String Password) {
		return encrypt(content,new SecretKeySpec(Password.getBytes(), ALGO),false);
	}
	/*
	 * 解密Base64字符串,返回字符串
	 * content 带加密的Base64字符串
	 * Password 秘钥(必须为16位的倍数)
	 */
	public static String decryptBase64(String content, String Password) {
		return decrypt(content,new SecretKeySpec(Password.getBytes(), ALGO),false);
	}
	
	private static String encrypt(String content, Key key,boolean EncodeHex) {
		if (key == null) return null;
		try {
			if (isAddProvider == false) {
				Security.addProvider(new BouncyCastleProvider());
				isAddProvider = true;
			}
			Cipher cipher = Cipher.getInstance(ALGO);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			if (EncodeHex == true) return toHex(cipher.doFinal(content.getBytes()));
			else return Base64.encodeBase64String(cipher.doFinal(content.getBytes()));
		} catch (Exception e) {
			return null;
		}
	}
	
	private static String decrypt(String content, Key key,boolean EncodeHex) {
		if (key == null) return null;
		try {
			if (isAddProvider == false) {
				Security.addProvider(new BouncyCastleProvider());
				isAddProvider = true;
			}
			Cipher cipher = Cipher.getInstance(ALGO);
			cipher.init(Cipher.DECRYPT_MODE, key);
			if (EncodeHex == true) return new String(cipher.doFinal(toByte(content)),"UTF-8");
			else return new String(cipher.doFinal(Base64.decodeBase64(content)),"UTF-8");
		} catch (Exception e) {
			return null;
		}
	}
	
	// Hex to Byte and Byte to Hex
	private static void appendHex(StringBuffer sb, byte b) {
		sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
	}

	private static byte[] toByte(String hexString) {
		int len = hexString.length() / 2;
		byte[] result = new byte[len];
		for (int i = 0; i < len; i++)
			result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2),
					16).byteValue();
		return result;
	}

	private static String toHex(byte[] buff) {
		if (buff == null)
			return "";
		StringBuffer result = new StringBuffer(2 * buff.length);
		for (int i = 0; i < buff.length; i++) {
			appendHex(result, buff[i]);
		}
		return result.toString();
	}
}