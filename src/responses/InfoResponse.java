package responses;

import de.xleitstelle.xbau.schema._2._1.IdentifikationNachricht;

public class InfoResponse {
	
	public InfoResponse() {
		// TODO Auto-generated constructor stub
	}
	
	
	private String nachrichtentyp;
	private String bezeichnung;
	
	private IdentifikationNachricht identifikation;

	
	
	public String getNachrichtentyp() {
		return nachrichtentyp;
	}
	
	public void setNachrichtentyp(String nachrichtentyp) {
		this.nachrichtentyp = nachrichtentyp;
	}

	
	public String getBezeichnung() {
		return bezeichnung;
	}

	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}
	
	public IdentifikationNachricht getIdentifikation() {
		return identifikation;
	}
	
	public void setIdentifikation(IdentifikationNachricht identifikation) {
		this.identifikation = identifikation;
	}
}
