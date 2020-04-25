import com.example.cfg.Configuration;
import com.example.dao.IUserMapper;
import com.example.entity.User;
import com.example.io.Resources;
import com.example.session.SqlSession;
import com.example.session.SqlSessionFactory;
import com.example.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MybatisTest {
    public static void main(String[] args) {
        InputStream in = Resources.getResourceAsStream("mybatis-config.xml");
        System.out.println(in);
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        SqlSessionFactory builder = sqlSessionFactoryBuilder.builder(in);
        SqlSession sqlSession = builder.openSession();
        IUserMapper mapper = sqlSession.getMapper(IUserMapper.class);
        List<User> userList = mapper.findAll();
        System.out.println(userList);

        sqlSession.close();
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
