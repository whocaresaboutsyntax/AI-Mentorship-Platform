package com.example.aimentorship;

import java.util.Scanner;

import java.io.IOException;


public class Main {
    private static final String[] MENTORSHIP_KEYWORDS = {"mentorship", "career", "college", "job", "guidance", "advice"};

    public static void main(String[] args) {
        String apiKey = "AIzaSyARLyJKPqz9ifFSuD8S_BJhPGWZRJhEoyc"; // Replace with your actual API key
        GeminiAPIClient geminiClient = new GeminiAPIClient(apiKey);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the AI Mentorship Platform!");

        while (true) {
            System.out.print("\nEnter your mentorship domain (college/career/job) or 'exit' to quit: ");
            String domain = scanner.nextLine().trim();
            if (domain.equalsIgnoreCase("exit")) {
                System.out.println("Exiting the AI Mentorship Platform. Goodbye!");
                break;
            }

            System.out.print("Enter your question: ");
            String question = scanner.nextLine().trim();

            if (!isMentorshipQuestion(question)) {
                System.out.println("Sorry, I can only assist with mentorship-related questions.");
                continue;
            }

            try {
                String guidance = geminiClient.getResponse(question, domain);
                System.out.println("\nAI Mentor's Guidance:\n" + guidance);
            } catch (IOException e) {
                System.err.println("Error communicating with Gemini API: " + e.getMessage());
            }
        }

        scanner.close();
    }

    private static boolean isMentorshipQuestion(String question) {
        String lowerCaseQuestion = question.toLowerCase();
        for (String keyword : MENTORSHIP_KEYWORDS) {
            if (lowerCaseQuestion.contains(keyword)) {
                return true;
            }
        }
        return false;
    }
}

