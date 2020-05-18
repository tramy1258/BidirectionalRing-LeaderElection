package ringleader;
import fr.lip6.move.pnml.ptnet.hlapi.TransitionHLAPI;

public class Result {
	private TransitionHLAPI hdl;
	private int res;
	
	public Result (TransitionHLAPI hdl, int res) {
		this.res = res;
		this.hdl = hdl;
	}
	
	public int get_res() {
		return res;
	}
	
	public TransitionHLAPI get_hdl() {
		return hdl;
	}
}
