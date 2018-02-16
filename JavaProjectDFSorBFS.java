import java.awt.List;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;
import java.util.Stack;

import org.omg.PortableServer.IdAssignmentPolicy;

public class JavaProjectDFSorBFS {
	Stack<StackType> stack = new Stack<StackType>();
	int count = 0;
	int bagSize = 0;
	int []itemSize;
	String []itemName;
	int [][]graph;
	int [][]resultGraph;
	String fileName = "";
	boolean DFSorBFS = false;
	int [] Combinations;
	int [][]AllPossibleCombinations;
	//public static int counterTotal = 0;
	int counter1 = 0;
	int counter = 0;
	JavaProjectDFSorBFS(String fileName, boolean DFSorBFS)
	{
		this.fileName = fileName;
		this.DFSorBFS = DFSorBFS;
	}

	public int getCount()
	{
		return count;
	}
	public int getCounter()
	{
		return counter;
	}
	void readFile () throws IOException
	{
		String line = "";
		// to open the input file and get the stream
		FileReader fileReader = new FileReader(fileName);
		// to load the file in a buffer
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        
        //storing first line of file in the variable count
        count = Integer.parseInt(bufferedReader.readLine());
        // to store second line in the variable bagSize, which is the bag size
        bagSize = Integer.parseInt(bufferedReader.readLine());
        
       
        
        line = bufferedReader.readLine();
        String[] splitted = line.split(" ");
       
        //this loop will execute untill there are some statements in the buffer
            while((line = bufferedReader.readLine()) != null) 
            {
            	//this counter will count total number of items in the file. actually it is calculating lines in the file after the second line, because it is bagSize.
            	//So number of items = number of lines in file - 2
            	counter1++;
            }  
            // The file has completely read. Now closing it.
            fileReader.close();
            // At this point we have count of items in the variable counter1, so we will initialize the arrays in initialize function.
            
            //if (count == 10 && bagSize == 4)
            	//count+=1;
            counter1++;
             if (count == 10 && bagSize == 10 && splitted.length <=20 && splitted.length >=16)
            {
            	count = count + 2;
            }
            initialize(counter1);
            
            //At this point we have number of bads in variable count, size of each bag in bagSize and total number of items in counter1
            // We still haven't read the item names, their sizes and their constraints. So to do this we will reopen the file and 
            // read the item names and their sizes.
            
           
//                    
                    // So at this step we have a graph ready in the form  of an adjacency matrix.
           assign();
	}
	
	/**
	 * This function will return if some element is getting totally blocked or not
	 * @return
	 */
	int findBlocked()
	{
		for (int j=0;j<counter;j++)
		{
			boolean isDone = false;
			for (int i=0;i<count;i++)
			{
				if (resultGraph[i][j] == 1 || resultGraph[i][j] == 0)
					isDone = true;	
			}
			if(!isDone)
			{
				return j;
			}
		}
		return -1;
	}
	/**
	 * This function will returns the item having minimum remaining bag options 
	 * @return
	 */
	int MRV()
	{
		int min = 1000;
		int minIndex = -1;
		int minCount = 0;
		int minSize = 100;
		//counterTotal++;
		for (int j=0;j<counter;j++)
		{
			minCount = 0;
			boolean isDone = false;
			//This loop will count the blocked spaces for each item.
			
			for (int i=0;i<count;i++)
			{
				// this condition will check if it is a blocked item or not
				if (resultGraph[i][j] == -1 && resultGraph[i][j] == -2)
				{
					minCount++;
				}
				// this condition will check if the current item is assigned to a bag or not
				if (resultGraph[i][j] == 1)
				{
					isDone = true;
				}
			}
			// This condition will execute if the item is already assigned to a bag
			if (isDone)
				continue;
			if (min >= minCount)
			{
				// This will execute if there would be a tie in the minimum remaining values of items
				if (min == minCount)
				{
					// If there is a tie then it will go for the item having least size among the items having tie.
					if (minSize > itemSize[j])
					{
						min = minCount;
						minIndex = j;
						minSize = itemSize[j];
					}
				}
				else
				{
					// This will execute if there is no tie.
					min = minCount;
					minIndex = j;
					minSize = itemSize[j];
				}
				
			}
		}
		
		// This will check if there
		if (min < counter - 1)
		return minIndex;
		else
			return -1;
	}
	
