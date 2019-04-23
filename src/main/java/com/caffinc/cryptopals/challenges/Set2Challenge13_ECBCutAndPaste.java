package com.caffinc.cryptopals.challenges;

import com.caffinc.cryptopals.AESUtil;
import com.caffinc.cryptopals.HexUtil;
import com.caffinc.cryptopals.Util;

import javax.crypto.Cipher;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.Map;

public class Set2Challenge13_ECBCutAndPaste {
    private static Map<String, String> parseCookie(String cookie) throws ParseException, UnsupportedEncodingException {
        Map<String, String> result = new LinkedHashMap<>();
        for (String kv : cookie.split("&")) {
            String[] kvSplit = kv.split("=");
            if (kvSplit.length != 2) {
                throw new ParseException("Invalid cookie", 0);
            }
            result.put(URLDecoder.decode(kvSplit[0], "UTF-8"), URLDecoder.decode(kvSplit[1], "UTF-8"));
        }
        return result;
    }

    private static String profileFor(String email) throws UnsupportedEncodingException {
        return "email=" + URLEncoder.encode(email, "UTF-8") + "&uid=10&role=user";
    }

    private static byte[] key = AESUtil.generate128BitKey();

    private static Map<String, String> getDecryptedProfile(byte[] encryptedEncodedProfile) throws GeneralSecurityException, ParseException, UnsupportedEncodingException {
        return parseCookie(new String(Util.removePadding(AESUtil.aesecb(Cipher.DECRYPT_MODE, key, encryptedEncodedProfile))));
    }

    private static byte[] getEncryptedProfileFor(String email) throws GeneralSecurityException, UnsupportedEncodingException {
        return AESUtil.aesecb(Cipher.ENCRYPT_MODE, key, Util.applyPadding(profileFor(email).getBytes(), 16));
    }

    public static void main(String[] args) throws Exception {
        System.out.println(parseCookie("foo=bar&baz=qux&zap=zazzle"));
        System.out.println(profileFor("foo@bar.com"));
        byte[] cookie = getEncryptedProfileFor("foo@bar.com");
        System.out.println(HexUtil.encodeHex(cookie));
        System.out.println(getDecryptedProfile(cookie));
    }
}
