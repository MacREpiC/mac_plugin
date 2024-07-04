package org.utilidades_server.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.utilidades_server.UtilidadesServer;

public class MainConfigManager {
    private CustomConfig configFile;
    private UtilidadesServer plugin;
    private String msjPermisos;
    private String serverPrefix;
    private String pluginPrefix;
    private boolean guardarTiempoVuelo;

    public MainConfigManager(UtilidadesServer plugin){
        this.plugin = plugin;

        configFile = new CustomConfig("config.yml", null, plugin, true);
        configFile.registerConfig();
        loadConfig();
    }

    public void loadConfig(){
        FileConfiguration config = configFile.getConfig(); // Obtener el archivo de configuración
        msjPermisos = config.getString("mensajes.permiso_denegado");
        serverPrefix = config.getString("prefijos.server_prefix");
        pluginPrefix = config.getString("prefijos.plugin_prefix");
        guardarTiempoVuelo = config.getBoolean("config.guardar_tiempo_vuelo.enabled");
    }

    public void reloadConfig(){
        configFile.reloadConfig();
        loadConfig();
    }

    public CustomConfig getConfigFile() {
        return configFile;
    }

    public UtilidadesServer getPlugin() {
        return plugin;
    }

    public String getMsjPermisos() {
        return msjPermisos;
    }

    public String getServerPrefix() {
        return serverPrefix;
    }

    public String getPluginPrefix() {
        return pluginPrefix;
    }

    public boolean isGuardarTiempoVuelo() {
        return guardarTiempoVuelo;
    }
}
