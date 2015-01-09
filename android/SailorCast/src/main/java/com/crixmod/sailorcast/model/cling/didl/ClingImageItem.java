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

import com.crixmod.sailorcast.R;

import org.fourthline.cling.support.model.Res;
import org.fourthline.cling.support.model.item.ImageItem;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ClingImageItem extends ClingDIDLItem
{
	public ClingImageItem(ImageItem item)
	{
		super(item);
	}

	@Override
	public String getDescription()
	{
		List<Res> res = item.getResources();
		if(res!=null && res.size()>0)
			return "" + ((res.get(0).getResolution()!=null) ? res.get(0).getResolution() : "");

		return "";
	}

	@Override
	public String getCount()
	{
		try
		{
			SimpleDateFormat formatIn = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
			Date date = formatIn.parse(((ImageItem) item).getDate());
			DateFormat formatOut = DateFormat.getDateTimeInstance();
			return formatOut.format(date);
		}
		catch (Exception e) {}
		return "";
	}

	@Override
	public int getIcon()
	{
		return R.drawable.ic_action_picture;
	}
}