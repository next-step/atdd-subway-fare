package atdd.user.application;

import atdd.security.JwtTokenProvider;
import atdd.user.application.dao.UserDao;
import atdd.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User createUser(User user) {
        user.encryptPassword();

        return userDao.save(user);
    }

    public void deleteUser(final long id) {
        userDao.deleteById(id);
    }

    public String login(final String email, final String password) {
        User findUser = userDao.findByEmail(email);

        findUser.checkPassword(password);

        return jwtTokenProvider.createToken(findUser.getEmail());
    }

    public User myInfo(final String email) {
        return userDao.findByEmail(email);
    }

    public User findUserByEmail(final String email) {
        return userDao.findByEmail(email);
    }
}

