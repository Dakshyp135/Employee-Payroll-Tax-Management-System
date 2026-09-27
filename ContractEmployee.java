public class ContractEmployee extends Employee {
    public ContractEmployee(int id, String name, double salary) throws InvalidInputException {
        super(id, name, salary);
    }

    @Override
    public double calculateAllowance() {
        return getBasicSalary() * 0.10;
    }

    @Override
    public String getEmployeeType() { return "Contract"; }
}
