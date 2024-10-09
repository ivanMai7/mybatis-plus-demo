package xyz.ivan.demo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.ivan.demo.enums.UserStatus;
import xyz.ivan.demo.mapper.UserMapper;
import xyz.ivan.demo.po.Address;
import xyz.ivan.demo.po.User;
import xyz.ivan.demo.service.UserService;
import xyz.ivan.demo.vo.AddressVO;
import xyz.ivan.demo.vo.UserVO;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    @Transactional
    public void deductBalance(Long id, Integer money) {
        // 1、根据id查询用户
        User user = this.getById(id);
        // 2、 判断用户状态
        if(user == null || user.getStatus() == UserStatus.FROZEN){
            throw new RuntimeException("用户不存在或被冻结");
        }
        // 3、判断余额是否充足
        if(user.getBalance() < money){
            throw new RuntimeException("用户余额不足");
        }
        Integer remain = user.getBalance() - money;
        // 4、 扣减余额，如果扣减完的余额为0，则冻结账户
        this.lambdaUpdate()
                .set(User::getBalance, remain)
                .set(remain == 0, User::getStatus, UserStatus.FROZEN)
                .eq(User::getId, id)
                .eq(User::getBalance, user.getBalance()) // 乐观锁 compare and set
                .update();
    }

    @Override
    public List<User> queryUsers(String name, Integer status, Integer maxBalance, Integer minBalance) {
        return this.lambdaQuery()
                .like(name != null, User::getUsername, name)
                .eq(status != null, User::getStatus, status)
                .le(maxBalance != null, User::getBalance, maxBalance)
                .ge(minBalance != null, User::getBalance, minBalance)
                .list();
    }

    @Override
    public UserVO getUserAndAddressById(Long id) {
        User user = this.getById(id);
        if(user == null || user.getStatus() == UserStatus.FROZEN){
            throw new RuntimeException("用户不存在或用户状态异常");
        }
        List<Address> addresses = Db.lambdaQuery(Address.class)
                .eq(Address::getUserId, id)
                .list();
        UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
        userVO.setAddressVOs(BeanUtil.copyToList(addresses, AddressVO.class));
        return userVO;
    }

    @Override
    public List<UserVO> getUsersAndAddressesByIds(List<Long> ids) {
        List<User> users = this.listByIds(ids);
        List<Address> addresses = Db.lambdaQuery(Address.class)
                .in(Address::getUserId, ids).list();
        Map<Long, List<Address>> map = addresses.stream().collect(Collectors.groupingBy(Address::getUserId));
        List<UserVO> userVOS = BeanUtil.copyToList(users, UserVO.class);
        for (UserVO userVo : userVOS) {
            List<Address> userAddress = map.get(userVo.getId());
            List<AddressVO> addressVOS = BeanUtil.copyToList(userAddress, AddressVO.class);
            userVo.setAddressVOs(addressVOS);
        }
        return userVOS;
    }
}
