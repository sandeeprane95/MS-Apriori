# MS-Apriori Algorithm -

Implementation of the Multiple Support-Apriori Algorithm in JAVA. 

# About the Algorithm -
MS-Apriori is an algorithm for mining frequent itemsets by using multiple minimum supports. It is a generalization of the Apriori algorithm, which uses a single minimum support threshold.

A transactional database consists of a set of transactions. Each transaction consists of a set of items. The support of an itemset is the number of transactions containing the itemset divided by the total number of transactions.

The algorithm works on the basic principle that some items occur more frequently than others in a dataset and hence different minimum supports must be assigned to each item.
 
An itemset is a frequent itemset if its support is higher or equal to the smallest minimum support threshold from the minimum support thresholds of all its items.
