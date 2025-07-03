package prereqchecker;
import java.util.*;

public class CoursePrerequisites{
    private int V;
    private int E;
    private ArrayList<LinkedList<String>> prereqs;

    CoursePrerequisites(String inputfile, String outputfile){

        StdIn.setFile(inputfile);
        int v = StdIn.readInt();
        StdIn.readLine();

        this.V = v;
        ArrayList<String> courses = new ArrayList<>();

        for (int i = 0; i < V; i++){
            courses.add(StdIn.readLine());
        }
        
        this.E = StdIn.readInt();
        StdIn.readLine();

        ArrayList<String> prereqarray = new ArrayList<>();

        for (int i = 0; i < E; i++){
            prereqarray.add(StdIn.readLine());
        }

        prereqs = new ArrayList<LinkedList<String>>();

        String[] b = prereqarray.get(0).split(" ");
        String n = b[0];
        String m = b[1];
        prereqs.add(new LinkedList<>());
        prereqs.get(0).add(n);
        prereqs.get(0).add(m);

        for (int i = 1; i < E; i++){
            String[] z = prereqarray.get(i).split(" ");
            String course = z[0];
            String prereq = z[1];
            boolean x = false;
            for (int j = 0; j < prereqs.size(); j++){
                if (prereqs.get(j).getFirst().equalsIgnoreCase(course)){
                    prereqs.get(j).add(prereq);
                    x = true;
                    break;

                }
                else {x = false;}
            }
                    boolean f = false;

            if (x == false){
                    prereqs.add(new LinkedList<>());
                    prereqs.get(prereqs.size() - 1).add(course);
                    prereqs.get(prereqs.size() - 1).add(prereq);
            }
            
            for (int j = 0; j < prereqs.size(); j++){
                if (prereqs.get(j).getFirst().equalsIgnoreCase(prereq)){
                    f = true;
                    break;
                }
                else {f = false;}
            }

            if (f == false){
                prereqs.add(new LinkedList<>());
                prereqs.get(prereqs.size() - 1).add(prereq);
            }
        }

        StdOut.setFile(outputfile);
        for (int i = 0; i < prereqs.size(); i++){
            ListIterator<String> t = prereqs.get(i).listIterator();
            ArrayList<String> s = new ArrayList<String>();
            while (t.hasNext()){
                s.add(t.next().toString());
            }
            String d = s.get(0);
            for (int j = 1; j < s.size(); j++){
                d = d + " " + s.get(j);
            }
            StdOut.println(d);
        }
        StdOut.close();
    }

    public void valid(String inputfile, String outputfile){
        StdIn.setFile(inputfile);
        String c1 = StdIn.readLine();
        String c2 = StdIn.readLine();


        for (int i = 0; i < V; i++){
            if (prereqs.get(i).contains(c1)){
                if (prereqs.get(i).getFirst().equalsIgnoreCase(c1)){
                    prereqs.get(i).add(c2);
                    break;
                }
            }

        }

        StdOut.setFile(outputfile);

        if (this.hasCycle()) {
            StdOut.println("NO");
        } else {
            StdOut.println("YES");
        }
    }

    public void eligible(String inputfile, String outputfile) {
        ArrayList<String> targets = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        ArrayList<String> advance = new ArrayList<>();

        readTargetsAndAdvance(inputfile, targets, advance, visited);

        ArrayList<String> pres = new ArrayList<>();
        for (String target : targets) {
            pres.addAll(getprereqsMain(target));
            pres.add(target);
        }

        for (String prereq : pres) {
            advance.addAll(getadvanced(prereq));
        }

        Set<String> uniquepre = new HashSet<>(pres);
        pres.clear();
        pres.addAll(uniquepre);

        Set<String> uniqueAdv = new HashSet<>(advance);
        advance.clear();
        advance.addAll(uniqueAdv);

        advance.removeAll(pres);

        ArrayList<String> result = finalClasses(pres, advance);

        writeResult(outputfile, result);
    }

    private void readTargetsAndAdvance(String inputfile, ArrayList<String> targets, ArrayList<String> advance, Set<String> visited) {
        StdIn.setFile(inputfile);
        int n = StdIn.readInt();
        StdIn.readLine();

        for (int i = 0; i < n; i++) {
            String course = StdIn.readLine();
            targets.add(course);

            ArrayList<String> z = getadvanced(course);
            for (String advancedCourse : z) {
                if (!visited.contains(advancedCourse)) {
                    visited.add(advancedCourse);
                    advance.add(advancedCourse);
                }
            }
        }
    }