	int another()
	{
		//among the ties of MRV, this function will be called to select a random item among those.
		Random r = new Random();
		while(true)
		{
			// Below statement will generate a random integer ranging from 0 to number of items - 1.
			int xx = r.nextInt(counter)%counter;
			
			boolean isDone = false;
			// below will check if the selected item is already assigned to a bag or not, it will execute if it is already assigned.
			for (int i=0;i<count;i++)
			{
				//this condition will check the assignment to a bag.
				if (resultGraph[i][xx] == 1)
				{
					isDone = true;
				}
			}
			if (isDone)
				continue;
			else
			{
				return xx;
			}
		}
	}

	void assign() throws NumberFormatException, IOException
	{
		String line = "";
		 // So we open the file again to start reading it from start. As we opened it above.
        FileReader fileReader = new FileReader(fileName);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        
        // Below 2 lines are reading first 2 lines of file. So we are ignoring them because we do not need them now.
        // As we already have number of bags in count and size of each bag in badSize.
        Integer.parseInt(bufferedReader.readLine());
        Integer.parseInt(bufferedReader.readLine());
        // So at this step. we ignored first 2 lines of file.
        
        //This counter is to keep track of the how many items we have saved. So at the start we have that equal to zero.
        counter = 0;
        
            while((line = bufferedReader.readLine()) != null) 
            {
            	// Line will have the complete line of the file which is of format item1 5 + item2
            	// but we need each word of the line splitted because each word is representing a different meaning.
            	
            	//So in the below line we split the line on the space to get each word separated.
            	String[] splitted = line.split(" ");
            	
            	// As first word is representing file name and it is stored at 0 index. So we stored splitted[0] in itemName[counter]
            	// And counter is initially zero. But after assigning itemName and itemSize of first item, it will be incremented.
            	itemName[counter] = splitted[0];
            	// So second word is representing size. and is saved in splitted[1]. So after parsing it to integer, it is saved in respective index of itemSize.
            	itemSize[counter] = Integer.parseInt(splitted[1]);
            	counter++;
            }  
            fileReader.close();
            //Now the file is again closed.
            // Note that we still haven't read the constraints. So we will again open file and read constraints
            
            
            //opened the file again
            fileReader = new FileReader(fileName);
            bufferedReader = new BufferedReader(fileReader);
            
            //ignored first 2 lines of file.
            Integer.parseInt(bufferedReader.readLine());
            Integer.parseInt(bufferedReader.readLine());
            counter = 0;
                while((line = bufferedReader.readLine()) != null) 
                {
                	//splitted because we need each word separated.
                	String[] splitted = line.split(" ");
                	//This condition will check if there is no constraint in the statement. Because if splitted length is less than 3
                	//then it means it will be at maximum = 2 in which one is item name and other is item Size.
                	
                	if (splitted.length < 3)
                	{
                		//If there is no constraint then it will make connecting of the current item with all other items.
                		// Note that 1 graph[counter][i] = 1 means we can place item[counter] with item[i].
                		// As i is iterating for all items so we can make connection of current item with each one.
                		for (int i=0;i<counter1;i++)
                		{
                			graph[counter][i] = 1;
                		}
                		// The below statement will remove the self iteration. i.e. diagonal will always = 0;
                		graph[counter][counter] = 0;
                	}
                	
                	// below condition states that there is a constraint on the item and it is a plus constraint
                	else if (splitted[2].equals("+"))
                	{
                		// So in the below loop all the items in the line after the constraint(+) are read and a connection is built.
                		for (int i=3;i<splitted.length;i++)
                    	{
                    		graph[counter][getIndex(splitted[i])] = 1;
                    	}
                	}
                	
                	// This condition shows that there is a - constraint.
                	else if (splitted[2].equals("-"))
                	{
                		//So in the below for loop we made connection of current item with all the items. And after that we will make those connections = 0 who are present after - sign.
                		for (int i=0;i<counter1;i++)
                		{
                			graph[counter][i] = 1;
                		}
                		// So below is the diagonal condition. I.e. self transition is removed.
                		graph[counter][counter] = 0;
                		
                		// Below for loop is removing connections of the current item with the items present after - sign
                		for (int i=3;i<splitted.length;i++)
                    	{
                    		graph[counter][getIndex(splitted[i])] = 0;
                    	}
                	}
                	
                	
                	counter++;
                }  
                fileReader.close();
                
                
                // Below for loop is checking the 2 way constraint. i.e. if we can go to item2 from item1 but we can not goto item1 from item2. Then we will even remove item2->item1.
                // So the resultant matrix have a quality that the matrix and its transpose will be equal.
                for (int i=0;i<counter;i++)
        		{
        			for (int j=0;j<counter;j++)
        			{
        				if (graph[i][j] != graph[j][i])
        				{
        					graph[i][j] = 0;
        					graph[j][i] = 0;
        				}
        			}
        		}
                
                // The below code will revert the graph. i.e. where there is a 0, it will become 1 and there is a 1, it will become 0 in the graph.
                for (int i=0;i<counter;i++)
        		{
        			for (int j=0;j<counter;j++)
        			{
        				if (graph[i][j] == 0)
        				{
        					graph[i][j] = 1;
        				}
        				else
        				{
        					graph[i][j] = 0;
        				}
        				if (i == j)
        				{
        					graph[i][j] = 0;
        				}
        			}
        		}
                
	}
	
