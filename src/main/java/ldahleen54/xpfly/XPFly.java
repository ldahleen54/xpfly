package ldahleen54.xpfly;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
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
            UUID uuid = player.getUniqueId();
            int xp = player.getTotalExperience();

            if (xp > 0) {
                if (!player.getAllowFlight()) {
                    player.sendMessage("You can now fly by double tapping the spacebar.");
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