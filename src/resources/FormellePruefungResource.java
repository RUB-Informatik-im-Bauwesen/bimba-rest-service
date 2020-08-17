package resources;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import de.rub.bi.inf.xbau.factory.NachrichtenFactory;
import de.rub.bi.inf.xbau.io.XBauExporter;
import de.xleitstelle.xbau.schema._2._1.BaugenehmigungAntrag0200;
import de.xleitstelle.xbau.schema._2._1.BaugenehmigungFormellePruefung0201;
import de.xleitstelle.xbau.schema._2._1.BefundlisteAntragFormell;
import de.xleitstelle.xbau.schema._2._1.Bezug;
import de.xleitstelle.xbau.schema._2._1.CodeFormelleBefundeAntragArt;
import de.xleitstelle.xbau.schema._2._1.Text;
import de.xleitstelle.xbau.schema._2._1.BefundlisteAntragFormell.Befund;
import model.LoadInformation;
import model.XbauContainer;
import requests.FormellePruefungRequest;
import requests.MyRequest;
import responses.FormellePruefungDataResponse;
import responses.FormellePruefungResponse;

@Path("/formellePruefung")
public class FormellePruefungResource {
	
	
	@POST
	@Path("/data")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response data(MyRequest data) {

		LoadInformation loadInformation = XbauContainer.getInstance().load(data);
		

		
		if (loadInformation.identifier!=null) {
			
			Object o = XbauContainer.getInstance().loadedXbau.get(loadInformation.identifier);
			
			if (o instanceof BaugenehmigungFormellePruefung0201) {
				BaugenehmigungFormellePruefung0201 formellePruefung0201 =
						(BaugenehmigungFormellePruefung0201) o;
				
				FormellePruefungDataResponse dataResponse = new FormellePruefungDataResponse();
				
				for (Befund befund : formellePruefung0201.getBefundliste().getBefund()) {
				
					if(befund.getArtDesBefundes().getCode().equals("1")) {
						dataResponse.setBefundBeteiligte(befund.getBeschreibung().getTextabsatz().get(0));
					}
					if(befund.getArtDesBefundes().getCode().equals("2")) {
						dataResponse.setBefundBauvorhaben(befund.getBeschreibung().getTextabsatz().get(0));
					}
					if(befund.getArtDesBefundes().getCode().equals("3")) {
						dataResponse.setBefundLokalisierung(befund.getBeschreibung().getTextabsatz().get(0));
					}
					if(befund.getArtDesBefundes().getCode().equals("4")) {
						dataResponse.setBefundAnlageMangel(befund.getBeschreibung().getTextabsatz().get(0));
					}
					if(befund.getArtDesBefundes().getCode().equals("5")) {
						dataResponse.setBefundAnlageFehlt(befund.getBeschreibung().getTextabsatz().get(0));
					}
					if(befund.getArtDesBefundes().getCode().equals("6")) {
						dataResponse.setBefundAbweichung(befund.getBeschreibung().getTextabsatz().get(0));
					}
				}
				
				dataResponse.setFrist(formellePruefung0201.getFrist().toString());
				
				
				return Response.status(200)
						.entity(dataResponse).build();
			}else {
				System.out.println("Falsche Nachricht: "+o.getClass().getName());
				return Response.status(404).build();
			}
			
			
			
		}else {
			System.out.println("Datei konnte nicht geladen werden");
			return loadInformation.failResponse;
		}
	}
	
	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response formellePruefung(FormellePruefungRequest request) {
		
		BaugenehmigungAntrag0200 vorganger=null;
		
		if(request.getVorgaengerPath() != null) {
			MyRequest data = new MyRequest();
			data.setPath(Paths.get(request.getVorgaengerPath(),"content").toString());
			LoadInformation info = XbauContainer.getInstance().load(data);
			
			if(info.identifier !=null) {
				vorganger = (BaugenehmigungAntrag0200) XbauContainer.getInstance().loadedXbau.get(info.identifier);
			}
		}
		
		//TODO Existens des Vorg�ngers sicherstellen, ansonsten Fehler
		
		
		BaugenehmigungFormellePruefung0201 baugenehmigungFormellePruefung0201=null;
		
		if(request.getPath() != null) {
			MyRequest data = new MyRequest();
			data.setPath(Paths.get(request.getPath(),"content").toString());
			
			LoadInformation info = XbauContainer.getInstance().load(data);
			
			if(info.identifier !=null) {
				baugenehmigungFormellePruefung0201 = (BaugenehmigungFormellePruefung0201) XbauContainer.getInstance().loadedXbau.get(info.identifier);
			}
		}
		
		
		if(baugenehmigungFormellePruefung0201==null) {
			baugenehmigungFormellePruefung0201 = new BaugenehmigungFormellePruefung0201();
			baugenehmigungFormellePruefung0201.setNachrichtenkopfG2Privat(
					NachrichtenFactory.getInstance().newNachrichtenkopfG2Privat("0201", "baugenehmigung.formellePruefung.0201"));
			//TODO set Autor und Beh�rde
			Bezug bezug = new Bezug();
			baugenehmigungFormellePruefung0201.setBezug(bezug);
			if(vorganger.getNachrichtenkopfPrivat2G()!=null
					&& vorganger.getNachrichtenkopfPrivat2G().getIdentifikationNachricht()!=null) {
				bezug.setBezugNachricht(vorganger.getNachrichtenkopfPrivat2G().getIdentifikationNachricht());
			}
		}
		
		
		BefundlisteAntragFormell befundliste = new BefundlisteAntragFormell();
		baugenehmigungFormellePruefung0201.setBefundliste(befundliste);
		
		if(request.getBefundBeteiligte()!=null) {
			befundliste.getBefund().add(erstelleBefund("1", request.getBefundBeteiligte()));
		}
		if(request.getBefundBauvorhaben()!=null) {
			befundliste.getBefund().add(erstelleBefund("2", request.getBefundBauvorhaben()));
		}
		if(request.getBefundLokalisierung()!=null) {
			befundliste.getBefund().add(erstelleBefund("3", request.getBefundLokalisierung()));
		}
		if(request.getBefundAnlageMangel()!=null) {
			befundliste.getBefund().add(erstelleBefund("4", request.getBefundAnlageMangel()));
		}
		if(request.getBefundAnlageFehlt()!=null) {
			befundliste.getBefund().add(erstelleBefund("5", request.getBefundAnlageFehlt()));
		}
		if(request.getBefundAbweichung()!=null) {
			befundliste.getBefund().add(erstelleBefund("6", request.getBefundAbweichung()));
		}
		
		System.out.println(request.getFrist());
		
		if(request.getFrist()!=null) {
			
			try {
				XMLGregorianCalendar xmlFrist = DatatypeFactory.newInstance().newXMLGregorianCalendar(request.getFrist());
				
				baugenehmigungFormellePruefung0201.setFrist(xmlFrist);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
		
		//Exportiere zu Pfad mit Nachricht.
		String path = Paths.get(request.getPath(), "content", "index.xbau").toString();
		System.out.println(path);
		new XBauExporter().saveToFile(path, baugenehmigungFormellePruefung0201);
	
		
		//using PosixFilePermission to set file permissions 777
        Set<PosixFilePermission> perms = new HashSet<PosixFilePermission>();
        //add owners permission
        perms.add(PosixFilePermission.OWNER_READ);
        perms.add(PosixFilePermission.OWNER_WRITE);
        perms.add(PosixFilePermission.OWNER_EXECUTE);
        //add group permissions
        perms.add(PosixFilePermission.GROUP_READ);
        perms.add(PosixFilePermission.GROUP_WRITE);
        perms.add(PosixFilePermission.GROUP_EXECUTE);
        //add others permissions
        perms.add(PosixFilePermission.OTHERS_READ);
        //perms.add(PosixFilePermission.OTHERS_WRITE);
        perms.add(PosixFilePermission.OTHERS_EXECUTE);
		
        try {
			Files.setPosixFilePermissions(Paths.get(path), perms);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        
		return Response.status(200).entity(new FormellePruefungResponse()).build();
		
	}
	
	private Befund erstelleBefund(String code, String textinhalt) {
		CodeFormelleBefundeAntragArt art = new CodeFormelleBefundeAntragArt();
		art.setCode(code);
		
		Befund befund = new Befund();
		befund.setArtDesBefundes(art);
		
		Text text = new Text();
		text.getTextabsatz().add(textinhalt);
		befund.setBeschreibung(text);
		
		return befund;
	}

}
