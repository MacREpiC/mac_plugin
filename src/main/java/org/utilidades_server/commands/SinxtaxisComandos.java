package org.utilidades_server.commands;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.utilidades_server.UtilidadesServer;
import org.utilidades_server.utils.Utilidades;

public class SinxtaxisComandos {
    private static final String BARRA = "---------------------------------";
    public static void sComandosAll(@NotNull CommandSender sender) {
        sender.sendMessage(Utilidades.getMensajeError(UtilidadesServer.prefix_plugin, "Error de sintaxis."));
        sender.sendMessage(BARRA);
        sender.sendMessage(Utilidades.getMensajeColor("Sintaxis: /comandos_all <tiempo> <comando> <solicitante>"));
        sender.sendMessage(Utilidades.getMensajeColor("Sintaxis: /comandos_all 3600 fly ADMINISTRACIÓN"));
        sender.sendMessage(Utilidades.getMensajeColor("&o&eEl tiempo tiene que ser en segundos."));
        sender.sendMessage(Utilidades.getMensajeColor("&efly"));
        sender.sendMessage(Utilidades.getMensajeColor("&ejump"));
        sender.sendMessage(Utilidades.getMensajeColor("&eback"));
        sender.sendMessage(Utilidades.getMensajeColor("&etop"));
        sender.sendMessage(Utilidades.getMensajeColor("&etodos"));
        sender.sendMessage(BARRA);
    }
}
