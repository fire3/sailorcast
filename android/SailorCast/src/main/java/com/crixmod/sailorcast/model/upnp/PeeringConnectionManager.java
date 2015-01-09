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

package com.crixmod.sailorcast.model.upnp;

import android.widget.RemoteViews;

import org.fourthline.cling.model.ServiceReference;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.support.connectionmanager.AbstractPeeringConnectionManagerService;
import org.fourthline.cling.support.model.ConnectionInfo;
import org.fourthline.cling.support.model.ProtocolInfo;
import org.fourthline.cling.support.model.ProtocolInfos;

public class PeeringConnectionManager extends AbstractPeeringConnectionManagerService {

	PeeringConnectionManager(ProtocolInfos sourceProtocolInfo, ProtocolInfos sinkProtocolInfo) {
		super(sourceProtocolInfo, sinkProtocolInfo);
	}

	@Override
	protected ConnectionInfo createConnection(int connectionID, int peerConnectionId,
	                                          ServiceReference peerConnectionManager,
	                                          ConnectionInfo.Direction direction, ProtocolInfo protocolInfo) throws RemoteViews.ActionException {

		// Create the connection on "this" side with the given ID now...
		ConnectionInfo con = new ConnectionInfo(connectionID, 123, // Logical Rendering Control service ID
				456, // Logical AV Transport service ID
				protocolInfo, peerConnectionManager, peerConnectionId, direction, ConnectionInfo.Status.OK);

		return con;
	}

	@Override
	protected void closeConnection(ConnectionInfo connectionInfo) {
		// Close the connection
	}

	@Override
	protected void peerFailure(ActionInvocation invocation, UpnpResponse operation, String defaultFailureMessage) {
		System.err.println("Error managing connection with peer: " + defaultFailureMessage);
	}
}