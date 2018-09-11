package xyz.biscut.chunkbuster.hooks;


import com.massivecraft.factions.*;
import com.massivecraft.factions.struct.Role;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class FactionsUUIDHook {

    // This hook works with FactionsUUID but it should also work with
    // its forks like SavageFactions.

    public boolean hasFaction(Player p) {
        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(p);
        return fPlayer.hasFaction();
    }

    public boolean isWilderness(Location loc) {
        FLocation fLoc = new FLocation(loc);
        Faction fLocFaction = Board.getInstance().getFactionAt(fLoc);
        return fLocFaction.isWilderness();
    }

    public boolean compareLocPlayerFaction(Location loc, Player p) {
        Faction locFaction = Board.getInstance().getFactionAt(new FLocation(loc));
        Faction pFaction = FPlayers.getInstance().getByPlayer(p).getFaction();
        return locFaction.equals(pFaction);
    }

    public boolean checkRole(Player p, String role) {
        Role playerRole = FPlayers.getInstance().getByPlayer(p).getRole();
        switch (role) {
            case "leader": case "admin":
                if (playerRole.equals(Role.ADMIN)) {
                    return true;
                }
                break;
            case "coleader":
                if (playerRole.equals(Role.COLEADER) || playerRole.equals(Role.ADMIN)) {
                    return true;
                }
                break;
            case "moderator":
                if (playerRole.equals(Role.MODERATOR) || playerRole.equals(Role.COLEADER) || playerRole.equals(Role.ADMIN)) {
                    return true;
                }
                break;
            case "member": case "normal":
                if (playerRole.equals(Role.NORMAL) || playerRole.equals(Role.MODERATOR) || playerRole.equals(Role.COLEADER) || playerRole.equals(Role.ADMIN)) {
                    return true;
                }
                break;
            case "recruit": case "any":
                return true;
        }
        return false;
    }
}
