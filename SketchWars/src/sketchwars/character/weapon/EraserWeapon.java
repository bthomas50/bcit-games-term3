/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars.character.weapon;

import java.awt.Color;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import sketchwars.character.SketchCharacter;
import sketchwars.character.projectiles.AbstractProjectile;
import sketchwars.character.projectiles.ProjectileFactory;
import sketchwars.graphics.Texture;
import sketchwars.input.MouseHandler;
import sketchwars.input.MouseState;
import sketchwars.map.AbstractMap;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class EraserWeapon extends AbstractWeapon {
    private final Texture eraser;
    private BufferedImage erasingImage;
    private AbstractMap currentMap;
    
    private float eWidth;
    private float eHeight;
    
    public EraserWeapon(Texture texture, Texture eraser, BufferedImage erasingImage, float width, float height, ProjectileFactory projectileFactory) {
        super(texture, width, height, projectileFactory);
        
        this.eraser = eraser;
        this.erasingImage = erasingImage;
        
        eWidth = width/2;
        eHeight = height/2;
    }

    @Override
    public void render() {
        posX = MouseHandler.getNormalizedX();
        posY = MouseHandler.getNormalizedY();
        
        if (eraser != null && currentMap != null && erasingImage != null &&
                MouseHandler.state == MouseState.DOWN) {
            Texture mapForeground = currentMap.getForeground();
            BufferedImage mapForegroundImage = currentMap.getForegroundImage();
            float xEraser = posX - 0.04f;
            float yEraser = posY - 0.08f;
            
            int widthFG = mapForegroundImage.getWidth();
            int heightFG = mapForegroundImage.getHeight();
            int xFG = (int)((widthFG/2.0f)*(xEraser + 1));
            int yFG = (int)((heightFG/2.0f)*(2 - (yEraser + 1)));
            
            System.out.println(xFG + ",, " + yFG);
            
            BufferedImage replacedRegion = eraseArea(mapForegroundImage, erasingImage, xFG, yFG);
            
            eraser.draw(null, xEraser, yEraser, eWidth, eHeight);
            
            if (replacedRegion != null) {
                mapForeground.setSubTexture(replacedRegion, xFG, yFG, erasingImage.getWidth(), erasingImage.getHeight());
            }
        }
        
        super.render();
    }

    public void setMap(AbstractMap map) {
        this.currentMap = map;
        
        try {
            BufferedImage mapForegroundImage = map.getForegroundImage();

            int newWidth = (int)((mapForegroundImage.getWidth()/2.0f) * eWidth);
            int newHeight = (int)((mapForegroundImage.getHeight()/2.0f) * eHeight);

            this.erasingImage = Texture.resizeImage(erasingImage, newWidth, newHeight);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected AbstractProjectile createProjectile(SketchCharacter owner, long vPosition, long vVelocity) {
        return null;
    }

    @Override
    protected double getProjectileSpeed(float power) {
        return 0;
    }

    private BufferedImage eraseArea(BufferedImage image, BufferedImage subImage, int xImage, int yImage) {
        int width = image.getWidth();
        int height = image.getHeight();
                
        int subWidth = subImage.getWidth();
        int subHeight = subImage.getHeight();
        
        if ((xImage + erasingImage.getWidth()) < width && (erasingImage.getHeight() + yImage) < height) {
            for (int i = 0; i < subWidth; i++) {
                for (int j = 0; j < subHeight; j++) {
                    int xSet = xImage + i;
                    int ySet = yImage + j;
                    if (xSet < width && ySet < height) {
                        int alpha = subImage.getRGB(i, j) >> 24;

                        if (alpha != 0) { 
                            image.setRGB(xSet, ySet, Color.TRANSLUCENT);
                        }
                    }
                }
            }
            return image.getSubimage(xImage, yImage, erasingImage.getWidth(), erasingImage.getHeight());
        }
        return null;
    }
    
}
