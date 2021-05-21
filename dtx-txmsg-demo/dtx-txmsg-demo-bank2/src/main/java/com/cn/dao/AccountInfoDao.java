package com.cn.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: helisen
 * @create: 2021-05-21 10:13
 **/
@Mapper
@Component
public interface AccountInfoDao {

    @Update("update account_info set account_balance = account_balance + #{amount} where account_no = #{accountNo}")
    int updateAccountBalance(@Param("accountNo")String accountNo, @Param("amount")Double amount);

    @Select("select count(1) from de_duplication where tx_no = #{txNo}")
    int isExistTx(String txNo);

    @Select("insert into de_duplication values(#{txNo}, now());")
    int addTx(String txNo);
}
