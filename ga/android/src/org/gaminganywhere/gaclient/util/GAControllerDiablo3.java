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
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import org.gaminganywhere.gaclient.util.Joystick.JoystickView;
import org.gaminganywhere.gaclient.util.Pad.PartitionEventListener;
import org.gaminganywhere.gaclient.util.Joystick.JoystickMovedListener;


public class GAControllerDiablo3 extends GAController implements
	OnClickListener, PartitionEventListener
{
	int centerX = 0;
    int centerY = 0;
    float playerX  = 0;
    float playerY = 0;

    private Button buttonEsc = null;
    private Button buttonOne = null;
    private Button buttonTwo = null;
    private Button buttonThree = null;
    private Button buttonFour = null;

    private Button mouseLeftClick = null;
    private Button mouseRightClick = null;

    private Button buttonPotion = null;

    JoystickView joystick;

	public GAControllerDiablo3(Context context) {
        super(context);
        this.relative = false;

	}
	
	public static String getName() {
		return "Diablo 3";
	}
	
	public static String getDescription() {
		return "virtual pad controller";
	}

	@Override
	public void onDimensionChange(int width, int height) {

        centerX = width /2;
        centerY = height/2;
        playerX = centerX;
        playerY = (height - (height*18/100))/2;
        joystick = new JoystickView(getContext());
        joystick.setMovementConstraint(JoystickView.CONSTRAIN_CIRCLE);
        joystick.setOnJostickMovedListener(_listener);


		int keyBtnHeight = height/8;
        int keyBtnWidth = keyBtnHeight;
        int margin = (height-(keyBtnHeight*6))/6;
        int paddingLeft = (width/10)*9;
		// must be called first!
		super.onDimensionChange(width,  height);
		// button ESC
		buttonEsc = null;
		buttonEsc = new Button(getContext());
		buttonEsc.setTextSize(10);
		buttonEsc.setText("ESC");
		buttonEsc.setOnClickListener(this);
		placeView(buttonEsc, width/20, height/20, keyBtnWidth, keyBtnHeight);
		//
        buttonOne = newButton("1", paddingLeft, margin, keyBtnWidth, keyBtnHeight);
        buttonOne.setOnTouchListener(this);
        buttonTwo = newButton("2", paddingLeft, margin+keyBtnHeight+margin, keyBtnWidth, keyBtnHeight);
        buttonTwo.setOnTouchListener(this);
        buttonThree = newButton("3", paddingLeft, (margin+keyBtnHeight)*2, keyBtnWidth, keyBtnHeight);
        buttonThree.setOnTouchListener(this);
        buttonFour = newButton("4", paddingLeft, (margin+keyBtnHeight)*3, keyBtnWidth, keyBtnHeight);
        buttonFour.setOnTouchListener(this);
        mouseLeftClick = newButton("L", paddingLeft, (margin+keyBtnHeight)*4, keyBtnWidth, keyBtnHeight);
        mouseLeftClick.setOnTouchListener(this);
        mouseRightClick = newButton("R", paddingLeft, (margin+keyBtnHeight)*5, keyBtnWidth, keyBtnHeight);
        mouseRightClick.setOnTouchListener(this);
        buttonPotion = newButton("q", 0, height/2, keyBtnWidth, keyBtnHeight);
        buttonPotion.setOnTouchListener(this);

        placeView(joystick, 0, (height/3)*2, width/3, height/3);

	}

	@Override
	public boolean onTouch(View v, MotionEvent evt) {
		int count = evt.getPointerCount();
        int action = evt.getActionMasked();

        if(v == buttonOne){
            return handleButtonTouch(action, SDL2.Scancode._1, SDL2.Keycode._1, 0, 0);
        }
        if(v == buttonTwo) {
            return handleButtonTouch(action, SDL2.Scancode._2, SDL2.Keycode._2, 0, 0);
        }
        if(v == buttonThree) {
            return handleButtonTouch(action, SDL2.Scancode._3, SDL2.Keycode._3, 0, 0);
        }
        if(v == buttonFour) {
            return handleButtonTouch(action, SDL2.Scancode._4, SDL2.Keycode._4, 0, 0);
        }
        if(v == buttonPotion) {
            return handleButtonTouch(action, SDL2.Scancode.Q , SDL2.Keycode.q, 0, 0);
        }
        if(v == mouseLeftClick){
            emulateMouseButtons(action, 1);
        }
        if(v == mouseRightClick){
            emulateMouseButtons(action, 2);
        }
		// must be called last
		return super.onTouch(v, evt);
	}

    @Override
    public boolean onKeyDownReceived(int keyCode) {
        return false;
    }

    @Override
    public boolean onKeyUpReceived(int keyCode) {
        return false;
    }

    private int mouseButton = -1;
    //TODO reset to mouse buttons
	private void emulateMouseButtons(int action, int part) {
		switch(action) {
		case MotionEvent.ACTION_DOWN:
		//case MotionEvent.ACTION_POINTER_DOWN:
			if(part == 1)
				mouseButton = SDL2.Button.LEFT;
			else
				mouseButton = SDL2.Button.RIGHT;
			this.sendMouseKey(true, mouseButton, getMouseX(), getMouseY());
			break;
		case MotionEvent.ACTION_UP:
		//case MotionEvent.ACTION_POINTER_UP:
			if(mouseButton != -1) {
				sendMouseKey(false, mouseButton, getMouseX(), getMouseY());
				mouseButton = -1;
			}
			break;
		}
	}
	
	@Override
	public void onPartitionEvent(View v, int action, int part) {

	}

	@Override
	public void onClick(View v) {
		if(v == buttonEsc) {
			sendKeyEvent(true, SDL2.Scancode.ESCAPE, 0x1b, 0, 0);
			sendKeyEvent(false, SDL2.Scancode.ESCAPE, 0x1b, 0, 0);
		}
	}

    private JoystickMovedListener _listener = new JoystickMovedListener() {

        @Override
        public void OnMoved(int pan, int tilt) {

            int offsetX = 10;
            int offsetY = 10;
            float dx = pan * offsetX + playerX ;
            float dy = tilt * offsetY + playerY;
            //Log.d("ga_log", String.format("joystick position (%s, %s) centerX = %s, centerY = %s", dx, dy, centerX, centerY));
            //sendMouseKey(true, SDL2.Button.LEFT, dx, dy);
            sendMouseMotion(dx, dy, 0, 0, 0, false);
            drawCursor((int) dx, (int) dy);
        }

        @Override
        public void OnReleased() {
            //sendMouseKey(true, SDL2.Button.LEFT, playerX, playerY);
            //sendMouseKey(false, SDL2.Button.LEFT, playerX, playerY);
        }

        public void OnReturnedToCenter() {
            //sendMouseKey(true, SDL2.Button.LEFT, playerX, playerY);
            //sendMouseKey(false, SDL2.Button.LEFT, playerX, playerY);
        };
    };
}
