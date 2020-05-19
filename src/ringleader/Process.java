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
public class Process extends ReducedProcess{

	public Process(PageHLAPI page, int id, int x, int y, int half) {
		super(page, id, x, y, half);
	}
	
	public Result get_handling_election(String dir, int id, int r, int d, PlaceHLAPI src) {
		//System.out.println("handling election "+dir+","+id+","+r+","+d+" by "+this.id);
		try {
			int y = 290*r;
			
			TransitionHLAPI t1 = new TransitionHLAPI("election_"+id+"_"+r+"_"+d+"_from_"+dir+"_"+this.id,page);
			NodeGraphicsHLAPI pg0 = new NodeGraphicsHLAPI(t1);
			DimensionHLAPI dim0 = new DimensionHLAPI(10,25,pg0);
			OffsetHLAPI o0 = new OffsetHLAPI(-t1.getId().length()*5/2,-30,new AnnotationGraphicsHLAPI(new NameHLAPI(t1.getId(),t1)));
			LineHLAPI l0 = new LineHLAPI(pg0);
			l0.setColorHLAPI(CSS2Color.MAROON);
			PositionHLAPI pos0 = new PositionHLAPI(lastx,lasty+y,pg0);
	
			PlaceHLAPI c1 = new PlaceHLAPI("rcv_"+dir+"_election_"+id+"_"+r+"_"+d+"_"+this.id,page);
			NodeGraphicsHLAPI pg1 = new NodeGraphicsHLAPI(c1);
			DimensionHLAPI dim1 = new DimensionHLAPI(25,25,pg1);
			OffsetHLAPI o1 = new OffsetHLAPI(-c1.getId().length()*5/2,-30,new AnnotationGraphicsHLAPI(new NameHLAPI(c1.getId(),c1)));
			LineHLAPI l1 = new LineHLAPI(pg1);
			l1.setColorHLAPI(CSS2Color.MAROON);
			PositionHLAPI pos1 = new PositionHLAPI(lastx,lasty+y+60,pg1);

			TransitionHLAPI t2 = new TransitionHLAPI("hdl_"+dir+"_election_"+id+"_"+r+"_"+d+"_"+this.id,page);
			NodeGraphicsHLAPI pg2 = new NodeGraphicsHLAPI(t2);
			DimensionHLAPI dim2 = new DimensionHLAPI(10,25,pg2);
			OffsetHLAPI o2 = new OffsetHLAPI(-t2.getId().length()*5/2,-30,new AnnotationGraphicsHLAPI(new NameHLAPI(t2.getId(),t2)));
			LineHLAPI l2 = new LineHLAPI(pg2);
			l2.setColorHLAPI(CSS2Color.MAROON);
			PositionHLAPI pos2 = new PositionHLAPI(lastx,lasty+y+120,pg2);
			
			this.lastx = this.lastx + 85;
			this.lasty = this.lasty + 20;
			
			ArcHLAPI a0 = new ArcHLAPI(src.getId()+"___"+t1.getId(),src,t1,page);
			ArcHLAPI a1 = new ArcHLAPI(this.msg.getId()+"___"+t1.getId(),this.msg,t1,page);
			ArcGraphicsHLAPI ag1 = new ArcGraphicsHLAPI(a1);
			LineHLAPI agl1 = new LineHLAPI(ag1);
			agl1.setColorHLAPI(CSS2Color.BLACK);
			
			ArcHLAPI a2 = new ArcHLAPI(t1.getId()+"___"+c1.getId(),t1,c1,page);
			ArcGraphicsHLAPI ag2 = new ArcGraphicsHLAPI(a2);
			LineHLAPI agl2 = new LineHLAPI(ag2);
			agl2.setColorHLAPI(CSS2Color.BLACK);
			
			ArcHLAPI a3 = new ArcHLAPI(c1.getId()+"___"+t2.getId(),c1,t2,page);
			ArcGraphicsHLAPI ag3 = new ArcGraphicsHLAPI(a3);
			LineHLAPI agl3 = new LineHLAPI(ag3);
			agl3.setColorHLAPI(CSS2Color.BLACK);
			
			ArcHLAPI a4 = new ArcHLAPI(t2.getId()+"___"+this.msg.getId(),t2,this.msg,page);
			ArcGraphicsHLAPI ag4 = new ArcGraphicsHLAPI(a4);
			LineHLAPI agl4 = new LineHLAPI(ag4);
			agl4.setColorHLAPI(CSS2Color.BLACK);
			
			//System.out.println("-->"+a0.getId());
			//System.out.println("-->"+a1.getId());
			//System.out.println("-->"+a2.getId());
			//System.out.println("-->"+a3.getId());
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
			
			return new Result(t2,res);
			
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
			int y = 290*r;
			
			TransitionHLAPI t1 = new TransitionHLAPI("reply_"+id+"_"+r+"_from_"+dir+"_"+this.id,page);
			NodeGraphicsHLAPI pg0 = new NodeGraphicsHLAPI(t1);
			DimensionHLAPI dim0 = new DimensionHLAPI(10,25,pg0);
			OffsetHLAPI o0 = new OffsetHLAPI(-t1.getId().length()*5/2,-30,new AnnotationGraphicsHLAPI(new NameHLAPI(t1.getId(),t1)));
			LineHLAPI l0 = new LineHLAPI(pg0);
			l0.setColorHLAPI(CSS2Color.ORANGE);
			PositionHLAPI pos0 = new PositionHLAPI(lastx,lasty+y,pg0);
			
			PlaceHLAPI c1 = new PlaceHLAPI("rcv_"+dir+"_reply_"+id+"_"+r+"_"+this.id,page);
			NodeGraphicsHLAPI pg1 = new NodeGraphicsHLAPI(c1);
			DimensionHLAPI dim1 = new DimensionHLAPI(25,25,pg1);
			OffsetHLAPI o1 = new OffsetHLAPI(-c1.getId().length()*5/2,-30,new AnnotationGraphicsHLAPI(new NameHLAPI(c1.getId(),c1)));
			LineHLAPI l1 = new LineHLAPI(pg1);
			l1.setColorHLAPI(CSS2Color.ORANGE);
			PositionHLAPI pos1 = new PositionHLAPI(lastx,lasty+y+60,pg1);
			
			TransitionHLAPI t2 = new TransitionHLAPI("hdl_"+dir+"_reply_"+id+"_"+r+"_"+this.id,page);
			NodeGraphicsHLAPI pg2 = new NodeGraphicsHLAPI(t2);
			DimensionHLAPI dim2 = new DimensionHLAPI(10,25,pg2);
			OffsetHLAPI o2 = new OffsetHLAPI(-t2.getId().length()*5/2,-30,new AnnotationGraphicsHLAPI(new NameHLAPI(t2.getId(),t2)));
			LineHLAPI l2 = new LineHLAPI(pg2);
			l2.setColorHLAPI(CSS2Color.ORANGE);
			PositionHLAPI pos2 = new PositionHLAPI(lastx,lasty+y+120,pg2);
			
			this.lastx = this.lastx + 85;
			this.lasty = this.lasty + 20; 
			
			ArcHLAPI a0 = new ArcHLAPI(src.getId()+"___"+t1.getId(),src,t1,page);
			ArcHLAPI a1 = new ArcHLAPI(this.msg.getId()+"___"+t1.getId(),this.msg,t1,page);
			ArcGraphicsHLAPI ag1 = new ArcGraphicsHLAPI(a1);
			LineHLAPI agl1 = new LineHLAPI(ag1);
			agl1.setColorHLAPI(CSS2Color.BLACK);
			
			ArcHLAPI a2 = new ArcHLAPI(t1.getId()+"___"+c1.getId(),t1,c1,page);
			ArcGraphicsHLAPI ag2 = new ArcGraphicsHLAPI(a2);
			LineHLAPI agl2 = new LineHLAPI(ag2);
			agl2.setColorHLAPI(CSS2Color.BLACK);
			
			ArcHLAPI a3 = new ArcHLAPI(c1.getId()+"___"+t2.getId(),c1,t2,page);
			ArcGraphicsHLAPI ag3 = new ArcGraphicsHLAPI(a3);
			LineHLAPI agl3 = new LineHLAPI(ag3);
			agl3.setColorHLAPI(CSS2Color.BLACK);
			
			ArcHLAPI a4 = new ArcHLAPI(t2.getId()+"___"+this.msg.getId(),t2,this.msg,page);
			ArcGraphicsHLAPI ag4 = new ArcGraphicsHLAPI(a4);
			LineHLAPI agl4 = new LineHLAPI(ag4);
			agl4.setColorHLAPI(CSS2Color.BLACK);
			
			//System.out.println("-->"+a0.getId());
			//System.out.println("-->"+a1.getId());
			//System.out.println("-->"+a2.getId());
			//System.out.println("-->"+a3.getId());
			//System.out.println("-->"+a4.getId());
			
			int res = 1; //-> reply(id,r) to next neighbor
			if (id == this.id) {
				res = 2; //id == this.id
			}
			
			return new Result(t2,res);
			
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
