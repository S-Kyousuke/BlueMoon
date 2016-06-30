/*
 * Copyright 2016 Surasek Nusati <surasek@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package th.skyousuke.libgdx.bluemoon.game.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Align;

/**
 * Created by Skyousuke <surasek@gmail.com> on 28/6/2559.
 */
public class Dialog extends Actor {

    private static final float DEFAULT_TEXT_SPEED = 30.0f;
    private static final float MAX_TEXT_SPEED = 100.0f;

    private BitmapFont font;
    private TextureRegion dialogTexture;
    private float textStartX;
    private float textStartY;

    private float textSpeed = DEFAULT_TEXT_SPEED;
    private String text = "";
    private int charCount = 0;
    private float stringCompleteness = 0;

    private boolean hiding = false;
    private boolean showing = false;
    private Action showingEffect = null;
    private Action hidingEffect = null;

    private boolean goingToChangePage = false;

    private float dragStartX = 0;
    private float dragStartY = 0;

    private Action typingAction = new Action() {
        @Override
        public boolean act(float delta) {
            stringCompleteness += textSpeed * delta;
            charCount = (int) stringCompleteness;
            if (charCount >= text.length()) {
                charCount = text.length();
                return true;
            }
            goingToChangePage = false;
            return false;
        }
    };

    private Action waitingAction = new Action() {
        @Override
        public boolean act(float delta) {
            return goingToChangePage;
        }
    };

    private Action noAction = new Action() {
        @Override
        public boolean act(float delta) {
            return true;
        }
    };

    private DragListener dragListenner = new DragListener() {
        @Override
        public void dragStart(InputEvent event, float x, float y, int pointer) {
            dragStartX = x;
            dragStartY= y;
        }

        @Override
        public void drag(InputEvent event, float x, float y, int pointer)  {
            moveBy(x - dragStartX, y - dragStartY);
        };
    };

    public Dialog(BitmapFont font, TextureRegion dialogTexture, float textStartX, float textStartY) {
        this.font = font;
        this.dialogTexture = dialogTexture;

        this.textStartX = textStartX;
        this.textStartY = textStartY;

        setWidth(dialogTexture.getRegionWidth());
        setHeight(dialogTexture.getRegionHeight());
    }

    public void enableDragging() {
        addListener(dragListenner);
    }

    public void disableDragging() {
        removeListener(dragListenner);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(dialogTexture, getX(), getY(), getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());

        Color fontColor = font.getColor();
        font.setColor(fontColor.r, fontColor.g, fontColor.b, color.a*parentAlpha);
        font.draw(batch, text,
                getX() + textStartX,
                getY() + textStartY,
                0, charCount,
                getWidth() - textStartX*2, Align.left, true);
    }

    public void setShowingEffect(Action effect) {
        showingEffect = effect;
    }

    public void setHidingEffect(final Action effect) {
        hidingEffect = effect;
    }

    public void hide() {
        hide(noAction);
    }

    public void hide(final Action afterHideAction) {
        if (isVisible() && !hiding && !showing) {
            hiding = true;
            Action completedAction = new Action() {
                @Override
                public boolean act(float delta) {
                    hiding = false;
                    if (hidingEffect != null) hidingEffect.restart();
                    addAction(afterHideAction);
                    return true;
                }
            };

            SequenceAction allActions = Actions.action(SequenceAction.class);
            if (hidingEffect != null) allActions.addAction(hidingEffect);
            allActions.addAction(Actions.hide());
            allActions.addAction(completedAction);
            addAction(allActions);
        }
    }

    public void show() {
        show(noAction);
    }


    public void show(final Action afterShowAction) {
        if (!isVisible() && !hiding && !showing) {
            showing = true;
            Action completedAction = new Action() {
                @Override
                public boolean act(float delta) {
                    showing = false;
                    if (showingEffect != null) showingEffect.restart();
                    addAction(afterShowAction);
                    return true;
                }
            };

            SequenceAction allActions = Actions.action(SequenceAction.class);
            allActions.addAction(Actions.show());
            if (showingEffect != null) allActions.addAction(showingEffect);
            allActions.addAction(completedAction);
            addAction(allActions);
        }
    }

    public void addPage(final String text, float delaySec) {
        SequenceAction addPageAction = Actions.action(SequenceAction.class);
        addPageAction.addAction(new Action() {
            @Override
            public boolean act(float delta) {
                setText(text);
                return true;
            }
        });
        addPageAction.addAction(typingAction);
        addPageAction.addAction(Actions.delay(delaySec));

        addAction(Actions.after(addPageAction));
    }

    public void addLastPage(String text, float delaySec) {
        addLastPage(text, delaySec, noAction);
    }

    public void addLastPage(String text, float delaySec, Action afterHideAction) {
        addPage(text, 0);
        addAfterLastAction(afterHideAction);
    }

    public void addWaitingPage(String text) {
        addPage(text, 0);
        addAction(Actions.after(waitingAction));
    }

    public void addLastWaitingPage(String text) {
        addLastWaitingPage(text, noAction);
    }

    public void addLastWaitingPage(String text, Action afterHideAction) {
        addWaitingPage(text);
        addAfterLastAction(afterHideAction);
    }

    private void addAfterLastAction(final Action afterHideAction) {
        addAction(Actions.after(new Action() {
            @Override
            public boolean act(float delta) {
                hide(afterHideAction);
                return true;
            }
        }));
    }

    protected void setText(String text) {
        textSpeed = DEFAULT_TEXT_SPEED;
        stringCompleteness = 0;
        charCount = 0;

        this.text = text;
    }

    public void tryToChangePage() {
        textSpeed = MAX_TEXT_SPEED;
        goingToChangePage = true;
    }

}
