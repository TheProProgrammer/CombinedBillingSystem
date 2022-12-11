import java.io.Serializable;

public class CBSRecord implements Serializable
{
    String customerName;
    long CID;
    CBSUtility[] utilities = new CBSUtility[5];
    String monthYear;

    CBSRecord()
    {
        customerName = "";
        CID = 0;
        monthYear = "";
        for (byte utility = 0; utility < utilities.length; utility++)
        {
            utilities[utility] = new CBSUtility();
        }
    }

    public long allUtilitiesBill()
    {
        long sum = 0;
        for (CBSUtility util : utilities)
        {
            sum+=util.dueAmount;
        }
        return sum;
    }

    void calculateEachUtilityBill()
    {
        for (byte utility = 0; utility < utilities.length; utility++)
        {
            utilities[utility].calculate(utility);
        }
    }

    void setupForNewMonth()
    {
        for (byte utility = 0; utility < utilities.length; utility++)
        {
            utilities[utility].prevReading = utilities[utility].currentReading;
            utilities[utility].currentReading = 0;
            utilities[utility].unitsConsumed = 0;
            utilities[utility].dueAmount = 0;

        }
    }
}
