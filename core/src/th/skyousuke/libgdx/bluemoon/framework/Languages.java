package th.skyousuke.libgdx.bluemoon.framework;

/**
 * Languages list.
 * Created by S. Kyousuke <surasek@gmail.com> on 28/7/2559.
 */
public class Languages {

    public static final int ENGLISH = 0;
    public static final int THAI = 1;

    public static String toString(int languageCode) {
        switch (languageCode) {
            case ENGLISH:
                return LanguageManager.instance.getText("english");
            case THAI:
                return LanguageManager.instance.getText("thai");
            default:
                return null;
        }
    }

}
