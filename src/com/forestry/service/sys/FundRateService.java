package com.forestry.service.sys;

import java.util.List;

import com.forestry.model.sys.FundRate;
import core.service.Service;

public interface FundRateService extends Service<FundRate> {

	Long getFundRateCount(FundRate entity);
	
	List<FundRate> getFundRate(FundRate entity);
	
	FundRate getFundRateByCode(String fdcode);
}
