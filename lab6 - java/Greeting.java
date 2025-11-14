import java.util.Scanner;

public class Greeting {
    Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Greeting greeting = new Greeting();
        greeting.greetUser();
    }

    public void greetUser() {
        while (true) {
            IO.print("Enter your name: ");
            String name;
            try {
                name = IO.readln();
            } catch (Exception e) {
                IO.println("An error occurred while reading your name.\n");
                continue;
            }

            int age;
            IO.print("Enter your age: ");
            try {
                age = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                IO.println("Invalid input for age. Please enter a valid number.\n");
                continue;
            }

            IO.println("Hello, " + name + "!" + " You are " + age + " years old.");
            break;
        }
    }
}
