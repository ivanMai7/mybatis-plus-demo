package xyz.ivan.demo.mapper;

import org.apache.ibatis.annotations.Mapper;
import xyz.ivan.demo.po.Address;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ivan
 * @since 2024-10-08
 */
@Mapper
public interface AddressMapper extends BaseMapper<Address> {

}
