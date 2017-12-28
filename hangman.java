/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyPrograms;

/**
 *
 * @author s-jazhang
 */

import java.util.Scanner;
import java.util.Random;
import java.util.*;
import java.io.*;

public class hangman {
    private static Scanner scanner = new Scanner( System.in );
    
    public static void main(String[] args) throws IOException
    {
        //Reads the file and creates a list of players and their scores
        BufferedReader bufferedReader = new BufferedReader(new FileReader("C:\\Users\\jasmine\\Desktop\\netbeansStuff\\Unit_One\\src\\MyPrograms\\scoreboard.txt"));
        
        int numOfPlayers = Integer.parseInt(bufferedReader.readLine());
        List<String> playerList = new ArrayList<String>();
        List<Integer> scoreList = new ArrayList<Integer>();
        
        for (int i = 0; i < numOfPlayers; i++)
        {
            playerList.add(bufferedReader.readLine());
            scoreList.add(Integer.parseInt(bufferedReader.readLine()));
        }
        
        bufferedReader.close();
        
        //The list of words to be chosen from; the chosen word is removed from the list
        ArrayList<String> bankList = new ArrayList<>(Arrays.asList("azure", "bagpipes", "banjo", "bikini", "bookworm", "buffalo", "dwarves",
                "equip", "fishhook", "galaxy", "hyphen", "icebox", "injury", "ivory", "ivy", "jaywalk", "jovial", "jigsaw", "joyful", "juicy",
                "jumbo", "keyhole", "luxury", "oxidize", "pajama", "pixel", "polka", "quiz", "sphinx", "topaz", "unknown", "unzip", "subway",
                "vodka", "vortex", "waltz", "wavy", "wizard", "youthful", "zigzag", "zodiac", "zombie"));
                
        Random rand = new Random();
        int location = (int)(rand.nextInt(bankList.size()));
        String word = bankList.get(location).toString();

        bankList.remove(location);
        
        //Sets variables and creates an empty arraylist of guessed letters
        boolean isPlaying = true;
        int lives = 10;
        int correct = 0;
        int score = 0;
        ArrayList<Character> guessedLetters = new ArrayList<Character>();
        
        //Generates stars depending on the length of the word
        String emptyString = generateEmpty(word.length());
        
        //Saves your name to be used on the leaderboards
        boolean nameEntered = false;
        System.out.print("Enter your name: ");
        String player = scanner.nextLine();
        while (!nameEntered)
        {
            if (player.isEmpty())
            {
                System.out.println("Please try again.");
                System.out.print("Enter your name: ");
                player = scanner.nextLine();
            }
            else
            {
                nameEntered = true;
            }
        }
        
        while (isPlaying)
        {  
            System.out.println(emptyString);
            
            //Gets the first letter of the input
            String inputStr = scanner.nextLine();
            
            if (!inputStr.isEmpty())
            {
                char input = inputStr.toLowerCase().charAt(0);

                //Checking if the letter has already been guessed
                if (wasGuessed(guessedLetters, input))
                {
                    System.out.println("You've already guessed that letter.");
                }
                else if (!Character.isLetter(input))
                {
                    System.out.println("Please input a letter.");
                }
                else
                {
                    guessedLetters.add(input);
                    //Checks if the guessed letter is correct
                    if (isCorrect(word, input))
                    {
                        //Modifies the emptySpaces string
                        emptyString = emptySpaces(word, emptyString, input);

                        //Changes the counter for number of letters correct
                        correct = changeCorrect(word, input, correct);

                        //If you've guess all the letters, choose a different word
                        if (correct == word.length())
                        {
                            System.out.println(emptyString);

                            //Gives emotional support to the player
                            System.out.println("Good job! The word was '" + word + "'!");

                            //Resets information and adds to your score
                            correct = 0;
                            score += 1;
                            lives = 10;
                            guessedLetters.clear();

                            //Finds a new word
                            location = (int)(rand.nextInt(bankList.size()));
                            word = bankList.get(location).toString();

                            bankList.remove(location);

                            //Generates the empty star string again
                            emptyString = generateEmpty(word.length());
                        }
                    }
                    else
                    {
                        lives -= 1;

                        if (lives <= 0)
                        {
                            //Stops the game, and prints game over and your score
                            isPlaying = false;
                            System.out.println("Game Over! The word was '" + word + ".'");
                            System.out.println("Your score: " + score + "\n");

                            //Adds player and their score to the player and score lists and prints the scoreboard
                            updateScoreboard(playerList, scoreList, player, score);

                            //Writes the scoreboard out to a file
                            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("C:\\Users\\jasmine\\Desktop\\netbeansStuff\\Unit_One\\src\\MyPrograms\\scoreboard.txt"));
                            bufferedWriter.write(Integer.toString(playerList.size()));
                            bufferedWriter.newLine();

                            for (int i = 0; i < playerList.size(); i++)
                            {
                                bufferedWriter.write(playerList.get(i));
                                bufferedWriter.newLine();
                                bufferedWriter.write(Integer.toString(scoreList.get(i)));
                                bufferedWriter.newLine();
                            }

                            bufferedWriter.close();

                            //Stops the "lives left" thing from printing
                            break;
                        }

                        //If you haven't lost all your lives yet, it prints how many you have left
                        System.out.println("Lives left: " + lives);
                    }
                }
            }
            else
            {
                //Lul this is if the player just pressed enter
                System.out.println("Please input a letter.");
            }
        }     
    }
    
    //Checks if letter is present in the word
    private static boolean isCorrect(String word, char letter)
    {
        
        for (int i = 0; i < word.length(); i++)
        {
            if (word.charAt(i) == letter)
            {
                return true;
            }
        }
        return false;
    }
    
    //Generates the original empty string at the beginning of the game
    private static String generateEmpty(int length)
    {
        String empty = "";
        
        for(int i = 0; i < length; i++)
        {
            empty += "*";
        }
        
        return empty;
    }
    
    //Replaces *'s in the string with the correctly guessed letters
    private static int changeCorrect(String word, char letter, int correct)
    {
        for (int i = 0; i < word.length(); i++)
        {
            if (word.charAt(i) == letter)
            {
                correct += 1;
            }
        }
        
        return correct;
    }
    
    //Generats the string of *'s and correctly guessed letters
    private static String emptySpaces(String word, String oldString, char letter)
    {   
        char[] cArray = oldString.toCharArray();
        
        for (int i = 0; i < word.length(); i++)
        {
            if (word.charAt(i) == letter)
            {
                cArray[i] = letter;
            }
        }
        
        String newString = String.valueOf(cArray);

        return newString;
    }
    
    //Checks if a letter was guessed already
    private static boolean wasGuessed(ArrayList<Character> guessedLetters, char letter)
    {
        for (int i = 0; i < guessedLetters.size(); i++)
        {
            if (guessedLetters.get(i) == letter)
            {
                return true;
            }
        }
        
        return false;
    }
    
    //Prints the scoreboard
    private static void printScoreboard(List<String> playerList, List<Integer> scoreList)
    {
        System.out.println("Highscores:");
        for (int i = 0; i < playerList.size(); i++)
        {
            System.out.println(playerList.get(i) + ": " + scoreList.get(i));
        }
    }
    
    //Updates the player's highscore and adds them to the player list if they're not already in it
    private static void updateScoreboard(List<String> playerList, List<Integer> scoreList, String player, int score)
    {
        boolean inList = false;
        
        if (playerList.contains(player))
        {
            inList = true;
            int location = playerList.indexOf(player);
            
            if (scoreList.get(location) < score)
            {
                scoreList.set(location, score);
            }
        }
        
        if (!inList)
        {
            playerList.add(player);
            scoreList.add(score);
        }
        
        printScoreboard(playerList, scoreList);
    }
}
