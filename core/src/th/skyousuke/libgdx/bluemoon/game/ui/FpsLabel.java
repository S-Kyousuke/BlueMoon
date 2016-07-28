package th.skyousuke.libgdx.bluemoon.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * Frame per second label class;
 * Created by S. Kyousuke <surasek@gmail.com> on 27/7/2559.
 */
public class FpsLabel extends Label {

    long startTime;

    public FpsLabel(Label label) {
        super("", label.getStyle());
        pack();
    }

    @Override
    public void act(float delta) {
        if (TimeUtils.nanoTime() - startTime > 1000000000) {
            setText("FPS: " + Gdx.graphics.getFramesPerSecond());
            startTime = TimeUtils.nanoTime();
        }
        super.act(delta);
    }

}
