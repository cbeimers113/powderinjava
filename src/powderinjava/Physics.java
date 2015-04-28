/**
 * @Copyright 2015 firefreak11
 *
 *            This file is part of PowderInJava.
 *
 *            PowderInJava is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *            PowderInJava is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 *            You should have received a copy of the GNU General Public License along with PowderInJava. If not, see <http://www.gnu.org/licenses/>.
 **/

package powderinjava;

import static powderinjava.Powder.maxPres;
import static powderinjava.Powder.maxTemp;
import static powderinjava.Powder.minPres;
import static powderinjava.Powder.minTemp;
import static powderinjava.Powder.presConst;
import static powderinjava.Powder.pv;
import static powderinjava.Powder.tv;
import static powderinjava.Powder.vx;
import static powderinjava.Powder.vy;

public class Physics{

	private static Thread thread;

	public static void start(){
		thread=new Thread(){

			public void run(){
				while(!Powder.isCloseRequested)
					update();
			}
		};
		thread.start();
	}

	private static void update(){
		for(int y=0;y<Powder.HEIGHT;y++){
			for(int x=0;x<Powder.WIDTH;x++){
				if(!Powder.paused){
					float pAv=0.0f;
					float tAv=0.0f;
					float s=0.0f;
					for(int i=0;i<8;i++){
						int ax=i==0||i==6||i==7?-1:i==1||i==5?0:1;
						int ay=i==0||i==1||i==2?-1:i==3||i==7?0:1;
						if(x+ax>Powder.xMarginLeft&&x+ax<Powder.xMarginRight&&y+ay>Powder.yMarginTop&&y+ay<Powder.yMarginBottom){
							tAv+=tv[x+ax][y+ay];
							s++;
						}
					}
					tv[x][y]=tAv/s;
					s=0.0f;
					for(int i=0;i<16;i++){
						int ax=i==0||(i>=12&&i<=15)?-2:i==1||i==11?-1:i==2||i==10?0:i==3||i==9?1:2;
						int ay=i>=0&&i<=4?-2:i==5||i==15?-1:i==6||i==14?0:i==7||i==13?1:2;
						if(x+ax>Powder.xMarginLeft&&x+ax<Powder.xMarginRight&&y+ay>Powder.yMarginTop&&y+ay<Powder.yMarginBottom){
							pAv+=pv[x+ax][y+ay];
							s++;
						}
					}
					pv[x][y]=pAv/s;
					if(tv[x][y]>maxTemp) tv[x][y]=maxTemp;
					if(tv[x][y]<minTemp) tv[x][y]=minTemp;
					if(pv[x][y]>maxPres) pv[x][y]=maxPres;
					if(pv[x][y]<minPres) pv[x][y]=minPres;
					pv[x][y]+=presConst*(tv[x][y]/maxTemp);
					if(x-1>Powder.xMarginLeft&&x+1<Powder.xMarginRight&&y-1>Powder.yMarginTop&&y+1<Powder.yMarginBottom){
						float a=pv[x+1][y];
						float b=pv[x-1][y];
						float c=pv[x][y-1];
						float d=pv[x][y+1];
						vx[x][y]=b-a;
						vy[x][y]=c-d;
						int nvx=x+(int)Math.ceil(vx[x][y]);
						int nvy=y+(int)Math.ceil(vy[x][y]);
						if(nvx<Powder.xMarginRight||nvx>Powder.xMarginLeft||nvy<Powder.yMarginTop||nvy>Powder.yMarginBottom)continue;
						float mag=(float)Math.sqrt(nvx*nvx+nvy*nvy);
						int[]normal=new int[]{(int)Math.ceil(nvx/mag),(int)Math.ceil(nvy/mag)};
						pv[normal[0]][normal[1]]=pv[x][y]=(pv[normal[0]][normal[1]]+pv[x][y])/2;
						displacePressure(x,y,normal[0]*2,normal[1]*2);
					}
				}
			}
		}
	}
	
	private static void displacePressure(int x, int y, int xd, int yd){
		int px=x;
		int py=y;
		try{
			float ppv=pv[(int)(x+xd+vx[x][y])][(int)(y+yd+vy[x][y])];
			x+=xd+vx[px][py];
			y+=yd+vy[px][py];
			pv[x][y]=pv[px][py];
			pv[px][py]=ppv;
		}catch(ArrayIndexOutOfBoundsException e){
			return;
		}
	}
}
