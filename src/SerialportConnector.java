import jssc.*;

public class SerialportConnector {
    private SerialPort serialPort = null;
    private String result = null;

    public SerialportConnector(int portNummer) {
        //konstruktør oprettes
        String[] portnames = null;//oprettelse af StringArray
        try {
            portnames = SerialPortList.getPortNames();//her hentes navnene til portene der er tilkoblet computeren
            serialPort = new SerialPort(portnames[portNummer]);//objektet serialPort tildeles den første port
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
        try {
            if (serialPort.getInputBufferBytesCount() >= 12) {//kontrolstruktur
                result = serialPort.readString();//strengen aflæses og tildeles result
                String[] rawValues;
                if (result != null && result.charAt(result.length() - 1) == ' ') {//result kontroleres
                    result = result.substring(0, result.length() - 1);//her fjernes det sidste index(#)
                    rawValues = result.split(" ");//nu splittes strengen og gemmes i et array
                    int[] values = new int[rawValues.length];
                    for (int i = 0; i < rawValues.length; i++) {
                        values[i] = Integer.parseInt(rawValues[i]);
                    }
                    return values;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;//returnArray returneres
    }
}