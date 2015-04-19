package powderinjava.elements;

import powderinjava.Particle;
import powderinjava.State;


public class HYGN extends Element{

	public HYGN(){
		super("HYGN",State.GAS,0x0D86FF,0,true,false,false);
	}
	
	public int update(int x, int y, Particle p){
		return 0;
	}

	public void onSpawn(Particle p){
		
	}
}
