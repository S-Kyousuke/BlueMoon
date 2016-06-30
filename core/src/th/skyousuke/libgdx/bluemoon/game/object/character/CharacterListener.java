package th.skyousuke.libgdx.bluemoon.game.object.character;

import th.skyousuke.libgdx.bluemoon.game.object.character.effect.AbstractEffect;

/**
 * Created by Skyousuke <surasek@gmail.com> on 30/6/2559.
 */
public interface CharacterListener {

    void onStatusChange(CharacterStatusType statusType);
    void onPrimaryAttributeChange(CharacterPrimaryAttribute primaryAttribute);
    void onDerivedAttributeChange(CharacterDerivedAttribute derivedAttribute);
    void onEffectAdd(AbstractEffect effect);
    void onEffectRemove(AbstractEffect effect);

}
