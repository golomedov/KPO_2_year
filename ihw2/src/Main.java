import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Insert abs. path to root directory:");
        String absRootPath = scanner.nextLine();
        System.out.println("Insert abs. path to output file:");
        String outputFilePath = scanner.nextLine();

        try {
            DependencyFinder finder = new DependencyFinder(absRootPath);
            boolean success = finder.concatFilesByDependenciesOrder(outputFilePath);
            if(success){
                System.out.println("The output file has been created successfully.");
            } else {
                System.out.println("The output file was not created.");
            }
        } catch (Exception e){
            System.out.println("Something went wrong. Try again.");
        }
    }
}