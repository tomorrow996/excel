import dao.UserMapper;
import model.User;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import util.MybatisUtil;

import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;

/**
 * Created by zc on 2017/4/19.
 */
public class TestMybatis {

    static SqlSessionFactory sqlSessionFactory = null;

    static {
        sqlSessionFactory = MybatisUtil.getSqlSessionFactory();
    }

    public static void main(String[] args) {
        testAdd();
        getUser();
    }

    public static void testAdd() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            User user = new User("aaa", "2548795@163.com", new BigDecimal(11000));
            userMapper.insertUser(user);
            sqlSession.commit();// 这里一定要提交，不然数据进不去数据库中
        } finally {
            sqlSession.close();
        }
    }

    public static void getUser() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            User user = userMapper.getUser("aa");
            System.out.println(user.toString());
        } finally {
            sqlSession.close();
        }
    }
}
