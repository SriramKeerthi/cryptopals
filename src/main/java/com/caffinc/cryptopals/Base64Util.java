package com.caffinc.cryptopals;

import java.util.Base64;

public class Base64Util {
    public static byte[] fromBase64(String base64String) {
        return Base64.getDecoder().decode(base64String.replace("\n", ""));
    }

    public static String toBase64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static void main(String[] args) {
        System.out.println(toBase64(fromBase64("SSdtIGtpbGxpbmcgeW91ciBicmFpbiBsaWtlIGEgcG9pc29ub3VzIG11c2hyb29t")));
    }
}
