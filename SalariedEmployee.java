public class SalariedEmployee extends Employee {
    private final double allowanceRate;

    public SalariedEmployee(int id, String name, double salary) throws InvalidInputException {
        this(id, name, salary, 0.20); 
    }

    public SalariedEmployee(int id, String name, double salary, double allowanceRate) throws InvalidInputException {
        super(id, name, salary); 
        this.allowanceRate = allowanceRate;
    }

    @Override
    public double calculateAllowance() {
        return getBasicSalary() * allowanceRate;
    }

    @Override
    public String getEmployeeType() { return "Salaried"; }
}
