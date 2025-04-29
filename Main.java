package com.example.ainotegenerator;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String apiKey = "AIzaSyARLyJKPqz9ifFSuD8S_BJhPGWZRJhEoyc"; // Replace with your actual API key

        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the AI Note Generator!");

        // Prompt user to input the transcript
        System.out.println("\nPlease enter the transcript (end input with an empty line):");
        StringBuilder transcriptBuilder = new StringBuilder();
        String line;
        while (!(line = scanner.nextLine()).isEmpty()) {
            transcriptBuilder.append(line).append("\n");
        }
        String transcript = transcriptBuilder.toString().trim();

        GeminiApiClient geminiClient = new GeminiApiClient(apiKey);

        while (true) {
            System.out.print("\nEnter your question (or type 'exit' to quit): ");
            String question = scanner.nextLine().trim();
            if (question.equalsIgnoreCase("exit")) {
                System.out.println("Exiting the AI Note Generator. Goodbye!");
                break;
            }

            try {
                String response = geminiClient.getResponse(question, transcript);
                System.out.println("\nAI Response:\n" + response);
            } catch (IOException e) {
                System.err.println("Error communicating with Gemini API: " + e.getMessage());
            }
        }

        scanner.close();
    }
}
