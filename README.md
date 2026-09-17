Overview:
This project can be used to create weighted graphs, with connections between nodes using decimal weights, as well as to search among nodes to find the shortest path from one to another. The program does allow for negative edge weights between nodes, as it chooses from three different searching algorithms depending on the input graphs. With no negative edge weights, it selects Dijkstra's. With at least one negative edge weight, and a single target source and destination, it selects Bellman Ford. For all pairs shortest paths, taking in a src input of "<ALL>" and a dest input of "<ALL>", it selects Floyd Warshall. Nodes may be added, removed, or requested, and the program will automatically convert between storing the graph as an Adjacency List or an Adjacency Matrix depending on what is more optimal for the given graph (Adjacency List functions better for sparse graphs, and Adjacency matrix for dense ones). At a density of less than 0.33, the graph will convert into an adjacency list. At a density of greater than 0.66, it will convert into an adjacency matrix.

Features:
Calculates all pairs shortest paths search using Floyd Warshall algorithm
Calculates shortest path from one node to another using Bellman Ford algorithm in the case of negative edge weights
Calculates shortest path from one node to another using Dijkstra's algorithm in the case of no negative edge weights
Automatically converts between adjacency list and adjacency matrix to store the graph as the graph becomes denser or sparser
Add Nodes to graph
Add connection between nodes on graph, adding the specified nodes if they do not already exist
Remove Entire node from the graph
Remove a specific connection from the graph
Return the density of the graph
Return the weight of a connection
Return a list of edges
Return a list of Nodes
Read a graph from a filepath as input

Input Specifications:
Input graph must be in standard JSON format, specifically as shown in the example below:
{
	"source1" : {
		"dest11" : weight11,
		"dest12" : weight12,
		"dest13" : weight13,
		"dest14" : weight14
	},
	"source2" : {
		"dest21" : weight21,
		"dest22" : weight22,
		"dest23" : weight23,
		"dest24" : weight24,
		"dest25" : weight25,
		"dest26" : weight26,
		"dest27" : weight27,
		"dest28" : weight28
	}
}
