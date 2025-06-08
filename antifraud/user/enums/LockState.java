package antifraud.user.enums;

public enum LockState {
    LOCKED(true),
    UNLOCKED(false);

    private boolean locked;
    private LockState(boolean locked) {
        this.locked = locked;
    }

    public static LockState is(boolean locked) {
        return locked ? LOCKED : UNLOCKED;
    }

    public boolean isLocked() {
        return locked;
    }
}
