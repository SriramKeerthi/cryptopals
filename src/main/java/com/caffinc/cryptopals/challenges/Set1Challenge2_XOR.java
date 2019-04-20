package com.caffinc.cryptopals.challenges;

import com.caffinc.cryptopals.HexUtil;
import com.caffinc.cryptopals.XORUtil;

public class Set1Challenge2_XOR {
    public static void main(String[] args) {
        byte[] array1 = HexUtil.fromHex("1c0111001f010100061a024b53535009181c");
        byte[] array2 = HexUtil.fromHex("686974207468652062756c6c277320657965");
        byte[] result = XORUtil.xor(array1, array2);
        System.out.println(HexUtil.toHex(result).equals("746865206b696420646f6e277420706c6179"));
    }
}
