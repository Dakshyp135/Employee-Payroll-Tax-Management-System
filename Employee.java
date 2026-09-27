public abstract class Employee {
    private final int employeeId;   
    private String name;
    private double basicSalary;

    public Employee(int employeeId, String name, double basicSalary) throws InvalidInputException {
        if (employeeId <= 0 || name == null || name.trim().isEmpty() || basicSalary < 0) {
            throw new InvalidInputException("ID must be positive, name cannot be empty, and salary cannot be negative.");
        }
        this.employeeId = employeeId;
        this.name = name.trim();
        this.basicSalary = basicSalary;
    }

    public int getEmployeeId() { return employeeId; }
    public String getName() { return name; }
    public double getBasicSalary() { return basicSalary; }

    public void setName(String name) throws InvalidInputException {
        if (name == null || name.trim().isEmpty()) throw new InvalidInputException("Name cannot be empty.");
        this.name = name.trim();
    }

    public void setBasicSalary(double basicSalary) throws InvalidInputException {
        if (basicSalary < 0) throw new InvalidInputException("Salary cannot be negative.");
        this.basicSalary = basicSalary;
    }

    public abstract double calculateAllowance();

    public abstract String getEmployeeType();

    public double calculateGrossSalary(double bonus) {
        return basicSalary + calculateAllowance() + bonus;
    }

    @Override
    public String toString() {
        return employeeId + " - " + name + " (" + getEmployeeType() + ")";
    }
}
