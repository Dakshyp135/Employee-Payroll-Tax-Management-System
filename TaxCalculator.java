public class TaxCalculator {
    public double calculateTax(double annualGrossIncome) {
        double taxableIncome = Math.max(0, annualGrossIncome);
        if (taxableIncome <= 400_000) 
            return 0;

        if (taxableIncome <= 800_000)
            return (taxableIncome - 400_000) * 0.05;

        if (taxableIncome <= 1_200_000)
            return 20_000 + (taxableIncome - 800_000) * 0.10;

        if (taxableIncome <= 1_600_000)
            return 60_000 + (taxableIncome - 1_200_000) * 0.15;

        if (taxableIncome <= 2_000_000)
            return 120_000 + (taxableIncome - 1_600_000) * 0.20;

        if (taxableIncome <= 2_400_000)
            return 200_000 + (taxableIncome - 2_000_000) * 0.25;

        return 300_000 + (taxableIncome - 2_400_000) * 0.30;
    }
}
