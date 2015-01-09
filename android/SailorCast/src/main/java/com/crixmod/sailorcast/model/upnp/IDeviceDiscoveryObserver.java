package com.crixmod.sailorcast.model.upnp;

public interface IDeviceDiscoveryObserver {

	public void addedDevice(IUpnpDevice device);

	public void removedDevice(IUpnpDevice device);
}