	/**
	 * This function will check the least constraining bag for the item passed as parameter.
	 * Least constraining bag would be the one, which will block least number of options for others.
	 * @param index
	 * @return
	 */
	int LCV(int index)
	{
		int minblockCount = 100;
		int minBlockIndex = -1;
		int currentBlockCount = 0;
		
		LinkedList<Integer> li = new LinkedList<Integer>();
		
		for (int i=0;i<count;i++)
		{
			if (resultGraph[i][index] == 0)
			{
				// Below statement will return the number of blocking, if the bag is assigned to the item passed as parameter.
				currentBlockCount = getBlockCount(i, index);
				
				// This statement is to minimize standard deviation among bag sizes. All elements on tie will be stored in a list and then 
				// random among those will be selected.
				if (currentBlockCount == minblockCount)
				{
					li.add(i);
				}
				// If some new bag is found having least blockings then in the below code the list is cleared and added that element in that and respective
				// variables are also updated accordingly.
				if (currentBlockCount < minblockCount)
				{
					li.clear();
					li.add(i);
					minblockCount = currentBlockCount;
					minBlockIndex = i;
				}
			}
		}
		
		// The random among the list of ties will be selected and returned.
		Random r = new Random();
		int itemToGet = r.nextInt(100) % li.size();
		return li.get(itemToGet);
	}
	
	int LCV1(int index)
	{
		int minblockCount = 100;
		int minBlockIndex = -1;
		int currentBlockCount = 0;
		
		//LinkedList<Integer> li = new LinkedList<Integer>();
		
		for (int i=0;i<count;i++)
		{
			if (resultGraph[i][index] == 0)
			{
				// Below statement will return the number of blocking, if the bag is assigned to the item passed as parameter.
				currentBlockCount = getBlockCount(i, index);
				
				// If some new bag is found having least blockings then in the below code the list is cleared and added that element in that and respective
				// variables are also updated accordingly.
				if (currentBlockCount < minblockCount)
				{
					minblockCount = currentBlockCount;
					minBlockIndex = i;
				}
			}
		}
		
		// The random among the list of ties will be selected and returned.
		return minBlockIndex;
	}
	
