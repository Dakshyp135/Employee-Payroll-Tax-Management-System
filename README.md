# Employee Payroll & Tax Management System


## 1. Project snapshot

| Item | Details |
|---|---|
| Title | Employee Payroll & Tax Management System |
| Type | Console-based Java application |
| Main class | `PayrollApp` |
| Storage | In-memory employee array + text-file payroll reports |
| Suitable for | Java/OOP, file handling, exceptions, and threads mini-project |

## 2. Problem statement

Small organizations need a simple way to store employee details, calculate salary components, estimate deductions, and retain a readable payroll report. Manual calculations can be slow and lead to mistakes. This application lets a user add employees, calculate a monthly payslip, and save the result in a text file.

## 3. Objectives

- Store salaried and contract employee details.
- Calculate allowance, gross salary, simulated tax, deductions, and net salary.
- Demonstrate essential Java syllabus concepts in one easy-to-present project.
- Validate invalid entries with a custom exception.
- Save reports safely using a synchronized background thread.

## 4. Features

- Menu-driven console interface.
- Maximum 50 employees stored in an array during one program run.
- Two employee categories with different allowance rules.
- Simple, consistent monthly tax calculation based on annualized gross income.
- Input validation for IDs, names, salaries, bonuses, and deductions.
- Appends each payslip to `reports/payroll_reports.txt`.

## 5. Simple academic tax model

The application converts the monthly gross salary to annual gross salary (`monthly gross × 12`), subtracts a fixed simulated deduction of ₹50,000, then applies these slabs:

| Taxable annual income | Simulated tax |
|---|---|
| Up to ₹3,00,000 | 0% |
| ₹3,00,001–₹6,00,000 | 5% on amount above ₹3,00,000 |
| ₹6,00,001–₹9,00,000 | ₹15,000 + 10% on amount above ₹6,00,000 |
| Above ₹9,00,000 | ₹45,000 + 15% on amount above ₹9,00,000 |

Monthly tax = annual tax ÷ 12.

### Salary formula

```text
Gross salary = Basic salary + Allowance + Bonus
Net salary   = Gross salary − Monthly tax − Other deductions

Salaried allowance = 20% of basic salary
Contract allowance = 10% of basic salary
```

## 6. Folder structure

```text
EmployeePayrollTaxManagementSystem/
├── README.md
├── src/
│   ├── PayrollApp.java                # main menu and input
│   ├── Employee.java                  # abstract parent class
│   ├── SalariedEmployee.java          # child class
│   ├── ContractEmployee.java          # child class
│   ├── TaxCalculator.java             # tax logic + overloading
│   ├── PayrollService.java            # payroll calculation
│   ├── PayrollDetails.java            # returned result object
│   ├── PayrollReportWriter.java       # synchronized file writer
│   ├── ReportGeneratorThread.java     # report-saving thread
│   └── InvalidInputException.java     # custom exception
├── data/                              # reserved for future data files
└── reports/payroll_reports.txt        # created automatically after a report is saved
```

## 7. Setup and run

1. Install a JDK (Java Development Kit), version 8 or later.
2. Open a terminal in the `EmployeePayrollTaxManagementSystem` folder.
3. Compile:

```bash
javac -d out src/*.java
```

4. Run:

```bash
java -cp out PayrollApp
```

5. Use menu option 1 to add an employee, then option 3 to calculate and save a payroll report. The report appears at `reports/payroll_reports.txt`.

## 8. How the program works

```text
Start
  ↓
Show menu
  ├─ Add employee → validate input → create SalariedEmployee / ContractEmployee → store in array
  ├─ View employees → print the employee array
  ├─ Calculate payroll → find employee → calculate gross/tax/net → show payslip
  │                                                        ↓
  │                                           start report-saving thread
  │                                                        ↓
  │                                        synchronized write to text file
  └─ Exit
```

## 9. Class diagram / mind map

```text
                          Employee Payroll & Tax System
                                      │
        ┌─────────────────────────────┼─────────────────────────────┐
        │                             │                             │
   Console layer                 Business layer                 Persistence
   PayrollApp                    PayrollService                 PayrollReportWriter
        │                             │                             │
   Employee[] array ───────> PayrollDetails <────── TaxCalculator  synchronized file write
        │                                                           │
        │                                                           └─ ReportGeneratorThread
        ▼
 Employee (abstract)
   ├─ SalariedEmployee (20% allowance)
   └─ ContractEmployee (10% allowance)

 InvalidInputException → used when entered data is invalid
```

## 10. Explanation of every class

### `PayrollApp.java`

This is the starting class and contains `main()`. It prints the menu, reads user input using `Scanner`, stores employees in the `Employee[]` array, searches for an employee, displays the payslip, and starts the report thread. `readInt()` and `readDouble()` use `Integer.parseInt()` and `Double.parseDouble()` to convert text input to primitive values safely.

### `Employee.java`

