import java.util.Scanner;
/**
 * This class runs the Mastermind game, it asks the user if they want to guess or want to set code
 * and then runs the approperiate method.
 * @author Luke Ward
 * @version 12/16/2022
 */
public class Driver{
    public static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args){
        boolean playerGuess = false;
        System.out.print("Enter g if you want to guess. Press anything else if you want the computer to guess> ");

        playerGuess = (scanner.nextLine().toUpperCase().equals("G"));
        if(playerGuess){
            Game game = new Game(true);
            game.go();
        } else {
            Com com = new Com();
            com.comgo();
        }
    }
}