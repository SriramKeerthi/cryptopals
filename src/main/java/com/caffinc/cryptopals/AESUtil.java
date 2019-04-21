package com.caffinc.cryptopals;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.SecureRandom;
import java.security.spec.InvalidParameterSpecException;
import java.util.Arrays;

public class AESUtil {
    private static SecureRandom random = new SecureRandom();

    public static byte[] aesecb(int mode, byte[] key, byte[] data) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
        cipher.init(mode, new SecretKeySpec(key, "AES"));
        return cipher.doFinal(data);
    }

    public static byte[] aescbc(int mode, byte[] key, byte[] iv, byte[] data) throws GeneralSecurityException {
        if (key.length != 16 && key.length != 32) {
            throw new InvalidKeyException("Key length not 128 or 256 bit");
        }
        if (iv == null || iv.length != 16) {
            throw new InvalidParameterSpecException("IV not valid");
        }
        if (data.length % 16 != 0) {
            throw new BadPaddingException("Data not padded");
        }
        byte[] result = new byte[data.length];
        byte[] previousBlock = Arrays.copyOf(iv, 16);
        for (int i = 0; i < result.length; i += 16) {
            byte[] block;
            if (mode == Cipher.ENCRYPT_MODE) {
                block = XORUtil.xor(Arrays.copyOfRange(data, i, i + 16), iv);
            } else {
                block = Arrays.copyOfRange(data, i, i + 16);
            }
            if (mode == Cipher.ENCRYPT_MODE) {
                previousBlock = aesecb(mode, key, block);
                System.arraycopy(previousBlock, 0, result, i, 16);
            } else {
                System.arraycopy(XORUtil.xor(previousBlock, aesecb(mode, key, block)), 0, result, i, 16);
                previousBlock = block;
            }
        }
        return result;
    }

    public static byte[] generate128BitKey() {
        byte[] key = new byte[16];
        random.nextBytes(key);
        return key;
    }

    public static byte[] encryptionOracle(byte[] data) throws GeneralSecurityException {
        byte[] key = generate128BitKey();
        if (random.nextBoolean()) {
            System.out.println("E");
            return aesecb(Cipher.ENCRYPT_MODE, key, data);
        } else {
            System.out.println("C");
            byte[] iv = new byte[16];
            random.nextBytes(iv);
            return aescbc(Cipher.ENCRYPT_MODE, key, iv, data);
        }
    }
}
