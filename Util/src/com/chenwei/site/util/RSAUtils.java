package com.chenwei.site.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chenwei
 * @date 2019-01-30 14:28
 **/
public class RSAUtils {

    /**
     * 加密算法RSA
     */
    public static final String KEY_ALGORITHM = "RSA";


    /**
     * 签名算法
     */
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";


    /**
     * 获取公钥的key
     */
    private static final String PUBLIC_KEY = "RSAPublicKey";


    /**
     * 获取私钥的key
     */
    private static final String PRIVATE_KEY = "RSAPrivateKey";


    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;


    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;


    /**
     * RSA 位数 如果采用2048 上面最大加密和最大解密则须填写:  245 256
     */
    private static final int INITIALIZE_LENGTH = 1024;


    /**
     * <p>
     * 生成密钥对(公钥和私钥)
     * </p>
     *
     * @return
     * @throws Exception
     */
    public static Map<String, Object> genKeyPair() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(INITIALIZE_LENGTH);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        Map<String, Object> keyMap = new HashMap<String, Object>(2);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }


    /**
     * <p>
     * 用私钥对信息生成数字签名
     * </p>
     *
     * @param data       已加密数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static String sign(byte[] data, String privateKey) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateK);
        signature.update(data);
        return Base64.encodeBase64String(signature.sign());
    }


    /**
     * <p>
     * 校验数字签名
     * </p>
     *
     * @param data      已加密数据
     * @param publicKey 公钥(BASE64编码)
     * @param sign      数字签名
     * @return
     * @throws Exception
     */
    public static boolean verify(byte[] data, String publicKey, String sign) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicK = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicK);
        signature.update(data);
        return signature.verify(Base64.decodeBase64(sign));
    }

    private static byte[] getBytes(byte[] data, String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {
        return getBytesReal(data, publicKey, Cipher.ENCRYPT_MODE, MAX_ENCRYPT_BLOCK);
    }

    private static byte[] getBytesReal(byte[] data, String publicKey, int encryptMode, int maxEncryptBlock) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {
        byte[] keyBytes = Base64.decodeBase64(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        return getCipher(data, encryptMode, maxEncryptBlock, keyFactory, publicK);
    }

    private static byte[] getCipher(byte[] data, int encryptMode, int maxEncryptBlock, KeyFactory keyFactory, Key publicK) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(encryptMode, publicK);
        int inputLen = data.length;
        return getBytes(data, maxEncryptBlock, cipher, inputLen);
    }

    private static byte[] getBytes(byte[] data, String privateKey, int encryptMode, int maxEncryptBlock) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {
        byte[] keyBytes = Base64.decodeBase64(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        return getCipher(data, encryptMode, maxEncryptBlock, keyFactory, privateK);
    }

    private static byte[] getBytes(byte[] data, int maxEncryptBlock, Cipher cipher, int inputLen) throws IllegalBlockSizeException, BadPaddingException, IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > maxEncryptBlock) {
                cache = cipher.doFinal(data, offSet, maxEncryptBlock);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * maxEncryptBlock;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    /**
     * 私钥解密
     *
     * @param encryptedData 已加密数据
     * @param privateKey    私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    private static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey) throws Exception {
        return getBytes(encryptedData, privateKey, Cipher.DECRYPT_MODE, MAX_DECRYPT_BLOCK);
    }

    /**
     * <p>
     * 公钥加密
     * </p>
     *
     * @param data      源数据
     * @param publicKey 公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    private static byte[] encryptByPublicKey(byte[] data, String publicKey) throws Exception {
        return getBytes(data, publicKey);
    }

    /**
     * <p>
     * 公钥解密
     * </p>
     *
     * @param encryptedData 已加密数据
     * @param publicKey     公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    private static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey) throws Exception {
        return getBytesReal(encryptedData, publicKey, Cipher.DECRYPT_MODE, MAX_DECRYPT_BLOCK);
    }

    /**
     * <p>
     * 私钥加密
     * </p>
     *
     * @param data       源数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    private static byte[] encryptByPrivateKey(byte[] data, String privateKey) throws Exception {
        return getBytes(data, privateKey, Cipher.ENCRYPT_MODE, MAX_ENCRYPT_BLOCK);
    }


    /**
     * java端私钥加密
     *
     * @param data
     * @param publicKey
     * @return
     */
    public static String encryptByPrivateKeyOnJava(String data, String publicKey) {
        try {
            data = Base64.encodeBase64String(encryptByPrivateKey(data.getBytes(), publicKey));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * java端公钥解密
     *
     * @param data
     * @param privateKey
     * @return
     */
    public static String decryptByPublicKeyOnJava(String data, String privateKey) {
        try {
            byte[] rs = Base64.decodeBase64(data);
            data = new String(RSAUtils.decryptByPublicKey(rs, privateKey), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * java端公钥加密
     */
    public static String encryptByPublicKeyOnJava(String data, String publicKey) {
        try {
            data = Base64.encodeBase64String(encryptByPublicKey(data.getBytes(), publicKey));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * java端私钥解密
     */
    public static String decryptByPrivateKeyOnJava(String data, String privateKey) {
        try {
            byte[] rs = Base64.decodeBase64(data);
            data = new String(RSAUtils.decryptByPrivateKey(rs, privateKey), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }


    /**
     * <p>
     * 获取私钥
     * </p>
     *
     * @param keyMap 密钥对
     * @return
     * @throws Exception
     */
    public static String getPrivateKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return Base64.encodeBase64String(key.getEncoded());
    }


    /**
     * <p>
     * 获取公钥
     * </p>
     *
     * @param keyMap 密钥对
     * @return
     * @throws Exception
     */
    public static String getPublicKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return Base64.encodeBase64String(key.getEncoded());
    }

    public static void main(String[] args) throws Exception {
        //获取公钥私钥
        Map<String, Object> stringObjectMap = genKeyPair();
        String publicKey = getPublicKey(stringObjectMap);
        String privateKey = getPrivateKey(stringObjectMap);
        //公钥加密，私钥解密
        String test = "test";
        String encrypt = encryptByPublicKeyOnJava("test", publicKey);
        System.out.println("公钥加密前：" + test + "    加密结果：" + encrypt);
        String decrypt = decryptByPrivateKeyOnJava(encrypt, privateKey);
        System.out.println("私钥解密结果：" + decrypt);
        //私钥加密，公钥解密
        encrypt = encryptByPrivateKeyOnJava("test", privateKey);
        System.out.println("私钥加密前：" + test + "    加密结果：" + encrypt);
        decrypt = decryptByPublicKeyOnJava(encrypt, publicKey);
        System.out.println("公钥解密结果：" + decrypt);
    }
}
