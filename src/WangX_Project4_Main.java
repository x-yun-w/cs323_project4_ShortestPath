import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

class treeNode{
	protected int key1;
	protected int key2;
	protected int rank;  //-1 means no father; 5 means not yet know; 1 means the 1st child of its father, 2 means 2nd child; etc.
	protected treeNode child1;
	protected treeNode child2;
	protected treeNode child3;
	protected treeNode father;
	
	public treeNode(int k1, int k2, int r, treeNode c1, treeNode c2, treeNode c3, treeNode f) {
		key1 = k1;
		key2 = k2;
		rank = r;
		child1= c1;
		child2 = c2;
		child3 = c3;
		father = f;
	}
	
	public static void printNode(treeNode n, FileWriter out) throws IOException {
		if(n.child1 == null) {
			out.write("(" + n.key1 + ", " + n.key2 + ", " + n.rank + ", null, null, null, ");
		}
		 if(n.child1 != null && n.child3 == null) {
			out.write("(" + n.key1 + ", " + n.key2 + ", " + n.rank + ", " +n.child1.key1 + ", " + n.child2.key2 + ", null, " );			
		}
		if(n.child1 != null && n.child3 != null) {
			out.write("(" + n.key1 + ", " + n.key2 + ", " + n.rank + ", " +n.child1.key1 + ", " + n.child2.key2 + ", " + n.child3.key1 + ", " );	
		}
		
		if(n.father != null)
			out.write( n.father.key1 + ") \n");
		else
			out.write( "null) \n");
	}
	
}

public class WangX_Project4_Main{
	protected static treeNode root;
	
	public static treeNode initialTree(File in, FileWriter debug) throws IOException {
		debug.write("Entering initialTree () method\n");
		root = new treeNode(-1, -1, -1, null, null, null, null);
		Scanner s = new Scanner(in);
		int data1 = s.nextInt();
		int data2 = s.nextInt();
		debug.write("Before swap data1 and data2 are: " + data1 + ", " + data2 + "\n");
		if(data2 < data1) {
			int t = data1;
			data1 = data2;
			data2 = t;
		}
		
		debug.write("After swap data1 and data2 are: " + data1 + ", " + data2 + "\n");
		
		treeNode n1 = new treeNode(data1, -1, 1, null, null, null, root);
		treeNode n2 = new treeNode(data2, -1, 2, null, null, null, root);
		
		root.child1 = n1;
		root.child2 = n2;
		root.key1 = data2;
		debug.write("Printing the Root Node\n ");
		root.printNode(root, debug);
		debug.write("Exiting initialTree () method\n");
		return root;
	}
	
	public static void build23Tree(File in, treeNode r, FileWriter debug) throws IOException {
		debug.write("Entering build23Tree () method\n");
		Scanner s = new Scanner(in);
		int data =0;
		treeNode spot;
		while(s.hasNextInt()) {
			data = s.nextInt();
			spot = findSpot(root, data, debug);
			while(spot == null && s.hasNextInt()) {
				data = s.nextInt();
				spot = findSpot(root, data, debug);
			}
			if(spot != null ) {
			debug.write("in build23Tree (), printing Spot info: \n");
			spot.printNode (spot, debug);
			treeNode leafNode = new treeNode(data, -1, 5, null, null, null, null);
			treeInsert(spot, leafNode, debug);
			}
		}
		debug.write("In build23Tree; printing preOrder () after one treeInsert: \n");
		
		preOrder (root, debug);
	}
	
	public static treeNode findSpot(treeNode spot, int data, FileWriter debug) throws IOException {
		debug.write("Entering findSpot () method \n");
		debug.write("Spot's key1 and key2 and data are: " + spot.key1 + ", " + spot.key2 + ", " + data + "\n");
		
		if(spot.child1 == null) {
			debug.write("In findSpot () You are at leaf level, you are too far down the tree!!\n");
			return null;
		}
		if(data == spot.key1 || data == spot.key2) {
			debug.write("In findSpot (): data is already in Spot’s keys, no need to search further!\n");
			return null;
		}
		if( isLeaf(spot.child1)) {
			if(data == spot.key1 || data == spot.key2) {
				debug.write("In findSpot (): data is already in a leaf node.\n");
				return null;
			}
			else
				return spot;
		}
		else
			if(data < spot.key1)
				return findSpot(spot.child1, data, debug);
			else if(spot.key2 == -1 || data < spot.key2)
				return findSpot(spot.child2, data, debug);
			else if(spot.key2 != -1 && data >= spot.key2)
				return findSpot(spot.child3, data, debug);
			else {
				debug.write("In findSpot (), something is wrong about data.\n");
				return null;
			}
	}
	
	public static void treeInsert(treeNode spot, treeNode n, FileWriter debug) throws IOException {
		debug.write("Entering treeInsert () method.\n");
		int count;
		if(spot == null) {
			debug.write("In treeInsert (), Spot is null, something is wrong.\n");
		}
		else {
			debug.write("In treeInsert (). Printing Spot and newNode info.\n");
			spot.printNode(spot, debug);
			spot.printNode(n, debug);
		}
		
		if(spot.key2 == -1)
			count = 2;
		else
			count = 3;
		
		debug.write("In treeInsert () method; Spot kids count is: " + count + "\n");
		
		if (count == 2)
			spotHas2kidsCase(spot, n, debug);
		else if(count == 3)
			spotHas3kidsCase(spot, n, debug);
		
		debug.write("Leaving treeInsert () method\n");
	}
	
