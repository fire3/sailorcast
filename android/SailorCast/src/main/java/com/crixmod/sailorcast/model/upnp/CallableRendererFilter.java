package com.crixmod.sailorcast.model.upnp;

public class CallableRendererFilter implements ICallableFilter {

	private IUpnpDevice device;

	public void setDevice(IUpnpDevice device)
	{
		this.device = device;
	}

	@Override
	public Boolean call() throws Exception
	{
		return device.asService("RenderingControl");
	}
}
