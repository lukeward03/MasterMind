import java.util.NoSuchElementException;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Random;
/**
 * This class holds all of the logic for when the user is guessing the com code
 * @author Luke Ward
 * @version 12/16/2022
 */
public class Game {
    /** An integer array that holds the code for the game */
    private Integer[] code;
    /** an int that holds the round number */
    private int round;
    /** A string which holds round info to print every round */
    private String gameString;


    public static final int MIN_PEG = 1; //Min value for pegs
    public static final int MAX_PEG = 6; // MAx value for pegs
    public static final int MAX_ROUNDS = 12; // Number of rounds in a game
    public static final int CODE_LENGTH = 4; // How many ints a code is
    
    public static Scanner scanner = new Scanner(System.in); //Scanner for user input

    /**
     * Constructor which sets fields for the game
     * @param playerGuessing - true if player is guessing com code
     */
    public Game(boolean playerGuessing){

        if(playerGuessing){
            this.code = comGenCode();
        } else {
            this.code = playerGenCode();
        }
        round = 0;
        this.gameString = "";
    }
    /**
     * This method returns the code
     * @return this.code - The code
     */
    public Integer[] getCode(){
        return this.code;
    }

    /**
     * This method returns the round number
     * @return this.round - round number
     */
    public int getRound(){
        return this.round;
    }

    /**
     * Returns the game string  
     * @return this.gameString - the game string
     */
    public String getGameString(){
        return this.gameString;
    }

    /** 
     * Set the round to a new 
     * @param newRound - new round value
    */
    public void setRound(int newRound){
        this.round = newRound;
    }

    /**
     * Set the code to a new falue
     * @param code - New code value
     */
    protected void setCode(Integer[] code){
        this.code = code;
    }

    /**
     * runs the playerguess function
    */ 
    public void go(){
        playerGuess();
    }

    /**
     * Holds all of the player guessing logic and stuff. Calls approperate functions when
     * necessary
     */
    private void playerGuess(){
        while(this.round < MAX_ROUNDS){
            Integer[] guess = new Integer[CODE_LENGTH];
            Result[] result = new Result[CODE_LENGTH];

            for(int i = 0; i < CODE_LENGTH; i++){
                try{
                guess[i] = playerInputPeg(i);
                } catch (InputMismatchException ime){
                    i--;
                } catch (NoSuchElementException nsee){
                    i--;
                }
            }

            result = check(guess, this.code);
            if(winnerCheck(result)){
                setGameString(guess, result);
                System.out.println(this.gameString);
                winner();
                this.round = MAX_ROUNDS + 1;
            } else {
                setGameString(guess, result);
                System.out.println(this.gameString);
                this.round++;
                if(this.round == MAX_ROUNDS){
                    loser();
                }
            }
        }
    }

    /**
     * Prints a string for when the player loses (They run out of guesses)
     */
    private void loser(){
        System.out.println("You did not guess the code correctly in " + MAX_ROUNDS + " rounds. Sorry.\n" +
                            "The code was " + Arrays.toString(code));
    }

    /**
     * Updates the gamestring to include that rounds information
     */
    protected void setGameString(Integer[] guess, Result[] result){
        this.gameString = this.gameString + "\nRound " + (this.round + 1) + ":\n" + Arrays.toString(guess) + "\n" + resultToString(result) + "\n" + 
                          "===================================";
    }

    /**
     * returns a string containting !, ?, or - depending on if that relative
     * slot is right number right spot, right number wrong spot, or wrong number
     * @param res- Array of result enums
     * @return string- the result as a string
     */
    private String resultToString(Result[] res){
        String string = "";
        for(Result result: res){
            if(result == Result.CORRECT){
                string = string + "!  ";
            } else if (result == Result.CONTAINS){
                string = string + "?  ";
            } else {
                string = string + "-  ";
            }

        }

        return string;
    }

    /**
     * Prints a winner string for if the user wins
     */
    private void winner(){
        System.out.println("\nYou guessed the code " + Arrays.toString(code) + " correctly in " + (this.round + 1) + " Rounds! ");
    }

    /**
     * Checks to see if the result is correct 4 times, meaning the user wins!
     * @return winner - true if they have won
     */
    protected boolean winnerCheck(Result[] res){

        boolean winner = true;
        for(int i = 0; i < CODE_LENGTH && winner; i++){
            if(res[i] != Result.CORRECT){
                winner = false;
            }
        }

        return winner;
    }

    /**
     * Compares two values to see what the result of guess is respective to code
     * @param guess - The value being comped to code
     * @param code - The code the guess is being comped to
     * @return Result[] - An array of Result enums 
     */
    protected Result[] check(Integer[] guess, Integer[] code){
        Result[] result = new Result[CODE_LENGTH];
        Integer[] tmp = code.clone();

        for(int h = 0; h < CODE_LENGTH; h++){
            if(this.code[h].equals(guess[h])){
                result[h] = Result.CORRECT;
                tmp[h] = -1;
            }
        }

        for(int i = 0; i < CODE_LENGTH; i++){
            for(int j = 0; j < CODE_LENGTH; j++){
                if(tmp[i].equals(guess[j]) && result[j] != Result.CORRECT){
                    result[j] = Result.CONTAINS;
                }
            }
        }

        for(int k = 0; k < CODE_LENGTH; k++){
            if(result[k] == null){
                result[k] = Result.NOTCORRECT;
            }
        }

        return result;
    }

    /**
     * The code for the computer to generate a code
     * @return Integer[] - The coms code
     */
    private Integer[] comGenCode(){
        Random random = new Random();
        Integer[] comCode = new Integer[CODE_LENGTH];

        for(int i = 0; i < CODE_LENGTH; i++){
            comCode[i] = random.nextInt(MAX_PEG) + 1;
        }
        return comCode;
    }
    /**
     * Function for the user to set the code, takes user input to do so
     * @return Integer[] - The players chosen code
     */
    private Integer[] playerGenCode(){
        Integer[] playerCode = new Integer[CODE_LENGTH];
        System.out.println("You are entering the code you want to set.");
        for(int i = 0; i < CODE_LENGTH; i++){
            try{
                playerCode[i] = playerInputPeg(i);
            } catch (InputMismatchException ime){
                i--;
            } catch (NoSuchElementException nsee){
                i--;
            }
             
        }
        return playerCode;
    }
 
    /**
     * Function for the user to input a peg value
     * @param i - The spot for the peg
     * @return int - The peg value
     * @throws InputMismatchException - If the user does not enter an int
     * @throws NoSuchElementException - If the user enters nothing
     */
    private int playerInputPeg(int i) throws InputMismatchException, NoSuchElementException{
        boolean done = false; 
        int tmp = 0;

            System.out.print("Please enter the value for Peg " + (i + 1) + ">");
       
            try{
                while (!done){
                    tmp = scanner.nextInt();
                    // If peg intetered isnt in bounds
                    if(tmp < MIN_PEG || tmp > MAX_PEG){
                        System.out.println("Your peg must be between 1 and 6. Please try again");
                    } else {
                        done = true;
                    }
                }
                
            } catch (InputMismatchException ime){
                System.out.println("You did not enter an int, please try again.");
                scanner.nextLine();
                throw new InputMismatchException();
            } catch (NoSuchElementException nsee){
                System.out.println("You did not eneter an element. try again.");
                scanner.nextLine();
                throw new NoSuchElementException();
            }
        
        return tmp;
    }
}