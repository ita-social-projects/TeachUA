package com.softserve.teachua.utils.viberbot.enums;

import java.util.Arrays;

public enum CallbackType {
    DELIVERED {
        @Override
        public String toString() {
            return "delivered";
        }
    },
    SEEN {
        @Override
        public String toString() {
            return "seen";
        }
    },
    FAILED {
        @Override
        public String toString() {
            return "failed";
        }
    },
    SUBSCRIBED {
        @Override
        public String toString() {
            return "subscribed";
        }
    },
    UNSUBSCRIBED {
        @Override
        public String toString() {
            return "unsubscribed";
        }
    },
    CONVERSATION_STARTED {
        @Override
        public String toString() {
            return "conversation_started";
        }
    },
    MESSAGE {
        @Override
        public String toString() {
            return "message";
        }
    },
    NONE;

    public static CallbackType of(String eventName) {
        return Arrays.stream(values())
                .filter(value -> value.name().equalsIgnoreCase(eventName))
                .findFirst().orElse(NONE);
    }
}

