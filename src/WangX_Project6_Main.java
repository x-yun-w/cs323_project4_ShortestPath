import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class WangX_Project6_Main {
	protected static int numNodes;
	protected static int sourceNode;
	protected static int minNode;
	protected static int currentNode;
	protected static int newCost;
	protected static int[][] costMatrix;
	protected static int[] fatherAry;
	protected static int[] todoAry;
	protected static int[] bestAry;
	
	public WangX_Project6_Main() {
		
	}
	
	public static void loadCostMatrix(File in) throws FileNotFoundException {
		Scanner s = new Scanner(in);
		numNodes = s.nextInt()+1;
		costMatrix = new int[numNodes][numNodes];
		
		for(int i = 0; i<numNodes; i++) {
			for(int j = 0; j < numNodes; j++) {
				if(i == j)
					costMatrix[i][j] = 0;
				else
					costMatrix[i][j] = 9999;
			}
		}
		int r = 0;
		int c = 0; 
		int cost = 0;
		while(s.hasNextLine()) {
			r= s.nextInt();
			c = s.nextInt();
			cost = s.nextInt();
			costMatrix[r][c] = cost;
		}
	}
	
	public static void setBestAry(int sourceNode) {
		bestAry = new int[numNodes];
		for(int j = 0; j < numNodes; j++) {
			bestAry[j] =  costMatrix[sourceNode][j];
		}
	}
	public static void setFatherAry(int sourceNode) {
		fatherAry = new int[numNodes];
		for(int j = 0; j < numNodes; j++) {
			fatherAry[j] =  sourceNode;
		}
	}
	
	public static void setTodoAry(int sourceNode) {
		todoAry = new int[numNodes];
		for(int j = 0; j < numNodes; j++) {
			if(j != sourceNode)
				todoAry[j] =  1;
			else
				todoAry[j] = 0;
		}
	}
	
	public static int findMinNode() {
		int minCost = 99999;
		minNode = 0;
		int i = 1;
		while(i<numNodes) {
			if(todoAry[i] == 1 && bestAry[1] < minCost) {
				minCost = bestAry[1];
				minNode = i;
			}
			i++;
		}
		return minNode;
	}
	
	public static int computeCost(int min, int n) {
		return bestAry[min] + costMatrix[min][n];
	}
	
	public static boolean checkTodoAry() {
		int c = 0;
		for(int i = 1; i< todoAry.length; i++) {
			if(todoAry[i] == 0)
				c++;
		}
		return c == (todoAry.length-1);
	}
	
	public static void debugPrint(FileWriter debug) throws IOException {
		debug.write("\n\nthe sourceNode is: " + sourceNode +"\n");
		debug.write("the fatherAry is: ");
		for(int j = 1; j < numNodes; j++) {
			debug.write(fatherAry[j]+", ");
		}
		debug.write("\nthe bestCostAry is: ");
		for(int j = 1; j < numNodes; j++) {
			debug.write(bestAry[j]+", ");
		}
		debug.write("\nthe todoAry is: ");
		for(int j = 1; j < numNodes; j++) {
			debug.write(todoAry[j]+", ");
		}
	}
	
	public static void printShortestPath(int current, int source, FileWriter sss) throws IOException {
		sss.write("The shortest path from the currentNode: " + current + " to the sourceNode: " + source + " is: ");
		int i = current;
		while(fatherAry[i] != source) {
			
			sss.write( fatherAry[i] + "-> ");
			
			i = fatherAry[i];
		}
		sss.write(fatherAry[source]+ "\n");
		sss.write("the total cost is: " + bestAry[current] + "\n");
	}
	
	public static void main(String[] args) throws IOException {
		File inFile = new File(args[0]);
		File sssFile = new File(args[1]);
		File debugFile = new File(args[2]);
		FileWriter debug = new FileWriter (debugFile);
		FileWriter sss = new FileWriter (sssFile);
		
		loadCostMatrix(inFile);
		sourceNode = 1;
		while(sourceNode < numNodes) {
			setBestAry (sourceNode);
			setFatherAry (sourceNode);
			setTodoAry (sourceNode);
			
			while(checkTodoAry()!= true) {
				minNode = findMinNode();
				todoAry[minNode] = 0;
				debugPrint(debug);
				int childNode = 1;
				
				while(childNode<numNodes) {
					if(todoAry[childNode] == 1) {
						newCost = computeCost(minNode, childNode);
						if(newCost < bestAry[childNode]) {
							bestAry[childNode] = newCost;
							fatherAry[childNode] = minNode;
							debugPrint(debug);
						}
					}
					childNode++;
				}
			}
			currentNode = 1;
			while(currentNode < numNodes) {
				printShortestPath(currentNode, sourceNode, sss);
				currentNode++;
			}
			sss.write("\n");
			sourceNode++;
		}
		
		debug.close();
		sss.close();
	}
}
