package com.cjt.tuesday.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cjt.tuesday.dtos.WbsDto;
import com.cjt.tuesday.mapper.WbsMapper;

@Service
public class WbsService {

    @Autowired
    private WbsMapper wbsMapper;

    public List<WbsDto> getWbsByProjectId(Integer projectId) {
        return wbsMapper.findWbsByProjectId(projectId);
    }

    public WbsDto getWbsById(Integer id) {
        return wbsMapper.findWbsById(id);
    }

    public WbsDto addWbs(WbsDto wbs) {
        wbsMapper.insertWbs(wbs);
        return wbs;
    }
    public void updateWbs(WbsDto wbs) {
        wbsMapper.updateWbs(wbs);
    }

    public void deleteWbs(Integer id) {
        wbsMapper.deleteWbs(id);
    }
}
