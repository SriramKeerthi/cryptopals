package com.caffinc.cryptopals.challenges;

import com.caffinc.cryptopals.*;

import javax.crypto.Cipher;
import java.security.GeneralSecurityException;

public class Set2Challenge12_ByteAtATimeECBDecryption {
    private static byte[] appendedData = Base64Util.decode("Um9sbGluJyBpbiBteSA1LjAKV2l0aCBteSByYWctdG9wIGRvd24gc28gbXkg\n" +
            "aGFpciBjYW4gYmxvdwpUaGUgZ2lybGllcyBvbiBzdGFuZGJ5IHdhdmluZyBq\n" +
            "dXN0IHRvIHNheSBoaQpEaWQgeW91IHN0b3A/IE5vLCBJIGp1c3QgZHJvdmUg\n" +
            "YnkK");

    private static byte[] key = AESUtil.generate128BitKey();

    private static final ThrowingFunction<byte[], byte[], GeneralSecurityException> blackBox =
            data ->
                    AESUtil.aesecb(
                            Cipher.ENCRYPT_MODE,
                            key,
                            Util.applyPadding(Util.concat(data, appendedData), 16));

    public static void main(String[] args) throws GeneralSecurityException {
        System.out.println(new String(AESUtil.decryptByteAtATime(blackBox)));
    }
}
