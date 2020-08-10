
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;



public class NearestNeighbor {

/*
Name: Jonah Miller
Course Name: Programming Fundamentals
Semester: Summer 2020
Assignment Name: Programming Assignment 3 (Nearest Neighbor)
*/



	public static void main(String[] args) throws FileNotFoundException{
	
		//Display identifier
		System.out.println("Programming Fundamentals Summer 2020");
		System.out.println("NAME: Jonah Miller");
		System.out.println("PROGRAMMING ASSIGNMENT 3\n");
		
		
		//Asks for the training and testing file names, takes these names in via scanner, creates files using said name,
		//and then creates scanners for those files
		//NOTE: Java will attempt to find the data files within the Java Project folder, so make sure that they're saved in this location
		Scanner userInput = new Scanner(System.in);
		
		System.out.print("Enter the name of the training file: ");
		File trainingData = new File(userInput.nextLine());
		Scanner trainingScanner = new Scanner(trainingData);
		
		System.out.print("Enter the name of the testing file: ");
		File testingData = new File(userInput.nextLine());
		Scanner testingScanner = new Scanner(testingData);
		
		System.out.println();

		//These arrays are used to store data from the corresponding files
		double[][] trainingValues = new double[75][4];
		double[][] testingValues = new double[75][4];
		String[] trainingClasses = new String[75];
		String[] testingClasses = new String[75];
		
		//This is the array which will store the predictions of the algorithm
		String[] predictedClasses = new String[75];
		
		//This runs the method which will copy the measurements data from the csv files to their corresponding 2D double arrays
		trainingValues = stringToDouble(trainingScanner, trainingValues);
		testingValues = stringToDouble(testingScanner, testingValues);
		
		//The scanners need to be reset at this point before they can be used again
		trainingScanner = new Scanner(trainingData);
		testingScanner = new Scanner(testingData);
		
		//This runs the method which will copy the flower classification data from the csv files into their corresponding string arrays
		trainingClasses = pullNames(trainingScanner, trainingClasses);
		testingClasses = pullNames(testingScanner, testingClasses);

		//This loop will go through each "row" of the testing data array and use the method to find the nearest neighbor
		//within the training data array. It will then record the prediction for this row in the predicted classes array.
		for (int i = 0; i < trainingClasses.length; i++) {
			int nearestNeighbor = findNearestNeighbor(testingValues[i][0], testingValues[i][1],
					testingValues[i][2], testingValues[i][3], trainingValues);
	
			predictedClasses[i] = trainingClasses[nearestNeighbor];		
		}
		
		
		//This runs the print results method, which will give the final output of the program
		printResults(testingClasses, predictedClasses);
		
		//Closes the scanners
		userInput.close();
		trainingScanner.close();
		testingScanner.close();
	}
	
	/*This method takes in a scanner set to a CSV file, and 2D double array
	 * The method creates a new 2D string array, and an int which is used for tracking rows within the CSV file
	 * The while loop in this method functions for as long as the CSV file has more rows to be counted.
	 * The loop first takes the row of the CSV file it is currently on, and uses the split method to import the data into a string array without commas
	 * A for loop then goes through each value in the split string array, and uses Double.Parse to narrow it into a Double variable.
	 * These double variables are then assigned to the 2D Double Array, and this array is returned.
	 * The final output of the method is a 2D Double Array which contains all of the double values of the CSV files (the String values are created in a separate array).
	 */
	static double[][] stringToDouble(Scanner values, double[][] array) {
		
		String[][] tempString = new String[array.length][array[1].length];
		int rowNumber = 0;
		
		while (values.hasNextLine()) {
		
			String[] splitString = values.nextLine().split(",");

			
			for (int i = 0; i < splitString.length-1; i++) {
				tempString[rowNumber][i] = splitString[i];
				array[rowNumber][i] = Double.parseDouble(tempString[rowNumber][i]);
			}			
			rowNumber++;			
		}
		return array;
	}
	
