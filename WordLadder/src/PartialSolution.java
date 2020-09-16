public class PartialSolution<String> {
    public String value;
    public PartialSolution<String> next;
    public PartialSolution<String> last;


    public PartialSolution(String value) {
        this.value = value;
    }
}


