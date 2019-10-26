package codes.biscuit.chunkbuster.hooks;


import codes.biscuit.chunkbuster.ChunkBuster;
import com.massivecraft.factions.*;
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
}
