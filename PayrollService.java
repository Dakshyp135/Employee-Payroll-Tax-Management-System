public class PayrollService {
    private final TaxCalculator taxCalculator = new TaxCalculator();

    public PayrollDetails calculatePayroll(Employee employee, double bonus, double otherDeductions)
            throws InvalidInputException {
        if (bonus < 0 || otherDeductions < 0) throw new InvalidInputException("Bonus and deductions cannot be negative.");
        double allowance = employee.calculateAllowance();
        double gross = employee.calculateGrossSalary(bonus);
        double monthlyTax = taxCalculator.calculateTax(gross * 12) / 12;
        double net = gross - monthlyTax - otherDeductions;
        return new PayrollDetails(employee, allowance, bonus, gross, monthlyTax, otherDeductions, net);
    }
}
