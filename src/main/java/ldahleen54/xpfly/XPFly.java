package ldahleen54.xpfly;

import net.kyori.adventure.util.TriState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class XPFly extends JavaPlugin {

    private final Set<UUID> flyingPlayers = new HashSet<>();

    @Override
    public void onEnable() {
        getLogger().info("xpfly is enabled!");

        // Schedule XP draining task every 2 seconds
        Bukkit.getScheduler().runTaskTimer(this, this::drainXpFromFlyingPlayers, 0L, 40L);

        // Register command using Paper's lifecycle
        /*this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            event.registrar().register(createXPFlyCommand());
        });*/
    }

/*    private LiteralCommandNode<CommandSourceStack> createXPFlyCommand() {
        return Commands.literal("xpfly")
                .requires(source -> true)
                .executes(ctx -> {
                    CommandSender sender = ctx.getSource().getSender();

                    if (!(sender instanceof Player player)) {
                        sender.sendMessage("Only players can use this command.");
                        return 1;
                    }

                    UUID uuid = player.getUniqueId();

                    if (player.getAllowFlight()) {
                        player.setFlying(false);
                        player.setAllowFlight(false);
                        flyingPlayers.remove(uuid);
                        player.sendMessage("XP flight disabled.");
                    } else {
                        if (player.getTotalExperience() < 1) {
                            player.sendMessage("You need XP to enable flight");
                            return 1;
                        }
                        player.setAllowFlight(true);
                        flyingPlayers.add(uuid);
                        player.sendMessage("XP flight enabled. 1 XP will be drained every 2 seconds.");
                    }

                    return 1;
                })
                .build();
    }*/

    private void drainXpFromFlyingPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            int xp = player.getTotalExperience();

            if (xp > 0) {
                player.setFlyingFallDamage(TriState.TRUE);
                if (!player.getAllowFlight()) {
                    player.sendMessage("You can now fly by double tapping the spacebar. When flying xp will be drained over time.");
                    player.sendMessage("When you run out of xp you will drop to the ground and possibly die.");
                    player.setAllowFlight(true);
                }
            } else {
                player.setAllowFlight(false);
                if (player.isFlying()) {
                    player.setFlying(false);
                    player.sendMessage("You've run out of XP. Flight disabled.");
                }
            }
            if (player.isFlying()) {
                player.giveExp(-4);
            }
        }
    }
}