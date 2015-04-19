package powderinjava.elements;

import powderinjava.Particle;
import powderinjava.Physics;
import powderinjava.State;

public class FIRE extends Element{

	private int flameTimer=0;
	
	public FIRE(){
		super("FIRE",State.PLASMA,0xff0000,0,false,false,false);
	}

	public int update(int x,int y,Particle p){
		if(elementAt(x,y).flammable){
			if(++flameTimer%Physics.getBurnRate(elementAt(x,y))==0){
				changePart(x,y,this);
				return 1;
			}
		}
		return 0;
	}

	public void onSpawn(Particle p){
		p.temp=400.0f;
	}
}
