package xyz.biscut.chunkbuster.hooks;


import com.massivecraft.factions.*;
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
}