	/*
	 * This method uses a while loop to go through each line of a csv file scanner, and use the split method to put each entry into a string array.
	 * It then takes the 5th element from the string array (in this case, the name of the Iris) and assigns it to the array at the current row number.
	 * The input array is then returned with it's new assigned string components.
	 */
	static String[] pullNames(Scanner values, String[] array) {
		
		int rowNumber = 0;

		while (values.hasNextLine()) {
		
			String[] splitString = values.nextLine().split(",");	
			array[rowNumber] = splitString[4];
			rowNumber++;			
		}
		return array;
	}
	
	/*
	 * This method uses the distance formula to compare the total distance in measurements between two data points. 
	 * This is done by calculating the distance between the set measurements and each measurement in the array by using a for loop.
	 * Each of these distances is measured against the current smallest distance, with the smaller of the two being kept.
	 * The smallest distance is then returned, which is used to predict the Iris type of the test variable by finding the Iris type at 
	 * the same location within the training data.
	 */
	static int findNearestNeighbor(double sepalLength, double sepalWidth, double petalLength, double petalWidth, double[][] trainingValues) {
		
		int nearestNeighborRow = 0;
		double leastDistance = Math.sqrt(((sepalLength -trainingValues[0][0]) * (sepalLength -trainingValues[0][0]))+ 
				((sepalWidth-trainingValues[0][1]) * (sepalWidth-trainingValues[0][1])) + 
				((petalLength-trainingValues[0][2]) * (petalLength-trainingValues[0][2])) +
				((petalWidth-trainingValues[0][3])* (petalWidth-trainingValues[0][3])));
		
		for(int i = 0; i < trainingValues.length; i++) {
				
				if ((Math.sqrt(((sepalLength -trainingValues[i][0]) * (sepalLength -trainingValues[i][0])) + 
						((sepalWidth-trainingValues[i][1]) * (sepalWidth-trainingValues[i][1])) + 
						((petalLength-trainingValues[i][2]) * (petalLength-trainingValues[i][2])) + 
						((petalWidth-trainingValues[i][3]) * (petalWidth-trainingValues[i][3])))) < leastDistance) {
					
					leastDistance = (Math.sqrt(((sepalLength -trainingValues[i][0]) * (sepalLength -trainingValues[i][0])) + 
							((sepalWidth-trainingValues[i][1]) * (sepalWidth-trainingValues[i][1])) + 
							((petalLength-trainingValues[i][2]) * (petalLength-trainingValues[i][2])) + 
							((petalWidth-trainingValues[i][3]) * (petalWidth-trainingValues[i][3]))));
					nearestNeighborRow = i;
					
			}
		}
		
		return nearestNeighborRow;
	}
	
	/*
	 * This method uses a for loop to output the true and predicted classification of each Iris within the dataset.
	 * It also uses the String.equals method to determine if a prediction was correct, and if so, it adds it to a tally.
	 * The method then finally outputs the total tally of correct/incorrect results.
	 */
	static void printResults(String[]trueLabels, String[]predictedLabels) {
		
		double correctCounts = 0;
		
		System.out.println("EX#: TRUE LABEL, PREDICTED LABEL");
		
		for (int i = 0; i < trueLabels.length; i++) {
			
			System.out.println((i +1) + ": " + trueLabels[i] + " " + predictedLabels[i]);
			
			if (trueLabels[i].equals(predictedLabels[i])) {
				correctCounts++;
			}
			
		}
		
		System.out.println("ACCURACY: " + (correctCounts/trueLabels.length));
	}
	
	/*
	 * This overloaded method was used for testing purposes, and isn't called in the current main method.
	 * It prints out either a single 2D Double Array, or a 2D Double Array and a 1D String Array.
	 */
	static void print2DArray(double[][] array) {
		for (int i = 0; i < array.length; i++) {
			for (int k = 0; k < array[i].length; k++) {
				
				if (k == array[i].length - 1) {
					System.out.print(array[i][k]);
				}
				
				else {
				System.out.print(array[i][k] + " ");
				}
			}
			System.out.println();
		}
	}
	
	static void print2DArray(double[][] doubleArray, String[] stringArray) {
		for (int i = 0; i < doubleArray.length; i++) {
			for (int k = 0; k < doubleArray[i].length; k++) {
				System.out.print(doubleArray[i][k] + " ");
			}
			System.out.println(stringArray[i]);
		}
	}
}