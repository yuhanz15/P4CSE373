package mazes.logic.carvers;

import graphs.EdgeWithData;
import graphs.minspantrees.MinimumSpanningTree;
import graphs.minspantrees.MinimumSpanningTreeFinder;
import mazes.entities.Room;
import mazes.entities.Wall;
import mazes.logic.MazeGraph;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Carves out a maze based on Kruskal's algorithm.
 */
public class KruskalMazeCarver extends MazeCarver {
    MinimumSpanningTreeFinder<MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder;
    private final Random rand;

    public KruskalMazeCarver(MinimumSpanningTreeFinder
                                 <MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder) {
        this.minimumSpanningTreeFinder = minimumSpanningTreeFinder;
        this.rand = new Random();
    }

    public KruskalMazeCarver(MinimumSpanningTreeFinder
                                 <MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder,
                             long seed) {
        this.minimumSpanningTreeFinder = minimumSpanningTreeFinder;
        this.rand = new Random(seed);
    }

    @Override
    protected Set<Wall> chooseWallsToRemove(Set<Wall> walls) {
        // TO DO: replace this with your code
        // Hint: you'll probably need to include something like the following:
        // this.minimumSpanningTreeFinder.findMinimumSpanningTree(new MazeGraph(edges));
        Set<EdgeWithData<Room, Wall>> edges = new HashSet<>();
        for (Wall wall : walls) {
            double randomWeight = rand.nextDouble();
            edges.add(new EdgeWithData<>(wall.getRoom1(), wall.getRoom2(), randomWeight, wall));
        }
        MinimumSpanningTree<Room, EdgeWithData<Room, Wall>> fMST =
            this.minimumSpanningTreeFinder.findMinimumSpanningTree(new MazeGraph(edges));
        Set<Wall> usedToRemove = new HashSet<>();
        for (EdgeWithData<Room, Wall> edge : fMST.edges()) {
            usedToRemove.add(edge.data());
        }
        return usedToRemove;
    }
}
