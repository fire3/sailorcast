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
import org.fourthline.cling.support.model.item.AudioItem;
import org.fourthline.cling.support.model.item.MusicTrack;

import java.util.List;

public class ClingAudioItem extends ClingDIDLItem
{
	public ClingAudioItem(AudioItem item)
	{
		super(item);
	}

	@Override
	public String getDescription()
	{
		if(item instanceof MusicTrack)
		{
			MusicTrack track = (MusicTrack) item;
			return ( (track.getFirstArtist()!=null && track.getFirstArtist().getName()!=null) ? track.getFirstArtist().getName() : "") +
				((track.getAlbum()!=null) ?  (" - " + track.getAlbum()) : "");
		}
		return ((AudioItem) item).getDescription();
	}

	@Override
	public String getCount()
	{
		List<Res> res = item.getResources();
		if(res!=null && res.size()>0)
			return "" + ((res.get(0).getDuration()!=null) ? res.get(0).getDuration().split("\\.")[0] : "");

		return "";
	}

	@Override
	public int getIcon()
	{
		return R.drawable.ic_action_headphones;
	}
}
