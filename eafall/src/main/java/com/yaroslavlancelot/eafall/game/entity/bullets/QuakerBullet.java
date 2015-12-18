package com.yaroslavlancelot.eafall.game.entity.bullets;

import com.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.yaroslavlancelot.eafall.game.engine.InstantRotationModifier;
import com.yaroslavlancelot.eafall.game.entity.gameobject.GameObject;
import com.yaroslavlancelot.eafall.game.entity.gameobject.equipment.damage.Damage;
import com.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.planet.PlanetStaticObject;
import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.Unit;
import com.yaroslavlancelot.eafall.game.player.IPlayer;
import com.yaroslavlancelot.eafall.game.player.PlayersHolder;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.modifier.IModifier;

import java.util.List;

/**
 * @author Yaroslav Havrylovych
 */
public class QuakerBullet extends AbstractBullet {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================
    /** used to hit all enemies in the given range */
    private String mEnemyPlayer;
    /** contains true if bullet was shot on the planet */
    private boolean mIsPlanet;

    // ===========================================================
    // Constructors
    // ===========================================================
    public QuakerBullet(int width, int height, ITextureRegion textureRegion,
                        VertexBufferObjectManager vertexBufferObjectManager) {
        super(width, height, textureRegion, vertexBufferObjectManager);
        registerEntityModifier(new InstantRotationModifier(2));
    }


    // ===========================================================
    // Getter & Setter
    // ===========================================================
    public void setPlayerName(String playerName) {
        mEnemyPlayer = playerName;
    }

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================
    @Override
    protected float duration(final float distance) {
        if (distance < 100) {
            return 0.4f;
        } else if (distance < 200) {
            return 0.8f;
        } else if (distance < 300) {
            return 1.2f;
        } else if (distance < 400) {
            return 1.6f;
        } else if (distance < 500) {
            return 2f;
        } else if (distance < 600) {
            return 2.4f;
        }
        return 3f;
    }

    @Override
    public void fire(final Damage damage, final float x, final float y, final GameObject gameObject) {
        super.fire(damage, x, y, gameObject);
        mIsPlanet = gameObject instanceof PlanetStaticObject;
    }

    @Override
    public void onModifierFinished(final IModifier pModifier, final Object pItem) {
        if (mIsPlanet) {
            if (mTarget.isObjectAlive()) {
                mTarget.damageObject(mDamage);
            }
        } else {
            IPlayer player = PlayersHolder.getPlayer(mEnemyPlayer);
            List<Unit> list = player.getUnitMap().getInRange(mTarget.getX(), mTarget.getY(),
                    SizeConstants.BULLET_MASS_DAMAGE_RADIUS);
            for (int i = 0; i < list.size(); i++) {
                Unit unit = list.get(i);
                if (unit.isObjectAlive()) {
                    unit.damageObject(mDamage);
                }
            }
        }
        destroy();
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
