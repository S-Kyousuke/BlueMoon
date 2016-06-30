package th.skyousuke.libgdx.bluemoon.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;

import th.skyousuke.libgdx.bluemoon.BlueMoon;
import th.skyousuke.libgdx.bluemoon.game.object.character.CharacterDerivedAttribute;
import th.skyousuke.libgdx.bluemoon.game.object.character.CharacterListener;
import th.skyousuke.libgdx.bluemoon.game.object.character.CharacterPrimaryAttribute;
import th.skyousuke.libgdx.bluemoon.game.object.character.CharacterStatusType;
import th.skyousuke.libgdx.bluemoon.game.object.character.effect.AbstractEffect;

/**
 * Created by Skyousuke <surasek@gmail.com> on 30/6/2559.
 */
public class WorldGui extends InputAdapter implements Disposable, CharacterListener, WorldListener {

    private WorldController worldController;
    private Stage stage;

    private Window statusWindow;
    private Label healthLabel;
    private Label manaLabel;
    private Label staminaLabel;
    private Label fullnessLabel;
    private Label effectLabel;
    private List<String> effectList;
    private Array<String> effectArray;
    private ScrollPane effectPane;
    private TextButton addHealthButton;
    private TextButton subtractHealthButton;
    private TextButton addManaButton;
    private TextButton subtractManaButton;
    private TextButton addStaminaButton;
    private TextButton subtractStaminaButton;
    private TextButton addFullnessButton;
    private TextButton subtractFullnessButton;
    private TextButton toggleAttributeDisplayButton;

    private Window attributeWindow;
    private Label strenghtLabel;
    private Label agilityLabel;
    private Label intelligenceLabel;
    private Label charismaLabel;
    private Label luckLabel;
    private Label survivalLabel;
    private Label movingSpeedLabel;
    private Label maxStaminaLabel;
    private Label maxHealthLabel;
    private Label maxManaLabel;
    private Label maxFullnessLabel;
    private Label healthRegenerationLabel;
    private Label manaRegenerationLabel;
    private Label physicalDamageLabel;
    private Label magicalDamageLabel;
    private Label physicalDefenseLabel;
    private Label attackSpeedLabel;
    private Label craftingLabel;
    private Label fishingLabel;
    private Label fullnessDrainLabel;
    private Label toolsEfficiencyLabel;
    private Label toolsSpeedLabel;
    private Label toolsLevelLabel;
    private Label itemChanceLabel;
    private Label upgradeChanceLabel;
    private Label eventChanceLabel;
    private Label friendshipLabel;
    private Label shoppingLabel;

