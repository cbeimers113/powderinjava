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
import java.io.IOException;
import javax.imageio.ImageIO;

public class StaticData{
	
	public static String version="1.0.0";

	public static Particle[][] pmap=new Particle[Main.powder.width][Main.powder.height];
	public static float[][] pv=new float[Main.powder.width][Main.powder.height];
	public static float[][] tv=new float[Main.powder.width][Main.powder.height];
	public static float[][] vx=new float[Main.powder.width][Main.powder.height];
	public static float[][] vy=new float[Main.powder.width][Main.powder.height];

	public static int[] heatMapPos=new int[]{
			0x0000ff,
			0x0020ff,
			0x0040ff,
			0x0060ff,
			0x0080ff,
			0x00c0ff,
			0x00ffff,
			0x00ffc0,
			0x00ff80,
			0x00ff60,
			0x00ff40,
			0x00ff20,
			0x00ff00,
			0x20ff00,
			0x40ff00,
			0x60ff00,
			0x80ff00,
			0xc0ff00,
			0xffff00,
			0xffc000,
			0xff8000,
			0xff6000,
			0xff4000,
			0xff2000,
			0xff0000,
			0xff0020,
			0xff0040,
			0xff0060,
			0xff0080,
			0xff00c0,
			0xff00ff,
			0xc000ff,
			0x8000ff
	};

	public static int[] heatMapNeg=new int[]{
			0x0000ff,
			0x0000e0,
			0x0000c0,
			0x0000a0,
			0x000080,
			0x000070,
			0x000060,
			0x000050,
			0x000040,
			0x000030,
			0x000020,
			0x000010,
			0x000000
	};

	public static int[] pressureMapPos=new int[]{
			0x000000,
			0x2020ff,
			0x4040ff,
			0x6060ff,
			0x8080ff,
			0xc0c0ff,
			0xffffff,
			0xffc0c0,
			0xff8080,
			0xff6060,
			0xff4040,
			0xff2020,
			0xff0000
	};

	public static int[] pressureMapNeg=new int[]{
			0x000000,
			0x000010,
			0x000020,
			0x000030,
			0x000040,
			0x000050,
			0x000060,
			0x000070,
			0x000080,
			0x0000a0,
			0x0000c0,
			0x0000e0,
			0x0000ff,
	};

	public static BufferedImage pause;
	public static BufferedImage play;
	public static BufferedImage page;
	public static BufferedImage pauseH;
	public static BufferedImage playH;
	public static BufferedImage pageH;

	static{
		try{
			pause=ImageIO.read(StaticData.class.getResource("/assets/pause.png"));
			play=ImageIO.read(StaticData.class.getResource("/assets/play.png"));
			page=ImageIO.read(StaticData.class.getResource("/assets/new.png"));
			pauseH=ImageIO.read(StaticData.class.getResource("/assets/pauseHover.png"));
			playH=ImageIO.read(StaticData.class.getResource("/assets/playHover.png"));
			pageH=ImageIO.read(StaticData.class.getResource("/assets/newHover.png"));
		}catch(IOException e){
			e.printStackTrace();
			System.exit(1);
		}
	}
}