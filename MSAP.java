import java.util.*;
import java.io.*;
import java.lang.*;
class MSAP
{
	
	static BufferedReader br=null;
	static String lineRead;
	static int itemInTransaction[] = null;																					//Used for storing the integer items of the transaction from file
	static String []items=null;																								//Used for storing all the items of a transaction in string array
	static String itemValueStr=null;																						//To store ItemID from parameter file  
	static String misValueStr=null;																							//To store MIS Value from parameter file
	static List<ArrayList<Integer>> transactionDatabase = new ArrayList<ArrayList<Integer>>();								//Transaction database(List of array of items)
	static String []newitems=null;
	static int TransactionDBSize=0,itemCount=0,mustHaveItemSetSize=0,freqKminus1Size=0;
	static int i,j,l,h,kIterate=2;
	static double sdc=0;																									//Support Difference Constraint
	static List<ArrayList<Integer>> cannotBeTogetherItemSet = new ArrayList<>();											//Cannot be together items(Set of itemsets)
    static List<Integer> mustHaveItemSet = new ArrayList<>();																//Must have items(Set of items)
    static HashMap<Integer, Double> sortedByMISMap = new HashMap<Integer, Double>();
	static LinkedHashMap<Integer, Integer> initPassItemCount = new LinkedHashMap<Integer, Integer>();
	static LinkedHashMap<Integer, Integer> initPass = new LinkedHashMap<Integer, Integer>();
	static LinkedHashMap<ArrayList<Integer>, Double> frequentSet1 = new LinkedHashMap<ArrayList<Integer>, Double>();
	static List<ArrayList<Integer>> frequentKminus1Set = null;
	static TreeMap<Integer, Double> sortedMap = null;
	static List<ArrayList<Integer>> candidate2Set = new ArrayList<>();
	static List<ArrayList<Integer>> frequentKitemSet = null;
	static List<ArrayList<Integer>> finalFrequentItemSet = new ArrayList<>();
	static List<ArrayList<Integer>> candidateKSetCopy = new ArrayList<>();
	static LinkedHashMap<ArrayList<Integer>,Integer> candidateCount= new LinkedHashMap<ArrayList<Integer>,Integer>();
	static List<ArrayList<Integer>> candidateKSet = null;
	static List<ArrayList<Integer>> frequentKitemSetCopy = null;
	
