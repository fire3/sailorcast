/**
 * Copyright (C) 2013 Aurélien Chabot <aurelien@chabot.fr>
 * 
 * This file is part of DroidUPNP.
 * 
 * DroidUPNP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DroidUPNP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with DroidUPNP.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.crixmod.sailorcast.model.upnp;

import android.content.ServiceConnection;

import java.util.Collection;

public interface IServiceListener {

	public void addListener(IRegistryListener registryListener);

	public void removeListener(IRegistryListener registryListener);

	public void clearListener();

	public void refresh();

	public Collection<IUpnpDevice> getDeviceList();

	public Collection<IUpnpDevice> getFilteredDeviceList(ICallableFilter filter);

	public ServiceConnection getServiceConnexion();
}
