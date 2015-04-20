package powderinjava.elements;

import powderinjava.Particle;
import powderinjava.State;


public class SALT extends Element{

	public SALT(){
		super("SALT",State.POWDER,0xffffff,50,false,true,false);
	}
	
	public int update(int x,int y,Particle p){
		if(elementAt(x,y)==STNE){
			changeType(x,y,WOOD);
			return 1;
		}
		return 0;
	}
}
