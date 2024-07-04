package org.utilidades_server;

import com.sun.tools.javac.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.utilidades_server.commands.Comandos;
import org.utilidades_server.config.MainConfigManager;
import org.utilidades_server.listeners.JugadorListeners;

public class UtilidadesServer extends JavaPlugin {

    public static String prefix_plugin = "&8[&c&lMAC_PLUGIN&8] ";
    public static String prefix_server = "&x&F&F&0&0&0&0&l[&x&F&F&2&E&0&0&lM&x&F&F&5&D&0&0&li&x&F&F&8&B&0&0&ln&x&F&F&B&9&0&0&le&x&F&F&E&8&0&0&lI&x&F&F&E&8&0&0&lb&x&F&F&B&9&0&0&le&x&F&F&8&B&0&0&lr&x&F&F&5&D&0&0&li&x&F&F&2&E&0&0&la&x&F&F&0&0&0&0&l]&r";
    private String version = getDescription().getVersion();
    private MainConfigManager mainConfigManager;
    private static UtilidadesServer instancia;

    @Override
    public void onEnable() {
        instancia = this;
        registerCommands();
        registerEvents();
        mainConfigManager = new MainConfigManager(this);
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&fEl plugin: " + prefix_plugin + " &eha sido activado! &bVersión: " + version));
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&fEl plugin: " + prefix_plugin + "&rha sido desactivado."));
    }

    public static UtilidadesServer getInstance() {
        return instancia;
    }

    public void registerCommands() {
        this.getCommand("lobby").setExecutor(new Comandos(this));
        this.getCommand("menu").setExecutor(new Comandos(this));
        this.getCommand("chg").setExecutor(new Comandos(this));
        this.getCommand("chc").setExecutor(new Comandos(this));
        this.getCommand("cha").setExecutor(new Comandos(this));
        this.getCommand("che").setExecutor(new Comandos(this));
        this.getCommand("chm").setExecutor(new Comandos(this));
        this.getCommand("chs").setExecutor(new Comandos(this));
        this.getCommand("comandos_all").setExecutor(new Comandos(this));
        this.getCommand("walk_all").setExecutor(new Comandos(this));
        this.getCommand("resetear_mundos").setExecutor(new Comandos(this));
        this.getCommand("tiempo").setExecutor(new Comandos(this));
        this.getCommand("cargar_spawn_recursos").setExecutor(new Comandos(this));
    }

    public void registerEvents() {
        getServer().getPluginManager().registerEvents(new JugadorListeners(new Comandos(this), this), this);
    }

    public MainConfigManager getMainConfigManager() {
        return mainConfigManager;
    }
}
