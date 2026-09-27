import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class PayrollReportWriter {
    private static final String REPORT_FILE = "reports/payroll_reports.txt";

    public synchronized void saveReport(PayrollDetails p) throws IOException {
        File file = new File(REPORT_FILE);
        File parent = file.getParentFile();
        if (parent != null) parent.mkdirs();
        try (PrintWriter out = new PrintWriter(new FileWriter(file, true))) {
            out.println("----------------------------------------");
            out.printf("Employee: %s%n", p.getEmployee());
            out.printf("Basic salary: %.2f | Allowance: %.2f | Bonus: %.2f%n",
                    p.getEmployee().getBasicSalary(), p.getAllowance(), p.getBonus());
            out.printf("Gross: %.2f | Tax: %.2f | Other deductions: %.2f | Net: %.2f%n",
                    p.getGross(), p.getTax(), p.getOtherDeductions(), p.getNet());
        }
    }
}
