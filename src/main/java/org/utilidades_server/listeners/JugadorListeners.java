package org.utilidades_server.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.utilidades_server.UtilidadesServer;
import org.utilidades_server.commands.Comandos;
import org.utilidades_server.utils.Utilidades;

public class JugadorListeners implements Listener {
    private final Comandos comandos;
    private UtilidadesServer utilidadesServer;

    public JugadorListeners(Comandos comandos, UtilidadesServer utilidadesServer) {
        this.comandos = comandos;
        this.utilidadesServer = utilidadesServer;
    }

    @EventHandler
    public void onJugadorDesconectado(PlayerQuitEvent event){
        Player jugador = event.getPlayer();

        if(jugador.getWorld().getName().equals("Lobby")){
            jugador.getInventory().clear();
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player jugador = event.getPlayer();
        if(Comandos.getTiempoVueloGlobal() > 0){
            Utilidades.envitarTitutloBarJugador(jugador, ChatColor.GOLD + "Comandos exclusivos: " + ChatColor.WHITE + Comandos.getTiempoVueloGlobal());
        }
    }

    /*@EventHandler
    public void onFly(PlayerToggleFlightEvent event){
        if(event.getPlayer().getWorld().getName().equals("PartyGames") && event.isFlying() && !event.getPlayer().isOp()){
            event.setCancelled(true);
        }
    }*/
}
