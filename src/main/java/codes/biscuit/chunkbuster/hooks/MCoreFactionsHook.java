package codes.biscuit.chunkbuster.hooks;

import codes.biscuit.chunkbuster.ChunkBuster;
import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.ps.PS;
import org.bukkit.Location;
import org.bukkit.entity.Player;

class MCoreFactionsHook {

    // This hook works with MassiveCore Factions only.
    private ChunkBuster main;

    MCoreFactionsHook(ChunkBuster main) {
        this.main = main;
    }

    boolean hasFaction(Player p) {
        return MPlayer.get(p).hasFaction();
    }

    boolean isWilderness(Location loc) {
        return BoardColl.get().getFactionAt(PS.valueOf(loc)).isNone();
    }

    boolean compareLocPlayerFaction(Location loc, Player p) {
        Faction locFaction = BoardColl.get().getFactionAt(PS.valueOf(loc));
        Faction pFaction = MPlayer.get(p).getFaction();
        return locFaction.equals(pFaction) || (locFaction.isNone() && main.getConfigValues().canPlaceInWilderness());
    }

    boolean checkRole(Player p, String role) {
        Rel playerRole = MPlayer.get(p).getRole();
        switch (role) {
            case "leader": case "admin":
                if (playerRole.equals(Rel.LEADER)) {
                    return true;
                }
                break;
            case "officer":
                if (playerRole.equals(Rel.OFFICER) || playerRole.equals(Rel.LEADER)) {
                    return true;
                }
                break;
            case "member": case "normal":
                if (playerRole.equals(Rel.MEMBER) || playerRole.equals(Rel.OFFICER) || playerRole.equals(Rel.LEADER)) {
                    return true;
                }
                break;
            case "recruit": case "any":
                return true;
        }
        return false;
    }
}