	public static void spotHas2kidsCase(treeNode spot, treeNode n, FileWriter debug) throws IOException {
		debug.write("Entering spotHas2kidCase () method\n");
		debug.write("In spotHas2kidCase () method; Spot’s rank is: " + spot.rank +"\n");
		
		if(n.key1 < spot.child2.key1) {
			spot.child3 = spot.child2;
			spot.child2 = n;
		}
		else
				spot.child3 = n;
		
		if(spot.child2.key1 < spot.child1.key1) {
			treeNode tmp = spot.child1;
			spot.child1 = spot.child2;
			spot.child2 = tmp;
		}
		
		spot.child1.father = spot;
		spot.child1.rank = 1;
		spot.child2.father = spot;
		spot.child2.rank = 2;
		spot.child3.father = spot;
		spot.child3.rank = 3;
		
		updateKeys(spot, debug);
		
		if(spot.rank > 1)
			updateKeys(spot.father, debug);
		
		debug.write("Leaving spotHas2kidCase () method\n");
	}
	
	public static void spotHas3kidsCase(treeNode spot, treeNode n, FileWriter debug) throws IOException {
		debug.write("Entering spotHas3kidCase () method\n");
		debug.write("In spotHas3kidCase () method; Spot’s rank is: " + spot.rank +"\n");
		
		treeNode sibling = new treeNode(-1, -1, 5, null, null, null, null);
		
		if(n.key1 > spot.child3.key1) {
			sibling.child2 = n;
			sibling.child1 = spot.child3;
			spot.child3 = null;
		}
		else if(n.key1 < spot.child3.key1) {
			sibling.child2 = spot.child3;
			spot.child3 = n;
		}
		
		if(spot.child3 != null) {
			if(spot.child3.key1 > spot.child2.key1) {
				sibling.child1 = spot.child3;
				spot.child3 = null;
			}
			else {
				sibling.child1 = spot.child2;
				spot.child2 = n;
			}
		}
		else if (spot.child2.key1 < spot.child1.key1) {
			treeNode tmp  = spot.child1;
			spot.child1 = spot.child2;
			spot.child2 = tmp;
		}
		
		spot.child1.father = spot;
		spot.child1.rank = 1;
		spot.child2.father = spot;
		spot.child2.rank = 2;
		spot.child3= null;
		
		sibling.child1.father = sibling;
		sibling.child1.rank = 1;
		sibling.child2.father = sibling;
		sibling.child2.rank = 2;
		sibling.child3 = null;
		
		updateKeys (spot, debug);
		updateKeys (sibling, debug);
		
		if(spot.rank == -1 && spot.father == null)
			root = makeNewRoot(spot, sibling, debug);
		else
			treeInsert(spot.father, sibling, debug);
		
		if(spot.rank > 1)
			updateKeys (spot.father, debug);
		
		debug.write("Leaving spotHas3kidCase () method\n");
	}
	
	public static treeNode makeNewRoot(treeNode spot, treeNode sibling, FileWriter debug) throws IOException {
		debug.write("Entering makeNewRoot () method\n");
		treeNode nr = new treeNode(-1, -1, -1, null, null, null, null);
		nr.child1 = spot;
		nr.child2 = sibling;
		nr.child3 = null;
		nr.key1 = findMinLeaf(sibling);
		nr.key2 = -1;
		spot.father = nr;
		spot.rank = 1;
		sibling.father = nr;
		sibling.rank = 2;
		
		debug.write("Leaving makeNewRoot () method\n");
		return nr;
	}
	
	public static int findMinLeaf(treeNode t) {
		if(t == null)
			return -1;
		
		if(t.child1 == null)
			return t.key1;
		else
			return findMinLeaf(t.child1);
	}
	
	public static void updateKeys(treeNode t, FileWriter debug) throws IOException {
		debug.write("Entering updateKeys () method\n");
		
		if(t == null) {
			
		}
		else {
			t.key1 = findMinLeaf(t.child2);
			t.key2 = findMinLeaf(t.child3);
			
			if(t.rank >1)
			updateKeys(t.father, debug);
		}
		
		debug.write("Leaving updateKeys () method\n");
	}
	
	public static void preOrder(treeNode root, FileWriter treeFile) throws IOException {
	    if (root == null)
	        return;
	    
	    if(isLeaf(root))
	        treeNode.printNode(root, treeFile);
	    else {
	        treeNode.printNode(root, treeFile);
	        preOrder(root.child1, treeFile); 
	        preOrder(root.child2, treeFile); 
	        preOrder(root.child3, treeFile); 
	    }
	}

	
	public static boolean isLeaf(treeNode t) {
		if (t == null) {
	        return false;
	    }
	    return t.child1 == null && t.child2 == null && t.child3 == null;
	}
	
	public int countKids(treeNode t) {
		if(t.child3 == null) 
			return 2;
		else
			return 3;
	}
	
	public static void swap(int d1, int d2) {
		int tmp = d1;
		d1 = d2;
		d2 = tmp;
	}	
	
	public static void main(String[] args) throws IOException {
		File inFile = new File(args[0]);
		File treeFile = new File(args[1]);
		File debugFile = new File(args[2]);
		FileWriter treeF = new FileWriter(treeFile);
		FileWriter debug = new FileWriter(debugFile);
		
		initialTree(inFile, debug);
		build23Tree(inFile, root, debug);
		preOrder(root, treeF);
		
		treeF.close();
		debug.close();
	}
}
