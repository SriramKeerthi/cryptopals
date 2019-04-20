package com.caffinc.cryptopals.challenges;

import com.caffinc.cryptopals.HexUtil;
import com.caffinc.cryptopals.XORUtil;

public class Set1Challenge5_RepeatingKeyXOR {
    public static void main(String[] args) {
        String data = "Burning 'em, if you ain't quick and nimble\nI go crazy when I hear a cymbal";
        System.out.println(("0b3637272a2b2e63622c2e69692a23693a2a3c6324202d623d63343c2a26226324272765272" +
                "a282b2f20430a652e2c652a3124333a653e2b2027630c692b20283165286326302e27282f")
                .equals(HexUtil.toHex(XORUtil.repeatingXor("ICE".getBytes(), data.getBytes()))));
    }
}
