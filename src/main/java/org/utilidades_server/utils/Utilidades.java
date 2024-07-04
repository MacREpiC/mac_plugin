package org.utilidades_server.utils;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Utilidades {
    public static String getMensajeError(String anunciante, String mensaje) {
        return ChatColor.translateAlternateColorCodes('&', anunciante + " &c" + mensaje);
    }

    public static String getMensajeColor(String mensaje) {
        return ChatColor.translateAlternateColorCodes('&', mensaje);
    }

    public static TextComponent crearComponenteTexto(String text, ChatColor color, String hoverText, String command) {
        TextComponent component = new TextComponent(text);
        component.setColor(color.asBungee());
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(hoverText)));
        component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        return component;
    }

    public static void mandarMensajeTodos(String anunciante, String mensaje) {
        String anuncianteFormateado = ChatColor.translateAlternateColorCodes('&', anunciante);
        String mensajeFormateado = ChatColor.translateAlternateColorCodes('&', mensaje);
        for (Player jugador : Bukkit.getOnlinePlayers()) {
            jugador.sendMessage(anuncianteFormateado, mensajeFormateado);
        }
    }

    public static void enviarTituloATodosPantalla(String titulo, String subtitulo) {
        for (Player jugador : Bukkit.getOnlinePlayers()) {
            jugador.sendTitle(titulo, subtitulo, 10, 70, 20); // 10 ticks fade in, 70 ticks stay, 20 ticks fade out
        }
    }

    public static void envitarTitutloTodosBar(String mensaje){
        for (Player jugador : Bukkit.getOnlinePlayers()) {
            jugador.spigot().sendMessage(ChatMessageType.ACTION_BAR, new net.md_5.bungee.api.chat.TextComponent(mensaje));
        }
    }

    public static void envitarTitutloBarJugador(Player jugador, String mensaje) {
        jugador.spigot().sendMessage(ChatMessageType.ACTION_BAR, new net.md_5.bungee.api.chat.TextComponent(mensaje));
    }
}
