package xyz.ivan.demo.service;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.ivan.demo.po.User;
import xyz.ivan.demo.po.UserInfo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    void testSave(){
        User user = new User();
        user.setId(5L);
        user.setUsername("Hermione");
        user.setPassword("123");
        user.setPhone("18688990011");
        user.setBalance(200);
        user.setInfo(UserInfo.of(24, "英文老师", "female"));
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userService.save(user);
    }

    @Test
    void testSaveOneByOne() {
        long b = System.currentTimeMillis();
        for (int i = 1; i <= 100000; i++) {
            userService.save(buildUser(i));
        }
        long e = System.currentTimeMillis();
        System.out.println("耗时：" + (e - b));
    }

    private User buildUser(int i) {
        User user = new User();
        user.setUsername("user_" + i);
        user.setPassword("123");
        user.setPhone("" + (18688190000L + i));
        user.setBalance(2000);
        user.setInfo(UserInfo.of(24, "英文老师", "female"));
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(user.getCreateTime());
        return user;
    }
    @Test
    void testSaveBatch() {
        // 准备10万条数据
        List<User> list = new ArrayList<>(1000);
        long b = System.currentTimeMillis();
        for (int i = 1; i <= 100000; i++) {
            list.add(buildUser(i));
            // 每1000条批量插入一次
            if (i % 1000 == 0) {
                userService.saveBatch(list);
                list.clear();
            }
        }
        long e = System.currentTimeMillis();
        System.out.println("耗时：" + (e - b));
    }


    @Test
    void testPageQuery() {
        // 1.分页查询，new Page()的两个参数分别是：页码、每页大小
        Page<User> userPage = Page.of(1, 2);
        // 按照balance 降序
        userPage.addOrder(new OrderItem("balance",false));
        Page<User> page = userService.page(userPage);
        // 2.总条数
        System.out.println(page.getTotal());
        // 3.总页数
        System.out.println(page.getPages());
        // 4.数据
        page.getRecords().forEach(System.out::println);
    }
}