package powderinjava.elements;

import powderinjava.Particle;
import powderinjava.State;

public class COAL extends Element{

	public COAL(){
		super("COAL",State.SOLID,0x222222,100,true,false,false);
	}

	public int update(int x,int y,Particle p){
		return 0;
	}
}
