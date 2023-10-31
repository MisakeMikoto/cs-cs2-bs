package com.example.pabs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author MisakiMikoto
 * @Date 2023/10/31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("contacts")
public class Contact {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String name;
    private String email;
    @TableField(value = "phoneNumber")
    private String phoneNumber;
}
