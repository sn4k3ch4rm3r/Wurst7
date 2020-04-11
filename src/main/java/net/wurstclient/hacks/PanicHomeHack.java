package net.wurstclient.hacks;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.wurstclient.events.UpdateListener;
import net.wurstclient.hack.Hack;

public class PanicHomeHack extends Hack implements UpdateListener {

	private int timer;

	public PanicHomeHack() {
		super("PanicHome", "Use your home hotkey when a player that is not on your friend list is detected.");
	}

	@Override
	public void onEnable() {
		EVENTS.add(UpdateListener.class, this);
		timer = 0;
	}

	@Override
	public void onDisable() {
		EVENTS.remove(UpdateListener.class, this);
	}

	@Override
	public void onUpdate() {
		if (timer > 0) timer--;

		Stream<LivingEntity> stream = StreamSupport
			.stream(MC.world.getEntities().spliterator(), true)
			.filter(e -> e instanceof LivingEntity).map(e -> (LivingEntity)e)
			.filter(e -> e instanceof PlayerEntity)
			.filter(e -> e != MC.player)
			.filter(e -> !WURST.getFriends().contains(e.getEntityName()));

		if(stream.count() > 0 && timer == 0){
			WURST.getCmdProcessor()
				.process(WURST.getKeybinds()
				.getCommands("key.keyboard.h"));
			timer = 50;
		}
	}
}