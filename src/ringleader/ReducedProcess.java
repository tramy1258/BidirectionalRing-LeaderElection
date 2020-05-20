package ringleader;
import fr.lip6.move.pnml.ptnet.hlapi.PageHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.PlaceHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.PositionHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.TransitionHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.AnnotationGraphicsHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.ArcHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.ArcGraphicsHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.DimensionHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.NameHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.NodeGraphicsHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.ArcGraphicsHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.OffsetHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.PTMarkingHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.PTArcAnnotationHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.LineHLAPI;
import fr.lip6.move.pnml.ptnet.CSS2Color;
import fr.lip6.move.pnml.framework.utils.exception.InvalidIDException;
import fr.lip6.move.pnml.framework.utils.exception.VoidRepositoryException;
import java.lang.Math;
import java.util.List;
import java.util.LinkedList;
public class ReducedProcess {
	
	protected int id;
	protected int half;
	protected int lastx;       //x for handling position
	protected int lasty = 200; //y for handling position
	protected int x;           //x of initial position
	protected PageHLAPI page;
	protected PlaceHLAPI init;
	protected TransitionHLAPI start;
	protected PlaceHLAPI msg;
	protected List<TransitionHLAPI> rounds = new LinkedList<TransitionHLAPI>();

	public ReducedProcess(PageHLAPI page, int id, int x, int y, int half) {
		this.id = id;
		this.half = half;
		this.page = page;
		this.x = x;
		this.lastx = x-150;
		try {
			
			init = new PlaceHLAPI("init"+id, page);
			init.setInitialMarkingHLAPI(new PTMarkingHLAPI(1L));
			NodeGraphicsHLAPI pg1 = new NodeGraphicsHLAPI(init);
			PositionHLAPI pos1 = new PositionHLAPI(x,y,pg1);
			DimensionHLAPI dim1 = new DimensionHLAPI(25,25,pg1);
			OffsetHLAPI o1 = new OffsetHLAPI(-init.getId().length()*5/2,-30,new AnnotationGraphicsHLAPI(new NameHLAPI(init.getId(),init)));
			OffsetHLAPI omk1 = new OffsetHLAPI(-5,-10,new AnnotationGraphicsHLAPI(init.getInitialMarkingHLAPI()));
			LineHLAPI l1 = new LineHLAPI(pg1);
			l1.setColorHLAPI(CSS2Color.OLIVE);
			
			start = new TransitionHLAPI("start"+id, page);
			NodeGraphicsHLAPI pg2 = new NodeGraphicsHLAPI(start);
			PositionHLAPI pos2 = new PositionHLAPI(x,y+50,pg2);
			DimensionHLAPI dim2 = new DimensionHLAPI(25,10,pg2);
			OffsetHLAPI o2 = new OffsetHLAPI(-start.getId().length()*5-15,-15,new AnnotationGraphicsHLAPI(new NameHLAPI(start.getId(),start)));
			LineHLAPI l2 = new LineHLAPI(pg2);
			l2.setColorHLAPI(CSS2Color.OLIVE);
			
			msg = new PlaceHLAPI("newmsg"+id, page);
			NodeGraphicsHLAPI pg3 = new NodeGraphicsHLAPI(msg);
			PositionHLAPI pos3 = new PositionHLAPI(x,y+130,pg3);
			DimensionHLAPI dim3 = new DimensionHLAPI(25,25,pg3);
			OffsetHLAPI o3 = new OffsetHLAPI(-msg.getId().length()*5-30,-15,new AnnotationGraphicsHLAPI(new NameHLAPI(msg.getId(),msg)));
			LineHLAPI l3 = new LineHLAPI(pg3);
			l3.setColorHLAPI(CSS2Color.AQUA);
			
			ArcHLAPI a0 = new ArcHLAPI(init.getId()+"___"+start.getId(),init,start,page);
			ArcGraphicsHLAPI ag0 = new ArcGraphicsHLAPI(a0);
			LineHLAPI agl0 = new LineHLAPI(ag0);
			agl0.setColorHLAPI(CSS2Color.BLACK);
			ArcHLAPI a1 = new ArcHLAPI(start.getId()+"___"+msg.getId(),start,msg,page);
			ArcGraphicsHLAPI ag1 = new ArcGraphicsHLAPI(a1);
			LineHLAPI agl1 = new LineHLAPI(ag1);
			agl0.setColorHLAPI(CSS2Color.BLACK);
			
			//System.out.println("-->"+a0.getId());
			//System.out.println("-->"+a1.getId());
		} catch (InvalidIDException e) {
			e.printStackTrace();
		} catch (VoidRepositoryException e) {
			e.printStackTrace();	
		}
	}
	
