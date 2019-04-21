package com.caffinc.cryptopals;

public class Util {
    public static int hammingDistance(byte[] data1, byte[] data2) {
        if (data1 == null || data2 == null || data1.length != data2.length) {
            throw new IllegalArgumentException("Invalid input");
        }
        byte[] d = XORUtil.xor(data1, data2);
        int hammingDistance = 0;
        for (byte b : d) {
            byte x = 1;
            for (int i = 0; i < 8; i++) {
                hammingDistance += (b & x) == 0 ? 0 : 1;
                x *= 2;
            }
        }
        return hammingDistance;
    }

    public static byte[][] getBlocks(byte[] bytes, int keySize) {
        int blockSize = bytes.length / keySize;
        byte[][] blocks = new byte[keySize][blockSize];
        for (int blockIndex = 0; blockIndex < keySize; blockIndex++) {
            for (int indexInBlock = 0; indexInBlock < blockSize; indexInBlock++) {
                if (indexInBlock * keySize + blockIndex < bytes.length) {
                    blocks[blockIndex][indexInBlock] = bytes[indexInBlock * keySize + blockIndex];
                }
            }
        }
        return blocks;
    }

    public static byte[] applyPadding(byte[] data, int blockSize) {
        int paddingBytes = blockSize - data.length % blockSize;
        if (paddingBytes == 0) {
            paddingBytes = blockSize;
        }
        byte[] result = new byte[data.length + paddingBytes];
        System.arraycopy(data, 0, result, 0, data.length);
        for (int i = data.length; i < result.length; i++) {
            result[i] = 0x04;
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(hammingDistance("this is a test".getBytes(), "wokka wokka!!!".getBytes()));
    }
}
