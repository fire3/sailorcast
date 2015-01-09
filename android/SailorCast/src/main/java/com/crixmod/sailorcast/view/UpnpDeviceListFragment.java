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

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.crixmod.sailorcast.R;
import com.crixmod.sailorcast.model.upnp.IDeviceDiscoveryObserver;
import com.crixmod.sailorcast.model.upnp.IUpnpDevice;

public abstract class UpnpDeviceListFragment extends ListFragment implements IDeviceDiscoveryObserver {

	protected static final String TAG = "UpnpDeviceListFragment";

	protected ArrayAdapter<DeviceDisplay> list;

	private final boolean extendedInformation;

	public UpnpDeviceListFragment()
	{
		this(false);
	}

	public UpnpDeviceListFragment(boolean extendedInformation)
	{
		this.extendedInformation = extendedInformation;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		list = new ArrayAdapter<DeviceDisplay>(this.getView().getContext(), R.layout.device_list_item);
		setListAdapter(list);

		Log.d(TAG, "Activity created");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.sub_device_fragment, container, false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreated");
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		this.getListView().setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
			{
				showInfoDialog(position);
				return true;
			}
		});
	}

	private void showInfoDialog(int position)
	{
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		DialogFragment newFragment = DeviceInfoDialog.newInstance(list.getItem(position));
		//newFragment.show(ft, "dialog");
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		Log.d(TAG, "onDestroy");
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id)
	{
		super.onListItemClick(l, v, position, id);
	}

	@Override
	public void addedDevice(IUpnpDevice device)
	{
		Log.v(TAG, "New device detected : " + device.getDisplayString());

		final DeviceDisplay d = new DeviceDisplay(device, extendedInformation);

		if (getActivity() != null) // Visible
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run()
				{
					try {
						int position = list.getPosition(d);
						if (position >= 0)
						{
							// Device already in the list, re-set new value at same position
							list.remove(d);
							list.insert(d, position);
						}
						else
						{
							list.add(d);
						}
						if (isSelected(d.getDevice()))
						{
							position = list.getPosition(d);
							getListView().setItemChecked(position, true);

							Log.i(TAG, d.toString() + " is selected at position " + position);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
	}

	@Override
	public void removedDevice(IUpnpDevice device)
	{
		Log.v(TAG, "Device removed : " + device.getFriendlyName());

		final DeviceDisplay d = new DeviceDisplay(device, extendedInformation);

		if (getActivity() != null) // Visible
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run()
				{
					try {
						// Remove device from list
						list.remove(d);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
	}

	/**
	 * Filter to know if device is selected
	 * 
	 * @param d
	 * @return
	 */
	protected abstract boolean isSelected(IUpnpDevice d);

	/**
	 * Select a device
	 * 
	 * @param device
	 */
	protected abstract void select(IUpnpDevice device);

	/**
	 * Select a device
	 * 
	 * @param device
	 * @param force
	 */
	protected abstract void select(IUpnpDevice device, boolean force);
}