	public TransitionHLAPI get_start() {
		return start;
	}
	
	public TransitionHLAPI get_round(int id_in_list_round) {//index of round r will be (r-1) because round 0 not in list
		return rounds.get(id_in_list_round);
	}
	
	public void set_lastx() {
		this.lastx = x-150;
	}
	public void set_round(int r, PlaceHLAPI src1, PlaceHLAPI src2) {
		try {
			
			rounds.add(new TransitionHLAPI("next_round"+r+"_"+this.id,page));
			NodeGraphicsHLAPI pg = new NodeGraphicsHLAPI(rounds.get(r-1));
			DimensionHLAPI dim = new DimensionHLAPI(10,25,pg);
			OffsetHLAPI o = new OffsetHLAPI(-rounds.get(r-1).getId().length()*5/2,-30,new AnnotationGraphicsHLAPI(new NameHLAPI(rounds.get(r-1).getId(),rounds.get(r-1))));
			LineHLAPI l = new LineHLAPI(pg);
			l.setColorHLAPI(CSS2Color.RED);
			//setting position of transition based on position of source
			int x1 = src1.getNodegraphicsHLAPI().getPositionHLAPI().getX();
			int x2 = src2.getNodegraphicsHLAPI().getPositionHLAPI().getX();
			PositionHLAPI pos = new PositionHLAPI((x1+x2)/2,src1.getNodegraphicsHLAPI().getPositionHLAPI().getY()+70,pg);
			
			ArcHLAPI lt2next = new ArcHLAPI(src1.getId()+"___"+rounds.get(r-1).getId(),src1,rounds.get(r-1),page);
			ArcGraphicsHLAPI aglt = new ArcGraphicsHLAPI(lt2next);
			LineHLAPI llt = new LineHLAPI(aglt);
			llt.setColorHLAPI(CSS2Color.BLACK);
			
			ArcHLAPI rt2next = new ArcHLAPI(src2.getId()+"___"+rounds.get(r-1).getId(),src2,rounds.get(r-1),page);
			ArcGraphicsHLAPI agrt = new ArcGraphicsHLAPI(rt2next);
			LineHLAPI lrt = new LineHLAPI(agrt);
			lrt.setColorHLAPI(CSS2Color.BLACK);
			
			//System.out.println("-->"+lt2next.getId());
			//System.out.println("-->"+rt2next.getId());
			
		} catch (InvalidIDException e) {
			e.printStackTrace();
		} catch (VoidRepositoryException e) {
			e.printStackTrace();	
		}
	}
	
