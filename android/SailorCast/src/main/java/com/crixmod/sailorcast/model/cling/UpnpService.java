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

import android.content.Intent;
import android.util.Log;

import org.fourthline.cling.android.AndroidUpnpServiceConfiguration;
import org.fourthline.cling.android.AndroidUpnpServiceImpl;

public class UpnpService extends AndroidUpnpServiceImpl {

	@Override
	protected AndroidUpnpServiceConfiguration createConfiguration()
	{
		return new AndroidUpnpServiceConfiguration() {

			@Override
			public int getRegistryMaintenanceIntervalMillis()
			{
				return 7000;
			}

		};
	}

	@Override
	public boolean onUnbind(Intent intent)
	{
		Log.d(this.getClass().getName(), "Unbind");
		return super.onUnbind(intent);
	}
}
