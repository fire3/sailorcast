package com.crixmod.sailorcast.view;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.crixmod.sailorcast.R;

public class DeviceInfoDialog extends DialogFragment {

	private String title;
	private String manufacturer;
	private String manufacturer_url;
	private String model_name;
	private String model_url;
	private String model_number;
	private String model_desc;
	private String presentation_url;
	private String desc_xml;

	/**
	 * Create a new instance of MyDialogFragment, providing "num" as an argument.
	 */
	static DeviceInfoDialog newInstance(DeviceDisplay d)
	{
		DeviceInfoDialog f = new DeviceInfoDialog();

		Bundle args = new Bundle();
		args.putString("title", d.toString());

		args.putString("manufacturer", d.getDevice().getManufacturer());
		args.putString("manufacturer_url", d.getDevice().getManufacturerURL());
		args.putString("model_name", d.getDevice().getModelName());
		args.putString("model_url", d.getDevice().getModelURL());
		args.putString("model_number", d.getDevice().getModelNumber());
		args.putString("model_desc", d.getDevice().getModelDesc());
		args.putString("presentation_url", d.getDevice().getPresentationURL());
		args.putString("desc_xml", d.getDevice().getXMLURL());

		f.setArguments(args);

		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		title = getArguments().getString("title");
		manufacturer = getArguments().getString("manufacturer");
		manufacturer_url = getArguments().getString("manufacturer_url");
		model_name = getArguments().getString("model_name");
		model_url = getArguments().getString("model_url");
		model_number = getArguments().getString("model_number");
		model_desc = getArguments().getString("model_desc");
		presentation_url = getArguments().getString("presentation_url");
		desc_xml = getArguments().getString("desc_xml");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.popup_info, container, false);
		((TextView) v.findViewById(R.id.manufacturer)).setText(manufacturer);
		((TextView) v.findViewById(R.id.manufacturer_url)).setText(manufacturer_url);
		((TextView) v.findViewById(R.id.model_name)).setText(model_name);
		((TextView) v.findViewById(R.id.model_url)).setText(model_url);
		((TextView) v.findViewById(R.id.model_number)).setText(model_number);
		((TextView) v.findViewById(R.id.model_desc)).setText(model_desc);
		((TextView) v.findViewById(R.id.presentation_url)).setText(presentation_url);
		((TextView) v.findViewById(R.id.desc_xml)).setText(desc_xml);

		final DeviceInfoDialog deviceInfoDialog = this;

		this.getDialog().setTitle(title);

		// Watch for button clicks.
		Button button = (Button) v.findViewById(R.id.button);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v)
			{
				deviceInfoDialog.dismiss();
			}
		});

		return v;
	}
}