This is an **abstract class**, so objects of `Employee` itself cannot be created. It contains common fields: ID, name, and basic salary. Its private fields demonstrate encapsulation. The ID is `final`, meaning it cannot change after the constructor initializes it. `calculateAllowance()` is abstract because each employee type has a different rule.

### `SalariedEmployee.java` and `ContractEmployee.java`

Both extend `Employee`. They use `super(...)` to call the parent constructor and override `calculateAllowance()` and `getEmployeeType()`. When the program calls `employee.calculateAllowance()`, Java chooses the right child method at runtime: this is runtime polymorphism.

### `TaxCalculator.java`

Contains the simplified tax slabs. The `final` constant `STANDARD_DEDUCTION` cannot be changed. `calculateTax` is overloaded: one version accepts only annual income and uses the standard deduction; another accepts annual income plus a supplied deduction.

### `PayrollService.java`

Separates calculation logic from menu code. Its `calculatePayroll(Employee employee, ...)` method receives an object reference and returns a new `PayrollDetails` object. It calculates allowance, gross salary, simulated tax, and net salary.

### `PayrollDetails.java`

This small data class groups all calculated values into one object. Returning one `PayrollDetails` object is clearer than returning many separate values.

### `InvalidInputException.java`

A custom checked exception. Constructors and setters throw it when an ID, name, or salary is invalid. `PayrollApp` catches it and displays a friendly message instead of ending the program.

### `PayrollReportWriter.java`

Creates/appends to a text file using `FileWriter` and `PrintWriter`. `saveReport()` is `synchronized`, so only one thread can write to the shared report file at a time. This avoids interleaved or corrupted report lines.

### `ReportGeneratorThread.java`

Extends `Thread`. Its `run()` method calls `PayrollReportWriter.saveReport()`. In the app, `start()` launches the thread and `join()` waits for it to finish, so the success message is reliable.

## 11. Important code ideas

### Creating different employee objects

```java
Employee employee = type == 1 ? new SalariedEmployee(id, name, basic)
                              : new ContractEmployee(id, name, basic);
```

The variable type is `Employee`, but it can refer to either child object. This is a parent-class reference and child-class object.

### Polymorphic allowance calculation

```java
double allowance = employee.calculateAllowance();
```

The same statement gives 20% for a `SalariedEmployee` and 10% for a `ContractEmployee`, because the child method is chosen at runtime.

### Thread safety

```java
public synchronized void saveReport(PayrollDetails p) throws IOException {
    // only one report thread can execute this method at a time
}
```

Synchronization matters whenever multiple threads share one writable resource, here the report file.

## 12. Topic-to-code mapping

| Syllabus topic | Exact use in project |
|---|---|
| Datatypes | `int employeeId`, `double salary`, `String name`, `boolean running` in `PayrollApp` / `Employee` |
| Operators | `+`, `-`, `*`, `/`, `==`, `<=`, ternary `?:` in calculations and menu logic |
| Control flow | `while`, `for`, `if/else`, `switch`, `try/catch` in `PayrollApp` and `TaxCalculator` |
| Type conversion | `Integer.parseInt()` and `Double.parseDouble()` in `PayrollApp` |
| Wrapper classes | `Integer` and `Double` parsing methods in `PayrollApp` |
| Arrays | `Employee[] employees` stores up to 50 references |
| Classes and objects | `new SalariedEmployee`, `new PayrollService`, etc. |
| Constructors | All model classes initialize their state in constructors |
| Object references | `Employee employee`, `PayrollDetails details`, and array entries |
| Methods | `addEmployee`, `calculatePayroll`, `saveReport`, getters, etc. |
| Method overloading | Two `calculateTax(...)` methods in `TaxCalculator` and two constructors in `SalariedEmployee` |
| Passing/returning objects | `calculatePayroll(Employee...)` accepts `Employee` and returns `PayrollDetails`; `findEmployee` returns `Employee` |
| Access control | `private` fields, `public` API methods, private helper methods |
| `final` | immutable employee ID, constants, service fields |
| Inheritance | both employee subclasses `extends Employee` |
| Method overriding | child `calculateAllowance()` and `getEmployeeType()` methods |
| Abstract class | `Employee` declares abstract behavior |
| `super` | child constructors call `super(id, name, salary)` |
| Console I/O | `Scanner` reads inputs; `System.out` shows menu/payslip |
| File handling | `File`, `FileWriter`, `PrintWriter` write reports |
| Exceptions | `try/catch`, `NumberFormatException`, `IOException`, `InterruptedException` |
| Custom exception | `InvalidInputException` |
| Multithreading | `ReportGeneratorThread` extends `Thread`; `start`, `run`, `join` |
| Synchronization | `synchronized saveReport()` protects the report file |

## 13. Sample input/output

