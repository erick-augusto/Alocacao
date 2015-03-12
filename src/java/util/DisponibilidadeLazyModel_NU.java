/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import model.Disponibilidade;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SelectableDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author vinivcp
 */
public class DisponibilidadeLazyModel_NU extends LazyDataModel<Disponibilidade> implements SelectableDataModel<Disponibilidade>{

    private List<Disponibilidade> datasource;

    public DisponibilidadeLazyModel_NU(List<Disponibilidade> datasource) {
        this.datasource = datasource;
    }

    @Override
    public Disponibilidade getRowData(String rowKey) {
        for (Disponibilidade disponibilidade : datasource) {
            if (disponibilidade.getId().equals(rowKey)) {
                return disponibilidade;
            }
        }

        return null;
    }

    @Override
    public Object getRowKey(Disponibilidade disponibilidade) {
        return disponibilidade.getId();
    }

    @Override
    public List<Disponibilidade> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        List<Disponibilidade> data = new ArrayList<>();

        //filter
        for (Disponibilidade disponibilidade : datasource) {
            boolean match = true;

            if (filters != null) {
                for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
                    try {
                        String filterProperty = it.next();
                        Object filterValue = filters.get(filterProperty);
                        String fieldValue = String.valueOf(disponibilidade.getClass().getField(filterProperty).get(disponibilidade));

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
                data.add(disponibilidade);
            }
        }

        //sort
        if (sortField != null) {
            Collections.sort(data, new DisponibilidadeLazySorter_NU(sortField, sortOrder));
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

