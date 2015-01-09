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

package com.crixmod.sailorcast.model.cling.didl;

import com.crixmod.sailorcast.model.upnp.didl.IDIDLObject;

import org.fourthline.cling.support.model.DIDLObject;

public class ClingDIDLObject implements IDIDLObject {

	private static final String TAG = "ClingDIDLObject";

	protected DIDLObject item;

	public ClingDIDLObject(DIDLObject item)
	{
		this.item = item;
	}

	public DIDLObject getObject()
	{
		return item;
	}

	@Override
	public String getTitle()
	{
		return item.getTitle();
	}

	@Override
	public String getDescription()
	{
		return "";
	}

	@Override
	public String getCount()
	{
		return "";
	}

	@Override
	public int getIcon()
	{
		return android.R.color.transparent;
	}

	@Override
	public String getParentID()
	{
		return item.getParentID();
	}

	@Override
	public String getId()
	{
		return item.getId();
	}
}
