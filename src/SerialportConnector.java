import jssc.*;

public class SerialportConnector {
    private SerialPort serialPort = null;
    private String result = null;

    public SerialportConnector() {
        //konstruktør oprettes

        String[] portnames = null;//oprettelse af StringArray

        try {
            portnames = SerialPortList.getPortNames();//her hentes navnene til portene der er tilkoblet computeren
            serialPort = new SerialPort(portnames[0]);//objektet serialPort tildeles den første port
            serialPort.openPort();//porten åbnes
            serialPort.setRTS(true);//klar til at sende(ReadyToSend = true)
            serialPort.setDTR(true);//klar til at modtage(DataToReceive = true)
            serialPort.setParams(115200, 8, 1, SerialPort.PARITY_NONE);//parametre bestemmes
            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);//kontrolere flowet af data

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public int[] getData() {//metoden oprettes
        String[] rawValues = new String[10];//StringArray'et rawValues oprettes og længen bestemmes
        int ir = 0;//initialisering af lokale variable
        int red = 0;
        while (ir == 0 || red == 0) {//løkke oprettes
            try {
                if (serialPort.getInputBufferBytesCount() >= 12) {//kontrolstruktur
                    result = serialPort.readString();//strengen aflæses og tildeles result
                    Thread.sleep(20);//forsinkelse bestemmes til 20ms
                    if (result != null && result.charAt(result.length() - 1) == '#') {//result kontroleres
                        result = result.substring(0, result.length() - 1);//her fjernes det sidste index(#)
                        rawValues = result.split(" ");//nu splittes strengen og gemmes i et array
                    }
                    if (rawValues != null && rawValues.length >= 2) {//kontrollere om rawValues har nok indexer til konvertering
                        try {
                            ir = Integer.parseInt(rawValues[0]);//0. index konverteres til Integer og tildeles ir
                        } catch (Exception e) {//hvis der er et problem tildeles ir værdien 0
                            ir = 0;
                        }
                        try {
                            red = Integer.parseInt(rawValues[1]);//1. index konverteres til Integer og tildeles red
                        } catch (Exception e) {//hvis der er et problem tildeles red værdien 0
                            red = 0;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        int[] returnArray = new int[]{ir, red};//returnArray oprettes med ir som 0. index og red som 1. index
        return returnArray;//returnArray returneres
    }
}

