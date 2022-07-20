package dev.projectearth.genoa_plugin.commands;

import java.util.Optional;
import java.util.UUID;

import com.nukkitx.protocol.bedrock.data.command.CommandParamType;
import dev.projectearth.genoa_plugin.GenoaPlugin;
import org.cloudburstmc.server.command.Command;
import org.cloudburstmc.server.command.CommandSender;
import org.cloudburstmc.server.command.data.CommandData;
import org.cloudburstmc.server.command.data.CommandParameter;
import org.cloudburstmc.server.entity.Entity;
import org.cloudburstmc.server.entity.EntityType;
import org.cloudburstmc.server.level.Location;
import org.cloudburstmc.server.player.IPlayer;
import org.cloudburstmc.server.player.Player;
import org.cloudburstmc.server.registry.EntityRegistry;
import org.cloudburstmc.server.utils.Identifier;

/**
 * A basic test command for spawning entities.
 *
 * @author rtm516
 */
public class SummonCommand extends Command {

    public SummonCommand() {
        super("summon", CommandData.builder("summon")
                .setDescription("Summon an entity")
                .setUsageMessage("/summon <entity> <player>")
                .setPermissions("genoa.command.summon")
                .setParameters(new CommandParameter[] {
                        new CommandParameter("id", CommandParamType.STRING, false),
                        new CommandParameter("player", CommandParamType.TARGET, false)
                })
                .build());
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }

        if (args.length == 0) {
            return false;
        }

        /*if (!(sender instanceof Player)) {
            return false;
        }*/

        EntityType<? extends Entity> entityType = EntityRegistry.get().getEntityType(Identifier.fromString(args[0]));

        Optional<UUID> uuid = sender.getServer().lookupName(args[1]);

        if (uuid.isPresent()) {
            IPlayer player = sender.getServer().getOfflinePlayer(uuid.get());

            if (entityType == null) {
                return false;
            }

            if (player instanceof Player) {
                Player actualPlayer = (Player) player;
                Location playerLocation = actualPlayer.getLocation();

                Location pos = playerLocation;

                Entity ent = EntityRegistry.get().newEntity(entityType, pos);
                GenoaPlugin.get().getLogger().info("Spawning " + ent.getName() + " at " + pos.getPosition());
                ent.setPosition(pos.getPosition());
                ent.spawnToAll();

                return true;
            }
        }
        return false;
    }
}
