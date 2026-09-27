public class PayrollDetails {
    private final Employee employee;
    private final double allowance, bonus, gross, tax, otherDeductions, net;

    public PayrollDetails(Employee employee, double allowance, double bonus, double gross,
                          double tax, double otherDeductions, double net) {
        this.employee = employee;
        this.allowance = allowance;
        this.bonus = bonus;
        this.gross = gross;
        this.tax = tax;
        this.otherDeductions = otherDeductions;
        this.net = net;
    }

    public Employee getEmployee() { return employee; }
    public double getAllowance() { return allowance; }
    public double getBonus() { return bonus; }
    public double getGross() { return gross; }
    public double getTax() { return tax; }
    public double getOtherDeductions() { return otherDeductions; }
    public double getNet() { return net; }
}
