/**
 * @author Siddhant Kalash, Ibrahim Oyekan, Jonathan Chismar
 * @project Elevator Test Cases Text File Generator
 * @class CSE 2010, Spring 2015
 * @date 2/26/15
 */
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
 
public class TestCase {
    
    public static void main(String[] args){
 
        Random rand = new Random();
        
        for(int i = 5; i <= 15; i++){
            
            for (int j = 1; j <= 3; j++){
                        
                try { 
                
                    File file = new File("/users/Ibrahim/documents/" + i + "_Floors_Day_" + j + ".txt");
                    file.createNewFile();
                    FileWriter fw = new FileWriter(file.getAbsoluteFile());
                    BufferedWriter bw = new BufferedWriter(fw);      
                    bw.write(String.valueOf(i));
                    bw.write ( "\r\n");
                        
                    for (int k = 2; k < 52; k++){
                        bw.write(String.valueOf(rand.nextInt(k/2) + 1)); //time
                        bw.write(" " + String.valueOf(rand.nextInt(i))); //floor at
                        bw.write(" " + String.valueOf(rand.nextInt(i))); //floor-going to   
                        bw.write ("\r\n");
                    }
        
                    bw.close();     
                    System.out.println("Done Creating Test Case Text File: " + i + "_Floors_Day_" + j + ".txt");
 
                }catch (IOException e){             
                    e.printStackTrace();
                }
            }
        }
    }
}
 