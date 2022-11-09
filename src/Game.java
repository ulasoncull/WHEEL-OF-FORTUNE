import java.io.*;
import java.lang.Thread;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Iterator;
import java.util.Locale;
import java.util.Random;
public class Game {
	static Stack Countries = new Stack(34);
	static Stack Letters = new Stack(26);
	static Stack temp = new Stack(26);
	static Stack tmpStack = new Stack(34);
	static String Country = null;
	static Stack Names = new Stack(11);
	static Stack Scores = new Stack(11);
	static CircularQueue wheel = new CircularQueue(8);
	static int step = 0;
	static int score = 0;
	static char Letter = 0;
	static Object wheels = null;
	static int matchedNumber = 0;
	public static void main(String[] args) {	
		
		wheel.enqueue(10); wheel.enqueue(50); wheel.enqueue(100); wheel.enqueue(250); wheel.enqueue(500);
		wheel.enqueue(1000); wheel.enqueue("Double Money"); wheel.enqueue("Bankrupt");
		wheelSpin(wheel);
		ReadCountries();
		System.out.println("Welcome to Wheel of Fortune!\n");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		StartOfTheGame();			
	}
	public static void wheelSpin(CircularQueue wheel) {//spins the wheel
		Random number = new Random();
		int x = 1 + number.nextInt(7);
		for(int i = 0; i <= x; i++) {
			Object temp = (Object)wheel.peek();
			wheel.dequeue();
			wheel.enqueue(temp);
		}
		wheels = wheel.peek();//returns the peek
	}
	public static void CreateFirstQueue(int length,char Letter) {//Creates the queue with the country selected.
		CircularQueue RealCountry = new CircularQueue(length);
		for(int i = 0; i<= length-1; i++) {
			RealCountry.enqueue(Country.charAt(i));
		}
		CreateSecondQueue(RealCountry, Country.length(),Letter);
		
	}
	public static void CreateSecondQueue(CircularQueue Real, int length,char Letter) {//Creates the queue in which the player will play.
		CircularQueue TempCountry = new CircularQueue(length);
		for(int i = 0; i<= length-1; i++) {
			TempCountry.enqueue('-');
		}
		PlayingAlgorithm(Country.length() , Real, TempCountry);
		
	}
	public static void ChooseALetter() {//The letter is guessed.
		int size = Letters.size();
		int letter_number =0;
		if(size==1)//to avoid the bug
			letter_number = 1;
		else
			letter_number = ThreadLocalRandom.current().nextInt(1, Letters.size()+1);

		Random number = new Random();
		Object tempp = null;		
		for(int i = 0; i<letter_number; i++) {
			if(i==letter_number-1) {
				Object Lettersss = Letters.pop().toString();
				Letter = Lettersss.toString().charAt(0);
			}
				
			else
				temp.push(Letters.pop());
		}				
		while(!temp.isEmpty()) {
			Letters.push(temp.pop());
		}
		if(step==0)
			CreateFirstQueue(Country.length(),Letter);
		
	}
	public static void print(CircularQueue TempCountry,int step,int score) {//Prints the values ​​that should be displayed on the game screen.
		System.out.print("Word: ");
		for(int i = 0; i < Country.length(); i++) {
			char temp = (Character) TempCountry.peek();
			System.out.print(temp+" ");
			TempCountry.dequeue();
			TempCountry.enqueue(temp);
			
			
		}
		System.out.print("       Step : "+step+"  Score : "+score+"      ");
		while(!Letters.isEmpty()) {
			System.out.print(Letters.peek());
			temp.push(Letters.pop());
		}
		while(!temp.isEmpty()) {//loads them back to main stack
			Letters.push(temp.pop());
		}
		
		System.out.println();
		System.out.println("Wheel : "+ wheels );
		System.out.print("Guess : "+Letter);
		System.out.println();
	}
	public static CircularQueue PlayingAlgorithm(int lengthOfCountry,CircularQueue RealCountry,CircularQueue TempCountry) {
		int size = RealCountry.size();
		int how_many_letters = 0;
		
		while(how_many_letters != Country.length()) {
			matchedNumber = 0;
			for(int i = 0; i < size; i++) {//checks every letter
				char temp = (Character) RealCountry.peek();
				char temp2 = (Character) TempCountry.peek();
				if((Character)RealCountry.peek()==Letter) {
					RealCountry.dequeue();
					RealCountry.enqueue(temp);
					
					TempCountry.dequeue();//replaces the new letters
					TempCountry.enqueue(Letter);					
					how_many_letters++;
					matchedNumber++;//keeping this for the score calculations.
				}
				else {
					RealCountry.dequeue();
					RealCountry.enqueue(temp);
					
					TempCountry.dequeue();//keeps the old matched letters
					TempCountry.enqueue(temp2);
				}
				
				
			}			
			step++;
			wheelSpin( wheel);
			Score();
			print(TempCountry,step,score);
			if(how_many_letters!= Country.length())
				ChooseALetter();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return RealCountry;

	}
	public static void Score() {//Calculates The score.
		
		if(matchedNumber!=0) {
			if(wheels=="Double Money")
				score = score*2;
			else if(wheels!="Bankrupt")
				score = score+ ((Integer)wheels*matchedNumber);
		}
		if(wheels=="Bankrupt")
			score = 0;

	}
	public static void StartOfTheGame() {
		Random number = new Random();
		int x = 1 + number.nextInt(33);
		System.out.println("Randomly generated number: "+ x);
		ChooseCountry(x);
			
	}
	public static void ChooseCountry(int number) {//Chooses The country.	
		for (int i = number; i>0; i--) {
			tmpStack.pop();
			if(i ==1) {
				Country = tmpStack.pop().toString().toUpperCase(Locale.ENGLISH);
				int lenghtOfCountry = Country.length();
			}
		}
		ReadLetters();
			
	}
	public static void ReadCountries() {
		String dataWhichReaded = null;
        File bufferDosya = new File("countries.txt");        
        FileReader reader;
        BufferedReader bufferedReader;        
        try{
            reader = new FileReader(bufferDosya);
            bufferedReader = new BufferedReader(reader);
            dataWhichReaded = bufferedReader.readLine();
            while(dataWhichReaded != null){
                
                Countries.push((Object)dataWhichReaded);
                dataWhichReaded = bufferedReader.readLine();
            }
            bufferedReader.close();
           
            SortingStack();
        }
        catch (IOException e){
        	
        }
    }
	public static void ReadHighScore() {
		String dataWhichReaded = null;
        File bufferDosya = new File("highscore.txt");        
        FileReader reader;
        BufferedReader bufferedReader;        
        try{
            reader = new FileReader(bufferDosya);
            bufferedReader = new BufferedReader(reader);
            dataWhichReaded = bufferedReader.readLine();
            while(dataWhichReaded != null){
                dataWhichReaded.split(" ");
                Names.push((Object)dataWhichReaded);
                dataWhichReaded = bufferedReader.readLine();
            }
            bufferedReader.close();
           
            SortingStack();
        }
        catch (IOException e){
        	
        }
    }
	public static void ReadLetters() {
		String dataWhichReaded = null;
        File bufferDosya = new File("Letters.txt");
   
        FileReader reader;
        BufferedReader bufferedReader;        
        try{
            reader = new FileReader(bufferDosya);
            bufferedReader = new BufferedReader(reader);
            dataWhichReaded = bufferedReader.readLine();
            while(dataWhichReaded != null){
                
                Letters.push((Object)dataWhichReaded);
                dataWhichReaded = bufferedReader.readLine();
            }
            bufferedReader.close();

        }
        catch (IOException e){
        	
        }
        ChooseALetter();
    }
	public static void SortingStack() {
		
        while(!Countries.isEmpty())
        {
            // pop out the first element
            String tmp = (String)Countries.pop();                     
            // while temporary stack is not empty and
            // top of stack is greater than temp
            while(!tmpStack.isEmpty() && compareToStrings((String)tmpStack.peek(),tmp))
            {
                // pop from temporary stack and
                // push it to the input stack
            Countries.push(tmpStack.pop());
            }
             
            // push temp in temporary of stack
            tmpStack.push(tmp);
        }
        
        
	}
	
	static boolean compareToStrings(String first,String second) {
		int comparedResult = first.compareTo(second);

        if (comparedResult > 0) {
           return true;
       
        }
        else
        	return false;
	
     }
	
	
}
