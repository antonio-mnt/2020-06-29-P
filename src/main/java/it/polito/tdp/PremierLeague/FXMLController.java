/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.PremierLeague;

import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.PremierLeague.model.Arco;
import it.polito.tdp.PremierLeague.model.Match;
import it.polito.tdp.PremierLeague.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	Model model;
	private boolean flag = false;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnConnessioneMassima"
    private Button btnConnessioneMassima; // Value injected by FXMLLoader

    @FXML // fx:id="btnCollegamento"
    private Button btnCollegamento; // Value injected by FXMLLoader

    @FXML // fx:id="txtMinuti"
    private TextField txtMinuti; // Value injected by FXMLLoader

    @FXML // fx:id="cmbMese"
    private ComboBox<Month> cmbMese; // Value injected by FXMLLoader

    @FXML // fx:id="cmbM1"
    private ComboBox<Match> cmbM1; // Value injected by FXMLLoader

    @FXML // fx:id="cmbM2"
    private ComboBox<Match> cmbM2; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doConnessioneMassima(ActionEvent event) {
    	
    	if(this.flag==false) {
    		this.txtResult.setText("Devi creare prima il grafo\n");
    		return;
    	}
    	
    	this.txtResult.setText("Coppie con connessione massima:\n\n");
    	
    	for(Arco a: this.model.trovaConnessioneMax()) {
    		this.txtResult.appendText(a.toString());
    	}
    	
    	
    	
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	
    	int minuti;
    	
    	try {
    		
    	    minuti = Integer.parseInt(this.txtMinuti.getText());
    	    	    		
    	}catch(NumberFormatException ne) {
    	    this.txtResult.setText("Formato minuti errato\n");
    	    return;
    	}
    	
    	if(minuti<0) {
    		this.txtResult.setText("Il numero di minuti deve essere positivo\n");
    		return;
    	}
    	
    	Month mese = this.cmbMese.getValue();
    	
    	if(mese==null) {
    		this.txtResult.setText("Seleziona prima un mese\n");
    		return;
    	}
    	
    	this.model.creaGrafo(minuti, mese.getValue());
    	
    	this.txtResult.setText("Grafo creato\n#VERTICI: "+this.model.getNumeroVertici()+"\n#ARCHI: "+this.model.getNumeroArchi()+"\n");
    	
    	this.cmbM1.getItems().setAll(this.model.getVertici());
    	this.cmbM2.getItems().setAll(this.model.getVertici());
    	
    	this.flag = true;
    	
    }

    @FXML
    void doCollegamento(ActionEvent event) {
    	
    	if(this.flag==false) {
    		this.txtResult.setText("Devi creare prima il grafo\n");
    		return;
    	}
    	
    	Match m1 = this.cmbM1.getValue();
    	Match m2 = this.cmbM2.getValue();
    	
    	if(m1==null || m2==null) {
    		this.txtResult.setText("Devi selezionare tutte e due le squadre\n");
    		return;
    	}
    	
    	if(this.model.controllaSquadre(m1, m2)==false) {
    		this.txtResult.setText("Non puoi selezionare due match dove giocano le stesse squadre\n");
    		return;
    	}
    	
    	this.model.trovaPercorso(m1, m2);
    	
    	if(this.model.getBest()==null) {
    		this.txtResult.setText("Non è stato possibile trovare un percorso\n");
    		return;
    	}
    	
    	this.txtResult.setText("Peso: "+this.model.getBestPeso()+"\n");
    	
    	for(Match m: this.model.getBest()) {
    		this.txtResult.appendText(m+"\n");
    	}
    	
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnConnessioneMassima != null : "fx:id=\"btnConnessioneMassima\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCollegamento != null : "fx:id=\"btnCollegamento\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtMinuti != null : "fx:id=\"txtMinuti\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbMese != null : "fx:id=\"cmbMese\" was not injected: check your FXML file 'Scene.fxml'.";        assert cmbM1 != null : "fx:id=\"cmbM1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbM2 != null : "fx:id=\"cmbM2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	
    	for(int i = 1; i<=12; i++) {
    		this.cmbMese.getItems().add(Month.of(i));
    		
    	}
    	
  
    }
    
    
}
