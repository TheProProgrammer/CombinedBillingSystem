import java.util.Scanner;

public class unitTester
{
    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in);
        System.out.printf("Enter units: ");
        int units = input.nextInt();
        System.out.println("Your Bill is: "+calculateForGas(units));
    }
    private static float calculateForGas(int unitsConsumed)
    {
        float dueAmount = 0.0F;
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
        return dueAmount;
    }
    private static float HMtoMMBTU(float hm)
    {
        float GCV = 990;
        return (float) (hm * GCV / 281.7385);
    }
}
