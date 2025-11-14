import java.util.Scanner;

public class Rectangle {
    private int length;
    private int width;

    public Rectangle(int length, int width) {
        this.length = length;
        this.width = width;
    }

    public float calculateArea() {
        return (this.length * this.width);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter length (cm): ");
        int length = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter width (cm): ");
        int width = Integer.parseInt(scanner.nextLine());

        Rectangle rect = new Rectangle(length, width);
        System.out.println("Area of rectangle: " + rect.calculateArea() + " cm");
    }
}
