package com.gmail.yaroslavlancelot.eafall.game.resources;

import com.gmail.yaroslavlancelot.eafall.game.resources.loaders.ClientResourcesLoader;

/** return game resources loader instance */
public class ResourceFactory {
    public ResourceFactory() {
    }

    public IResourcesLoader getLoader() {
        return new ClientResourcesLoader();
    }
}
