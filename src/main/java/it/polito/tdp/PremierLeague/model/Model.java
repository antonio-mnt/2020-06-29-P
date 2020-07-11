package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	private PremierLeagueDAO dao;
	private SimpleWeightedGraph<Match, DefaultWeightedEdge> grafo;
	private List<Match> vertici;
	private Map<Integer,Match> idMap;
	private List<Arco> archi;
	private List<Match> best;
	private double bestPeso;
	
	public Model() {
		dao = new PremierLeagueDAO();
	}
	
	
	public void creaGrafo(int min, int mese) {
		
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		this.vertici = new ArrayList<>(this.dao.listAllMatches(mese));
		
		Graphs.addAllVertices(this.grafo, this.vertici);
		
		this.idMap = new HashMap<>();
		
		for(Match m: this.vertici) {
			this.idMap.put(m.getMatchID(), m);
		}
		
		this.archi = new ArrayList<>(this.dao.listAllArchi(min, mese, idMap));
		
		for(Arco a: this.archi) {
			Graphs.addEdge(this.grafo, a.getM1(), a.getM2(), a.getPeso());
		}
		
		
	}
	
	

public int getNumeroVertici() {
	return this.grafo.vertexSet().size();
}
	
	
public int getNumeroArchi() {
	return this.grafo.edgeSet().size();
}


public List<Arco> trovaConnessioneMax() {
	
	List<Arco> connessi  = new ArrayList<>();
	double giocatori = 0;
	
	for(DefaultWeightedEdge e: this.grafo.edgeSet()) {
		
		double peso = this.grafo.getEdgeWeight(e);
		
		if(peso>giocatori) {
			giocatori = peso;
			connessi.clear();
			connessi.add(new Arco(this.grafo.getEdgeSource(e),this.grafo.getEdgeTarget(e),giocatori));
		}else if(peso==giocatori) {
			connessi.add(new Arco(this.grafo.getEdgeSource(e),this.grafo.getEdgeTarget(e),giocatori));
		}
		
		
	}
	
	return connessi;
	
}


public List<Match> getVertici() {
	return vertici;
}



public void trovaPercorso(Match partenza, Match destinazione) {
	
	List<Match> parziale = new ArrayList<>();
	this.best = null;
	this.bestPeso = 0;
	parziale.add(partenza);
	
	ricorsione(parziale,destinazione,0);
}


private void ricorsione(List<Match> parziale, Match destinazione, int livello) {
	
	
	if(parziale.size()>1) {
		if(controllaSquadre(parziale.get(parziale.size()-2),parziale.get(parziale.size()-1))==false) {
			return;
		}
	}
	
	
	if(parziale.get(parziale.size()-1).equals(destinazione)) {
		double pesoP = calcolaPeso(parziale);
		if(pesoP>this.bestPeso) {
			this.best = new ArrayList<>(parziale);
			this.bestPeso = pesoP;
		}
		return;
	}
	
	for(Match vicino : Graphs.neighborListOf(this.grafo, parziale.get(parziale.size() -1 ))) {
		if(!parziale.contains(vicino)) {
			parziale.add(vicino);
			this.ricorsione(parziale,destinazione, livello+1);
			parziale.remove(parziale.size() -1);
		}
	}
	
	
}


public boolean controllaSquadre(Match match, Match match2) {
	
	if((match.getTeamHomeID()==match2.getTeamHomeID() && match.getTeamAwayID()==match2.getTeamAwayID())  || 
		(match.getTeamHomeID()==match2.getTeamAwayID() && match.getTeamAwayID()==match2.getTeamHomeID())) {
		return false;
	}else {
		return true;
	}

}


private double calcolaPeso(List<Match> parziale) {
	
	double peso = 0;
	
	for(int i = 1; i<parziale.size(); i++) {
		peso += this.grafo.getEdgeWeight(this.grafo.getEdge(parziale.get(i-1), parziale.get(i)));
	}
	
	
	return peso;
}


public List<Match> getBest() {
	return best;
}


public double getBestPeso() {
	return bestPeso;
}


	
	
	
	
	
}