	public PlaceHLAPI get_election(String dir, int id, int r, int d, TransitionHLAPI src) { //ready2dir_election(id,r,d)_this.id
		//System.out.println("election_"+dir+"("+id+","+r+","+d+")_"+this.id);
		try {
			PlaceHLAPI election = new PlaceHLAPI("election_"+id+"_"+r+"_"+d+"_to_"+dir+"_"+this.id,page);
			NodeGraphicsHLAPI pg = new NodeGraphicsHLAPI(election);
			DimensionHLAPI dim = new DimensionHLAPI(25,25,pg);
			OffsetHLAPI o = new OffsetHLAPI(-election.getId().length()*5/2,-30,new AnnotationGraphicsHLAPI(new NameHLAPI(election.getId(),election)));
			LineHLAPI l = new LineHLAPI(pg);
			if (r == 0) {
				l.setColorHLAPI(CSS2Color.OLIVE);
			} else {
				l.setColorHLAPI(CSS2Color.MAROON);
			}
			PositionHLAPI pos = null;

			//setting position of place based on position of source
			//int x =  src.getNodegraphicsHLAPI().getPositionHLAPI().getX().intValue();
			int y = src.getNodegraphicsHLAPI().getPositionHLAPI().getY().intValue();
			if (dir == "left" ) {
				pos = new PositionHLAPI(this.x-this.half,y+25,pg);
			} else {
				pos = new PositionHLAPI(this.x+this.half,y+75,pg);
			}
			
			ArcHLAPI arc = new ArcHLAPI(src.getId()+"___"+election.getId(),src,election,page);
			
			//System.out.println("-->"+arc.getId());
			return election;
			
		} catch (InvalidIDException e) {
			e.printStackTrace();
			return null;
		} catch (VoidRepositoryException e) {
			e.printStackTrace();	
			return null;
		} catch (NullPointerException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public PlaceHLAPI get_reply(String dir, int id, int r, TransitionHLAPI src) {
		//System.out.println("reply_"+dir+"("+id+","+r+")_"+this.id);
		try {
			
			PlaceHLAPI reply = new PlaceHLAPI("reply_"+id+"_"+r+"_to_"+dir+"_"+this.id,page);
			NodeGraphicsHLAPI pg = new NodeGraphicsHLAPI(reply);
			DimensionHLAPI dim = new DimensionHLAPI(25,25,pg);
			OffsetHLAPI o = new OffsetHLAPI(-reply.getId().length()*5/2,-30,new AnnotationGraphicsHLAPI(new NameHLAPI(reply.getId(),reply)));
			LineHLAPI l = new LineHLAPI(pg);
			l.setColorHLAPI(CSS2Color.ORANGE);
			PositionHLAPI pos = null;

			//setting position of place based on position of source
			//int x =  src.getNodegraphicsHLAPI().getPositionHLAPI().getX().intValue();
			int y = src.getNodegraphicsHLAPI().getPositionHLAPI().getY().intValue();
			if (dir == "left" ) {
				pos = new PositionHLAPI(this.x-this.half,y+40,pg);
			} else {
				pos = new PositionHLAPI(this.x+this.half,y+55,pg);
			}
			
			ArcHLAPI arc = new ArcHLAPI(src.getId()+"___"+reply.getId(),src,reply,page);
			
			//System.out.println("-->"+arc.getId());
			return reply;
			
		} catch (InvalidIDException e) {
			e.printStackTrace();
			return null;
		} catch (VoidRepositoryException e) {
			e.printStackTrace();	
			return null;
		} catch (NullPointerException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public PlaceHLAPI get_elected(int id, PlaceHLAPI src, PlaceHLAPI yes, PlaceHLAPI not) {
		//System.out.println("elected("+id+")_"+this.id);
		try {
			
			TransitionHLAPI elected = new TransitionHLAPI("elected_"+id+"_"+this.id,page);
			NodeGraphicsHLAPI pg = new NodeGraphicsHLAPI(elected);
			DimensionHLAPI dim = new DimensionHLAPI(10,25,pg);
			OffsetHLAPI o = new OffsetHLAPI(-elected.getId().length()*5/2,-30,new AnnotationGraphicsHLAPI(new NameHLAPI(elected.getId(),elected)));
			LineHLAPI l = new LineHLAPI(pg);
			l.setColorHLAPI(CSS2Color.NAVY);
			PositionHLAPI pos = new PositionHLAPI(x,src.getNodegraphicsHLAPI().getPositionHLAPI().getY()+10,pg);
			
			ArcHLAPI arc1 = new ArcHLAPI(src.getId()+"___"+elected.getId(),src,elected,page);			
			ArcHLAPI arc2 = new ArcHLAPI(this.msg.getId()+"___"+elected.getId(),this.msg,elected,page);
			ArcGraphicsHLAPI ag2 = new ArcGraphicsHLAPI(arc2);
			LineHLAPI agl2 = new LineHLAPI(ag2);
			agl2.setColorHLAPI(CSS2Color.BLACK);
			
			//System.out.println("-->"+arc1.getId());
			//System.out.println("-->"+arc2.getId());
			
			if (this.id != id) {
				ArcHLAPI arc = new ArcHLAPI(elected.getId()+"___"+not.getId(),elected,not,page);
				ArcGraphicsHLAPI ag = new ArcGraphicsHLAPI(arc);
				LineHLAPI agl = new LineHLAPI(ag);
				agl.setColorHLAPI(CSS2Color.BLACK);
				
				PlaceHLAPI rd = new PlaceHLAPI("elected_"+id+"_to_left_"+this.id,page);
				NodeGraphicsHLAPI pg0 = new NodeGraphicsHLAPI(rd);
				DimensionHLAPI dim0 = new DimensionHLAPI(25,25,pg0);
				OffsetHLAPI o0 = new OffsetHLAPI(-rd.getId().length()*5/2,-30,new AnnotationGraphicsHLAPI(new NameHLAPI(rd.getId(),rd)));
				LineHLAPI l0 = new LineHLAPI(pg0);
				l0.setColorHLAPI(CSS2Color.NAVY);
				PositionHLAPI pos0 = new PositionHLAPI(this.x-this.half,src.getNodegraphicsHLAPI().getPositionHLAPI().getY()+60,pg0);
				
				ArcHLAPI arc3 = new ArcHLAPI(elected.getId()+"___"+rd.getId(),elected,rd,page);
				
				//System.out.println("-->"+arc.getId());
				//System.out.println("-->"+arc3.getId());
				return rd;
			} else {
				ArcHLAPI arc = new ArcHLAPI(elected.getId()+"___"+yes.getId(),elected,yes,page);
				ArcGraphicsHLAPI ag = new ArcGraphicsHLAPI(arc);
				LineHLAPI agl = new LineHLAPI(ag);
				agl.setColorHLAPI(CSS2Color.BLACK);
				
				//System.out.println("-->"+arc.getId());
				return null;
			}
						
		} catch (InvalidIDException e) {
			e.printStackTrace();
			return null;
		} catch (VoidRepositoryException e) {
			e.printStackTrace();	
			return null;
		} catch (NullPointerException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Result get_handling_election(String dir, int id, int r, int d, PlaceHLAPI src) {
		//System.out.println("handling election "+dir+","+id+","+r+","+d+" by "+this.id);
		try {
			int y = 190*r;
			
			TransitionHLAPI t1 = new TransitionHLAPI("election_"+id+"_"+r+"_"+d+"_from_"+dir+"_"+this.id,page);
			NodeGraphicsHLAPI pg0 = new NodeGraphicsHLAPI(t1);
			DimensionHLAPI dim0 = new DimensionHLAPI(10,25,pg0);
			OffsetHLAPI o0 = new OffsetHLAPI(-t1.getId().length()*5/2,-30,new AnnotationGraphicsHLAPI(new NameHLAPI(t1.getId(),t1)));
			LineHLAPI l0 = new LineHLAPI(pg0);
			l0.setColorHLAPI(CSS2Color.MAROON);
			PositionHLAPI pos0 = new PositionHLAPI(lastx,lasty+y,pg0);
			
			this.lastx = this.lastx + 85;
			this.lasty = this.lasty + 20;
			
			ArcHLAPI a0 = new ArcHLAPI(src.getId()+"___"+t1.getId(),src,t1,page);
			ArcHLAPI a1 = new ArcHLAPI(this.msg.getId()+"___"+t1.getId(),this.msg,t1,page);
			ArcGraphicsHLAPI ag1 = new ArcGraphicsHLAPI(a1);
			LineHLAPI agl1 = new LineHLAPI(ag1);
			agl1.setColorHLAPI(CSS2Color.BLACK);
			
			ArcHLAPI a4 = new ArcHLAPI(t1.getId()+"___"+this.msg.getId(),t1,this.msg,page);
			ArcGraphicsHLAPI ag4 = new ArcGraphicsHLAPI(a4);
			LineHLAPI agl4 = new LineHLAPI(ag4);
			agl4.setColorHLAPI(CSS2Color.BLACK);
			
			//System.out.println("-->"+a0.getId());
			//System.out.println("-->"+a1.getId());
			//System.out.println("-->"+a4.getId());
			
			int res = 3; //id<this.id -> skip
			if (id > this.id) {
				if (d < (int) Math.pow(2, r)) {
					res = 1; //-> election (id,r,d+1) to next neighbor
				} else {
					res = 2; //-> reply (id,r) back to sender
				}
			} else if (id == this.id) {
				res = 4; //-> elected(id) to left
			}
			
			return new Result(t1,res);
			
		} catch (InvalidIDException e) {
			e.printStackTrace();
			return null;
		} catch (VoidRepositoryException e) {
			e.printStackTrace();	
			return null;
		} catch (NullPointerException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Result get_handling_reply(String dir, int id, int r, PlaceHLAPI src) {
		//System.out.println("handling reply "+dir+","+id+","+r+" by "+this.id);
		try {			
			int y = 190*r;
			
			TransitionHLAPI t1 = new TransitionHLAPI("reply_"+id+"_"+r+"_from_"+dir+"_"+this.id,page);
			NodeGraphicsHLAPI pg0 = new NodeGraphicsHLAPI(t1);
			DimensionHLAPI dim0 = new DimensionHLAPI(10,25,pg0);
			OffsetHLAPI o0 = new OffsetHLAPI(-t1.getId().length()*5/2,-30,new AnnotationGraphicsHLAPI(new NameHLAPI(t1.getId(),t1)));
			LineHLAPI l0 = new LineHLAPI(pg0);
			l0.setColorHLAPI(CSS2Color.ORANGE);
			PositionHLAPI pos0 = new PositionHLAPI(lastx,lasty+y,pg0);
			
			this.lastx = this.lastx + 85;
			this.lasty = this.lasty + 20; 
			
			ArcHLAPI a0 = new ArcHLAPI(src.getId()+"___"+t1.getId(),src,t1,page);
			ArcHLAPI a1 = new ArcHLAPI(this.msg.getId()+"___"+t1.getId(),this.msg,t1,page);
			ArcGraphicsHLAPI ag1 = new ArcGraphicsHLAPI(a1);
			LineHLAPI agl1 = new LineHLAPI(ag1);
			agl1.setColorHLAPI(CSS2Color.BLACK);
			
			ArcHLAPI a4 = new ArcHLAPI(t1.getId()+"___"+this.msg.getId(),t1,this.msg,page);
			ArcGraphicsHLAPI ag4 = new ArcGraphicsHLAPI(a4);
			LineHLAPI agl4 = new LineHLAPI(ag4);
			agl4.setColorHLAPI(CSS2Color.BLACK);
			
			//System.out.println("-->"+a0.getId());
			//System.out.println("-->"+a1.getId());
			//System.out.println("-->"+a4.getId());
			
			int res = 1; //-> reply(id,r) to next neighbor
			if (id == this.id) {
				res = 2; //id == this.id
			}

			return new Result(t1,res);
			
		} catch (InvalidIDException e) {
			e.printStackTrace();
			return null;
		} catch (VoidRepositoryException e) {
			e.printStackTrace();	
			return null;
		} catch (NullPointerException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
