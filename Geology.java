package geology;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;
public final class Geology {

    private static final Logger logger = Logger.getLogger(Geology.class.getName());

    private  final EnumMap<Landscape, Runnable> modifications = new EnumMap<>(Landscape.class);

    private int numPoints;

    private final List<Modification> operationsList;


     Geology(int numPoints, List<Modification> operationsList) {
        this.numPoints =  checkNegative(numPoints);
        this.operationsList = operationsList;
    }

    /**
     * This is a getter method for the sequence of operations
     * @return operationsList the list of operations
     */
    public List<Modification> operationsList() {
        return operationsList;
    }

    /**
     * Factory method that should be immutable
     * @param numPoints the points in the graph
     * @param operationsList sequence of modifications within graph
     * @return geology object that is a defensive copy
     */
    public static Geology of(int numPoints, List<Modification> operationsList){
        return new Geology(numPoints, new ArrayList<>(operationsList));
    }

    /**
     * This algorithm will check if the landscape is valid or not
     * It will have a number of points that are in the landscape
     * It will also have a list of modifications that are to be performed on the landscape
     * It will also have a list of final heights that we check to verify if the landscape is correct
     * @param numPoints the number of points in the landscape
     * @param modifications the list of modifications between the points of the landscape and the operations to be performed
     * @param finalHeights  the expected final heights that we check to verify if the landscape is correct
     * @return true if the landscape is valid, false otherwise
     */
    public  boolean isValid(int numPoints, List<Modification> modifications, List<Integer> finalHeights) {

        //checking any invalid data.
        GeologyException.checkNullParams(modifications, finalHeights);
        //check if any specific elements are null.
        GeologyException.checkNullIndexes(modifications, finalHeights);

        //check if the enums within the modifications object are null
        GeologyException.checkNullModification(modifications);

        //check if the number of points is valid
        int actualPoints = checkNumPoints(numPoints, finalHeights);

        return finalHeights.equals(processModifications(actualPoints, modifications, finalHeights));
    }


    //this will compute the list of actual heights of the landscape after all the modifications have been performed
       private List<Integer> processModifications(int numPoints, List<Modification> operationsList, List<Integer> expectedHeights){
        //assertions
        assert operationsList != null : "The list of operations is null";
        assert expectedHeights != null : "The list of expected heights is null";
        assert numPoints == expectedHeights.size() : "The number of points is not equal to the number of expected heights";

        List<Integer> actualHeights = new ArrayList<>(numPoints);

        IntStream.range(0, numPoints).forEach(i -> actualHeights.add(0));

        operationsList.forEach(element -> {
            //change the values to positive if they are negative.
            int x1Val = checkNegative(element.x1());
            int x2Val = checkNegative(element.x2());

            createEnum(actualHeights, x1Val, x2Val);
            modifications.get(element.operation()).run();
        });

        return actualHeights;
    }


    //using a lookup table to perform the modifications
    //this will perform the modifications on the landscape
    //raise the height of the y values.
    private void createEnum(List<Integer> actualHeights, int x1, int x2) {
       //if x1 is greater than x2, clean up data.
        int x1Val = IntPair.checkX1X2(x1, x2).x1();
        int x2Val = IntPair.checkX1X2(x1, x2).x2();

        modifications.put(Landscape.RAISE, () -> raise(actualHeights, x1Val, x2Val));
        modifications.put(Landscape.DEPRESS, () -> depress(actualHeights, x1Val, x2Val));
        modifications.put(Landscape.HILL, () -> hill(actualHeights, x1Val, x2Val));
        modifications.put(Landscape.VALLEY, () -> valley(actualHeights, x1Val, x2Val));
    }


    //check if the vars are negative.
    private int checkNegative(int x) {
        if (x < 0) {
            logger.log(Level.WARNING, "The value cannot be negative");
            return Math.abs(x);
        }
        return x;
    }

    private record IntPair(int x1, int x2){
        public static IntPair checkX1X2(int x1, int x2){
            if(x1 > x2){
                return new IntPair(x2, x1);
            }
            else
                return new IntPair(x1, x2);
        }
    }


    /*
     * check if the number of points on the axis is equal to final heights list size
     */
     private int checkNumPoints(int numPoints, List<Integer> finalHeights) {
         checkNegative(numPoints);

         assert finalHeights != null : "The list of final heights is null";
         assert numPoints >= 0 : "The number of points is negative";

         if(numPoints != finalHeights.size()){
            logger.log(Level.WARNING, "numPoints is not equal to than the size of the finalHeights list");
            return finalHeights.size();
        }
        else
             return numPoints;
    }

    //modifications
    private void raise(List<Integer> landscape, int x1, int x2) {
        assert landscape != null : "The landscape is null";
        assert x1 >= 0 : "The x1 value is negative";
        assert x2 >= 0 : "The x2 value is negative";
        assert x1 <= x2 : "The x1 value is greater than the x2 value";

         for (int i = x1; i <= x2; i++) {
            landscape.set(i, landscape.get(i) + 1);
        }
    }

    private void depress(List<Integer> landscape, int x1, int x2) {
        assert landscape != null : "The landscape is null";
        assert x1 >= 0 : "The x1 value is negative";
        assert x2 >= 0 : "The x2 value is negative";
        assert x1 <= x2 : "The x1 value is greater than the x2 value";

         for (int i = x1; i <= x2; i++) {
            landscape.set(i, landscape.get(i) - 1);
        }
    }

    private void hill(List<Integer> landscape, int x1, int x2) {
        assert landscape != null : "The landscape is null";
        assert x1 >= 0 : "The x1 value is negative";
        assert x2 >= 0 : "The x2 value is negative";
        assert x1 <= x2 : "The x1 value is greater than the x2 value";

        while (x2 - x1 > 1) {
            raise(landscape, x1, x2);
            x1++;
            x2--;
        }
    }

    private void valley(List<Integer> landscape, int x1, int x2) {
        assert landscape != null : "The landscape is null";
        assert x1 >= 0 : "The x1 value is negative";
        assert x2 >= 0 : "The x2 value is negative";
        assert x1 <= x2 : "The x1 value is greater than the x2 value";

         while (x2 - x1 > 1) {
            depress(landscape, x1, x2);
            x1++;
            x2--;
        }

    }

}









