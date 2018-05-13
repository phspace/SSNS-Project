package com.hungpham.Utils;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Utils {

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHexString(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static byte[] stringToHexBytes(String string) {
        byte[] bytes = new byte[0];
        try {
            bytes = Hex.decodeHex(string.toCharArray());
        } catch (DecoderException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public static ArrayList<String> readHexFromFile(String path) {
        ArrayList<String> result = new ArrayList<String>();
        File file = new File(path);
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                result.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


}
