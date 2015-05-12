/*
 * Copyright (c) 2013 Chun-Ying Huang
 *
 * This file is part of GamingAnywhere (GA).
 *
 * GA is free software; you can redistribute it and/or modify it
 * under the terms of the 3-clause BSD License as published by the
 * Free Software Foundation: http://directory.fsf.org/wiki/License:BSD_3Clause
 *
 * GA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * You should have received a copy of the 3-clause BSD License along with GA;
 * if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package org.gaminganywhere.gaclient.util;

import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import org.gaminganywhere.gaclient.util.Joystick.JoystickMovedListener;
import org.gaminganywhere.gaclient.util.Joystick.JoystickView;
import org.gaminganywhere.gaclient.util.Pad.PartitionEventListener;


public class GAControllerMouse extends GAController implements
	OnClickListener, PartitionEventListener
{
    private Button buttonPotion = null;

	public GAControllerMouse(Context context) {
        super(context);
        this.relative = false;

	}
	
	public static String getName() {
		return "Mouse";
	}
	
	public static String getDescription() {
		return "Mouse only controller for diablo 3";
	}

	@Override
	public void onDimensionChange(int width, int height) {

		int keyBtnHeight = height/8;
        int keyBtnWidth = keyBtnHeight;
		// must be called first!
		super.onDimensionChange(width, height);
        buttonPotion = newButton("q", 0, height/2, keyBtnWidth, keyBtnHeight);
        buttonPotion.setOnTouchListener(this);
	}

    boolean PRIMARY = false;
    boolean SECONDARY = false;

	@Override
	public boolean onTouch(View v, MotionEvent evt) {
		int action = evt.getActionMasked();
        int mouseButton = evt.getButtonState();
        Log.d("BUTTON_STATE", Integer.toString(mouseButton));
        Log.d("ACTION", Integer.toString(action));

        switch (evt.getButtonState()) {
            case MotionEvent.BUTTON_PRIMARY:
                PRIMARY = true;
                break;
            case MotionEvent.BUTTON_SECONDARY:
                SECONDARY = true;
                break;
        }

        switch (evt.getActionMasked()) {
            case MotionEvent.ACTION_MOVE:
                switch(mouseButton){
                    case 1:
                        PRIMARY = true;
                        SECONDARY = false;
                        break;
                    case 2:
                        PRIMARY = false;
                        SECONDARY = true;
                        break;
                    case 3:
                        PRIMARY = true;
                        SECONDARY = true;
                        break;
                }
                break;
            case MotionEvent.ACTION_UP:
                PRIMARY = false;
                SECONDARY = false;
                break;
        }
        Log.d("mouse primary", Boolean.toString(PRIMARY));
        Log.d("mouse secondary", Boolean.toString(SECONDARY));
        this.sendMouseKey(PRIMARY, SDL2.Button.LEFT, getMouseX(), getMouseY());
        this.sendMouseKey(SECONDARY, SDL2.Button.RIGHT, getMouseX(), getMouseY());

		return super.onTouch(v, evt);
	}


    private boolean sendKey(int keyCode, int event) {
        switch(keyCode)  {
            case 4:
                Log.d("sendKey", Integer.toString(keyCode));
                handleButtonTouch(event, SDL2.Scancode._1, SDL2.Keycode._1, 0, 0);
                break;
            case 125:
                Log.d("sendKey", Integer.toString(keyCode));
                handleButtonTouch(event, SDL2.Scancode._2, SDL2.Keycode._2, 0, 0);
                break;
        }
        return true;
    }

    @Override
    public boolean onKeyDownReceived(int keyCode) {
        return sendKey(keyCode, MotionEvent.ACTION_DOWN);
    }

    @Override
    public boolean onKeyUpReceived(int keyCode) {
        return sendKey(keyCode, MotionEvent.ACTION_UP);
    }

    @Override
	public void onPartitionEvent(View v, int action, int part) {

	}

	@Override
	public void onClick(View v) {
	}
}
