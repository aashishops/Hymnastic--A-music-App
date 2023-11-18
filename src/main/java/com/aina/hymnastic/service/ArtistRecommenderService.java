package com.aina.hymnastic.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ArtistRecommenderService {

    public static void main(String[] args) {
        // Specify the Python script path
        String pythonScriptPath = "E:\\2nd year\\3rd sem\\Aoop\\RealProject\\Hymnastic--A-music-App\\src\\main\\python\\ArtistRecommender.py";

        // Input parameters
        
        String artistName = "Gen Hoshino";
        int numRecommendations = 5;

        try {
            // Build the command to execute the Python script
            ProcessBuilder processBuilder = new ProcessBuilder("python", pythonScriptPath, artistName, String.valueOf(numRecommendations));
            processBuilder.redirectErrorStream(true);

            // Start the process
            Process process = processBuilder.start();

            // Read the output from the Python script
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            StringBuilder output = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            // Wait for the process to finish
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                // Process completed successfully
                System.out.println("Recommendations:\n" + output.toString());
            } else {
                // Process failed
                System.err.println("Error while running the Python script.");
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
