package com.powerbridge.bssp.sas.service.impl;

import com.powerbridge.bssp.sas.entity.SasVehicleIReg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.powerbridge.bssp.base.service.impl.BaseServiceImpl;
import com.powerbridge.bssp.sas.dao.SasVehicleIRegMapper;
import com.powerbridge.bssp.sas.service.ISasVehicleIRegService;

@Service("sasVehicleIRegService")
@Transactional(readOnly=true)
public class SasVehicleIRegServiceImpl extends BaseServiceImpl<SasVehicleIRegMapper, SasVehicleIReg> implements ISasVehicleIRegService {

	@Autowired
	private SasVehicleIRegMapper sasVehicleIRegMapper;

}
  
    