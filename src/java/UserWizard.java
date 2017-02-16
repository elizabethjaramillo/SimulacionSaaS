
import RedBayesiana.LeerRed;
import RedBayesiana.Parametro;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.openmarkov.core.exception.IncompatibleEvidenceException;
import org.openmarkov.core.exception.InvalidStateException;
import org.openmarkov.core.exception.NonProjectablePotentialException;
import org.openmarkov.core.exception.NotEvaluableNetworkException;
import org.openmarkov.core.exception.ParserException;
import org.openmarkov.core.exception.ProbNodeNotFoundException;
import org.openmarkov.core.exception.UnexpectedInferenceException;
import org.openmarkov.core.exception.WrongCriterionException;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FlowEvent;
import org.primefaces.model.chart.PieChartModel;

@ManagedBean
@ViewScoped
public class UserWizard implements Serializable {

    private PieChartModel livePieModel;
    private double porcentaje;
    private boolean skip;
    private boolean cuentaprovedor=true;
    private String tipocontrato;
    private String[] selectedClausulas;
    private String[] selectedAcuerdos;
    private String[] clausulasAdicionales;
    private String tipoEsquema;
    private String protocolotransferencia;
    private String experiencia;

    public UserWizard() {
//        livePieModel=new PieChartModel();
//        livePieModel.getData().put("Confiabildad", 100);
//        livePieModel.getData().put("No comfiable", 0);
//        livePieModel.setTitle("Confiabilidad Proveedor");
//        livePieModel.setLegendPosition("ne");
    }

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

    public String getTipocontrato() {
        return tipocontrato;
    }

    public void setTipocontrato(String tipocontrato) {
        this.tipocontrato = tipocontrato;
    }

    public String[] getClausulasAdicionales() {
        return clausulasAdicionales;
    }

    public void setClausulasAdicionales(String[] clausulasAdicionales) {
        this.clausulasAdicionales = clausulasAdicionales;
    }

    public String getTipoEsquema() {
        return tipoEsquema;
    }

    public void setTipoEsquema(String tipoEsquema) {
        this.tipoEsquema = tipoEsquema;
    }

    public String getProtocolotransferencia() {
        return protocolotransferencia;
    }