```text
=== EMPLOYEE PAYROLL & TAX MANAGEMENT SYSTEM ===
Academic simulation only; not current Indian tax advice.

1. Add employee
2. View employees
3. Calculate payroll and save report
4. Exit
Choose an option: 1
Employee ID: 101
Name: Aditi Sharma
Monthly basic salary: 50000
1. Salaried  2. Contract: 1
Employee added successfully.

Choose an option: 3
Employee ID: 101
Bonus: 5000
Other monthly deductions: 2000

--- PAYSLIP: Aditi Sharma ---
Basic: 50000.00
Allowance: 10000.00
Bonus: 5000.00
Gross: 65000.00
Tax: 2333.33
Other deductions: 2000.00
NET SALARY: 60666.67
Report saved for Aditi Sharma by Thread-0
```

Explanation: annual gross is ₹7,80,000; after the ₹50,000 academic deduction, taxable income is ₹7,30,000. Annual tax is ₹28,000, so monthly tax is ₹2,333.33. (The illustrative output above should therefore show `Tax: 2333.33` and `NET SALARY: 60666.67`.)

## 14. Limitations

- Data is stored only while the application is running; employee records are not loaded back from a database/file.
- It supports only two employee types and a fixed-size array.
- Tax slabs are intentionally simplified and are not legally valid.
- No graphical user interface, login, or database.

## 15. Future scope

- Replace the array with `ArrayList` and save employee data permanently.
- Use a database such as MySQL with JDBC.
- Add leave, attendance, PF, insurance, and department modules.
- Build a Swing/JavaFX web interface.
- Generate PDF payslips and allow search/update/delete.
- Use a thread pool to create many reports efficiently.

## 16. Viva questions and simple answers

1. **What is OOP?** It is a programming style that organizes code using classes and objects.
2. **What is a class?** A blueprint containing data and methods.
3. **What is an object?** A real instance created from a class using `new`.
4. **Why is `Employee` abstract?** It has common employee data, but each employee type must define its own allowance rule.
5. **Can we create an object of an abstract class?** No.
6. **What is inheritance?** A child class gets accessible properties and methods of a parent class.
7. **Which classes inherit in this project?** `SalariedEmployee` and `ContractEmployee` inherit from `Employee`.
8. **What is method overriding?** A child class gives its own implementation of a parent method with the same signature.
9. **Show overriding here.** Both child classes override `calculateAllowance()`.
10. **What is polymorphism?** One parent reference can work with objects of different child types.
11. **What is runtime polymorphism here?** `employee.calculateAllowance()` runs the correct child version at runtime.
12. **What is encapsulation?** Keeping data private and accessing it through methods.
13. **Why are fields private?** To prevent uncontrolled direct changes to important data.
14. **What is a constructor?** A special method that initializes an object when it is created.
15. **What does `super` do?** It accesses the parent class; here it calls the parent constructor.
16. **What is `final`?** It prevents reassignment, overriding, or inheritance depending on where it is used.
17. **Why is employee ID final?** An employee ID should remain fixed after the employee is created.
18. **What is method overloading?** Same method name with different parameter lists.
19. **Where is overloading used?** In `TaxCalculator.calculateTax` and `SalariedEmployee` constructors.
20. **What is an array?** A fixed-size collection of same-type values; here it stores employee references.
21. **What is an object reference?** A variable that points to an object, such as `Employee employee`.
22. **How is an object passed to a method?** `PayrollService.calculatePayroll` receives an `Employee` reference.
23. **How is an object returned?** `calculatePayroll` returns a `PayrollDetails` object.
24. **What is type conversion?** Changing data from one type to another, such as text to `int`.
25. **What are wrapper classes?** Object forms of primitives, for example `Integer` for `int` and `Double` for `double`.
26. **Why use `Integer.parseInt`?** Console input arrives as text and must be converted to an integer.
27. **What is exception handling?** Handling abnormal situations so the program can continue safely.
28. **What is a custom exception?** A programmer-created exception class for a specific problem.
29. **Which custom exception is used?** `InvalidInputException`.
30. **What is the difference between `throw` and `throws`?** `throw` creates/sends an exception; `throws` declares that a method may send one.
31. **What is file handling?** Reading or writing data in files; this project writes reports to a text file.
32. **Why is try-with-resources used?** It closes the writer automatically, even if an error happens.
33. **What is a thread?** A separate path of execution within a program.
34. **Why use a thread here?** It saves the payroll report as a separate task from the main console flow.
35. **What does `start()` do?** It starts a new thread and then calls `run()` on that thread.
36. **What does `join()` do?** It makes the current thread wait until another thread completes.
37. **What is synchronization?** A way to allow only one thread at a time to access shared critical code/resource.
38. **Why synchronize `saveReport`?** Multiple report threads must not write mixed text into the same file.
39. **What does `static` mean?** The member belongs to the class, not a particular object.
40. **Is this tax system real?** No; it is an academic simulation only.

## 17. Suggested presentation order

1. Problem statement and objective.
2. Show the flowchart and class diagram.
3. Run one example: add employee, calculate payroll, open the generated report.
4. Explain `Employee` → child classes as inheritance/polymorphism.
5. Explain custom exception and synchronized report thread.
6. Mention limitations and future scope.
