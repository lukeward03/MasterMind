import java.util.Queue;
import java.util.LinkedList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * The computer logic for the game. Is a child of the Game class
 * @author Luke Ward
 * @version 12/16/2022
 */
public class Com extends Game {
    /** A queue of ints[] containing all possible codes [1,1,1,1] ... [6,6,6,6] */
    private Queue<Integer[]> allCodes;
    /** The guess that the computer will guess */
    private Integer[] toGuess;
    /** A queue of ints[] containing a list of codes that are possible for a certain result */
    private Queue<Integer[]> possibleCodes;

    /**
     * Initilizer, calls super with playerguessing false. Also sets all codes, possible codes, and the toguess to [1,1,2,2]
     */
    public Com(){
        super(false);
        Integer[] tmp = {1,1,2,2};
        this.allCodes = setAllCodes();
        this.possibleCodes = setAllCodes();
        this.toGuess = tmp;
        }

    /**
     * This function sets all possible values [1,1,1,1] => [6,6,6,6] 
     * @return allCodes - A list of all possible values
     */
    private Queue<Integer[]> setAllCodes(){
        Queue<Integer[]> allCodes = new LinkedList<>();
        for(int i = MIN_PEG; i <= MAX_PEG; i++){
            for(int j = MIN_PEG; j <= MAX_PEG; j++){
                for(int k = MIN_PEG; k <= MAX_PEG; k++){
                    for(int l = MIN_PEG; l <= MAX_PEG; l++){
                        Integer[] tmp = {i, j, k, l};
                        allCodes.add(tmp);
                    }
                }
            }
        }
        return allCodes;
    }

    /**
     * This method destroys invalid codes by comparing the code to the result
     * @param - Code, the code to compare to res
     * @param - the Result[] to check the value to
     */
    private void destroyCodes(Integer[] code, Result[] res){
        Iterator<Integer[]> itir = possibleCodes.iterator();
        while(itir.hasNext()){
            Integer[] tmp = itir.next();
            if(!(Arrays.equals(compTwoCodes(tmp, this.toGuess), res))){
                itir.remove();
            }
        }
    }

    /**
     * This method compares two codes together and checks the relative result between them
     * @param code1- Code one to compare to code2
     * @param code2- code two to compare to code1
     * @return REsult[] - the result between the two
     */
    private Result[] compTwoCodes(Integer[] code1, Integer[] code2){
        Result[] result = new Result[CODE_LENGTH];
        Integer[] tmp = code1.clone();
    
        for(int h = 0; h < CODE_LENGTH; h++){
            if(code1[h].equals(code2[h])){
                result[h] = Result.CORRECT;
                tmp[h] = -1;
            }
        }
    
        for(int i = 0; i < CODE_LENGTH; i++){
            for(int j = 0; j < CODE_LENGTH; j++){
                if(tmp[i].equals(code2[j]) && result[j] != Result.CORRECT){
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
     * Return the toguess value
     * @return toGuess
     */
    public Integer[] getToGuess(){
        return this.toGuess;
    }

    /**
     * Prints a winner line for when the computer wins
     */
    private void winner(){
        System.out.println("I win! Better luck next time.\nI guessed the code " + Arrays.toString(toGuess) + " in " + (super.getRound() + 1) + " round(s)");
    }

    /**
     * Prints a loser line for when the comp loses (this shouldn't be possible, but its flavor text)
     */
    private void loser(){
        System.out.println("I... I lost? That shouldn't be possible. Im flawless.");
    }

    /**
     * This function holds the five guess algorithim. Essentially chooces the code that had the lest amount of hits, meaning
     * that it would remove the most possible codes from the remaining pool.
     */
    private void fiveGuess(){
        int smallestHitCount = 10000000;
        Result[] res = super.check(this.toGuess, super.getCode());
        Integer[] guess = new Integer[CODE_LENGTH];
    
        for (Integer[] code1 : this.possibleCodes){
            int hitCount = 0;
            for(Integer[] code2: this.allCodes){
                    
                if(Arrays.equals(compTwoCodes(code1, code2), res)){
                    hitCount++;
                }

            }
            if(hitCount < smallestHitCount){
                smallestHitCount = hitCount;
                guess = code1;
            }
        }
        this.toGuess = guess;
    }

    /**
     * This method runs the com logic, this is what the Driver calls.
     */
    public void comgo(){
        Result[] result = new Result[CODE_LENGTH];
        while(super.getRound() < MAX_ROUNDS){
            result = super.check(this.toGuess, super.getCode());
            setGameString(this.toGuess, result);
            System.out.println(super.getGameString());
            this.allCodes.remove(this.toGuess);
    
            if(winnerCheck(result)){
                this.winner();
                setRound(MAX_ROUNDS + 1);
            } else {
                this.destroyCodes(this.toGuess, result);
                setRound(getRound() + 1);
                if(super.getRound() == MAX_ROUNDS) loser();
                fiveGuess();
            }
        }
    }
}
