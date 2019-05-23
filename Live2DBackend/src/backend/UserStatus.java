package backend;

public enum UserStatus {
    NOT_EXISTING("NOT_EXISTING"),
    NOT_ONLINE("NOT_ONLINE"),
    NOT_TALKING("NOT_TALKING"),
    WAITING("WAITING"),
    PVP_TALKING("PVP_TALKING"),
    PVE_TALKING("PVE_TALKING");

    private String typename;

    private UserStatus(String typename) {  this.typename = typename; }

    boolean isTalking() {
        return this == PVP_TALKING || this == PVE_TALKING;
    }

    boolean isWaiting() {
        return this == WAITING;
    }

    public String getTypeName() { return this.typename; }
    public static UserStatus fromTypeName(String typeName) {
        for (UserStatus status : UserStatus.values()) {
            if (status.getTypeName().equals(typeName))
                return status;
        }
        return null;
    }
}
