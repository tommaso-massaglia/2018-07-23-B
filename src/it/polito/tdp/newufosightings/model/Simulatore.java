package it.polito.tdp.newufosightings.model;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import it.polito.tdp.newufosightings.db.NewUfoSightingsDAO;

public class Simulatore {

	private Map<String, StatoDefcon> defcon_state;
	private int T1;
	private int T2;

	private PriorityQueue<Evento> queue;

	public String getNumDefcon() {
		String result = "Elenco stati con numero di emergenze:\n";
		for (StatoDefcon sd : this.defcon_state.values()) {
			result += sd.getState().getName() + " è stato in massima allerta " + sd.getN_defcon_5() + " volte;\n";
		}
		return result;
	}

	public void init(int T1, int T2, int year) {

		NewUfoSightingsDAO dao = new NewUfoSightingsDAO();
		this.queue = new PriorityQueue<>();
		this.defcon_state = new HashMap<String, StatoDefcon>();
		this.T1 = T1;
		this.T2 = T2;

		for (Sighting s : dao.loadSightingsYear(year)) {
			this.queue.add(new Evento(s.getDatetime(), s.getState().toUpperCase(), Evento.TIPO.AVVISTAMENTO));
		}
		for (State s : dao.loadAllStates()) {
			this.defcon_state.put(s.getId(), new StatoDefcon(s, 5, 0));
		}

	}

	public void run() {

		Evento e;
		while ((e = queue.poll()) != null) {
			switch (e.getTipo()) {
			case AVVISTAMENTO:
				if (!this.defcon_state.get(e.getState()).isEmergenza()) {
					this.defcon_state.get(e.getState()).addDEFCON();
					if (this.defcon_state.get(e.getState()).getDEFCON() == 1) {
						this.queue.add(new Evento(e.getData().plusDays(T2), e.getState(), Evento.TIPO.EMERGENZA));
						this.defcon_state.get(e.getState()).setEmergenza();
					} else {
						this.queue.add(
								new Evento(e.getData().plusDays(T1), e.getState(), Evento.TIPO.RIPRISTINO_DEFCON));
					}

				}
				break;

			case EMERGENZA:
				this.defcon_state.get(e.getState()).resetDEFCON();
				this.defcon_state.get(e.getState()).setTregua();
				this.defcon_state.get(e.getState())
						.setN_defcon_5(this.defcon_state.get(e.getState()).getN_defcon_5() + 1);
				break;

			case RIPRISTINO_DEFCON:
				if (!this.defcon_state.get(e.getState()).isEmergenza()) {
					this.defcon_state.get(e.getState()).removeDEFCON();
				}
				break;
			}
		}
	}
}
