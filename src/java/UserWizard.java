
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.FlowEvent;

@ManagedBean
@ViewScoped
public class UserWizard implements Serializable {

    private boolean skip;
    private boolean cuentaprovedor;
    private int tipocontrato;
    private String[] selectedClausulas;
    private String[] selectedAcuerdos;

    public boolean isSkip() {
        return skip;
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
    }

    public String onFlowProcess(FlowEvent event) {
        if (skip) {
            skip = false;   //reset in case user goes back
            return "confirm";
        } else {
            return event.getNewStep();
        }
    }

    public boolean isCuentaprovedor() {
        return cuentaprovedor;
    }

    public void setCuentaprovedor(boolean cuentaprovedor) {
        this.cuentaprovedor = cuentaprovedor;
    }

    public int getTipocontrato() {
        return tipocontrato;
    }

    public void setTipocontrato(int tipocontrato) {
        this.tipocontrato = tipocontrato;
    }

    public String[] getSelectedClausulas() {
        return selectedClausulas;
    }

    public void setSelectedClausulas(String[] selectedClausulas) {
        this.selectedClausulas = selectedClausulas;
    }

    public String[] getSelectedAcuerdos() {
        return selectedAcuerdos;
    }

    public void setSelectedAcuerdos(String[] selectedAcuerdos) {
        this.selectedAcuerdos = selectedAcuerdos;
    }
    

}