	public static void main(String[] args) throws Exception
	{
		
        ReadInputFilefn();
		ReadParameterFilefn();
		BufferedWriter writer = new BufferedWriter(new FileWriter("D:\\DMTM\\output.txt"));
		PrintWriter printWriter = new PrintWriter(writer);

		//System.out.println("Transaction Database: "+transactionDatabase);
		
		sortedMap = sortMapByValue(sortedByMISMap); 																		//Sort the items on basis of MIS Values 
		//System.out.println(sortedMap);
		
		generateInitPass_F1Set();
        System.out.println("Frequent-1 itemsets:");
        writer.write("Frequent-1 itemsets:");
        writer.newLine();
        Set<ArrayList<Integer>> c=frequentSet1.keySet();
        for(ArrayList<Integer> arrayList : c)
        {
            System.out.println();
            writer.newLine();
            double xyz = frequentSet1.get(arrayList);
            System.out.print("\t"+(int)(xyz*TransactionDBSize)+" :"+" {"+arrayList.get(0)+"}");
            printWriter.print("\t"+(int)(xyz*TransactionDBSize)+" :"+" {"+arrayList.get(0)+"}");
        }
        System.out.println();
        System.out.println();
        System.out.println("\tTotal number of Frequent-1 itemsets = "+frequentSet1.size());
        System.out.println();

        writer.newLine();
		writer.newLine();
        printWriter.printf("\tTotal number of Frequent-1 itemsets = "+frequentSet1.size());
		writer.newLine();
		writer.newLine();

		while(!frequentKminus1Set.isEmpty())
		{
			if(kIterate==2)
			{
				level2_candidate_gen();
				//System.out.println("Level 2 Candidate Set: "+candidate2Set);
				candidateKSet= new ArrayList<>(candidate2Set);
			}
			else
			{
				MScandidate_gen();
				//System.out.println("Level "+kIterate+" Candidate Set: "+candidateKSet);
			}
			frequentKitemSet = new ArrayList<>();
			frequentKitemSetCopy = new ArrayList<>();
			frequentKminus1Set = new ArrayList<>();
			for(i=0;i<candidateKSet.size();i++)
			{
				candidateCount.put(candidateKSet.get(i),0);
			}
			for(i=0;i<candidateKSet.size();i++)
			{
				ArrayList<Integer> temp = new ArrayList<>();
				temp = candidateKSet.get(i);
				candidateKSetCopy.add(new ArrayList<Integer>(temp));
			}
			for(h=0;h<candidateKSetCopy.size();h++)
			{
				ArrayList<Integer> temp = null;
				temp = new ArrayList<>(candidateKSetCopy.get(h));
				temp.remove(0);
				candidateCount.put(temp,0);
			}
			
			Set<ArrayList<Integer>> s=candidateCount.keySet();
		
			for(i=0;i<TransactionDBSize;i++)
			{
				for (ArrayList<Integer> arrayList : s) 
				{
					if((transactionDatabase.get(i)).containsAll(arrayList))
					{
						int cCount = candidateCount.get(arrayList);
						candidateCount.put(arrayList, cCount + 1);
					}
				}
			}
			for(i=0;i<candidateKSet.size();i++)
			{
				int flag1=0, flag2=0;
				if((double)candidateCount.get(candidateKSet.get(i))/TransactionDBSize>=sortedByMISMap.get(candidateKSet.get(i).get(0)))
				{
					for (j=0;j<cannotBeTogetherItemSet.size();j++)
					{
						if((candidateKSet.get(i)).containsAll(cannotBeTogetherItemSet.get(j)))
						{
							flag1=1;
						}
					}
					for(j=0;j<mustHaveItemSet.size();j++)
					{
						if(candidateKSet.get(i).contains(mustHaveItemSet.get(j)))
						{
							flag2=1;
						}
					}
					if(flag1==0 && flag2==1)
						frequentKitemSetCopy.add(candidateKSet.get(i));

					frequentKitemSet.add(candidateKSet.get(i));
					frequentKminus1Set.add(new ArrayList<Integer>(candidateKSet.get(i)));
					//finalFrequentItemSet.add(new ArrayList<Integer>(candidateKSet.get(i)));

				}
			}

            System.out.println();
			writer.newLine();
			if(frequentKitemSetCopy.size()!=0)
			{
                System.out.println("Frequent " + kIterate + " Itemsets:");
                printWriter.print("Frequent " + kIterate + " Itemsets:");
                System.out.println();
                writer.newLine();
                for (i = 0; i < frequentKitemSetCopy.size(); i++) {
                    System.out.println();
                    writer.newLine();
                    StringBuilder sb = new StringBuilder();
                    for (int j = 0; j<frequentKitemSetCopy.get(i).size();j++) {
                        int num = frequentKitemSetCopy.get(i).get(j);
                        sb.append(num);
                        sb.append(", ");
                    }
                    sb.setLength(sb.length() - 2);
                    System.out.print("\t" + candidateCount.get(frequentKitemSetCopy.get(i)) + " :" + " {" + sb + "}");
                    printWriter.print("\t" + candidateCount.get(frequentKitemSetCopy.get(i)) + " :" + " {" + sb + "}");
                    ArrayList<Integer> temp = new ArrayList<>(frequentKitemSetCopy.get(i));
                    temp.remove(0);
                    System.out.println("\nTailcount = " + candidateCount.get(temp));
                    writer.newLine();
                    printWriter.print("\nTailcount = " + candidateCount.get(temp));
                }
                System.out.println();
                writer.newLine();
                System.out.println();
				writer.newLine();
                System.out.println("\tTotal number of Frequent-" + kIterate + " itemsets = " + frequentKitemSetCopy.size());
				writer.newLine();
				printWriter.print("\tTotal number of Frequent-" + kIterate + " itemsets = " + frequentKitemSetCopy.size());
                System.out.println();
                writer.newLine();
                writer.newLine();
            }
			candidateKSet = new ArrayList<ArrayList<Integer>>();
			candidateKSetCopy = new ArrayList<ArrayList<Integer>>();
			candidateCount = new LinkedHashMap<ArrayList<Integer>,Integer>();

			//System.out.println("FK-1:"+frequentKminus1Set);
			//System.out.println(candidateKSetCopy);
			//System.out.println(candidateKSet);
			//System.out.println("CC:"+candidateCount);
			//System.out.println(finalFrequentItemSet);
			kIterate++;
		}
		writer.write("...");
		writer.close();
	}
 
