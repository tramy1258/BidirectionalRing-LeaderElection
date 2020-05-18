package ringleader;
import fr.lip6.move.pnml.ptnet.hlapi.PetriNetDocHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.PetriNetHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.PageHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.PNTypeHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.PlaceHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.PositionHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.TransitionHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.AnnotationGraphicsHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.ArcHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.ArcGraphicsHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.DimensionHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.NameHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.NodeGraphicsHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.OffsetHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.PTMarkingHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.LineHLAPI;
import fr.lip6.move.pnml.ptnet.CSS2Color;
import fr.lip6.move.pnml.framework.general.PnmlExport;
import fr.lip6.move.pnml.framework.utils.exception.InvalidIDException;
import fr.lip6.move.pnml.framework.utils.exception.VoidRepositoryException;
import fr.lip6.move.pnml.framework.utils.exception.OCLValidationFailed;
import fr.lip6.move.pnml.framework.utils.ModelRepository;
import java.io.File;
import java.io.IOException;
import java.io.FileReader;
import java.util.Random;

public class Ring {
	
	//Interesting for placing processus randomly on ring
	/*protected int[] randomize(int a, int b){
		Random rgen = new Random();  // Random number generator		
		int size = b-a;
		int[] array = new int[size];
 
		for(int i=0; i<size; i++){
			array[i] = a+i;
		}
 
		for (int i=0; i<size; i++) {
		    int randomPosition = rgen.nextInt(array.length);
		    int temp = array[i];
		    array[i] = array[randomPosition];
		    array[randomPosition] = temp;
		}
 
		//for (int s: array)
		//	System.out.println(s);
 
		return array;
	}*/
	
