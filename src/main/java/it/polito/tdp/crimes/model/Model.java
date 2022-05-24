package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	
	//non creo un identity map perchè non ho id da collegare
	private Graph<String, DefaultWeightedEdge> grafo;
	private EventsDao dao;
	private List<String> best;
	
	
	public Model() {
		dao = new EventsDao();
	}
	
	public void creaGrafo(String categoria, int mese) {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		//aggiungi i vertici
		Graphs.addAllVertices(this.grafo, dao.getVertici(categoria, mese));
		
		
		//aggiunta archi
		for(Adiacenza a : dao.getArchi(categoria, mese)) {
			Graphs.addEdgeWithVertices(this.grafo, a.getV1(), a.getV2(),a.getPeso());
		}
		//todo riempire la tendina degli archi
		
		System.out.println("Grafo creato!\n");
		System.out.println("#VERTICI: " + this.grafo.vertexSet().size());
		System.out.println("#ARCHI: " + this.grafo.edgeSet().size());
	}
	
	public List<Adiacenza> getArchiMaggioriPesoMedio(){
		
		//scorro gli archi del grafo e calcolo il peso medio
		double pesoTot = 0.0;
		for(DefaultWeightedEdge e : this.grafo.edgeSet()) {
			pesoTot += this.grafo.getEdgeWeight(e);
			
		}
		double avg = pesoTot/this.grafo.edgeSet().size();
		
		//riscorro tutti gli archi prendendo quelli maggiori di avg
		List<Adiacenza > result = new ArrayList<>();
		for(DefaultWeightedEdge e : this.grafo.edgeSet()) {
			if(this.grafo.getEdgeWeight(e)> avg) {
				result.add(new Adiacenza(this.grafo.getEdgeSource(e),
						this.grafo.getEdgeTarget(e),
						(int) this.grafo.getEdgeWeight(e)));
			}
		}
		return result;
	}
	
	public List<String> calcolaPercorso(String sorgente, String destinazione){
		best= new LinkedList<>();
		List<String> parziale = new LinkedList<>();
		parziale .add(sorgente);
		
		cerca(parziale, destinazione);
		return best;
		
	}
	
	private void cerca(List<String> parziale, String destinazione) {
		//condizione di terminazione
		if(parziale.get(parziale.size()-1).equals(destinazione)) {
			//è la soluzione migliore?
			if(parziale.size()>best.size()) {
				best = new LinkedList<>(parziale);
			}
			return;
		}
		
		//scorro i vicini del'ultimo inserito e provo le diverse strade
		for(String s : Graphs.neighborListOf(this.grafo, 
				parziale.get(parziale.size()-1))) {
			//per avere ricrsione aciclica
			if(!parziale.contains(s)) {
			parziale.add(s);
			cerca(parziale, destinazione);
			parziale.remove(parziale.size()-1);
			}
		}
	}
	
}
