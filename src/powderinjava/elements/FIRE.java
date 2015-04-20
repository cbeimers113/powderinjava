package powderinjava.elements;

import java.awt.Color;
import powderinjava.Particle;
import powderinjava.Physics;
import powderinjava.State;

public class FIRE extends Element{

	private int flameTimer=0;
	
	public FIRE(){
		super("FIRE",State.PLASMA,0xff0000,0,false,false,false);
	}

	public int update(int x,int y,Particle p){
		try{
			p.extraColour=new Color(255,(int)(p.temp/p.tempInit)*255,0,colour.getAlpha());
		}catch(IllegalArgumentException e){
			p.extraColour=colour;
		}
		if(--p.life==0){
			changeType(p.x,p.y,SMKE);
			return 1;
		}
		if(elementAt(x,y).flammable){
			if(++flameTimer%Physics.getBurnRate(elementAt(x,y))==0){
				changeType(x,y,this);
				return 1;
			}
		}
		return 0;
	}
}
