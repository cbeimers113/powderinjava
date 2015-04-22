/**
 *	@Copyright 2015 firefreak11
 *
 *	This file is part of PowderInJava.
 *
 *	PowderInJava is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.
 *
 *	PowderInJava is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.
 *
 *	You should have received a copy of the GNU General Public License
 *	along with PowderInJava.  If not, see <http://www.gnu.org/licenses/>.
 **/

package powderinjava;

import java.awt.image.BufferedImage;
import powderinjava.elements.Element;

public class Physics{

	public static Particle[][] pmap=new Particle[Main.powder.width][Main.powder.height];
	public static float[][] pv=new float[Main.powder.width][Main.powder.height];
	public static float[][] tv=new float[Main.powder.width][Main.powder.height];
	public static float maxTemp=10_000.0f;
	public static float minTemp=-maxTemp;
	public static int defaultBurnRate=50;

	public BufferedImage tempView;
	
	public int width;
	public int height;

	public Physics(int width,int height){
		this.width=width;
		this.height=height;
		for(int y=0;y<Main.powder.height;y++){
			for(int x=0;x<Main.powder.width;x++){
				pv[x][y]=0.0f;
				tv[x][y]=22.0f;
			}
		}
	}

	public void update(){
		for(int y=0;y<height;y++){
			for(int x=0;x<width;x++){
				// Average pressure and temperature
				float pAv=0.0f;
				float tAv=0.0f;
				float success=0.0f;
				for(int i=0;i<8;i++){
					int ax=i==0||i==6||i==7?-1:i==1||i==5?0:1;
					int ay=i==0||i==1||i==2?-1:i==3||i==7?0:1;
					if(x+ax>Powder.xMarginLeft&&x+ax<Powder.xMarginRight()&&y+ay>Powder.yMarginTop&&y+ay<Powder.yMarginBottom()){
						pAv+=pv[x+ax][y+ay];
						tAv+=tv[x+ax][y+ay];
						success++;
					}
				}
				pv[x][y]=pAv/success;
				tv[x][y]=tAv/success;
				tempView.setRGB(x,y,(int)((tv[x][y]/1_000_000.0f)*255));
			}
		}
	}

	public void addAir(int x,int y,float pressure){
		pv[x][y]+=pressure;
	}

	public static int getBurnRate(Element e){
		if(!e.flammable) return 0;
		if(e==Element.OXGN||e==Element.HYGN){
			return 10;
		}else if(e==Element.WOOD){
			return 50;
		}else if(e==Element.COAL){
			return 150;
		}else if(e==Element.C4){
			return 1;
		}else{
			return defaultBurnRate;
		}
	}
}
