package th.skyousuke.libgdx.bluemoon.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

import th.skyousuke.libgdx.bluemoon.BlueMoon;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
            return new GwtApplicationConfiguration(BlueMoon.SCENE_WIDTH, BlueMoon.SCENE_HEIGHT);
        }

        @Override
        public ApplicationListener createApplicationListener () {
            return new BlueMoon();
        }
}