package unluckyrobot;

import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author Denmar Ermitano
 */
public class UnluckyRobot {

    public static void main(String[] args) {
        int totalScore = 300;
        int itrCount = 0;
        int reward;
        int x = 0;
        int y = 0;
        int payment1 = 10;
        int payment2 = 50;
        int exceedDmg = 2000;
        
        do {
            displayInfo(x, y, itrCount, totalScore);
            char direction = inputDirection();
            
            if (doesExceed(x, y, direction)) {
                System.out.println("Exceed boundary, -2000 damage applied");
                totalScore -= exceedDmg;
            }
            else {
                switch (direction) {
                    case 'u':
                        y++;
                        break;
                    case 'd':
                        y--;
                        break;
                    case 'l':
                        x--;
                        break;
                    case 'r':
                        x++;
                        break;
                }
            }
            
            if (direction == 'u')
                totalScore -= payment1;
            else 
                totalScore -= payment2;
            
            reward = punishOrMercy(direction, reward());
            totalScore += reward;
            System.out.println();
            itrCount++;
        } while (!isGameOver(x, y, totalScore, itrCount));
        
        Scanner console = new Scanner(System.in);
        System.out.print("Please enter your name (only two words): ");
        String name = toTitleCase(console.nextLine());
        evaluation(totalScore, name);
    }
    
    /**
     * To display current position of robot, the number of iterations and the score
     * @param x the position of the robot on the x axis
     * @param y the position of the robot on the y axis
     * @param itrCount the total number of iterations
     * @param totalScore the total score of the player
     */
    public static void displayInfo(int x, int y, int itrCount, int totalScore) {
        System.out.printf("%s (%c=%d, %c=%d) %s: %d %s: %d\n", "For point", 'X', x, 'Y', y,
                "at iteration", itrCount, "the total score is", totalScore);
    }
    
    /**
     * To check if the robot is out of bounds, below 0 or above 4 on either axis
     * @param x the position of the robot on the x axis
     * @param y the position of the robot on the y axis
     * @param direction the chosen direction for the robot to move in
     * @return true if robot has a position outside of [0, 4], false if positions are in [0, 4]
     */
    public static boolean doesExceed(int x, int y, char direction) {
        switch (direction) {
            case 'u':
                y++;
                break;
            case 'd':
                y--;
                break;
            case 'l':
                x--;
                break;
            case 'r':
                x++;
                break;
        }
        
        int lowerLimit = 0;
        int upperLimit = 4;
        return (x < lowerLimit || x > upperLimit || y < lowerLimit || y > upperLimit);
    }
    
    /**
     * To roll a dice and calculate the reward to give for a movement
     * @return the reward in points to give to the player
     */
    public static int reward() {
        Random rand = new Random();
        int amount = 6;
        int min = 1;
        int dice = rand.nextInt(amount) + min;
        int reward;
        
        switch (dice) {
            case (1):
                reward = -100;
                break;
            case (2):
                reward = -200;
                break;
            case (3):
                reward = -300;
                break;
            case (4):
                reward = 300;
                break;
            case (5):
                reward = 400;
                break;
            default:
                reward = 600;
        }
        
        System.out.printf("%s: %d, %s: %d\n", "Dice", dice, "reward", reward);
        return reward;
    }
    
    /**
     * To flip a coin and decide if the player should not get a negative reward
     * @param direction the chosen direction for the robot to move in
     * @param reward the negative reward to be applied or removed
     * @return the reward the player should get, either negative or nothing
     */
    public static int punishOrMercy(char direction, int reward) {
        if (reward > 0)
            return reward;
        else if (direction != 'u')
            return reward;
        
        Random rand = new Random();
        int amount = 2;
        int coin = rand.nextInt(amount);
        
        if (coin == 0) {
            System.out.printf("%s: %s | %s\n", "Coin", "tail",
                    "Mercy, the negative reward is removed");
            return 0;
        }
        else {
            System.out.printf("%s: %s | %s\n", "Coin", "head",
                    "No mercy, the negative reward is applied");
            return reward;
        }
    }
    
    /**
     * To change the name of the player into title case
     * @param str the name of the player
     * @return the name of the player in title case
     */
    public static String toTitleCase(String str) {
        str = str.toLowerCase();
        int idx = str.indexOf(" ");
        
        String firstName = Character.toTitleCase(str.charAt(0)) + str.substring(1, idx);
        String lastName = Character.toTitleCase(str.charAt(idx + 1)) + str.substring(idx + 2);
        
        return firstName + " " + lastName;
    }
    
    /**
     * To print a statement related to the total score of the player
     * @param totalScore the score of the player at the end of the game
     * @param name the name of the player
     */
    public static void evaluation (int totalScore, String name) {
        int winningScore = 2000;
        
        if (totalScore >= winningScore)
            System.out.printf("%s, %s, %s %d\n", "Victory", name, "your score is", totalScore);
        else
            System.out.printf("%s, %s, %s %d\n", "Mission failed", name, 
                    "your score is", totalScore);
    }
    
    /**
     * To ask the user to input a direction for the robot to move in
     * @return the direction of the robot to move in
     */
    public static char inputDirection() {
        Scanner console = new Scanner(System.in);
        char direction;
        
        do {
            System.out.print("Please enter a valid direction: ");
            direction = console.next().charAt(0);
        } while (direction != 'u' && direction != 'd' && direction != 'l' && direction != 'r');
        
        return direction;
    }
    
    /**
     * To check if the game is over
     * @param x the position of the robot on the x axis
     * @param y the position of the robot on the y axis
     * @param totalScore the final score of the player
     * @param itrCount the total number of iterations
     * @return true if any of the conditions are met and the game is over,
     * false if none of the conditions are met and the game is not yet over.
     */
    public static boolean isGameOver(int x, int y, int totalScore, int itrCount) {
        int maxItr = 20;
        int losingScore = -1000;
        int winningScore = 2000;
        
        int xEndingPosition = 4;
        int yEndingPosition1 = 0;
        int yEndingPosition2 = 4;
        
        if (itrCount > maxItr)
            return true;
        else if (totalScore < losingScore || totalScore >= winningScore)
            return true;
        else 
            return (x == xEndingPosition && y == yEndingPosition1 ||
                    x == xEndingPosition && y == yEndingPosition2);
    }
}
