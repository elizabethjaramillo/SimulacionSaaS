/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RedBayesiana;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openmarkov.core.exception.IncompatibleEvidenceException;
import org.openmarkov.core.exception.InvalidStateException;
import org.openmarkov.core.exception.NonProjectablePotentialException;
import org.openmarkov.core.exception.NotEvaluableNetworkException;
import org.openmarkov.core.exception.ParserException;
import org.openmarkov.core.exception.ProbNodeNotFoundException;
import org.openmarkov.core.exception.UnexpectedInferenceException;
import org.openmarkov.core.exception.WrongCriterionException;
import org.openmarkov.core.inference.InferenceAlgorithm;
import org.openmarkov.core.model.network.EvidenceCase;
import org.openmarkov.core.model.network.Finding;
import org.openmarkov.core.model.network.ProbNet;
import org.openmarkov.core.model.network.Util;
import org.openmarkov.core.model.network.Variable;
import org.openmarkov.core.model.network.potential.TablePotential;
import org.openmarkov.inference.variableElimination.VariableElimination;
import org.openmarkov.io.probmodel.PGMXReader;

/**
 *
 * @author Yeferson
 */
public class LeerRed implements Serializable {

    final public static List<String> s = new ArrayList<String>();
    final private String bayesNetworkName = "confiabilidadP.pgmx";

    public String LeerArchivo(List<Parametro> listaParametro) throws FileNotFoundException, ParserException, NonProjectablePotentialException, WrongCriterionException, ProbNodeNotFoundException, NotEvaluableNetworkException, IncompatibleEvidenceException, UnexpectedInferenceException, InvalidStateException {
        String parametro1 = listaParametro.get(0).getNombre();
        //InputStream file = new FileInputStream(new File(System.getProperty("user.dir") + "/src/java/archivo/" + nombrered));
        InputStream file = new FileInputStream(new File("/home/glassfish/" + bayesNetworkName));
        PGMXReader reader = new PGMXReader();
        ProbNet prob = reader.loadProbNet(file, parametro1).getProbNet();
//        ProbNet prob1 = reader.loadProbNet(file, "ahesion").getProbNet();

        EvidenceCase evidence = new EvidenceCase();
        try {

            //agregar los parameetros y estados de  la Red Bayesiana
            for (int i = 0; i < listaParametro.size(); i++) {
                evidence.addFinding(prob, listaParametro.get(i).getNombre(), listaParametro.get(i).getEstado());
            }

        } catch (InvalidStateException ex) {
            Logger.getLogger(LeerRed.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IncompatibleEvidenceException ex) {
            Logger.getLogger(LeerRed.class.getName()).log(Level.SEVERE, null, ex);
        }
        InferenceAlgorithm variableElimination = new VariableElimination(prob);
        variableElimination.setPreResolutionEvidence(evidence);
        Variable estado = prob.getVariable("ConfiabilidadProveedor");
        ArrayList<Variable> variablesOfInterest = new ArrayList<Variable>();
        variablesOfInterest.add(estado);

        HashMap<Variable, TablePotential> posteriorProbabilities = variableElimination.getProbsAndUtilities();
        System.out.println("estado" + posteriorProbabilities);
        System.out.println("estado");

        return printResults(evidence, variablesOfInterest, posteriorProbabilities);

    }

    public String printResults(EvidenceCase evidence, ArrayList<Variable> variablesOfInterest,
            HashMap<Variable, TablePotential> posteriorProbabilities) {
        String resultados = "";
        for (Finding finding : evidence.getFindings()) {
            //resultados += "1:  " + finding.getVariable() + ": ";
           
            s.add(String.valueOf(finding.getVariable()));
            s.add(finding.getState());            
            //resultados += finding.getState();
        }
        for (Variable variable : variablesOfInterest) {
            double value;
            TablePotential posteriorProbabilitiesPotential = posteriorProbabilities.get(variable);
            //resultados += " 2:  " + variable + ": ";
            int stateIndex = -1;
            try {
                stateIndex = variable.getStateIndex("yes");
                value = posteriorProbabilitiesPotential.values[stateIndex];
                s.add(String.valueOf(Util.roundedString(value, "0.00001")));
                // s.add(sss);
                resultados += Util.roundedString(value, "0.0001");
            } catch (InvalidStateException e) {
                System.err.println("State \"present\" not found for variable \""
                        + variable.getName() + "\".");
                e.printStackTrace();
            }
        }
        return resultados;
    }

    public static void main(String[] args) {
        List<Parametro> listaParametro = new ArrayList<>();
        //Experiencia (solo elegir uno)
        listaParametro.add(new Parametro("anos02", "no"));
        listaParametro.add(new Parametro("anos3_5", "no"));
        listaParametro.add(new Parametro("anos6_10", "no"));
        listaParametro.add(new Parametro("mas10anos", "yes"));
        //tipo de contrato (solo elegir uno)
        listaParametro.add(new Parametro("ahesion", "no"));
        listaParametro.add(new Parametro("negociado", "yes"));
        //ClausulaContrato
        listaParametro.add(new Parametro("PlanContinuidad", "yes"));
        listaParametro.add(new Parametro("TerminacionModificacion", "yes"));
        listaParametro.add(new Parametro("Confidencialida", "yes"));
        listaParametro.add(new Parametro("Disponibilidad", "yes"));
        listaParametro.add(new Parametro("Rendimiento", "yes"));
        listaParametro.add(new Parametro("SuspencionS", "yes"));
        listaParametro.add(new Parametro("ServicioSoporte", "yes"));
        listaParametro.add(new Parametro("Seguridad", "yes"));
        listaParametro.add(new Parametro("Privacidad", "yes"));
        listaParametro.add(new Parametro("Pagos", "yes"));
        //acuerdo de nivel de servicio
        listaParametro.add(new Parametro("recuperacionDatos", "yes"));
        listaParametro.add(new Parametro("ViabilidadLargoPlazo", "yes"));
        listaParametro.add(new Parametro("AccesoUsuarioPrivilegiado", "yes"));
        listaParametro.add(new Parametro("UbicacionDatos", "yes"));

        //CuestinamientoAdicional
        listaParametro.add(new Parametro("RequerimientoContraseña", "yes"));
        listaParametro.add(new Parametro("RestrigirNIntento", "yes"));
        listaParametro.add(new Parametro("PalabraClave", "yes"));
        listaParametro.add(new Parametro("CifradoSSL", "yes"));
        listaParametro.add(new Parametro("MAC", "yes"));
        listaParametro.add(new Parametro("PreguntaSeguridad", "yes"));
        listaParametro.add(new Parametro("ListaBlancaURL", "yes"));
        listaParametro.add(new Parametro("OTP", "yes"));
        listaParametro.add(new Parametro("SSP", "yes"));
        //esquema bd (solo elegir uno)
        listaParametro.add(new Parametro("STSI", "no"));
        listaParametro.add(new Parametro("ITSI", "no"));
        listaParametro.add(new Parametro("IDII", "yes"));
        //transferencia de datos (solo elegir uno)
        listaParametro.add(new Parametro("HTTP", "no"));
        listaParametro.add(new Parametro("HTTPS", "no"));
        listaParametro.add(new Parametro("TLS", "no"));
        listaParametro.add(new Parametro("SSH", "yes"));                

        LeerRed l = new LeerRed();

        try {
            System.out.println("*****");
            System.out.println(l.LeerArchivo(listaParametro));
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
        }
    }
}
