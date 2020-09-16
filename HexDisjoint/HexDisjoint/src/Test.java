import org.junit.Assert;

public class Test {

    private int NumElements = 110;

    @org.junit.Test
    public void TestUnions() {

        int unions = 5;
        DisjointSet s = new DisjointSet(NumElements);

        for (int j = 1; j < 25; j *= 5) {
            for (int i = 0; i < unions; i++) {
                s.union(unions, i);
            }
            Assert.assertEquals(j * unions, s.find(unions * j));
        }
    }

    @org.junit.Test
    public void TestFind() {

        int finds = 10;
        DisjointSet s = new DisjointSet(NumElements);


        for (int i = 0; i < finds; i++) {
                s.union(i, finds);
            Assert.assertEquals(s.find(i), s.find(finds));
        }
        for (int i = 11; i < NumElements; i *= finds) {
            Assert.assertNotEquals(s.find(i), s.find(finds));
        }
    }

    @org.junit.Test
    public void TestDisjointSet() {

        int NumInSameSet = 11;
        int expected = 0;
        DisjointSet s = new DisjointSet(NumElements);

        for( int k = 0; k < NumElements; k += NumInSameSet ) {
            for( int j = 1; j < NumInSameSet; j++) {
                s.union(k+j, k);
            }
        }

        for( int i = 0; i < NumElements; i++ ) {
            if(i % NumInSameSet == NumInSameSet - 1) {
                Assert.assertEquals(expected*NumInSameSet+1, s.find(i));
                expected++;
            }
        }
    }
}
