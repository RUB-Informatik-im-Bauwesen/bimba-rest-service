package requests;

import javax.xml.datatype.XMLGregorianCalendar;

public class FormellePruefungRequest {
	
	public FormellePruefungRequest() {
		// TODO Auto-generated constructor stub
	}
	
	private String path;
	
	private String vorgaengerPath;
	
	private String vorgaengerUUID;
	private String vorgaengerTypCode;

	
	private String befundBeteiligte, befundBauvorhaben, befundLokalisierung, befundAnlageMangel, befundAnlageFehlt,
			befundAbweichung;
	
	private String frist;
	
	
	

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getBefundBeteiligte() {
		return befundBeteiligte;
	}

	public void setBefundBeteiligte(String befundBeteiligte) {
		this.befundBeteiligte = befundBeteiligte;
	}

	public String getBefundBauvorhaben() {
		return befundBauvorhaben;
	}

	public void setBefundBauvorhaben(String befundBauvorhaben) {
		this.befundBauvorhaben = befundBauvorhaben;
	}

	public String getBefundLokalisierung() {
		return befundLokalisierung;
	}

	public void setBefundLokalisierung(String befundLokalisierung) {
		this.befundLokalisierung = befundLokalisierung;
	}

	public String getBefundAnlageMangel() {
		return befundAnlageMangel;
	}

	public void setBefundAnlageMangel(String befundAnlageMangel) {
		this.befundAnlageMangel = befundAnlageMangel;
	}

	public String getBefundAnlageFehlt() {
		return befundAnlageFehlt;
	}

	public void setBefundAnlageFehlt(String befundAnlageFehlt) {
		this.befundAnlageFehlt = befundAnlageFehlt;
	}

	public String getBefundAbweichung() {
		return befundAbweichung;
	}

	public void setBefundAbweichung(String befundAbweichung) {
		this.befundAbweichung = befundAbweichung;
	}
	
	public String getFrist() {
		return frist;
	}
	
	public void setFrist(String frist) {
		this.frist = frist;
	}	
	
	public String getVorgaengerUUID() {
		return vorgaengerUUID;
	}

	public void setVorgaengerUUID(String vorgaengerUUID) {
		this.vorgaengerUUID = vorgaengerUUID;
	}

	public String getVorgaengerTypCode() {
		return vorgaengerTypCode;
	}

	public void setVorgaengerTypCode(String vorgaengerTypCode) {
		this.vorgaengerTypCode = vorgaengerTypCode;
	}
	
	public void setVorgaengerPath(String vorgaengerPath) {
		this.vorgaengerPath = vorgaengerPath;
	}
	
	public String getVorgaengerPath() {
		return vorgaengerPath;
	}
	
	
	
	
	

}
