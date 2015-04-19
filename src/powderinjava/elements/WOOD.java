package powderinjava.elements;

import powderinjava.Particle;
import powderinjava.State;


public class WOOD extends Element{

	public WOOD(){
		super("WOOD",State.SOLID,0xD99B38,100,true,false,false);
	}

	public int update(int x,int y,Particle p){
		return 0;
	}

	public void onSpawn(Particle p){
		
	}
}
