import java.io.*;
import java.util.Random;

public class RandomDataFromPrevGen
{
    static CBSRecord[][][][][] dataset = new CBSRecord[10][4][10][20][3];
    //static String[] customers = {"Jason", "John", "Carl", "Tyler", "Devon", "Travis", "Dustin", "Lucas"};

    public static void main(String[] args)
    {
        String fileName = "June2022.ser";
        String fromFileName = "May2022.ser";
        try{
        new File(fileName).createNewFile();
        copyFile(fromFileName, fileName);
        dataset = (CBSRecord[][][][][]) readObject(fileName);
        for (byte sector = 1; sector <= dataset.length; sector++) {
            for (byte subSector = 1; subSector <= dataset[0].length; subSector++) {
                for (byte street = 1; street <= dataset[0][0].length; street++) {
                    for (byte house = 1; house <= dataset[0][0][0].length; house++) {
                        for (byte portion = 1; portion <= dataset[0][0][0][0].length; portion++) {
                            dataset[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].setupForNewMonth();
                            dataset[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].monthYear = "June2022";

                            dataset[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].utilities[0].currentReading = new Random().nextInt(500,600);

                            dataset[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].utilities[1].currentReading = new Random().nextInt(500,600);

                            dataset[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].utilities[2].currentReading = new Random().nextInt(0,100);

                            dataset[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].utilities[3].currentReading = new Random().nextInt(0,100);

                            dataset[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].utilities[3].internationalPhoneMinutes = new Random().nextInt(0,100);

                            dataset[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].utilities[4].currentReading = new Random().nextInt(0,100);

                            dataset[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].calculateEachUtilityBill();
                        }
                    }
                }
            }
        }
        saveObject(fileName, dataset);
    } catch (IOException | ClassNotFoundException e)
        {
        System.out.println("Error, Try again!");
            //e.printStackTrace();
        }
    }
    private static void saveObject(String outputFilename, Object anObject)
    {
        try
        {
            FileOutputStream FOS = new FileOutputStream(outputFilename);
            ObjectOutputStream OOS = new ObjectOutputStream(FOS);
            OOS.writeObject(anObject);
            OOS.flush();
            OOS.close();
        }
        catch (IOException x)
        {
            System.out.println("Error:" + x);
        }
    }
    public static void copyFile(String inputFilename, String outputFilename) {
        try {
            FileInputStream fpin = new FileInputStream(inputFilename);
            FileOutputStream fpout = new FileOutputStream(outputFilename);
            byte[] buffer = new byte[8192];
            int length = 0;
            while ((length = fpin.read(buffer, 0, buffer.length)) > 0) {
                fpout.write(buffer, 0, length);
            }
            fpout.flush();
            fpout.close();
            fpin.close();
        } catch (Exception x) {
            System.out.println("Error:" + x);
        }
    }

    private static Object readObject(String inputFilename) throws IOException, ClassNotFoundException {
        Object anObject = new Object();
        FileInputStream fpin = new FileInputStream(inputFilename);
        ObjectInputStream obIn = new ObjectInputStream(fpin);
        anObject = obIn.readObject();
        obIn.close();
        return anObject;
    }
}
