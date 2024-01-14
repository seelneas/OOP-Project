package Project;
import java.util.*;

class Movie {
    private String title;
    private double price;
    private String showtime;

    public Movie(String title, double price, String showtime) {
        this.title = title;
        this.price = price;
        this.showtime = showtime;
    }

    public String getTitle() {
        return title;
    }

    public double getPrice() {
        return price;
    }

    public String getShowtime() {
        return showtime;
    }
    
}

class Customer {
    private String username;
    private String password;
    private double rating;
    private String review;

    public Customer(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}

public class Main {
    private static List<Movie> movies = new ArrayList<>();
    private static List<Customer> customers = new ArrayList<>();
    private static double revenue = 0;

    public static void main(String[] args) {
        initializeMovies();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Welcome to the Movie Ticketing System!");

            System.out.println("Are you a customer or a manager? (Enter 'customer' or 'manager')");
            String userType = scanner.nextLine();

            if (userType.equalsIgnoreCase("customer")) {
                System.out.println("Do you have an account? (Enter 'yes' or 'no')");
                String haveAccount = scanner.nextLine();

                if (haveAccount.equalsIgnoreCase("yes")) {
                    Customer customer = login(scanner);
                    if (customer == null) {
                        System.out.println("Invalid username or password.");
                        continue;
                    }
                } else if (haveAccount.equalsIgnoreCase("no")) {
                    Customer newCustomer = signUp(scanner);
                    if (newCustomer == null) {
                        System.out.println("Failed to create an account.");
                        continue;
                    }
                } else {
                    System.out.println("Invalid option.");
                    continue;
                }

                displayMovies();
                Movie selectedMovie = selectMovie(scanner);

                if (selectedMovie == null) {
                    System.out.println("Invalid movie selection.");
                    continue;
                }

                int[][] seats = new int[7][7];
                selectSeats(scanner, seats);

                double totalPrice = selectedMovie.getPrice() * countSelectedSeats(seats);

                System.out.println("Total Price: $" + totalPrice);
                System.out.println("Thank you for your purchase!");

                revenue += totalPrice;

                rateAndReview(scanner);
                cancelTicket(scanner);

            } else if (userType.equalsIgnoreCase("manager")) {
                System.out.println("Enter the manager username:");
                String username = scanner.nextLine();

                System.out.println("Enter the manager password:");
                String password = scanner.nextLine();

                if (username.equals("admin") && password.equals("password")) {
                    System.out.println("Daily Revenue: $" + revenue);
                    manageMovies(scanner);
                } else {
                    System.out.println("Invalid username or password.");
                    continue;
                }
            } else {
                System.out.println("Invalid option.");
            }
        }
    }

    private static void initializeMovies() {
        movies.add(new Movie("Movie 1", 10.0, "10:00 AM"));
        movies.add(new Movie("Movie 2", 12.0, "1:00 PM"));
        movies.add(new Movie("Movie 3", 15.0, "4:00 PM"));
    }

    private static Customer login(Scanner scanner) {
        System.out.println("Enter your username:");
        String username = scanner.nextLine();

        System.out.println("Enter your password:");
        String password = scanner.nextLine();

        for (Customer customer : customers) {
            if (customer.getUsername().equals(username) && customer.getPassword().equals(password)) {
                System.out.println("Login successful. Welcome, " + username + "!");
                return customer;
            }
        }

        return null;
    }

    private static Customer signUp(Scanner scanner) {
        System.out.println("Enter a username:");
        String username = scanner.nextLine();

        System.out.println("Enter a password:");
        String password = scanner.nextLine();

        Customer newCustomer = new Customer(username, password);
        customers.add(newCustomer);

        System.out.println("Account created successfully. Welcome, " + username + "!");
        return newCustomer;
    }

    private static void displayMovies() {
        System.out.println("Available Movies:");

        for (int i = 0; i < movies.size(); i++) {
            Movie movie = movies.get(i);
            System.out.println((i + 1) + ". " + movie.getTitle() + " | Price: $" + movie.getPrice() + " | Showtime: " + movie.getShowtime());
        }
    }

    private static Movie selectMovie(Scanner scanner) {
        System.out.println("Enter the number of the movie you want to watch:");
        int movieNumber = scanner.nextInt();
        scanner.nextLine();

        if (movieNumber > 0 && movieNumber <= movies.size()) {
            return movies.get(movieNumber - 1);
        }

        return null;
    }

