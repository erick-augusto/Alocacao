package util;

import java.lang.reflect.Field;
import java.util.Comparator;
import model.OfertaDisciplina;
import org.primefaces.model.SortOrder;

public class OfertaDisciplinaLazySorter implements Comparator<OfertaDisciplina> {

    private final String sortField;

    private final SortOrder sortOrder;

    /**
     * initializing sorting field and sorting order
     * @param sortField
     * @param sortOrder
     */
    public OfertaDisciplinaLazySorter(String sortField, SortOrder sortOrder) {
        this.sortField = sortField;
        this.sortOrder = sortOrder;
    }

    /**
     * Comparing object1 and object2 with reflection
     * @param oferta1
     * @param oferta2
     * @return
     */
    @Override
    public int compare(OfertaDisciplina oferta1, OfertaDisciplina oferta2) {
        try {
            Field field1 = oferta1.getClass().getDeclaredField(this.sortField);
            Field field2 = oferta2.getClass().getDeclaredField(this.sortField);
            field1.setAccessible(true);
            field2.setAccessible(true);
            Object value1 = field1.get(oferta1);
            Object value2 = field2.get(oferta2);

            int value = ((Comparable)value1).compareTo(value2);
            return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
        }
        catch(NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException();
        }
    }
}