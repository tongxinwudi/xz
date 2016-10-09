/**
 * 
 */
package com.forestry.dao.sys;

import java.util.List;

import com.forestry.model.sys.RateConfig;

import core.dao.Dao;

/**
 * @author Steve
 *
 */
public interface RateConfigDao extends Dao<RateConfig> {
	Long getAllRateConfigCount();
	List<RateConfig> getAllRateConfig();
}
