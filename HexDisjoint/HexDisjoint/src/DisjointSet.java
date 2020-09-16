import java.util.Arrays;

public class DisjointSet {
    int[] set;

    /**
     * Constructor for the class that will make setSize number of nodes
     * @param setSize number of nodes to be in the set
     */
    public DisjointSet(int setSize){
        set = new int[setSize];

        Arrays.fill(set, -1);
    }

    /**
     * This method takes the two values that needs to be unioned,
     * find their roots and merge their roots depending on their size.
     * @param index1 index of first value to be unioned
     * @param index2 index of second value that needs to be unioned
     */
    public void union(int index1, int index2){
        int root1 = find(index1);
        int root2 = find(index2);
        if (set[root1] > set[root2]){
            set[root1] = root2;
            set[index1] = root2; //tree 2 is deeper so add tree 1 to it
            set[root2]--;
        }
        else {
            set[root2] = root1;
            set[index2] = root1;
            set[root1]--;
        }
    }

    /**
     * This method checks to see if the index is of a root and if it is not
     * it will recurse through till it gets the root. executes path compression
     * @param index index of value in array being verified
     * @return returns the roots index
     */
    public int find(int index){
        int output;
        if(set[index] < 0)
            return index;
        else
            output = find(set[index]);
            if (set[index] != output) {
                set[index] = output;
                set[output]--;
            }
            return output;
    }

    public static void main( String [ ] args )
    {
        int NumElements = 110;
        int NumInSameSet = 11;

        DisjointSet ds = new DisjointSet( NumElements );
        int set1, set2;

        for( int i = 0; i < NumElements; i++ )
        {
            System.out.print( ds.find( i )+ "*" );
            if( i % NumInSameSet == NumInSameSet - 1 )
                System.out.println( );
        }
        for( int k = 0; k < NumElements; k += NumInSameSet ) {
            for( int j = 1; j < NumInSameSet; j++) {
                ds.union(k+j, k);
            }
        }

        for( int i = 0; i < NumElements; i++ )
        {
            System.out.print( ds.find( i )+ "*" );
            if( i % NumInSameSet == NumInSameSet - 1 )
                System.out.println( );
        }
        System.out.println( );
    }
}
