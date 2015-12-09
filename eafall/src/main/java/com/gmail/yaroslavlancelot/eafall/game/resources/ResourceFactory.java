package com.gmail.yaroslavlancelot.eafall.game.resources;

import com.gmail.yaroslavlancelot.eafall.game.resources.loaders.campaign.CampaignResourceLoader;
import com.gmail.yaroslavlancelot.eafall.game.resources.loaders.game.ClientResourcesLoader;
import com.gmail.yaroslavlancelot.eafall.game.sandbox.resources.SandboxResourcesLoader;

/** return game resources loader instance */
public class ResourceFactory {
    public static final String RESOURCE_LOADER = "resource_loader";

    public ResourceFactory() {
    }

    public IResourcesLoader getLoader(TypeResourceLoader typeResourceLoader) {
        if (typeResourceLoader == TypeResourceLoader.CAMPAIGN) {
            return new CampaignResourceLoader();
        } else if (typeResourceLoader == TypeResourceLoader.SANDBOX) {
            return new SandboxResourcesLoader();
        }
        return new ClientResourcesLoader();
    }

    public enum TypeResourceLoader {
        CLIENT, CAMPAIGN, SANDBOX
    }
}
