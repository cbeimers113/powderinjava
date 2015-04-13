/**
 *	@Copyright 2015 Chris Beimers/firefreak11
 *
 *	This file is part of BeanPowder.
 *
 *	BeanPowder is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.
 *
 *	BeanPowder is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.
 *
 *	You should have received a copy of the GNU General Public License
 *	along with BeanPowder.  If not, see <http://www.gnu.org/licenses/>.
 **/

package net.codarch.bpowder;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;

import net.codarch.bpowder.elements.Element;
import codarch.zedengine.core.Engine;

public class Powder extends Engine {

	/**
	 * Bean Powder, a Java recreation of The Powder Toy
	 * 
	 * @link http://powdertoy.co.uk
	 * @author Chris Beimers/firefreak11
	 * @date April 12, 2015
	 */

	// SIMULATION

	private static final long serialVersionUID = 1L;

	public static BufferedImage icon;

	static {
		try {
			icon = ImageIO.read(Powder.class.getResource("/assets/icon.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Element spawnType;

	private int mx;
	private int my;
	private int cursorRadius;

	private boolean spawning;

	public Powder() {
		super("Bean Powder", 650, 475, false, false);
		frame.setIconImage(icon);
		spawnType = Element.air;
	}

	public void update() {
		if (spawning)
			fillCursor(cursorRadius);
		for (Iterator<Particle> iterator = Particle.particles.iterator(); iterator
				.hasNext();) {
			Particle particle = iterator.next();
			particle.update();
		}
	}

	public void render() {
		for (Iterator<Particle> iterator = Particle.particles.iterator(); iterator
				.hasNext();) {
			Particle particle = iterator.next();
			int x = particle.x;
			int y = particle.y;
			Color baseColour = particle.element.colour;
			Color glow = new Color(baseColour.getRed() / 2,
					baseColour.getGreen() / 2, baseColour.getBlue() / 2);
			for (int i = 0; i < 4; i++)
				try {
					render.pixels[i == 0 ? x - 1 + y * width : i == 1 ? x + 1
							+ y * width : i == 2 ? x + (y - 1) * width : x
							+ (y + 1) * width] = glow.getRGB();
				} catch (ArrayIndexOutOfBoundsException e) {
					continue;
				}
			render.pixels[x + y * width] = baseColour.getRGB();
		}
		drawCursor(mx, my, cursorRadius);
	}

	public void spawnParticle(int x, int y, Element spawnType) {
		if (Particle.particleAt(x, y) != null||Particle.particles.size()>=1000)
			return;
		new Particle(x, y, spawnType);
	}

	public void save() {

	}

	public void keyPressed(KeyEvent e) {

	}

	public void keyReleased(KeyEvent e) {

	}

	public void keyTyped(KeyEvent e) {

	}

	public void mouseClicked(MouseEvent e) {

	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	public void mousePressed(MouseEvent e) {
		spawning = true;
	}

	public void mouseReleased(MouseEvent e) {
		spawning = false;
	}

	public void mouseDragged(MouseEvent e) {
		mx = e.getX();
		my = e.getY();
	}

	public void mouseMoved(MouseEvent e) {
		mx = e.getX();
		my = e.getY();
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		int n = e.getWheelRotation();
		if (n > 0 && cursorRadius < 50)
			cursorRadius++;
		if (n < 0 && cursorRadius > 0)
			cursorRadius--;
	}

	// INTERFACE

	public void drawPixel(int x, int y, int colour) {
		if (x < 0 || x >= width || y < 0 || y >= height)
			return;
		render.pixels[x + y * width] = colour;
	}

	public void drawCursor(int x0, int y0, int radius) {
		int x = radius;
		int y = 0;
		int radiusError = 1 - x;

		while (x >= y) {
			drawPixel(x + x0, y + y0, 0xaaaaaa);
			drawPixel(y + x0, x + y0, 0xaaaaaa);
			drawPixel(-x + x0, y + y0, 0xaaaaaa);
			drawPixel(-y + x0, x + y0, 0xaaaaaa);
			drawPixel(-x + x0, -y + y0, 0xaaaaaa);
			drawPixel(-y + x0, -x + y0, 0xaaaaaa);
			drawPixel(x + x0, -y + y0, 0xaaaaaa);
			drawPixel(y + x0, -x + y0, 0xaaaaaa);
			y++;
			if (radiusError < 0) {
				radiusError += 2 * y + 1;
			} else {
				x--;
				radiusError += 2 * (y - x) + 1;
			}
		}
	}

	public void fillCursor(int radius) {
		if (radius == 0) {
			spawnParticle(my, my, spawnType);
			return;
		}
		int x = radius;
		int y = 0;
		int radiusError = 1 - x;

		while (x >= y) {
			spawnParticle(x + mx, y + my, spawnType);
			spawnParticle(y + mx, x + my, spawnType);
			spawnParticle(-x + mx, y + my, spawnType);
			spawnParticle(-y + mx, x + my, spawnType);
			spawnParticle(-x + mx, -y + my, spawnType);
			spawnParticle(-y + mx, -x + my, spawnType);
			spawnParticle(x + mx, -y + my, spawnType);
			spawnParticle(y + mx, -x + my, spawnType);
			y++;
			if (radiusError < 0) {
				radiusError += 2 * y + 1;
			} else {
				x--;
				radiusError += 2 * (y - x) + 1;
			}
		}
		fillCursor(--radius);
	}
}
