package ca.marioux.easybounty.domain;

import org.bukkit.event.player.PlayerItemDamageEvent;

import java.util.UUID;

public class PlayerId {
    private UUID uuid;

    public PlayerId(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PlayerId)) {
            return false;
        }

        PlayerId other = (PlayerId) obj;
        return uuid.equals(other.getUuid());
    }

    @Override
    public String toString() {
        return uuid.toString();
    }

    public static PlayerId fromString(String value) {
        return new PlayerId(UUID.fromString(value));
    }
}
