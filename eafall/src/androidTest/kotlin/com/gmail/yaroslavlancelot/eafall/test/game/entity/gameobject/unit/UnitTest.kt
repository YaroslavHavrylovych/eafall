package com.gmail.yaroslavlancelot.eafall.test.game.entity.gameobject.unit

import android.content.Context
import android.test.AndroidTestCase
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.gmail.yaroslavlancelot.eafall.EaFallApplication
import com.gmail.yaroslavlancelot.eafall.game.alliance.imperials.Imperials
import com.gmail.yaroslavlancelot.eafall.game.batching.BatchingKeys
import com.gmail.yaroslavlancelot.eafall.game.batching.SpriteGroupHolder
import com.gmail.yaroslavlancelot.eafall.game.client.IPhysicCreator
import com.gmail.yaroslavlancelot.eafall.game.configuration.Config
import com.gmail.yaroslavlancelot.eafall.game.configuration.mission.MissionConfig
import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants
import com.gmail.yaroslavlancelot.eafall.game.entity.BodiedSprite
import com.gmail.yaroslavlancelot.eafall.game.player.IPlayer
import com.gmail.yaroslavlancelot.eafall.game.player.Player
import com.gmail.yaroslavlancelot.eafall.game.player.PlayersHolder
import com.gmail.yaroslavlancelot.eafall.general.SelfCleanable
import org.andengine.engine.handler.IUpdateHandler
import org.andengine.entity.modifier.RotationModifier
import org.andengine.entity.sprite.batch.SpriteGroup
import org.andengine.extension.physics.box2d.PhysicsConnector
import org.andengine.extension.physics.box2d.PhysicsFactory
import org.andengine.extension.physics.box2d.PhysicsWorld
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants
import org.andengine.opengl.texture.EmptyTexture
import org.andengine.opengl.texture.TextureManager
import org.andengine.opengl.vbo.VertexBufferObjectManager
import kotlin.test.*

/**
 * Unit class test (it's abstract as no such entity as just unit)
 *
 * @author Yaroslav Havrylovych
 */
abstract class UnitTest : AndroidTestCase() {
    public val mPlayerName: String = "test_player"
    public val mEnemyPlayerName: String = "test_enemy_player"
    public val mUnitsSpriteGroupName: String = BatchingKeys.getUnitSpriteGroup(mPlayerName)
    protected val mWorld: PhysicsWorld = PhysicsWorld(Vector2(0f, 0f), false, 2, 2)
    protected val mUnitTexture: EmptyTexture = EmptyTexture(TextureManager(), 20, 20)


    abstract fun createUnit(): com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.Unit

    /////////////////////
    //Current class tests
    /////////////////////
    /** invokes after each test method */
    override fun tearDown() {
        super.tearDown()
        SelfCleanable.clearMemory()
    }

    /** invokes before each test method */
    override fun setUp() {
        super.setUp()
        val vBom = VertexBufferObjectManager()
        val imperials = Imperials(vBom)
        var player = Player(mPlayerName, imperials, IPlayer.ControlType.USER_CONTROL_ON_SERVER_SIDE, -1, MissionConfig())
        PlayersHolder.getInstance().addElement(mPlayerName, player)
        player = Player(mEnemyPlayerName, imperials, IPlayer.ControlType.BOT_CONTROL_ON_SERVER_SIDE, 5, MissionConfig())
        PlayersHolder.getInstance().addElement(mEnemyPlayerName, player)
        PlayersHolder.getPlayer(mPlayerName).enemyPlayer = player;
        //used for bullets, health and units
        val spriteGroup = SpriteGroup(mUnitTexture, 10, vBom)
        SpriteGroupHolder.addGroup(mUnitsSpriteGroupName, spriteGroup)
        SpriteGroupHolder.addGroup(BatchingKeys.BULLET_AND_HEALTH, spriteGroup)
    }

    public fun testUpdatersRegistration() {
    }


    public fun testInitDestroy() {
        EaFallApplication.setConfig(HiddenHealthBarConfig(EaFallApplication.getContext()))
        //new unit
        val unit = createUnit()
        unit.setPlayer(mPlayerName)
        val player = PlayersHolder.getPlayer(mPlayerName)
        val physicCreator = PhysicCreator(player, mWorld)
        var posX = 10f
        var posY = 10f
        assertNull(unit.getBody(), "unit has body but he shouldn't")
        unit.init(posX, posY, physicCreator)
        checkInit(unit, posX.toInt(), posY.toInt(), 90f, player)
        checkDestroy(unit, player)
        //existing unit
        val body = unit.getBody()
        posX = (SizeConstants.GAME_FIELD_WIDTH - 10).toFloat()
        posY = (SizeConstants.GAME_FIELD_HEIGHT - 10).toFloat()
        unit.init(posX, posY, physicCreator)
        checkInit(unit, posX.toInt(), posY.toInt(), -90f, player)
        checkDestroy(unit, player)
        assertEquals(body, unit.getBody(), "existing unit body doesn't have to change")
    }

    ////////////////
    //Children tests
    ////////////////
    abstract fun testRotation()

    /////////
    //helpers
    /////////
    protected fun checkDestroy(unit: com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.Unit,
                               player: IPlayer) {
        if (unit is TestableUnit) {
            unit.invokeDestroy()
        } else {
            throw IllegalStateException("unit has to implement TestableUnit")
        }
        val pos = BodiedSprite.BODY_OUT_OF_CAMERA
        assertTrue(unit.isVisible(), "unit visible")
        assertFalse(unit.getBody().isActive(), "unit body active")
        val position = unit.getBody().getPosition()
        assertTrue(position.x == pos && position.y == pos, "unit body positions doesn't set")
        assertFalse(player.getPlayerUnits().contains(unit), "unit doesn't removed to the payer")
    }

    protected fun checkInit(unit: com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.Unit,
                            posX: Int, posY: Int, rotation: Float, player: IPlayer) {
        assertNotNull(unit.getBody(), "unit doesn't have body")
        assertTrue(unit.getRotation() == rotation, "unit rotated correctly on init")
        assertTrue(unit.isVisible(), "unit invisible")
        assertFalse(unit.isIgnoreUpdate(), "unit ignores updates")
        assertTrue(unit.getBody().isActive(), "unit body disabled")
        val position = unit.getBody().getPosition()
        val x = posX / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT
        val y = posY / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT
        assertTrue(position.x == x && position.y == y, "unit body positions doesn't set")
        assertTrue(player.getPlayerUnits().contains(unit), "unit doesn't added to the payer")
    }

    //////////////////////////////
    //Custom Classes && Interfaces
    //////////////////////////////
    protected class PhysicCreator(val mPlayer: IPlayer, val mWorld: PhysicsWorld) : IPhysicCreator {
        override fun createPhysicBody(bodiedSprite: BodiedSprite, bodyType: BodyDef.BodyType, fixtureDef: FixtureDef): Body {
            val body = PhysicsFactory.createCircleBody(mWorld, bodiedSprite, BodyDef.BodyType.DynamicBody, mPlayer.getFixtureDefUnit())
            mWorld.registerPhysicsConnector(PhysicsConnector(bodiedSprite, body, true, false))
            bodiedSprite.setBody(body)
            return body
        }
    }

    public class HiddenHealthBarConfig(context: Context?) : Config(context) {
    }

    public interface TestableUnit {
        fun getRotationModifier(): RotationModifier

        fun getUpdateHandler(): IUpdateHandler

        fun invokeDestroy()

        fun setStr(str: String)

        fun getStr(): String
    }
}
