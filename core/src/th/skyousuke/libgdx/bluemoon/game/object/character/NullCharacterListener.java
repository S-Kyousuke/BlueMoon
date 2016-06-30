package th.skyousuke.libgdx.bluemoon.game.object.character;

import th.skyousuke.libgdx.bluemoon.game.object.character.effect.AbstractEffect;

/**
 * Created by Skyousuke <surasek@gmail.com> on 30/6/2559.
 */
public class NullCharacterListener implements CharacterListener {

    @Override
    public void onStatusChange(CharacterStatusType statusType) {

    }

    @Override
    public void onPrimaryAttributeChange(CharacterPrimaryAttribute primaryAttribute) {

    }

    @Override
    public void onDerivedAttributeChange(CharacterDerivedAttribute derivedAttribute) {

    }

    @Override
    public void onEffectAdd(AbstractEffect effect) {

    }

    @Override
    public void onEffectRemove(AbstractEffect effect) {

    }
}