    private static void selectSeats(Scanner scanner, int[][] seats) {
        System.out.println("Select seats (enter row and column, separated by space; enter 'done' when finished or insert 'delete' to remove already chosen seat):");
    
        while (true) {
            String input = scanner.nextLine();
    
            if (input.equalsIgnoreCase("done")) {
                break;
            }
    
            if (input.equalsIgnoreCase("delete")) {
                System.out.println("Enter the seat to delete (row and column, separated by space):");
                String seatToDelete = scanner.nextLine();
                String[] seatCoordinates = seatToDelete.split(" ");
    
                if (seatCoordinates.length != 2) {
                    System.out.println("Invalid input. Please try again.");
                    continue;
                }
    
                int row, column;
    
                try {
                    row = Integer.parseInt(seatCoordinates[0]);
                    column = Integer.parseInt(seatCoordinates[1]);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please try again.");
                    continue;
                }
    
                if (row < 1 || row > 7 || column < 1 || column > 7) {
                    System.out.println("Invalid seat. Please try again.");
                    continue;
                }
    
                if (seats[row - 1][column - 1] == 0) {
                    System.out.println("Seat not selected. Please try again.");
                    continue;
                }
    
                System.out.println("Are you sure you want to delete this seat? (yes/no)");
                String confirmation = scanner.nextLine();
    
                if (confirmation.equalsIgnoreCase("yes")) {
                    seats[row - 1][column - 1] = 0;
                    System.out.println("Seat deleted successfully.");
                } else {
                    System.out.println("Seat deletion canceled.");
                }
                continue;
            }
    
            String[] seatCoordinates = input.split(" ");
    
            if (seatCoordinates.length != 2) {
                System.out.println("Invalid input. Please try again.");
                continue;
            }
    
            int row, column;
    
            try {
                row = Integer.parseInt(seatCoordinates[0]);
                column = Integer.parseInt(seatCoordinates[1]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please try again.");
                continue;
            }
    
            if (row < 1 || row > 7 || column < 1 || column > 7) {
                System.out.println("Invalid seat. Please try again.");
                continue;
            }
    
            if (seats[row - 1][column - 1] == 1) {
                System.out.println("Seat already selected. Please try again.");
                continue;
            }
    
            seats[row - 1][column - 1] = 1;
        }
    }

    private static int countSelectedSeats(int[][] seats) {
        int count = 0;

        for (int i = 0; i < seats.length; i++) {
            for (int j = 0; j < seats[i].length; j++) {
                if (seats[i][j] == 1) {
                    count++;
                }
            }
        }

        return count;
    }

    private static void rateAndReview(Scanner scanner) {
        System.out.println("Please rate our service out of 10:");
        int rating = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Please write a review (optional):");
        String review = scanner.nextLine();

        

        System.out.println("Thank you for your feedback!");
    }

    private static void cancelTicket(Scanner scanner) {
        System.out.println("Do you want to cancel the ticket? (Enter 'yes' or 'no')");
        String cancelOption = scanner.nextLine();

        if (cancelOption.equalsIgnoreCase("yes")) {
            System.out.println("Ticket cancelled successfully.");

           

            System.out.println("Refund processed successfully.");
        }
    }

    private static void manageMovies(Scanner scanner) {
        while (true) {
            System.out.println("Currently Available Movies:");

            for (int i = 0; i < movies.size(); i++) {
                Movie movie = movies.get(i);
                System.out.println((i + 1) + ". " + movie.getTitle() + " | Price: $" + movie.getPrice() + " | Showtime: " + movie.getShowtime());
            }

            System.out.println("Select an option:");
            System.out.println("1. Add a movie");
            System.out.println("2. Remove a movie");
            System.out.println("3. Go back to the main menu");
            System.out.println("4. Exit");

            int option = scanner.nextInt();
            scanner.nextLine();

            if (option == 1) {
                addMovie(scanner);
            } else if (option == 2) {
                removeMovie(scanner);
            } else if (option == 3) {
                break;}
                else if (option == 4){
                    System.exit(option);
                }
             else {
                System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void addMovie(Scanner scanner) {
        System.out.println("Enter the movie title:");
        String title = scanner.nextLine();

        System.out.println("Enter the movie price:");
        double price = scanner.nextDouble();
        scanner.nextLine();

        System.out.println("Enter the movie showtime:");
        String showtime = scanner.nextLine();

        Movie newMovie = new Movie(title, price, showtime);
        movies.add(newMovie);

        System.out.println("Movie added successfully.");
    }

    private static void removeMovie(Scanner scanner) {
        System.out.println("Enter the number of the movie you want to remove:");
        int movieNumber = scanner.nextInt();
        scanner.nextLine();

        if (movieNumber > 0 && movieNumber <= movies.size()) {
            Movie removedMovie = movies.remove(movieNumber - 1);
            System.out.println(removedMovie.getTitle() + " removed successfully.");
        }
    }
}

