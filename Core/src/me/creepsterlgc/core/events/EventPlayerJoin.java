package me.creepsterlgc.core.events;

import me.creepsterlgc.core.customized.CoreDatabase;
import me.creepsterlgc.core.customized.CorePlayer;
import me.creepsterlgc.core.customized.CoreServer;
import me.creepsterlgc.core.customized.CoreSpawn;
import me.creepsterlgc.core.files.FileMessages;
import me.creepsterlgc.core.files.FileMotd;
import me.creepsterlgc.core.utils.TextUtils;

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.World;

import com.flowpowered.math.vector.Vector3d;


public class EventPlayerJoin {
	
	@Listener
    public void onPlayerJoin(ClientConnectionEvent.Join event) {
    	
    	Player player = event.getTargetEntity();
    	
		String uuid = player.getUniqueId().toString();
		String name = player.getName().toLowerCase();
		
		CorePlayer player_uuid = CoreDatabase.getPlayer(CoreDatabase.getUUID(name));
		CorePlayer player_name = CoreDatabase.getPlayer(uuid);
		
    	if(FileMessages.EVENTS_JOIN_ENABLE()) {
    		event.setMessage(TextUtils.color(FileMessages.EVENTS_JOIN_MESSAGE().replaceAll("%player", event.getTargetEntity().getName())));
    	}
    	
		CorePlayer pl = CoreDatabase.getPlayer(event.getTargetEntity().getUniqueId().toString());
		if(pl != null) {
			pl.setLastaction(System.currentTimeMillis());
			CoreDatabase.addPlayer(pl.getUUID(), pl);
		}
		
		if(player_uuid == null && player_name == null) {
			
			CorePlayer p = new CorePlayer(uuid, name, "", "", 0, 0, 0, 0, 0, 0, "", "", "", System.currentTimeMillis(), System.currentTimeMillis());
			p.setLastaction(System.currentTimeMillis());
			p.insert();
			
	    	if(FileMessages.EVENTS_FIRSTJOIN_ENABLE()) {
	    		event.setMessage(TextUtils.color(FileMessages.EVENTS_FIRSTJOIN_MESSAGE().replaceAll("%player", event.getTargetEntity().getName())));
	    		if(FileMessages.EVENTS_FIRSTJOIN_UNIQUEPLAYERS_SHOW()) {
		    		event.setMessage(Texts.of(TextUtils.color(FileMessages.EVENTS_FIRSTJOIN_MESSAGE().replaceAll("%player", player.getName())), "\n", TextUtils.color(FileMessages.EVENTS_FIRSTJOIN_UNIQUEPLAYERS_MESSAGE().replaceAll("%players", String.valueOf(CoreDatabase.getPlayers().size())))));
	    		}
	    	}
			
			CoreSpawn spawn = CoreDatabase.getSpawn("default");
			if(spawn != null) {
				
				if(event.getGame().getServer().getWorld(spawn.getWorld()).isPresent()) {
					Transform<World> t = event.getToTransform();
					t.setExtent(event.getGame().getServer().getWorld(spawn.getWorld()).get());
					t.setPosition(new Vector3d(spawn.getX(), spawn.getY(), spawn.getZ()));
					event.setToTransform(t);				
				}
				
			}
			
		}
		else if(player_uuid == null && player_name != null) {
			
			CoreDatabase.removePlayer(player_name.getUUID());
			CoreDatabase.removeUUID(player_name.getName());
			
			player_name.setName(player_name.getUUID());
			player_name.update();
			
			CorePlayer p = new CorePlayer(uuid, name, "", "", 0, 0, 0, 0, 0, 0, "", "", "", System.currentTimeMillis(), System.currentTimeMillis());
			p.setLastaction(System.currentTimeMillis());
			p.insert();
			
	    	if(FileMessages.EVENTS_FIRSTJOIN_ENABLE()) {
	    		event.setMessage(TextUtils.color(FileMessages.EVENTS_FIRSTJOIN_MESSAGE().replaceAll("%player", event.getTargetEntity().getName())));
	    		if(FileMessages.EVENTS_FIRSTJOIN_UNIQUEPLAYERS_SHOW()) {
		    		event.setMessage(Texts.of(TextUtils.color(FileMessages.EVENTS_FIRSTJOIN_MESSAGE().replaceAll("%player", player.getName())), "\n", TextUtils.color(FileMessages.EVENTS_FIRSTJOIN_UNIQUEPLAYERS_MESSAGE().replaceAll("%players", String.valueOf(CoreDatabase.getPlayers().size())))));
	    		}
	    	}
			
			CoreSpawn spawn = CoreDatabase.getSpawn("default");
			if(spawn != null) {
				
				if(event.getGame().getServer().getWorld(spawn.getWorld()).isPresent()) {
					Transform<World> t = event.getToTransform();
					t.setExtent(event.getGame().getServer().getWorld(spawn.getWorld()).get());
					t.setPosition(new Vector3d(spawn.getX(), spawn.getY(), spawn.getZ()));
					event.setToTransform(t);				
				}
				
			}
			
		}
		else if(player_uuid != null && player_name == null) {
			
			CoreDatabase.removePlayer(player_uuid.getUUID());
			CoreDatabase.removeUUID(player_uuid.getName());
			
			player_uuid.setName(name);
			player_uuid.setLastaction(System.currentTimeMillis());
			player_uuid.update();
			
			CoreServer.broadcast(Texts.of(TextColors.GOLD, player_uuid.getName(), " is now known as ", player.getName(), "!"));
			
		}
		else {
			
		}
		
		if(FileMotd.SHOW_ON_JOIN()) {
			for(String s : FileMotd.MESSAGE()) {
				s = s.replaceAll("%player", player.getName());
				player.sendMessage(TextUtils.color(s));
			}
		}
		
    }

}
