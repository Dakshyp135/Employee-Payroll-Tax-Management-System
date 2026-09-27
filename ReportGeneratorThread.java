import java.io.IOException;

public class ReportGeneratorThread extends Thread {
    private final PayrollReportWriter writer;
    private final PayrollDetails details;

    public ReportGeneratorThread(PayrollReportWriter writer, PayrollDetails details) {
        this.writer = writer;
        this.details = details;
    }

    @Override
    public void run() {
        try {
            writer.saveReport(details);
            System.out.println("Report saved for " + details.getEmployee().getName() + " by " + getName());
        } catch (IOException e) {
            System.out.println("Could not save report: " + e.getMessage());
        }
    }
}
