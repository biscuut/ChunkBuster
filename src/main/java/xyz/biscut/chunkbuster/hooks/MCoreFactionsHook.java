package xyz.biscut.chunkbuster.hooks;

import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.ps.PS;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class MCoreFactionsHook {

    // This hook works with MassiveCore Factions only.

    public boolean hasFaction(Player p) {
        MPlayer mPlayer = MPlayer.get(p);
        return mPlayer.hasFaction();
    }

    public boolean isWilderness(Location loc) {
        Faction faction = BoardColl.get().getFactionAt(PS.valueOf(loc));
        return faction.isNone();
    }

    public boolean compareLocPlayerFaction(Location loc, Player p) {
        Faction locFaction = BoardColl.get().getFactionAt(PS.valueOf(loc));
        Faction pFaction = MPlayer.get(p).getFaction();
        return locFaction.equals(pFaction);
    }
}
