package com.yaroslavlancelot.eafall.test.game.entity.gameobject.unit

import android.content.Context
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.yaroslavlancelot.eafall.EaFallApplication
import com.yaroslavlancelot.eafall.game.alliance.imperials.Imperials
import com.yaroslavlancelot.eafall.game.batching.BatchingKeys
import com.yaroslavlancelot.eafall.game.batching.SpriteGroupHolder
import com.yaroslavlancelot.eafall.game.client.IPhysicCreator
import com.yaroslavlancelot.eafall.game.configuration.Config
import com.yaroslavlancelot.eafall.game.configuration.mission.MissionConfig
import com.yaroslavlancelot.eafall.game.constant.SizeConstants
import com.yaroslavlancelot.eafall.game.entity.BodiedSprite
import com.yaroslavlancelot.eafall.game.player.IPlayer
import com.yaroslavlancelot.eafall.game.player.Player
import com.yaroslavlancelot.eafall.game.player.PlayersHolder
import com.yaroslavlancelot.eafall.general.SelfCleanable
import com.yaroslavlancelot.eafall.test.game.EaFallTestCase
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

/**
 * Unit class test (it's abstract as no such entity as just unit)
 *
 * @author Yaroslav Havrylovych
 */
abstract class UnitTest : EaFallTestCase() {
    val mPlayerName: String = "test_player"
    val mEnemyPlayerName: String = "test_enemy_player"
    val mUnitsSpriteGroupName: String = BatchingKeys.getUnitSpriteGroup(mPlayerName)
    protected val mWorld: PhysicsWorld = PhysicsWorld(Vector2(0f, 0f), false, 2, 2)
    protected val mUnitTexture: EmptyTexture = EmptyTexture(TextureManager(), 20, 20)


    abstract fun createUnit(): com.yaroslavlancelot.eafall.game.entity.gameobject.unit.Unit

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
        var player = Player(mPlayerName, imperials, IPlayer.ControlType.USER_CONTROL_ON_SERVER_SIDE,
                1000, -1, MissionConfig())
        PlayersHolder.getInstance().addElement(mPlayerName, player)
        player = Player(mEnemyPlayerName, imperials, IPlayer.ControlType.BOT_CONTROL_ON_SERVER_SIDE,
                1000, 5, MissionConfig())
        PlayersHolder.getInstance().addElement(mEnemyPlayerName, player)
        PlayersHolder.getPlayer(mPlayerName).enemyPlayer = player
        //used for bullets, health and units
        val spriteGroup = SpriteGroup(mUnitTexture, 10, vBom)
        SpriteGroupHolder.addGroup(mUnitsSpriteGroupName, spriteGroup)
        SpriteGroupHolder.addGroup(BatchingKeys.BULLET_AND_HEALTH, spriteGroup)
    }

    fun testUpdatersRegistration() {
    }


    fun testInitDestroy() {
        EaFallApplication.setConfig(HiddenHealthBarConfig(EaFallApplication.getContext()))
        //new unit
        val unit = createUnit()
        unit.setPlayer(mPlayerName)
        val player = PlayersHolder.getPlayer(mPlayerName)
        val physicCreator = PhysicCreator(player, mWorld)
        var posX = 10f
        var posY = 10f
        assertTrue(unit.body == null, "unit has body but he shouldn't")
        unit.init(posX, posY, physicCreator)
        checkInit(unit, posX.toInt(), posY.toInt(), 90f, player)
        checkDestroy(unit, player)
        //existing unit
        val body = unit.body
        posX = (SizeConstants.GAME_FIELD_WIDTH - 10).toFloat()
        posY = (SizeConstants.GAME_FIELD_HEIGHT - 10).toFloat()
        unit.init(posX, posY, physicCreator)
        checkInit(unit, posX.toInt(), posY.toInt(), -90f, player)
        checkDestroy(unit, player)
        assertEquals("existing unit body doesn't have to change", body, unit.body)
    }

    ////////////////
    //Children tests
    ////////////////
    abstract fun testRotation()

    /////////
    //helpers
    /////////
    protected fun checkDestroy(unit: com.yaroslavlancelot.eafall.game.entity.gameobject.unit.Unit,
                               player: IPlayer) {
        if (unit is TestableUnit) {
            unit.invokeDestroy()
        } else {
            throw IllegalStateException("unit has to implement TestableUnit")
        }
        val pos = BodiedSprite.BODY_OUT_OF_CAMERA
        assertTrue(unit.isVisible, "unit visible")
        assertFalse(unit.body.isActive, "unit body active")
        val position = unit.body.position
        assertTrue(position.x == pos && position.y == pos, "unit body positions doesn't set")
        assertFalse(player.playerUnits.contains(unit), "unit doesn't removed to the payer")
    }

    protected fun checkInit(unit: com.yaroslavlancelot.eafall.game.entity.gameobject.unit.Unit,
                            posX: Int, posY: Int, rotation: Float, player: IPlayer) {
        assertNotNull(unit.body, "unit doesn't have body")
        assertTrue(unit.rotation == rotation, "unit rotated correctly on init")
        assertTrue(unit.isVisible, "unit invisible")
        assertFalse(unit.isIgnoreUpdate, "unit ignores updates")
        assertTrue(unit.body.isActive, "unit body disabled")
        val position = unit.body.position
        val x = posX / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT
        val y = posY / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT
        assertTrue(position.x == x && position.y == y, "unit body positions doesn't set")
        assertTrue(player.playerUnits.contains(unit), "unit doesn't added to the payer")
    }

    //////////////////////////////
    //Custom Classes && Interfaces
    //////////////////////////////
    protected class PhysicCreator(val mPlayer: IPlayer, val mWorld: PhysicsWorld) : IPhysicCreator {
        override fun createPhysicBody(bodiedSprite: BodiedSprite, bodyType: BodyDef.BodyType, fixtureDef: FixtureDef): Body {
            val body = PhysicsFactory.createCircleBody(mWorld, bodiedSprite, BodyDef.BodyType.DynamicBody, mPlayer.fixtureDefUnit)
            mWorld.registerPhysicsConnector(PhysicsConnector(bodiedSprite, body, true, false))
            bodiedSprite.body = body
            return body
        }
    }

    class HiddenHealthBarConfig(context: Context?) : Config(context) {
    }

    interface TestableUnit {
        fun getRotationModifier(): RotationModifier

        fun getUpdateHandler(): IUpdateHandler

        fun invokeDestroy()

        fun setStr(str: String)

        fun getStr(): String
    }
}
