package com.hungpham.Controller;

import com.hungpham.MainClass;
import com.hungpham.Utils.Utils;
import gnu.io.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.TooManyListenersException;

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

    public static volatile int mode = 0;

    private Thread writeThread;

    private String data;

    public SerialPortController() {
        data = null;
        utils = new Utils();
        init();
    }

    private void init() {
        String osname = System.getProperty("os.name", "").toLowerCase();
        String defaultPort = null;
        if (osname.startsWith("windows")) {
            // windows
            defaultPort = "COM3";
        } else if (osname.startsWith("linux")) {
            // linux
            defaultPort = "/dev/ttyS0";
        } else if (osname.startsWith("mac")) {
            // mac
            defaultPort = "/dev/tty.usbmodemL1000051";
        } else {
            System.out.println("Sorry, your operating system is not supported");
            return;
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
        writeThread = new Thread(this);
        writeThread.start();
    }

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

    public void write(String message) {
        byte[] bytes = utils.stringToHexBytes(message);
        System.out.println("Writing \"" + message + "\" to " + serialPort.getName());
        try {
            outputStream.write(bytes);
        } catch (IOException e) {
        }
    }

    public synchronized void serialEvent(SerialPortEvent event) {
        if (event.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            //System.out.println("Data available: ");
            byte[] readBuffer = new byte[64];
            try {
                while (inputStream.available() > 0) {
                    int numBytes = inputStream.read(readBuffer);
                }
                data = utils.bytesToHexString(readBuffer);
                if (mode == 1) {
                    /**
                     * add data to a queue for the data notifier SensorData to update new value
                     * and notify observers
                     */
                    utils.TCPSend("localhost", Definitions.RECEIVING_SENSOR_VALUE_PORT, data);
                }
                //System.out.println(data);
            } catch (IOException ioe) {
                System.out.println("Exception " + ioe);
            }
        }
    }

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

    public void run() {
        initWrite();
        executeControlHex("autoConnectHex.txt");
        executeControlHex("runSensor");
        mode = 1;
        while (true) {
            if (MainClass.command.equalsIgnoreCase("stop")) {
                mode = 0;
                break;
            }
        }
        executeControlHex("autoDisconnectHex");
        System.exit(0);
    }

}
