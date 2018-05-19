/*
 *  Copyright (C) 2013 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.function.scan.supported;

import com.dove.sample.function.common.Conversions;
import com.dove.sample.wrapper.rws.service.scanner.Capability;

import java.util.List;

public final class MailAddressManualDestinationSettingSupported {
	
	private final MaxLengthSupported supportedMailAddressLength;
	private final List<String> supportedMailToCcBcc;

	static MailAddressManualDestinationSettingSupported getInstance(Capability.MailAddressInfoCapability capability) {
		if (capability == null) {
			return null;
		}
		return new MailAddressManualDestinationSettingSupported(capability);
	}
	
	private MailAddressManualDestinationSettingSupported(Capability.MailAddressInfoCapability capability) {
		supportedMailAddressLength = MaxLengthSupported.getMaxLengthSupported(capability.getMailAddressLength());
		supportedMailToCcBcc = Conversions.getList(capability.getMailToCcBccList());
	}
	
	public MaxLengthSupported getSupportedMailAddressLength() {
		return supportedMailAddressLength;
	}
	
	public List<String> getSupportedMailToCcBcc() {
		return supportedMailToCcBcc;
	}
	
}