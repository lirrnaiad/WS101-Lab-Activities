import java.util.Scanner;

public class Fibonacci {
    public static void main(String[] args) {
        int n = getInput();
        displayFibonacciSequence(n);
    }

    public static int getInput() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter nth term: ");
        int n;
        try {
            n = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid integer.\n");
            return getInput();
        }

        return n;
    }

    public static void displayFibonacciSequence(int terms) {
        for (int i = 0; i < terms; i++) {
            System.out.print(fibonacci(i) + " ");
        }
        System.out.println();
    }

    public static int fibonacci(int n) {
        if (n <= 0) return 0;
        if (n == 1) return 1;

        return fibonacci(n - 1) + fibonacci(n - 2);
    }
}
