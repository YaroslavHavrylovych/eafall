package com.yaroslavlancelot.eafall.test.game

import android.test.AndroidTestCase
import com.yaroslavlancelot.eafall.game.EaFallActivity
import com.yaroslavlancelot.eafall.game.GameState
import com.yaroslavlancelot.eafall.game.constant.StringConstants
import com.yaroslavlancelot.eafall.game.scene.SceneManager
import com.yaroslavlancelot.eafall.game.scene.hud.BaseGameHud
import com.yaroslavlancelot.eafall.game.scene.scenes.EaFallScene
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Test possible game sates and transitions between them.
 *
 * @author Yaroslav Havrylovych
 */
class GameStateTest : AndroidTestCase() {
    protected val testGameActivity = TestActivity();

    //#1
    fun testReset() {
        checkInitializationState()
    }

    //#2
    fun testStates() {
        checkInitializationState()

        testGameActivity.startAsyncResourceLoading()
        assertTrue(GameState.getState() == GameState.State.RESOURCE_LOADING,
                "state wasn't changed to RESOURCE_LOADING")
        assertFalse(GameState.isResourcesLoaded(),
                "GameState.isResourcesLoaded() doesn't work as it should")
        testGameActivity.setState(GameState.State.RESUMED)
        assertTrue(GameState.isResourcesLoaded(),
                "GameState.isResourcesLoaded() doesn't work as it should")
        assertTrue(GameState.isResumed(),
                "GameState.isResumed() doesn't work as it should")
        testGameActivity.setState(GameState.State.PAUSED)
        assertTrue(GameState.isResourcesLoaded(),
                "GameState.isResourcesLoaded() doesn't work as it should")
        assertTrue(GameState.isPaused(),
                "GameState.isPaused() doesn't work as it should")
        testGameActivity.setState(GameState.State.ENDED)
        assertTrue(GameState.isResourcesLoaded(),
                "GameState.isResourcesLoaded() doesn't work as it should")
        assertTrue(GameState.isGameEnded(),
                "GameState.isGameEnded() doesn't work as it should")
    }

    /** will work only if current game state is {@link GameState.State.INITIALIZATION} */
    fun checkInitializationState() {
        assertTrue(GameState.getState() == GameState.State.INITIALIZATION,
                "game state has to be INITIALIZED after reset")
        assertFalse(GameState.isResourcesLoaded(),
                "GameState.isResourcesLoaded() doesn't work as it should")
        assertFalse(GameState.isGameEnded(),
                "GameState.isResourcesLoaded() doesn't work as it should")
        assertFalse(GameState.isPaused(),
                "GameState.isResourcesLoaded() doesn't work as it should")
        assertFalse(GameState.isResumed(),
                "GameState.isResourcesLoaded() doesn't work as it should")
    }

    protected class TestActivity : EaFallActivity {
        override fun createHud(): BaseGameHud? {
            return BaseGameHud()
        }

        override fun preResourcesLoading() {
        }

        override fun createMusicPath(): String? {
            return StringConstants.getMusicPath() + "background_1.ogg";
        }

        constructor() {
            mSceneManager = DummySceneManager(this)
        }

        public override fun setState(state: GameState.State): Boolean {
            return super.setState(state)
        }

        public override fun startAsyncResourceLoading() {
            super.startAsyncResourceLoading();
        }

        override fun loadResources() {
        }

        override fun onResourcesLoaded() {
        }

        override fun onPopulateWorkingScene(scene: EaFallScene?) {
            throw UnsupportedOperationException()
        }

        override fun onShowWorkingScene() {
            throw UnsupportedOperationException()
        }

        class DummySceneManager : SceneManager {
            var mScene: EaFallScene

            constructor(eaFallActivity: EaFallActivity) : super(eaFallActivity) {
                mScene = EaFallScene(false)
            }

            override fun getWorkingScene(): EaFallScene? {
                return mScene
            }
        }
    }
}
