package me.creepsterlgc.core.events;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;

import me.creepsterlgc.core.customized.DATABASE;
import me.creepsterlgc.core.customized.MESSAGES;
import me.creepsterlgc.core.customized.PLAYER;
import me.creepsterlgc.core.customized.SERIALIZE;
import me.creepsterlgc.core.customized.TEXT;


public class EventPlayerQuit {

	@Listener
    public void onPlayerQuit(ClientConnectionEvent.Disconnect event) {
		
    	if(MESSAGES.EVENTS_LEAVE_ENABLE()) {
    		event.setMessage(TEXT.color(MESSAGES.EVENTS_LEAVE_MESSAGE().replaceAll("%player", event.getTargetEntity().getName())));
    	}
    	
    	Player player = event.getTargetEntity();
    	PLAYER p = DATABASE.getPlayer(player.getUniqueId().toString());
    	
    	String world = player.getWorld().getName();
    	double x = player.getLocation().getX();
    	double y = player.getLocation().getY();
    	double z = player.getLocation().getZ();
    	double yaw = 0;
    	double pitch = 0;
    	String location = SERIALIZE.location(world, x, y, z, yaw, pitch);
    	
    	p.setLastlocation(location);
    	p.setLastseen(System.currentTimeMillis());
    	p.update();
    	
    }
	
}
