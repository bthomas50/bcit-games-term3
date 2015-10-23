/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars.character.weapon;

import java.awt.Color;
import java.awt.image.BufferedImage;
import sketchwars.character.SketchCharacter;
import sketchwars.character.projectiles.AbstractProjectile;
import sketchwars.character.projectiles.ProjectileFactory;
import sketchwars.graphics.Texture;
import sketchwars.input.MouseHandler;
import sketchwars.input.MouseState;
import sketchwars.map.AbstractMap;
import sketchwars.physics.BitMask;
import sketchwars.physics.BitMaskFactory;
import sketchwars.physics.BoundingBox;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class PencilWeapon extends AbstractWeapon {
    private final Texture point;
    private BufferedImage pointImage;
    private AbstractMap currentMap;
    
    private final float eWidth;
    private final float eHeight;
    
    private final boolean eraser;
    
    public PencilWeapon(Texture texture, Texture point, BufferedImage pointImage, float width, float height, 
            ProjectileFactory projectileFactory, boolean eraser) {
        super(texture, width, height, projectileFactory);
        
        this.eraser = eraser;
        this.point = point;
        this.pointImage = pointImage;
        
        eWidth = width/2;
        eHeight = height/2;
    }

    @Override
    public void update(double elapsed) {
        posX = MouseHandler.getNormalizedX();
        posY = MouseHandler.getNormalizedY();
        
        handleErasing();
        
        super.update(elapsed); 
    }
    
    @Override
    public void render() {
        if (texture != null) {
            texture.draw(null, posX, posY, width, height);
        }
    }

    public void setMap(AbstractMap map) {
        this.currentMap = map;
        
        try {
            BufferedImage mapForegroundImage = map.getForegroundImage();

            int newWidth = (int)((mapForegroundImage.getWidth()/2.0f) * eWidth);
            int newHeight = (int)((mapForegroundImage.getHeight()/2.0f) * eHeight);

            this.pointImage = Texture.resizeImage(pointImage, newWidth, newHeight);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
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

    private void handleErasing() {
        if (point != null && currentMap != null && pointImage != null &&
                MouseHandler.state == MouseState.DOWN) {
            Texture mapForeground = currentMap.getForeground();
            BufferedImage mapForegroundImage = currentMap.getForegroundImage();
            float xEraser = posX - 0.04f;
            float yEraser = posY - 0.08f;
            
            int widthFG = mapForegroundImage.getWidth();
            int heightFG = mapForegroundImage.getHeight();
            int xFG = (int)((widthFG/2.0)*(xEraser + 1.0))  - 6;
            int yFG = (int)((heightFG/2.0)*(2.0 - (yEraser + 1.0))) - 2;
                    
            BufferedImage replacedRegion = updateArea(mapForegroundImage, pointImage, xFG, yFG);
            point.draw(null, xEraser, yEraser, eWidth, eHeight);
            
            if (replacedRegion != null) {
                mapForeground.setSubTexture(replacedRegion, xFG, yFG, replacedRegion.getWidth(), replacedRegion.getHeight());
                updateInPhysics(xEraser, yEraser, eWidth, eHeight);
            }
        }
    }
    
    private BufferedImage updateArea(BufferedImage image, BufferedImage subImage, int xImage, int yImage) {
        int sWidth = image.getWidth();
        int sHeight = image.getHeight();
                
        int subWidth = subImage.getWidth();
        int subHeight = subImage.getHeight();
        
        if ((xImage + subWidth) < sWidth && (yImage + subHeight) < sHeight) {
            for (int i = 0; i < subWidth; i++) {
                for (int j = 0; j < subHeight; j++) {
                    int xSet = xImage + i;
                    int ySet = yImage + j;
                    if (xSet < sWidth && ySet < sHeight) {
                        int argb = subImage.getRGB(i, j);
                        int alpha = argb >> 24;

                        if (alpha != 0) { 
                            if (eraser) {
                                image.setRGB(xSet, ySet, Color.TRANSLUCENT);
                            } else {
                                image.setRGB(xSet, ySet, argb);
                            }
                        }
                    }
                }
            }
            return image.getSubimage(xImage, yImage, subImage.getWidth(), subImage.getHeight());
        }
        return null;
    }

    private void updateInPhysics(float xEraser, float yEraser, float eWidth, float eHeight) {
        BitMask mapBitmask = currentMap.getMapCollider().getPixels();
        
        int xPhysics = (int) ((xEraser - 0.018) * 1024.0);
        int yPhysics = (int) ((yEraser - 0.025) * 1024.0);
        
        int widthPhysics = (int)(eWidth * 1024.0);
        int heightPhysics = (int)(eHeight * 1024.0);
        
        BoundingBox bb = new BoundingBox(yPhysics, xPhysics, yPhysics + heightPhysics, xPhysics + widthPhysics);
        
        if (eraser) {
            BitMaskFactory.updateFromImageAlpha(pointImage, mapBitmask, bb, false);
        } else {
            BitMaskFactory.updateFromImageAlpha(pointImage, mapBitmask, bb, true);
        }
    }
}