	public static TreeMap<Integer, Double> sortMapByValue(HashMap<Integer, Double> map)										//Used for sorting purpose
    {
		Comparator<Integer> comparator = new ValueComparator(map);
		//TreeMap is a map sorted by its keys. 
		//The comparator is used to sort the TreeMap by keys. 
		TreeMap<Integer, Double> result = new TreeMap<Integer, Double>(comparator);
		result.putAll(map);
		return result;
	}
	
	//Used for reading the content of Input File
	public static void ReadInputFilefn() throws Exception
	{
		BufferedReader br = new BufferedReader(new FileReader("D:\\DMTM\\input-data.txt"));
		while (((lineRead = br.readLine()) != null))																		 
		{ 
			lineRead = lineRead.substring(1, lineRead.length() - 1);														//Remove the brackets
			lineRead = lineRead.replace(",", "").trim();																	//Remove commas and extra spaces
			
		    items = lineRead.split(" ");																					//Get the entire set of items

			ArrayList<Integer> itemInTransaction = new ArrayList<Integer>();												//Set the array size as length of the itemSet																		
            for(i = 0; i < items.length; i++)
            {
                itemInTransaction.add(Integer.parseInt(items[i]));                 											//Convert string to integer
            }
            transactionDatabase.add(itemInTransaction);																		//Add the integer itemset to transaction database
            //System.out.println(transactionDatabase);
            TransactionDBSize++;
		}
		br.close();
	}
	
	//Used for reading the content of parameter file
	public static void ReadParameterFilefn() throws Exception
	{
		br = new BufferedReader(new FileReader("D:\\DMTM\\parameter-file.txt"));
        while (((lineRead = br.readLine()) != null)) 
		{
			if (lineRead.contains("MIS"))																					//To handle the MIS()=__  lines 
			{
                lineRead = lineRead.substring(4); 																			//Substring without "MIS("																	//Removes MIS( from line
                items = lineRead.split("=");																				//Splits the line at '='
				itemValueStr = items[0].replace(")", "");
				itemValueStr = itemValueStr.replace(" ", "");
                int itemValue = Integer.parseInt(itemValueStr);
			  	misValueStr = items[1].replace(" ", "");																	//Remove space appearing before the value
                double misValue = Double.parseDouble(misValueStr);
                sortedByMISMap.put(itemValue, misValue);
                itemCount++;
            }
			else if (lineRead.contains("SDC"))																				//To handle the SDC=__ lines		
            {
                lineRead = lineRead.substring(6);																			//Substring without "SDC = "
                lineRead = lineRead.replace(" ", "");																			
                sdc = Double.parseDouble(lineRead);
                //System.out.println(sdc);
            }
			else if (lineRead.contains("cannot_be_together")) 																//To handle the cannot_be_together=__ lines
            {
                lineRead = lineRead.substring(19);																			//Substring without "cannot_be_together:"
				lineRead = lineRead.trim();																					
                lineRead = lineRead.substring(1, lineRead.length() - 1);													
                lineRead = lineRead.replace(",", "").trim();
                lineRead = lineRead.replace("{", "").trim();
		        items = lineRead.split("}");
		           
	            for(i = 0; i < items.length; i++)
	            {
	             	items[i] = items[i].trim();
	                newitems = items[i].split(" ");
	                
	                ArrayList<Integer> itemInTransaction = new ArrayList<Integer>();										//Set the array size as length of the itemSet																		
	                for(j = 0; j < newitems.length; j++)
	                {
	                    itemInTransaction.add(Integer.parseInt(newitems[j]));          									    //Convert string to integer
	                }
	                cannotBeTogetherItemSet.add(itemInTransaction);
	            }
	            //System.out.println("CBT:"+cannotBeTogetherItemSet);
	        }
            else if (lineRead.contains("must-have"))																		//To handle the must-have=__ lines
            {
                lineRead = lineRead.replace("must-have: ", "").trim();
			    items = lineRead.split("or");

			    for (i=0;i<items.length;i++) 
			    {
			        items[i] = items[i].trim();
			        mustHaveItemSet.add(Integer.parseInt(items[i]));
					//System.out.println(mustHaveItemSet);
			        mustHaveItemSetSize++;
			    }   
            }
		}
		if(mustHaveItemSet.size()==0)
		{
			Set<Integer> s=sortedByMISMap.keySet();
			for (Integer n : s)
			{
				mustHaveItemSet.add(n);
			}
		}
		br.close();
	}
	
