package backend;

public enum UserStatus {
    NOT_EXISTING,
    NOT_ONLINE,
    NOT_TALKING,
    WAITING,
    PVP_TALKING,
    PVE_TALKING;

    boolean isTalking() {
        return this == PVP_TALKING || this == PVE_TALKING;
    }

    boolean isWaiting() {
        return this == WAITING;
    }
}
