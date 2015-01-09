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

package com.crixmod.sailorcast.view;

import com.crixmod.sailorcast.model.upnp.IUpnpDevice;

public class DeviceDisplay {

	private final IUpnpDevice device;
	private final boolean extendedInformation;

	public DeviceDisplay(IUpnpDevice device, boolean extendedInformation)
	{
		this.device = device;
		this.extendedInformation = extendedInformation;
	}

	public DeviceDisplay(IUpnpDevice device)
	{
		this(device, false);
	}

	public IUpnpDevice getDevice()
	{
		return device;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		DeviceDisplay that = (DeviceDisplay) o;
		return device.equals(that.device);
	}

	@Override
	public int hashCode()
	{
		if (device == null)
			return 0;

		return device.hashCode();
	}

	@Override
	public String toString()
	{
		if (device == null)
			return "";

		String name = getDevice().getFriendlyName();

		if (extendedInformation)
			name += getDevice().getExtendedInformation();

		return name;
	}
}