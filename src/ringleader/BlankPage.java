package ringleader;
import fr.lip6.move.pnml.ptnet.hlapi.PetriNetDocHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.PetriNetHLAPI;
import fr.lip6.move.pnml.framework.utils.ModelRepository;
import fr.lip6.move.pnml.ptnet.hlapi.NameHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.PNTypeHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.PageHLAPI;

import java.io.File;
import java.io.IOException;

import fr.lip6.move.pnml.framework.general.PnmlExport;
import fr.lip6.move.pnml.framework.utils.exception.InvalidIDException;
import fr.lip6.move.pnml.framework.utils.exception.OCLValidationFailed;
import fr.lip6.move.pnml.framework.utils.exception.VoidRepositoryException;
import fr.lip6.move.pnml.framework.utils.ModelRepository;
import java.io.IOException;

public class BlankPage {
	public static void main (String[] args) {
		try {
			ModelRepository.getInstance().createDocumentWorkspace("workspace");
			PetriNetDocHLAPI doc = new PetriNetDocHLAPI();
			PetriNetHLAPI net = new PetriNetHLAPI("blank", PNTypeHLAPI.PTNET, new NameHLAPI("blank"), doc);
			PageHLAPI page = new PageHLAPI("toppage", new NameHLAPI("blank"), null, net);
			
			File dir = new File (System.getProperty("user.dir")+"/rapport");
			if (!dir.exists()) {
				if (!dir.mkdir()) {
					throw new IOException("Failed to create directory " + dir.getAbsolutePath());
				}
			}
			PnmlExport pex = new PnmlExport();
			try {
				pex.exportObject(doc,"rapport/"+args[0]+".pnml");
				System.out.println("File "+args[0]+".pnml exported to rapport directory.");
			} catch (OCLValidationFailed e) {
				e.printStackTrace();
			} catch (ArrayIndexOutOfBoundsException e) {
				e.printStackTrace();
			}
			ModelRepository.getInstance().destroyCurrentWorkspace();
			
		} catch (InvalidIDException e) {
			System.out.println("InvalidIDException caught by while running generator");
			e.printStackTrace();
		} catch (VoidRepositoryException e) {
			System.out.println("VoidRepositoryException caught by while running generator");
			e.printStackTrace();	
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
