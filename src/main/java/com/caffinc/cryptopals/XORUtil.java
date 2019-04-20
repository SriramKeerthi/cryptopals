package com.caffinc.cryptopals;

import java.util.*;

public class XORUtil {
    public static byte[] xor(byte[]... byteArrays) {
        if (byteArrays.length == 0) {
            throw new IllegalArgumentException("No byte arrays passed");
        } else if (byteArrays.length == 1) {
            return byteArrays[0];
        }
        int len = byteArrays[0].length;
        for (int i = 1; i < byteArrays.length; i++) {
            if (byteArrays[i].length != len) {
                throw new IllegalArgumentException("Not all byte arrays are of equal length");
            }
        }
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++) {
            result[i] = 0;
            for (int j = 0; j < byteArrays.length; j++) {
                result[i] ^= byteArrays[j][i];
            }
        }
        return result;
    }

    public static byte[] repeatingXor(byte[] key, byte[] data) {
        if (key == null || key.length == 0) {
            throw new IllegalArgumentException("Invalid key");
        }
        if (data == null) {
            throw new IllegalArgumentException("data is null") ;
        }
        byte[] result = new byte[data.length];
        for (int i = 0; i < data.length; i++) {
            result[i] = (byte) (data[i] ^ key[i % key.length]);
        }
        return result;
    }

    public static List<Pair<Byte, Pair<Double, String>>> getSingleByteXOR(byte[][] blocks) {
        List<Pair<Byte, Pair<Double, String>>> result = new ArrayList<>();
        for (byte[] data : blocks) {
            result.add(getSingleByteXOR(data));
        }
        return result;
    }

    public static Pair<Byte, Pair<Double, String>> getSingleByteXOR(byte[] data) {
        Map<Byte, Pair<Byte, Integer>> freqMap = new HashMap<>();
        for (byte b : data) {
            if (!freqMap.containsKey(b)) {
                freqMap.put(b, new Pair<>(b, 0));
            }
            freqMap.get(b).setT2(freqMap.get(b).getT2() + 1);
        }

        List<Pair<Byte, Integer>> sortedFreq = new ArrayList<>(freqMap.values());
        sortedFreq.sort(Comparator.comparing(Pair::getT2));

        double highestScore = 0;
        String bestText = null;
        byte key = '?';
        for (int i = sortedFreq.size() - 1; i >= 0; i--) {
            Pair<Byte, Integer> pair = sortedFreq.get(i);
            for (byte c : " ETAOINSHRDLU".getBytes()) {
                for (int ca = 0; ca < 2; ca++) {
                    if (ca == 0) {
                        c = (byte) Character.toLowerCase(c);
                    } else {
                        c = (byte) Character.toUpperCase(c);
                    }
                    byte k = (byte) (pair.getT1() ^ c);
                    byte[] clearText = new byte[data.length];
                    for (int j = 0; j < clearText.length; j++) {
                        clearText[j] = (byte) (data[j] ^ k);
                    }
                    String text = new String(clearText);
                    double score = scoreText(text);
                    if (score > highestScore) {
                        highestScore = score;
                        bestText = text;
                        key = k;
                    }
                }
            }
        }
        return new Pair<>(key, new Pair<>(highestScore, bestText));
    }

    public static double scoreText(String text) {
        double score = 0;
        String commonChars = " ETAOINSHRDLU";
        for (char c : text.toCharArray()) {
            c = Character.toUpperCase(c);
            if (commonChars.indexOf(c) >= 0) {
                score += 1 + (commonChars.length() - commonChars.indexOf(c) / (double) commonChars.length());
            } else if (!(
                    (c >= 'A' && c <= 'Z') ||
                            (c >= '0' && c <= '9'))) {
                score--;
            }
        }
        return score;
    }

    public static void main(String[] args) {
        System.out.println(HexUtil.toHex(xor(HexUtil.decodeHex("0011"), HexUtil.decodeHex("0001"))));
        System.out.println(HexUtil.toHex(xor(HexUtil.decodeHex("0010"), HexUtil.decodeHex("0001"))));
        System.out.println(HexUtil.toHex(xor(HexUtil.decodeHex("0011"), HexUtil.decodeHex("0010"))));
    }
}
