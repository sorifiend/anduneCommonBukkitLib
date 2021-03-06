/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (c) 2013 Andune (andune.alleria@gmail.com)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer
 * in the documentation and/or other materials provided with the
 * distribution.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */
/**
 * 
 */
package com.andune.minecraft.commonlib.server.bukkit.events;


import com.andune.minecraft.commonlib.server.api.Location;
import com.andune.minecraft.commonlib.server.bukkit.BukkitFactory;
import com.andune.minecraft.commonlib.server.bukkit.BukkitLocation;


/**
 * @author andune
 *
 */
public class PlayerRespawnEvent extends PlayerEvent
implements com.andune.minecraft.commonlib.server.api.events.PlayerRespawnEvent
{
    private org.bukkit.event.player.PlayerRespawnEvent event;

    public PlayerRespawnEvent(org.bukkit.event.player.PlayerRespawnEvent event, BukkitFactory bukkitFactory) {
        super(event, bukkitFactory);
        this.event = event;
    }

    @Override
    public void setRespawnLocation(Location respawnLocation) {
        event.setRespawnLocation(new BukkitLocation(respawnLocation).getBukkitLocation());
    }

    @Override
    public boolean isBedSpawn() {
        return event.isBedSpawn();
    }

    @Override
    public Location getRespawnLocation() {
        return new BukkitLocation(event.getRespawnLocation());
    }
}
