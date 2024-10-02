
/* Put your student number here
 * C2040032
 * Optionally, if you have any comments regarding your submission, put them here. 
 * For instance, specify here if your program does not generate the proper output or does not do it in the correct manner.
 */

import java.util.*;
import java.beans.IndexedPropertyChangeEvent;
import java.io.*;

// A class that represents a dense vector and allows you to read/write its elements
class DenseVector {
	private int[] elements;

	public DenseVector(int n) {
		elements = new int[n];
	}

	public DenseVector(String filename) {
		File file = new File(filename);
		ArrayList<Integer> values = new ArrayList<Integer>();

		try {
			Scanner sc = new Scanner(file);

			while (sc.hasNextInt()) {
				values.add(sc.nextInt());
			}

			sc.close();

			elements = new int[values.size()];
			for (int i = 0; i < values.size(); ++i) {
				elements[i] = values.get(i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Read an element of the vector
	public int getElement(int idx) {
		return elements[idx];
	}

	// Modify an element of the vector
	public void setElement(int idx, int value) {
		elements[idx] = value;
	}

	// Return the number of elements
	public int size() {
		return (elements == null) ? 0 : (elements.length);
	}

	// Print all the elements
	public void print() {
		if (elements == null) {
			return;
		}

		for (int i = 0; i < elements.length; ++i) {
			System.out.println(elements[i]);
		}
	}
}

// A class that represents a sparse matrix
public class SparseMatrix {
	// Auxiliary function that prints out the command syntax
	public static void printCommandError() {
		System.err.println("ERROR: use one of the following commands");
		System.err.println(" - Read a matrix and print information: java SparseMatrix -i <MatrixFile>");
		System.err.println(" - Read a matrix and print elements: java SparseMatrix -r <MatrixFile>");
		System.err.println(" - Transpose a matrix: java SparseMatrix -t <MatrixFile>");
		System.err.println(" - Add two matrices: java SparseMatrix -a <MatrixFile1> <MatrixFile2>");
	}

	public static void main(String[] args) throws Exception {
		if (args.length < 2) {
			printCommandError();
			System.exit(-1);
		}

		if (args[0].equals("-i")) {
			if (args.length != 2) {
				printCommandError();
				System.exit(-1);
			}

			SparseMatrix mat = new SparseMatrix();
			mat.loadEntries(args[1]);
			System.out.println("Read matrix from " + args[1]);
			System.out.println("The matrix has " + mat.getNumRows() + " rows and " + mat.getNumColumns() + " columns");
			System.out.println("It has " + mat.numNonZeros() + " non-zeros");
		} else if (args[0].equals("-r")) {
			if (args.length != 2) {
				printCommandError();
				System.exit(-1);
			}

			SparseMatrix mat = new SparseMatrix();
			mat.loadEntries(args[1]);
			System.out.println("Read matrix from " + args[1] + ":");
			mat.print();
		} else if (args[0].equals("-t")) {
			if (args.length != 2) {
				printCommandError();
				System.exit(-1);
			}

			SparseMatrix mat = new SparseMatrix();
			mat.loadEntries(args[1]);
			System.out.println("Read matrix from " + args[1]);
			SparseMatrix transpose_mat = mat.transpose();
			System.out.println();
			System.out.println("Matrix elements:");
			mat.print();
			System.out.println();
			System.out.println("Transposed matrix elements:");
			transpose_mat.print();
		} else if (args[0].equals("-a")) {
			if (args.length != 3) {
				printCommandError();
				System.exit(-1);
			}

			SparseMatrix mat1 = new SparseMatrix();
			mat1.loadEntries(args[1]);
			System.out.println("Read matrix 1 from " + args[1]);
			System.out.println("Matrix elements:");
			mat1.print();

			System.out.println();
			SparseMatrix mat2 = new SparseMatrix();
			mat2.loadEntries(args[2]);
			System.out.println("Read matrix 2 from " + args[2]);
			System.out.println("Matrix elements:");
			mat2.print();
			
			SparseMatrix transpose_mat1 = mat1.transpose();
			SparseMatrix transpose_mat2 = mat2.transpose();
			
			SparseMatrix mat_sum1 = mat1.add(mat2);
			SparseMatrix trans_mat_sum1 = transpose_mat1.add(transpose_mat2);

			System.out.println();

			System.out.println("Matrix1 + Matrix2 =");
			mat_sum1.print();
			System.out.println();

			System.out.println("transpose(Matrix1) + transpoe(Matrix2) =");
			trans_mat_sum1.print();
			System.out.println();
		}
	}

	// Loading matrix entries from a text file
	// You need to complete this implementation
	public void loadEntries(String filename) {
		File file = new File(filename);

		try {
			Scanner sc = new Scanner(file);
			numRows = sc.nextInt();
			numCols = sc.nextInt();
			entries = new ArrayList<Entry>();

			while (sc.hasNextInt()) {
				// Read the row index, column index, and value of an element
				int row = sc.nextInt();
				int col = sc.nextInt();
				int val = sc.nextInt();

				// Add your code here to add the element into data member entries
				int pos = row * numCols + col;
				Entry newEntry = new Entry(pos, val);
				entries.add(newEntry);
				
			}

			// Add your code here for sorting non-zero elements
			// I implemented a recursive top down merge sort algorithm
			// i used https://www.geeksforgeeks.org/merge-sort/ as a resource to better understand the merge sort algorithm as I was not familiar with the algorithm before
			// this algorithm is fast with a time complexity of O(NlogN) in worst case performance
			entries = mergeSort(entries);

		} catch (Exception e) {
			e.printStackTrace();
			numRows = 0;
			numCols = 0;
			entries = null;
		}
	}
	
	public static ArrayList<Entry> mergeSort(ArrayList<Entry> original) {

		ArrayList<Entry> sorted = new ArrayList<Entry>();

		//if the entries list only has 1 item there is no need to sort so the arraylist is returned
		// this is also the pre-loop-check to work back up the recursion queue as there are no further division to be made to the list
		if (original.size() == 1) {
			sorted = original;
			return sorted;
		}

		int mid = original.size() / 2;

		//splits the array list into left and rigth sub lists
		ArrayList<Entry> leftSide = new ArrayList<Entry>(original.subList(0, mid));
		ArrayList<Entry> rightSide = new ArrayList<Entry>(original.subList(mid, original.size()));

		// the lines below are the recursive section of the mergesort algorithm
		leftSide = mergeSort(leftSide);
		rightSide = mergeSort(rightSide);

		//
		sorted = merge(leftSide, rightSide);


		return sorted;
	}

	public static ArrayList<Entry> merge(ArrayList<Entry> leftSide, ArrayList<Entry> rightSide) {

		int leftIndex = 0;
		int rightIndex = 0;

		ArrayList<Entry> sorted = new ArrayList<Entry>();

		Entry leftItem = leftSide.get(leftIndex);
		Entry rightItem = rightSide.get(rightIndex);

		while(leftIndex < leftSide.size() && rightIndex < rightSide.size()) {

			leftItem = leftSide.get(leftIndex);
			rightItem = rightSide.get(rightIndex);
			
			if (leftItem.getPosition() < rightItem.getPosition()){
				sorted.add(leftItem);
				leftIndex++;
			}
			else {
				sorted.add(rightItem);
				rightIndex++;
			}
		}

		while (leftIndex < leftSide.size()){
			sorted.add(leftSide.get(leftIndex));
			leftIndex++;
		}
		while (rightIndex < rightSide.size()){
			sorted.add(rightSide.get(rightIndex));
			rightIndex++;
		}

		return sorted;

	}
	
	// Default constructor
	public SparseMatrix() {
		numRows = 0;
		numCols = 0;
		entries = null;
	}

	// A class representing a pair of column index and elements
	private class Entry {
		private int position; // Position within row-major full array representation
		private int value; // Element value

		// Constructor using the column index and the element value
		public Entry(int pos, int val) {
			this.position = pos;
			this.value = val;
		}

		// Copy constructor
		public Entry(Entry entry) {
			this(entry.position, entry.value);
		}

		// Read column index
		int getPosition() {
			return position;
		}

		// Set column index
		void setPosition(int pos) {
			this.position = pos;
		}

		// Read element value
		int getValue() {
			return value;
		}

		// Set element value
		void setValue(int val) {
			this.value = val;
		}
	}

	// Adding two matrices
	public SparseMatrix add(SparseMatrix M) {
		// Add your code here

		//Intilialsing the new matrix where the result will be stored
		SparseMatrix summedMatrix = new SparseMatrix();
		//Generating the variables each sparse matrix requires
		summedMatrix.entries = new ArrayList<Entry>();
		summedMatrix.numCols = numCols;
		summedMatrix.numRows = numRows;

		//relabelling the entries arrays and creating labelled index variables to help with readability of the code
		ArrayList<Entry> entriesA = entries;
		ArrayList<Entry> entriesB = M.entries;
		int indexA = 0;
		int indexB = 0;
		
		//iterates through both arrays of entries until they have both been fully passed
		while (indexA < entriesA.size() && indexB < entriesB.size()) {
			// creating new entry variables for both A and B to reduce code bloat
			Entry currentA = entriesA.get(indexA);
			Entry currentB = entriesB.get(indexB);

			//
			if (currentA.getPosition() < currentB.getPosition()){
				summedMatrix.entries.add(currentA);
				indexA++;
			
			//
			} else if (currentA.getPosition() > currentB.getPosition()){
				summedMatrix.entries.add(currentB);
				indexB++;
			
			//
			} else {

				int newVal = currentA.getValue() + currentB.getValue();

				if (newVal != 0) {
					int newPos = currentA.getPosition();

					Entry newEntry = new Entry(newPos, newVal);
					summedMatrix.entries.add(newEntry);
				}
				indexA++;
				indexB++;
			}


		}

		return summedMatrix;
	}

	// Transposing a matrix
	public SparseMatrix transpose() {
		// Add your code here
		
		SparseMatrix transposed = new SparseMatrix();
		transposed.entries = new ArrayList<Entry>();

		transposed.numRows = numCols;
		transposed.numCols = numRows;

		for (Entry items : entries) {
			int itemPos = items.getPosition();
			int itemVal = items.getValue();

			int transposedCol = itemPos % transposed.numRows;
			int transposedRow = (itemPos - transposedCol) / transposed.numRows;

			int transposedPos = (transposedCol * transposed.numCols) + transposedRow;

			Entry transposedEntry = new Entry(transposedPos, itemVal);
			transposed.entries.add(transposedEntry);
		}

		//!!!
		//!!!
		//The line below was copied from a source online as i was getting the wrong result and did not undersyand why, i am not claiming it as my own code
		transposed.entries.sort(Comparator.comparing(Entry::getPosition));

		return transposed;
	}

	// Return the number of non-zeros
	public int numNonZeros() {
		// Add your code here
		return entries.size();
	}

	// Number of rows of the matrix
	public int getNumRows() {
		return this.numRows;
	}

	// Number of columns of the matrix
	public int getNumColumns() {
		return this.numCols;
	}

	// Output the elements of the matrix, including the zeros
	// Do not modify this method
	public void print() {
		int n_elem = numRows * numCols;
		int pos = 0;

		for (int i = 0; i < entries.size(); ++i) {
			int nonzero_pos = entries.get(i).getPosition();

			while (pos <= nonzero_pos) {
				if (pos < nonzero_pos) {
					System.out.print("0 ");
				} else {
					System.out.print(entries.get(i).getValue());
					System.out.print(" ");
				}

				if ((pos + 1) % this.numCols == 0) {
					System.out.println();
				}

				pos++;
			}
		}

		while (pos < n_elem) {
			System.out.print("0 ");
			if ((pos + 1) % this.numCols == 0) {
				System.out.println();
			}

			pos++;
		}
	}

	private int numRows; // Number of rows
	private int numCols; // Number of columns
	private ArrayList<Entry> entries; // Non-zero elements
}
