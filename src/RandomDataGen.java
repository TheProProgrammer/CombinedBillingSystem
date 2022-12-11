import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Random;

public class RandomDataGen
{
    static CBSRecord[][][][][] dataset = new CBSRecord[10][4][10][20][3];
    //static String[] customers = {"Jason", "John", "Carl", "Tyler", "Devon", "Travis", "Dustin", "Lucas"};
    static long CID = 0;

    public static void main(String[] args)
    {
        for (byte sector = 1; sector <= dataset.length; sector++)
        {
            for (byte subSector = 1; subSector <= dataset[0].length; subSector++)
            {
                for (byte street = 1; street <= dataset[0][0].length; street++)
                {
                    for (byte house = 1; house <= dataset[0][0][0].length; house++)
                    {
                        for (byte portion = 1; portion <= dataset[0][0][0][0].length; portion++)
                        {
                            dataset[sector-1][subSector-1][street-1][house-1][portion-1] = new CBSRecord();

                            CID++;
                            dataset[sector-1][subSector-1][street-1][house-1][portion-1].CID = CID;

                            dataset[sector-1][subSector-1][street-1][house-1][portion-1].monthYear = "January2022";

                            dataset[sector-1][subSector-1][street-1][house-1][portion-1].customerName =randomName();

                            dataset[sector-1][subSector-1][street-1][house-1][portion-1].utilities[0].currentReading = new Random().nextInt(0,100);

                            dataset[sector-1][subSector-1][street-1][house-1][portion-1].utilities[1].currentReading = new Random().nextInt(0,100);

                            dataset[sector-1][subSector-1][street-1][house-1][portion-1].utilities[2].currentReading = new Random().nextInt(0,100);

                            dataset[sector-1][subSector-1][street-1][house-1][portion-1].utilities[3].currentReading = new Random().nextInt(0,100);

                            dataset[sector-1][subSector-1][street-1][house-1][portion-1].utilities[4].currentReading = new Random().nextInt(0,100);

                            dataset[sector-1][subSector-1][street-1][house-1][portion-1].calculateEachUtilityBill();
                        }
                    }
                }
            }
        }
        saveObject("January2022.ser",dataset);
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
    private static String randomName()
    {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        int length = 5;
        for(int i = 0; i < length; i++) {
            int index = random.nextInt(alphabet.length());
            char randomChar = alphabet.charAt(index);
            sb.append(randomChar);
        }
        return sb.toString();
    }
}
