## MS-Apriori Algorithm -

Implementation of the Multiple Support-Apriori Algorithm in JAVA.  
Course : Data Mining and Text Mining under Prof. Bing Liu at the University of Illinois at Chicago.

## About the Algorithm -
MS-Apriori is an algorithm for mining frequent itemsets by using multiple minimum supports. It is a generalization of the Apriori algorithm, which uses a single minimum support threshold.

A transactional database consists of a set of transactions. Each transaction consists of a set of items. The support of an itemset is the number of transactions containing the itemset divided by the total number of transactions.

The algorithm works on the basic principle that some items occur more frequently than others in a dataset and hence different minimum supports must be assigned to each item.
 
An itemset is a frequent itemset if its support is higher or equal to the smallest minimum support threshold from the minimum support thresholds of all its items.

Reference to the algorithm can be found in the book 'Web Data Mining: Exploring hyperlinks, contents and usage data' by Bing Liu.

## Steps to setup the code - 
1. INPUT FILE PATH - Set input file path in MSAP.java file. (Line 212 on Text editor)
2. PARAMETER FILE PATH - Set parameter file path in MSAP.java file. (Line 235 on Text editor)
3. OUTPUT FILE PATH - Set a path where the output file would be generated. (Line 39 on Text editor)

## Steps to run the code -
1. From the Command Prompt, navigate to the directory containing your .java files, say C:\Apriori\, by typing the cd command below.  
>C:\Users\username> cd C:\Apriori\  
>C:\Apriori\>

2. Assuming the file MSAP.java, is in the current working directory, type the javac command in boldface below to compile it.  
>C:\Apriori\> javac MSAP.java  
>C:\Apriori\>  

3. You will then use the java command to execute your program.  
>C:\Apriori\> java MSAP  
