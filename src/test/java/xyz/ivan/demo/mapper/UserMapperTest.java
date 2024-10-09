package xyz.ivan.demo.mapper;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.ivan.demo.po.User;
import xyz.ivan.demo.po.UserInfo;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void testInsert() {
        User user = new User();
        user.setId(5L);
        user.setUsername("Lucy");
        user.setPassword("123");
        user.setPhone("18688990011");
        user.setBalance(200);
        user.setInfo(UserInfo.of(24, "英文老师", "female"));
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userMapper.insert(user);
    }

    @Test
    void testSelectById() {
        User user = userMapper.selectById(5L);
        System.out.println("user = " + user);
    }


    @Test
    void testQueryByIds() {
        List<User> users = userMapper.selectBatchIds(List.of(1L, 2L, 3L, 4L));
        users.forEach(System.out::println);
    }

    @Test
    void testUpdateById() {
        User user = new User();
        user.setId(5L);
        user.setBalance(20000);
        userMapper.updateById(user);
    }

    @Test
    void testDeleteUser() {
        userMapper.deleteById(5L);
    }

    @Test
    /**
     * 查询出名字中带o的，存款大于等于1000元的人的id、username、info、balance字段
     */
    void test1() {
        // 使用非lambda表达式
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        QueryWrapper<User> balance = queryWrapper.ge("balance", 1000);
        QueryWrapper<User> in = balance.like("username", "o");
        userMapper.selectList(in.select("id", "username")).forEach(System.out::println);
        // 使用lambda表达式的形式
        userMapper.selectList(new LambdaQueryWrapper<User>().like(User::getUsername, "o")
                        .ge(User::getBalance, 1000)
                        .select(User::getId, User::getUsername))
                .forEach(System.out::println);
    }


    @Test
    /**
     *更新用户名为jack的用户的余额为2000
     */
    void test2() {
        System.out.println(userMapper.update(null, new LambdaUpdateWrapper<User>().eq(User::getUsername, "jack").set(User::getBalance, 2000)));
    }


    @Test
    /**
     *需求：更新id为1,2,4的用户的余额，扣200
     */
    void test3() {
        userMapper.update(null, new LambdaUpdateWrapper<User>()
                .in(User::getId,1L, 2L, 4L)
                .setSql("balance = balance - 200")
        );
    }

    @Test
    /**
     *需求：更新id为1,2,4的用户的余额，扣200,同test3，不过使用自定义SQL的形式
     */
    void test4(){
        int amount = 200;
        userMapper.updateBalanceByIds(new LambdaUpdateWrapper<User>().in(User::getId, List.of(1L, 2L, 4L)), amount);
    }

}
