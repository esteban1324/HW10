package geology;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class GeologyException extends Exception {

    private static final Logger logger = Logger.getLogger(GeologyException.class.getName());

    private Error error;

    public GeologyException(Error error) {
        this.error = error;
    }

    public Error error() {
        return error;
    }

    /**
     * Checks if the parameters are null
     *
     * @param modifications modifications list
     * @param finalHeights  final heights list
     * @throws IllegalArgumentException if the arguments are null
     */
    public static void checkNullParams(List<Modification> modifications, List<Integer> finalHeights) throws IllegalArgumentException {
        if (modifications == null || finalHeights == null) {
            logger.log(Level.SEVERE, "modifications is null");
            logger.log(Level.SEVERE, "actualHeights is null");
            throw new IllegalArgumentException("The arguments are null because of the modifications or actualHeights");
        }
    }

    /**
     * This will check if there are any null indexes in the list of modifications and final heights
     * @param modifications modifications list
     * @param finalHeights  final heights list
     * @throws IllegalArgumentException if the list indexes in either list is null
     */
    public static void checkNullIndexes(List<Modification> modifications, List<Integer> finalHeights) throws IllegalArgumentException {
        checkNullParams(modifications, finalHeights);

        for (Modification modification : modifications) {
            if (modification == null) {
                logger.log(Level.SEVERE, "modifications contains null indexes");
                throw new IllegalArgumentException("The list of modifications contains null indexes");
            }
        }

        finalHeights.forEach(height -> {
            if (height == null) {
                logger.log(Level.SEVERE, "finalHeights contains null indexes");
                throw new IllegalArgumentException("The list of finalHeights contains null indexes");
            }
        });
    }

    /**
     * This method checks specifically for the enum if it's null inside the list of the record objects.
     * @param modification the list that holds the x coordinates and the enum operations.
     * @throws IllegalArgumentException when there is a null enum operation.
     */
    public static void checkNullModification(List<Modification> modification) throws IllegalArgumentException {
        //we stream the list and try to gather all the enums with the list containing record objects
        //we then make a check to see if the enums are null and if they, for each null enum inside the record object,
        //we will throw a severe message and illegal argument exception
        modification.stream().map(Modification::operation).filter(operation -> operation == null).forEach(operation -> {
            logger.log(Level.SEVERE, "modification is null");
            throw new IllegalArgumentException("The modification is null");
        });
    }
}








