package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import model.Turma;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SelectableDataModel;
import org.primefaces.model.SortOrder;

public class TurmasPlanejamentoLazyModel extends LazyDataModel<Turma> implements SelectableDataModel<Turma>{

    private List<Turma> datasource;

    public TurmasPlanejamentoLazyModel(List<Turma> datasource) {
        this.datasource = datasource;
    }

    @Override
    public Turma getRowData(String rowKey) {
        for (Turma turma : datasource) {
            if (turma.getID().equals(rowKey)) {
                return turma;
            }
        }

        return null;
    }

    @Override
    public Object getRowKey(Turma turma) {
        return turma.getID();
    }

    @Override
    public List<Turma> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        List<Turma> data = new ArrayList<>();

        //filter
        for (Turma turma : datasource) {
            boolean match = true;

            if (filters != null) {
                for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
                    try {
                        String filterProperty = it.next();
                        Object filterValue = filters.get(filterProperty);
                        String fieldValue = String.valueOf(turma.getClass().getField(filterProperty).get(turma));

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
                data.add(turma);
            }
        }

        //sort
        if (sortField != null) {
            Collections.sort(data, new TurmasPlanejamentoLazySorter(sortField, sortOrder));
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
