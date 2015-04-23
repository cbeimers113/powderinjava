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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import powderinjava.elements.Element;

public class Physics{

	public static Particle[][] pmap=new Particle[Main.powder.width][Main.powder.height];
	public static float[][] pv=new float[Main.powder.width][Main.powder.height];
	public static float[][] tv=new float[Main.powder.width][Main.powder.height];
	public static float maxTemp=10_000.0f;
	public static float minTemp=-maxTemp;
	public static int defaultBurnRate=50;

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

	public void update(Graphics g,BufferedImage img){
		for(int y=0;y<height;y++){
			for(int x=0;x<width;x++){
				// Average pressure and temperature
				if(!Main.powder.paused){
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
					if(tv[x][y]>maxTemp) tv[x][y]=maxTemp;
					if(tv[x][y]<minTemp) tv[x][y]=minTemp;
				}
				// /TODO: Pressure check
				Particle p=pmap[x][y];
				if(!Main.powder.paused&&p!=null) p.update();
				if(Main.powder.tempGraphics&&x>Powder.xMarginLeft&&x<Powder.xMarginRight()&&y>Powder.yMarginTop&&y<Powder.yMarginBottom()){
					boolean ambient=p==null;
					img.setRGB(x,y,getTempColour(ambient?tv[x][y]:p.temp).getRGB());
				}
				// Update Particles
				if(p==null) continue;
				if(p.removeQueue){
					Physics.pmap[x][y]=null;
					continue;
				}
				if(Main.powder.fancyGraphics){
					Color glow=new Color(p.extraColour.getRed(),p.extraColour.getGreen(),p.extraColour.getBlue(),p.extraColour.getAlpha());
					if(p.type.state.equals(State.LIQUID)){
						for(int i=0;i<4;i++){
							int ax=i==0||i==2?0:i==1?1:-1;
							int ay=i==0?-1:i==1||i==3?0:1;
							img.setRGB(p.x+ax,p.y+ay,glow.getRGB());
						}
						img.setRGB(p.x,p.y,p.type.colour.getRGB());
					}else if(p.type.state.equals(State.GAS)||p.type.state.equals(State.PLASMA)){
						Color pixel=new Color(glow.getRed(),glow.getGreen(),glow.getBlue(),glow.getAlpha()/3);
						g.setColor(pixel);
						g.fillOval(p.x-3/2,p.y-3/2,3,3);
					}else img.setRGB(p.x,p.y,p.type.colour.getRGB());
				}
				if(!Main.powder.fancyGraphics&&!Main.powder.tempGraphics) img.setRGB(p.x,p.y,p.type.colour.getRGB());
			}
		}
	}

	private Color getTempColour(float t){
		try{
			return new Color(MapData.heatMap[(int)(t*(MapData.heatMap.length-1)/maxTemp)]);
		}catch(ArrayIndexOutOfBoundsException e){
			return new Color(MapData.heatMap[MapData.heatMap.length-1]);
		}
	}

	public void addAir(int x,int y,float pressure){
		pv[x][y]+=pressure;
	}

	public static int getBurnRate(Element e) throws Exception{
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
			throw new Exception("You forgot to specify the burn rate of some flammable elements!");
		}
	}
}
