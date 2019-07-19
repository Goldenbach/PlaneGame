package com.object.game;

import java.awt.Graphics;
/**
 * 
 * 此类是制作爆炸的基类
 *
 */
public abstract class Animation {//活泼，生气；激励；卡通片绘制
	private double x,y;//位置
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public abstract void draw(Graphics g);//抽象方法：画 图形
	public void setLocation(double x,double y){//设置坐标
    	this.x = x;
        this.y = y;
    }
	

}