    public ArrayList<String> finalClasses(ArrayList<String> pres, ArrayList<String> adv) {
        ArrayList<String> finale = new ArrayList<>();
    
        for (int i = 0; i < adv.size(); i++){
            for (int j = 0; j < prereqs.size(); j++){
                boolean x = true;;
                if (prereqs.get(j).getFirst().equalsIgnoreCase(adv.get(i))){
                    ListIterator<String> t = prereqs.get(j).listIterator();
                                            t.next();

                    while (t.hasNext()){
                            if (!pres.contains(t.next().toString())){
                                x = false;
                                break;
                            }

                    }
                    if (x == true){
                        finale.add(adv.get(i));
                        break;
                    }
                    else {
                        break;
                    }
                }

            }
        }


    
        return finale;
    }
    

    private void writeResult(String outputfile, ArrayList<String> result) {
        StdOut.setFile(outputfile);
        for (String course : result) {
            StdOut.println(course);
        }
        StdOut.close();
    }


    public ArrayList<String> getprereqsMain(String target) {
        Set<String> visited = new HashSet<>();
        ArrayList<String> result = new ArrayList<>();
    
        getprereqsMainRecursive(target, visited, result);
    
        return result;
    }
    
    private void getprereqsMainRecursive(String target, Set<String> visited, ArrayList<String> result) {
        if (!visited.contains(target)) {
            visited.add(target);
    
            for (LinkedList<String> prereqList : prereqs) {
                String course = prereqList.getFirst();
                
                if (course.equalsIgnoreCase(target)) {
                    ListIterator<String> iterator = prereqList.listIterator();
                    iterator.next(); 
    
                    while (iterator.hasNext()) {
                        String prereq = iterator.next();
                        result.add(prereq);
                        getprereqsMainRecursive(prereq, visited, result);
                    }
                    break;
                }
            }
        }
    }
    
    

    public ArrayList<String> getadvanced(String target) {
        ArrayList<String> advanced = new ArrayList<>();
    
        for (LinkedList<String> prereqList : prereqs) {
            String course = prereqList.getFirst();
    
            if (prereqList.contains(target) && !course.equalsIgnoreCase(target)) {
                advanced.add(course);
            }
        }
    
        return advanced;
    }
    

    public void need(String inputfile, String outputfile){
        StdIn.setFile(inputfile);
        String target = StdIn.readLine();

        int n = StdIn.readInt();
        StdIn.readLine();

        ArrayList<String> taken = new ArrayList<>();
        for (int i = 0; i < n; i++) taken.add(StdIn.readLine());

        ArrayList<String> pres = new ArrayList<>();
        for (int i = 0; i < taken.size(); i++){
            pres.addAll(getprereqsMain(taken.get(i)));
        }
        ArrayList<String> prereqs = new ArrayList<>();
            pres.addAll(taken);

        for (String s : pres) if (!prereqs.contains(s)) prereqs.add(s);
        

        ArrayList<String> adv = new ArrayList<>();
        for (int i = 0; i < taken.size(); i++){

            adv.addAll(getadvanced(taken.get(i)));
        }

        for (int i = 0; i < prereqs.size(); i++){
            adv.addAll(getadvanced(prereqs.get(i)));
        }

        adv.removeAll(prereqs);
        adv.removeAll(taken);


        ArrayList<String> advanced = new ArrayList<>();
        for (String s : adv) if (!advanced.contains(s)) advanced.add(s);


        ArrayList<String> t = getprereqsMain(target);
        ArrayList<String> w = new ArrayList<>();
        for (String s : t) if (!w.contains(s)) w.add(s);

        w.removeAll(prereqs);

        
        
        StdOut.setFile(outputfile);
        for (String s : w) StdOut.println(s);
    }

    private boolean detectCycle(){
        Set<String> visited = new HashSet<>();

        for (LinkedList<String> prereqList : prereqs){
            String course = prereqList.getFirst();
            if (!visited.contains(course) && dfs(course, visited)){
                return true;
            }
        }

        return false;
    }

    private boolean dfs(String node, Set<String> visited){
        if (visited.contains(node)){
            return true;
        }

        visited.add(node);

        for (String neighbor : getadvanced(node)){
            if (dfs(neighbor, visited)){
                return true;
            }
        }

        visited.remove(node);
        return false;
    }

    public boolean hasCycle(){
        return detectCycle();
    }

    public static void main(String[] args){
        
        // CoursePrerequisites graph = new CoursePrerequisites(args[0], "adjlist.out");

        // // graph.valid("validprereq.in", "validprereq.out");

        // graph.eligible(args[1], args[2]);

        // graph.need("needtotake.in", "needtotake.out");


    }
}