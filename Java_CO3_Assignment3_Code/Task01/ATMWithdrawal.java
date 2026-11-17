import java.util.Scanner;

class InsufficientBalanceException extends Exception {
    InsufficientBalanceException(String message) { super(message); }
}

class InvalidAmountException extends RuntimeException {
    InvalidAmountException(String message) { super(message); }
}

class ATM {
    private double balance;

    ATM(double balance) { this.balance = balance; }

    void withdraw(String accountNumber, double amount)
            throws InsufficientBalanceException {
        if (accountNumber == null || accountNumber.isBlank())
            throw new IllegalArgumentException("Account number cannot be empty");
        if (amount < 0) throw new InvalidAmountException("Withdrawal amount cannot be negative");
        if (amount == 0) throw new InvalidAmountException("Withdrawal amount must be greater than zero");
        if (amount > balance)
            throw new InsufficientBalanceException("Insufficient account balance");
        balance -= amount;
        System.out.printf("Withdrawal successful. Remaining balance: %.2f%n", balance);
    }
}

public class ATMWithdrawal {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ATM atm = new ATM(10000);
        try {
            System.out.print("Enter account number: ");
            String account = sc.nextLine();
            System.out.print("Enter withdrawal amount: ");
            String rawAmount = sc.nextLine();
            double amount = Double.parseDouble(rawAmount);
            atm.withdraw(account, amount);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input: enter the amount using numbers only.");
        } catch (InsufficientBalanceException e) {
            System.out.println("Transaction declined: " + e.getMessage());
        } catch (InvalidAmountException | IllegalArgumentException e) {
            System.out.println("Invalid transaction: " + e.getMessage());
        } finally {
            System.out.println("ATM session ended. Please collect your card.");
            sc.close();
        }
    }
}
