package th.skyousuke.libgdx.bluemoon.utils.collisions;

public class NullCollsionCheck implements CollisionCheck {

    @Override
    public boolean isCollidesRight() {
        return false;
    }

    @Override
    public boolean isCollidesLeft() {
        return false;
    }

    @Override
    public boolean isCollidesTop() {
        return false;
    }

    @Override
    public boolean isCollidesBottom() {
        return false;
    }

}
