import java.io.*;
import java.util.*;

public class CBS implements Serializable {
    static CBSRecord[][][][][] dataset = new CBSRecord[10][4][10][20][3];
    //static CBSRecord[][][][][] dataset = new CBSRecord[10][2][2][2][1];
    static List<CBSRecord[][][][][]> datasets = new ArrayList<>();

    static char[] sectors = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};
    static String[] portions = {"Ground", "First", "Second"};
    static String[] utilities = {"Electricity", "Gas", "Water", "Phone", "Internet"};
    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            showMenu();
        }
    }

    private static void showMenu() {
        System.out.print("\n____MENU____\n1.Add\n2.Read\n3.Reports\n4.Modification\n0.Exit\n\nSelection:");
        switch (input.nextByte()) {
            case 1 -> addRecordMenu();
            case 2 -> readRecordMenu();
            case 3 -> reportMenu();
            case 4 -> {
                try {modification();} catch (Exception e) {System.out.println("There was an error with your query please check and try again!");}
            }
            case 0 -> System.exit(0);
            default -> {
                System.out.println("Invalid Input, Try again");
                showMenu();
            }
        }
    }

    private static void reportMenu() {
        System.out.println("""
                1. the maximum and/or minimum bill for a specific utility or house or street or sub-block or block.
                2. Month-wise billing report for a specific consumer for a specified period.
                3. Detailed report of consumers not using all or a specific utility between a specified period.
                4. Yearly reports based on the total bill for each consumer.
                Selection: """);
        switch (input.nextByte()) {
            case 1:
                maxMin();
                break;
            case 2:
                monthWiseReportMenu();
                break;
            case 3:
                nonUsageReport();
                break;
            case 4:
                yearlyReport();
                break;
        }
    }

    private static void readRecordMenu() {
        String fileName = "data/";
        System.out.print("For which month: ");
        fileName += input.next();
        System.out.print("For which year: ");
        fileName += input.next();
        fileName += ".ser";

        if (new File(fileName).exists()) {
            try {
                dataset = (CBSRecord[][][][][]) readObject(fileName);
                for (byte sector = 1; sector <= dataset.length; sector++) {
                    for (byte subSector = 1; subSector <= dataset[0].length; subSector++) {
                        for (byte street = 1; street <= dataset[0][0].length; street++) {
                            for (byte house = 1; house <= dataset[0][0][0].length; house++) {
                                for (byte portion = 1; portion <= dataset[0][0][0][0].length; portion++) {
                                    System.out.printf("Address: Sector %c, SubSector %d, Street %d, House %d, %s Floor\n", sectors[sector - 1], subSector, street, house, portions[portion - 1]);

                                    System.out.printf("CID: %d\n", dataset[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].CID);

                                    System.out.printf("Name: %s\n", dataset[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].customerName);

                                    System.out.printf("Electricity Units: %d\n", dataset[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].utilities[0].unitsConsumed);
                                    System.out.printf("Electricity Bill: %d pkr\n", dataset[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].utilities[0].dueAmount);

                                    System.out.printf("Gas Units: %d\n", dataset[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].utilities[1].unitsConsumed);
                                    System.out.printf("Gas Bill: %d pkr\n", dataset[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].utilities[1].dueAmount);

                                    System.out.printf("Water Units: %d\n", dataset[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].utilities[2].unitsConsumed);
                                    System.out.printf("Water Bill: %d pkr\n", dataset[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].utilities[2].dueAmount);

                                    System.out.printf("Phone Minutes (Local): %d\n", dataset[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].utilities[3].unitsConsumed);
                                    System.out.printf("Phone Minutes (International): %d\n", dataset[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].utilities[3].internationalPhoneMinutes);
                                    System.out.printf("Phone Bill: %d pkr\n", dataset[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].utilities[3].dueAmount);

                                    System.out.printf("Internet Units: %d\n", dataset[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].utilities[4].unitsConsumed);
                                    System.out.printf("Internet Bill: %d pkr\n", dataset[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].utilities[4].dueAmount);

                                    System.out.printf("Total Bill: %d pkr\n\n", dataset[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].allUtilitiesBill());
                                }
                            }
                        }
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error encountered, Try again!");
            }
        } else {
            System.out.println("No Such File Exists");
        }
    }

    private static void addRecordMenu() {
        String monthYear = "";

        String fileName = "data/";
        System.out.print("For which month: ");
        monthYear += input.next();
        System.out.print("For which Year: ");
        monthYear += input.next();

        fileName += monthYear;
        fileName += ".ser";

        System.out.print("Do you want to inherit from previous readings? (y/n) ");

        if (input.next().toCharArray()[0] == 'y') {
            String fromFileName = "data/";
            System.out.print("Previous month: ");
            fromFileName += input.next();
            System.out.print("Previous month's Year: ");
            fromFileName += input.next();
            fromFileName += ".ser";

            try {
                new File(fileName).createNewFile();
                copyFile(fromFileName, fileName);
                dataset = (CBSRecord[][][][][]) readObject(fileName);
                for (byte sector = 1; sector <= dataset.length; sector++) {
                    for (byte subSector = 1; subSector <= dataset[0].length; subSector++) {
                        for (byte street = 1; street <= dataset[0][0].length; street++) {
                            for (byte house = 1; house <= dataset[0][0][0].length; house++) {
                                for (byte portion = 1; portion <= dataset[0][0][0][0].length; portion++) {
                                    dataset[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].setupForNewMonth();
                                    dataset[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].monthYear = monthYear;

                                    System.out.printf("\nAddress: Sector %c, SubSector %d, Street %d, House %d, %s Floor\n", sectors[sector - 1], subSector, street, house, portions[portion - 1]);

                                    System.out.printf("CID: %d\n", dataset[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].CID);

                                    System.out.printf("Name: %s\n", dataset[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].customerName);

                                    System.out.println("Previous Electricity Reading: " + dataset[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].utilities[0].prevReading);
                                    System.out.print("Enter current Electricity Reading: ");
                                    dataset[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].utilities[0].currentReading = input.nextInt();

                                    System.out.println("Previous Gas Reading: " + dataset[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].utilities[1].prevReading);
                                    System.out.print("Enter current Gas Reading: ");
                                    dataset[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].utilities[1].currentReading = input.nextInt();

                                    System.out.print("Enter current Water Consumption in L: ");
                                    dataset[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].utilities[2].currentReading = input.nextInt();

                                    System.out.print("Enter Phone Local Minutes: ");
                                    dataset[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].utilities[3].currentReading = input.nextInt();

                                    System.out.print("Enter Phone International Minutes: ");
                                    dataset[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].utilities[3].internationalPhoneMinutes = input.nextInt();

                                    System.out.print("Enter Internet Usage in GBs: ");
                                    dataset[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].utilities[4].currentReading = input.nextInt();

                                    dataset[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].calculateEachUtilityBill();
                                }
                            }
                        }
                    }
                }
                saveObject(fileName, dataset);
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error, Try again!");
                //e.printStackTrace();
            }
        } else {
            long CID = 0;
            for (byte sector = 1; sector <= dataset.length; sector++) {
                for (byte subSector = 1; subSector <= dataset[0].length; subSector++) {
                    for (byte street = 1; street <= dataset[0][0].length; street++) {
                        for (byte house = 1; house <= dataset[0][0][0].length; house++) {
                            for (byte portion = 1; portion <= dataset[0][0][0][0].length; portion++) {
                                dataset[sector - 1][subSector - 1][street - 1][house - 1][portion - 1] = new CBSRecord();

                                CID++;
                                dataset[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].CID = CID;

                                System.out.printf("\nAddress: Sector %c, SubSector %d, Street %d, House %d, %s Floor\n", sectors[sector - 1], subSector, street, house, portions[portion - 1]);
                                System.out.printf("CID: %d\n", dataset[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].CID);


                                System.out.print("Enter Customer Name: ");
                                dataset[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].customerName = input.next();

                                System.out.print("Enter Electricity Reading: ");
                                dataset[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].utilities[0].currentReading = input.nextInt();

                                System.out.print("Enter Gas Reading: ");
                                dataset[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].utilities[1].currentReading = input.nextInt();

                                System.out.print("Enter Water Consumption in L: ");
                                dataset[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].utilities[2].currentReading = input.nextInt();

                                System.out.print("Enter Phone Local Minutes: ");
                                dataset[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].utilities[3].currentReading = input.nextInt();
                                System.out.print("Enter Phone International Minutes: ");
                                dataset[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].utilities[3].internationalPhoneMinutes = input.nextInt();

                                System.out.print("Enter Internet Usage in GBs: ");
                                dataset[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].utilities[4].currentReading = input.nextInt();

                                dataset[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].calculateEachUtilityBill();
                            }
                        }
                    }
                }
            }
            saveObject(fileName, dataset);
        }
    }

    private static void saveObject(String outputFilename, Object anObject) {
        try {
            FileOutputStream FOS = new FileOutputStream(outputFilename);
            ObjectOutputStream OOS = new ObjectOutputStream(FOS);
            OOS.writeObject(anObject);
            OOS.flush();
            OOS.close();
        } catch (IOException x) {
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

    private static boolean containsByte(byte[] array, byte key) {
        for (byte element : array) {
            if (element == key) {
                return true;
            }
        }
        return false;
    }

    private static void nonUsageReport() {
        loadAllDataSets();
        System.out.println("Data of users who didn't use a certain utility");
        for (byte datafile = 1; datafile <= datasets.size(); datafile++) {
            for (byte sector = 1; sector <= datasets.get(datafile - 1).length; sector++) {
                for (byte subSector = 1; subSector <= datasets.get(datafile - 1)[sector - 1].length; subSector++) {
                    for (byte street = 1; street <= datasets.get(datafile - 1)[sector - 1][subSector - 1].length; street++) {
                        for (byte house = 1; house <= datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1].length; house++) {
                            for (byte portion = 1; portion <= datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1].length; portion++) {
                                for (byte utils = 1; utils <= datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].utilities.length; utils++) {
                                    if (datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].utilities[utils - 1].unitsConsumed == 0) {
                                        //print is waqt ka data
                                        System.out.println("For the month of: " + datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].monthYear);
                                        System.out.println(datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].customerName +
                                                " against CID: " +
                                                datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].CID +
                                                " did not consume any units for the utility of " +
                                                utilities[utils - 1]);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static void yearlyReport() {
        loadAllDataSets();
        System.out.print("Enter CID of user: ");
        String name = "";
        long CID = input.nextLong();
        for (byte datafile = 1; datafile <= datasets.size(); datafile++) {
            for (byte sector = 1; sector <= datasets.get(datafile - 1).length; sector++) {
                for (byte subSector = 1; subSector <= datasets.get(datafile - 1)[sector - 1].length; subSector++) {
                    for (byte street = 1; street <= datasets.get(datafile - 1)[sector - 1][subSector - 1].length; street++) {
                        for (byte house = 1; house <= datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1].length; house++) {
                            for (byte portion = 1; portion <= datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1].length; portion++) {
                                if (CID == datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].CID) {
                                    name = datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].customerName;
                                }
                            }
                        }
                    }
                }
            }
        }
        System.out.println(name + "'s Yearly Report\n");
        for (byte datafile = 1; datafile <= datasets.size(); datafile++) {
            for (byte sector = 1; sector <= datasets.get(datafile - 1).length; sector++) {
                for (byte subSector = 1; subSector <= datasets.get(datafile - 1)[sector - 1].length; subSector++) {
                    for (byte street = 1; street <= datasets.get(datafile - 1)[sector - 1][subSector - 1].length; street++) {
                        for (byte house = 1; house <= datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1].length; house++) {
                            for (byte portion = 1; portion <= datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1].length; portion++) {
                                if (CID == datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].CID) {
                                    System.out.println("For the month of " + datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].monthYear);
                                    System.out.printf("Total bill was %d \n\n", datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].allUtilitiesBill());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static void monthWiseReportMenu() {
        loadAllDataSets();
        System.out.print("Enter CID of user: ");
        long CID = input.nextLong();
        System.out.print("1. For all Utilities\n2. For a specific utility\nSelection: ");
        switch (input.nextInt()) {
            case 1:
                monthWiseReport(CID, new byte[]{0, 1, 2, 3, 4});
                break;
            case 2:
                System.out.print("For which utility?\n1. Electricity\n2. Gas\n3. Water\n4. Phone\n5. Internet\n Selection: ");
                byte selection = input.nextByte();
                if ((0 <= selection) && (selection <= 5)) {
                    monthWiseReport(CID, new byte[]{selection});
                } else {
                    System.out.print("Invalid Input try again!");
                    monthWiseReportMenu();
                }
                break;
            default:
                System.out.println("Invalid input try again!");
                monthWiseReportMenu();
                break;
        }
    }

    private static void modification() throws Exception
    {
        String fileName = "";
        System.out.print("In which month: ");
        fileName += input.nextLine();
        System.out.print("In which year: ");
        fileName += input.nextLine();
        fileName += ".ser";
        dataset = (CBSRecord[][][][][]) readObject(fileName);
        System.out.println("Which Sector: (1 is A, 2 is B and so on...)");
        byte sector = input.nextByte();
        System.out.println("Which SubSector: ");
        byte subSector = input.nextByte();
        System.out.println("Which Street: ");
        byte street = input.nextByte();
        System.out.println("Which House: ");
        byte house = input.nextByte();
        System.out.println("Which Portion: (0 is Ground, 1 is first, 2 is second)");
        byte portion = input.nextByte();
        System.out.print("Which Utility:\n1. Electricity\n2. Gas\n3. Water\n4. Phone\n5. Internet");
        byte utility = input.nextByte();

        System.out.printf("\nAddress: Sector %c, SubSector %d, Street %d, House %d, %s Floor\n", sectors[sector - 1], subSector-1, street-1, house-1, portions[portion]);
        System.out.printf("CID: %d\n", dataset[sector - 1][subSector - 1][street - 1][house - 1][portion].CID);
        System.out.printf("Name: %s\n", dataset[sector - 1][subSector - 1][street - 1][house - 1][portion].customerName);
        System.out.printf("Current units for %s is %d\n",utilities[utility-1], dataset[sector - 1][subSector - 1][street - 1][house - 1][portion].utilities[utility-1].currentReading);
        System.out.printf("Enter how many units to adjust for: (negative for reduction, positive for increase) ");
        int units = input.nextByte();
        dataset[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].utilities[utility-1].currentReading += units;
        dataset[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].utilities[utility-1].calculate((byte) (utility-1));
        saveObject(fileName,dataset);
    }

    private static void monthWiseReport(long cid, byte[] utilsToCheck) {
        for (byte datafile = 1; datafile <= datasets.size(); datafile++) {
            for (byte sector = 1; sector <= datasets.get(datafile - 1).length; sector++) {
                for (byte subSector = 1; subSector <= datasets.get(datafile - 1)[sector - 1].length; subSector++) {
                    for (byte street = 1; street <= datasets.get(datafile - 1)[sector - 1][subSector - 1].length; street++) {
                        for (byte house = 1; house <= datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1].length; house++) {
                            for (byte portion = 1; portion <= datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1].length; portion++) {
                                if (cid == datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].CID) {
                                    System.out.printf("Address: Sector %c, SubSector %d, Street %d, House %d, %s Floor\n", sectors[sector - 1], subSector, street, house, portions[portion - 1]);
                                    System.out.println(datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].customerName + "'s bill for " + datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].monthYear);
                                    for (byte utils = 1; utils <= datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].utilities.length; utils++) {
                                        if (containsByte(utilsToCheck, utils)) {
                                            System.out.printf(utilities[utils - 1] + " Units: %d\n", datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].utilities[utils].unitsConsumed);
                                            System.out.printf(utilities[utils - 1] + " Bill: %d pkr\n", datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].utilities[utils].dueAmount);
                                        }
                                    }
                                    System.out.println();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static void maxMin() {
        loadAllDataSets();
        System.out.print("Do you want 1. Maximum or 2. Minimum: ");
        byte minOrMax = input.nextByte();
        if ((minOrMax <= 2) && (minOrMax >= 1)) {
            System.out.println("At what level?\n1. Sector\n2. Sub-Sector\n3. Street\n4. House\n5. Utility");
            byte level = input.nextByte();
            try
            {
                switch (level) {
                    case 1: {
                        System.out.println("In which Sector: (1 is A, 2 is B and so on...)");
                        byte sector = input.nextByte();
                        getMaxMinInSector(minOrMax, sector);
                    }
                    break;
                    case 2: {
                        System.out.println("Which Sector: (1 is A, 2 is B and so on...)");
                        byte sector = input.nextByte();
                        System.out.println("Which SubSector: ");
                        byte subSector = input.nextByte();
                        getMaxMinInSubSector(minOrMax, sector, subSector);
                    }
                    break;
                    case 3: {
                        System.out.println("Which Sector: (1 is A, 2 is B and so on...)");
                        byte sector = input.nextByte();
                        System.out.println("Which SubSector: ");
                        byte subSector = input.nextByte();
                        System.out.println("Which Street: ");
                        byte street = input.nextByte();
                        getMaxMinInStreet(minOrMax, sector, subSector, street);
                    }
                    break;
                    case 4: {
                        System.out.println("Which Sector: (1 is A, 2 is B and so on...)");
                        byte sector = input.nextByte();
                        System.out.println("Which SubSector: ");
                        byte subSector = input.nextByte();
                        System.out.println("Which Street: ");
                        byte street = input.nextByte();
                        System.out.println("Which House: ");
                        byte house = input.nextByte();
                        getMaxMinInHouse(minOrMax, sector, subSector, street, house);
                    }
                    break;
                    case 5: {
                        System.out.println("Which Utility:\n1. Electricity\n2. Gas\n3. Water\n4. Phone\n5. Internet");
                        byte utility = input.nextByte();
                        getMaxMinInUtility(minOrMax, utility);
                    }
                    break;
                }
            }
            catch (Exception e)
            {
                System.out.println("An error was occured, theres a problem with your query, please try again.");
                e.printStackTrace();
            }
        } else {
            System.out.println("Invalid Input Try again!");
            maxMin();
        }
    }

    private static void loadAllDataSets()
    {
        datasets.clear();
        File f = new File("data");
        File[] matchingFiles = f.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".ser");
            }
        });
        for (File file : matchingFiles) {
            try {
                datasets.add((CBSRecord[][][][][]) readObject("data/" + file.getName()));
            } catch (Exception e) {
                System.out.println("Encountered an error try again");
            }
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

    private static void getMaxMinInSector(byte minOrMax, byte sector) throws Exception {
        long MinOrMax = datasets.get(0)[sector - 1][0][0][0][0].utilities[0].dueAmount;
        CBSRecord winner = new CBSRecord();
        String utilityThatWon = utilities[0];
        String forTheMonthOf = datasets.get(0)[sector - 1][0][0][0][0].monthYear;

        for (byte datafile = 1; datafile <= datasets.size(); datafile++) {
            for (byte subSector = 1; subSector <= datasets.get(datafile - 1)[sector - 1].length; subSector++) {
                for (byte street = 1; street <= datasets.get(datafile - 1)[sector - 1][subSector - 1].length; street++) {
                    for (byte house = 1; house <= datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1].length; house++) {
                        for (byte portion = 1; portion <= datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1].length; portion++)
                            for (byte utility = 1; utility <= datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].utilities.length; utility++) {
                                if ((minOrMax == 1) && (datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].utilities[utility - 1].dueAmount > MinOrMax)) {
                                    winner.CID = datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].CID;
                                    winner.customerName = datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].customerName;
                                    utilityThatWon = utilities[utility - 1];
                                    forTheMonthOf = datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].monthYear;
                                    MinOrMax = datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].utilities[utility - 1].dueAmount;
                                } else if ((minOrMax == 2) && (datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].utilities[utility - 1].dueAmount < MinOrMax)) {
                                    winner.CID = datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].CID;
                                    winner.customerName = datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].customerName;
                                    utilityThatWon = utilities[utility - 1];
                                    forTheMonthOf = datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].monthYear;
                                    MinOrMax = datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].utilities[utility - 1].dueAmount;
                                }
                            }
                    }
                }
            }
        }
        //print the result
        System.out.printf("\n%s had the bill for %s for the month of %s for %d", winner.customerName, utilityThatWon, forTheMonthOf, MinOrMax);

    }

    private static void getMaxMinInSubSector(byte minOrMax, byte sector, byte subSector) throws Exception {
        long MinOrMax = datasets.get(0)[sector - 1][subSector - 1][0][0][0].utilities[0].dueAmount;
        CBSRecord winner = new CBSRecord();
        String utilityThatWon = utilities[0];
        String forTheMonthOf = datasets.get(0)[sector - 1][subSector - 1][0][0][0].monthYear;

        for (byte datafile = 1; datafile <= datasets.size(); datafile++) {
            for (byte street = 1; street <= datasets.get(datafile - 1)[sector - 1][subSector - 1].length; street++) {
                for (byte house = 1; house <= datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1].length; house++) {
                    for (byte portion = 1; portion <= datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1].length; portion++)
                        for (byte utility = 1; utility <= datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].utilities.length; utility++) {
                            if ((minOrMax == 1) && (datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].utilities[utility - 1].dueAmount > MinOrMax)) {
                                winner.CID = datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].CID;
                                winner.customerName = datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].customerName;
                                utilityThatWon = utilities[utility - 1];
                                forTheMonthOf = datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].monthYear;
                                MinOrMax = datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].utilities[utility - 1].dueAmount;
                            } else if ((minOrMax == 2) && (datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].utilities[utility - 1].dueAmount < MinOrMax)) {
                                winner.CID = datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].CID;
                                winner.customerName = datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].customerName;
                                utilityThatWon = utilities[utility - 1];
                                forTheMonthOf = datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].monthYear;
                                MinOrMax = datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].utilities[utility - 1].dueAmount;
                            }
                        }
                }
            }
        }
        //print the result
        System.out.printf("\n%s had the bill for %s for the month of %s for %d", winner.customerName, utilityThatWon, forTheMonthOf, MinOrMax);

    }

    private static void getMaxMinInStreet(byte minOrMax, byte sector, byte subSector, byte street) throws Exception {

        long MinOrMax = datasets.get(0)[sector - 1][subSector - 1][street - 1][0][0].utilities[0].dueAmount;
        CBSRecord winner = new CBSRecord();
        String utilityThatWon = utilities[0];
        String forTheMonthOf = datasets.get(0)[sector - 1][subSector - 1][street - 1][0][0].monthYear;

        for (byte datafile = 1; datafile <= datasets.size(); datafile++) {
            for (byte house = 1; house <= datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1].length; house++) {
                for (byte portion = 1; portion <= datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1].length; portion++)
                    for (byte utility = 1; utility <= datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].utilities.length; utility++) {
                        if ((minOrMax == 1) && (datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].utilities[utility - 1].dueAmount > MinOrMax)) {
                            winner.CID = datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].CID;
                            winner.customerName = datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].customerName;
                            utilityThatWon = utilities[utility - 1];
                            forTheMonthOf = datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].monthYear;
                            MinOrMax = datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].utilities[utility - 1].dueAmount;
                        } else if ((minOrMax == 2) && (datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].utilities[utility - 1].dueAmount < MinOrMax)) {
                            winner.CID = datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].CID;
                            winner.customerName = datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].customerName;
                            utilityThatWon = utilities[utility - 1];
                            forTheMonthOf = datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].monthYear;
                            MinOrMax = datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].utilities[utility - 1].dueAmount;
                        }
                    }
            }
        }
        //print the result
        System.out.printf("\n%s had the bill for %s for the month of %s for %d", winner.customerName, utilityThatWon, forTheMonthOf, MinOrMax);

    }

    private static void getMaxMinInHouse(byte minOrMax, byte sector, byte subSector, byte street, byte house) throws Exception {

        long MinOrMax = datasets.get(0)[sector - 1][subSector - 1][street - 1][house - 1][0].utilities[0].dueAmount;
        CBSRecord winner = new CBSRecord();
        String utilityThatWon = utilities[0];
        String forTheMonthOf = datasets.get(0)[sector - 1][subSector - 1][street - 1][house - 1][0].monthYear;

        for (byte datafile = 1; datafile <= datasets.size(); datafile++) {
            for (byte portion = 1; portion <= datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1].length; portion++)
                for (byte utility = 1; utility <= datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].utilities.length; utility++) {
                    if ((minOrMax == 1) && (datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].utilities[utility - 1].dueAmount > MinOrMax)) {
                        winner.CID = datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].CID;
                        winner.customerName = datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].customerName;
                        utilityThatWon = utilities[utility - 1];
                        forTheMonthOf = datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].monthYear;
                        MinOrMax = datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].utilities[utility - 1].dueAmount;
                    } else if ((minOrMax == 2) && (datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].utilities[utility - 1].dueAmount < MinOrMax)) {
                        winner.CID = datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].CID;
                        winner.customerName = datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].customerName;
                        utilityThatWon = utilities[utility - 1];
                        forTheMonthOf = datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].monthYear;
                        MinOrMax = datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].utilities[utility - 1].dueAmount;
                    }
                }
        }
        //print the result
        System.out.printf("\n%s had the bill for %s for the month of %s for %d", winner.customerName, utilityThatWon, forTheMonthOf, MinOrMax);

    }

    private static void getMaxMinInUtility(byte minOrMax, byte utility) throws Exception {

        long MinOrMax = datasets.get(0)[0][0][0][0][0].utilities[utility].dueAmount;
        CBSRecord winner = new CBSRecord();
        String utilityThatWon = utilities[utility];
        String forTheMonthOf = datasets.get(0)[0][0][0][0][0].monthYear;

        for (byte datafile = 1; datafile <= datasets.size(); datafile++) {
            for (byte sector = 1; sector <= datasets.get(datafile - 1).length; sector++) {
                for (byte subSector = 1; subSector <= datasets.get(datafile - 1)[sector - 1].length; subSector++) {
                    for (byte street = 1; street <= datasets.get(datafile - 1)[sector - 1][subSector - 1].length; street++) {
                        for (byte house = 1; house <= datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1].length; house++) {
                            for (byte portion = 1; portion <= datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1].length; portion++) {
                                if ((minOrMax == 1) && (datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].utilities[utility - 1].dueAmount > MinOrMax)) {
                                    winner.CID = datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].CID;
                                    winner.customerName = datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].customerName;
                                    utilityThatWon = utilities[utility - 1];
                                    forTheMonthOf = datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].monthYear;
                                    MinOrMax = datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].utilities[utility - 1].dueAmount;
                                } else if ((minOrMax == 2) && (datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].utilities[utility - 1].dueAmount < MinOrMax)) {
                                    winner.CID = datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].CID;
                                    winner.customerName = datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].customerName;
                                    utilityThatWon = utilities[utility - 1];
                                    forTheMonthOf = datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].monthYear;
                                    MinOrMax = datasets.get(datafile - 1)[sector - 1][subSector - 1][street - 1][house - 1][portion - 1].utilities[utility - 1].dueAmount;
                                }
                            }
                        }
                    }
                }
            }
        }
        System.out.printf("\n%s had the bill for %s for the month of %s for %d", winner.customerName, utilityThatWon, forTheMonthOf, MinOrMax);

    }
}
