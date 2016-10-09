package com.forestry.service.sys;

import java.util.List;

import com.forestry.model.sys.RateConfig;

import core.service.Service;

public interface RateConfigService extends Service<RateConfig> {
	Long getAllRateConfigCount();
	List<RateConfig> getAllRateConfig();
}
