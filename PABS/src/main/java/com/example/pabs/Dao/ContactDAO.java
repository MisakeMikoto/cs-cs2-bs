package com.example.pabs.Dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pabs.entity.Contact;
import org.apache.ibatis.annotations.Mapper;


/**
 * @Author MisakiMikoto
 * @Date 2023/10/31
 */
@Mapper
public interface ContactDAO extends BaseMapper<Contact> {
}
