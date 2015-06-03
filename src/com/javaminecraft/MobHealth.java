package com.javaminecraft;

import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class MobHealth extends JavaPlugin implements Listener {

    public static final Logger log = Logger.getLogger("Minecraft");

    private boolean display = false;
    private static int SCALE = 20;
    Player me;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        me = (Player) sender;
        if (sender instanceof Player) {
            if (arguments.length > 0) {
                if (label.equalsIgnoreCase("mobhealth")) {

                    if (arguments[0].equals("on")) {
                        me.sendMessage("[MobHealth] on");
                        display = true;

                    }

                    if (arguments[0].equals("off")) {
                        me.sendMessage("[MobHealth] off");
                        display = false;
                    }
                }
                return true;

            }
        }
        return false;
    }

    @Override
    public void onEnable() {
        Server server = getServer();
        PluginManager manager = server.getPluginManager();
        manager.registerEvents(this, this);
    }

    //display mob's current health
    public void showHealth(LivingEntity entity) {
        if (!display) {
            return;
        }

        int maxHealth = (int) entity.getMaxHealth();
        int currentHealth = (int) entity.getHealth();
        String text = makeBarGraph(currentHealth, maxHealth);
        entity.setCustomName(text);
    }

    //make the health bar
    public String makeBarGraph(int x, int y) {
        int percent = (int) ((x / (float) y) * SCALE);
        StringBuilder output = new StringBuilder(12 + SCALE);
        output.append(ChatColor.GREEN);
        if (percent > 0) {
            // show bars in graph
            for (int i = 0; i < (percent); i++) {
                output.append("O");
            }
        }

        output.append(ChatColor.RED);

        if (percent < SCALE) {
            //show red bars (eg health taken)
            for (int i = 0; i < (SCALE - percent); i++) {
                output.append("|");
            }
        }
        return output.toString();

    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity) event.getEntity();
            showHealth(entity);
        }
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent event) {
        if (event.getEntity() instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity) event.getEntity();
            showHealth(entity);
        }
    }
}
