package vip.mcsj.www;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import vip.mcsj.www.data.SmokeData;
import vip.mcsj.www.gui.SmokeGui;
import static vip.mcsj.www.Smoke.perms;

public class SmokeCommandExecutor implements org.bukkit.command.CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length != 0) {
            try {
                switch (strings[0]) {
                    case "give":
                        if(perms.has(commandSender,"smoke.use.give")) {
                            try {
                                SmokeData.giveItemToOp(Bukkit.getPlayerExact(strings[1]), strings[2]);
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                                commandSender.sendMessage("§c此香烟名不存在");
                            }
                            return true;
                        }
                    case "add":
                        if(perms.has(commandSender,"smoke.use.add")){
                        }
                        return true;
                    case "delete":
                        if(perms.has(commandSender,"smoke.use.delete")){
                        }
                        return true;
                    case "open":
                        if(perms.has(commandSender,"smoke.use.open")) {
                            if (commandSender instanceof Player) {
                                new SmokeGui((Player) commandSender, 36).createANInventory();
                            } else {
                                commandSender.sendMessage("此命令必须由玩家执行");
                            }
                        }
                        return true;
                    case "getSmokeList":
                        if(perms.has(commandSender,"smoke.use.getsmokelist")) {
                            SmokeData.getSmokelist();
                        }
                        return true;
                }
            }catch (Exception e){
                e.printStackTrace();
                commandSender.sendMessage("参数错误");
            }
        }
        //能到这里说明命令压根没起效
        commandSender.sendMessage("参数错误");
        return false;


//            if (Bukkit.getPlayerExact(strings[1]) == null) {
//                commandSender.sendMessage("此玩家不在线或不存在");
//                return false;
//            } else {
//                SmokeTool.giveItemToOp(Bukkit.getPlayerExact(strings[1]), strings[0]);
//                return true;
//            }
    }
}
