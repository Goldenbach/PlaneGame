package com.object.game;

import java.awt.Graphics;
import java.awt.Image;

public class SupplyPacket3 extends GameObject implements Runnable{//雷电
	private boolean live;
	public SupplyPacket3(Image img) {
		setImg(img);
		setPosition((Math.random()*6*64+34), -35);
		setHeight(img.getHeight(null));
		setWidth(img.getWidth(null));
	}
	public void drawSelf(Graphics g) {
		if(live){
			g.drawImage(getImg(), (int)getX(), (int)getY(), null);
			move();
		}
		checkLocation();
	}

	
	public void checkLocation() {
		if(!live||getY()>Constant.GAME_HEIGHT){
			setPosition((Math.random()*6*64+34), -35);
			live=false;
		}
	}

	public void move() {
		moveY(getY()+2);
	}
	public void run() {
		while(true){
			try{
				Thread.sleep(53580);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
			live=true;
		}
	}
	public boolean isLive(){
		return live;
	}
	public void setLive(boolean live){
		this.live=live;
	}

}
