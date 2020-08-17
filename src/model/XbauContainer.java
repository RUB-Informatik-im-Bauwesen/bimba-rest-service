package model;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.UUID;

import javax.ws.rs.core.Response;

import de.rub.bi.inf.xbau.io.XBauImporter;
import requests.MyRequest;

public class XbauContainer {
	
	private XbauContainer() {
		// TODO Auto-generated constructor stub
	}
	
	private static XbauContainer instance;
	public static XbauContainer getInstance() {
		if (instance==null)
			instance = new XbauContainer();
		return instance;
	}
	
	public HashMap<String, Object> loadedXbau = new HashMap<>();
	
	
	public LoadInformation load(MyRequest data) {
		
		LoadInformation loadInformation = new LoadInformation();
		
		if (data.getLoadedId() != null) {
			if (XbauContainer.getInstance().loadedXbau.containsKey(data.getLoadedId())) {
				
				loadInformation.identifier=data.getLoadedId();
				return loadInformation;
			}
		}

		java.nio.file.Path path = Paths.get(data.getPath());

		if (path.toFile().exists() == false) {
			loadInformation.failResponse = Response.status(404).build();
			return loadInformation;

		}
			
		
		File fileName = new File(data.getPath());
		File[] fileList = fileName.listFiles();

		File xBauFile = null;
		for (File file : fileList) {
			if (file.getName().endsWith(".xbau")) {
				xBauFile = file; 
				break;
			}
		}

		if (xBauFile == null) {
			loadInformation.failResponse = Response.status(404, "No file with extension .xbau is available").build();
			return loadInformation;
		}
			

		System.out.println("Formelle Pr�fung:" +xBauFile.getName());

		Object o;
		try {
			o = new XBauImporter().readFromFile(xBauFile.getAbsolutePath());

			System.out.println(o.getClass().getSimpleName());

		} catch (Exception e) {
			e.printStackTrace();
			loadInformation.failResponse = Response.status(404, e.getMessage()).build();
			return loadInformation;
		}

		UUID uuid = UUID.randomUUID();
		XbauContainer.getInstance().loadedXbau.put(uuid.toString(), o);

		loadInformation.identifier = uuid.toString();
		return loadInformation;
	}

}
