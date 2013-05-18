/**
 * 
 */
package com.andune.minecraft.commonlib.perm;

import java.lang.reflect.Field;

import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;
import org.bukkit.plugin.Plugin;

import com.andune.minecraft.commonlib.Logger;
import com.andune.minecraft.commonlib.LoggerFactory;
import com.sk89q.wepif.PermissionsResolver;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;

/**
 * @author andune
 *
 */
public class Wepif extends BasePermissionChecks implements PermissionInterface {
    private final Logger log = LoggerFactory.getLogger(Wepif.class);
    private PermissionsResolver wepifPerms = null;

    @Override
    public String getSystemName() {
        return "wepif";
    }

    @Override
    public boolean init(Plugin plugin) {
        this.plugin = plugin;
        
        try {
            Plugin worldEdit = plugin.getServer().getPluginManager().getPlugin("WorldEdit");
            String version = null;
            int versionNumber = 840;    // assume compliance unless we find otherwise
            
            try {
                version = worldEdit.getDescription().getVersion();

                // version "4.7" is equivalent to build #379
                if( "4.7".equals(version) )
                    versionNumber = 379;
                // version "5.0" is equivalent to build #670
                else if( "5.0".equals(version) )
                    versionNumber = 670;
                else if( version.startsWith("5.") )     // 5.x series
                    versionNumber = 840;
                else {
                    int index = version.indexOf('-');
                    versionNumber = Integer.parseInt(version.substring(0, index));
                }
            }
            catch(Exception e) {}   // catch any NumberFormatException or anything else
            
//          System.out.println("WorldEdit version: "+version+", number="+versionNumber);
            if( versionNumber < 660 ) {
                log.info("You are currently running version "+version+" of WorldEdit. WEPIF was changed in #660, please update to latest WorldEdit. (skipping WEPIF for permissions)");
                return false;
            }

            if( worldEdit != null ) {
                WorldEditPlugin wep = (WorldEditPlugin) worldEdit;
                wepifPerms = wep.getPermissionsResolver();
//              wepifPerms.initialize(plugin);
//              wepifPerms = new PermissionsResolverManager(this, "LoginLimiter", log);
//              (new PermissionsResolverServerListener(wepifPerms, this)).register(this);
            }
        }
        catch(Exception e) {
            log.info("Unexpected error trying to setup WEPIF permissions hooks (this message can be ignored): "+e.getMessage());
        }
        
        return wepifPerms != null;
    }

    /* (non-Javadoc)
     * @see com.andune.minecraft.commonlib.perm.PermissionInterface#has(org.bukkit.permissions.Permissible, java.lang.String)
     */
    @Override
    public boolean has(Permissible sender, String permission) {
        boolean permAllowed = super.has(sender, permission);
        if( !permAllowed && sender instanceof Player )
            permAllowed = wepifPerms.hasPermission(((Player) sender).getName(), permission);
        log.debug("has() permAllowed={}", permAllowed);
        return permAllowed;
    }

    /* (non-Javadoc)
     * @see com.andune.minecraft.commonlib.perm.PermissionInterface#has(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public boolean has(String world, String player, String permission) {
        return wepifPerms.hasPermission(player, permission) || isOp(player);
    }

    /* (non-Javadoc)
     * @see com.andune.minecraft.commonlib.perm.PermissionInterface#getPlayerGroup(java.lang.String, java.lang.String)
     */
    @Override
    public String getPlayerGroup(String world, String playerName) {
        String[] groups = wepifPerms.getGroups(playerName);
        if( groups != null && groups.length > 0 )
            return groups[0];
        else
            return null;
    }
    
    public String getSystemInUseString() {
        String wepifPermInUse = "";
        try {
            Class<?> clazz = wepifPerms.getClass();
            Field field = clazz.getDeclaredField("permissionResolver");
            field.setAccessible(true);
            Object o = field.get(wepifPerms);
            String className = o.getClass().getSimpleName();
            wepifPermInUse = ":"+className.replace("Resolver", "");
        }
        // catch both normal and runtime exceptions
        catch(Throwable t) {
            // we don't care, it's just extra information if we can get it
        }
        
        return "WEPIF" + wepifPermInUse;
    }
}