package graphs.shortestpaths;

import graphs.BaseEdge;
import graphs.Graph;
import priorityqueues.DoubleMapMinPQ;
import priorityqueues.ExtrinsicMinPQ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Computes shortest paths using Dijkstra's algorithm.
 * @see SPTShortestPathFinder for more documentation.
 */
public class DijkstraShortestPathFinder<G extends Graph<V, E>, V, E extends BaseEdge<V, E>>
    extends SPTShortestPathFinder<G, V, E> {

    protected <T> ExtrinsicMinPQ<T> createMinPQ() {
        return new DoubleMapMinPQ<>();
        /*
        If you have confidence in your heap implementation, you can disable the line above
        and enable the one below.
         */
        // return new ArrayHeapMinPQ<>();

        /*
        Otherwise, do not change this method.
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
         */
    }

    @Override
    protected Map<V, E> constructShortestPathsTree(G graph, V start, V end) {
        // according to the Pseudocode from lecture 21
        HashMap<V, E> edgeTo = new HashMap<>();
        HashMap<V, Double> distTo = new HashMap<>();
        ExtrinsicMinPQ<V> perimeter = createMinPQ();
        perimeter.add(start, 0);
        distTo.put(start, 0.0);


        while (!perimeter.isEmpty()) {
            V u = perimeter.removeMin();

            if (Objects.equals(end, u)) {
                break;
            }

            for (E edge : graph.outgoingEdgesFrom(u)) {
                V v = edge.to();
                double w = edge.weight();
                if (!distTo.containsKey(v)) {
                    distTo.put(v, Double.POSITIVE_INFINITY);
                }
                double oldDist = distTo.get(v);
                double newDist = distTo.get(u) + w;
                if (newDist < oldDist) {
                    distTo.put(v, newDist);
                    edgeTo.put(v, edge);
                    if (perimeter.contains(v)) {
                        perimeter.changePriority(v, newDist);
                    } else {
                        perimeter.add(v, newDist);
                    }
                }
            }
        }
        return edgeTo;
    }

    @Override
    protected ShortestPath<V, E> extractShortestPath(Map<V, E> spt, V start, V end) {
        // TO DO: replace this with your code
        if (Objects.equals(start, end)) {
            return new ShortestPath.SingleVertex<>(start);
        }

        E edge = spt.get(end);
        if (edge == null) {
            return new ShortestPath.Failure<>();
        }

        ArrayList<E> list = new ArrayList<>();
        V from = edge.from();
        while (!Objects.equals(start, from)) {
            list.add(0, edge);
            edge = spt.get(from);
            from = edge.from();
        }
        list.add(0, edge);
        return new ShortestPath.Success<>(list);
    }

}
