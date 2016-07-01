package th.skyousuke.libgdx.bluemoon.game.object.character;

import th.skyousuke.libgdx.bluemoon.game.object.character.effect.AbstractCharacterEffect;

/**
 * Created by Skyousuke <surasek@gmail.com> on 30/6/2559.
 */
public interface CharacterListener {

    void onStatusChange(CharacterStatusType statusType);
    void onPrimaryAttributeChange(CharacterPrimaryAttribute primaryAttribute);
    void onDerivedAttributeChange(CharacterDerivedAttribute derivedAttribute);
    void onEffectAdd(AbstractCharacterEffect effect);
    void onEffectRemove(AbstractCharacterEffect effect);

}
