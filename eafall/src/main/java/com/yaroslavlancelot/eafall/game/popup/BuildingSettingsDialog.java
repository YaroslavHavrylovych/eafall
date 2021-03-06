package com.yaroslavlancelot.eafall.game.popup;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.yaroslavlancelot.eafall.R;
import com.yaroslavlancelot.eafall.android.activities.StartupActivity;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.buildings.IUnitBuilding;
import com.yaroslavlancelot.eafall.general.locale.LocaleImpl;

/**
 * Building settings dialog fragment
 *
 * @author Yaroslav Havrylovych
 */
public class BuildingSettingsDialog extends Dialog {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================
    /** Health bar unit behaviour from the settings */
    private IUnitBuilding mUnitBuilding;
    private int mSelectedId;
    private TextView mTitle;

    // ===========================================================
    // Constructors
    // ===========================================================
    public BuildingSettingsDialog(final Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.dialog_building_settings);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mTitle = (TextView) findViewById(R.id.title_container);
        updateTitle(mUnitBuilding.getBuildingDummy().getStringId());
        //variants
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.building_settings);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final RadioGroup group, final int checkedId) {
                mSelectedId = checkedId;
            }
        });
        //done button
        Button doneButton = (Button) findViewById(R.id.done_button);
        doneButton.getPaint().setShader(StartupActivity.getTextGradient(doneButton.getLineHeight()));
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                switch (mSelectedId) {
                    case R.id.building_settings_pause: {
                        mUnitBuilding.pause();
                        break;
                    }
                    case R.id.building_settings_top: {
                        mUnitBuilding.unPause();
                        mUnitBuilding.setPath(true);
                        break;
                    }
                    default: {
                        mUnitBuilding.unPause();
                        mUnitBuilding.setPath(false);
                    }
                }
                dismiss();
            }
        });
    }


    // ===========================================================
    // Methods
    // ===========================================================
    public void init(IUnitBuilding unitBuilding) {
        updateTitle(unitBuilding.getBuildingDummy().getStringId());
        //select one
        mUnitBuilding = unitBuilding;
        if (unitBuilding.isPaused()) {
            mSelectedId = R.id.building_settings_pause;
        } else if (unitBuilding.isTopPath()) {
            mSelectedId = R.id.building_settings_top;
        } else {
            mSelectedId = R.id.building_settings_bottom;
        }
        RadioButton radioButton = (RadioButton) findViewById(mSelectedId);
        radioButton.setChecked(true);
    }

    private void updateTitle(int resId) {
        if (mTitle != null) {
            String str = LocaleImpl.getInstance().getStringById(resId);
            mTitle.setText(str.toUpperCase());
        }
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
