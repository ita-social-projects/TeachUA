package com.softserve.teachua.utils.viberbot.enums;

import java.util.HashMap;
import java.util.Map;

public enum Action {
    MAIN_MENU,
    CERTIFICATES,
    INFO,
    USER_INPUT,
    AUTH,
    BACK;
    private static final Map<String, Action> actionByName = new HashMap<>();

    static {
        for (Action action : Action.values()) {
            actionByName.put(action.name(), action);
        }
    }

    public static Action of(String nameOfAction) {
        return actionByName.getOrDefault(nameOfAction, USER_INPUT);
    }

}
