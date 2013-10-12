package com.gmail.yaroslavlancelot.spaceinvaders.ai;

import com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam;

public class SimpleBot implements Runnable {
    public static final int DELAY_BETWEEN_ITERATIONS = 20;
    private final ITeam mBotTeam;

    public SimpleBot(ITeam botTeam) {
        mBotTeam = botTeam;
    }

    @Override
    public void run() {
        while (true) {
            if (mBotTeam.getMoney() >= 100)
                mBotTeam.getTeamPlanet().buildFirstBuilding();
            try {
                Thread.sleep(DELAY_BETWEEN_ITERATIONS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