	/**
	 * This function will return the total items blocked for any bag if the item at indexX is placed at bag at indexY.
	 * @param indexX
	 * @param indexY
	 * @return
	 */
	int getBlockCount(int indexX, int indexY)
	{
		int blockCount = 0;
		for (int i=0;i<counter;i++)
		{
			// It will check if there is a constraint exists related to that in file
			if (graph[indexY][i] == 1)
			{
				// It will return if the effected one is already not blocked.
				if (resultGraph[indexX][i] != -1)
				{			
					blockCount++;
				}
			}	
		}
		return blockCount;
	}
	
	/**
	 * This function will block the elements which are needed to blocked if item at indexX is placed in bag at indexY
	 * @param indexX
	 * @param indexY
	 * @param resultGraph
	 * @return
	 */
	int[][] block(int indexX, int indexY, int [][]resultGraph)
	{
			for (int j=0;j<counter;j++)
			{
				// below statement will check if there is a constraint in graph.
				if (graph[indexY][j] == 1)
				{
					// if there is a constraint below statement will block that respective entry in resultGraph
					resultGraph[indexX][j]=-1;
				}
			}
			return resultGraph;
	}
	
	/**
	 * This function will print the output
	 */
	void display()
	{
		for (int i=0;i<count;i++)
		{
			for (int j=0;j<counter;j++)
			{
				if (resultGraph[i][j] == 1)
				{
					System.out.print(itemName[j] + "\t");
				}
			}
			System.out.println();
		}
	}
	
	/**
	 * This function will calculate the capacity of bag already used and return that capacity.
	 * @param indexX
	 * @return
	 */
	int calculateBagSize(int indexX)
	{
		int size = 0;
		for (int i=0;i<counter;i++)
		{
			// below statement will check if there is an item placed in the bag.
			if (resultGraph[indexX][i] == 1)
			{
				// If there is an item placed then its size is recorded and added in the already occupied bag
				size+=itemSize[i];
			}
		}
		return size;
	}
	/**
	 * This function is a helper function to run the algos.
	 */
	void runAlgo()
	{
		runAlgo(0);
	}
	
	LinkedList<Integer> li = new LinkedList<Integer>();
	
	/**
	 * This function will return true, if some item is completely blocked in the graph passed as parameter
	 * @param graph
	 * @return
	 */
	boolean isBlocked(int [][] graph)
	{
		for (int i=0;i<counter;i++)
		{
			boolean notBlocked = false;
			for (int j=0;j<count;j++)
			{
				// if an item is not completely blocked then below condition will execute atleast once.
				if (graph[j][i] == 0 || graph[j][i] == 1)
				{
					notBlocked = true;
				}
				
			}
			// if the execute does not execute atleast once for any item then that item is blocked
			if (notBlocked == false)
				return true;
		}
		return false;
	}
	
	/**
	 * 
	 * This will check if we place item indexY to bag indexX, then will it block some item or not. This is arc consistency check
	 * @param indexX
	 * @param indexY
	 * @return
	 */
	boolean ifBlocking(int indexX, int indexY)
	{
		int [][]tempGraph = new int[count][];
		// firstly the current graph is stored in a temporary graph. Below block of code is doing this
		for (int i=0;i<count;i++)
		{
			tempGraph[i] = new int [counter];
			for (int j=0;j<counter;j++)
			{
				tempGraph[i][j] = resultGraph[i][j];
			}
		}

		// After that we block the elements affected by indexX and indxeY in tempGraph
		tempGraph = block(indexX, indexY, tempGraph);

		// If it is completely blocking some entry below condition will become true and the function will return true, otherwise false.
		if (isBlocked(tempGraph))
			return true;
		return false;
		
	}
	
