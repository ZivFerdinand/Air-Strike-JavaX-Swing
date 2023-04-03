package Objects;

import java.awt.*;

import GameStates.Playing;
import Interfaces.IGameStandard;
import Interfaces.ILaser;
import static Utils.Constants.ObjectSizeData.*;
import static Utils.Constants.Path.*;

public class LaserEnemy extends Object implements ILaser,IGameStandard {
    private final int laserSpeedX, laserSpeedY;
    private final int initPosX, initPosY;
    private int totalMvmt = 0;

    public LaserEnemy(int posX, int posY, int laserSpeedX, int laserSpeedY) {
        super(posX, posY,85, 85, 31, 32, LASER_ENEMY, ENEMY_LASER, 4);
        this.initPosX = posX;
        this.initPosY = posY;
        this.laserSpeedX = laserSpeedX;
        this.laserSpeedY = laserSpeedY;
    }

    public boolean checkHasMoved() {
        if (posX != initPosX || posY != initPosY)
            return true;

        return false;
    }

    public void resetPos(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public void laserDisplayNone()
    {
        this.posX = this.posY = -1000;
    }

    public void update() {
        if(!Playing.isPaused() && !Playing.isGameOver())
            updatePosition();
    }

    public void render(Graphics g) {
        g.drawImage(img, (int) posX + 85, (int) posY+85, imageWidth, imageHeight, null);
    }

    private void updatePosition() {

        posY += laserSpeedY;
        posX += laserSpeedX;
        totalMvmt += speed;
    }

    public int getPosY() {
        return (int)posY;
    }

    public int getPosX() {
        return (int)posX;
    }

    public int getTotalMvmt() {
        return totalMvmt;
    }

    public void setTotalMvmt(int totalMvmt) {
        this.totalMvmt = totalMvmt;
    }
}