	//Used to generate the frequent-1 itemset (represented as frequentSet1)
	public static void generateInitPass_F1Set()
	{
		int flag = 0;
		frequentKminus1Set = new ArrayList<ArrayList<Integer>>();
		Set set = sortedMap.entrySet();
        Iterator iterator = set.iterator();																					//To iterate over the hash-map
		while(iterator.hasNext())
	    {
			int numC=0;
			Map.Entry mentry = (Map.Entry)iterator.next();
			for(i=0;i<TransactionDBSize;i++)																				//Iterate 'transaction database size' times
			{
				for(j=0;j<(transactionDatabase.get(i)).size();j++)
		     	{
					int temp1=Integer.valueOf((int)mentry.getKey());
					int temp2=((transactionDatabase.get(i)).get(j));
					if(temp1==temp2)																						//Used to compare the item with the transaction itemset
					{
						numC++;
					}
			    }
			} 
			initPassItemCount.put(Integer.valueOf((int)mentry.getKey()), numC);												//Stores the count for each item
			for(i=0;i<initPassItemCount.size();i++)
			{
				if(sortedByMISMap.get(initPassItemCount.keySet().toArray()[i])<=((double)(initPassItemCount.get(initPassItemCount.keySet().toArray()[i]))/TransactionDBSize))
				{
					initPass.put(Integer.valueOf((int)initPassItemCount.keySet().toArray()[i]), (initPassItemCount.get(initPassItemCount.keySet().toArray()[i])));
					break;
				}
			}
			for(j=i+1;j<initPassItemCount.size();j++)
			{
				if((sortedByMISMap.get(initPassItemCount.keySet().toArray()[i]))<=((double)initPassItemCount.get(initPassItemCount.keySet().toArray()[j])/TransactionDBSize))
				{
					initPass.put(Integer.valueOf((int)initPassItemCount.keySet().toArray()[j]), (initPassItemCount.get(initPassItemCount.keySet().toArray()[j])));
				}	
			}
			if((double)numC/TransactionDBSize>=Double.valueOf((double)mentry.getValue()))
			{
				int flag1=0, flag2=0;
				ArrayList<Integer> intermediate = new ArrayList<>();
				intermediate.add(Integer.valueOf((int)mentry.getKey()));
				for (j=0;j<cannotBeTogetherItemSet.size();j++)
				{
					if(intermediate.containsAll(cannotBeTogetherItemSet.get(j)))
					{
						flag1=1;
					}
				}
				for(j=0;j<mustHaveItemSet.size();j++)
				{
					if(intermediate.contains(mustHaveItemSet.get(j)))
					{
						flag2=1;
					}
				}
				if(flag1==0 && flag2==1)
				{
					frequentSet1.put(intermediate, (double) numC / TransactionDBSize);
					frequentKminus1Set.add(intermediate);
				}
			}
		}
		
		//System.out.println("IC: "+initPassItemCount);
		//System.out.println("L: "+initPass);
		//System.out.println("Frequent-1 Set: "+frequentSet1);
	}
	
