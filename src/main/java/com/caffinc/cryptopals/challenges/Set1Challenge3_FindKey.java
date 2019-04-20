package com.caffinc.cryptopals.challenges;

import com.caffinc.cryptopals.HexUtil;
import com.caffinc.cryptopals.XORUtil;

public class Set1Challenge3_FindKey {
    public static void main(String[] args) {
        System.out.println(XORUtil.getSingleByteXOR(HexUtil.decodeHex("1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736")));
    }
}
