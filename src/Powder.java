/**
 *	@Copyright 2015 Chris Beimers/firefreak11
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
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Iterator;

import powderinjava.elements.Element;

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

	private Element spawnType;

	private int mx;
	private int my;
	private int cursorRadius;

	private boolean glowing;
	private boolean spawning;
	private boolean erasing;

	public Powder() {
		super("Powder In Java", 650, 475, false, false);
		spawnType = Element.AIR;
	}

	public void update() {

	}

	public void render() {
		if (spawning && !erasing)
			fillCursor(cursorRadius);
		if (!spawning && erasing)
			eraseCursor(cursorRadius);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				img.setRGB(x, y, 0xff000000);
			}
		}
		for (Iterator<Particle> iterator = Particle.particles.iterator(); iterator
				.hasNext();) {
			Particle particle = iterator.next();
			if (particle.removeQueue) {
				iterator.remove();
				return;
			} else {
				particle.update();
				int x = particle.x;
				int y = particle.y;
				Color baseColour = particle.element.colour;
				if (glowing) {
					Color glow = new Color(baseColour.getRed() / 2,
							baseColour.getGreen() / 2,
							baseColour.getBlue() / 2, baseColour.getAlpha() / 2);
					for (int i = 0; i < 4; i++)
						try {
							int ax = i == 0 ? x - 1 : i == 1 ? x + 1 : x;
							int ay = i == 2 ? y - 1 : i == 3 ? y + 1 : y;
							img.setRGB(ax, ay * width, glow.getRGB());
						} catch (ArrayIndexOutOfBoundsException e) {
							continue;
						}
				}
				try {
					img.setRGB(x, y * width, baseColour.getRGB());
				} catch (ArrayIndexOutOfBoundsException e) {
					continue;
				}
			}
		}
		drawCursor(mx, my, cursorRadius);
	}

	public void spawnParticle(int x, int y, Element spawnType) {
		if (Particle.particleAt(x, y) != null
				|| Particle.particles.size() >= 1000)
			return;
		new Particle(x, y, spawnType);
	}

	public void save() {

	}

	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_1:
			glowing = true;
			break;
		case KeyEvent.VK_2:
			glowing = false;
			break;
		}
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
		if (e.getButton() == MouseEvent.BUTTON1)
			spawning = true;
		else if (e.getButton() == MouseEvent.BUTTON3)
			erasing = true;
	}

	public void mouseReleased(MouseEvent e) {
		spawning = false;
		erasing = false;
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
		if (n < 0 && cursorRadius < 50)
			cursorRadius++;
		if (n > 0 && cursorRadius > 0)
			cursorRadius--;
	}

	public void fillCursor(int radius) {
		if (radius == 0) {
			spawnParticle(mx, my, spawnType);
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

	public void eraseCursor(int radius) {
		if (radius == 0) {
			spawnParticle(mx, my, spawnType);
			return;
		}
		int x = radius;
		int y = 0;
		int radiusError = 1 - x;

		while (x >= y) {
			Particle[] toRemove = new Particle[8];
			toRemove[0] = Particle.particleAt(x + mx, y + my);
			toRemove[1] = Particle.particleAt(y + mx, x + my);
			toRemove[2] = Particle.particleAt(-x + mx, y + my);
			toRemove[3] = Particle.particleAt(-y + mx, x + my);
			toRemove[4] = Particle.particleAt(-x + mx, -y + my);
			toRemove[5] = Particle.particleAt(-y + mx, -x + my);
			toRemove[6] = Particle.particleAt(x + mx, -y + my);
			toRemove[7] = Particle.particleAt(y + mx, -x + my);
			for (Particle p : toRemove)
				if (p == null)
					continue;
				else
					p.remove();
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

	public void drawCursor(int x0, int y0, int radius) {
		int x = radius;
		int y = 0;
		int radiusError = 1 - x;

		while (x >= y) {
			drawPixel(x + x0, y + y0, 0xffaaaaaa);
			drawPixel(y + x0, x + y0, 0xffaaaaaa);
			drawPixel(-x + x0, y + y0, 0xffaaaaaa);
			drawPixel(-y + x0, x + y0, 0xffaaaaaa);
			drawPixel(-x + x0, -y + y0, 0xffaaaaaa);
			drawPixel(-y + x0, -x + y0, 0xffaaaaaa);
			drawPixel(x + x0, -y + y0, 0xffaaaaaa);
			drawPixel(y + x0, -x + y0, 0xffaaaaaa);
			y++;
			if (radiusError < 0) {
				radiusError += 2 * y + 1;
			} else {
				x--;
				radiusError += 2 * (y - x) + 1;
			}
		}
	}

	public void drawPixel(int x, int y, int colour) {
		try {
			img.setRGB(x, y, colour);
		} catch (ArrayIndexOutOfBoundsException e) {
			return;
		}
	}
}
