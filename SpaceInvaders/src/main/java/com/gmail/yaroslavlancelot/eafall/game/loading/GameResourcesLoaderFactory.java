package com.gmail.yaroslavlancelot.eafall.game.loading;

/** return game resources loader instance */
public class GameResourcesLoaderFactory {
    public GameResourcesLoaderFactory() {
    }

    public GameResourceLoader getLoader() {
        return new GameResourcesLoaderImpl();
    }
}
