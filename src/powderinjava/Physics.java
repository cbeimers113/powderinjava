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

import powderinjava.elements.Element;


public class Physics{
	
	private static int defaultBurnRate;
		
	public float[][]vx;
	public float[][]vy;
	public int width;
	public int height;
	
	public Physics(int width, int height){
		this.width=width;
		this.height=height;
		vx=new float[width][height];
		vy=new float[width][height];
		for(int y=0;y<height;y++)
			for(int x=0;x<width;x++)
				vx[x][y]=vy[x][y]=0.0f;
		defaultBurnRate=50;
	}
	
	public void update(){
		for(int y=0;y<height;y++){
			for(int x=0;x<width;x++){
				float vxAv=0.0f;
				float vyAv=0.0f;
				for(int i=0;i<8;i++){
					int ax=i==0||i==6||i==7?-1:i==1||i==5?0:1;
					int ay=i==0||i==1||i==2?-1:i==3||i==7?0:1;
					try{
						vxAv+=vx[ax][ay];
					}catch(ArrayIndexOutOfBoundsException e){
						continue;
					}
					try{
						vyAv+=vy[ax][ay];
					}catch(ArrayIndexOutOfBoundsException e){
						continue;
					}
				}
				vx[x][y]=vxAv/8.0f;
				vy[x][y]=vyAv/8.0f;
			}
		}
	}
	
	public void addAir(int x, int y, int radius){
		
	}
	
	public float getVelocity(int x, int y){
		return (float)Math.sqrt(vx[x][y]*vx[x][y]+vy[x][y]*vy[x][y]);
	}
	
	public static int getBurnRate(Element e){
		if(!e.flammable)return 0;
		return e.equals(Element.WOOD)?defaultBurnRate:e.equals(Element.OXGN)||e.equals(Element.HYGN)?5:defaultBurnRate;
	}
}
