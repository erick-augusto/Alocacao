package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import model.Afinidades;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SelectableDataModel;
import org.primefaces.model.SortOrder;

public class AfinidadesLazyModel extends LazyDataModel<Afinidades> implements SelectableDataModel<Afinidades>{

    private List<Afinidades> datasource;

    public AfinidadesLazyModel(List<Afinidades> datasource) {
        this.datasource = datasource;
    }

    @Override
    public Afinidades getRowData(String rowKey) {
        for (Afinidades afinidade : datasource) {
            if (afinidade.getId().equals(rowKey)) {
                return afinidade;
            }
        }

        return null;
    }

    @Override
    public Object getRowKey(Afinidades afinidade) {
        return afinidade.getId();
    }

    @Override
    public List<Afinidades> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        List<Afinidades> data = new ArrayList<>();

        //filter
        for (Afinidades afinidade : datasource) {
            boolean match = true;

            if (filters != null) {
                for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
                    try {
                        String filterProperty = it.next();
                        Object filterValue = filters.get(filterProperty);
                        String fieldValue = String.valueOf(afinidade.getClass().getField(filterProperty).get(afinidade));

                        if (filterValue == null || fieldValue.startsWith(filterValue.toString())) {
                            match = true;
                        } else {
                            match = false;
                            break;
                        }
                    } catch (Exception e) {
                        match = false;
                    }
                }
            }

            if (match) {
                data.add(afinidade);
            }
        }

        //sort
        if (sortField != null) {
            Collections.sort(data, new AfinidadesLazySorter(sortField, sortOrder));
        }

        //rowCount
        int dataSize = data.size();
        this.setRowCount(dataSize);

        //paginate
        if (dataSize > pageSize) {
            try {
                return data.subList(first, first + pageSize);
            } catch (IndexOutOfBoundsException e) {
                return data.subList(first, first + (dataSize % pageSize));
            }
        } else {
            return data;
        }
    }
}