	public void generate_ring(int nb, int dim, boolean reduced) { 
		
		//int[] ids = randomize(1,nb+1); //identities of processes
		
		int[] ids = new int[nb];
		for (int i=0; i<nb; i++) {
			ids[i] = i+1;
		}
		ReducedProcess[] pcs = new ReducedProcess[nb]; //processes
		boolean[] competitor = new boolean[nb]; //competitor[i] = true if pcs[i] is still competing
		for (int i=0; i<nb; i++) {
			competitor[i] = true;
		}
		int d;
		PlaceHLAPI repl, repr;
		boolean left, right;
		Result tmpl, tmpr;
		PlaceHLAPI rdl, rdr;
		//int xfinl=0, xfinr=0; // to calculate position of fin
		PlaceHLAPI fin = null; //first ready2elected message
		
		try {
			ModelRepository.getInstance().createDocumentWorkspace("ring");
			PetriNetDocHLAPI doc = new PetriNetDocHLAPI();
			PetriNetHLAPI net = new PetriNetHLAPI("leader_election", PNTypeHLAPI.PTNET, new NameHLAPI("leader"), doc);
			PageHLAPI page = new PageHLAPI("toppage", new NameHLAPI("leader"), null, net);
			
			int rmax = (int) Math.ceil(Math.log(nb)/(Math.log(2)+1e-10))+1; //number of rounds needed
			
			if (reduced) {
				for (int i=0; i<nb; i++) {
					pcs[i] = new ReducedProcess(page,ids[i],dim/nb/2+i*dim/nb,20,rmax,dim/nb/2);
				}
			} else {
				for (int i=0; i<nb; i++) {
					pcs[i] = new Process(page,ids[i],dim/nb/2+i*dim/nb,20,rmax,dim/nb/2);
				}
			}
			
			for (int r=0; r<rmax; r++) {	
				
				int dmax = (int) Math.pow(2,r);
				//System.out.println("r = "+r+" dmax = "+dmax);
				for (int i=0; i<nb; i++) {
					pcs[i].set_lastx();
				}
				for (int i=0; i<nb; i++) {
					if (competitor[i]) {
						d = 0;
						repl = null; //reply(pcs[i],r) from left, pcs[i] won in its left neighborhood 
						left = true; //pcs[i] is still a potential winner in its left neighborhood 
						tmpl = null;
						rdl = null; //ready2election...
												
						//pcs[i] competing in left neighborhood
						while (left && d<dmax) {
							
							if (d == 0) { //first election message of round r
								if (r == 0) { //first round
									rdl = pcs[i].get_election("left", ids[i], r, d+1, pcs[i].get_start());
								} else {
									rdl = pcs[i].get_election("left", ids[i], r, d+1, pcs[i].get_round(r-1));
								}
							} else {
								rdl = pcs[(i+nb-d)%nb].get_election("left", ids[i], r, d+1, tmpl.get_hdl());
							}
							
							if (rdl != null) {
								tmpl = pcs[(i+nb-d-1)%nb].get_handling_election("right", ids[i], r, d+1, rdl);
								
								if (tmpl.get_res() == 1) { //election message transfered to next neighbor to the left
									
									//nothing to do here, case taken care of in next iteration of while loop
									
								} else if (tmpl.get_res() == 2) { //reply back to the winner pcs[i] to announce its victory
									
									//number of replies needed equals to distance the election message has traveled, which is dmax
									PlaceHLAPI rd1 = pcs[(i+nb-dmax)%nb].get_reply("right", ids[i], r, tmpl.get_hdl());;
									Result tmp1 = pcs[(i+nb-dmax+1)%nb].get_handling_reply("left", ids[i], r, rd1);
									
									if (tmp1.get_res() == 2) { //the winner learning about its victory
										repl = new PlaceHLAPI("reply_"+ids[i]+"_"+r+"_left"+ids[i],page);
										ArcHLAPI torepl = new ArcHLAPI(tmp1.get_hdl().getId()+"___"+repl.getId(),tmp1.get_hdl(),repl,page);
										ArcGraphicsHLAPI aglt = new ArcGraphicsHLAPI(torepl);
										LineHLAPI llt = new LineHLAPI(aglt);
										llt.setColorHLAPI(CSS2Color.BLACK);
										//System.out.println("-->"+torepl.getId());
									}
									
									for (int k = dmax-1; k>0; k--) {
										rd1 = pcs[(i+nb-k)%nb].get_reply("right", ids[i], r, tmp1.get_hdl());
										tmp1 = pcs[(i+nb-k+1)%nb].get_handling_reply("left", ids[i], r, rd1);	
										
										if (tmp1.get_res() == 2) { //the winner learning about its victory
											repl = new PlaceHLAPI("reply_"+ids[i]+"_"+r+"_left"+ids[i],page);
											ArcHLAPI torepl = new ArcHLAPI(tmp1.get_hdl().getId()+"___"+repl.getId(),tmp1.get_hdl(),repl,page);
											ArcGraphicsHLAPI aglt = new ArcGraphicsHLAPI(torepl);
											LineHLAPI llt = new LineHLAPI(aglt);
											llt.setColorHLAPI(CSS2Color.BLACK);
											//System.out.println("-->"+torepl.getId());
										}
									}
									NodeGraphicsHLAPI pgl = new NodeGraphicsHLAPI(repl);
									DimensionHLAPI diml = new DimensionHLAPI(25,25,pgl);
									OffsetHLAPI ol = new OffsetHLAPI(-repl.getId().length()*4/2,-30,new AnnotationGraphicsHLAPI(new NameHLAPI(repl.getId(),repl)));
									LineHLAPI ll = new LineHLAPI(pgl);
									ll.setColorHLAPI(CSS2Color.RED);
									int xl =  tmp1.get_hdl().getNodegraphicsHLAPI().getPositionHLAPI().getX().intValue();
									int yl = tmp1.get_hdl().getNodegraphicsHLAPI().getPositionHLAPI().getY().intValue();
									PositionHLAPI posl = new PositionHLAPI(xl,yl+60,pgl);
									
								} else if (tmpl.get_res() == 3) { //pcs[i] is out of competition
									
									left = false;
									
								} else if (tmpl.get_res() == 4) { //pcs[i] is elected
									
									//xfinl = tmpl.get_hdl().getNodegraphicsHLAPI().getPositionHLAPI().getX();
									break; //all done, no more election message
									
								}
							}
							
							d++;						
						}
						
						//pcs[i] competing in right neighborhood
						d = 0;
						repr = null; //reply(pcs[i],r) from right, pcs[i] won in its right neighborhood
						right = true; //pcs[i] is still a potential winner in its right neighborhood
						tmpr = null;
						rdr = null;
						
						while (right && d<dmax) {

							if (d == 0) { //first election message of round r
								if (r == 0) { //first round
									rdr = pcs[i].get_election("right", ids[i], r, d+1, pcs[i].get_start());
								} else {
									rdr = pcs[i].get_election("right", ids[i], r, d+1, pcs[i].get_round(r-1));
								}
							} else {
								rdr = pcs[(i+nb+d)%nb].get_election("right", ids[i], r, d+1, tmpr.get_hdl());
							}
							
							if (rdr != null) {
								tmpr = pcs[(i+nb+d+1)%nb].get_handling_election("left", ids[i], r, d+1, rdr);
								
								if (tmpr.get_res() == 1) { //election message transfered to next neighbor to the left
									
									//nothing to do here, case taken care of in next iteration of the while loop
									
								} else if (tmpr.get_res() == 2) { //reply back to the winner pcs[i] to announce its victory
									
									//number of replies equals to distance election message has traveled, which is dmax
									PlaceHLAPI rd1 = pcs[(i+nb+dmax)%nb].get_reply("left", ids[i], r, tmpr.get_hdl());;
									Result tmp1 = pcs[(i+nb+dmax-1)%nb].get_handling_reply("right", ids[i], r, rd1);
									
									if (tmp1.get_res() == 2) { //the winner learning about its victory
										repr = new PlaceHLAPI("reply_"+ids[i]+"_"+r+"_right"+ids[i],page);
										ArcHLAPI torepr = new ArcHLAPI(tmp1.get_hdl().getId()+"___"+repr.getId(),tmp1.get_hdl(),repr,page);
										ArcGraphicsHLAPI agrt = new ArcGraphicsHLAPI(torepr);
										LineHLAPI lrt = new LineHLAPI(agrt);
										lrt.setColorHLAPI(CSS2Color.BLACK);
										//System.out.println("-->"+torepr.getId());
									}
									
									for (int k = dmax-1; k>0; k--) {
										rd1 = pcs[(i+nb+k)%nb].get_reply("left", ids[i], r, tmp1.get_hdl());
										tmp1 = pcs[(i+nb+k-1)%nb].get_handling_reply("right", ids[i], r, rd1);	
										
										if (tmp1.get_res() == 2) { //the winner learning about its victory
											repr = new PlaceHLAPI("reply_"+ids[i]+"_"+r+"_right"+ids[i],page);
											ArcHLAPI torepr = new ArcHLAPI(tmp1.get_hdl().getId()+"___"+repr.getId(),tmp1.get_hdl(),repr,page);
											ArcGraphicsHLAPI agrt = new ArcGraphicsHLAPI(torepr);
											LineHLAPI lrt = new LineHLAPI(agrt);
											lrt.setColorHLAPI(CSS2Color.BLACK);
											//System.out.println("-->"+torepr.getId());
										}
									}
									NodeGraphicsHLAPI pgr = new NodeGraphicsHLAPI(repr);
									DimensionHLAPI dimr = new DimensionHLAPI(25,25,pgr);
									OffsetHLAPI or = new OffsetHLAPI(-repr.getId().length()*4/2,-30,new AnnotationGraphicsHLAPI(new NameHLAPI(repr.getId(),repr)));
									LineHLAPI lr = new LineHLAPI(pgr);
									lr.setColorHLAPI(CSS2Color.RED);					
									int xr =  tmp1.get_hdl().getNodegraphicsHLAPI().getPositionHLAPI().getX().intValue();
									int yr = tmp1.get_hdl().getNodegraphicsHLAPI().getPositionHLAPI().getY().intValue();
									PositionHLAPI posr = new PositionHLAPI(xr,yr+60,pgr);
									
								} else if (tmpr.get_res() == 3) { //pcs[i] is out of competition
									
									right = false;
									
								} else if (tmpr.get_res() == 4) { //pcs[i] is elected
									
									//xfinr = tmpr.get_hdl().getNodegraphicsHLAPI().getPositionHLAPI().getX();
									
									fin = new PlaceHLAPI("elected_"+ids[i]+"_to_left"+ids[i],page);
									NodeGraphicsHLAPI pgfin = new NodeGraphicsHLAPI(fin);
									DimensionHLAPI dimfin = new DimensionHLAPI(25,25,pgfin);
									OffsetHLAPI ofin = new OffsetHLAPI(-fin.getId().length()*5/2,-30,new AnnotationGraphicsHLAPI(new NameHLAPI(fin.getId(),fin)));
									LineHLAPI lfin = new LineHLAPI(pgfin);
									PositionHLAPI posfin = new PositionHLAPI(i*dim/nb,tmpr.get_hdl().getNodegraphicsHLAPI().getPositionHLAPI().getY()+80,pgfin);
									
									ArcHLAPI arcl = new ArcHLAPI(tmpl.get_hdl().getId()+"___"+fin.getId(),tmpl.get_hdl(),fin,page);									
									ArcHLAPI arcr = new ArcHLAPI(tmpr.get_hdl().getId()+"___"+fin.getId(),tmpr.get_hdl(),fin,page);									
									//System.out.println("-->"+arcl.getId());
									//System.out.println("-->"+arcr.getId());
									PlaceHLAPI elt = pcs[(i+nb-1)%nb].get_elected(ids[i],fin);
									for (int k=2; k<=nb; k++) {
										elt = pcs[(i+nb-k)%nb].get_elected(ids[i],elt);										
									}
									break; //all done, no more election message
								}
							}
							
							d++;
						}
						
						if (tmpl.get_res() != 4 && tmpr.get_res() != 4) { //move on to next round if not already in the final round
							//System.out.println("r = "+r+" p_"+ids[i]+" left = "+left+" right = "+right);
							if (left && right) {
								pcs[i].set_round(r+1,repl,repr); //next round is round r+1, which is rounds[r] because round 0 is not in list
							} else {
								competitor[i] = false;
							}
						}
					}
				}
			}
			File dir = new File (System.getProperty("user.dir")+"/testmodel");
			if (!dir.exists()) {
				if (!dir.mkdir()) {
					throw new IOException("Failed to create directory " + dir.getAbsolutePath());
				}
			}
			PnmlExport pex = new PnmlExport();
			try {
				if (reduced) {
					pex.exportObject(doc,"testmodel/ring_"+nb+"_abs.pnml");
					System.out.println("File ring_"+nb+"_abs.pnml exported to testmodel directory.");
				} else {
					pex.exportObject(doc,"testmodel/ring_"+nb+".pnml");
					System.out.println("File ring_"+nb+".pnml exported to testmodel directory.");
				}
			} catch (OCLValidationFailed e) {
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