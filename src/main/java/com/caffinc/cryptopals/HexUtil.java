package com.caffinc.cryptopals;

public class HexUtil {

    public static byte[] decodeHex(String hexVal) {
        return fromHex(hexVal);
    }

    public static byte[] fromHex(String hexVal) {
        if (hexVal.length() % 2 != 0) {
            throw new IllegalArgumentException("Hex Value not valid");
        }
        String hexValLowerCase = hexVal.toLowerCase();
        byte[] result = new byte[hexVal.length() / 2];
        for (int i = 0; i < hexVal.length(); i += 2) {
            byte b = 0;
            for (int j = 0; j < 2; j++) {
                char c = hexValLowerCase.charAt(i + j);
                if (!((c >= 'a' && c <= 'f') || (c>='0' && c<='9'))) {
                    throw new IllegalArgumentException("Character " + hexVal.charAt(i + j) + " not valid Hex");
                }
                b *= 16;
                b += c >= 'a' ? c - 'a' + 10 : c - '0';
            }
            result[i/2] = b;
        }
        return result;
    }

    public static String toHex(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public static String encodeHex(byte[] bytes) {
        return toHex(bytes);
    }

    public static void main(String[] args) {
        System.out.println(toHex(fromHex("fff0")));
    }
}
