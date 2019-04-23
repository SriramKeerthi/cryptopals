package com.caffinc.cryptopals.challenges;

import com.caffinc.cryptopals.Base64Util;
import com.caffinc.cryptopals.HexUtil;

public class Set1Challenge1_HexToBase64 {
    public static void main(String[] args) {
        System.out.println(Base64Util.encode(HexUtil.fromHex("49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d")).equals("SSdtIGtpbGxpbmcgeW91ciBicmFpbiBsaWtlIGEgcG9pc29ub3VzIG11c2hyb29t"));
    }
}
