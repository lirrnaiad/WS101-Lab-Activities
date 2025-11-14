import java.util.Scanner;

public class Palindrome {
    public static void main(String[] args) {
        String input = getInput();
        System.out.println("Is '" + input + "' a palindrome? " + (checkPalindrome(input) ? "Yes" : "No"));
    }

    public static String getInput() {
        System.out.print("Enter input: ");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    public static boolean checkPalindrome(String input) {
        input = input.toLowerCase().replace(" ", "");
        String reverse = reverseString(input);

        return input.equals(reverse);
    }

    public static String reverseString(String str) {
        StringBuilder sb = new StringBuilder(str);
        return sb.reverse().toString();
    }
}