    public void setProtocolotransferencia(String protocolotransferencia) {
        this.protocolotransferencia = protocolotransferencia;
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

    public String getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(String experiencia) {
        this.experiencia = experiencia;
    }

    public void redireccion() {

        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/");
        } catch (IOException ex) {
            Logger.getLogger(UserWizard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void redirect() throws IOException {
        // ...

        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.redirect("http://localhost:8080/SimulacionSaaS/faces/index.html#sec1");
    }

    public void redirect2() throws IOException {
        // ...

        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.redirect("http://localhost:8080/SimulacionSaaS/faces/index.html#sec2");
    }

    public void redirect3() throws IOException {
        // ...

        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.redirect("http://localhost:8080/SimulacionSaaS/faces/index.html#sec3");
    }

    public void sla() throws IOException {
        // ...

        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.redirect("http://localhost:8080/SimulacionSaaS/faces/index.html#sec4");
    }

    public void riesgos() throws IOException {
        // ...

        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.redirect("http://localhost:8080/SimulacionSaaS/faces/index.html#sec5");
    }

    public void vulne() throws IOException {
        // ...

        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.redirect("http://localhost:8080/SimulacionSaaS/faces/index.html#sec6");
    }

    public void navigate() {
        System.out.println("proveedor" + cuentaprovedor);
        if (cuentaprovedor == true) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensaje", "“Para hacer uso del simulador y calcular la confiabilidad del proveedor, debe contar con un posible proveedor”");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }

    public String calcularProbabilidad() {
        List<Parametro> listaParametro = inicializar();
        listaParametro = cambiarEstado(listaParametro, tipocontrato);
        listaParametro = cambiarEstado(listaParametro, tipoEsquema);
        listaParametro = cambiarEstado(listaParametro, protocolotransferencia);
        listaParametro = cambiarEstado(listaParametro, experiencia);
        for (String selectedClausula : selectedClausulas) {
            listaParametro = cambiarEstado(listaParametro, selectedClausula);
        }

        for (String selectedAcuerdo : selectedAcuerdos) {
            listaParametro = cambiarEstado(listaParametro, selectedAcuerdo);
        }
        for (String clausulasAdicionale : clausulasAdicionales) {
            System.out.println(clausulasAdicionale);
            listaParametro = cambiarEstado(listaParametro, clausulasAdicionale);
        }

        LeerRed l = new LeerRed();

        try {
            System.out.println("*****");
            String res = l.LeerArchivo(listaParametro);
            porcentaje = Double.parseDouble(res) * 100;
            // setLivePieModel();
//            RequestContext.getCurrentInstance().update("frmResultado:panelResultado");
//            RequestContext.getCurrentInstance().execute("PF('dialogResultado').show()");
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Confiabilidad Proveedor", "La confiabilidad es:" + res + "<br> Porcentaje:" + porcentaje + "%");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LeerRed.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserException ex) {
            Logger.getLogger(LeerRed.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NonProjectablePotentialException ex) {
            Logger.getLogger(LeerRed.class.getName()).log(Level.SEVERE, null, ex);
        } catch (WrongCriterionException ex) {
            Logger.getLogger(LeerRed.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ProbNodeNotFoundException ex) {
            Logger.getLogger(LeerRed.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotEvaluableNetworkException ex) {
            Logger.getLogger(LeerRed.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IncompatibleEvidenceException ex) {
            Logger.getLogger(LeerRed.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnexpectedInferenceException ex) {
            Logger.getLogger(LeerRed.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidStateException ex) {
            Logger.getLogger(LeerRed.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(UserWizard.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void setLivePieModel() {
        int random1 = (int) porcentaje;
        int random2 = 100 - random1;

        livePieModel.getData().put("Confiabildad", random1);
        livePieModel.getData().put("No comfiable", random2);

        livePieModel.setTitle("Confiabilidad Proveedor");
        livePieModel.setLegendPosition("ne");

    }

    public PieChartModel getLivePieModel() {
        return livePieModel;
    }

    public List<Parametro> inicializar() {
        List<Parametro> listaParametro = new ArrayList<>();
        //Experiencia (solo elegir uno)
        listaParametro.add(new Parametro("anos02", "no"));
        listaParametro.add(new Parametro("anos3_5", "no"));
        listaParametro.add(new Parametro("anos6_10", "no"));
        listaParametro.add(new Parametro("mas10anos", "no"));
        //tipo de contrato (solo elegir uno)
        listaParametro.add(new Parametro("ahesion", "no"));
        listaParametro.add(new Parametro("negociado", "no"));
        //ClausulaContrato
        listaParametro.add(new Parametro("PlanContinuidad", "no"));
        listaParametro.add(new Parametro("TerminacionModificacion", "no"));
        listaParametro.add(new Parametro("Confidencialida", "no"));
        listaParametro.add(new Parametro("Disponibilidad", "no"));
        listaParametro.add(new Parametro("Rendimiento", "no"));
        listaParametro.add(new Parametro("SuspencionS", "no"));
        listaParametro.add(new Parametro("ServicioSoporte", "no"));
        listaParametro.add(new Parametro("Seguridad", "no"));
        listaParametro.add(new Parametro("Privacidad", "no"));
        listaParametro.add(new Parametro("Pagos", "no"));
        //acuerdo de nivel de servicio

        //CuestinamientoAdicional
        listaParametro.add(new Parametro("RequerimientoContraseña", "no"));
        listaParametro.add(new Parametro("RestrigirNIntento", "no"));
        listaParametro.add(new Parametro("PalabraClave", "no"));
        listaParametro.add(new Parametro("CifradoSSL", "no"));
        listaParametro.add(new Parametro("MAC", "no"));
        listaParametro.add(new Parametro("PreguntaSeguridad", "no"));
        listaParametro.add(new Parametro("ListaBlancaURL", "no"));
        listaParametro.add(new Parametro("OTP", "no"));
        listaParametro.add(new Parametro("SSP", "no"));
        //esquema bd (solo elegir uno)
        listaParametro.add(new Parametro("STSI", "no"));
        listaParametro.add(new Parametro("ITSI", "no"));
        listaParametro.add(new Parametro("IDII", "no"));
        //transferencia de datos (solo elegir uno)
        listaParametro.add(new Parametro("HTTP", "no"));
        listaParametro.add(new Parametro("HTTPS", "no"));
        listaParametro.add(new Parametro("TLS", "no"));
        listaParametro.add(new Parametro("SSH", "no"));
        listaParametro.add(new Parametro("recuperacionDatos", "no"));
        listaParametro.add(new Parametro("ViabilidadLargoPlazo", "no"));
        listaParametro.add(new Parametro("AccesoUsuarioPrivilegiado", "no"));
        listaParametro.add(new Parametro("UbicacionDatos", "no"));

        //CuestinamientoAdicional
        listaParametro.add(new Parametro("RequerimientoContraseña", "no"));
        listaParametro.add(new Parametro("RestrigirNIntento", "no"));
        listaParametro.add(new Parametro("PalabraClave", "no"));
        listaParametro.add(new Parametro("CifradoSSL", "no"));
        listaParametro.add(new Parametro("MAC", "no"));
        listaParametro.add(new Parametro("PreguntaSeguridad", "no"));
        listaParametro.add(new Parametro("ListaBlancaURL", "no"));
        listaParametro.add(new Parametro("OTP", "no"));
        listaParametro.add(new Parametro("SSP", "no"));
        //esquema bd (solo elegir uno)
        listaParametro.add(new Parametro("STSI", "no"));
        listaParametro.add(new Parametro("ITSI", "no"));
        listaParametro.add(new Parametro("IDII", "no"));
        //transferencia de datos (solo elegir uno)
        listaParametro.add(new Parametro("HTTP", "no"));
        listaParametro.add(new Parametro("HTTPS", "no"));
        listaParametro.add(new Parametro("TLS", "no"));
        listaParametro.add(new Parametro("SSH", "no"));
        return listaParametro;
    }

    public List<Parametro> cambiarEstado(List<Parametro> lista, String parametro) {
        for (int i = 0; i < lista.size(); i++) {
            Parametro p = lista.get(i);
            if (p.getNombre().equals(parametro)) {
                p.setEstado("yes");
                lista.set(i, p);
                System.out.println("se cambio");
            }
        }
        return lista;
    }

}
