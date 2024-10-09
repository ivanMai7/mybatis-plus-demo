package xyz.ivan.demo.service.impl;

import xyz.ivan.demo.po.Address;
import xyz.ivan.demo.mapper.AddressMapper;
import xyz.ivan.demo.service.IAddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ivan
 * @since 2024-10-08
 */
@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements IAddressService {

}
