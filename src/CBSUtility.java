import java.io.Serializable;

public class CBSUtility implements Serializable
{
    long prevReading;
    long currentReading;
    long unitsConsumed;
    long dueAmount;
    int internationalPhoneMinutes = 0;

    CBSUtility()
    {
        prevReading = 0;
        currentReading = 0;
        unitsConsumed = 0;
        dueAmount = 0;
    }
    void calculate(byte utilityType)
    {
        switch(utilityType)
        {
            case 0 -> calculateForElectricity();
            case 1 -> calculateForGas();
            case 2 -> calculateForWater();
            case 3 -> calculateForPhone();
            case 4 -> calculateForInternet();
        }
    }

    private void calculateForGas()
    {
        unitsConsumed = currentReading-prevReading;

        long cubicMeter = unitsConsumed;
        float cubicHM = (float)cubicMeter/100;
        float gasCharges = 0;

        if (cubicHM <= 0.5)
            gasCharges = 121*HMtoMMBTU(cubicHM);
        else if(cubicHM <= 1.0)
        {
            gasCharges = 121*HMtoMMBTU(0.5F);
            gasCharges += 300*HMtoMMBTU(cubicHM - 0.5f);
        }
        else if(cubicHM <= 2.0)
        {
            gasCharges = 300*HMtoMMBTU(1F);
            gasCharges += 553*HMtoMMBTU(cubicHM - 1F);
        }
        else if(cubicHM <=3.0)
        {
            gasCharges = 553*HMtoMMBTU(2F);
            gasCharges += 738*HMtoMMBTU(cubicHM - 2F);
        }
        else if(cubicHM <= 4.0)
        {
            gasCharges = 738*HMtoMMBTU(3F);
            gasCharges += 1107*HMtoMMBTU(cubicHM - 3F);
        }
        else if(cubicHM > 4.0)
        {
            gasCharges = 1107*HMtoMMBTU(4F);
            gasCharges += 1460*HMtoMMBTU(cubicHM - 4F);
        }

        if(gasCharges < 172.58)
            gasCharges = (float) 172.58;

        dueAmount = (long) gasCharges+20;
        dueAmount += (dueAmount*0.17);
    }

    private void calculateForInternet()
    {
        unitsConsumed = currentReading;

        dueAmount = 10*unitsConsumed;
    }

    private void calculateForPhone()
    {
        unitsConsumed = currentReading;

        dueAmount = (unitsConsumed * 5) + (internationalPhoneMinutes * 7L);
    }

    private void calculateForWater()
    {
        unitsConsumed = currentReading;

        if (unitsConsumed <= 1000)
            dueAmount = 400;
        else if(unitsConsumed <= 2000)
            dueAmount = 1000;
        else dueAmount = (long) (1000 + (1.5 * unitsConsumed));
    }

    private void calculateForElectricity()
    {
        unitsConsumed = currentReading-prevReading;

        if (unitsConsumed < 100)
            dueAmount = unitsConsumed*10;
        else if(unitsConsumed < 200)
            dueAmount = 1000 + (unitsConsumed-100)*15;
        else if(unitsConsumed <= 300)
            dueAmount = 3000 + (unitsConsumed-200)*18;
        else dueAmount = unitsConsumed*25;
    }

    private float HMtoMMBTU(float hm)
    {
        float GCV = 990;
        return (float) (hm * GCV / 281.7385);
    }
}
