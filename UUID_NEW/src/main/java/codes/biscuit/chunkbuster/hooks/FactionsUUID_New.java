package codes.biscuit.chunkbuster.hooks;

import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.perms.Role;
import org.bukkit.entity.Player;

public class FactionsUUID_New implements IFactionsUUIDHook {

    // the role is a different import
    public boolean checkRole(Player p, String role) {
        Role playerRole = FPlayers.getInstance().getByPlayer(p).getRole();
        if (playerRole == null) {
            return false;
        }
        Role adminRole;
        try {
            adminRole = Role.valueOf("ADMIN");
        } catch (Exception ex) {
            try {
                adminRole = Role.valueOf("LEADER");
            } catch (Exception ex2) {
                return false;
            }
        }
        Enum coLeader = null;
        try {
            coLeader = Role.valueOf("COLEADER");
        } catch (Exception ignored) {}
        switch (role) {
            case "leader": case "admin":
                if (playerRole.equals(adminRole)) {
                    return true;
                }
                break;
            case "coleader": case "co-leader":
                if (playerRole.equals(coLeader) || playerRole.equals(adminRole)) {
                    return true;
                }
                break;
            case "moderator":
                if (playerRole.equals(Role.MODERATOR) || playerRole.equals(coLeader) || playerRole.equals(adminRole)) {
                    return true;
                }
                break;
            case "member": case "normal":
                if (playerRole.equals(Role.NORMAL) || playerRole.equals(Role.MODERATOR) || playerRole.equals(coLeader) || playerRole.equals(adminRole)) {
                    return true;
                }
                break;
            case "recruit": case "any":
                return true;
        }
        return false;
    }
}
