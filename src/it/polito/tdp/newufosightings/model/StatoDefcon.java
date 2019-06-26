package it.polito.tdp.newufosightings.model;

public class StatoDefcon {

	private State state;
	private int DEFCON;
	private int n_defcon_5;
	private boolean emergenza;

	public StatoDefcon(State state, int DEFCON, int n_defcon_5) {
		this.state = state;
		this.DEFCON = DEFCON;
		this.n_defcon_5 = n_defcon_5;
		this.emergenza = false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + DEFCON;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StatoDefcon other = (StatoDefcon) obj;
		if (DEFCON != other.DEFCON)
			return false;
		return true;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public int getDEFCON() {
		return DEFCON;
	}

	public void resetDEFCON() {
		this.DEFCON=5;
	}
	
	public void addDEFCON() {
		this.DEFCON--;
	}
	
	public void removeDEFCON() {
		this.DEFCON++;
	}

	public int getN_defcon_5() {
		return n_defcon_5;
	}

	public void setN_defcon_5(int n_defcon_5) {
		this.n_defcon_5 = n_defcon_5;
	}

	public boolean isEmergenza() {
		return emergenza;
	}

	public void setEmergenza() {
		this.emergenza = true;
	}

	public void setTregua() {
		this.emergenza = false;
	}

	@Override
	public String toString() {
		return "StatoDefcon [state=" + state + ", DEFCON=" + DEFCON + ", n_defcon_5=" + n_defcon_5 + ", emergenza="
				+ emergenza + "]";
	}

}
