package com.aina.hymnastic.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.aina.hymnastic.constant.Template;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Controller
public class RecommendController {

    private static final Logger logger = LoggerFactory.getLogger(RecommendationController.class);

    @GetMapping("/recommendTracks")
    public String getTrackRecommendations(
            @RequestParam String track,
            @RequestParam String artist,
            Model model
    ){
        // Specify the Python script path
        String pythonScriptPath = "E:\\2nd year\\3rd sem\\Aoop\\RealProject\\Hymnastic--A-music-App\\src\\main\\python\\SongRecommender.py";

        // Input parameters
        String songName = track;
        String artistName = artist;
        int numRecommendations = 5;

        try {
            // Build the command to execute the Python script
            ProcessBuilder processBuilder = new ProcessBuilder("python", pythonScriptPath, "\""+songName+"\"", "\""+artistName+"\"", String.valueOf(numRecommendations));
            logger.info("Command: {}", String.join(" ", processBuilder.command()));

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
                String recommendations = output.toString();
                logger.info("{}", recommendations);
                model.addAttribute("recommendations", recommendations);
            } else {
                // Process failed
                String errorOutput = output.toString();
                logger.error("Error while running the Python script. Exit Code: {}, Output: {}", exitCode, errorOutput);
                model.addAttribute("error", "Error while running the Python script.");
            }

        } catch (IOException | InterruptedException e) {
            logger.error("Exception occurred: {}", e.getMessage(), e);
            model.addAttribute("error", "Exception occurred: " + e.getMessage());
        }

        return Template.Recommend_TRACKS;
    }
    

    @GetMapping("/RecommendArtists")
    public String getArtistRecommendations(
            @RequestParam String artist,
            Model model
    ){
        // Specify the Python script path
        String pythonScriptPath = "E:\\2nd year\\3rd sem\\Aoop\\RealProject\\Hymnastic--A-music-App\\src\\main\\python\\ArtistRecommender.py";

        // Input parameters
        String artistName = artist;
        

        try {
            // Build the command to execute the Python script
            ProcessBuilder processBuilder = new ProcessBuilder("python", pythonScriptPath,"\""+artistName+"\"" );
            logger.info("Command: {}", String.join(" ", processBuilder.command()));

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
                String recommendations = output.toString();
                logger.info("{}", recommendations);
                model.addAttribute("recommendations", recommendations);
            } else {
                // Process failed
                String errorOutput = output.toString();
                logger.error("Error while running the Python script. Exit Code: {}, Output: {}", exitCode, errorOutput);
                model.addAttribute("error", "Error while running the Python script.");
            }

        } catch (IOException | InterruptedException e) {
            logger.error("Exception occurred: {}", e.getMessage(), e);
            model.addAttribute("error", "Exception occurred: " + e.getMessage());
        }

        return Template.Recommend_TRACKS;
    }

    @GetMapping("/recommendLyrics")
    public String getLyricRecommendations(
            @RequestParam String keyword,
            Model model
    ){
        // Specify the Python script path
        String pythonScriptPath = "E:\\2nd year\\3rd sem\\Aoop\\RealProject\\Hymnastic--A-music-App\\src\\main\\python\\LyricRecommender.py";

        // Input parameters
        String Lyric = keyword;
        

        try {
            // Build the command to execute the Python script
            ProcessBuilder processBuilder = new ProcessBuilder("python", pythonScriptPath,"\""+Lyric+"\"" );
            logger.info("Command: {}", String.join(" ", processBuilder.command()));

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
                String recommendations = output.toString();
                logger.info("{}", recommendations);
                model.addAttribute("recommendations", recommendations);
            } else {
                // Process failed
                String errorOutput = output.toString();
                logger.error("Error while running the Python script. Exit Code: {}, Output: {}", exitCode, errorOutput);
                model.addAttribute("error", "Error while running the Python script.");
            }

        } catch (IOException | InterruptedException e) {
            logger.error("Exception occurred: {}", e.getMessage(), e);
            model.addAttribute("error", "Exception occurred: " + e.getMessage());
        }

        return Template.Recommend_LYRICS;
    }
}