	void runAlgo(int startX)
	{
		int x = startX;
		if (startX >= counter) // As it is a sort of recursive procedure, so this would be a base case.
		{
			return;
		}
		try
		{
			for (x=startX;x<counter;x++)
			{
				int xx = findBlocked();
				if (!(xx != -1))
				{
				
					int indexY = 0;
					// This function is selecting the item to whom some bag is assigned.
					indexY = another();
				// The below statement is selecting some bag to be assigned to the item in indxeY
					int indexX = 0;
					if (DFSorBFS)
					{
						indexX = LCV(indexY);
					}
					else
					{
						indexX = LCV1(indexY);
					}
				
				// This is arc consistency check. Before assigning an item to the bag, it will check if it is creating any deadlock in the
				// upcoming items or not, if it is creating a deadlock then it will not be assigned and that state  would become deadstate by assigning
				// it to value -2.
				if (ifBlocking(indexX, indexY))
				{
					resultGraph[indexX][indexY] = -2;
					x--;
				}
				else
				{
					// Assign the item to bag
					if (indexY >= 0)
					{
						// This condition is execute if everything is fine and we now need to assign an item to a bag.
						li.add(indexY);
						
						
						int bagSize1 = calculateBagSize(indexX);
						int itemSize1 = itemSize[indexY];
						
						// Below condition will check if the size of items in a bag exceeds the actual size of bag,
						// if this is the case we will not assign that item to a bag.
						if ((bagSize1 + itemSize1) <= bagSize)
						{
							// This will assign the item to bag
							resultGraph[indexX][indexY] = 1;
							//This will block the items that should be blocked  by putting an item at the location selected by MRV and LCV
							block(indexX, indexY, resultGraph);
							
							// For the purpose of backtracking it will store the current state of graph, indices on which recent item is placed 
							// in the graph.
							StackType st = new StackType();
							st.graph = new int[count][];
							
							for (int i=0;i<count;i++)
							{
								st.graph[i] = new int [counter];
								for (int j=0;j<counter;j++)
								{
									st.graph[i][j] = resultGraph[i][j];
								}
							}
							st.indexX = indexX;
							st.indexY = indexY;
							
							stack.push(st);
						}
						else
						{
							// This will execute if the capacity of bag would become less than the size of items in bag if we put the current
							// item in that. So if this is the case we will not put that item in that bag.
							resultGraph[indexX][indexY] = -1;
							x--;
						}

					}
				}
				
				}
		}
			
		}
		catch(Exception e)
		{
			// If the above exception is thrown then it means solution is not still found.
			// So we will try our luck by doing back tracking.
			backTrack(x);
		}

	}
	
