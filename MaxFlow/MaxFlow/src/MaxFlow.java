import java.util.ArrayList;

public class MaxFlow {
    public static void main(String[] args) {
        for (int i = 1; i < 6; i++) {
            //make graph
            String file = "demands" + i + ".txt";
            Graph graph = new Graph();
            graph.makeGraph(file);

            //compute max flow / min cut
            System.out.println(file);
            maxFlow(graph.G[0], graph.G[graph.G.length-1], graph);
            finalFlow(graph.G);
            cutMin(graph.G);
            System.out.println();
        }
    }

    /**
     * Finds the flows of the graph using other methods
     * @param source GraphNode where the graph starts
     * @param sink GraphNode where the graph ends
     * @param graph Overall graph
     */
    private static void maxFlow(GraphNode source, GraphNode sink, Graph graph) {
        ArrayList<Flow> flows = findFlows(source, sink, graph);

        //prints the max flow
        String output = "";
        int totFlow = 0;
        for (int i = 0; i < flows.size(); i++) {
            totFlow += flows.get(i).cap;
        }
        output += "MAX FLOW: \n";
        for (int i = 0; i < flows.size(); i++) {
            String line = "Found Flow " + flows.get(i).cap + ":";
            for (int j = 0; j < flows.get(i).path.length; j++) {
                line += " " + flows.get(i).path[j];
            }
            output += line + "\n";
        }
        output += "Produced: " + totFlow + "\n";
        System.out.println(output);
    }

    /**
     * Finds all the flows using the find flow method and translates them using flow class for better use
     * @param source GraphNode where the graph starts
     * @param sink GraphNode where the graph ends
     * @param graph Overall graph
     * @return ArrayList of Flow objects for all the flows in graph
     */
    private static ArrayList<Flow> findFlows(GraphNode source, GraphNode sink, Graph graph){
        ArrayList<Flow> flows = new ArrayList<>();

        //find all flows
        ArrayList<GraphNode> flow = findFlow(graph.G, source, sink);
        while (flow.size() != 0) {
            //find bottleneck
            int bottleneck = 100;
            for (int i = 0; i < flow.size()-1; i++) {
                for (int j = 0; j < flow.get(i).succ.size(); j++) {
                    GraphNode.EdgeInfo edge = flow.get(i).succ.get(j);
                    if(edge.to == flow.get(i+1).nodeID){
                        if (edge.capacity < bottleneck){
                            bottleneck = edge.capacity;
                        }
                    }
                }
            }

            //update capacities
            for (int i = 0; i < flow.size()-1; i++) {
                for (int j = 0; j < flow.get(i).succ.size(); j++) {
                    GraphNode.EdgeInfo edge = flow.get(i).succ.get(j);
                    if(edge.to == flow.get(i+1).nodeID){
                        edge.capacity -= bottleneck;
                    }
                }
            }
            flows.add(new Flow(flow,bottleneck));

            //find new flow
            flow = findFlow(graph.G, source, sink);
        }
        return flows;
    }

    /**
     * Finds the smallest flow with the largest capacity, treats length as priority over capacity
     * @param G Array of the graph vertices
     * @param s source
     * @param t sink
     * @return Array list that has the vertices of the chosen flow
     */
    private static ArrayList<GraphNode> findFlow(GraphNode[] G, GraphNode s, GraphNode t){
        ArrayList<Integer> queue = new ArrayList<>();
        ArrayList<GraphNode> flow = new ArrayList<>();

        //reset vertices
        for (GraphNode graphNode : G) {
            graphNode.visited = false;
        }

        //goes through graph finding the best path
        queue.add(s.nodeID);
        s.visited = true;
        s.parent = -1;
        boolean done = false;
        while (!done) {
            GraphNode node = G[queue.remove(0)];
            for (int i = 0; i < node.succ.size(); i++) {
                if (node.succ.get(i).capacity > 0) {
                    GraphNode subnode = G[node.succ.get(i).to];
                    if (!subnode.visited) {
                        queue.add(subnode.nodeID);
                        subnode.visited = true;
                        subnode.parent = node.nodeID;
                    }
                    if(subnode.nodeID == t.nodeID){
                        createFlow(subnode, G, flow);
                        done = true;
                    }
                }
            }
            if (queue.isEmpty()){
                done = true;
            }
        }
        return flow;
    }

    /**
     * recursive method that is called from find flow to set the flow in an array in the right order
     * @param node end node the recursies to the beginning
     * @param G array of graph vertices
     * @param flow created flow ArrayList
     */
    private static void createFlow(GraphNode node, GraphNode[] G, ArrayList<GraphNode> flow){
        if (node.parent == -1){
            flow.add(node);
            return;
        }
        createFlow(G[node.parent], G, flow);
        flow.add(node);
    }

    /**
     * Class the keeps track of a flows length. Flows bottleneck value and the path the flow follows
     */
    private static class Flow{
        int cap;
        int length;
        int[] path;
        private Flow(ArrayList<GraphNode> flow, int bottleneck){
            this.cap = bottleneck;
            this.length = flow.size();
            path = new int[length];
            for (int i = 0; i < length; i++) {
                path[i] = flow.get(i).nodeID;
            }
        }

    }

    /**
     * After max flow is calculated the final flow print out the edges that transports material
     * @param G array of graph vertices
     */
    private static void finalFlow(GraphNode[] G){
        for (int i = 0; i < G.length; i++) {
            for (int j = 0; j < G[i].succ.size(); j++) {
                GraphNode.EdgeInfo edge = G[i].succ.get(j);
                int capacity = edge.startCapacity-edge.capacity;
                if(capacity != 0) {
                    System.out.println("Edge (" + edge.from + "," + edge.to + ") transports "
                            + capacity + " cases");
                }
            }
        }
        System.out.println();
    }

    /**
     * Finds the R array that holds the vertices connected to the source that do not hit max capacity.
     * Uses the R array to find the min cute edges
     * @param G array of graph vertices
     */
    private static void cutMin(GraphNode[] G){
        ArrayList<GraphNode> queue = new ArrayList<>();
        ArrayList<GraphNode> R = new ArrayList<>();
        ArrayList<GraphNode.EdgeInfo> cuts = new ArrayList<>();
        ArrayList<Integer> Rtrack = new ArrayList<>();

        //initialize nodes
        for (GraphNode graphNode : G) {
            graphNode.visited = false;
        }

        //Find R's
        queue.add(G[0]);
        R.add(G[0]);
        while (!queue.isEmpty()){
            GraphNode node = G[queue.remove(0).nodeID];
            for (int j = 0; j < node.succ.size(); j++) {
                GraphNode subnode = G[node.succ.get(j).to];
                if (!subnode.visited && node.succ.get(j).capacity > 0) {
                    subnode.visited = true;
                    queue.add(subnode);
                    R.add(subnode);
                    Rtrack.add(subnode.nodeID);
                }
            }
        }

        //Find Min Cuts
        for (int i = 0; i < R.size(); i++) {
            GraphNode node = G[R.get(i).nodeID];
            for (int j = 0; j < node.succ.size(); j++) {
                GraphNode.EdgeInfo edge = node.succ.get(j);
                if(!Rtrack.contains(edge.to) && edge.capacity == 0){
                    cuts.add(edge);
                }
            }
        }

        //print results
        System.out.println("MIN CUT:");
        for (GraphNode.EdgeInfo edge : cuts) {
            System.out.println("Edge (" + edge.from + "," + edge.to + ") transports " +
                    edge.startCapacity + " cases");
        }

    }

}
