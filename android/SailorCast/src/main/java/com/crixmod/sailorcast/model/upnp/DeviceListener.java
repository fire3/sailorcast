package com.crixmod.sailorcast.model.upnp;


public class DeviceListener {

	// UPNP device listener
	private RendererDiscovery rendererDiscovery = null;

	public DeviceListener(IServiceListener serviceListener)
	{
		rendererDiscovery = new RendererDiscovery(serviceListener);
	}

	public RendererDiscovery getRendererDiscovery()
	{
		return rendererDiscovery;
	}

}
