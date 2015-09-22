/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars.map;

import sketchwars.graphics.Texture;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class TestMap extends AbstractMap {
    public  TestMap() {
       
    }
    
    @Override
    public void init() {
        texture = new Texture();
        texture.loadTexture("content/map/test.png");
    }
}
