package th.skyousuke.libgdx.bluemoon.framework.collision;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Rectangle;

import th.skyousuke.libgdx.bluemoon.game.object.AbstractGameObject;

public class TiledCollisionCheck implements CollisionCheck {

    private TiledMapTileLayer tileLayer;
    private AbstractGameObject gameObject;
    private String property;
    private Rectangle bounds;
    private float tileWidth;
    private float tileHeight;

    public TiledCollisionCheck(AbstractGameObject gameObject, TiledMapTileLayer tileLayer, String property) {
        this.tileLayer = tileLayer;
        this.gameObject = gameObject;
        this.property = property;
        bounds = gameObject.getBounds();
        tileWidth = tileLayer.getTileWidth();
        tileHeight = tileLayer.getTileHeight();
    }

    private boolean isCellHasProperty(float x, float y) {
        final Cell cell = tileLayer.getCell(getCellX(x), getCellY(y));
        return cell != null && cell.getTile().getProperties().containsKey(property);
    }

    private int getCellX(float x) {
        return (int) (x / tileWidth);
    }

    private int getCellY(float y) {
        return (int) (y / tileHeight);
    }

    @Override
    public void checkAndResponseX() {
        if (isCollidesRight()) {
            final int cellX = getCellX(bounds.x + bounds.width);
            gameObject.setPositionX((cellX * tileWidth) - bounds.width - 0.001f);
        } else if (isCollidesLeft()) {
            final int cellX = getCellX(bounds.x);
            gameObject.setPositionX((cellX + 1) * tileWidth);
        }
    }

    @Override
    public void checkAndResponseY() {
        if (isCollidesTop()) {
            final int cellY = getCellY(bounds.y + bounds.height);
            gameObject.setPositionY((cellY * tileHeight) - bounds.height - 0.001f);
        } else if (isCollidesBottom()) {
            final int cellY = getCellY(bounds.y);
            gameObject.setPositionY((cellY + 1) * tileHeight);
        }
    }

    private boolean isCollidesRight() {
        for (float step = 0; step < bounds.height; step += tileHeight)
            if (isCellHasProperty(bounds.x + bounds.width, bounds.y + step))
                return true;
        return isCellHasProperty(bounds.x + bounds.width, bounds.y + bounds.height);
    }

    private boolean isCollidesLeft() {
        for (float step = 0; step < bounds.height; step += tileHeight)
            if (isCellHasProperty(bounds.x, bounds.y + step))
                return true;
        return isCellHasProperty(bounds.x, bounds.y + bounds.height);
    }

    public boolean isCollidesTop() {
        for (float step = 0; step < bounds.width; step += tileWidth)
            if (isCellHasProperty(bounds.x + step, bounds.y + bounds.height))
                return true;
        return isCellHasProperty(bounds.x + bounds.width, bounds.y + bounds.height);
    }

    public boolean isCollidesBottom() {
        for (float step = 0; step < bounds.width; step += tileWidth)
            if (isCellHasProperty(bounds.x + step, bounds.y))
                return true;
        return isCellHasProperty(bounds.x + bounds.width, bounds.y);
    }

}
