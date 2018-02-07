package com.alkurop.mystreetplaces.djinkstra

import java.util.*

class D1(val graph: Graph) {

    val evaluated = mutableSetOf<Vertex>()
    val unknown = mutableSetOf<Vertex>()

    val optimalRoutes = hashMapOf<Vertex, Vertex>()
    val minDistance = hashMapOf<Vertex, Int>()

    fun execute(s: Vertex) {
        minDistance.put(s, 0)
        unknown.add(s)
        while (unknown.isNotEmpty()) {
            val source = getClosestUnknownVertex()!!
            evaluated.add(source)
            unknown.remove(source)
            getUnknownNeighbours(source)
                    .forEach { neibhor ->
                        val distanceToNeighbour = getKnownDistance(neibhor)
                        val distanceToSource = getKnownDistance(source)
                        val distanceOfEdge = getDistanceOfEdge(source, neibhor)
                        if (distanceToNeighbour > distanceToSource + distanceOfEdge) {
                            minDistance.put(neibhor, distanceToSource + distanceOfEdge)
                            optimalRoutes.put(neibhor, source)
                            unknown.add(neibhor)
                        }
                    }
        }
    }

    fun getKnownDistance(vertex: Vertex): Int =
            if (minDistance.containsKey(vertex)) {
                minDistance.get(vertex)!!
            } else Int.MAX_VALUE

    private fun getClosestUnknownVertex(): Vertex? {
        var minimum: Vertex? = null
        for (vertex in unknown) {
            if (minimum == null) {
                minimum = vertex
            } else {
                val knownDistance = getKnownDistance(vertex)
                val knownDistance1 = getKnownDistance(minimum)
                if (knownDistance < knownDistance1) {
                    minimum = vertex
                }
            }
        }
        return minimum
    }

    private fun getDistanceOfEdge(node: Vertex, target: Vertex): Int {
        for (edge in graph.edges) {
            if (edge.source == node && edge.destination == target) {
                return edge.weight
            }
        }
        throw RuntimeException("Should not happen")
    }

    fun getUnknownNeighbours(vertex: Vertex): List<Vertex> {
        val list = graph.edges
                .filter { it.source == vertex }
                .map { it.destination }
                .filter { evaluated.contains(it).not() }
        return list
    }

    fun getPath(target: Vertex): LinkedList<Vertex>? {
        val path = LinkedList<Vertex>()
        path.add(target)
        var temp: Vertex? = target
        if (optimalRoutes.containsKey(temp)) {
            temp = optimalRoutes.get(temp)
            path.add(temp!!)
        } else return null
        while (optimalRoutes.containsKey(temp)) {
            temp = optimalRoutes.get(temp)
            path.add(temp!!)
        }
        path.reverse()
        return path
    }

}
