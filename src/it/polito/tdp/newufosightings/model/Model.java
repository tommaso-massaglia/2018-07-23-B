package it.polito.tdp.newufosightings.model;

import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.newufosightings.db.NewUfoSightingsDAO;

public class Model {

	private Graph<State, DefaultWeightedEdge> grafo;
	private Map<String, State> stateIdMap;

	public void creaGrafo(int xG, int year) {

		NewUfoSightingsDAO dao = new NewUfoSightingsDAO();
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		this.stateIdMap = new HashMap<>();

		for (State s : dao.loadAllStates()) {
			this.stateIdMap.put(s.getId(), s);
		}
		Graphs.addAllVertices(this.grafo, this.stateIdMap.values());

		for (Neighborhood n : dao.loadAllNeighbor()) {
			this.grafo.addEdge(this.stateIdMap.get(n.getState1()), this.stateIdMap.get(n.getState2()));
			this.grafo.setEdgeWeight(
					this.grafo.getEdge(this.stateIdMap.get(n.getState1()), this.stateIdMap.get(n.getState2())),
					this.getPeso(this.stateIdMap.get(n.getState1()), this.stateIdMap.get(n.getState2()), year, xG));
		}

		System.out.println("Grafo creato con successo.");
	}

	public String getPesoAdiacenti() {
		String result = "";
		int peso = 0;
		for (State s1 : grafo.vertexSet()) {
			for (State s2 : Graphs.neighborListOf(grafo, s1)) {
				peso += grafo.getEdgeWeight(grafo.getEdge(s1, s2));
			}
			result += "Peso totale dei vertici di " + s1.getName() + ": " + peso + "\n";
			peso = 0;
		}
		return result;
	}

	private int getPeso(State s1, State s2, int year, int xG) {
		NewUfoSightingsDAO dao = new NewUfoSightingsDAO();
		int maxAvvistamenti = 0;
		int avvistamenti = 0;
		int giorni = 365;
		List<Sighting> toParse = new ArrayList<>(dao.loadSightingsYearStates(year, s1.getId(), s2.getId()));
		if (Year.isLeap(year))
			giorni++;
		for (int i = 0; i <= giorni - xG; i++) {
			avvistamenti = 0;
			for (Sighting s : toParse) {
				if (s.getDatetime().getDayOfYear() <= i + xG && s.getDatetime().getDayOfYear() >= i) {
					avvistamenti++;
				}
			}
			if (maxAvvistamenti < avvistamenti) {
				maxAvvistamenti = avvistamenti;
			}
		}

		return maxAvvistamenti;
	}
	
	public String simula(int T1, int T2, int year){
		Simulatore sim = new Simulatore();
		sim.init(T1, T2, year);
		sim.run();
		return sim.getNumDefcon();
	}

}