	//Level 2 Candidate generate function
	public static void level2_candidate_gen()
	{
		
		for(l=0;l<initPass.size();l++)
		{	
			if((double)(initPass.get(initPass.keySet().toArray()[l]))/TransactionDBSize>=sortedByMISMap.get(initPass.keySet().toArray()[l]))
			{
				for(h=l+1;h<initPass.size();h++)
				{
					ArrayList<Integer> addCandidateSet = new ArrayList<Integer>();
					if(((double)(initPass.get(initPass.keySet().toArray()[h]))/TransactionDBSize>=sortedByMISMap.get(initPass.keySet().toArray()[l]))&&((double)(Math.abs((initPass.get(initPass.keySet().toArray()[h]))-(initPass.get(initPass.keySet().toArray()[l]))))/TransactionDBSize)<=sdc)
					{
						addCandidateSet.add(Integer.valueOf((int)initPass.keySet().toArray()[l]));
						addCandidateSet.add(Integer.valueOf((int)initPass.keySet().toArray()[h]));
						candidate2Set.add(addCandidateSet);
					}
				}
			}
		}	
		//System.out.println(candidate2Set);	
	}
	
	public static void MScandidate_gen()
	{
		candidateKSet = new ArrayList<>();
		int ik_1, iDashk_1;
		for(i=0;i<frequentKminus1Set.size();i++)
		{
			for(j=i+1;j<frequentKminus1Set.size();j++)
			{
				ArrayList<Integer> f1 = new ArrayList<Integer>(frequentKminus1Set.get(i));
				ArrayList<Integer> f2 = new ArrayList<Integer>(frequentKminus1Set.get(j));
                ArrayList<Integer> f1Clone = new ArrayList<Integer>(f1);
                ArrayList<Integer> f2Clone = new ArrayList<Integer>(f2);
                f1Clone.remove(f1.size()-1);
                f2Clone.remove(f2.size()-1);
				if(f1Clone.equals(f2Clone))
				{
                    if(f1.get(f1.size()-1)<f2.get(f2.size()-1))
				    {
                        ik_1 = f1.get(f1.size() - 1);
                        iDashk_1 = f2.get(f2.size() - 1);
                    }
                    else
                    {
                        iDashk_1 = f1.get(f1.size() - 1);
                        ik_1 = f2.get(f2.size() - 1);
                    }
                    if ((double) Math.abs(initPassItemCount.get(ik_1) - initPassItemCount.get(iDashk_1)) / TransactionDBSize <= sdc) {
                        f1.remove(f1.size() - 1);
                        ArrayList<Integer> cInterim = new ArrayList<Integer>(f1);
                        cInterim.add(ik_1);
                        cInterim.add(iDashk_1);
                        candidateKSet.add(cInterim);
                        ArrayList<ArrayList<Integer>> subsetOfC = new ArrayList<ArrayList<Integer>>();
                        for (h = 0; h < cInterim.size(); h++) {
                            ArrayList<Integer> temp = new ArrayList<>(cInterim);
                            temp.remove(h);
                            ArrayList<Integer> cIntermMinus1 = new ArrayList<Integer>(temp);
                            subsetOfC.add(cIntermMinus1);
                        }
                        for (h = 0; h < subsetOfC.size(); h++) {
                            if ((subsetOfC.get(h)).contains(cInterim.get(0)) || ((sortedByMISMap.get(cInterim.get(0))) == (sortedByMISMap.get(cInterim.get(1))))) {
                                int flag = 0;
                                for (l = 0; l < frequentKminus1Set.size(); l++) {
                                    if ((frequentKminus1Set.get(l)).containsAll(subsetOfC.get(h)))
                                        flag = 1;
                                }
                                if (flag == 0)
                                    candidateKSet.remove(cInterim);
                            }
                        }
                    }
                }
			}
		}
		//System.out.println("CKSet "+kIterate+" set:"+candidateKSet);
	}
}

class ValueComparator implements Comparator<Integer>
{
	HashMap<Integer, Double> map = new HashMap<Integer, Double>();
	public ValueComparator(HashMap<Integer, Double> map)
	{
		this.map.putAll(map);
	}
	@Override
	public int compare(Integer s1, Integer s2) {
	if(map.get(s1) <= map.get(s2))
	{
		return -1;
	}else
	{
		return 1;
	}	
	}
}