package codes.biscuit.chunkbuster.hooks;


import codes.biscuit.chunkbuster.ChunkBuster;
import com.massivecraft.factions.*;
import com.massivecraft.factions.struct.Role;
import org.bukkit.Location;
import org.bukkit.entity.Player;

class FactionsUUIDHook {

    // This hook works with FactionsUUID but it should also work with
    // its forks like SavageFactions.

    private ChunkBuster main;

    FactionsUUIDHook(ChunkBuster main) {
        this.main = main;
    }

    boolean hasFaction(Player p) {
        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(p);
        return fPlayer.hasFaction();
    }

    boolean isWilderness(Location loc) {
        FLocation fLoc = new FLocation(loc);
        Faction fLocFaction = Board.getInstance().getFactionAt(fLoc);
        return fLocFaction.isWilderness();
    }

    boolean compareLocPlayerFaction(Location loc, Player p) {
        Faction locFaction = Board.getInstance().getFactionAt(new FLocation(loc));
        Faction pFaction = FPlayers.getInstance().getByPlayer(p).getFaction();
        return locFaction.equals(pFaction) || (locFaction.isWilderness() && main.getConfigValues().canPlaceInWilderness());
    }

    boolean checkRole(Player p, String role) {
        Role playerRole = FPlayers.getInstance().getByPlayer(p).getRole();
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
        switch (role) {
            case "leader": case "admin":
                if (playerRole.equals(adminRole)) {
                    return true;
                }
                break;
            case "coleader": case "co-leader":
                if (playerRole.equals(Role.COLEADER) || playerRole.equals(adminRole)) {
                    return true;
                }
                break;
            case "moderator":
                if (playerRole.equals(Role.MODERATOR) || playerRole.equals(Role.COLEADER) || playerRole.equals(adminRole)) {
                    return true;
                }
                break;
            case "member": case "normal":
                if (playerRole.equals(Role.NORMAL) || playerRole.equals(Role.MODERATOR) || playerRole.equals(Role.COLEADER) || playerRole.equals(adminRole)) {
                    return true;
                }
                break;
            case "recruit": case "any":
                return true;
        }
        return false;
    }
}
