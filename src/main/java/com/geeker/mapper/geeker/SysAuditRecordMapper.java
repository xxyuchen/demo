package com.geeker.mapper.geeker;

import com.geeker.model.SysAuditRecord;

public interface SysAuditRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysAuditRecord record);

    int insertSelective(SysAuditRecord record);

    SysAuditRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysAuditRecord record);

    int updateByPrimaryKey(SysAuditRecord record);
}