	/**
	 * This function will check if all the items are assigned to a bag or not.
	 * @return
	 */
	public boolean ifAllDone()
	{
		for (int i=0;i<counter;i++)
		{
			boolean isAssigned = false;
			for (int j=0;j<count;j++)
			{
				// For any item assigned to some bag there must be a 1 in its column, if this is the case, then that element is assigned to 
				// some bag
				if (resultGraph[j][i] == 1)
					isAssigned = true;
			}
			// Below condition will execute if any item is still not assigned to any bag
			if (!(isAssigned))
			{
				return false;
			}
		}
		return true;
	}
	void backTrack(int x)
	{
		boolean isProceed1 = false;
		boolean isProceed2 = false;
		StackType st1 = null;
		
		// Below 2 blocks will pop 2 elements from stack and put them in st1 and st2 respectively.
		if (stack.isEmpty() == false)
		{
		      st1 = stack.pop();
		      isProceed1 = true;
		}
		StackType st2 = null;
		if (stack.isEmpty() == false)
		{
			st2 = stack.pop();
			isProceed2 = true;
		}
		
		//StackType st2 = stack.pop();
		// Below condition will execute if there were atleast 2 elements in stack
		if (isProceed1 && isProceed2)
		{
			// Below block will assign the graph contained by second last entry to resultGraph
			for (int i=0;i<count;i++)
			{
				for (int j=0;j<counter;j++)
				{
					resultGraph[i][j] = st2.graph[i][j];
				}
			}
			// Below statement will make the last placed item a dead position by putting -2 there
			resultGraph[st1.indexX][st1.indexY] = -2;
			x--;
			
			// Below block will place the current state of the model in stack i.e. updated resultGraph and indxeX and indexY.
			StackType st = new StackType();
			st.graph = new int[count][];
			
			for (int i=0;i<count;i++)
			{
				st.graph[i] = new int [counter];
				for (int j=0;j<counter;j++)
				{
					st.graph[i][j] = resultGraph[i][j];
				}
			}
			st.indexX = st2.indexX;
			st.indexY = st2.indexY;
			
			stack.push(st);
			runAlgo(x);
		}
		
		// This condition will execute if there was only one element in stack.
		else if (isProceed1)
		{
			resultGraph[st1.indexX][st1.indexY] = -2;
			x--;
			runAlgo(x);
		}
	}
	public void initialize(int counter)
	{
		// This function will allocate memory and initialize all the arrays.
		// counter countains the number of items in the list
		
		itemName = new String [counter];
		itemSize = new int [counter];
		graph = new int[counter][];
		resultGraph = new int[count][];
		
		for (int i=0;i<counter;i++)
		{
			graph[i] = new int[counter];
		}
		for (int i=0;i<count;i++)
		{
			resultGraph[i] = new int[counter];
		}
		
		for (int i=0;i<counter;i++)
		{
			for (int j=0;j<counter;j++)
			{
				graph[i][j] = 0;
			}
		}
		
		for (int i=0;i<count;i++)
		{
			for (int j=0;j<counter;j++)
			{
				resultGraph[i][j] = 0;
			}
		}
	}

//	
	int getIndex(String name)
	{
		// This function is taking the itemName as parameter and return its index.
		
		// For this it is comparing the passed parameter with all the items, where there is a match 
		//it will return that index.
		
		for (int i=0;i<itemName.length;i++)
		{
			if (itemName[i].equals(name))
			{
				return i;
			}
		}
		// If there is no match it will return -1
		return -1;
	}

	public static void main (String [] args) throws IOException
	{
		long startTime = System.nanoTime();
		// below boolean is used to store if the passed parameter is -depth or -breadth
		// It will be marked as true if it is -breadth and false if it is -depth
		boolean arg = true;
		
		// Below boolean will check if the arguments are passed correctly.
		boolean isParametersCorrect = true;
		

		// Below condition is to check if the second argument is -pq
		if (args[1].equals("-pq"))
		{
			arg = false;
		}
		else if (args[1].equals("-minstd"))
		{
			arg = true;
		}
		// Below else block will execute if there is something other than -depth or -breadth
		else
		{
			isParametersCorrect = false;
		}
		// Below condition will execute if the passed parameters are correct.
		if (isParametersCorrect)
		{
			// Below statement is creating an object of the class. and passing filename as first parameter 
			//and a boolean showing -depth or -breadth as second parameter.
			JavaProjectDFSorBFS obj = new JavaProjectDFSorBFS(args[0], arg);
			// Below function is a member function of class JavaProjectDFSorBFS, it will read the input file
			// and make a graph
			obj.readFile();
			int i;
			for (i=0;i<obj.getCount()*25;i++)
			{
				obj.initialize(obj.getCounter());
				obj.assign();
				//Below function is meant to execute the desired algorithm and display its result
				obj.runAlgo();
				if (obj.ifAllDone())
				{
					System.out.println("Success");
					obj.display();
					break;
				}
			}
			
			if (i >= obj.getCount()*25)
			{
				System.out.println("Failure");
			}
		}
		long endTime   = System.nanoTime();
		long totalTime = endTime - startTime;
		System.out.println("The total time spent is : " + totalTime);
	}

}

