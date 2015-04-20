package powderinjava.elements;

import powderinjava.Particle;
import powderinjava.State;


public class SMKE extends Element{

	public SMKE(){
		super("SMKE",State.PLASMA,0x454545,0,false,false,false);
	}

	public int update(int x,int y,Particle p){
		if(--p.life==0){
			p.remove();
			return 1;
		}
		if(p.temp>=400.0f){
			changeType(p.x,p.y,FIRE);
			return 1;
		}
		return 0;
	}
}
