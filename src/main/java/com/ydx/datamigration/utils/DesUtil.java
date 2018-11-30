package com.ydx.datamigration.utils;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * DES加密工具
 */
public class DesUtil {
    public static final byte[] initKey = new byte[]{0, 0, 0, 0, 0, 0, 0, 0};
    public static final byte[] keyArray = "12345678abcdefgh87654321".getBytes();
    public static final byte[] keyArray2 = "hgfedcda87654321abcdefgh".getBytes();
    private static final String AlgorithmAll = "DES/CBC/NoPadding";

    public DesUtil() {
    }

    public static byte[] desEncrypt(byte[] ivstr, byte[] src, byte[] key) {
        try {
            IvParameterSpec e3 = null;
            if(ivstr != null) {
                e3 = new IvParameterSpec(ivstr);
            } else {
                e3 = new IvParameterSpec(initKey);
            }

            DESKeySpec dks = new DESKeySpec(key);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance("DES/CBC/NoPadding", "SunJCE");
            cipher.init(1, securekey, e3);
            return cipher.doFinal(src);
        } catch (NoSuchAlgorithmException var8) {
            var8.printStackTrace();
        } catch (NoSuchPaddingException var9) {
            var9.printStackTrace();
        } catch (Exception var10) {
            var10.printStackTrace();
        }

        return null;
    }

    public static byte[] desDecrypt(byte[] ivstr, byte[] src, byte[] key) {
        try {
            IvParameterSpec e3 = null;
            if(ivstr != null) {
                e3 = new IvParameterSpec(ivstr);
            } else {
                e3 = new IvParameterSpec(initKey);
            }

            DESKeySpec dks = new DESKeySpec(key);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance("DES/CBC/NoPadding", "SunJCE");
            cipher.init(2, securekey, e3);
            return cipher.doFinal(src);
        } catch (NoSuchAlgorithmException var8) {
            var8.printStackTrace();
        } catch (NoSuchPaddingException var9) {
            var9.printStackTrace();
        } catch (Exception var10) {
            var10.printStackTrace();
        }

        return null;
    }

    public static String fillInData(String data) {
        while(data.length() % 8 != 0 || data.length() <= 8) {
            data = data + "*";
        }

        return data;
    }

    public static String desEncrypt(String data) {
        data = fillInData(data);
        byte[] dataArray = data.getBytes();
        byte[] target = desEncrypt(initKey, dataArray, keyArray);
        return Base64.getEncoder().encodeToString(target);
    }

    public static String desDecrypt(String data) throws IOException {
        data = fillInData(data);
        byte[] targetArrayDB = Base64.getDecoder().decode(data);
        byte[] dataNew = desDecrypt(initKey, targetArrayDB, keyArray);
        String olderData = new String(dataNew, 0, dataNew.length);
        int location = olderData.indexOf("*");
        if(location != -1) {
            olderData = olderData.substring(0, location);
        }

        return olderData;
    }

    public static void main(String[] args) throws Exception {
        for(int i = 0; i < args.length; i += 2) {
            String operaName = args[i];
            String data = args[i + 1];
            data = fillInData(data);
            byte[] targetArrayDB;
            String olderData;
            byte[] dataNew;
            if("desEncrypt".equals(operaName)) {
                dataNew = data.getBytes();
                targetArrayDB = desEncrypt(initKey, dataNew, keyArray);
                olderData = Base64.getEncoder().encodeToString(targetArrayDB);
                System.out.println("加密后的数据为：" + olderData);
                break;
            }

            if("desDecrypt".equals(operaName)) {
                targetArrayDB = Base64.getDecoder().decode(data);
                dataNew = desDecrypt(initKey, targetArrayDB, keyArray);
                olderData = new String(dataNew, 0, dataNew.length);
                int location = olderData.indexOf("*");
                if(location != -1) {
                    olderData.substring(0, location);
                }
                break;
            }
        }

    }
}
