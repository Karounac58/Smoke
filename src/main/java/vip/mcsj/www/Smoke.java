package vip.mcsj.www;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import vip.mcsj.www.data.SmokeData;

import java.util.logging.Logger;


public class Smoke extends JavaPlugin {
    public static JavaPlugin instance;
    private static final Logger log = Logger.getLogger("Minecraft");
    public static Economy econ = null;
    public static Permission perms = null;

    public Smoke() {
    }

    public void onEnable() {
        instance = this;
        this.getLogger().info("Smoke插件启动中");
        try {
            Bukkit.getPluginCommand("smoke").setExecutor(new SmokeCommandExecutor());
            Bukkit.getPluginManager().registerEvents(new SmokeEvent(), this);
            saveDefaultConfig();
            SmokeData.getSmokelist();
        } catch (Exception var2) {
            var2.printStackTrace();
            this.getLogger().info("启动失败");
            Bukkit.getPluginManager().disablePlugin(this.instance);
        }

        this.getLogger().info("Smoke插件启动成功！");
        //==================================================以上为自定义的必要操作
        //下面是VaultAPI
        if (!setupEconomy() ) {
            log.severe(String.format("[%s] - 启动失败！找不到Vault依赖。", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        setupPermissions();

    }

    //Eco方法
    private boolean setupEconomy(){
        if(getServer().getPluginManager().getPlugin("Vault") == null){
            return false;
        }

        RegisteredServiceProvider<Economy> rsp =
                getServer().getServicesManager().getRegistration(Economy.class);
        if(rsp == null){
            return false;
        }
        econ = rsp.getProvider();
        return  econ != null;
    }

    //Perms方法
    private boolean setupPermissions(){
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }
}
