package util;

import java.util.List;
import javax.faces.model.ListDataModel;
import model.TurmasPlanejamento;
import org.primefaces.model.SelectableDataModel;

public class TurmasPlanejamentoDataModel extends ListDataModel implements SelectableDataModel<TurmasPlanejamento> {

    public TurmasPlanejamentoDataModel() {
    }

    public TurmasPlanejamentoDataModel(List<TurmasPlanejamento> data) {
        super(data);
    }

    @Override
    public TurmasPlanejamento getRowData(String rowKey) {
        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data  

        List<TurmasPlanejamento> turmas = (List<TurmasPlanejamento>) getWrappedData();

        for (TurmasPlanejamento turma : turmas) {
            if (turma.getID().equals(rowKey)) {
                return turma;
            }
        }

        return null;
    }

    @Override
    public Object getRowKey(TurmasPlanejamento turma) {
        return turma.getID();
    }

}
