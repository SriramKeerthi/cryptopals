package com.caffinc.cryptopals;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidParameterSpecException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
                block = XORUtil.xor(Arrays.copyOfRange(data, i, i + 16), previousBlock);
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
        int start = 5 + random.nextInt(6);
        byte[] eData = new byte[data.length + start + 5 + random.nextInt(6)];
        random.nextBytes(eData);
        System.arraycopy(data, 0, eData, start, data.length);
        eData = Util.applyPadding(eData, 16);
        byte[] key = generate128BitKey();
        if (random.nextBoolean()) {
            System.out.println("E");
            return aesecb(Cipher.ENCRYPT_MODE, key, eData);
        } else {
            System.out.println("C");
            byte[] iv = new byte[16];
            random.nextBytes(iv);
            return aescbc(Cipher.ENCRYPT_MODE, key, iv, eData);
        }
    }

    public static boolean isECB(ThrowingFunction<byte[], byte[], GeneralSecurityException> f) throws GeneralSecurityException {
        SecureRandom random = new SecureRandom();
        byte[] randomBlock = new byte[16];
        random.nextBytes(randomBlock);
        byte[] data = new byte[16*4];
        for (int i = 0; i < 4; i++) {
            System.arraycopy(randomBlock, 0, data, i * 16, 16);
        }
        byte[] enc = f.apply(data);
        return Arrays.equals(Arrays.copyOfRange(enc, 16, 32), Arrays.copyOfRange(enc, 32, 48));
    }

    public static int getBlockSize(ThrowingFunction<byte[], byte[], GeneralSecurityException> encrypter) throws GeneralSecurityException {
        int lastLength = encrypter.apply(new byte[1]).length;
        for (int i = 2; i < 32; i++) {
            int length = encrypter.apply(new byte[i]).length;
            if (length > lastLength) {
                return length - lastLength;
            }
        }
        throw new BadPaddingException("Unable to detect block size, possibly bad padding all-round?");
    }

    public static byte[] decryptByteAtATime(ThrowingFunction<byte[], byte[], GeneralSecurityException> encrypter) throws GeneralSecurityException {
        if (!AESUtil.isECB(encrypter)) {
            throw new NoSuchAlgorithmException("Unable to handle CBC yet");
        }

        int blockSize = getBlockSize(encrypter);
        int unknownSize = encrypter.apply(new byte[blockSize]).length - blockSize;

        byte[] discovered = new byte[unknownSize];
        for (int discoveredLength = 0; discoveredLength < unknownSize; discoveredLength++) {
            discovered[discoveredLength] = getNextByte(encrypter, blockSize, discovered, discoveredLength);
        }
        return discovered;
    }

    private static byte getNextByte(ThrowingFunction<byte[], byte[], GeneralSecurityException> encrypter, int blockSize, byte[] discovered, int discoveredLength) throws GeneralSecurityException {
        int unknownBlockIndex = discoveredLength / blockSize + 1;
        Map<String, Byte> encryptedMap = new HashMap<>();
        int discoveredInBlock = Math.min(blockSize - 1, discoveredLength);
        byte[] dummy = Util.concat(new byte[blockSize - discoveredInBlock - 1], Arrays.copyOfRange(discovered, discoveredLength - discoveredInBlock, discoveredLength), new byte[1]);
        for (int b = Byte.MIN_VALUE; b <= Byte.MAX_VALUE; b++) {
            dummy[blockSize - 1] = (byte)b;
            String key = HexUtil.encodeHex(Arrays.copyOf(encrypter.apply(dummy), blockSize));
            encryptedMap.put(key, (byte)b);
        }
        dummy = new byte[blockSize * 2 - 1 - (discoveredLength % blockSize)];
        return encryptedMap.get(
                HexUtil.encodeHex(
                        Arrays.copyOfRange(
                                encrypter.apply(dummy),
                                blockSize * unknownBlockIndex,
                                blockSize * (unknownBlockIndex + 1))));
    }
}
