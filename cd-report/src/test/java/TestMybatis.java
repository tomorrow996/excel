import com.cd.tech.report.dao.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by zc on 2017/4/19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring.xml"})
public class TestMybatis {
    @Resource
    UserMapper userMapper;

    @Test
    public void testAdd() {
//        User user = new User("aaa", "2548795@163.com", new BigDecimal(11000));
//        userMapper.insertUser(user);
    }

    @Test
    public void testImport() throws Exception {


    }
}