    public WorldGui(WorldController worldController) {
        this.worldController = worldController;
        stage = new Stage(new FitViewport(BlueMoon.SCENE_WIDTH, BlueMoon.SCENE_HEIGHT));

        healthLabel = new Label("", Assets.instance.skin);
        manaLabel = new Label("", Assets.instance.skin);
        staminaLabel = new Label("", Assets.instance.skin);
        fullnessLabel = new Label("", Assets.instance.skin);

        effectList = new List<>(Assets.instance.skin);
        effectArray = new Array<>();
        effectPane = new ScrollPane(effectList, Assets.instance.skin);
        effectLabel = new Label("Effects:", Assets.instance.skin);

        addHealthButton = new TextButton("+", Assets.instance.skin);
        addHealthButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                worldController.controlledPlayer.changeStatus(CharacterStatusType.HEALTH, 5);
            }
        });

        addManaButton = new TextButton("+", Assets.instance.skin);
        addManaButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                worldController.controlledPlayer.changeStatus(CharacterStatusType.MANA, 5);
            }
        });

        addStaminaButton = new TextButton("+", Assets.instance.skin);
        addStaminaButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                worldController.controlledPlayer.changeStatus(CharacterStatusType.STAMINA, 5);
            }
        });

        addFullnessButton = new TextButton("+", Assets.instance.skin);
        addFullnessButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                worldController.controlledPlayer.changeStatus(CharacterStatusType.FULLNESS, 5);
            }
        });

        subtractHealthButton = new TextButton("-", Assets.instance.skin);
        subtractHealthButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                worldController.controlledPlayer.changeStatus(CharacterStatusType.HEALTH, -5);
            }
        });

        subtractManaButton = new TextButton("-", Assets.instance.skin);
        subtractManaButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                worldController.controlledPlayer.changeStatus(CharacterStatusType.MANA, -5);
            }
        });

        subtractStaminaButton = new TextButton("-", Assets.instance.skin);
        subtractStaminaButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                worldController.controlledPlayer.changeStatus(CharacterStatusType.STAMINA, -5);
            }
        });

        subtractFullnessButton = new TextButton("-", Assets.instance.skin);
        subtractFullnessButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                worldController.controlledPlayer.changeStatus(CharacterStatusType.FULLNESS, -5);
            }
        });

        toggleAttributeDisplayButton = new TextButton("Show Attribute", Assets.instance.skin);
        toggleAttributeDisplayButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!attributeWindow.isVisible()) {
                    attributeWindow.setVisible(true);
                    attributeWindow.setPosition(statusWindow.getX() + 260, statusWindow.getY());
                    toggleAttributeDisplayButton.setText("Hide Attribute");
                }
                else {
                    attributeWindow.setVisible(false);
                    toggleAttributeDisplayButton.setText("Show Attribute");
                }
            }
        });

        statusWindow = new Window("", Assets.instance.skin);
        statusWindow.padLeft(10f);
        statusWindow.padRight(15f);
        statusWindow.padBottom(10f);
        statusWindow.align(Align.left);
        statusWindow.row().padTop(10f);
        statusWindow.add(healthLabel).align(Align.left).fillX().expandX();
        statusWindow.add(addHealthButton).width(20f).height(20f).padRight(5f);
        statusWindow.add(subtractHealthButton).width(20f).height(20f);
        statusWindow.row();
        statusWindow.add(manaLabel).align(Align.left).fillX().expandX();
        statusWindow.add(addManaButton).width(20f).height(20f).padRight(5f);
        statusWindow.add(subtractManaButton).width(20f).height(20f);
        statusWindow.row();
        statusWindow.add(staminaLabel).align(Align.left).fillX().expandX();
        statusWindow.add(addStaminaButton).width(20f).height(20f).padRight(5f);
        statusWindow.add(subtractStaminaButton).width(20f).height(20f);
        statusWindow.row();
        statusWindow.add(fullnessLabel).align(Align.left).fillX().expandX();
        statusWindow.add(addFullnessButton).width(20f).height(20f).padRight(5f);
        statusWindow.add(subtractFullnessButton).width(20f).height(20f);
        statusWindow.row().colspan(3);
        statusWindow.add(effectLabel).align(Align.left).padTop(5f);
        statusWindow.row().colspan(3);
        statusWindow.add(effectPane).fill().expand();
        statusWindow.row().colspan(3);
        statusWindow.add(toggleAttributeDisplayButton).width(130f).height(25f).padTop(10f);
        statusWindow.pack();
        statusWindow.setWidth(240f);
        statusWindow.setHeight(280f);
        statusWindow.setPosition(100, 256f);

        strenghtLabel = new Label("", Assets.instance.skin);
        agilityLabel = new Label("", Assets.instance.skin);
        intelligenceLabel = new Label("", Assets.instance.skin);
        charismaLabel = new Label("", Assets.instance.skin);
        luckLabel = new Label("", Assets.instance.skin);
        survivalLabel = new Label("", Assets.instance.skin);
        movingSpeedLabel = new Label("", Assets.instance.skin);
        maxStaminaLabel = new Label("", Assets.instance.skin);
        maxHealthLabel = new Label("", Assets.instance.skin);
        maxManaLabel = new Label("", Assets.instance.skin);
        maxFullnessLabel = new Label("", Assets.instance.skin);
        healthRegenerationLabel = new Label("", Assets.instance.skin);
        manaRegenerationLabel = new Label("", Assets.instance.skin);
        physicalDamageLabel = new Label("", Assets.instance.skin);
        magicalDamageLabel = new Label("", Assets.instance.skin);
        physicalDefenseLabel = new Label("", Assets.instance.skin);
        attackSpeedLabel = new Label("", Assets.instance.skin);
        craftingLabel = new Label("", Assets.instance.skin);
        fishingLabel = new Label("", Assets.instance.skin);
        fullnessDrainLabel = new Label("", Assets.instance.skin);
        toolsEfficiencyLabel = new Label("", Assets.instance.skin);
        toolsSpeedLabel = new Label("", Assets.instance.skin);
        toolsLevelLabel = new Label("", Assets.instance.skin);
        itemChanceLabel = new Label("", Assets.instance.skin);
        upgradeChanceLabel = new Label("", Assets.instance.skin);
        eventChanceLabel = new Label("", Assets.instance.skin);
        friendshipLabel = new Label("", Assets.instance.skin);
        shoppingLabel = new Label("", Assets.instance.skin);
        
        attributeWindow = new Window("", Assets.instance.skin);
        attributeWindow.setVisible(false);
        attributeWindow.pack();
        attributeWindow.setWidth(240f);
        attributeWindow.setHeight(280f);

        onPlayerChange();

        //stage.setDebugAll(true);
        stage.addActor(statusWindow);
        stage.addActor(attributeWindow);
        Gdx.input.setInputProcessor(stage);
    }


    private void updateHealthLabel() {
        healthLabel.setText(String.format("Health: %.1f/%.1f",
                worldController.controlledPlayer.getStatus(CharacterStatusType.HEALTH),
                worldController.controlledPlayer.getAttribute()
                        .getDerived(CharacterDerivedAttribute.MAX_HEALTH)));
    }

    private void updateManaLabel() {
        manaLabel.setText(String.format("Mana: %.1f/%.1f",
                worldController.controlledPlayer.getStatus(CharacterStatusType.MANA),
                worldController.controlledPlayer.getAttribute()
                        .getDerived(CharacterDerivedAttribute.MAX_MANA)));
    }

    private void updateStaminaLabel() {
        staminaLabel.setText(String.format("Stamina: %.1f/%.1f",
                worldController.controlledPlayer.getStatus(CharacterStatusType.STAMINA),
                worldController.controlledPlayer.getAttribute()
                        .getDerived(CharacterDerivedAttribute.MAX_STAMINA)));
    }

    private void updateFullnessLabel() {
        fullnessLabel.setText(String.format("Fullness: %.1f/%.1f",
                worldController.controlledPlayer.getStatus(CharacterStatusType.FULLNESS),
                worldController.controlledPlayer.getAttribute()
                        .getDerived(CharacterDerivedAttribute.MAX_FULLNESS)));
    }

    private void updateStatusWindowTitle() {
        statusWindow.getTitleLabel().setText(String.format("%s Status",
                worldController.controlledPlayer.getName()));
    }

    private void updateAttributeWindowTitle() {
        attributeWindow.getTitleLabel().setText(String.format("%s Attribute",
                worldController.controlledPlayer.getName()));
    }

    @Override
    public void onStatusChange(CharacterStatusType statusType) {
        switch (statusType) {
            case HEALTH:
                updateHealthLabel();
                break;
            case MANA:
                updateManaLabel();
                break;
            case STAMINA:
                updateStaminaLabel();
                break;
            case FULLNESS:
                updateFullnessLabel();
                break;
        }
    }

    public void update(float deltaTime) {
        stage.act(deltaTime);
    }

    public void render() {
        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    @Override
    public void onPrimaryAttributeChange(CharacterPrimaryAttribute primaryAttribute) {

    }

    @Override
    public void onDerivedAttributeChange(CharacterDerivedAttribute derivedAttribute) {

    }

    @Override
    public void onEffectAdd(AbstractEffect effect) {
        effectArray.add(effect.getName());
        effectList.setItems(effectArray);
    }

    @Override
    public void onEffectRemove(AbstractEffect effect) {
        effectArray.removeValue(effect.getName(), false);
        effectList.setItems(effectArray);
    }

    @Override
    public void onPlayerChange() {
        worldController.controlledPlayer.setListener(this);
        updateHealthLabel();
        updateManaLabel();
        updateStaminaLabel();
        updateFullnessLabel();
        updateStatusWindowTitle();
        updateAttributeWindowTitle();
    }
}
