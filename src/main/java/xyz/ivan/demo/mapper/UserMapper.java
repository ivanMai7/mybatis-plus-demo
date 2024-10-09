package xyz.ivan.demo.mapper;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import xyz.ivan.demo.po.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    void updateBalanceByIds(@Param("ew") LambdaUpdateWrapper<User> in,@Param("amount") int amount);

    @Update("UPDATE user SET `balance` = `balance` - #{money} WHERE `id` = #{id}")
    void deductBalance(Long id, Integer money);
}
