package com.gilded.thegame.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.gilded.thegame.TheGame;

public class GwtLauncher extends GwtApplication {
	@Override
	public GwtApplicationConfiguration getConfig () {
		GwtApplicationConfiguration cfg = new GwtApplicationConfiguration(480, 320);
		return cfg;
	}

	@Override
	public ApplicationListener getApplicationListener () {
		return new TheGame();
	}
}