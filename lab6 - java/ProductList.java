import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ProductList {
    private ArrayList<Product> products;

    public ProductList() {
        this.products = new ArrayList<>();
        products.add(new Product("iPad Air 5", 56000));
        products.add(new Product("Samsung Galaxy A56", 16000));
        products.add(new Product("Nintendo Switch Lite", 8000));
        products.add(new Product("POCO M6 Pro", 8600));
        products.add(new Product("Capacitive Stylus", 500));
        products.add(new Product("Phone Holder", 300));
        products.add(new Product("iPhone 17 Pro Max 1TB", 101990));
        products.add(new Product("Mitsubishi Montero Sport 2025", 1568000));
    }

    public List<Product> getProductsMoreExpensiveThan(double price) {
        return products.stream().filter(product -> product.getPrice() > price).collect(Collectors.toList());
    }

    public void printExpensiveProducts(List<Product> products) {
        for (Product product : products) {
            System.out.println(product.getName());
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ProductList productList = new ProductList();

        System.out.print("Enter price to find products more expensive than this: ");
        double price = Double.parseDouble(scanner.nextLine());
        productList.printExpensiveProducts(productList.getProductsMoreExpensiveThan(price));
    }

    public class Product {
        private String name;
        private double price;

        public Product(String name, double price) {
            this.name = name;
            this.price = price;
        }

        public String getName() {
            return this.name;
        }

        public double getPrice() {
            return this.price;
        }
    }
}
