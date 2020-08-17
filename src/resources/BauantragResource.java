package resources;

import java.io.File;
import java.nio.file.Paths;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.rub.bi.inf.xbau.io.XBauImporter;
import de.xleitstelle.xbau.schema._2._1.BaugenehmigungAntrag0200;
import de.xleitstelle.xbau.schema._2._1.Bauvorhaben;
import de.xleitstelle.xbau.schema._2._1.IdentifikationNachricht;
import model.XbauContainer;
import requests.MyRequest;
import responses.InfoResponse;

@Path("/bauantrag")
public class BauantragResource {

	@POST
	@Path("/load")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response load(MyRequest data) {

		if (data.getLoadedId() != null) {
			if (XbauContainer.getInstance().loadedXbau.containsKey(data.getLoadedId())) {
				return Response.status(201, "xbau successfully loaded")
						.entity("{\"id\":\"" + data.getLoadedId() + "\"}").build();
			}
		}

		java.nio.file.Path path = Paths.get(data.getPath());

		if (path.toFile().exists() == false)
			return Response.status(404).build();

		File fileName = new File(data.getPath());
		File[] fileList = fileName.listFiles();

		File xBauFile = null;
		for (File file : fileList) {
			if (file.getName().endsWith(".xbau")) {
				xBauFile = file;
				break;
			}
		}

		if (xBauFile == null)
			return Response.status(404, "No file with extension .xbau is available").build();

		System.out.println(xBauFile.getName());

		Object o;
		try {
			o = new XBauImporter().readFromFile(xBauFile.getAbsolutePath());

			System.out.println(o.getClass().getSimpleName());

		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(404, e.getMessage()).build();
		}

		UUID uuid = UUID.randomUUID();
		XbauContainer.getInstance().loadedXbau.put(uuid.toString(), o);

		return Response.status(201, "xbau 3eccsuccessfully loaded").entity("{\"id\":\"" + uuid.toString() + "\"}")
				.build();
	}

	@GET
	@Path("/getData/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getData(@PathParam("id") String id) {

		System.out.println(id);

		Object o = XbauContainer.getInstance().loadedXbau.get(id);
		if (o == null) {
			return Response.status(404, "Model not loaded").build();
		}

		BaugenehmigungAntrag0200 ba200 = null;

		if (o instanceof BaugenehmigungAntrag0200) {
			ba200 = (BaugenehmigungAntrag0200) o;

			System.out.println(ba200.getBauvorhaben().getBezeichnungDesBauvorhabens());
		}

		return Response.status(200).entity(ba200).build();

	}

	@GET
	@Path("/getInfo/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getInfo(@PathParam("id") String id) {

		System.out.println(id);

		Object o = XbauContainer.getInstance().loadedXbau.get(id);
		if (o == null) {
			return Response.status(404, "Model not loaded").build();
		}

		BaugenehmigungAntrag0200 ba200 = null;

		InfoResponse response = new InfoResponse();
		
		if (o instanceof BaugenehmigungAntrag0200) {
			ba200 = (BaugenehmigungAntrag0200) o;
			response.setNachrichtentyp("0200");
			
			if(ba200.getNachrichtenkopfPrivat2G() != null && ba200.getNachrichtenkopfPrivat2G().getIdentifikationNachricht() !=null) {
				IdentifikationNachricht identifikationNachricht = ba200.getNachrichtenkopfPrivat2G().getIdentifikationNachricht();
				response.setIdentifikation(identifikationNachricht);
				
			}

			Bauvorhaben bauvorhaben = ba200.getBauvorhaben();

			if (bauvorhaben != null) {
				response.setBezeichnung(bauvorhaben.getBezeichnungDesBauvorhabens());
			}

		}

		return Response.status(200).entity(response).build();

	}

	
	
	
	
}
