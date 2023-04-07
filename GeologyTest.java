package geology;

import java.util.*;
import static org.junit.Assert.*;
import org.junit.Test;


public class GeologyTest {



    @Test
    public void nominalCaseIsValid() {

        //test case 1
        Geology geology = Geology.of(6, List.of(new Modification(1, 7, Landscape.HILL)));

        assertTrue(geology.isValid(6, geology.operationsList(), List.of(0,1,2,3,3,3,2,1)));


        //test case 2
        Geology geology2 = new Geology(7, List.of(new Modification(1, 7, Landscape.HILL), new Modification(1, 7, Landscape.VALLEY)));
        assertTrue(geology2.isValid(7, geology2.operationsList(), List.of(0,0,0,0,0,0,0,0)));

        //test case 3
        Geology geology3 = new Geology(7, List.of(new Modification(1, 7, Landscape.HILL), new Modification(1, 7, Landscape.VALLEY), new Modification(1, 7, Landscape.RAISE)));

        assertTrue(geology3.isValid(7, geology3.operationsList(), List.of(0,1,1,1,1,1,1,1)));

        //test case 4
        Geology geology4 = new Geology(8,List.of(new Modification(1,7,Landscape.DEPRESS)));

        assertTrue(geology4.isValid(8,geology4.operationsList(), List.of(0,-1,-1,-1,-1,-1,-1,-1)));



    }

    @Test
    public void testNullParamsIndexes(){

        Geology geology = new Geology(6, List.of(new Modification(1, 7, Landscape.HILL)));

        //test case 1, inputted null param in modifications.
        assertThrows(IllegalArgumentException.class, () -> geology.isValid(6, null, List.of(0,1,2,3,3,3,2,1)));

        //test case 2, inputted null param in finalHeights.
        Geology geology2 = new Geology(6, List.of(new Modification(1, 7, Landscape.HILL)));
        assertThrows(IllegalArgumentException.class, () -> geology2.isValid(6, geology2.operationsList(), null));

        //test case 3 both null inputs
        Geology geology3 = new Geology(6, List.of(new Modification(1, 7, Landscape.HILL)));
        assertThrows(IllegalArgumentException.class, () -> geology3.isValid(6, null, null));
    }

    @Test
        public void testNullIndexesInModifications(){

        //test case 1, null index in modifications
        Geology geology = new Geology(6, Arrays.asList(new Modification(1, 7, Landscape.HILL), null));

        assertThrows(IllegalArgumentException.class, () -> geology.isValid(6, geology.operationsList(), List.of(0,1,2,3,3,3,2,1)));
    }

    @Test
        public void testNullIndexesInFinalHeights() {
            Geology geology = new Geology(6, List.of(new Modification(1, 7, Landscape.HILL)));
            List<Integer> arr = Arrays.asList(1,2,null);

            Error error = new Error();
            GeologyException err = new GeologyException(error);
            System.out.println(err.error().getMessage());

            assertThrows(IllegalArgumentException.class, () -> geology.isValid(6, geology.operationsList(), arr));

    }

    @Test
        public void testNegativeArgs(){
        //testing neg args in modifications, they go to positive in the list
        Geology geology = new Geology(6, List.of(new Modification(-1, -7, Landscape.RAISE)));
        assertTrue(geology.isValid(6, geology.operationsList(), List.of(0,1,1,1,1,1,1,1)));

        //testing neg arg in numPoints
        Geology geology2 = new Geology(-6, List.of(new Modification(1, 7, Landscape.RAISE)));
        assertTrue(geology.isValid(6, geology2.operationsList(), List.of(0,1,1,1,1,1,1,1)));

    }

    @Test
        public void testX1GreaterThanX2(){
        //testing x1 greater than x2
        Geology geology = new Geology(6, List.of(new Modification(7, 1, Landscape.RAISE)));
        assertTrue(geology.isValid(6, geology.operationsList(), List.of(0,1,1,1,1,1,1,1)));
    }

    @Test
        public void testBoundary(){
        //testing boundary
        Geology geology = new Geology(6, List.of(new Modification(0, 6, Landscape.RAISE)));
        assertTrue(geology.isValid(6, geology.operationsList(), List.of(1,1,1,1,1,1,1)));
    }

    @Test
        public void testX1LessThanZero(){
        //testing x1 less than 0
        Geology geology = new Geology(6, List.of(new Modification(-1, 6, Landscape.RAISE)));
        assertTrue(geology.isValid(6, geology.operationsList(), List.of(0,1,1,1,1,1,1)));
    }

    @Test
        public void testNullOperation(){
        //testing x2 greater than numPoints
        Geology geology = new Geology(6, List.of(new Modification(1, 7, null)));
        assertThrows(IllegalArgumentException.class, () -> geology.isValid(6, geology.operationsList(), List.of(0,1,2,3,3,3,2,1)));


    }

}