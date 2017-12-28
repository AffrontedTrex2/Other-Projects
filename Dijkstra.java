/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyPrograms;

/**
 *
 * @author s-jazhang
 */

import java.util.HashMap;
import java.util.*;
import java.util.Map;

public class Dijkstra {
    
    public static void main(String[] args)
    {   
        //http://hansolav.net/sql/dijkstra_graph.png
        
        //Matrix of distances between vertices; 0 = not connected
        int[][] list = new int[][]
        {
            { 0, 1306, 0, 0, 2161, 2661, 0, 0, 0, 0, 0, 0, 0},
            { 1306, 0, 629, 919, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            { 0, 629, 0, 0, 435, 0, 0, 0, 0, 0, 0, 0, 0},
            { 0, 919, 435, 0, 1225, 0, 0, 1983, 0, 0, 0, 0, 0},
            { 2161, 0, 0, 1225, 0, 1483, 0, 1258, 0, 0, 0, 0, 0},
            { 2661, 0, 0, 0, 1483, 0, 661, 1532, 0, 0, 0, 0, 0},
            { 0, 0, 0, 0, 0, 661, 0, 0, 0, 0, 0, 0, 0},
            { 0, 0, 0, 1983, 1258, 1532, 0, 0, 0, 0, 0, 0, 0},
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        };
        
        //Map of vertices to their names
        Map<Integer, String> names = new HashMap<>();
        names.put(0, "Seattle");
        names.put(1, "San Francisco"); 
        names.put(2, "Los Angeles");
        names.put(3, "Las Vegas");
        names.put(4, "Denver");
        names.put(5, "Minneapolis");
        names.put(6, "Chicago");
        names.put(7, "Dallas");
        names.put(8, "Washington DC");
        names.put(9, "Miami");
        names.put(10, "New York");
        names.put(11, "Boston");
        names.put(12, "Heaven");
        
        //Where we're staring from
        int source = 12;
        
        //Where we want to end
        int end = 12;
        
        //Units lol
        String units = " miles";
        
        //The gift from god
        d(list, source, end, names, units);
    }
    
    //Where the magic happens
    public static void d(int[][] graph, int source, int end, Map names, String units)
    {
        //Sets up the set/dictionaries to be used
        Set<Integer> q = new HashSet<>();
        Map<Integer , Double> distanceDict = new HashMap<>();
        Map<Integer , Integer> pathDict = new HashMap<>();
        
        //Puts the vertices into the set/dictionaries
        for (int i = 0; i < graph.length; i++)
        {
            if (i == source)
            {
                distanceDict.put(i, 0.0); 
            }
            else
            {
                distanceDict.put(i, Double.POSITIVE_INFINITY);
            }
            
            q.add(i);
            pathDict.put(i, null);
        }
        
        //For first iteration, remove the source from the set
        int currentVertex = source;
        q.remove(currentVertex);
        
        updateDist(currentVertex, distanceDict, pathDict, graph);
        
        //For the rest of the iterations, basically repeat the above
        while (!q.isEmpty())
        {
            currentVertex = shortestDist(distanceDict, q);
            q.remove(currentVertex);
            updateDist(currentVertex, distanceDict, pathDict, graph);
        }

        //Prints the distance and the path to the desired vertex
        double distance = distanceDict.get(end);
        if (distance != Double.POSITIVE_INFINITY)
        {
            System.out.println(names.get(end).toString() + " is " + distance + units + " away from " + names.get(source).toString());
        }
        else
        {
            System.out.println("There's no possible path from " + names.get(source).toString() + " to " + names.get(end).toString() + " :(");
        }
        
        if (pathDict.get(end) != null)
        {
            printPathway(pathDict, source, end, names);
        }
    }
    
    //Checks which vertex has the smallest distance that is stil
    //in the set to see which path should be checked next
    public static int shortestDist(Map distanceDict, Set q)
    {
        double shortestDist = Double.POSITIVE_INFINITY;
        int shortestVertex = -1;
        
        Map<Integer, Double> map = distanceDict;
        
        for (Map.Entry<Integer, Double> entry : map.entrySet())
        {
            if (entry.getValue() <= shortestDist && q.contains(entry.getKey()))
            {
                shortestVertex = entry.getKey();
                shortestDist = entry.getValue();
            }
        }
        return shortestVertex;
    }
    
    //Updates the distance and paths for each possible connection of a vertex
    public static void updateDist(Integer currentVertex, Map distanceDict, Map pathDict, int[][] graph)
    {
        //Creates a charList possiblePaths for the current vertex
        List<Integer> possiblePaths = new ArrayList<>();
        
        for (int i = 0; i < graph.length; i++)
        {
            if (graph[i][currentVertex] != 0)
            {
                possiblePaths.add(i);
            }
        }
        
        //For each possible path, calculate if the path is shorter, and if it is, replace the distance values
        for (int i = 0; i < possiblePaths.size(); i++)
        {
            int nextVertex = possiblePaths.get(i);
            
            isShorter(nextVertex, currentVertex, distanceDict, pathDict, graph);
        }
    }
    
    //This is the function that actually does the updating
    public static void isShorter(Integer vertex, Integer lastVertex, Map distanceDict, Map pathDict, int[][] graph)
    {
        //Gets the current distance from the source of the vertex
        //And gets how far the vertex is from the source before adding the new path distance
        Map<Object, Double> graphSave = distanceDict;
        double currentDist = graphSave.get(vertex);
        double lastVertexDist = graphSave.get(lastVertex);
        
        //If the new pathway is shorter, update the distanceDict and pathDict
        if (lastVertexDist + graph[vertex][lastVertex] < currentDist)
        {   
            distanceDict.put(vertex, lastVertexDist + graph[vertex][lastVertex]);
            pathDict.put(vertex, lastVertex);
        }
    }
    
    //Prints the path from the source to the end
    public static void printPathway(Map pathDict, Object source, Object end, Map names)
    {
        Object currentVertex = end;
        List<Object> pathway = new ArrayList<>();
        String name;
        
        //Trace back the path to the source through the pathDict
        while (currentVertex != source)
        {
            name = names.get(currentVertex).toString();
            pathway.add(name);
            currentVertex = pathDict.get(currentVertex);
        }
        
        //Prints out the vertices in reverse order of the list
        if (pathway.get(0) != null)
        {
            System.out.print(names.get(source).toString());
            for (int i = pathway.size() - 1; i >= 0; i--)
            {
                System.out.print("->");
                System.out.print(pathway.get(i));
            }
            System.out.println();
        }
    }
}
