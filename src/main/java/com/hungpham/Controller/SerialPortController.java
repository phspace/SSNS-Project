package com.hungpham.Controller;

import com.hungpham.Data.SerialData;
import com.hungpham.MainApplication;
import com.hungpham.Utils.Utils;
import gnu.io.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.TooManyListenersException;

import static com.hungpham.Controller.Definitions.OS_NAME;

/**
 * Main control class for Serial port
 * Write and read data from Serial port
 */

public class SerialPortController implements Runnable, SerialPortEventListener {
    private SerialPort serialPort;
    private InputStream inputStream;
    private OutputStream outputStream;
    private static CommPortIdentifier portID;
    private Utils utils;

    private static SerialData[] serialData = new SerialData[2];

    public static volatile int mode = 0;

    private Thread writeThread;

    private String data;

    private String firstPackage = "";
    private String secondPackage = "";
    private String thirdPackage = "";
    private String completePackage = "";

    private int conn;

    public SerialPortController(int conn) {
        this.conn = conn;
        data = null;
        utils = new Utils();
        init();
        serialData[conn] = new SerialData(conn);
    }

    /**
     * open connection with serial port and open writing session
     */
    private void init() {
        String defaultPort = null;
        if (conn == 0) {
            if (OS_NAME.startsWith("windows")) {
                // windows
                defaultPort = "COM3";
            } else if (OS_NAME.startsWith("linux")) {
                // linux
                defaultPort = "/dev/ttyS0";
            } else if (OS_NAME.startsWith("mac")) {
                // mac
                System.out.println("Launchpad 0 connecting");
                defaultPort = "/dev/tty.usbmodemL1000051";
            } else {
                System.out.println("Sorry, your operating system is not supported");
                return;
            }
        } else if (conn == 1) {
            if (OS_NAME.startsWith("windows")) {
                // windows
                defaultPort = "COM3";
            } else if (OS_NAME.startsWith("linux")) {
                // linux
                defaultPort = "/dev/ttyS0";
            } else if (OS_NAME.startsWith("mac")) {
                // mac
                defaultPort = "/dev/tty.usbmodemL1001651";
                System.out.println("Launchpad 1 connecting");
            } else {
                System.out.println("Sorry, your operating system is not supported");
                return;
            }
        }

        try {
            portID = CommPortIdentifier.getPortIdentifier(defaultPort);
            System.out.println("Serial port: " + portID.getName());
            serialPort = (SerialPort) portID.open("SerialPortController", 2000);
            inputStream = serialPort.getInputStream();
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
            serialPort.notifyOnBreakInterrupt(true);
            serialPort.notifyOnCarrierDetect(true);
            serialPort.notifyOnCTS(true);
            serialPort.notifyOnDataAvailable(true);
            serialPort.notifyOnDSR(true);
            serialPort.notifyOnFramingError(true);
            serialPort.notifyOnOutputEmpty(true);
            serialPort.notifyOnOverrunError(true);
            serialPort.notifyOnParityError(true);
            serialPort.notifyOnRingIndicator(true);
            serialPort.setSerialPortParams(115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
        } catch (NoSuchPortException e) {
            e.printStackTrace();
        } catch (PortInUseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TooManyListenersException e) {
            e.printStackTrace();
        } catch (UnsupportedCommOperationException e) {
            e.printStackTrace();
        }
        /**
         * open writing thread
         */
        writeThread = new Thread(this);
        writeThread.start();
    }

    /**
     * initialize writing session
     */
    public void initWrite() {
        try {
            outputStream = serialPort.getOutputStream();
        } catch (IOException e) {
        }

        try {
            serialPort.notifyOnOutputEmpty(true);
        } catch (Exception e) {
            System.out.println("Error setting event notification");
            System.out.println(e.toString());
            System.exit(-1);
        }
    }

    /**
     * write message to serial port
     * @param message
     */
    public void write(String message) {
        byte[] bytes = utils.stringToHexBytes(message);
        System.out.println("Writing \"" + message + "\" to " + serialPort.getName());
        try {
            outputStream.write(bytes);
        } catch (IOException e) {
        }
    }

    /**
     * listen to serial port data
     * @param event
     */
    public synchronized void serialEvent(SerialPortEvent event) {
        if (event.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
                while (inputStream.available() > 0) {
                    byte[] readBuffer = new byte[inputStream.available()];
                    inputStream.read(readBuffer);
                    data = utils.bytesToHexString(readBuffer);
                    if (mode == 1) {
                        /**
                         * add data to a queue for
                         */
//                        System.out.println(data);
                        if (OS_NAME.startsWith("linux")) {
                            // linux
                        } else if (OS_NAME.startsWith("mac") || OS_NAME.startsWith("windows")) {
                            /** package for mac os */

                            if (data.length() == 16) {
                                firstPackage = data;
                            }
                            if (firstPackage.length() == 16 && data.length() > 16) {
                                secondPackage = data;
                                completePackage = firstPackage + secondPackage;
//                                System.out.println(completePackage);
                                SerialData.dataPackage[conn].add(completePackage);
                            }
//                            if ((firstPackage.length() == 16 && secondPackage.length() > 16) && (data.length() == 2 || data.length() == 10)) {
//                                completePackage = completePackage + data;
//                                firstPackage = "";
//                                secondPackage = "";
//                                System.out.println(completePackage);
//                            }
                        } else {
                            System.out.println("Sorry, your operating system is not supported");
                            return;
                        }
                    }
                }

            } catch (IOException ioe) {
                System.out.println("Exception " + ioe);
            }
        }
    }

    /**
     * execute a bunch of commands in a file with path
     * @param path
     */
    private void executeControlHex(String path) {
        ArrayList<String> strings = utils.readHexFromFile(path);
        for (String s : strings) {
            write(s);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * run writing and controlling serial port thread
     */
    public void run() {
        initWrite();
        executeControlHex("setup");
        while (MainApplication.mode == 0) ;
        if (conn == 0) {
            System.out.println("Connecting to sensortag 0");
            executeControlHex("connect1");
        }
        if (conn == 1) {
            System.out.println("Connecting to sensortag 1");
            executeControlHex("connect2");
        }
        executeControlHex("runSensor");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mode = 1;
        while (true) {
            if (MainApplication.command.equalsIgnoreCase("stop") || mode == 0) {
                mode = 0;
                break;
            }
        }
        executeControlHex("autoDisconnectHex");
        //System.exit(0);
        MainApplication.mode = 3;
    }

}
