package cinema;

import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Stream;

public class Cinema {
    static boolean isFixed = false;
    static int currentIncome = 0;

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of rows:\n");
        int rows = scanner.nextInt();
        System.out.print("Enter the number of seats in each row:\n");
        int columns = scanner.nextInt();

        if (rows * columns <= 60) isFixed = true;

        char[][] seating = new char[rows][columns];
        int[][] seatPrices = new int[rows][columns];

        buildTheater(rows, columns, seating, seatPrices);

        begin(scanner, columns, rows, seating, seatPrices);

    }

    private static void begin(Scanner scanner, int columns, int rows, char[][] seating, int[][] seatPrice) {
        int key = showMenu(scanner);
        if (key != 0) {
            if (key == 1) {
                printSeats(columns, rows, seating);
                begin(scanner, columns, rows, seating, seatPrice);
            } else if (key == 2) {
                buyingSeats(scanner, seating, seatPrice);
                begin(scanner, columns, rows, seating, seatPrice);
            } else if (key == 3) {
                printStatistics(seating, seatPrice);
                begin(scanner, columns, rows, seating, seatPrice);
            } else scanner.close();
        }
    }

    private static void printStatistics(char[][] seating, int[][] seatPrice) {
        int purchasedNumber = (int) Stream.of(seating)
                .flatMapToInt(row -> new String(row).chars())
                .filter(c -> c == 'B')
                .count();

        System.out.println("\nNumber of purchased tickets: " + purchasedNumber);
        int totalSeats = seating.length * seating[0].length;
        double percentage = (double) purchasedNumber / totalSeats * 100.0;
        int possibleIncome = Arrays.stream(seatPrice)
                .flatMapToInt(Arrays::stream)
                .sum();

        System.out.printf("Percentage: %.2f%%\n", percentage);
        System.out.println("Current income: $" + currentIncome);
        System.out.println("Total income: $" + possibleIncome);
    }

    private static void buyingSeats(Scanner scanner, char[][] seating, int[][] seatPrice) {
        int selectedRow;
        int selectedColumn;

        System.out.println("Enter a row number:");
        selectedRow = scanner.nextInt();
        System.out.println("Enter a seat number in that row:");
        selectedColumn = scanner.nextInt();
        if (selectedRow <= seating.length && selectedColumn <= seating[0].length)
            if (seating[selectedRow - 1][selectedColumn - 1] != 'B') {
                int payment = seatPrice[selectedRow-1][selectedColumn-1];
                seating[selectedRow - 1][selectedColumn - 1] = 'B';
                currentIncome += payment;
                System.out.println("Ticket price: $" + payment);
            } else {
                System.out.println("That ticket has already been purchased");
                buyingSeats(scanner, seating, seatPrice);
            }else {
            System.out.println("Wrong input");
            buyingSeats(scanner, seating, seatPrice);
        }

    }

    private static int showMenu(Scanner scanner) {
        System.out.println("\n1. Show the seats\n2. Buy a ticket\n3. Statistics\n0. Exit");
        return scanner.nextInt();
    }

    private static void buildTheater(int rows, int columns, char[][] seating, int[][] seatPrices) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                seating[i][j] = 'S';
                setPrice(i, j, seatPrices);
            }
        }
    }

    private static void setPrice(int rows, int columns, int[][] seatPrices) {
        if (isFixed) seatPrices[rows][columns] = 10;
        else if (seatPrices.length / 2 > columns) {
            seatPrices[columns][rows] = 10;
        } else seatPrices[columns][rows] = 8;
    }

    private static void printSeats(int columns, int rows, char[][] seating) {
        System.out.println("Cinema:");
        System.out.print("  ");
        for (int j = 1; j <= columns; j++) {
            System.out.print(j + " ");
        }
        System.out.println();

        for (int i = 0; i < rows; i++) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < columns; j++) {
                System.out.print(seating[i][j] + " ");
            }
            System.out.println();
        }
    }
}