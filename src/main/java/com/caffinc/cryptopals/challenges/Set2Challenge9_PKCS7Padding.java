package com.caffinc.cryptopals.challenges;

import com.caffinc.cryptopals.HexUtil;
import com.caffinc.cryptopals.Util;

public class Set2Challenge9_PKCS7Padding {
    public static void main(String[] args) {
        byte[] data = "YELLOW SUBMARINE".getBytes();
        String paddedData = HexUtil.encodeHex(Util.applyPadding(data, 16));
        System.out.println(HexUtil.encodeHex(data));
        System.out.println(paddedData);
    }
}
