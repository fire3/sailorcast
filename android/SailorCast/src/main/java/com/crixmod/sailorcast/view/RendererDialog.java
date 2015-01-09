package com.crixmod.sailorcast.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.ArrayAdapter;

import com.crixmod.sailorcast.SailorCast;
import com.crixmod.sailorcast.R;
import com.crixmod.sailorcast.model.upnp.CallableRendererFilter;
import com.crixmod.sailorcast.model.upnp.IUpnpDevice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;

public class RendererDialog extends DialogFragment {

	private Callable<Void> callback = null;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		final Collection<IUpnpDevice> upnpDevices = SailorCast.upnpServiceController.getServiceListener()
				.getFilteredDeviceList(new CallableRendererFilter());

		ArrayList<DeviceDisplay> list = new ArrayList<DeviceDisplay>();
		for (IUpnpDevice upnpDevice : upnpDevices)
			list.add(new DeviceDisplay(upnpDevice));

		final DialogFragment dialog = this;

		if(list.size()==0)
		{
			builder.setTitle(R.string.selectRenderer)
				.setMessage(R.string.noRenderer)
				.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.dismiss();
					}
				});
		}
		else
		{
			ArrayAdapter<DeviceDisplay> rendererList = new ArrayAdapter<DeviceDisplay>(getActivity(),
				android.R.layout.simple_list_item_1, list);
			builder.setTitle(R.string.selectRenderer).setAdapter(rendererList, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					SailorCast.upnpServiceController.setSelectedRenderer((IUpnpDevice) upnpDevices.toArray()[which]);
					try {
						if (callback != null)
							callback.call();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
		return builder.create();
	}

	public void setCallback(Callable<Void> callback)
	{
		this.callback = callback;
	}
}
