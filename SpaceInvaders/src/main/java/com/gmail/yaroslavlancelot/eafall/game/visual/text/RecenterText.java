package com.gmail.yaroslavlancelot.eafall.game.visual.text;

import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.entity.text.exception.OutOfCharactersException;
import org.andengine.entity.text.vbo.ITextVertexBufferObject;
import org.andengine.opengl.font.FontUtils;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * AndeEngine {@link org.andengine.entity.text.Text} wrapper. Extend existing func with
 * re-center text if the content was changed. Used to fix left side of the text on the same position.
 * <br/>
 * In addition when you set the text, x is the left corner of the text (not the center)
 */
public abstract class RecenterText extends Text {
    public RecenterText(float pX, float pY, IFont pFont, CharSequence pText, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pFont, pText, pVertexBufferObjectManager);
    }

    public RecenterText(float pX, float pY, IFont pFont, CharSequence pText, VertexBufferObjectManager pVertexBufferObjectManager, ShaderProgram pShaderProgram) {
        super(pX, pY, pFont, pText, pVertexBufferObjectManager, pShaderProgram);
    }

    public RecenterText(float pX, float pY, IFont pFont, CharSequence pText, VertexBufferObjectManager pVertexBufferObjectManager, DrawType pDrawType) {
        super(pX, pY, pFont, pText, pVertexBufferObjectManager, pDrawType);
    }

    public RecenterText(float pX, float pY, IFont pFont, CharSequence pText, VertexBufferObjectManager pVertexBufferObjectManager, DrawType pDrawType, ShaderProgram pShaderProgram) {
        super(pX, pY, pFont, pText, pVertexBufferObjectManager, pDrawType, pShaderProgram);
    }

    public RecenterText(float pX, float pY, IFont pFont, CharSequence pText, TextOptions pTextOptions, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pFont, pText, pTextOptions, pVertexBufferObjectManager);
    }

    public RecenterText(float pX, float pY, IFont pFont, CharSequence pText, TextOptions pTextOptions, VertexBufferObjectManager pVertexBufferObjectManager, ShaderProgram pShaderProgram) {
        super(pX, pY, pFont, pText, pTextOptions, pVertexBufferObjectManager, pShaderProgram);
    }

    public RecenterText(float pX, float pY, IFont pFont, CharSequence pText, TextOptions pTextOptions, VertexBufferObjectManager pVertexBufferObjectManager, DrawType pDrawType) {
        super(pX, pY, pFont, pText, pTextOptions, pVertexBufferObjectManager, pDrawType);
    }

    public RecenterText(float pX, float pY, IFont pFont, CharSequence pText, TextOptions pTextOptions, VertexBufferObjectManager pVertexBufferObjectManager, DrawType pDrawType, ShaderProgram pShaderProgram) {
        super(pX, pY, pFont, pText, pTextOptions, pVertexBufferObjectManager, pDrawType, pShaderProgram);
    }

    public RecenterText(float pX, float pY, IFont pFont, CharSequence pText, int pCharactersMaximum, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pFont, pText, pCharactersMaximum, pVertexBufferObjectManager);
    }

    public RecenterText(float pX, float pY, IFont pFont, CharSequence pText, int pCharactersMaximum, VertexBufferObjectManager pVertexBufferObjectManager, ShaderProgram pShaderProgram) {
        super(pX, pY, pFont, pText, pCharactersMaximum, pVertexBufferObjectManager, pShaderProgram);
    }

    public RecenterText(float pX, float pY, IFont pFont, CharSequence pText, int pCharactersMaximum, VertexBufferObjectManager pVertexBufferObjectManager, DrawType pDrawType) {
        super(pX, pY, pFont, pText, pCharactersMaximum, pVertexBufferObjectManager, pDrawType);
    }

    public RecenterText(float pX, float pY, IFont pFont, CharSequence pText, int pCharactersMaximum, VertexBufferObjectManager pVertexBufferObjectManager, DrawType pDrawType, ShaderProgram pShaderProgram) {
        super(pX, pY, pFont, pText, pCharactersMaximum, pVertexBufferObjectManager, pDrawType, pShaderProgram);
    }

    public RecenterText(float pX, float pY, IFont pFont, CharSequence pText, int pCharactersMaximum, TextOptions pTextOptions, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pFont, pText, pCharactersMaximum, pTextOptions, pVertexBufferObjectManager);
    }

    public RecenterText(float pX, float pY, IFont pFont, CharSequence pText, int pCharactersMaximum, TextOptions pTextOptions, VertexBufferObjectManager pVertexBufferObjectManager, ShaderProgram pShaderProgram) {
        super(pX, pY, pFont, pText, pCharactersMaximum, pTextOptions, pVertexBufferObjectManager, pShaderProgram);
    }

    public RecenterText(float pX, float pY, IFont pFont, CharSequence pText, int pCharactersMaximum, TextOptions pTextOptions, VertexBufferObjectManager pVertexBufferObjectManager, DrawType pDrawType) {
        super(pX, pY, pFont, pText, pCharactersMaximum, pTextOptions, pVertexBufferObjectManager, pDrawType);
    }

    public RecenterText(float pX, float pY, IFont pFont, CharSequence pText, int pCharactersMaximum, TextOptions pTextOptions, VertexBufferObjectManager pVertexBufferObjectManager, DrawType pDrawType, ShaderProgram pShaderProgram) {
        super(pX, pY, pFont, pText, pCharactersMaximum, pTextOptions, pVertexBufferObjectManager, pDrawType, pShaderProgram);
    }

    public RecenterText(float pX, float pY, IFont pFont, CharSequence pText, int pCharactersMaximum, TextOptions pTextOptions, ITextVertexBufferObject pTextVertexBufferObject) {
        super(pX, pY, pFont, pText, pCharactersMaximum, pTextOptions, pTextVertexBufferObject);
    }

    public RecenterText(float pX, float pY, IFont pFont, CharSequence pText, int pCharactersMaximum, TextOptions pTextOptions, ITextVertexBufferObject pTextVertexBufferObject, ShaderProgram pShaderProgram) {
        super(pX, pY, pFont, pText, pCharactersMaximum, pTextOptions, pTextVertexBufferObject, pShaderProgram);
    }

    @Override
    public void setText(CharSequence pText) throws OutOfCharactersException {
        CharSequence oldText = getText();
        super.setText(pText);
        setX(getX() +
                (pText == null ? 0 : FontUtils.measureText(getFont(), pText) / 2) -
                (oldText == null ? 0 : FontUtils.measureText(getFont(), oldText) / 2));
    }
}
