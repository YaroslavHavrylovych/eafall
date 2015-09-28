package com.gmail.yaroslavlancelot.eafall.test.game.popup

import android.test.AndroidTestCase
import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants
import com.gmail.yaroslavlancelot.eafall.game.popup.IPopup
import com.gmail.yaroslavlancelot.eafall.game.popup.PopupScene
import com.gmail.yaroslavlancelot.eafall.game.scene.scenes.EaFallScene
import org.andengine.engine.camera.VelocityCamera
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Check basic popup scene functionality
 *
 * @author Yaroslav Havrylovych
 */
class PopupSceneTest : AndroidTestCase() {
    val mScene = EaFallScene(false)
    val mCamera = VelocityCamera(0f, 0f,
            SizeConstants.GAME_FIELD_WIDTH.toFloat(), SizeConstants.GAME_FIELD_HEIGHT.toFloat(),
            1f, 1f, 1.5f);

    fun testVisibility() {
        val popupScene = PopupScene(mScene, mCamera)
        assertFalse(popupScene.isShowing, "popup scene has to be invisible now")
        popupScene.showPopup()
        assertTrue(popupScene.isShowing, "popup has to be visible")
        popupScene.showPopup()
        assertTrue(popupScene.isShowing, "popup has to be visible")
        popupScene.hidePopup()
        assertFalse(popupScene.isShowing, "popup has to be invisible")
    }

    fun testStateChanges() {
        val popupScene = PopupScene(mScene, mCamera)
        assertFalse(popupScene.isShowing, "popup scene has to be invisible now")
        val stateListener = object : IPopup.StateChangingListener {
            var wasTrigger = false

            override fun onShowed() {
                wasTrigger = true
            }

            override fun onHided() {
                wasTrigger = false
            }
        }
        assertFalse(stateListener.wasTrigger, "shown state has to be false at the beginning")
        popupScene.setStateChangeListener(stateListener)
        popupScene.showPopup()
        assertTrue(stateListener.wasTrigger, "state change listener hasn't been triggered after the popup was showed")
        popupScene.hidePopup()
        assertFalse(stateListener.wasTrigger, "state change listener hasn't been triggered after the popup was hidden")
    }
}
