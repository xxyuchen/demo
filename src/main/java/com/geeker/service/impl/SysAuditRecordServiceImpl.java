package com.geeker.service.impl;

import com.geeker.mapper.geeker.SysAuditRecordMapper;
import com.geeker.model.SysAuditRecord;
import com.geeker.service.SysAuditRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2018/4/13 0013.
 */
@Service
public class SysAuditRecordServiceImpl implements SysAuditRecordService {
    @Resource
    private SysAuditRecordMapper sysAuditRecordMapper;

    @Override
    public int insert(SysAuditRecord sysAuditRecord) {
        return sysAuditRecordMapper.insert(sysAuditRecord);
    }
}
