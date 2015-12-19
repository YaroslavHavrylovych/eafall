package com.yaroslavlancelot.eafall.game.engine;

import android.graphics.Bitmap;

import org.andengine.opengl.texture.atlas.bitmap.source.BaseBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.util.GLHelper;
import org.andengine.util.adt.color.Color;
import org.andengine.util.debug.Debug;

/**
 * Swaps given color components for each pixel of an image
 *
 * @author Yaroslav Havrylovych
 */
public class ArgbColorComponentsSwapBitmapTextureAtlas extends BaseBitmapTextureAtlasSource {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================
    private RgbaPart mFirstComponent;
    private RgbaPart mSecondComponent;

    // ===========================================================
    // Constructors
    // ===========================================================

    public ArgbColorComponentsSwapBitmapTextureAtlas(final IBitmapTextureAtlasSource pBitmapTextureAtlasSource,
                                                     RgbaPart firstComponent,
                                                     RgbaPart secondComponent) {
        super(pBitmapTextureAtlasSource);
        mFirstComponent = firstComponent;
        mSecondComponent = secondComponent;
    }


    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================
    @Override
    public ArgbColorComponentsSwapBitmapTextureAtlas deepCopy() {
        return new ArgbColorComponentsSwapBitmapTextureAtlas(this.mBitmapTextureAtlasSource,
                mFirstComponent, mSecondComponent);
    }

    @Override
    public Bitmap onLoadBitmap(final Bitmap.Config pBitmapConfig) {
        return this.swapColor(super.onLoadBitmap(pBitmapConfig));
    }

    @Override
    public Bitmap onLoadBitmap(final Bitmap.Config pBitmapConfig, final boolean pMutable) {
        return this.swapColor(super.onLoadBitmap(pBitmapConfig, pMutable));
    }

    // ===========================================================
    // Methods
    // ===========================================================

    /**
     * swaps r,g or b color components for the given bitmap
     *
     * @param pBitmap         given bitmap
     * @param firstComponent  first component to swap
     * @param secondComponent second component to swap
     * @return return new bitmap with swapped color components for each pixel
     */
    protected static final Bitmap swapArgbColorComponents_8888(final Bitmap pBitmap,
                                                               RgbaPart firstComponent,
                                                               RgbaPart secondComponent) {
        if (firstComponent == secondComponent) {
            return pBitmap;
        }

        final int[] pixelsARGB_8888 = GLHelper.getPixelsARGB_8888(pBitmap);
        pBitmap.recycle();

        int r, g, b, a, from, to;
        for (int i = pixelsARGB_8888.length - 1; i >= 0; i--) {
            final int pixelARGB_8888 = pixelsARGB_8888[i];
            r = RgbaPart.R.extractColorValue(pixelARGB_8888);
            g = RgbaPart.G.extractColorValue(pixelARGB_8888);
            b = RgbaPart.B.extractColorValue(pixelARGB_8888);
            a = RgbaPart.A.extractColorValue(pixelARGB_8888);
            from = firstComponent.extractColorValue(pixelARGB_8888);
            to = secondComponent.extractColorValue(pixelARGB_8888);

            if (firstComponent == RgbaPart.R) {
                r = to;
            } else if (firstComponent == RgbaPart.G) {
                g = to;
            } else if (firstComponent == RgbaPart.B) {
                b = to;
            }

            if (secondComponent == RgbaPart.R) {
                r = from;
            } else if (secondComponent == RgbaPart.G) {
                g = from;
            } else if (secondComponent == RgbaPart.B) {
                b = from;
            }

            pixelsARGB_8888[i] = (r << Color.ARGB_PACKED_RED_SHIFT)
                    + (g << Color.ARGB_PACKED_GREEN_SHIFT)
                    + (b << Color.ARGB_PACKED_BLUE_SHIFT)
                    + (a << Color.ARGB_PACKED_ALPHA_SHIFT);
        }

        return Bitmap.createBitmap(pixelsARGB_8888, pBitmap.getWidth(), pBitmap.getHeight(), Bitmap.Config.ARGB_8888);
    }

    /**
     * triggers
     * {@link ArgbColorComponentsSwapBitmapTextureAtlas#swapArgbColorComponents_8888(Bitmap, RgbaPart, RgbaPart)}
     * or throws an exception if bitmap config unsupported
     */
    protected Bitmap swapColor(final Bitmap pBitmap) {
        final Bitmap.Config config = pBitmap.getConfig();
        switch (config) {
            case ARGB_8888:
                return ArgbColorComponentsSwapBitmapTextureAtlas.swapArgbColorComponents_8888(
                        pBitmap, mFirstComponent, mSecondComponent);
            default:
                Debug.w("Unexpected " + Bitmap.Config.class.getSimpleName() + ": '" + config + "'.");
                return pBitmap;
        }
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    /**
     * Simplified color component passing and extracting component
     * {@link ArgbColorComponentsSwapBitmapTextureAtlas.RgbaPart#extractColorValue(int)}
     * from {@link Color}
     */
    public enum RgbaPart {
        R, G, B, A;

        public static RgbaPart createFromColor(Color color) {
            if (color == Color.RED) {
                return R;
            } else if (color == Color.GREEN) {
                return G;
            } else if (color == Color.BLUE) {
                return B;
            }
            throw new IllegalArgumentException("Color can be only red, blue or green but on other");
        }

        public int extractColorValue(int argbColor) {
            switch (this) {
                case R:
                    return (argbColor >> Color.ARGB_PACKED_RED_SHIFT) & 0xFF;
                case G:
                    return (argbColor >> Color.ARGB_PACKED_GREEN_SHIFT) & 0xFF;
                case B:
                    return (argbColor >> Color.ARGB_PACKED_BLUE_SHIFT) & 0xFF;
                case A:
                    return (argbColor >> Color.ARGB_PACKED_ALPHA_SHIFT) & 0xFF;
                default:
                    throw new IllegalStateException("in ARGB not only ARGB");
            }
        }
    }
}
