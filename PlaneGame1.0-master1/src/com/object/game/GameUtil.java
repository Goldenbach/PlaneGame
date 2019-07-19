package com.object.game;

import java.awt.Image;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

/**
 * 
 *用于获取图片的工具类
 *
 */
public final class GameUtil {
    // 工具类最好将构造器私有化。
    private GameUtil() {
     
    } 
    //依据文件地址获取图片
    public static Image getImage(String path) {
        BufferedImage bi = null;
        try {
            URL u = GameUtil.class.getClassLoader().getResource(path);
            bi = ImageIO.read(u);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bi;
    }
}
