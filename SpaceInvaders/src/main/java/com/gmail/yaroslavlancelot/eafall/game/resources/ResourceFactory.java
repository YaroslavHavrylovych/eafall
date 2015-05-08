package com.gmail.yaroslavlancelot.eafall.game.resources;

import com.gmail.yaroslavlancelot.eafall.game.resources.loaders.CampaignResourceLoader;
import com.gmail.yaroslavlancelot.eafall.game.resources.loaders.ClientResourcesLoader;

/** return game resources loader instance */
public class ResourceFactory {
    public static final String RESOURCE_LOADER = "resource_loader";

    public ResourceFactory() {
    }

    public IResourcesLoader getLoader(TypeResourceLoader typeResourceLoader) {
        if (typeResourceLoader == TypeResourceLoader.CAMPAIGN) {
            return new CampaignResourceLoader();
        }
        return new ClientResourcesLoader();
    }

    public enum TypeResourceLoader {
        CLIENT, CAMPAIGN
    }
}
