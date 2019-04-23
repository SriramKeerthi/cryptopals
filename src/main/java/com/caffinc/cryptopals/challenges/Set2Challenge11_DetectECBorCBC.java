package com.caffinc.cryptopals.challenges;

import com.caffinc.cryptopals.AESUtil;

import java.security.GeneralSecurityException;

public class Set2Challenge11_DetectECBorCBC {
    public static void main(String[] args) throws GeneralSecurityException {
        for (int i = 0; i < 100; i++) {
            System.out.println(AESUtil.isECB(AESUtil::encryptionOracle));
        }
    }
}
