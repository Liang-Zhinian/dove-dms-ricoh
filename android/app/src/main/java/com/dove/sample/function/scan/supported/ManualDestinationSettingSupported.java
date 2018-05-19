/*
 *  Copyright (C) 2013-2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.function.scan.supported;

import com.dove.sample.function.common.Conversions;
import com.dove.sample.wrapper.rws.service.scanner.Capability;

import java.util.List;

public final class ManualDestinationSettingSupported {
	
	private List<String> supportedDestinationKinds;
	private MailAddressManualDestinationSettingSupported mailAddress;
	private SmbAddressManualDestinationSettingSupported smbAddress;
	private FtpAddressManualDestinationSettingSupported ftpAddress;
	private NcpAddressManualDestinationSettingSupported ncpAddress;
	private List<Boolean> supportedUseLoginUserAuthInfos;  //SmartSDK V2.12
	
	static ManualDestinationSettingSupported getInstance(Capability.ManualDestinationSettingCapability capability) {
		if (capability == null) {
			return null;
		}
		return new ManualDestinationSettingSupported(capability);
	}
	
	private ManualDestinationSettingSupported(Capability.ManualDestinationSettingCapability capability) {
		supportedDestinationKinds = Conversions.getList(capability.getDestinationKindObject());
		mailAddress = MailAddressManualDestinationSettingSupported.getInstance(capability.getMailAddressInfoCapability());
		smbAddress = SmbAddressManualDestinationSettingSupported.getInstance(capability.getSmbAddressInfoCapability());
		ftpAddress = FtpAddressManualDestinationSettingSupported.getInstance(capability.getFtpAddressInfoCapability());
		ncpAddress = NcpAddressManualDestinationSettingSupported.getInstance(capability.getNcpAddressInfoCapability());
		supportedUseLoginUserAuthInfos = Conversions.getList(capability.getUseLoginUserAuthInfoList());
	}
	
	public List<String> getSupportedDestinationKinds() {
		return supportedDestinationKinds;
	}
	
	public MailAddressManualDestinationSettingSupported getMailAddress() {
		return mailAddress;
	}
	
	public SmbAddressManualDestinationSettingSupported getSmbAddress() {
		return smbAddress;
	}
	
	public FtpAddressManualDestinationSettingSupported getFtpAddress() {
		return ftpAddress;
	}
	
	public NcpAddressManualDestinationSettingSupported getNcpAddress() {
		return ncpAddress;
	}
	
	/**
	 * @since SmartSDK V2.12
	 */
	public List<Boolean> getSupportedUseLoginUserAuthInfos(){
		return supportedUseLoginUserAuthInfos;
	}
}
