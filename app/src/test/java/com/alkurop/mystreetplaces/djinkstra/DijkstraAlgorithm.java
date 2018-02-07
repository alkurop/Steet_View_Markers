package com.alkurop.mystreetplaces.djinkstra;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DijkstraAlgorithm {
    private final List<Edge> edges;
    private Set<Vertex> settledNodes;
    private Set<Vertex> unSettledNodes;
    private Map<Vertex, Vertex> calculatedPaths;
    private Map<Vertex, Integer> distance;

    public DijkstraAlgorithm(Graph graph) {
        this.edges = new ArrayList<Edge>(graph.getEdges());
    }

    public void execute(Vertex source) {
        settledNodes = new HashSet<>();
        unSettledNodes = new HashSet<>();
        distance = new HashMap<>();
        calculatedPaths = new HashMap<>();

        distance.put(source, 0);
        unSettledNodes.add(source);

        while (unSettledNodes.size() > 0) {
            Vertex closestNode = getClosestUnknownVertex();
            settledNodes.add(closestNode);
            unSettledNodes.remove(closestNode);
            findMinimumDistances(closestNode);
        }
    }

    private void findMinimumDistances(Vertex node) {
        List<Vertex> neighbors = getUnsettledNeighbors(node);
        for (Vertex neibhor : neighbors) {
            int distanceToNeighbour = getSavedDistance(neibhor);
            int distanceToNode = getSavedDistance(node);
            int distanceOfEdge = getDistanceOfEdge(node, neibhor);


            if (distanceToNeighbour > distanceToNode + distanceOfEdge) {
                this.distance.put(neibhor, distanceToNode + distanceOfEdge);
                calculatedPaths.put(neibhor, node);
                unSettledNodes.add(neibhor);
            }
        }
    }

    private int getDistanceOfEdge(Vertex node, Vertex target) {
        for (Edge edge : edges) {
            if (edge.getSource().equals(node) && edge.getDestination().equals(target)) {
                return edge.getWeight();
            }
        }
        throw new RuntimeException("Should not happen");
    }

    private List<Vertex> getUnsettledNeighbors(Vertex node) {
        List<Vertex> neighbors = new ArrayList<Vertex>();
        for (Edge edge : edges) {
            if (edge.getSource().equals(node)
                    && !isSettled(edge.getDestination())) {
                neighbors.add(edge.getDestination());
            }
        }
        return neighbors;
    }

    private Vertex getClosestUnknownVertex( ) {
        Vertex minimum = null;
        for (Vertex vertex : unSettledNodes) {
            if (minimum == null) {
                minimum = vertex;
            } else {
                if (getSavedDistance(vertex) < getSavedDistance(minimum)) {
                    minimum = vertex;
                }
            }
        }
        return minimum;
    }

    private boolean isSettled(Vertex vertex) {
        return settledNodes.contains(vertex);
    }

    private int getSavedDistance(Vertex destination) {
        Integer d = distance.get(destination);
        if (d == null) {
            return Integer.MAX_VALUE;
        } else {
            return d;
        }
    }

    /*
     * This method returns the path from the source to the selected target and
     * NULL if no path exists
     */
    public LinkedList<Vertex> getPath(Vertex target) {
        LinkedList<Vertex> path = new LinkedList<Vertex>();
        Vertex destination = target;
        // check if a path exists
        if (calculatedPaths.get(destination) == null) {
            return null;
        }
        path.add(destination);
        while (calculatedPaths.get(destination) != null) {
            destination = calculatedPaths.get(destination);
            path.add(destination);
        }
        // Put it into the correct order
        Collections.reverse(path);
        Integer d = 0;
        for (Vertex vertex : path) {
            d+= distance.get(vertex);
        }
        System.out.println(d);
        return path;
    }
}
