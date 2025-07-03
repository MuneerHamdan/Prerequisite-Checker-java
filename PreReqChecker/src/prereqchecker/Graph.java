package prereqchecker;
import java.util.*;

public class Graph {
    private Map<String, List<String>> map;

    Graph(String inputString){
        this.map = new HashMap<>();
        
        StdIn.setFile(inputString);
        int a = StdIn.readInt();
        StdIn.readLine();


        for (int i = 0; i < a; i++){
            map.put(StdIn.readLine(), new ArrayList<>());
        }

        int b = StdIn.readInt();
        StdIn.readLine();


        for (int i = 0; i < b; i++){
            String[] v = StdIn.readLine().split(" ");
            String course = v[0];
            String prereq = v[1];
            map.get(course).add(prereq);
        }
    }

    public int size(){
        return map.size();
    }

    void BFS(String s)
    {
        
    }

    public List<String> getPrereqs(String course){
        return map.get(course);
    }

    public boolean isLinked(String startVertex, String targetVertex) {
        Set<String> visited = new HashSet<>();
        return DFSUtil(startVertex, targetVertex, visited);
    }

    private boolean DFSUtil(String vertex, String targetVertex, Set<String> visited) {
        visited.add(vertex);

        if (vertex.equals(targetVertex)) {
            return true;
        }

        for (String neighbor : map.getOrDefault(vertex, Collections.emptyList())) {
            if (!visited.contains(neighbor)) {
                if (DFSUtil(neighbor, targetVertex, visited)) {
                    return true;
                }
            }
        }

        return false;
    }
    
    public void print(String outputFile){
        StdOut.setFile(outputFile);

        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            String course = entry.getKey();
            List<String> prereqs = entry.getValue();

            StdOut.print(course + " " + String.join(" ", prereqs) + System.lineSeparator());
        }
    }
}
