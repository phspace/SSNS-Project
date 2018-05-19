# SSNS Project

## Instruction:

1. Use BLE Monitor Device to check the MAC address of your SensorTag. If you see "AA:BB:CC:DD:EE:FF" then reverse it to "FFEEDDCCBBAA"
2. Change the MAC address of the SensorTag: open file autoConnectHex, find the line with "6C1C530E6C54", replace it with "FFEEDDCCBBAA"



... To find number of serial port on Macbook: open Terminal, run command "ls /dev/tty.*" and take the smallest number.
it should be like this "/dev/tty.usbmodemL1000051", and change the value in the line "defaultPort = "/dev/tty.usbmodemL1000051";" of class SerialPortController


#### Please install Influxdb and read influxdb Getting Started (https://docs.influxdata.com/influxdb/v1.5/introduction/getting-started/)  before doing these
3. Create new influx database with name "ssns"
4. Create new influx db user : create user "ssns" with password 'ssns-project'
5. grant access: grant ALL on ssns to ssns


6. For mac os: copy librxtxSerial.jnilib and RXTXcomm.jar to /Library/Java/Extensions/
7. For windows: download rxtx library from here: https://bitbucket.org/jlauer/mfz-cdn/downloads/mfz-rxtx-2.2-20081207-win-x64.zip
Installing for Windows: http://rxtx.qbang.org/wiki/index.php/Installation_for_Windows


8. For record data to database: In Main: comment readFromDBExample(); , and uncomment startEverything();


9. For reading data from database: comment startEverything(); and uncomment readFromDBExample();
In the function readFromDBExample(), change the time interval