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

package com.crixmod.sailorcast.model.cling;

import com.crixmod.sailorcast.model.upnp.IRegistryListener;
import com.crixmod.sailorcast.model.upnp.IUpnpDevice;
import com.crixmod.sailorcast.model.upnp.IUpnpRegistry;

import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.registry.RegistryImpl;

import java.util.ArrayList;
import java.util.Collection;

@SuppressWarnings("rawtypes")
public class UpnpRegistry implements IUpnpRegistry {

	RegistryImpl clingRegistry;

	@Override
	public Collection<IUpnpDevice> getDevicesList()
	{
		Collection<IUpnpDevice> devices = new ArrayList<IUpnpDevice>();
		for (Device d : clingRegistry.getDevices())
			devices.add(new CDevice(d));

		return devices;
	}

	@Override
	public void addListener(IRegistryListener r)
	{
		clingRegistry.addListener((CRegistryListener) r);
	}

	@Override
	public void removeListener(IRegistryListener r)
	{
		clingRegistry.removeListener((CRegistryListener) r);
	}

}
