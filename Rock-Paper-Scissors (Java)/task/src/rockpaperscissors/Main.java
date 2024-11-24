package rockpaperscissors;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    private static final String RATING_FILE = "rating.txt";
    private static final Map<String, Integer> ratings = new HashMap<>();
    private static int userScore = 0;
    private static List<String> options = Arrays.asList("rock", "paper", "scissors");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your name: ");
        String userName = scanner.nextLine();
        System.out.println("Hello, " + userName);

        loadRatings();
        userScore = ratings.getOrDefault(userName, 0);

        System.out.print("Enter the options: ");
        String optionsInput = scanner.nextLine();
        if (!optionsInput.isEmpty()) {
            options = Arrays.asList(optionsInput.split(","));
        }
        System.out.println("Okay, let's start");

        String userChoice;
        while (true) {
            userChoice = scanner.nextLine().toLowerCase();

            if (userChoice.equals("!exit")) {
                System.out.println("Bye!");
                break;
            }

            if (userChoice.equals("!rating")) {
                System.out.println("Your rating: " + userScore);
                continue;
            }

            if (!options.contains(userChoice)) {
                System.out.println("Invalid input");
                continue;
            }

            String computerChoice = getRandomChoice();
            String result = determineResult(userChoice, computerChoice);

            System.out.println(result);
        }
    }

    private static void loadRatings() {
        try (BufferedReader br = new BufferedReader(new FileReader(RATING_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length == 2) {
                    String name = parts[0];
                    int score = Integer.parseInt(parts[1]);
                    ratings.put(name, score);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the rating file: " + e.getMessage());
        }
    }

    private static String getRandomChoice() {
        Random random = new Random();
        return options.get(random.nextInt(options.size()));
    }

    private static String determineResult(String userChoice, String computerChoice) {
        if (userChoice.equals(computerChoice)) {
            userScore += 50;
            return String.format("There is a draw (%s)", computerChoice);
        }

        List<String> remainingOptions = new ArrayList<>(options);
        int userChoiceIndex = remainingOptions.indexOf(userChoice);
        Collections.rotate(remainingOptions, -userChoiceIndex - 1);
        int halfSize = remainingOptions.size() / 2;
        List<String> losingOptions = remainingOptions.subList(0, halfSize);
        List<String> winningOptions = remainingOptions.subList(halfSize, remainingOptions.size());

        if (winningOptions.contains(computerChoice)) {
            userScore += 100;
            return String.format("Well done. The computer chose %s and failed", computerChoice);
        } else {
            return String.format("Sorry, but the computer chose %s", computerChoice);
        }
    }
}