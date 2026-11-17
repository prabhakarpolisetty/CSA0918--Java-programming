public class DeadlockAndPrevention {
    static class Wallet {
        final int id;
        int balance = 1000;
        Wallet(int id) { this.id = id; }
    }

    static void safeTransfer(Wallet from, Wallet to, int amount) {
        Wallet first = from.id < to.id ? from : to;
        Wallet second = from.id < to.id ? to : from;
        synchronized (first) {
            synchronized (second) {
                if (from.balance >= amount) {
                    from.balance -= amount;
                    to.balance += amount;
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Wallet a = new Wallet(1), b = new Wallet(2);
        Thread t1 = new Thread(() -> { for (int i=0;i<1000;i++) safeTransfer(a,b,1); });
        Thread t2 = new Thread(() -> { for (int i=0;i<1000;i++) safeTransfer(b,a,1); });
        t1.start(); t2.start();
        t1.join(); t2.join();
        System.out.println("Safe transfers completed.");
        System.out.println("Wallet A balance: " + a.balance);
        System.out.println("Wallet B balance: " + b.balance);
        System.out.println("Total balance: " + (a.balance + b.balance));
        System.out.println("Lock ordering prevents circular wait.");
    }
}
