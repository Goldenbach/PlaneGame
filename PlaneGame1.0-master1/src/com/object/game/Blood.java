package com.object.game;

import java.awt.Graphics;
import java.awt.Image;

public class Blood extends Animation {
	private static Image[] imgs = new Image[6];
	private int count=5;
	private boolean flag = false;
    static {
        for(int i=0;i<6;i++){
            imgs[i] = GameUtil.getImage("images/blood/w"+(i+1)+".jpg");
            imgs[i].getWidth(null);
        }
    }
    public Blood() {
	}
	public void draw(Graphics g) {//制图
		g.drawImage(imgs[count], 300, 40, null);
	}
	public void addBlood(){
		if(count<5)count++;
	}
	//如果飞机没血了，flag==true
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	//血条改为boolean,没血时返回true
	public void minusBlood(){
		if(count>0) {
			count--;
		}
		if(count<=0) {
			flag = true;
		}
	}
}
