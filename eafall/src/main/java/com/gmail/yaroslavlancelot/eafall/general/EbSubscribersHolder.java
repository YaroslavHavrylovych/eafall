package com.gmail.yaroslavlancelot.eafall.general;

import de.greenrobot.event.EventBus;

/**
 * EventBus doesn't have methods for the bulk subscribers unregistering.
 * It rather proxy to register classes as subscribers. Each class you registered with
 * {@link EbSubscribersHolder#register(Object)} will be unregistered automatically.
 *
 * @author Yaroslav Havrylovych
 */
public class EbSubscribersHolder extends Holder<Object> {
    // ===========================================================
    // Constants
    // ===========================================================
    /** current class instance (singleton realization) */
    private final static EbSubscribersHolder EB_SUBSCRIBERS_HOLDER = new EbSubscribersHolder();

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================
    public static EbSubscribersHolder getInstance() {
        return EB_SUBSCRIBERS_HOLDER;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================
    @Override
    public void clear() {
        for (Object subscriber : EB_SUBSCRIBERS_HOLDER.getElements()) {
            EventBus.getDefault().unregister(subscriber);
        }
        super.clear();
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    /**
     * register subscriber to {@link EventBus#register(Object)}. Keep track to
     * unsubscribe the subscriber as it unused.
     *
     * @param subscriber to event bus event
     */
    public static void register(Object subscriber) {
        EventBus.getDefault().register(subscriber);
        EB_SUBSCRIBERS_HOLDER.addElement(subscriber.toString(), subscriber);
    }


    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
