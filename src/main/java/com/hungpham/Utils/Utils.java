package com.hungpham.Utils;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Utils {

    private final char[] hexArray = "0123456789ABCDEF".toCharArray();

    public String bytesToHexString(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public byte[] stringToHexBytes(String string) {
        byte[] bytes = new byte[0];
        try {
            bytes = Hex.decodeHex(string.toCharArray());
        } catch (DecoderException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public ArrayList<String> readHexFromFile(String path) {
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

    public int hexStringToInt(String hexString) {
        int result = 0;
        result = (int) Long.parseLong(hexString, 16);
        return result;
    }

    public ArrayList<String> seperate4Hex(String hexString) {
        if (!(hexString == null)) {
            ArrayList<String> hexList = new ArrayList<>();
            for (int i = 0; i < hexString.length(); i = i + 4) {
                hexList.add(hexString.substring(i, i + 4));
            }
            return hexList;
        } else return null;
    }

    public ArrayList<String> seperate2Hex(String hexString) {
        if (!(hexString == null)) {
            ArrayList<String> hexList = new ArrayList<>();
            for (int i = 0; i < hexString.length(); i = i + 2) {
                hexList.add(hexString.substring(i, i + 2));
            }
            return hexList;
        } else return null;
    }

    public void TCPSend(String IP, int port, String message) {
        try {
            Socket s = new Socket(IP, port);
            DataOutputStream out = new DataOutputStream(s.getOutputStream());
            DataInputStream in = new DataInputStream(s.getInputStream());
            out.writeUTF(message);
            String data = in.readUTF();
            //System.out.println("Received: " + data);
            s.close();
        } catch (UnknownHostException e) {
            System.out.println(" Sock:" + e.getMessage());
        } catch (EOFException e) {
            System.out.println(" EOF:" + e.getMessage());
        } catch (IOException e) {
            System.out.println(" IO:" + e.getMessage() + " (Connection to " + port + ")");
        }
    }

    public String TCPReceive(int port) {
        ServerSocket listenSocket = null;
        DataInputStream in;
        DataOutputStream out;
        String message = "";
        try {

            listenSocket = new ServerSocket(port);
            Socket clientSocket = listenSocket.accept();
            out = new DataOutputStream(clientSocket.getOutputStream());
            in = new DataInputStream(clientSocket.getInputStream());
            message = in.readUTF();
            out.writeUTF(message);
            //System.out.println("Sent data back: " + message);
            clientSocket.close();
            listenSocket.close();
        } catch (EOFException e) {
            System.out.println(" EOF:" + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return message;
    }

}
