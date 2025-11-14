import java.util.Arrays;
import java.util.Scanner;

public class Array {
    public static void main(String[] args) {
        int[] arr = getInput();
        int sum = getSum(arr);
        float average = getAverage(arr);

        displayResults(arr, sum, average);
    }

    public static int[] getInput() {
        Scanner scanner = new Scanner(System.in);

        int[] arr = new int[5];

        int i = 0;
        while (i < arr.length) {
            System.out.printf("Number %d: ", i + 1);
            try {
                arr[i] = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input!");
                continue;
            }

            i++;
        }

        return arr;
    }

    public static void displayResults(int[] arr, int sum, float average) {
        System.out.print("\nArray: ");
        for (int num : arr) {
            System.out.print(num + " ");
        }
        System.out.println();

        System.out.println("Sum: " + sum);
        System.out.printf("Average: %.02f", average);
    }

    public static int getSum(int[] arr) {
        return Arrays.stream(arr).sum();
    }

    public static float getAverage(int[] arr) {
        int sum = getSum(arr);
        return (float) sum / arr.length;
    }
}
