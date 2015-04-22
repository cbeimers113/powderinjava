package powderinjava.elements;

import powderinjava.Particle;
import powderinjava.State;


public class PLSM extends Element{

	public PLSM(){
		super("PLSM",State.PLASMA,0xc636ff,0,0.05f,false,false,false);
	}

	public int update(int x,int y,Particle p){
		return 0;
	}
}
