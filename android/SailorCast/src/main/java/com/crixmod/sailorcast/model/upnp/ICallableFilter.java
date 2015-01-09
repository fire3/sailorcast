package com.crixmod.sailorcast.model.upnp;

import java.util.concurrent.Callable;

public interface ICallableFilter extends Callable<Boolean> {
	public void setDevice(IUpnpDevice device);
}
