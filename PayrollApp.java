import java.util.Scanner;


public class PayrollApp {
    private static final int MAX_EMPLOYEES = 50;
    private static final Employee[] employees = new Employee[MAX_EMPLOYEES];
    private static int employeeCount = 0;
    private static final PayrollService payrollService = new PayrollService();
    private static final PayrollReportWriter reportWriter = new PayrollReportWriter();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== EMPLOYEE PAYROLL & TAX MANAGEMENT SYSTEM ===");
        boolean running = true;
        while (running) {
            printMenu();
            switch (readInt("Choose an option: ")) {
                case 1 -> addEmployee();
                case 2 -> listEmployees();
                case 3 -> calculateAndSavePayroll();
                case 4 -> running = false;
                default -> System.out.println("Please enter a number from 1 to 4.");
            }
        }
        System.out.println("Thank you. Goodbye!");
    }

    private static void printMenu() {
        System.out.println("\n1. Add employee\n2. View employees\n3. Calculate payroll and save report\n4. Exit");
    }

    private static void addEmployee() {
        if (employeeCount == MAX_EMPLOYEES) { 
            System.out.println("Employee storage is full."); 
            return; 
        }
        try {
            int id = readInt("Employee ID: ");
            String name = readText("Name: ");
            double basic = readDouble("Monthly basic salary: ");
            int type = readInt("1. Salaried  2. Contract: ");
            Employee employee;

            employee = switch (type) {
                case 1 -> new SalariedEmployee(id, name, basic);
                case 2 -> new ContractEmployee(id, name, basic);
                default -> null;
            };
            if (employee == null) { 
                System.out.println("Invalid employee type."); 
                return; 
            }
            employees[employeeCount++] = employee;
            System.out.println("Employee added successfully.");
        } 
        catch (InvalidInputException e) { 
            System.out.println("Input error: " + e.getMessage()); 
        }
    }

    private static void listEmployees() {
        if (employeeCount == 0) { 
            System.out.println("No employees added yet."); 
            return; 
        }
        for (int i = 0; i < employeeCount; i++) System.out.println(employees[i]);
    }

    private static void calculateAndSavePayroll() {
        Employee employee = findEmployee(readInt("Employee ID: "));
        if (employee == null) { 
            System.out.println("Employee not found."); 
            return; 
        }
        try {
            PayrollDetails details = payrollService.calculatePayroll(employee,
                    readDouble("Bonus: "), readDouble("Other monthly deductions: "));
            printPayslip(details);
            ReportGeneratorThread reportThread = new ReportGeneratorThread(reportWriter, details);
            reportThread.start();
            reportThread.join();
        } catch (InvalidInputException e) { System.out.println("Input error: " + e.getMessage()); }
          catch (InterruptedException e) { Thread.currentThread().interrupt(); System.out.println("Report generation was interrupted."); }
    }

    private static Employee findEmployee(int id) {
        for (int i = 0; i < employeeCount; i++) if (employees[i].getEmployeeId() == id) return employees[i];
        return null;
    }

    private static void printPayslip(PayrollDetails p) {
        System.out.printf("%n--- PAYSLIP: %s ---%n", p.getEmployee().getName());
        System.out.printf("Basic: %.2f%nAllowance: %.2f%nBonus: %.2f%nGross: %.2f%n",
                p.getEmployee().getBasicSalary(), p.getAllowance(), p.getBonus(), p.getGross());
        System.out.printf("Tax: %.2f%nOther deductions: %.2f%nNET SALARY: %.2f%n",
                p.getTax(), p.getOtherDeductions(), p.getNet());
    }

    private static String readText(String prompt) { System.out.print(prompt); return scanner.nextLine(); }
    private static int readInt(String prompt) {
        while (true) try { return Integer.parseInt(readText(prompt)); }
        catch (NumberFormatException e) { System.out.println("Enter a whole number."); }
    }
    private static double readDouble(String prompt) {
        while (true) try { return Double.parseDouble(readText(prompt)); }
        catch (NumberFormatException e) { System.out.println("Enter a valid number."); }
    }
}
