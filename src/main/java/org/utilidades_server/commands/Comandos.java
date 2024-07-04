package org.utilidades_server.commands;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sun.tools.javac.Main;
import net.advancedplugins.chat.api.AdvancedChatAPI;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.utilidades_server.UtilidadesServer;
import org.utilidades_server.utils.Utilidades;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class Comandos implements CommandExecutor {
    private UtilidadesServer plugin;
    private static final String NETHER = "world_nether";
    private static final String END = "world_the_end";
    private static final String RECURSOS = "Recursos";
    private static final String LOBBY = "Lobby";
    private static long timepoVueloGlobal;
    private static boolean resetearMundos = false;

    public static final AdvancedChatAPI ac_api = new AdvancedChatAPI();
    static MultiverseCore core = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
    static MVWorldManager worldManager = core.getMVWorldManager();
    private static LuckPerms luckPerms = LuckPermsProvider.get();

    public Comandos(UtilidadesServer plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String alias, String[] args) {
        Player jugador = (Player) sender;
        UUID playerUUID = jugador.getUniqueId();

        switch (command.getName().toLowerCase()) {
            case "lobby":
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv tp " + jugador.getName() + " Lobby");
                sender.sendMessage(Utilidades.getMensajeError(UtilidadesServer.prefix_plugin, "Teletransportando a &aLobby&r..."));
                break;

            case "menu":
                if (jugador.getWorld().getName().equals(LOBBY)) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "cp menu " + jugador.getName());
                } else {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "cp menu_otros " + jugador.getName());
                }
                break;

            case "comandos_all":
                if (sender.isOp()) {
                    if (args.length < 3 || !args[0].matches("\\d+")) {
                        SinxtaxisComandos.sComandosAll(sender);
                        return true;
                    } else {
                        int tiempo;
                        String comandoTemporal;
                        String sujeto;
                        try { // Comprobar que me están pasando un entero por parámetro
                            tiempo = Integer.parseInt(args[0]);
                        } catch (NumberFormatException e) {
                            SinxtaxisComandos.sComandosAll(sender);
                            return true;
                        }
                        if (tiempo <= 0) {
                            SinxtaxisComandos.sComandosAll(sender);
                            return true;
                        }

                        comandoTemporal = args[1];
                        sujeto = args[2];

                        switch (comandoTemporal) {
                            case "fly":
                                anadirTiempoVueloTodos(tiempo);
                                calcularTiempoAnuncioTipo(tiempo, "VUELO", sujeto);
                                fuegosArtificialesYsonidos();

                                break;
                            case "jump":
                                /*
                                Añadir el permiso JUMP al grupo de default pasándole por parámetro el tiempo que me pasan
                                por comando e importante, añadiendo la palabra "accumulate" para que si se
                                vuelve a utilizar el comando, que se acumule el tiempo.
                                */
                                comandoTemporal = "jump";
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp group default permission settemp essentials.jump true " + tiempo + "s accumulate");
                                calcularTiempoAnuncioTipo(tiempo, "JUMP", sujeto);
                                fuegosArtificialesYsonidos();

                                break;
                            case "back":
                                comandoTemporal = "back";
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp group default permission settemp essentials.back true " + tiempo + "s accumulate");
                                fuegosArtificialesYsonidos();

                                break;
                            case "top":
                                comandoTemporal = "top";
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp group default permission settemp essentials.top true " + tiempo + "s accumulate");
                                calcularTiempoAnuncioTipo(tiempo, "TOP", sujeto);
                                fuegosArtificialesYsonidos();

                                break;
                            case "todos":
                                comandoTemporal = "todos";
                                anadirTiempoVueloTodos(tiempo);
                                fuegosArtificialesYsonidos();
                                calcularTiempoAnuncioTipo(tiempo, "COMANDOS EXCLUSIVOS", sujeto);
                                break;
                            default:
                                sender.sendMessage(Utilidades.getMensajeError(UtilidadesServer.prefix_plugin, "No conozco ese comando..."));
                                SinxtaxisComandos.sComandosAll(sender);
                                return true;
                        }
                        if (comandoTemporal.equals("todos")) {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp group default permission settemp essentials.jump true " + tiempo + "s accumulate");
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp group default permission settemp essentials.back true " + tiempo + "s accumulate");
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp group default permission settemp essentials.fly true " + tiempo + "s accumulate");
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp group default permission settemp essentials.top true " + tiempo + "s accumulate");
                        } else {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp group default permission settemp essentials." + comandoTemporal + " true " + tiempo + "s accumulate");
                        }
                    }
                } else {
                    sender.sendMessage(Utilidades.getMensajeError(UtilidadesServer.prefix_plugin, plugin.getMainConfigManager().getMsjPermisos()));
                }
                break;

            case "walk_all":
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.setFlying(false);
                }
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp group default permission unsettemp essentials.fly");
                setTiempoVueloGlobal(0);
                break;

            case "resetear_mundos":
                if (!sender.isOp()) {
                    sender.sendMessage(Utilidades.getMensajeError(UtilidadesServer.prefix_plugin, plugin.getMainConfigManager().getMsjPermisos()));
                    return true;
                } else {
                    if (sender instanceof Player) {
                        // Marca que estamos esperando una confirmación por parte del usuario con las dos acciones se le ofrece
                        setResetearMundos(true);

                        sender.sendMessage("¿Estás seguro de que deseas restablecer los mundos? Esto eliminará todo el progreso y los cambios realizados en los mundos.");

                        // Esto creará una ventana al poner el cursor encima del botón y realizará la acciones que se le estoy diciendo a cada uno
                        /*TextComponent confirmar = Utilidades.crearComponenteTexto("[Confirmar]", ChatColor.GREEN, "Haz clic aquí para confirmar.", "/confirmar_resetear_mundos");
                        TextComponent cancelar = Utilidades.crearComponenteTexto(" [Cancelar]", ChatColor.RED, "Haz clic aquí para cancelar.", "/cancelar_resetear_mundos");

                        jugador.spigot().sendMessage(confirmar, cancelar);*/
                        resetearMundo(jugador);
                    } else {
                        sender.sendMessage(Utilidades.getMensajeColor(UtilidadesServer.prefix_plugin + "¡Este comando tan solo lo puede ejecutar un jugador!"));
                    }

                }
                break;

            case "tiempo":
                sender.sendMessage(String.format("El tiempo restante para el permisos es de: %s s", getTiempoRestantePermisoGrupo("default", "essentials.fly")));
                sender.sendMessage(String.format("El tiempo restante para el permisos en la variable global: %s s", getTiempoVueloGlobal()));
                break;

            case "cargar_spawn_recursos":
                if(sender.isOp()){
                    cargarSpawnRecursos();
                    sender.sendMessage(Utilidades.getMensajeColor(UtilidadesServer.prefix_plugin + "Se creado el spawn de recursos."));
                } else{
                    sender.sendMessage(Utilidades.getMensajeError(UtilidadesServer.prefix_plugin, plugin.getMainConfigManager().getMsjPermisos()));
                    return true;
                }
                break;

            case "chg":
                ac_api.joinChatChannel(playerUUID, "general");
                break;
            case "chc":
                ac_api.joinChatChannel(playerUUID, "comercio");
                break;
            case "cha":
                ac_api.joinChatChannel(playerUUID, "ayuda");
                break;
            case "che":
                ac_api.joinChatChannel(playerUUID, "eventos");
                break;
            case "chm":
                ac_api.joinChatChannel(playerUUID, "mundo");
                break;
            case "chs":
                ac_api.joinChatChannel(playerUUID, "staff");
                break;

            default:
                return false; // Si el comando no coincide con ninguno de los casos, devuelve false.
        }
        return true;
    }

    private static void anadirTiempoVueloTodos(int tiempo) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp group default permission settemp essentials.fly true " + tiempo + " accumulate");
        setTiempoVueloGlobal(getTiempoRestantePermisoGrupo("default", "essential.fly"));

        // Crear una tarea que se ejecutará cada 1 segundo para restarle uno a la variable global
        new BukkitRunnable() {
            @Override
            public void run() {
                long tiempoRestante = getTiempoRestantePermisoGrupo("default", "essentials.fly");
                if (tiempoRestante > 0) {
                    setTiempoVueloGlobal(tiempoRestante);

                    String titulo = ChatColor.GOLD + "Comandos exclusivos: ";
                    String tiempo = ChatColor.WHITE + getTiempoFormateado(getTiempoVueloGlobal());
                    Utilidades.envitarTitutloTodosBar(titulo + tiempo);

                    if (getTiempoVueloGlobal() == 30 || getTiempoVueloGlobal() == 15 || getTiempoVueloGlobal() <= 5) {
                        Utilidades.enviarTituloATodosPantalla(ChatColor.RED + "ADVERTENCIA", ChatColor.WHITE + "Te quedan " + getTiempoVueloGlobal() + "s de vuelo!");
                    }
                } else {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.setFlying(false);
                    }
                    cancel();
                }
            }
        }.runTaskTimer(UtilidadesServer.getInstance(), 0L, 20L); // 20L representa 1 segundo (20 ticks)
    }

    private static void fuegosArtificialesYsonidos() {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "execute as @a at @s run summon firework_rocket ~ ~1 ~ {LifeTime:30,FireworksItem:{id:firework_rocket,Count:1,tag:{Fireworks:{Flight:2,Explosions:[{Type:1,Flicker:0,Trail:1,Colors:[I;8073150,14188952],FadeColors:[I;12801229]}]}}}}");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "execute as @a at @s run playsound minecraft:entity.player.levelup master @s ~ ~ ~ 100 1");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "execute as @a at @s run playsound minecraft:entity.player.levelup master @s ~ ~ ~ 100 2");
    }

    private static void calcularTiempoAnuncioTipo(int tiempo, String deQue, String solicitante) {
        String adTiempoVuelo;
        // Comprobar que medida de tiempo es para luego mostrarlo en el anuncio
        if (tiempo == 1) { // Segundo
            adTiempoVuelo = tiempo + " SEGUNDO";
        } else if (tiempo < 60) { // Segundos
            adTiempoVuelo = String.format("%s SEGUNDOS", tiempo / 60);
        } else if (tiempo == 60) { // Minuto
            adTiempoVuelo = String.format("%s MINUTO", tiempo / 60);
        } else if (tiempo < 3600) { // Minutos
            adTiempoVuelo = String.format("%s MINUTOS", tiempo / 60);
        } else if (tiempo == 3600) { // Horas
            adTiempoVuelo = String.format("%s HORA", tiempo / 3600);
        } else { // Horas
            adTiempoVuelo = String.format("%s HORAS", tiempo / 3600);
        }

        // Mandar mensaje a todos los jugadores
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(Utilidades.getMensajeColor("&a&lCORTESÍA DE &b" + solicitante + " &r&c&l" + adTiempoVuelo + " DE " + deQue + " PARA TODOS"));
        }
    }

    private static void cargarSpawnRecursos() {
        Path schematicsFolder = Bukkit.getPluginManager().getPlugin("WorldEdit").getDataFolder().toPath().resolve("schematics");

        Clipboard clipboard = null;

        try (ClipboardReader reader = ClipboardFormats.findByFile(schematicsFolder.resolve("recursos.schem").toFile()).getReader(new FileInputStream(schematicsFolder.resolve("recursos.schem").toFile()))) {
            if (reader != null){
                clipboard = reader.read();
            }
        } catch (IOException e) {
            // Manejo de excepciones, por ejemplo, imprimir un mensaje o lanzar una excepción
            e.printStackTrace();
        }
        World mundo = Bukkit.getWorld(RECURSOS);
        assert mundo != null;
        com.sk89q.worldedit.world.World casteo = BukkitAdapter.adapt(mundo); // Castear el mundo de Bukkit a uno de WorldEdit

        try (EditSession editSession = WorldEdit.getInstance().newEditSession(casteo)) {
            assert clipboard != null;
            Operation operation = new ClipboardHolder(clipboard)
                    .createPaste(editSession)
                    .to(BlockVector3.at(1, 200, 1))  // Coordenadas donde deseas pegar el esquema
                    .build();

            Operations.complete(operation);
        } catch (WorldEditException e) {
            // Manejo de excepciones de WorldEdit, por ejemplo, imprimir un mensaje o lanzar una excepción
            e.printStackTrace();
        }

        Location nuevaUbicacion = new Location(Bukkit.getWorld(RECURSOS), 1.4393808628243827, 200.0, 1.4726036739469868, 2.9996347f, 41.858368f);

        worldManager.getMVWorld(RECURSOS).setSpawnLocation(nuevaUbicacion);

        // Obtén el administrador de regiones para el mundo específico
        World mundo_recursos = Bukkit.getWorld(RECURSOS);
        assert mundo_recursos != null;
        RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(mundo_recursos));

        // Crear la región con las coordenadas en específico
        BlockVector3 min = BlockVector3.at(32, 151, -21);
        BlockVector3 max = BlockVector3.at(-47, 225, 58);
        ProtectedRegion region = new ProtectedCuboidRegion("spawn_recursos", min, max);

        regionManager.addRegion(region);

        // Crear un mapa para poner los flags
        Map<Flag<?>, Object> flags = new HashMap<>();
        flags.put(Flags.BUILD, State.DENY);
        flags.put(Flags.PVP, State.DENY);
        flags.put(Flags.USE, State.ALLOW);
        flags.put(Flags.INTERACT, State.ALLOW);
        flags.put(Flags.CREEPER_EXPLOSION, State.DENY);
        flags.put(Flags.BLOCK_BREAK, State.DENY);
        flags.put(Flags.FIRE_SPREAD, State.DENY);
        flags.put(Flags.CHEST_ACCESS, State.DENY);
        flags.put(Flags.DAMAGE_ANIMALS, State.DENY);
        flags.put(Flags.MOB_SPAWNING, State.DENY);
        flags.put(Flags.FALL_DAMAGE, State.DENY);
        flags.put(Flags.INVINCIBILITY, State.ALLOW);
        flags.put(Flags.GRASS_SPREAD, State.DENY);
        flags.put(Flags.ITEM_DROP, State.ALLOW);
        flags.put(Flags.ITEM_PICKUP, State.ALLOW);
        flags.put(Flags.LEAF_DECAY, State.DENY);
        flags.put(Flags.TNT, State.DENY);
        flags.put(Flags.DENY_MESSAGE, State.ALLOW);

        // Añadir flags del mapa creado antes
        region.setFlags(flags);
    }

    public static long getTiempoVueloGlobal() {
        return timepoVueloGlobal;
    }

    public static void setTiempoVueloGlobal(long segundos) {
        timepoVueloGlobal = segundos;
    }

    private void moverJugadoresMundo(World mundo, String destino) {
        Objects.requireNonNull(mundo, "El mundo no puede ser nulo.");

        // Obtén el nombre del mundo
        String worldName = mundo.getName();

        // Obtén el mundo de Bukkit por su nombre
        World bukkitWorld = Bukkit.getWorld(worldName);

        // Asegúrate de que el mundo no sea nulo después de obtenerlo por su nombre
        Objects.requireNonNull(bukkitWorld, "El mundo obtenido no puede ser nulo.");

        // Hacer lo mismo para el mundo destinatario
        World destinoWorld = Bukkit.getWorld(destino);
        Objects.requireNonNull(destinoWorld, "El mundo de destino no puede ser nulo.");

        // Itera sobre los jugadores en ese mundo
        for (Player player : bukkitWorld.getPlayers()) {
            // Teletransporta al jugador al mundo
            core.teleportPlayer(player, player, destinoWorld.getSpawnLocation());
        }
    }

    public void resetearMundo(Player jugador) {
        String[] mundos = {RECURSOS, NETHER, END};

        // Procesamos todos los mundos para que los jugadores que estaban dentro de este
        for (String nombreMundo : mundos) {
            procesarMundo(nombreMundo);
        }

        jugador.sendMessage(Utilidades.getMensajeColor("&cEsto puede ocasionar que el solicitante sea desconectado por un largo tiempo de espera a la respuesta por parte del servidor."));

        // Primero borramos los mundos para crear los otros
        if (worldManager.getMVWorld(Bukkit.getWorld(RECURSOS)) != null) {
            worldManager.deleteWorld(RECURSOS);
        }
        if (worldManager.getMVWorld(Bukkit.getWorld(NETHER)) != null) {
            worldManager.deleteWorld(NETHER);
        }

        if (worldManager.getMVWorld(Bukkit.getWorld(END)) != null) {
            worldManager.deleteWorld(END);
        }

        // Recursos
        worldManager.addWorld(
                RECURSOS,
                World.Environment.NORMAL,
                null,
                WorldType.NORMAL,
                true,
                null
        );

        cargarSpawnRecursos();

        // Nether
        worldManager.addWorld(
                NETHER,
                World.Environment.NETHER,
                null,
                WorldType.NORMAL,
                true,
                null
        );

        // END
        worldManager.addWorld(
                END,
                World.Environment.THE_END,
                null,
                WorldType.NORMAL,
                true,
                null
        );

        // Asignarle alias para que en SternalBoard no aparezca su nombre de referencia en código
        worldManager.getMVWorld(NETHER).setAlias("Nether");
        worldManager.getMVWorld(END).setAlias("End");
    }

    private void procesarMundo(String nombreMundo) {
        World mundo = Bukkit.getWorld(nombreMundo);
        if (mundo != null && !mundo.getPlayers().isEmpty()) {
            moverJugadoresMundo(mundo, LOBBY);

            for (Player player : Bukkit.getWorld(nombreMundo).getPlayers()) {
                player.sendMessage(Utilidades.getMensajeColor("&cSe te ha teletransportado al Lobby porque se están reseteando los mundos."));
            }
        }
    }

    private boolean getResetearMundos() {
        return resetearMundos;
    }

    private static void setResetearMundos(boolean resetearMundos) {
        Comandos.resetearMundos = resetearMundos;
    }

    private void anadirPermisoUsuario(@NotNull User usuario, @NotNull String permission) {
        User usuarioLP = luckPerms.getUserManager().getUser(usuario.toString());
        usuarioLP.data().add(Node.builder(permission).build());
        luckPerms.getUserManager().saveUser(usuarioLP); // Guardar los cambios
    }

    private void anadirPermisoGrupo(@NotNull String grupo, @NotNull String permiso) {
        Group grupoLP = luckPerms.getGroupManager().getGroup(grupo.toString());
        if (grupoLP != null) {
            grupoLP.data().add(Node.builder(permiso).build());
            luckPerms.getGroupManager().saveGroup(grupoLP);
        }
    }

    private static long getTiempoRestantePermisoGrupo(String nombreGrupo, String permiso) {
        // Obtener el grupo por su nombre
        Group grupoLP = luckPerms.getGroupManager().getGroup(nombreGrupo);
        long tiempo = 0;

        if (grupoLP != null) {
            Optional<Node> nodoPermiso = grupoLP.data().toCollection().stream()
                    .filter(node -> node.getKey().startsWith(permiso) && node.hasExpiry())
                    .findFirst();
            if (nodoPermiso.isPresent()) {
                tiempo = nodoPermiso.get().getExpiryDuration().getSeconds();
            }
        }
        return tiempo;
    }

    public static String getTiempoFormateado(long tiempo) {
        String tiempoFormateado;
        long horasCalc = tiempo / 3600;
        long minutosCalc = (tiempo % 3600) / 60;
        long segundosCalc = tiempo % 60;

        if (horasCalc <= 0) {
            tiempoFormateado = String.format("%2dm %2ds", minutosCalc, segundosCalc);
        } else if (minutosCalc <= 0) {
            tiempoFormateado = String.format("%2ds", segundosCalc);
        } else {
            tiempoFormateado = String.format("%2dh %2dm %2ds", horasCalc, minutosCalc, segundosCalc);
        }
        return tiempoFormateado;
    }
}